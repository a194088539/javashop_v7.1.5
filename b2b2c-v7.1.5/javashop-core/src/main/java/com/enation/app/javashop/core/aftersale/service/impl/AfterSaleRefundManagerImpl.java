package com.enation.app.javashop.core.aftersale.service.impl;

import com.enation.app.javashop.core.aftersale.AftersaleErrorCode;
import com.enation.app.javashop.core.aftersale.model.dos.AfterSaleRefundDO;
import com.enation.app.javashop.core.aftersale.model.dos.RefundDO;
import com.enation.app.javashop.core.aftersale.model.dto.RefundQueryParam;
import com.enation.app.javashop.core.aftersale.model.enums.*;
import com.enation.app.javashop.core.aftersale.model.vo.ApplyAfterSaleVO;
import com.enation.app.javashop.core.aftersale.model.vo.RefundApplyVO;
import com.enation.app.javashop.core.aftersale.model.vo.RefundRecordVO;
import com.enation.app.javashop.core.aftersale.service.*;
import com.enation.app.javashop.core.base.message.AfterSaleChangeMessage;
import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.payment.model.dos.PaymentMethodDO;
import com.enation.app.javashop.core.payment.service.PaymentMethodManager;
import com.enation.app.javashop.core.payment.service.RefundManager;
import com.enation.app.javashop.core.support.FlowCheckOperate;
import com.enation.app.javashop.core.system.enums.DeleteStatusEnum;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderItemsDO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderTypeEnum;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.util.BeanUtil;
import com.enation.app.javashop.framework.util.CurrencyUtil;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 退款单业务接口实现
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-22
 */
@Service
public class AfterSaleRefundManagerImpl implements AfterSaleRefundManager {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private RefundManager refundManager;

    @Autowired
    private AfterSaleQueryManager afterSaleQueryManager;

    @Autowired
    private AfterSaleManager afterSaleManager;

    @Autowired
    private AfterSaleLogManager afterSaleLogManager;

    @Autowired
    private PaymentMethodManager paymentMethodManager;

