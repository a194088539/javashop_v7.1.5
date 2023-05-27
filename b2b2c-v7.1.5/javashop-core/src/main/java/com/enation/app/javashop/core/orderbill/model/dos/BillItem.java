package com.enation.app.javashop.core.orderbill.model.dos;

import java.io.Serializable;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 结算单项表实体
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-04-26 15:39:57
 */
@Table(name = "es_bill_item")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BillItem implements Serializable {

    private static final long serialVersionUID = 8456961440202335L;

    /**
     * 主键id
     */
    @Id(name = "id")
    @ApiModelProperty(hidden = true)
    private Integer id;
    /**
     * 订单编号
     */
    @Column(name = "order_sn")
    @ApiModelProperty(name = "order_sn", value = "订单编号", required = false)
    private String orderSn;
    /**
     * 订单价格
     */
    @Column(name = "price")
    @ApiModelProperty(name = "price", value = "金额，退款金额或者收款金额", required = false)
    private Double price;
    /**
     * 优惠价格
     */
    @Column(name = "discount_price")
    @ApiModelProperty(name = "discount_price", value = "优惠价格", required = false)
    private Double discountPrice;
    /**
     * 单项类型 收款/退款
     */
    @Column(name = "item_type")
    @ApiModelProperty(name = "item_type", value = "单项类型 收款/退款", required = false)
    private String itemType;
    /**
     * 加入时间
     */
    @Column(name = "add_time")
    @ApiModelProperty(name = "add_time", value = "加入时间", required = false)
    private Long addTime;
    /**
     * 所属账单id
     */
    @Column(name = "bill_id")
    @ApiModelProperty(name = "bill_id", value = "所属账单id", required = false)
    private Integer billId;
    /**
     * 状态
     */
    @Column(name = "status")
    @ApiModelProperty(name = "status", value = "状态", required = false)
    private Integer status;
    /**
     * 店铺id
     */
    @Column(name = "seller_id")
    @ApiModelProperty(name = "seller_id", value = "店铺id", required = false)
    private Integer sellerId;
    /**
     * 下单时间
     */
    @Column(name = "order_time")
    @ApiModelProperty(name = "order_time", value = "下单时间", required = false)
    private Long orderTime;
    /**
     * 退款单号
     */
    @Column(name = "refund_sn")
    @ApiModelProperty(name = "refund_sn", value = "退款单号", required = false)
    private String refundSn;
    /**
     * 会员id
     */
    @Column(name = "member_id")
    @ApiModelProperty(name = "member_id", value = "会员id", required = false)
    private Integer memberId;
    /**
     * 会员名称
     */
    @Column(name = "member_name")
    @ApiModelProperty(name = "member_name", value = "会员名称", required = false)
    private String memberName;
    /**
     * 收货人
     */
    @Column(name = "ship_name")
    @ApiModelProperty(name = "ship_name", value = "收货人", required = false)
    private String shipName;
    /**
     * 支付方式
     */
    @Column(name = "payment_type")
    @ApiModelProperty(name = "payment_type", value = "支付方式", required = false)
    private String paymentType;
    /**
     * 退货时间
     */
    @Column(name = "refund_time")
    @ApiModelProperty(name = "refund_time", value = "退货时间", required = false)
    private Long refundTime;

    /**
     * 站点优惠券金额
     */
    @Column(name = "site_coupon_price")
    @ApiModelProperty(name = "site_coupon_price", value = "站点优惠券金额", required = false)
    private Double siteCouponPrice;

    /**
     * 站点优惠券佣金比例
     */
    @Column(name = "coupon_commission")
    @ApiModelProperty(name = "coupon_commission", value = "站点优惠券佣金比例", required = false)
    private Double couponCommission;


    @PrimaryKeyField
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public Long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Long orderTime) {
        this.orderTime = orderTime;
    }

    public String getRefundSn() {
        return refundSn;
    }

    public void setRefundSn(String refundSn) {
        this.refundSn = refundSn;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Long getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Long refundTime) {
        this.refundTime = refundTime;
    }

    public Double getSiteCouponPrice() {
        return siteCouponPrice;
    }

    public void setSiteCouponPrice(Double siteCouponPrice) {
        this.siteCouponPrice = siteCouponPrice;
    }

    public Double getCouponCommission() {
        return couponCommission;
    }

    public void setCouponCommission(Double couponCommission) {
        this.couponCommission = couponCommission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BillItem that = (BillItem) o;
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (orderSn != null ? !orderSn.equals(that.orderSn) : that.orderSn != null) {
            return false;
        }
        if (price != null ? !price.equals(that.price) : that.price != null) {
            return false;
        }
        if (discountPrice != null ? !discountPrice.equals(that.discountPrice) : that.discountPrice != null) {
            return false;
        }
        if (itemType != null ? !itemType.equals(that.itemType) : that.itemType != null) {
            return false;
        }
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) {
            return false;
        }
        if (billId != null ? !billId.equals(that.billId) : that.billId != null) {
            return false;
        }
        if (status != null ? !status.equals(that.status) : that.status != null) {
            return false;
        }
        if (sellerId != null ? !sellerId.equals(that.sellerId) : that.sellerId != null) {
            return false;
        }
        if (orderTime != null ? !orderTime.equals(that.orderTime) : that.orderTime != null) {
            return false;
        }
        if (refundSn != null ? !refundSn.equals(that.refundSn) : that.refundSn != null) {
            return false;
        }
        if (memberId != null ? !memberId.equals(that.memberId) : that.memberId != null) {
            return false;
        }
        if (memberName != null ? !memberName.equals(that.memberName) : that.memberName != null) {
            return false;
        }
        if (shipName != null ? !shipName.equals(that.shipName) : that.shipName != null) {
            return false;
        }
        if (paymentType != null ? !paymentType.equals(that.paymentType) : that.paymentType != null) {
            return false;
        }
        return refundTime != null ? refundTime.equals(that.refundTime) : that.refundTime == null;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (orderSn != null ? orderSn.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (discountPrice != null ? discountPrice.hashCode() : 0);
        result = 31 * result + (itemType != null ? itemType.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        result = 31 * result + (billId != null ? billId.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (sellerId != null ? sellerId.hashCode() : 0);
        result = 31 * result + (orderTime != null ? orderTime.hashCode() : 0);
        result = 31 * result + (refundSn != null ? refundSn.hashCode() : 0);
        result = 31 * result + (memberId != null ? memberId.hashCode() : 0);
        result = 31 * result + (memberName != null ? memberName.hashCode() : 0);
        result = 31 * result + (shipName != null ? shipName.hashCode() : 0);
        result = 31 * result + (paymentType != null ? paymentType.hashCode() : 0);
        result = 31 * result + (refundTime != null ? refundTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BillItem{" +
                "id=" + id +
                ", orderSn='" + orderSn + '\'' +
                ", orderPrice=" + price +
                ", discountPrice=" + discountPrice +
                ", itemType='" + itemType + '\'' +
                ", addTime=" + addTime +
                ", billId=" + billId +
                ", status=" + status +
                ", sellerId=" + sellerId +
                ", orderTime=" + orderTime +
                ", refundSn='" + refundSn + '\'' +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", shipName='" + shipName + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", refundTime=" + refundTime +
                '}';
    }


}