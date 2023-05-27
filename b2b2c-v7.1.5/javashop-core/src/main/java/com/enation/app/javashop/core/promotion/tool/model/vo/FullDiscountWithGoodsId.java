package com.enation.app.javashop.core.promotion.tool.model.vo;

import com.enation.app.javashop.core.promotion.fulldiscount.model.vo.FullDiscountVO;
import com.enation.app.javashop.framework.database.annotation.Column;

/**
 * Created by kingapex on 2018/12/18.
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/12/18
 */
public class FullDiscountWithGoodsId extends FullDiscountVO {
    public FullDiscountWithGoodsId() {
    }

    @Column(name = "goods_id")
    private int goodsId;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }
}
