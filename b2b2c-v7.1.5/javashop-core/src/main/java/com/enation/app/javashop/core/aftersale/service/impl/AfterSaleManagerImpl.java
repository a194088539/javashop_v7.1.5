package com.enation.app.javashop.core.aftersale.service.impl;

import com.enation.app.javashop.core.aftersale.AftersaleErrorCode;
import com.enation.app.javashop.core.aftersale.model.dos.*;
import com.enation.app.javashop.core.aftersale.model.dto.*;
import com.enation.app.javashop.core.aftersale.model.enums.*;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceStatusEnum;
import com.enation.app.javashop.core.aftersale.model.vo.*;
import com.enation.app.javashop.core.aftersale.service.*;
import com.enation.app.javashop.core.base.message.AfterSaleChangeMessage;
import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.payment.service.RefundManager;
import com.enation.app.javashop.core.support.FlowCheckOperate;
import com.enation.app.javashop.core.system.enums.DeleteStatusEnum;
import com.enation.app.javashop.core.trade.cart.model.dos.OrderPermission;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderItemsDO;
import com.enation.app.javashop.core.trade.order.model.enums.*;
import com.enation.app.javashop.core.trade.order.model.vo.CancelVO;
import com.enation.app.javashop.core.trade.order.model.vo.OrderSkuVO;
import com.enation.app.javashop.core.trade.order.service.OrderOperateManager;
import com.enation.app.javashop.core.trade.order.service.OrderQueryManager;
import com.enation.app.javashop.core.trade.order.service.TradeSnCreator;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.util.*;
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
 * 售后服务管理接口实现
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-24
 */
@Service
public class AfterSaleManagerImpl implements AfterSaleManager {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private AfterSaleQueryManager afterSaleQueryManager;

    @Autowired
    private AfterSaleRefundManager afterSaleRefundManager;

    @Autowired
    private AfterSaleGoodsManager afterSaleGoodsManager;

    @Autowired
    private AfterSaleChangeManager afterSaleChangeManager;

    @Autowired
    private AfterSaleGalleryManager afterSaleGalleryManager;

    @Autowired
    private AfterSaleLogManager afterSaleLogManager;

    @Autowired
    private AfterSaleDataCheckManager afterSaleDataCheckManager;

    @Autowired
    private OrderQueryManager orderQueryManager;

    @Autowired
    private OrderOperateManager orderOperateManager;

    @Autowired
    private RefundManager refundManager;

