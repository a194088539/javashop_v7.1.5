package com.enation.app.javashop.core.promotion.tool.model.dos;

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
 * 有效活动商品对照表实体
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-27 15:17:47
 */
@Table(name="es_promotion_goods")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PromotionGoodsDO implements Serializable {

    private static final long serialVersionUID = 686823101861419L;

    /**商品对照ID*/
    @Id(name = "pg_id")
    @ApiModelProperty(hidden=true)
    private Integer pgId;

    /**商品id*/
    @Column(name = "goods_id")
    @ApiModelProperty(name="goods_id",value="商品id",required=false)
    private Integer goodsId;

    /**货品id*/
    @Column(name = "product_id")
    @ApiModelProperty(name="product_id",value="货品id",required=false)
    private Integer productId;

    /**活动开始时间*/
    @Column(name = "start_time")
    @ApiModelProperty(name="start_time",value="活动开始时间",required=false)
    private Long startTime;

    /**活动结束时间*/
    @Column(name = "end_time")
    @ApiModelProperty(name="end_time",value="活动结束时间",required=false)
    private Long endTime;

    /**活动id*/
    @Column(name = "activity_id")
    @ApiModelProperty(name="activity_id",value="活动id",required=false)
    private Integer activityId;

    /**促销工具类型*/
    @Column(name = "promotion_type")
    @ApiModelProperty(name="promotion_type",value="促销工具类型",required=false)
    private String promotionType;

    /**活动标题*/
    @Column(name = "title")
    @ApiModelProperty(name="title",value="活动标题",required=false)
    private String title;

    /**参与活动的商品数量*/
    @Column(name = "num")
    @ApiModelProperty(name="num",value="参与活动的商品数量",required=false)
    private Integer num;

    /**活动时商品的价格*/
    @Column(name = "price")
    @ApiModelProperty(name="price",value="活动时商品的价格",required=false)
    private Double price;

    @Column(name = "seller_id")
    @ApiModelProperty(name="seller_id",value="商家ID",required=false)
    private Integer sellerId;

    @PrimaryKeyField
    public Integer getPgId() {
        return pgId;
    }
    public void setPgId(Integer pgId) {
        this.pgId = pgId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getProductId() {
        return productId;
    }
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Long getStartTime() {
        return startTime;
    }
    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getActivityId() {
        return activityId;
    }
    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getPromotionType() {
        return promotionType;
    }
    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNum() {
        return num;
    }
    public void setNum(Integer num) {
        this.num = num;
    }

    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    @Override
    public String toString() {
        return "PromotionGoodsDO{" +
                "pgId=" + pgId +
                ", goodsId=" + goodsId +
                ", productId=" + productId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", activityId=" + activityId +
                ", promotionType='" + promotionType + '\'' +
                ", title='" + title + '\'' +
                ", num=" + num +
                ", price=" + price +
                ", sellerId=" + sellerId +
                '}';
    }
}