    @Autowired
    private AfterSaleDataCheckManager afterSaleDataCheckManager;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public Page list(RefundQueryParam param) {
        StringBuffer sqlBuffer = new StringBuffer("select * from es_refund where disabled = ? ");
        List<Object> term = new ArrayList<Object>();
        term.add(DeleteStatusEnum.NORMAL.value());

        //按会员ID查询
        if (param.getMemberId() != null && param.getMemberId() != 0) {
            sqlBuffer.append(" and member_id = ?");
            term.add(param.getMemberId());
        }

        //按商家ID查询
        if (param.getSellerId() != null && param.getSellerId() != 0) {
            sqlBuffer.append(" and seller_id = ?");
            term.add(param.getSellerId());
        }

        //按关键字查询
        if (StringUtil.notEmpty(param.getKeyword())) {
            sqlBuffer.append(" and (sn like ? or order_sn like ? or goods_json like ?)");
            term.add("%" + param.getKeyword() + "%");
            term.add("%" + param.getKeyword() + "%");
            term.add("%" + param.getKeyword() + "%");
        }

        //按售后服务单号查询
        if (StringUtil.notEmpty(param.getServiceSn())) {
            sqlBuffer.append(" and sn like ?");
            term.add("%" + param.getServiceSn() + "%");
        }

        //按订单编号查询
        if (StringUtil.notEmpty(param.getOrderSn())) {
            sqlBuffer.append(" and order_sn like ?");
            term.add("%" + param.getOrderSn() + "%");
        }

        //按商品名称查询
        if (StringUtil.notEmpty(param.getGoodsName())) {
            sqlBuffer.append(" and goods_json like ?");
            term.add("%" + param.getGoodsName() + "%");
        }

        //按退款单状态查询
        if (StringUtil.notEmpty(param.getRefundStatus())) {
            sqlBuffer.append(" and refund_status = ?");
            term.add(param.getRefundStatus());
        }

        //按退款方式查询
        if (StringUtil.notEmpty(param.getRefundWay())) {
            sqlBuffer.append(" and refund_way = ?");
            term.add(param.getRefundWay());
        }

        //按申请时间-起始时间查询
        if (param.getStartTime() != null && param.getStartTime() != 0) {
            sqlBuffer.append(" and create_time >= ?");
            term.add(param.getStartTime());
        }
        //按申请时间-结束时间查询
        if (param.getEndTime() != null && param.getEndTime() != 0) {
            sqlBuffer.append(" and create_time <= ?");
            term.add(param.getEndTime());
        }

        //按创建渠道查询
        if (StringUtil.notEmpty(param.getCreateChannel())) {
            sqlBuffer.append(" and create_channel = ?");
            term.add(param.getCreateChannel());
        }

        sqlBuffer.append(" order by create_time desc");

        Page page = this.daoSupport.queryForPage(sqlBuffer.toString(), param.getPageNo(), param.getPageSize(), RefundDO.class, term.toArray());

        //转换商品数据
        List<RefundDO> refundDOList = page.getData();
        List<RefundRecordVO> recordVOList = new ArrayList<>();

        for (RefundDO refund : refundDOList) {
            RefundRecordVO recordVO = new RefundRecordVO(refund);
            recordVOList.add(recordVO);
        }

        Page<RefundRecordVO> webPage = new Page(param.getPageNo(), page.getDataTotal(), param.getPageSize(), recordVOList);

        return webPage;
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void adminRefund(String serviceSn, Double refundPrice, String remark, ServiceOperateTypeEnum typeEnum) {

        //参数校验
        this.afterSaleDataCheckManager.checkAdminRefund(serviceSn, refundPrice, remark);

        //获取售后服务单详细信息
        ApplyAfterSaleVO applyAfterSaleVO = this.afterSaleQueryManager.detail(serviceSn);

        //操作权限验证
        if (!FlowCheckOperate.checkOperate(applyAfterSaleVO.getServiceType(), applyAfterSaleVO.getServiceStatus(), typeEnum.value())) {
            throw new ServiceException(AftersaleErrorCode.E601.name(), "当前售后服务单状态不允许进行退款操作");
        }

        RefundDO refund = this.getModel(serviceSn);
        if (refund == null) {
            throw new ServiceException(AftersaleErrorCode.E603.name(), "售后退款单信息不存在");
        }

        //获取退款时间
        long refundTime = DateUtil.getDateline();

        //将退款单状态改为已完成
        String sql = "update es_refund set refund_status = ?,refund_time = ?,actual_price = ? where sn = ?";
        this.daoSupport.execute(sql, RefundStatusEnum.COMPLETED.value(), refundTime, refundPrice, serviceSn);

        //修改售后服务退款相关信息
        sql = "update es_as_refund set actual_price = ?,refund_time = ? where service_sn = ?";
        this.daoSupport.execute(sql, refundPrice, refundTime, serviceSn);

        //将售后服务单状态和退款备注
        this.afterSaleManager.updateServiceStatus(serviceSn, ServiceStatusEnum.COMPLETED.value(), null, null, remark, null);

        //发送售后服务完成消息
        AfterSaleChangeMessage changeMessage = new AfterSaleChangeMessage(serviceSn, ServiceTypeEnum.valueOf(applyAfterSaleVO.getServiceType()), ServiceStatusEnum.COMPLETED);
        amqpTemplate.convertAndSend(AmqpExchange.AS_STATUS_CHANGE, AmqpExchange.AS_STATUS_CHANGE + "_QUEUE", changeMessage);

        //新增退款操作日志
        String log = "已成功将退款退还给买家，当前售后服务已完成。";
        this.afterSaleLogManager.add(serviceSn, log, "系统");
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void sellerRefund(String serviceSn) {
        //售后服务单号不允许为空
        if (StringUtil.isEmpty(serviceSn)) {
            throw new ServiceException(AftersaleErrorCode.E618.name(), "售后服务单号不能为空");
        }

        //获取售后服务单详细信息
        ApplyAfterSaleVO applyAfterSaleVO = this.afterSaleQueryManager.detail(serviceSn);

        Seller seller = UserContext.getSeller();
        if (seller == null || seller.getSellerId().intValue() != applyAfterSaleVO.getSellerId().intValue()) {
            throw new ServiceException(AftersaleErrorCode.E614.name(), "售后服务单信息不存在");
        }

        //获取售后服务单状态
        String serviceStatus = applyAfterSaleVO.getServiceStatus();

        //操作权限验证
        if (!FlowCheckOperate.checkOperate(applyAfterSaleVO.getServiceType(), serviceStatus, ServiceOperateTypeEnum.SELLER_REFUND.value())) {
            throw new ServiceException(AftersaleErrorCode.E601.name(), "当前售后服务单状态不允许进行商家退款操作");
        }

        //获取退款单相关信息
        RefundDO refund = this.getModel(serviceSn);

        if (refund == null) {
            throw new ServiceException(AftersaleErrorCode.E606.name(), "退款单信息不存在");
        }


        //日志详细
        String log = "";

        //如果支持原路退回，则商品入库后直接原路退款
        if (refund.getRefundWay().equals(RefundWayEnum.ORIGINAL.value())) {

            //原路退款操作
            Map map = refundManager.originRefund(refund.getPayOrderNo(), refund.getSn(), refund.getAgreePrice());

            //如果原路退款成功
            if ("true".equals(map.get("result").toString())) {

                serviceStatus = ServiceStatusEnum.COMPLETED.value();

                //获取退款时间
                long refundTime = DateUtil.getDateline();

                //如果原路退款操作成功，就将退款单状态修改为已完成并修改退款日期以及实际退款字段值
                String sql = "update es_refund set refund_status = ?,refund_time = ?,actual_price = ? where sn = ?";
                this.daoSupport.execute(sql, serviceStatus, refundTime, refund.getAgreePrice(), serviceSn);

                //修改售后服务退款相关信息
                sql = "update es_as_refund set actual_price = ?,refund_time = ? where service_sn = ?";
                this.daoSupport.execute(sql, refund.getAgreePrice(), refundTime, serviceSn);

                //新增售后操作日志
                log = "售后服务已退款成功，当前售后服务已完成，如有疑问请及时联系商家或平台。";

            } else {
                serviceStatus = ServiceStatusEnum.WAIT_FOR_MANUAL.value();

                //如果原路退款操作失败，就将退款单状态修改为退款中，等待平台线下退款
                String sql = "update es_refund set refund_status = ? where sn = ?";
                this.daoSupport.execute(sql, RefundStatusEnum.REFUNDING.value(), serviceSn);

                log = "售后服务原路退款操作失败，需要商家或平台进行人工退款处理。";
            }

        } else {
            serviceStatus = ServiceStatusEnum.WAIT_FOR_MANUAL.value();

            //如果退款方式是线下支付，就将退款单状态修改为退款中，等待平台线下退款
            String sql = "update es_refund set refund_status = ? where sn = ?";
            this.daoSupport.execute(sql, RefundStatusEnum.REFUNDING.value(), serviceSn);

            log = "商家已同意退款，等待平台进行人工退款处理。";
        }

        //修改售后服务单状态
        this.afterSaleManager.updateServiceStatus(serviceSn, serviceStatus);

        //新增售后日志
        this.afterSaleLogManager.add(serviceSn, log, seller.getUsername());

        //发送售后服务商家退款消息
        AfterSaleChangeMessage afterSaleChangeMessage = new AfterSaleChangeMessage(serviceSn, ServiceTypeEnum.valueOf(applyAfterSaleVO.getServiceType()), ServiceStatusEnum.valueOf(serviceStatus));
        amqpTemplate.convertAndSend(AmqpExchange.AS_STATUS_CHANGE, AmqpExchange.AS_STATUS_CHANGE + "_QUEUE", afterSaleChangeMessage);
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public RefundDO fillRefund(Double refundPrice, ApplyAfterSaleVO applyAfterSaleVO) {
        RefundDO refundDO = new RefundDO();
        BeanUtil.copyProperties(applyAfterSaleVO, refundDO);
        refundDO.setRefundWay(applyAfterSaleVO.getRefundInfo().getRefundWay());
        refundDO.setAccountType(applyAfterSaleVO.getRefundInfo().getAccountType());
        refundDO.setReturnAccount(applyAfterSaleVO.getRefundInfo().getReturnAccount());
        refundDO.setBankName(applyAfterSaleVO.getRefundInfo().getBankName());
        refundDO.setBankAccountNumber(applyAfterSaleVO.getRefundInfo().getBankAccountNumber());
        refundDO.setBankAccountName(applyAfterSaleVO.getRefundInfo().getBankAccountName());
        refundDO.setBankDepositName(applyAfterSaleVO.getRefundInfo().getBankDepositName());
        refundDO.setRefundPrice(applyAfterSaleVO.getRefundInfo().getRefundPrice());
        refundDO.setAgreePrice(refundPrice);
        refundDO.setRefundStatus(RefundStatusEnum.APPLY.value());
        refundDO.setDisabled(DeleteStatusEnum.NORMAL.value());
        refundDO.setPayOrderNo(applyAfterSaleVO.getRefundInfo().getPayOrderNo());
        Integer id = this.addRefund(refundDO);
        refundDO.setId(id);

        //修改售后服务退款相关信息
        String sql = "update es_as_refund set agree_price = ? where service_sn = ?";
        this.daoSupport.execute(sql, refundPrice, applyAfterSaleVO.getSn());

        return refundDO;
    }

    @Override
    public Integer addRefund(RefundDO refundDO) {
        this.daoSupport.insert(refundDO);
        return this.daoSupport.getLastId("es_refund");
    }

    @Override
    public RefundDO getModel(String serviceSn) {
        return this.daoSupport.queryForObject("select * from es_refund where sn = ?", RefundDO.class, serviceSn);
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void fillAfterSaleRefund(String serviceSn, Integer returnNum, OrderDO orderDO, OrderItemsDO itemsDO, AfterSaleRefundDO refundDO) {
        //填充售后退款账户相关信息
        refundDO.setServiceSn(serviceSn);
        refundDO.setPayOrderNo(orderDO.getPayOrderNo());

        //计算用户申请退货的可退款金额
        double refundPrice = 0.00;
        if (returnNum.intValue() == itemsDO.getNum().intValue()) {
            refundPrice = itemsDO.getRefundPrice();
        } else {
            refundPrice = CurrencyUtil.mul(returnNum, CurrencyUtil.div(itemsDO.getRefundPrice(), itemsDO.getNum(), 4));
        }
        refundDO.setRefundPrice(refundPrice);

        //获取订单的支付方式
        PaymentMethodDO paymentMethodDO = this.paymentMethodManager.getByPluginId(orderDO.getPaymentPluginId());

        //如果订单没有支付方式信息或者支付方式不支持原路退款
        if (paymentMethodDO == null || paymentMethodDO.getIsRetrace() == 0) {

            //校验退款信息
            this.afterSaleDataCheckManager.checkRefundInfo(refundDO);

            //退款方式为账户退款
            refundDO.setRefundWay(RefundWayEnum.ACCOUNT.value());

        } else {
            //如果支付方式支持原路退回，填充退款账号信息
            fillAccountInfo(refundDO, paymentMethodDO.getPluginId());
        }

        //退款相关信息入库
        this.addAfterSaleRefund(refundDO);
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void fillCancelOrderRefund(String serviceSn, OrderDO orderDO, RefundApplyVO refundApplyVO) {
        //填充并校验售后退款相关信息
        AfterSaleRefundDO afterSaleRefundDO = new AfterSaleRefundDO();
        BeanUtil.copyProperties(refundApplyVO, afterSaleRefundDO);
        afterSaleRefundDO.setServiceSn(serviceSn);
        afterSaleRefundDO.setPayOrderNo(orderDO.getPayOrderNo());

        //获取订单的支付方式
        PaymentMethodDO paymentMethodDO = this.paymentMethodManager.getByPluginId(orderDO.getPaymentPluginId());

        //如果订单没有支付方式信息或者支付方式不支持原路退款
        if (paymentMethodDO == null || paymentMethodDO.getIsRetrace() == 0) {

            if (OrderTypeEnum.PINTUAN.name().equals(orderDO.getOrderType())) {
                //退款方式为线下退款
                afterSaleRefundDO.setRefundWay(RefundWayEnum.OFFLINE.value());
            } else {
                //如果是普通订单申请取消需要校验申请参数
                this.afterSaleDataCheckManager.checkRefundInfo(afterSaleRefundDO);

                //退款方式为账户退款
                afterSaleRefundDO.setRefundWay(RefundWayEnum.ACCOUNT.value());
            }

        } else {

            //如果支付方式支持原路退回，填充退款账号信息
            fillAccountInfo(afterSaleRefundDO, paymentMethodDO.getPluginId());
        }

        //退款信息入库
        this.addAfterSaleRefund(afterSaleRefundDO);
    }

    @Override
    public void addAfterSaleRefund(AfterSaleRefundDO refundDO) {
        this.daoSupport.insert(refundDO);
    }

    @Override
    public AfterSaleRefundDO getAfterSaleRefund(String serviceSn) {
        String sql = "select * from es_as_refund where service_sn = ?";
        return this.daoSupport.queryForObject(sql, AfterSaleRefundDO.class, serviceSn);
    }

    /**
     * 填充退款账号信息
     * @param afterSaleRefundDO
     * @param pluginId
     */
    private void fillAccountInfo(AfterSaleRefundDO afterSaleRefundDO, String pluginId) {
        //如果支付方式支持原路退回
        String accountType = null;

        if ("weixinPayPlugin".equals(pluginId)) {
            accountType = AccountTypeEnum.WEIXINPAY.value();
        } else if ("alipayDirectPlugin".equals(pluginId)) {
            accountType = AccountTypeEnum.ALIPAY.value();
        } else if ("unionpayPlugin".equals(pluginId)) {
            accountType = AccountTypeEnum.BANKTRANSFER.value();
        } else if ("chinapayPlugin".equals(pluginId)) {
            accountType = AccountTypeEnum.BANKTRANSFER.value();
        }

        afterSaleRefundDO.setAccountType(accountType);
        //退款方式为原路退回
        afterSaleRefundDO.setRefundWay(RefundWayEnum.ORIGINAL.value());
    }
}
