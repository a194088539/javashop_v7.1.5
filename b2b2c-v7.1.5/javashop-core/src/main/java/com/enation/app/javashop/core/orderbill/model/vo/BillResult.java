package com.enation.app.javashop.core.orderbill.model.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author fk
 * @version v2.0
 * @Description: 统计结果
 * @date 2018/4/2 79:16
 * @since v7.0.0
 */
public class BillResult implements Serializable {

    @ApiModelProperty(name = "online_price", value = "在线支付的总金额", required = false)
    private Double onlinePrice;


    @ApiModelProperty(name = "online_refund_price", value = "在线支付后退款的总金额", required = false)
    private Double onlineRefundPrice;

    @ApiModelProperty(name = "cod_price", value = "货到付款的总金额", required = false)
    private Double codPrice;

    @ApiModelProperty(name = "cod_refund_price", value = "货到付款后退款的总金额", required = false)
    private Double codRefundPrice;

    @ApiModelProperty(name = "seller_id", value = "卖家id", required = false)
    private Integer sellerId;

    @ApiModelProperty(name = "site_coupon_commi", value = "站点优惠券佣金", required = false)
    private Double siteCouponCommi;


    public BillResult(Double onlinePrice, Double onlineRefundPrice, Double codPrice, Double codRefundPrice, Integer sellerId,Double siteCouponCommi) {
        this.onlinePrice = onlinePrice;
        this.onlineRefundPrice = onlineRefundPrice;
        this.codPrice = codPrice;
        this.codRefundPrice = codRefundPrice;
        this.sellerId = sellerId;
        this.siteCouponCommi = siteCouponCommi;
    }

    public BillResult() {
    }

    public Double getOnlinePrice() {
        return onlinePrice;
    }

    public void setOnlinePrice(Double onlinePrice) {
        this.onlinePrice = onlinePrice;
    }

    public Double getOnlineRefundPrice() {
        return onlineRefundPrice;
    }

    public void setOnlineRefundPrice(Double onlineRefundPrice) {
        this.onlineRefundPrice = onlineRefundPrice;
    }

    public Double getCodPrice() {
        return codPrice;
    }

    public void setCodPrice(Double codPrice) {
        this.codPrice = codPrice;
    }

    public Double getCodRefundPrice() {
        return codRefundPrice;
    }

    public void setCodRefundPrice(Double codRefundPrice) {
        this.codRefundPrice = codRefundPrice;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public Double getSiteCouponCommi() {
        return siteCouponCommi;
    }

    public void setSiteCouponCommi(Double siteCouponCommi) {
        this.siteCouponCommi = siteCouponCommi;
    }
}
