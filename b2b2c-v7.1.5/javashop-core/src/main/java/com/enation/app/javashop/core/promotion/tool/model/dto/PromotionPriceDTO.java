package com.enation.app.javashop.core.promotion.tool.model.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 活动DTO
 *
 * @author zh create in 2018/7/9
 * @version v2.0
 * @since v7.0.0
 */
public class PromotionPriceDTO implements Serializable {


    private static final long serialVersionUID = -975019606881133067L;

    @ApiModelProperty(value = "商品商品")
    private Integer goodsId;

    @ApiModelProperty(value = "活动价格")
    private Double price;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "PromotionPriceDTO{" +
                "goodsId=" + goodsId +
                ", price=" + price +
                '}';
    }
}
