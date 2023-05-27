package com.enation.app.javashop.core.member.model.dto;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


/**
 * 店铺评分DTO
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-05-03 10:38:00
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberShopScoreDTO implements Serializable {
			
    private static final long serialVersionUID = 9224429719464458L;

    /**发货速度评分*/
    @Column(name = "delivery_score")
    @ApiModelProperty(name="delivery_score",value="发货速度评分",required=false)
    private Double deliveryScore;
    /**描述相符度评分*/
    @Column(name = "description_score")
    @ApiModelProperty(name="description_score",value="描述相符度评分",required=false)
    private Double descriptionScore;
    /**服务评分*/
    @Column(name = "service_score")
    @ApiModelProperty(name="service_score",value="服务评分",required=false)
    private Double serviceScore;
    /**卖家*/
    @Column(name = "seller_id")
    @ApiModelProperty(name="seller_id",value="卖家",required=false)
    private Integer sellerId;

    public Double getDeliveryScore() {
        return deliveryScore;
    }

    public void setDeliveryScore(Double deliveryScore) {
        this.deliveryScore = deliveryScore;
    }

    public Double getDescriptionScore() {
        return descriptionScore;
    }

    public void setDescriptionScore(Double descriptionScore) {
        this.descriptionScore = descriptionScore;
    }

    public Double getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(Double serviceScore) {
        this.serviceScore = serviceScore;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    @Override
    public String toString() {
        return "MemberShopScoreDTO{" +
                "deliveryScore=" + deliveryScore +
                ", descriptionScore=" + descriptionScore +
                ", serviceScore=" + serviceScore +
                ", sellerId=" + sellerId +
                '}';
    }
}