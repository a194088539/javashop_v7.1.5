package com.enation.app.javashop.core.aftersale.service.impl;

import com.enation.app.javashop.core.aftersale.AftersaleErrorCode;
import com.enation.app.javashop.core.aftersale.model.dos.AfterSaleExpressDO;
import com.enation.app.javashop.core.aftersale.model.dos.AfterSaleRefundDO;
import com.enation.app.javashop.core.aftersale.model.dto.AfterSaleApplyDTO;
import com.enation.app.javashop.core.aftersale.model.dto.PutInWarehouseDTO;
import com.enation.app.javashop.core.aftersale.model.enums.AccountTypeEnum;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceStatusEnum;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceTypeEnum;
import com.enation.app.javashop.core.aftersale.model.vo.ApplyAfterSaleVO;
import com.enation.app.javashop.core.aftersale.model.vo.RefundApplyVO;
import com.enation.app.javashop.core.aftersale.service.AfterSaleDataCheckManager;
import com.enation.app.javashop.core.aftersale.service.AfterSaleQueryManager;
import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.system.model.dos.LogisticsCompanyDO;
import com.enation.app.javashop.core.system.service.LogisticsCompanyManager;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderItemsDO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderServiceStatusEnum;
import com.enation.app.javashop.core.trade.order.model.vo.OrderSkuVO;
import com.enation.app.javashop.core.trade.order.service.OrderQueryManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import com.enation.app.javashop.framework.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 售后服务数据校验接口实现类
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-12-03
 */
@Service
public class AfterSaleDataCheckManagerImpl implements AfterSaleDataCheckManager {

    @Autowired
    private OrderQueryManager orderQueryManager;

    @Autowired
    private LogisticsCompanyManager logisticsCompanyManager;

    @Autowired
    private AfterSaleQueryManager afterSaleQueryManager;

