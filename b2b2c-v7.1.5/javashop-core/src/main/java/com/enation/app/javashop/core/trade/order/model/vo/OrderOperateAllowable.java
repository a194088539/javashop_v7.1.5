package com.enation.app.javashop.core.trade.order.model.vo;

import com.enation.app.javashop.core.support.FlowCheckOperate;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.enums.*;
import com.enation.app.javashop.core.trade.order.support.OrderOperateFlow;
import com.enation.app.javashop.core.trade.order.support.OrderStep;
import com.enation.app.javashop.framework.util.StringUtil;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;

/**
 * 订单可进行的操作
 *
 * @author Snow create in 2018/5/15
 * @version v2.0
 * @since v7.0.0
 */
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderOperateAllowable implements Serializable {

    @ApiModelProperty(value = "是否允许被取消")
    private Boolean allowCancel;

    @ApiModelProperty(value = "是否允许被确认")
    private Boolean allowConfirm;

    @ApiModelProperty(value = "是否允许被支付")
    private Boolean allowPay;

    @ApiModelProperty(value = "是否允许被发货")
    private Boolean allowShip;

    @ApiModelProperty(value = "是否允许被收货")
    private Boolean allowRog;

    @ApiModelProperty(value = "是否允许被评论")
    private Boolean allowComment;

    @ApiModelProperty(value = "是否允许被完成")
    private Boolean allowComplete;

    @ApiModelProperty(value = "是否允许申请售后")
    private Boolean allowApplyService;

    @ApiModelProperty(value = "是否允许取消(售后)")
    private Boolean allowServiceCancel;

    @ApiModelProperty(value = "是否允许查看物流信息")
    private Boolean allowCheckExpress;

    @ApiModelProperty(value = "是否允许更改收货人信息")
    private Boolean allowEditConsignee;

    @ApiModelProperty(value = "是否允许更改价格")
    private Boolean allowEditPrice;

    @ApiModelProperty(value = "是否允许查看取消订单信息")
    private Boolean allowCheckCancel;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    public Boolean getAllowCancel() {
        return allowCancel;
    }

    public void setAllowCancel(Boolean allowCancel) {
        this.allowCancel = allowCancel;
    }

    public Boolean getAllowConfirm() {
        return allowConfirm;
    }

    public void setAllowConfirm(Boolean allowConfirm) {
        this.allowConfirm = allowConfirm;
    }

    public Boolean getAllowPay() {
        return allowPay;
    }

    public void setAllowPay(Boolean allowPay) {
        this.allowPay = allowPay;
    }

    public Boolean getAllowShip() {
        return allowShip;
    }

    public void setAllowShip(Boolean allowShip) {
        this.allowShip = allowShip;
    }

    public Boolean getAllowRog() {
        return allowRog;
    }

    public void setAllowRog(Boolean allowRog) {
        this.allowRog = allowRog;
    }

    public Boolean getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Boolean allowComment) {
        this.allowComment = allowComment;
    }

    public Boolean getAllowComplete() {
        return allowComplete;
    }

    public void setAllowComplete(Boolean allowComplete) {
        this.allowComplete = allowComplete;
    }

    public Boolean getAllowApplyService() {
        return allowApplyService;
    }

    public void setAllowApplyService(Boolean allowApplyService) {
        this.allowApplyService = allowApplyService;
    }

    public Boolean getAllowServiceCancel() {
        return allowServiceCancel;
    }

    public void setAllowServiceCancel(Boolean allowServiceCancel) {
        this.allowServiceCancel = allowServiceCancel;
    }

    public Boolean getAllowCheckExpress() {
        return allowCheckExpress;
    }

    public void setAllowCheckExpress(Boolean allowCheckExpress) {
        this.allowCheckExpress = allowCheckExpress;
    }

    public Boolean getAllowEditConsignee() {
        return allowEditConsignee;
    }

    public void setAllowEditConsignee(Boolean allowEditConsignee) {
        this.allowEditConsignee = allowEditConsignee;
    }

    public Boolean getAllowEditPrice() {
        return allowEditPrice;
    }

    public void setAllowEditPrice(Boolean allowEditPrice) {
        this.allowEditPrice = allowEditPrice;
    }

    public Boolean getAllowCheckCancel() {
        return allowCheckCancel;
    }

    public void setAllowCheckCancel(Boolean allowCheckCancel) {
        this.allowCheckCancel = allowCheckCancel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderOperateAllowable allowable = (OrderOperateAllowable) o;

        return new EqualsBuilder()
                .append(allowCancel, allowable.allowCancel)
                .append(allowConfirm, allowable.allowConfirm)
                .append(allowPay, allowable.allowPay)
                .append(allowShip, allowable.allowShip)
                .append(allowRog, allowable.allowRog)
                .append(allowComment, allowable.allowComment)
                .append(allowComplete, allowable.allowComplete)
                .append(allowApplyService, allowable.allowApplyService)
                .append(allowServiceCancel, allowable.allowServiceCancel)
                .append(allowCheckCancel, allowable.allowCheckCancel)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(allowCancel)
                .append(allowConfirm)
                .append(allowPay)
                .append(allowShip)
                .append(allowRog)
                .append(allowComment)
                .append(allowComplete)
                .append(allowApplyService)
                .append(allowServiceCancel)
                .append(allowCheckCancel)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "OrderOperateAllowable{" +
                "allowCancel=" + allowCancel +
                ", allowConfirm=" + allowConfirm +
                ", allowPay=" + allowPay +
                ", allowShip=" + allowShip +
                ", allowRog=" + allowRog +
                ", allowComment=" + allowComment +
                ", allowComplete=" + allowComplete +
                ", allowApplyService=" + allowApplyService +
                ", allowServiceCancel=" + allowServiceCancel +
                ", allowCheckCancel=" + allowCheckCancel +
                '}';
    }

    /**
     * 空构造器
     */
    public OrderOperateAllowable() {

    }


    /**
     * 根据各种状态构建对象
     *
     * @param order
     */
    public OrderOperateAllowable(OrderDO order) {

        String paymentType = order.getPaymentType();

        //获取订单状态
        OrderStatusEnum orderStatus = OrderStatusEnum.valueOf(order.getOrderStatus());
        //获取订单售后状态
        String serviceStatus = order.getServiceStatus();
        //获取订单发货状态
        String shipStatus = order.getShipStatus();
        //获取订单评论状态
        String commentStatus = order.getCommentStatus();
        //获取订单付款状态
        String payStatus = order.getPayStatus();
        //获取订单类型
        String orderType = order.getOrderType();
        //获取订单金额
        double orderPrice = order.getOrderPrice();

        //如果是拼团订单取拼团流程
        if (OrderTypeEnum.PINTUAN.name().equals(orderType)) {
            paymentType = OrderTypeEnum.PINTUAN.name();
        }

        //是否允许被取消
        this.allowCancel = FlowCheckOperate.checkOperate(paymentType, orderStatus.value(), OrderOperateEnum.CANCEL.name());

        //是否允许被确认
        this.allowConfirm = FlowCheckOperate.checkOperate(paymentType, orderStatus.value(), OrderOperateEnum.CONFIRM.name());

        //是否允许被支付
        this.allowPay = FlowCheckOperate.checkOperate(paymentType, orderStatus.value(), OrderOperateEnum.PAY.name());

        //是否允许被发货
        this.allowShip = FlowCheckOperate.checkOperate(paymentType, orderStatus.value(), OrderOperateEnum.SHIP.name()) && !OrderServiceStatusEnum.APPLY.value().equals(serviceStatus);

        //是否允许被收货
        this.allowRog = FlowCheckOperate.checkOperate(paymentType, orderStatus.value(), OrderOperateEnum.ROG.name()) && !OrderServiceStatusEnum.APPLY.value().equals(serviceStatus);

        //是否允许被评论: 收货后可以评论，且评论未完成（评论完成后就不可以再评论了）
        this.allowComment = CommentStatusEnum.UNFINISHED.value().equals(commentStatus) && ShipStatusEnum.SHIP_ROG.value().equals(shipStatus);

        //是否允许被完成
        this.allowComplete = FlowCheckOperate.checkOperate(paymentType, orderStatus.value(), OrderOperateEnum.COMPLETE.name());

        //如果订单是在线支付的
        if (PaymentTypeEnum.ONLINE.name().equals(paymentType)) {

            //订单是否允许取消(这里的取消订单指的是已付款未收货过程中的取消订单) = 支付状态已付款 && 订单金额不为0 && (订单状态已付款 || 已发货) && 订单未申请取消订单 && 订单类型为普通类型订单
            this.allowServiceCancel = PayStatusEnum.PAY_YES.value().equals(payStatus) && orderPrice != 0
                    && (OrderStatusEnum.PAID_OFF.value().equals(orderStatus.value()) || OrderStatusEnum.SHIPPED.value().equals(orderStatus.value()))
                    && OrderServiceStatusEnum.NOT_APPLY.name().equals(serviceStatus) && OrderTypeEnum.NORMAL.name().equals(orderType);

            //是否允许查看订单取消详细信息
            this.allowCheckCancel = OrderServiceStatusEnum.APPLY.value().equals(serviceStatus);

        }

        //是否允许查看物流信息 = 当物流单号不为空并且物流公司不为空
        this.allowCheckExpress = order.getLogiId() != null && !order.getLogiId().equals(0) && !StringUtil.isEmpty(order.getShipNo());
        //是否允许更改收货人信息 = 发货状态未发货 && 未申请取消订单(这里的取消订单指的是已付款未收货过程中的取消订单)
        this.allowEditConsignee = ShipStatusEnum.SHIP_NO.value().equals(order.getShipStatus()) && !OrderServiceStatusEnum.APPLY.value().equals(serviceStatus);
        //是否允许更改价格 = （在线支付 && 未付款）||（货到付款 && 未发货）
        this.allowEditPrice = (PaymentTypeEnum.ONLINE.value().equals(order.getPaymentType())
                && PayStatusEnum.PAY_NO.value().equals(order.getPayStatus()))
                || (PaymentTypeEnum.COD.value().equals(order.getPaymentType())
                && ShipStatusEnum.SHIP_NO.value().equals(order.getShipStatus()));

    }



}
