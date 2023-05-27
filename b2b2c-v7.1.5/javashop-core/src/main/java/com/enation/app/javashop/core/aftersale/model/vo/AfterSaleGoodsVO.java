package com.enation.app.javashop.core.aftersale.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 售后商品信息VO
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-28
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AfterSaleGoodsVO implements Serializable {

    private static final long serialVersionUID = -4144278368570923608L;

    /**
     * 商品ID
     */
    @ApiModelProperty(name = "goods_id", value = "商品ID")
    private Integer goodsId;
    /**
     * 商品SKUID
     */
    @ApiModelProperty(name = "sku_id", value = "商品SKUID")
    private Integer skuId;
    /**
     * 商品成交价
     */
    @ApiModelProperty(name = "price", value = "商品成交价")
    private Double price;
    /**
     * 购买数量
     */
    @ApiModelProperty(name = "ship_num", value = "购买数量")
    private Integer shipNum;
    /**
     * 退还数量
     */
    @ApiModelProperty(name = "return_num", value = "退还数量")
    private Integer returnNum;
    /**
     * 商品名称
     */
    @ApiModelProperty(name = "goods_name", value = "商品名称")
    private String goodsName;
    /**
     * 商品缩略图
     */
    @ApiModelProperty(name = "goods_image", value = "商品缩略图")
    private String goodsImage;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getShipNum() {
        return shipNum;
    }

    public void setShipNum(Integer shipNum) {
        this.shipNum = shipNum;
    }

    public Integer getReturnNum() {
        return returnNum;
    }

    public void setReturnNum(Integer returnNum) {
        this.returnNum = returnNum;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    @Override
    public String toString() {
        return "AfterSaleGoodsVO{" +
                "goodsId=" + goodsId +
                ", skuId=" + skuId +
                ", price=" + price +
                ", shipNum=" + shipNum +
                ", returnNum=" + returnNum +
                ", goodsName='" + goodsName + '\'' +
                ", goodsImage='" + goodsImage + '\'' +
                '}';
    }
}
