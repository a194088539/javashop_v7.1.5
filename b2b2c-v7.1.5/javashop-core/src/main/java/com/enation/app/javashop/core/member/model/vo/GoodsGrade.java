package com.enation.app.javashop.core.member.model.vo;

/**
 * @author fk
 * @version v1.0
 * @Description: 商品好平率
 * @date 2018/5/4 10:45
 * @since v7.0.0
 */
public class GoodsGrade {

    private Integer goodsId;

    private Double goodRate;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Double getGoodRate() {
        return goodRate;
    }

    public void setGoodRate(Double goodRate) {
        this.goodRate = goodRate;
    }
}
