package com.enation.app.javashop.core.goods.model.vo;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 购买数量
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-07-06 上午1:22
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BuyCountVO {


    /**
     * 主键
     */
    @Id(name = "goods_id")
    @ApiModelProperty(hidden = true)
    private Integer goodsId;
    /**
     * 购买数量
     */
    @Column(name = "buy_count")
    @ApiModelProperty(name = "buy_count", value = "购买数量", required = false)
    private Integer buyCount;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    @Override
    public String toString() {
        return "BuyCountVO{" +
                "goodsId=" + goodsId +
                ", buyCount=" + buyCount +
                '}';
    }
}
