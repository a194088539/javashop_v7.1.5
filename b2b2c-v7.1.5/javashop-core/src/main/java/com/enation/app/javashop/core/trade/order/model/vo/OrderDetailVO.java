package com.enation.app.javashop.core.trade.order.model.vo;

import com.enation.app.javashop.core.member.model.dos.ReceiptHistory;
import com.enation.app.javashop.core.promotion.fulldiscount.model.dos.FullDiscountGiftDO;
import com.enation.app.javashop.core.trade.cart.model.vo.CouponVO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.enums.*;
import com.enation.app.javashop.core.trade.order.support.OrderSpecialStatus;
import com.enation.app.javashop.framework.util.CurrencyUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;

/**
 * 订单明细
 *
 * @author Snow create in 2018/5/15
 * @version v2.0
 * @since v7.0.0
 */
@ApiModel(description = "订单明细")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderDetailVO extends OrderDO {

    @ApiModelProperty(value = "订单操作允许情况")
    private OrderOperateAllowable orderOperateAllowableVO;

    @ApiModelProperty(value = "订单状态文字")
    private String orderStatusText;

    @ApiModelProperty(value = "付款状态文字")
    private String payStatusText;

    @ApiModelProperty(value = "发货状态文字")
    private String shipStatusText;

    @ApiModelProperty(value = "售后状态文字")
    private String serviceStatusText;

    @ApiModelProperty(value = "支付方式")
    private String paymentName;

    @ApiModelProperty(value = "sku列表")
    private List<OrderSkuVO> orderSkuList;

    @ApiModelProperty(value = "发票信息")
    private ReceiptHistory receiptHistory;

    @ApiModelProperty(value = "订单赠品列表")
    private List<FullDiscountGiftDO> giftList;

    @ApiModelProperty(value = "返现金额")
    private Double cashBack;

    @ApiModelProperty(value = "优惠券抵扣金额")
    private Double couponPrice;

    @ApiModelProperty(value = "赠送的积分")
    private Integer giftPoint;

    @ApiModelProperty(value = "赠送的优惠券")
    private CouponVO giftCoupon;


    @ApiModelProperty(value = "此订单使用的积分")
    private Integer usePoint;


    @ApiModelProperty(value = "满减金额")
    private Double fullMinus;

    /**
     * 拼团订单状态
     */
    @ApiModelProperty(value = "拼团订单状态")
    private String pingTuanStatus;
    /**
     * 订单是否支持原路退回
     * 未支付的订单为false
     */
    @ApiModelProperty(name = "is_retrace", value = "订单是否支持原路退回")
    private Boolean isRetrace;


    public ReceiptHistory getReceiptHistory() {
        return receiptHistory;
    }

    public void setReceiptHistory(ReceiptHistory receiptHistory) {
        this.receiptHistory = receiptHistory;
    }

    public OrderOperateAllowable getOrderOperateAllowableVO() {
        return orderOperateAllowableVO;
    }

    public void setOrderOperateAllowableVO(OrderOperateAllowable orderOperateAllowableVO) {
        this.orderOperateAllowableVO = orderOperateAllowableVO;
    }

    public String getPingTuanStatus() {
//        pingTuanStatus = "";
//        //已经付款的拼团订单的状态为待成团
//        if (OrderTypeEnum.pintuan.name().equals(this.getOrderType())) {
//            if (this.getPayStatus().equals(PayStatusEnum.PAY_NO.value())) {
//                if(OrderStatusEnum.CANCELLED.value().equals(this.getOrderStatus())){
//                    pingTuanStatus = "未成团";
//                }else{
//                    pingTuanStatus = "待成团";
//                }
//            } else if (OrderStatusEnum.PAID_OFF.value().equals(this.getOrderStatus())) {
//                pingTuanStatus = "待成团";
//            } else {
//                pingTuanStatus = "已成团";
//            }
//
//        }

        return pingTuanStatus;
    }

    public void setPingTuanStatus(String pingTuanStatus) {
        this.pingTuanStatus = pingTuanStatus;
    }

    public String getOrderStatusText() {

        //先从特殊的流程-状态显示 定义中读取，如果为空说明不是特殊的状态，直接显示为 状态对应的提示词
        orderStatusText = OrderSpecialStatus.getStatusText(getOrderType(), getPaymentType(), getOrderStatus());
        if (StringUtil.isEmpty(orderStatusText)) {
            orderStatusText = OrderStatusEnum.valueOf(getOrderStatus()).description();
        }

        return orderStatusText;
    }

    public void setOrderStatusText(String orderStatusText) {
        this.orderStatusText = orderStatusText;
    }

    public String getPayStatusText() {
        if (this.getPayStatus() != null) {
            PayStatusEnum payStatusEnum = PayStatusEnum.valueOf(this.getPayStatus());
            payStatusText = payStatusEnum.description();
        }
        return payStatusText;
    }

    public void setPayStatusText(String payStatusText) {
        this.payStatusText = payStatusText;
    }

    public String getShipStatusText() {
        if (this.getShipStatus() != null) {
            ShipStatusEnum shipStatusEnum = ShipStatusEnum.valueOf(this.getShipStatus());
            shipStatusText = shipStatusEnum.description();
        }
        return shipStatusText;
    }

    public void setShipStatusText(String shipStatusText) {
        this.shipStatusText = shipStatusText;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }


    public List<OrderSkuVO> getOrderSkuList() {
        return orderSkuList;
    }

    public void setOrderSkuList(List<OrderSkuVO> orderSkuList) {
        this.orderSkuList = orderSkuList;
    }

    public String getServiceStatusText() {
        if (this.getServiceStatus() != null) {
            OrderServiceStatusEnum serviceStatusEnum = OrderServiceStatusEnum.valueOf(this.getServiceStatus());
            serviceStatusText = serviceStatusEnum.description();
        }
        return serviceStatusText;
    }

    public void setServiceStatusText(String serviceStatusText) {
        this.serviceStatusText = serviceStatusText;
    }

    public List<FullDiscountGiftDO> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<FullDiscountGiftDO> giftList) {
        this.giftList = giftList;
    }

    public Double getCashBack() {
        return cashBack;
    }

    public void setCashBack(Double cashBack) {
        this.cashBack = cashBack;
    }

    public Double getCouponPrice() {
        if (couponPrice == null) {
            return 0D;
        }
        return couponPrice;
    }

    public void setCouponPrice(Double couponPrice) {
        this.couponPrice = couponPrice;
    }

    public Integer getGiftPoint() {
        return giftPoint;
    }

    public void setGiftPoint(Integer giftPoint) {
        this.giftPoint = giftPoint;
    }

    public Integer getUsePoint() {
        return usePoint;
    }

    public void setUsePoint(Integer usePoint) {
        this.usePoint = usePoint;
    }

    public CouponVO getGiftCoupon() {
        return giftCoupon;
    }

    public void setGiftCoupon(CouponVO giftCoupon) {
        this.giftCoupon = giftCoupon;
    }

    public Double getFullMinus() {
        if (fullMinus == null) {
            return 0D;
        }
        return fullMinus;
    }

    public void setFullMinus(Double fullMinus) {
        this.fullMinus = fullMinus;
    }

    public Boolean getIsRetrace() {
        return isRetrace;
    }

    public void setIsRetrace(Boolean isRetrace) {
        this.isRetrace = isRetrace;
    }

    @Override
    public Double getGoodsPrice() {

        Double goodsPrice = super.getGoodsPrice();
        //7.0.x数据库缺少商品原价字段，使用动态计算的方法算出商品原价
        goodsPrice = CurrencyUtil.add(goodsPrice, getDiscountPrice());

        return goodsPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        OrderDetailVO that = (OrderDetailVO) o;
        return  Objects.equals(orderOperateAllowableVO, that.orderOperateAllowableVO) &&
                Objects.equals(orderStatusText, that.orderStatusText) &&
                Objects.equals(payStatusText, that.payStatusText) &&
                Objects.equals(shipStatusText, that.shipStatusText) &&
                Objects.equals(serviceStatusText, that.serviceStatusText) &&
                Objects.equals(paymentName, that.paymentName) &&
                Objects.equals(orderSkuList, that.orderSkuList) &&
                Objects.equals(receiptHistory, that.receiptHistory) &&
                Objects.equals(giftList, that.giftList) &&
                Objects.equals(cashBack, that.cashBack) &&
                Objects.equals(couponPrice, that.couponPrice) &&
                Objects.equals(giftPoint, that.giftPoint) &&
                Objects.equals(giftCoupon, that.giftCoupon) &&
                Objects.equals(usePoint, that.usePoint) &&
                Objects.equals(fullMinus, that.fullMinus) &&
                Objects.equals(pingTuanStatus, that.pingTuanStatus) &&
                Objects.equals(isRetrace, that.isRetrace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), orderOperateAllowableVO, orderStatusText, payStatusText, shipStatusText, serviceStatusText, paymentName, orderSkuList, receiptHistory, giftList, cashBack, couponPrice, giftPoint, giftCoupon, usePoint, fullMinus, pingTuanStatus, isRetrace);
    }

    @Override
    public String toString() {
        return "OrderDetailVO{" +
                "orderOperateAllowableVO=" + orderOperateAllowableVO +
                ", orderStatusText='" + orderStatusText + '\'' +
                ", payStatusText='" + payStatusText + '\'' +
                ", shipStatusText='" + shipStatusText + '\'' +
                ", serviceStatusText='" + serviceStatusText + '\'' +
                ", paymentName='" + paymentName + '\'' +
                ", orderSkuList=" + orderSkuList +
                ", receiptHistory=" + receiptHistory +
                ", giftList=" + giftList +
                ", cashBack=" + cashBack +
                ", couponPrice=" + couponPrice +
                ", giftPoint=" + giftPoint +
                ", giftCoupon=" + giftCoupon +
                ", usePoint=" + usePoint +
                ", fullMinus=" + fullMinus +
                ", pingTuanStatus='" + pingTuanStatus + '\'' +
                ", isRetrace=" + isRetrace +
                '}';
    }
}