    @Override
    public AfterSaleApplyDTO checkApplyService(ApplyAfterSaleVO applyAfterSaleVO) {

        //获取当前登录的会员信息
        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E110.code(), "当前会员已经退出登录");
        }

        //订单编号不能为空
        if (StringUtil.isEmpty(applyAfterSaleVO.getOrderSn())) {
            throw new ServiceException(AftersaleErrorCode.E604.name(), "订单编号不能为空");
        }

        //获取要申请售后的订单信息
        OrderDO order = this.orderQueryManager.getOrder(applyAfterSaleVO.getOrderSn());

        //不存在的订单或者不属于当前会员的订单进行校验
        if (order == null || !buyer.getUid().equals(order.getMemberId())) {
            throw new ServiceException(AftersaleErrorCode.E604.name(), "申请售后服务的订单信息不存在");
        }

        //初始化订单购买的商品sku信息
        List<OrderSkuVO> skuList = JsonUtil.jsonToList(order.getItemsJson(), OrderSkuVO.class);
        String serviceStatus = OrderServiceStatusEnum.NOT_APPLY.value();

        //循环获取当前申请售后商品的售后状态
        for (OrderSkuVO skuVO : skuList) {
            if (applyAfterSaleVO.getSkuId().intValue() == skuVO.getSkuId().intValue()) {
                serviceStatus = skuVO.getServiceStatus();
            }
        }

        //操作权限校验
        if (!OrderServiceStatusEnum.NOT_APPLY.value().equals(serviceStatus)) {
            throw new ServiceException(AftersaleErrorCode.E601.name(), "当前订单商品不允许进行申请售后服务操作");
        }

        //获取订单中申请售后服务的商品信息
        List<OrderItemsDO> itemsDOList = this.orderQueryManager.orderItems(applyAfterSaleVO.getOrderSn());

        OrderItemsDO orderItemsDO = null;

        for (OrderItemsDO itemsDO : itemsDOList) {
            if (applyAfterSaleVO.getSkuId().intValue() == itemsDO.getProductId().intValue()) {
                orderItemsDO = itemsDO;
            }
        }

        //商品信息不能为空
        if (orderItemsDO == null) {
            throw new ServiceException(AftersaleErrorCode.E604.name(), "申请售后服务的订单商品信息不正确");
        }

        //申请售后服务的商品数量不能为空也不能为0
        if (applyAfterSaleVO.getReturnNum() == null || applyAfterSaleVO.getReturnNum() == 0) {
            throw new ServiceException(AftersaleErrorCode.E607.name(), "申请售后服务的商品数量不能为空也不能为0");
        }

        //退货数量不能大于订单购买的商品数量
        if (applyAfterSaleVO.getReturnNum().intValue() > orderItemsDO.getNum()) {
            throw new ServiceException(AftersaleErrorCode.E607.name(), "申请售后服务的商品数量不能大于订单购买的商品数量");
        }

        //申请售后类型不能为空
        if (StringUtil.isEmpty(applyAfterSaleVO.getServiceType())) {
            throw new ServiceException(AftersaleErrorCode.E610.name(), "申请售后类型不能为空");
        }

        //提交原因不能为空
        if (StringUtil.isEmpty(applyAfterSaleVO.getReason())) {
            throw new ServiceException(AftersaleErrorCode.E612.name(), "提交原因不能为空");
        }

        //问题描述必须填写
        if (StringUtil.isEmpty(applyAfterSaleVO.getProblemDesc())) {
            throw new ServiceException(AftersaleErrorCode.E618.name(), "问题描述不能为空");
        }

        //问题描述不能超过200个字符
        if (applyAfterSaleVO.getProblemDesc().length() > 200) {
            throw new ServiceException(AftersaleErrorCode.E618.name(), "问题描述不能超过200个字符");
        }

        //收货地址不能为空
        if (StringUtil.isEmpty(applyAfterSaleVO.getChangeInfo().getShipAddr())) {
            throw new ServiceException(AftersaleErrorCode.E604.name(), "收货地址不能为空");
        }

        //收货人不能为空
        if (StringUtil.isEmpty(applyAfterSaleVO.getChangeInfo().getShipName())) {
            throw new ServiceException(AftersaleErrorCode.E604.name(), "收货人不能为空");
        }

        //收货人手机号不能为空并且格式要正确
        if (StringUtil.isEmpty(applyAfterSaleVO.getChangeInfo().getShipMobile()) || !Validator.isMobile(applyAfterSaleVO.getChangeInfo().getShipMobile())) {
            throw new ServiceException(AftersaleErrorCode.E604.name(), "请填写正确格式的收货人手机号码");
        }

        AfterSaleApplyDTO applyDTO = new AfterSaleApplyDTO();
        applyDTO.setBuyer(buyer);
        applyDTO.setOrderDO(order);
        applyDTO.setItemsDO(orderItemsDO);
        return applyDTO;
    }

    @Override
    public AfterSaleApplyDTO checkCancelOrder(RefundApplyVO refundApplyVO) {
        //获取当前登录的会员信息
        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E110.code(), "当前会员已经退出登录");
        }

        //订单编号不能为空
        if (StringUtil.isEmpty(refundApplyVO.getOrderSn())) {
            throw new ServiceException(AftersaleErrorCode.E618.name(), "订单编号不能为空");
        }

        //获取要申请售后的订单信息
        OrderDO order = this.orderQueryManager.getOrder(refundApplyVO.getOrderSn());

        //不存在的订单或者不属于当前会员的订单进行校验
        if (order == null || !buyer.getUid().equals(order.getMemberId())) {
            throw new ServiceException(AftersaleErrorCode.E604.name(), "申请售后服务的订单信息不存在");
        }

        //操作权限校验
        if (!OrderServiceStatusEnum.NOT_APPLY.value().equals(order.getServiceStatus())) {
            throw new ServiceException(AftersaleErrorCode.E601.name(), "当前订单不允许进行取消订单操作");
        }

        //提交原因不能为空
        if (StringUtil.isEmpty(refundApplyVO.getReason())) {
            throw new ServiceException(AftersaleErrorCode.E612.name(), "提交原因不能为空");
        }

        //收货人手机号不能为空并且格式要正确
        if (StringUtil.isEmpty(refundApplyVO.getMobile()) || !Validator.isMobile(refundApplyVO.getMobile())) {
            throw new ServiceException(AftersaleErrorCode.E618.name(), "请填写正确格式的手机号码");
        }

        AfterSaleApplyDTO applyDTO = new AfterSaleApplyDTO();
        applyDTO.setBuyer(buyer);
        applyDTO.setOrderDO(order);
        return applyDTO;
    }

    @Override
    public void checkRefundInfo(AfterSaleRefundDO refundDO) {
        //退款方式不能为空
        if (StringUtil.isEmpty(refundDO.getAccountType())) {
            throw new ServiceException(AftersaleErrorCode.E605.name(), "请选择退款方式");
        }

        //如果退款方式为银行转账
        if (AccountTypeEnum.BANKTRANSFER.value().equals(refundDO.getAccountType())) {

            if (StringUtil.isEmpty(refundDO.getBankName())) {
                throw new ServiceException(AftersaleErrorCode.E611.name(), "银行名称不能为空");
            }

            if (StringUtil.isEmpty(refundDO.getBankAccountNumber())) {
                throw new ServiceException(AftersaleErrorCode.E611.name(), "银行账号不能为空");
            }

            if (StringUtil.isEmpty(refundDO.getBankAccountName())) {
                throw new ServiceException(AftersaleErrorCode.E611.name(), "银行开户名不能为空");
            }

            if (StringUtil.isEmpty(refundDO.getBankDepositName())) {
                throw new ServiceException(AftersaleErrorCode.E611.name(), "银行开户行不能为空");
            }

        } else {
            //如果退款方式为其他方式（如支付宝或微信），需要判断退款账号是否为空
            if (StringUtil.isEmpty(refundDO.getReturnAccount())) {
                throw new ServiceException(AftersaleErrorCode.E611.name(), "退款账号不能为空");
            }
        }
    }

    @Override
    public String checkAfterSaleExpress(AfterSaleExpressDO afterSaleExpressDO) {

        //售后服务单编号不能为空
        if (StringUtil.isEmpty(afterSaleExpressDO.getServiceSn())) {
            throw new ServiceException(AftersaleErrorCode.E618.name(), "售后服务单编号不能为空");
        }

        //快递公司信息必填
        if (afterSaleExpressDO.getCourierCompanyId() == null) {
            throw new ServiceException(AftersaleErrorCode.E618.name(), "请选择快递公司");
        }

        //快递单号不能为空
        if (StringUtil.isEmpty(afterSaleExpressDO.getCourierNumber())) {
            throw new ServiceException(AftersaleErrorCode.E618.name(), "快递单号不能为空");
        }

        //发货时间不能为空
        if (afterSaleExpressDO.getShipTime() == null) {
            throw new ServiceException(AftersaleErrorCode.E618.name(), "发货时间不能为空");
        }

        //获取物流公司信息
        LogisticsCompanyDO logisticsCompanyDO = this.logisticsCompanyManager.getModel(afterSaleExpressDO.getCourierCompanyId());

        if (logisticsCompanyDO == null) {
            throw new ServiceException(AftersaleErrorCode.E618.name(), "快递公司不存在");
        }

        return logisticsCompanyDO.getName();
    }

    @Override
    public ApplyAfterSaleVO checkAudit(String serviceSn, String auditStatus, Double refundPrice, String returnAddr, String auditRemark) {
        //售后服务单号都不允许为空
        if (StringUtil.isEmpty(serviceSn)) {
            throw new ServiceException(AftersaleErrorCode.E618.name(), "售后服务单号不能为空");
        }

        //审核状态都不允许为空
        if (StringUtil.isEmpty(auditStatus)) {
            throw new ServiceException(AftersaleErrorCode.E618.name(), "审核状态不能为空");
        }

        if (!ServiceStatusEnum.PASS.value().equals(auditStatus) && !ServiceStatusEnum.REFUSE.value().equals(auditStatus)) {
            throw new ServiceException(AftersaleErrorCode.E618.name(), "审核状态不正确");
        }

        //如果商家填写了审核备注，那么备注信息字数不能超过150个字符
        if (StringUtil.notEmpty(auditRemark)) {
            if (auditRemark.length() > 150) {
                throw new ServiceException(AftersaleErrorCode.E618.name(), "审核备注信息不可超过150个字符");
            }
        }

        //获取售后服务详细信息
        ApplyAfterSaleVO applyAfterSaleVO = this.afterSaleQueryManager.detail(serviceSn);

        //对当前登录商家进行权限判断
        Seller seller = UserContext.getSeller();
        if (seller == null || seller.getSellerId().intValue() != applyAfterSaleVO.getSellerId().intValue()) {
            throw new ServiceException(AftersaleErrorCode.E614.name(), "售后服务单信息不存在");
        }

        //获取售后服务类型
        String serviceType = applyAfterSaleVO.getServiceType();

        //如果审核状态为审核通过
        if (ServiceStatusEnum.PASS.value().equals(auditStatus)) {
            //如果售后服务类型为退货或者取消订单，则需要校验退款金额
            if (ServiceTypeEnum.RETURN_GOODS.value().equals(serviceType) || ServiceTypeEnum.ORDER_CANCEL.value().equals(serviceType)) {
                if (refundPrice == null) {
                    throw new ServiceException(AftersaleErrorCode.E618.name(), "退款金额不能为空");
                }

                if (refundPrice <= 0) {
                    throw new ServiceException(AftersaleErrorCode.E618.name(), "退款金额不可以小于或等于0元");
                }
            }

            //如果售后服务类型为退货或者换货，则需要校验退货地址信息是否为空并且修改售后服务信息中的退货地址信息
            if (ServiceTypeEnum.RETURN_GOODS.value().equals(serviceType) || ServiceTypeEnum.CHANGE_GOODS.value().equals(serviceType)) {
                if (StringUtil.isEmpty(returnAddr)) {
                    throw new ServiceException(AftersaleErrorCode.E618.name(), "退货地址信息不能为空");
                }
            }
        }

        return applyAfterSaleVO;
    }

    @Override
    public void checkPutInWarehouse(String serviceSn, List<PutInWarehouseDTO> storageList, String remark) {
        //售后服务单号不允许为空
        if (StringUtil.isEmpty(serviceSn)) {
            throw new ServiceException(AftersaleErrorCode.E618.name(), "售后服务单号不能为空");
        }

        //入库信息集合不能为空
        if (storageList == null || storageList.size() == 0) {
            throw new ServiceException(AftersaleErrorCode.E618.name(), "入库信息不能为空");
        }

        //如果商家填写了入库备注信息，那么需要对入库备注信息长度进行校验
        if (StringUtil.notEmpty(remark)) {
            if (remark.length() > 150) {
                throw new ServiceException(AftersaleErrorCode.E618.name(), "入库备注信息不可超过150个字符");
            }
        }

    }

    @Override
    public void checkCloseAfterSale(String serviceSn, String remark) {
        //售后服务单号不允许为空
        if (StringUtil.isEmpty(serviceSn)) {
            throw new ServiceException(AftersaleErrorCode.E618.name(), "售后服务单号不能为空");
        }

        //备注不能为空
        if (StringUtil.isEmpty(remark)) {
            throw new ServiceException(AftersaleErrorCode.E618.name(), "关闭原因不能为空");
        }

        //备注长度不能超过150个字符
        if (remark.length() > 150) {
            throw new ServiceException(AftersaleErrorCode.E618.name(), "关闭原因不可超过150个字符");
        }
    }

    @Override
    public void checkAdminRefund(String serviceSn, Double refundPrice, String remark) {

        //退款单编号和退款金额不能为空
        if (StringUtil.isEmpty(serviceSn)) {
            throw new ServiceException(AftersaleErrorCode.E618.name(), "退款单编号不能为空");
        }

        if (refundPrice == null) {
            throw new ServiceException(AftersaleErrorCode.E618.name(), "退款金额不能为空");
        }

        //退款金额不能小于或等于0元
        if (refundPrice.doubleValue() <= 0) {
            throw new ServiceException(AftersaleErrorCode.E618.name(), "退款金额不能小于或等于0元");
        }

        //限制备注字符长度
        if (StringUtil.notEmpty(remark)) {
            if (remark.length() > 150) {
                throw new ServiceException(AftersaleErrorCode.E618.name(), "退款备注不能超过150个字符");
            }
        }

    }
}