    @Autowired
    private TradeSnCreator tradeSnCreator;

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void apply(ApplyAfterSaleVO applyAfterSaleVO) {

        //校验售后申请参数并获取组成售后服务单的相关数据
        AfterSaleApplyDTO applyDTO = this.afterSaleDataCheckManager.checkApplyService(applyAfterSaleVO);
        //获取当前登录申请售后服务的会员信息
        Buyer buyer = applyDTO.getBuyer();
        //获取申请售后服务的订单信息
        OrderDO order = applyDTO.getOrderDO();
        //获取申请售后服务的商品sku信息
        OrderItemsDO itemsDO = applyDTO.getItemsDO();

        //生成售后服务单号
        String serviceSn = "AS" + tradeSnCreator.generateAfterSaleServiceSn();

        //初始化售后服务单基础信息
        AfterSaleServiceDO afterSaleServiceDO = new AfterSaleServiceDO();
        BeanUtil.copyProperties(applyAfterSaleVO, afterSaleServiceDO);
        afterSaleServiceDO.setServiceStatus(ServiceStatusEnum.APPLY.value());
        afterSaleServiceDO.setCreateChannel(CreateChannelEnum.NORMAL.value());
        //填充申请售后服务信息
        this.fillService(serviceSn, buyer.getUid(), buyer.getUsername(), order, afterSaleServiceDO);

        //填充并入库售后服务的商品信息
        AfterSaleGoodsDO afterSaleGoodsDO = this.afterSaleGoodsManager.fillGoods(serviceSn, applyAfterSaleVO.getReturnNum(), itemsDO);

        //获取售后服务单中的售后商品json信息
        List<AfterSaleGoodsVO> goodsList = new ArrayList<>();
        AfterSaleGoodsVO goodsVO = new AfterSaleGoodsVO();
        BeanUtil.copyProperties(afterSaleGoodsDO, goodsVO);
        goodsList.add(goodsVO);
        afterSaleServiceDO.setGoodsJson(JsonUtil.objectToJson(goodsList));

        //售后服务单基础信息入库
        this.addAfterSaleService(afterSaleServiceDO);

        //填充并入库售后服务收货地址相关信息
        this.afterSaleChangeManager.fillChange(serviceSn, applyAfterSaleVO.getChangeInfo());

        //填充并入库售后图片信息
        this.afterSaleGalleryManager.fillImage(serviceSn, applyAfterSaleVO.getImages());

        //如果用户申请的服务类型是退货或者是取消订单
        if (ServiceTypeEnum.RETURN_GOODS.value().equals(applyAfterSaleVO.getServiceType())) {
            //填充并入库售后服务退款相关信息
            this.afterSaleRefundManager.fillAfterSaleRefund(serviceSn, applyAfterSaleVO.getReturnNum(), order, itemsDO, applyAfterSaleVO.getRefundInfo());
        }

        //申请售后服务成功后，要修改订单的售后服务状态
        this.updateOrderServiceStatus(order.getSn(), order.getItemsJson(), applyAfterSaleVO.getSkuId(), applyAfterSaleVO.getServiceType());

        //发送售后服务申请成功的消息
        AfterSaleChangeMessage afterSaleChangeMessage = new AfterSaleChangeMessage(serviceSn, ServiceTypeEnum.valueOf(applyAfterSaleVO.getServiceType()), ServiceStatusEnum.APPLY);
        amqpTemplate.convertAndSend(AmqpExchange.AS_STATUS_CHANGE, AmqpExchange.AS_STATUS_CHANGE + "_QUEUE", afterSaleChangeMessage);

        //新增售后日志
        this.afterSaleLogManager.add(serviceSn, "售后服务单已申请成功，等待售后审核中。", buyer.getUsername());
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void applyCancelOrder(RefundApplyVO refundApplyVO) {

        //校验取消订单申请参数
        AfterSaleApplyDTO applyDTO = this.afterSaleDataCheckManager.checkCancelOrder(refundApplyVO);
        //获取当前登录申请售后服务的会员信息
        Buyer buyer = applyDTO.getBuyer();
        //获取申请售后服务的订单信息
        OrderDO order = applyDTO.getOrderDO();

        //调用公共方法取消订单
        String serviceSn = this.commonCancelOrder(refundApplyVO, order, buyer.getUid(), buyer.getUsername(), CreateChannelEnum.NORMAL.value());

        //新增售后日志
        this.afterSaleLogManager.add(serviceSn, "售后服务单已申请成功，等待售后审核中。", buyer.getUsername());

        //发送售后服务申请成功的消息
        AfterSaleChangeMessage afterSaleChangeMessage = new AfterSaleChangeMessage(serviceSn, ServiceTypeEnum.ORDER_CANCEL, ServiceStatusEnum.APPLY);
        amqpTemplate.convertAndSend(AmqpExchange.AS_STATUS_CHANGE, AmqpExchange.AS_STATUS_CHANGE + "_QUEUE", afterSaleChangeMessage);
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void applyShip(AfterSaleExpressDO afterSaleExpressDO) {

        //获取当前登录的会员信息
        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E110.code(), "当前会员已经退出登录");
        }

        //校验用户退还售后商品填写的物流信息并返回物流公司名称
        String logiName = this.afterSaleDataCheckManager.checkAfterSaleExpress(afterSaleExpressDO);
        afterSaleExpressDO.setCourierCompany(logiName);

        //根据售后服务单号获取服务单信息
        AfterSaleServiceDO afterSaleServiceDO = this.afterSaleQueryManager.getService(afterSaleExpressDO.getServiceSn());

        if (afterSaleServiceDO == null || afterSaleServiceDO.getMemberId().intValue() != buyer.getUid().intValue()) {
            throw new ServiceException(AftersaleErrorCode.E614.name(), "售后服务单信息不存在");
        }

        //操作权限验证
        if (!FlowCheckOperate.checkOperate(afterSaleServiceDO.getServiceType(), afterSaleServiceDO.getServiceStatus(), ServiceOperateTypeEnum.FILL_LOGISTICS_INFO.value())) {
            throw new ServiceException(AftersaleErrorCode.E601.name(), "当前售后服务单状态不允许进行填写物流信息操作");
        }

        //物流信息入库
        this.daoSupport.insert(afterSaleExpressDO);

        //修改售后服务单状态为填充物流信息
        this.updateServiceStatus(afterSaleExpressDO.getServiceSn(), ServiceStatusEnum.FULL_COURIER.value());

        //新增售后日志
        this.afterSaleLogManager.add(afterSaleExpressDO.getServiceSn(), "申请售后服务的商品已寄出，等待商家收货。", buyer.getUsername());

        //发送会员填充售后服务物流信息成功的消息
        AfterSaleChangeMessage afterSaleChangeMessage = new AfterSaleChangeMessage(afterSaleExpressDO.getServiceSn(), ServiceTypeEnum.valueOf(afterSaleServiceDO.getServiceType()), ServiceStatusEnum.FULL_COURIER);
        amqpTemplate.convertAndSend(AmqpExchange.AS_STATUS_CHANGE, AmqpExchange.AS_STATUS_CHANGE + "_QUEUE", afterSaleChangeMessage);
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void audit(String serviceSn, String auditStatus, Double refundPrice, String returnAddr, String auditRemark) {

        //获取售后服务详细信息
        ApplyAfterSaleVO applyAfterSaleVO = this.afterSaleDataCheckManager.checkAudit(serviceSn, auditStatus, refundPrice, returnAddr, auditRemark);

        //操作权限验证
        if (!FlowCheckOperate.checkOperate(applyAfterSaleVO.getServiceType(), applyAfterSaleVO.getServiceStatus(), ServiceOperateTypeEnum.SELLER_AUDIT.value())) {
            throw new ServiceException(AftersaleErrorCode.E601.name(), "当前售后服务单状态不允许进行审核操作");
        }

        //获取售后服务单类型
        String serviceType = applyAfterSaleVO.getServiceType();

        //审核日志详细
        String logDetail = "售后服务申请已通过商家审核。";

        //如果商家审核通过
        if (ServiceStatusEnum.PASS.value().equals(auditStatus)) {
            //如果审核的售后服务类型为退货或者取消订单，则需要生成退款单
            if (ServiceTypeEnum.RETURN_GOODS.value().equals(serviceType) || ServiceTypeEnum.ORDER_CANCEL.value().equals(serviceType)) {
                //售后服务单审核通过后要生成退款单信息
                this.afterSaleRefundManager.fillRefund(refundPrice, applyAfterSaleVO);
            }

            //如果审核的售后服务类型为退货或者换货，则需要修改售后服务单的退货地址
            if (ServiceTypeEnum.RETURN_GOODS.value().equals(serviceType) || ServiceTypeEnum.CHANGE_GOODS.value().equals(serviceType)) {
                //修改退货地址信息
                this.updateReturnAddr(serviceSn, returnAddr);
            }

        } else {
            logDetail = "售后服务申请未通过商家审核，如有疑问请及时与商家联系。";
        }

        //修改售后服务单状态和审核备注
        this.updateServiceStatus(serviceSn, auditStatus, auditRemark, null, null, null);

        //添加售后操作日志
        this.afterSaleLogManager.add(serviceSn, logDetail, applyAfterSaleVO.getSellerName());

        //发送售后服务审核成功的消息
        AfterSaleChangeMessage afterSaleChangeMessage = new AfterSaleChangeMessage(serviceSn, ServiceTypeEnum.valueOf(serviceType), ServiceStatusEnum.valueOf(auditStatus));
        amqpTemplate.convertAndSend(AmqpExchange.AS_STATUS_CHANGE, AmqpExchange.AS_STATUS_CHANGE + "_QUEUE", afterSaleChangeMessage);
    }


    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void putInWarehouse(String serviceSn, List<PutInWarehouseDTO> storageList, String remark) {
        //参数校验
        this.afterSaleDataCheckManager.checkPutInWarehouse(serviceSn, storageList, remark);

        //获取售后服务详细信息
        ApplyAfterSaleVO applyAfterSaleVO = this.afterSaleQueryManager.detail(serviceSn);

        //操作权限验证
        if (!FlowCheckOperate.checkOperate(applyAfterSaleVO.getServiceType(), applyAfterSaleVO.getServiceStatus(), ServiceOperateTypeEnum.STOCK_IN.value())) {
            throw new ServiceException(AftersaleErrorCode.E601.name(), "当前售后服务单状态不允许进行入库操作");
        }

        //获取售后服务单类型
        String serviceType = applyAfterSaleVO.getServiceType();

        //修改售后服务单状态和商家入库备注
        this.updateServiceStatus(serviceSn, ServiceStatusEnum.STOCK_IN.value(), null, remark, null, null);

        String logDetail = "商家已成功将申请售后服务的商品入库，请等待商家进行退款。";
        if (ServiceTypeEnum.CHANGE_GOODS.value().equals(serviceType)) {
            logDetail = "商家已收到用户退还的售后服务商品并将商品入库。";
        }

        //添加售后操作日志
        this.afterSaleLogManager.add(serviceSn, logDetail, applyAfterSaleVO.getSellerName());

        //修改售后商品的入库数量
        for (PutInWarehouseDTO warehouseDTO : storageList) {
            this.afterSaleGoodsManager.updateStorageNum(serviceSn, warehouseDTO.getSkuId(), warehouseDTO.getStorageNum());
        }

        //发送售后服务商品入库消息
        AfterSaleChangeMessage afterSaleChangeMessage = new AfterSaleChangeMessage(serviceSn, applyAfterSaleVO.getOrderSn(),
                ServiceTypeEnum.valueOf(applyAfterSaleVO.getServiceType()), ServiceStatusEnum.STOCK_IN, storageList);
        amqpTemplate.convertAndSend(AmqpExchange.AS_STATUS_CHANGE, AmqpExchange.AS_STATUS_CHANGE + "_QUEUE", afterSaleChangeMessage);

        //如果售后服务类型为换货，商家收到买家退回的商品并成功入库后，这个服务单就算是结束了，因此修改售后服务单状态为已完成
        if (ServiceTypeEnum.CHANGE_GOODS.value().equals(serviceType)) {
            //修改售后服务单状态为已完成
            this.updateServiceStatus(serviceSn, ServiceStatusEnum.COMPLETED.value());

            //添加售后操作日志
            this.afterSaleLogManager.add(serviceSn, "当前售后服务已完成。", "系统");

            //发送售后服务商品入库消息
            AfterSaleChangeMessage changeMessage = new AfterSaleChangeMessage(serviceSn, ServiceTypeEnum.CHANGE_GOODS, ServiceStatusEnum.COMPLETED);
            amqpTemplate.convertAndSend(AmqpExchange.AS_STATUS_CHANGE, AmqpExchange.AS_STATUS_CHANGE + "_QUEUE", changeMessage);
        }
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void closeAfterSale(String serviceSn, String remark) {
        //参数校验
        this.afterSaleDataCheckManager.checkCloseAfterSale(serviceSn, remark);

        //获取售后服务单详细信息
        ApplyAfterSaleVO applyAfterSaleVO = this.afterSaleQueryManager.detail(serviceSn);

        //获取售后服务类型
        String serviceType = applyAfterSaleVO.getServiceType();

        //操作权限验证
        if (!FlowCheckOperate.checkOperate(serviceType, applyAfterSaleVO.getServiceStatus(), ServiceOperateTypeEnum.CLOSE.value())) {
            throw new ServiceException(AftersaleErrorCode.E601.name(), "当前售后服务单状态不允许进行关闭操作");
        }

        //修改售后服务单状态和关闭原因
        this.updateServiceStatus(serviceSn, ServiceStatusEnum.CLOSED.value(), null, null, null, remark);

        //添加售后操作日志
        this.afterSaleLogManager.add(serviceSn, "当前售后服务单已被商家关闭，关闭原因："+remark+"。", "系统");

        //发送售后服务关闭消息
        AfterSaleChangeMessage afterSaleChangeMessage = new AfterSaleChangeMessage(serviceSn, ServiceTypeEnum.valueOf(serviceType), ServiceStatusEnum.CLOSED);
        amqpTemplate.convertAndSend(AmqpExchange.AS_STATUS_CHANGE, AmqpExchange.AS_STATUS_CHANGE + "_QUEUE", afterSaleChangeMessage);
    }

    @Override
    public void editAfterSaleShopName(Integer shopId, String shopName) {
        String sql = "update es_as_order set seller_name = ? where seller_id = ? ";
        this.daoSupport.execute(sql, shopName, shopId);
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void cancelPintuanOrder(String orderSn, String cancelReason) {

        OrderDO order = this.orderQueryManager.getOrder(orderSn);

        //未付款订单直接调用订单的取消方法，将订单变为已取消
        if (PayStatusEnum.PAY_NO.value().equals(order.getPayStatus())) {
            CancelVO cancelVO = new CancelVO();
            cancelVO.setOrderSn(orderSn);
            cancelVO.setOperator("系统");
            cancelVO.setReason(cancelReason);
            this.orderOperateManager.cancel(cancelVO, OrderPermission.admin);
        } else {
            //如果订单已付款，那么需要走售后退款流程，因此需要自动创建一条售后服务单并自动退款
            RefundApplyVO refundApplyVO = new RefundApplyVO();
            refundApplyVO.setOrderSn(orderSn);
            refundApplyVO.setReason(cancelReason);
            refundApplyVO.setMobile(order.getShipMobile());
            refundApplyVO.setRefundPrice(order.getOrderPrice());

            //调用公共方法取消订单并自动生成售后服务单
            String serviceSn = this.commonCancelOrder(refundApplyVO, order, order.getMemberId(), order.getMemberName(), CreateChannelEnum.PINTUAN.value());

            //创建退款单
            ApplyAfterSaleVO applyAfterSaleVO = this.afterSaleQueryManager.detail(serviceSn);
            RefundDO refund = this.afterSaleRefundManager.fillRefund(order.getOrderPrice(), applyAfterSaleVO);

            String serviceStatus = applyAfterSaleVO.getServiceStatus();
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
                    this.daoSupport.execute(sql, serviceStatus, refundTime, order.getOrderPrice(), serviceSn);

                    //修改售后服务退款相关信息
                    sql = "update es_as_refund set actual_price = ?,refund_time = ? where service_sn = ?";
                    this.daoSupport.execute(sql, order.getOrderPrice(), refundTime, serviceSn);

                    //新增售后操作日志
                    log = "拼团活动结束订单未成团，系统自动取消拼团订单并已成功将退款原路返还。";

                } else {
                    serviceStatus = ServiceStatusEnum.WAIT_FOR_MANUAL.value();

                    //如果原路退款操作失败，就将退款单状态修改为退款中，等待平台线下退款
                    String sql = "update es_refund set refund_status = ? where sn = ?";
                    this.daoSupport.execute(sql, RefundStatusEnum.REFUNDING.value(), serviceSn);

                    log = "拼团活动结束订单未成团，系统自动取消拼团订单，退款原路返还失败，需要平台进行人工退款处理。";
                }

            } else {
                serviceStatus = ServiceStatusEnum.WAIT_FOR_MANUAL.value();

                //如果退款方式是线下支付，就将退款单状态修改为退款中，等待平台线下退款
                String sql = "update es_refund set refund_status = ? where sn = ?";
                this.daoSupport.execute(sql, RefundStatusEnum.REFUNDING.value(), serviceSn);

                log = "拼团活动结束订单未成团，系统自动取消拼团订单，需要平台进行人工退款处理。";
            }

            //修改售后服务单状态
            this.updateServiceStatus(serviceSn, serviceStatus);

            //新增售后日志
            this.afterSaleLogManager.add(serviceSn, log, "系统");

            //发送售后服务完成消息
            if (ServiceStatusEnum.COMPLETED.value().equals(serviceStatus)) {
                AfterSaleChangeMessage afterSaleChangeMessage = new AfterSaleChangeMessage(serviceSn, ServiceTypeEnum.ORDER_CANCEL, ServiceStatusEnum.COMPLETED);
                amqpTemplate.convertAndSend(AmqpExchange.AS_STATUS_CHANGE, AmqpExchange.AS_STATUS_CHANGE + "_QUEUE", afterSaleChangeMessage);
            }

        }
    }

    @Override
    public void addAfterSaleService(AfterSaleServiceDO serviceDO) {
        this.daoSupport.insert(serviceDO);
    }

    @Override
    public void updateServiceStatus(String serviceSn, String serviceStatus) {
        String sql = "update es_as_order set service_status = ? where sn = ?";
        this.daoSupport.execute(sql, serviceStatus, serviceSn);
    }

    @Override
    public void updateServiceStatus(String serviceSn, String serviceStatus, String auditRemark, String storageRemark, String refundRemark, String closeReason) {
        StringBuffer sqlBuffer = new StringBuffer("update es_as_order set service_status = ?");
        List<Object> term = new ArrayList<Object>();
        term.add(serviceStatus);

        //如果审核备注不为空
        if (StringUtil.notEmpty(auditRemark)) {
            sqlBuffer.append(",audit_remark = ?");
            term.add(auditRemark);
        }

        //如果入库备注不为空
        if (StringUtil.notEmpty(storageRemark)) {
            sqlBuffer.append(",stock_remark = ?");
            term.add(storageRemark);
        }

        //如果退款备注不为空
        if (StringUtil.notEmpty(refundRemark)) {
            sqlBuffer.append(",refund_remark = ?");
            term.add(refundRemark);
        }

        //如果关闭原因不为空
        if (StringUtil.notEmpty(closeReason)) {
            sqlBuffer.append(",close_reason = ?");
            term.add(closeReason);
        }

        sqlBuffer.append(" where sn = ?");
        term.add(serviceSn);

        this.daoSupport.execute(sqlBuffer.toString(), term.toArray());
    }

    @Override
    public void setServiceNewOrderSn(String serviceSn, String newOrderSn) {
        String sql = "update es_as_order set service_status = ?,new_order_sn = ? where sn = ?";
        this.daoSupport.execute(sql, ServiceStatusEnum.PASS.value(), newOrderSn, serviceSn);
    }

    /**
     * 取消订单的共用方法
     * @param refundApplyVO 取消订单VO
     * @param order 订单信息
     * @param memberId 会员id
     * @param memberName 会员用户名
     * @param createChannel 售后服务单创建渠道 NORMAL：正常渠道创建，PINTUAN：拼团失败自动创建
     * @return
     */
    private String commonCancelOrder(RefundApplyVO refundApplyVO, OrderDO order, Integer memberId, String memberName, String createChannel) {
        //生成售后单号
        String serviceSn = "AS" + tradeSnCreator.generateAfterSaleServiceSn();

        //填充申请售后服务信息
        AfterSaleServiceDO afterSaleServiceDO = new AfterSaleServiceDO();
        afterSaleServiceDO.setOrderSn(refundApplyVO.getOrderSn());
        afterSaleServiceDO.setReason(refundApplyVO.getReason());
        afterSaleServiceDO.setMobile(refundApplyVO.getMobile());
        afterSaleServiceDO.setServiceStatus(CreateChannelEnum.NORMAL.value().equals(createChannel) ? ServiceStatusEnum.APPLY.value() : ServiceStatusEnum.STOCK_IN.value());
        afterSaleServiceDO.setServiceType(ServiceTypeEnum.ORDER_CANCEL.value());
        afterSaleServiceDO.setCreateChannel(createChannel);
        this.fillService(serviceSn, memberId, memberName, order, afterSaleServiceDO);

        //获取订单项相关信息
        List<OrderItemsDO> itemsDOList = this.orderQueryManager.orderItems(order.getSn());

        List<AfterSaleGoodsVO> goodsList = new ArrayList<>();

        for (OrderItemsDO itemsDO : itemsDOList) {
            //填充售后商品信息并入库
            AfterSaleGoodsDO goodsDO = this.afterSaleGoodsManager.fillGoods(serviceSn, itemsDO.getNum(), itemsDO);
            AfterSaleGoodsVO goodsVO = new AfterSaleGoodsVO();
            BeanUtil.copyProperties(goodsDO, goodsVO);
            goodsList.add(goodsVO);
        }

        //设置售后服务单中的售后商品信息json
        afterSaleServiceDO.setGoodsJson(JsonUtil.objectToJson(goodsList));

        //售后服务单入库
        this.addAfterSaleService(afterSaleServiceDO);

        //填充订单收货地址相关信息
        AfterSaleChangeDO afterSaleChangeDO = new AfterSaleChangeDO(order);
        //填充并入库售后服务收货地址相关信息
        this.afterSaleChangeManager.fillChange(serviceSn, afterSaleChangeDO);

        //填充并入库售后服务退款相关信息
        this.afterSaleRefundManager.fillCancelOrderRefund(serviceSn, order, refundApplyVO);

        //申请取消订单成功后，要修改订单的售后服务状态
        this.updateOrderServiceStatus(order.getSn(), order.getItemsJson(), null, ServiceTypeEnum.ORDER_CANCEL.value());

        return serviceSn;
    }

    /**
     * 填充售后服务单信息
     * @param serviceSn 售后服务单编号
     * @param memberId 会员id
     * @param memberName 会员名称
     * @param order 订单信息
     * @param afterSaleServiceDO 售后服务单信息
     */
    private void fillService(String serviceSn, Integer memberId, String memberName, OrderDO order, AfterSaleServiceDO afterSaleServiceDO) {
        afterSaleServiceDO.setSn(serviceSn);
        afterSaleServiceDO.setMemberId(memberId);
        afterSaleServiceDO.setMemberName(memberName);
        afterSaleServiceDO.setSellerId(order.getSellerId());
        afterSaleServiceDO.setSellerName(order.getSellerName());
        afterSaleServiceDO.setCreateTime(DateUtil.getDateline());
        afterSaleServiceDO.setDisabled(DeleteStatusEnum.NORMAL.value());
    }

    /**
     * 修改订单售后状态
     * @param orderSn 订单编号
     * @param itemJson 订单项json数据
     * @param skuId 商品skuID
     * @param serviceType 售后服务类型
     */
    private void updateOrderServiceStatus(String orderSn, String itemJson, Integer skuId, String serviceType) {

        List<OrderSkuVO> skuList = JsonUtil.jsonToList(itemJson, OrderSkuVO.class);
        List<OrderSkuVO> newSkuList = new ArrayList<>();

        //如果售后服务类型为取消订单，那么需要直接将订单售后服务状态改为已申请；
        //否则，其它售后服务类型不需要修改订单的售后状态，只需修改订单item_json中的售后状态即可
        if (ServiceTypeEnum.ORDER_CANCEL.value().equals(serviceType)) {

            String sql = "update es_order set service_status = ? where sn = ?";
            this.daoSupport.execute(sql, OrderServiceStatusEnum.APPLY.value(), orderSn);

        } else {

            for (OrderSkuVO orderSkuVO : skuList) {
                if (orderSkuVO.getSkuId().intValue() == skuId) {
                    orderSkuVO.setServiceStatus(OrderServiceStatusEnum.APPLY.value());
                }
                newSkuList.add(orderSkuVO);
            }

            String sql = "update es_order set items_json = ? where sn = ?";
            this.daoSupport.execute(sql, JsonUtil.objectToJson(newSkuList), orderSn);
        }
    }

    /**
     * 修改售后服务单的退货地址信息
     * @param serviceSn 售后服务单号
     * @param returnAddr 退货地址信息
     */
    private void updateReturnAddr(String serviceSn, String returnAddr) {
        String sql = "update es_as_order set return_addr = ? where sn = ?";
        this.daoSupport.execute(sql, returnAddr, serviceSn);
    }
}