package com.enation.app.javashop.core.member.model.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 评论评分VO
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-05-03 10:38:00
 */
@ApiModel(description = "评论动态评分vo")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CommentScoreDTO implements Serializable {

    @ApiModelProperty(value = "会员评论vo的list")
    @NotNull(message = "商品评论不能为空")
    @Valid
    private List<CommentDTO> comments;

    /**
     * 订单编号
     */
    @ApiModelProperty(name = "order_sn", value = "订单编号", required = true)
    @NotEmpty(message = "订单编号不能为空")
    private String orderSn;

    /**
     * 发货速度评分
     */
    @ApiModelProperty(name = "delivery_score", value = "发货速度评分1-5", required = true)
    @NotNull(message = "请选择发货速度评分")
    @Min(value = 1, message = "发货速度评分有误")
    @Max(value = 5, message = "发货速度评分有误")
    private Integer deliveryScore;
    /**
     * 描述相符度评分
     */
    @ApiModelProperty(name = "description_score", value = "描述相符度评分1-5", required = true)
    @NotNull(message = "请选择描述相符度评分")
    @Min(value = 1, message = "描述相符度评分有误")
    @Max(value = 5, message = "描述相符度评分有误")
    private Integer descriptionScore;
    /**
     * 服务评分
     */
    @ApiModelProperty(name = "service_score", value = "服务评分1-5", required = true)
    @NotNull(message = "请选择描述服务评分")
    @Min(value = 1, message = "服务评分有误")
    @Max(value = 5, message = "服务评分有误")
    private Integer serviceScore;

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Integer getDeliveryScore() {
        return deliveryScore;
    }

    public void setDeliveryScore(Integer deliveryScore) {
        this.deliveryScore = deliveryScore;
    }

    public Integer getDescriptionScore() {
        return descriptionScore;
    }

    public void setDescriptionScore(Integer descriptionScore) {
        this.descriptionScore = descriptionScore;
    }

    public Integer getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(Integer serviceScore) {
        this.serviceScore = serviceScore;
    }

    @Override
    public String toString() {
        return "CommentScoreDTO{" +
                "comments=" + comments +
                ", orderSn='" + orderSn + '\'' +
                ", deliveryScore=" + deliveryScore +
                ", descriptionScore=" + descriptionScore +
                ", serviceScore=" + serviceScore +
                '}';
    }
}
