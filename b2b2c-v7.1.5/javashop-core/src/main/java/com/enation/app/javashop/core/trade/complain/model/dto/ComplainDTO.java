package com.enation.app.javashop.core.trade.complain.model.dto;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 交易投诉对象
 *
 * @author fk
 * @version v2.0
 * @since v2.0
 * 2019-11-27 16:48:27
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ComplainDTO implements Serializable {


    /**
     * 投诉主题
     */
    @ApiModelProperty(name = "complain_topic", value = "投诉主题", required = true)
    private String complainTopic;
    /**
     * 投诉内容
     */
    @ApiModelProperty(name = "content", value = "投诉内容", required = true)
    private String content;

    /**
     * 投诉凭证图片
     */
    @ApiModelProperty(name = "images", value = "投诉凭证图片，多图以逗号分隔", required = false)
    private String images;

    /**
     * 订单号
     */
    @ApiModelProperty(name = "order_sn", value = "订单号", required = true)
    private String orderSn;

    /**
     * skuid
     */
    @ApiModelProperty(name = "sku_id", value = "sku主键", required = true)
    private Integer skuId;

    public String getComplainTopic() {
        return complainTopic;
    }

    public void setComplainTopic(String complainTopic) {
        this.complainTopic = complainTopic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }
}
