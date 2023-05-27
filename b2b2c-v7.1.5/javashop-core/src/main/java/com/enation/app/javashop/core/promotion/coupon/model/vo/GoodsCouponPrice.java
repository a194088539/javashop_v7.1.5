package com.enation.app.javashop.core.promotion.coupon.model.vo;

import com.enation.app.javashop.core.trade.cart.model.vo.CartSkuVO;
import com.enation.app.javashop.framework.util.CurrencyUtil;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 商品优惠券金额
 *
 * @author fk
 * @version v2.0
 * @since v7.1.5
 * 2019-09-19 23:19:39
 */
public class GoodsCouponPrice implements Serializable {

    /**
     * skuid
     */
    private Integer skuId;

    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     * 优惠券抵扣的金额
     */
    private Double couponPrice;

    /**
     * 商品金额
     */
    private Double goodsOriginPrice;

    /**
     * 商家id
     */
    private Integer sellerId;

    /**
     * 优惠券id
     */
    private Integer couponId;

    /**
     * 会员优惠券id
     */
    private Integer memberCouponId;


    public GoodsCouponPrice() {

    }

    public GoodsCouponPrice(CartSkuVO skuVO) {

        this.setGoodsId(skuVO.getGoodsId());
        this.setSkuId(skuVO.getSkuId());
        this.setGoodsOriginPrice(CurrencyUtil.mul(skuVO.getOriginalPrice(),skuVO.getNum()));
        this.setSellerId(skuVO.getSellerId());
    }


    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Double getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(Double couponPrice) {
        this.couponPrice = couponPrice;
    }

    public Double getGoodsOriginPrice() {
        return goodsOriginPrice;
    }

    public void setGoodsOriginPrice(Double goodsOriginPrice) {
        this.goodsOriginPrice = goodsOriginPrice;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public Integer getMemberCouponId() {
        return memberCouponId;
    }

    public void setMemberCouponId(Integer memberCouponId) {
        this.memberCouponId = memberCouponId;
    }
}
