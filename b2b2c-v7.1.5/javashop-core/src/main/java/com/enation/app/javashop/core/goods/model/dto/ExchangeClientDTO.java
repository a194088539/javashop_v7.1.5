package com.enation.app.javashop.core.goods.model.dto;

import com.enation.app.javashop.core.promotion.exchange.model.dos.ExchangeDO;
import com.enation.app.javashop.core.promotion.tool.model.dto.PromotionGoodsDTO;

import java.io.Serializable;

/**
 * @author fk
 * @version v2.0
 * @Description:
 * @date 2018/8/21 15:15
 * @since v7.0.0
 */
public class ExchangeClientDTO implements Serializable {

    private ExchangeDO exchangeSetting;

    private PromotionGoodsDTO goodsDTO;

    public ExchangeClientDTO() {
    }

    public ExchangeClientDTO(ExchangeDO exchangeSetting, PromotionGoodsDTO goodsDTO) {
        this.exchangeSetting = exchangeSetting;
        this.goodsDTO = goodsDTO;
    }

    public ExchangeDO getExchangeSetting() {
        return exchangeSetting;
    }

    public void setExchangeSetting(ExchangeDO exchangeSetting) {
        this.exchangeSetting = exchangeSetting;
    }

    public PromotionGoodsDTO getGoodsDTO() {
        return goodsDTO;
    }

    public void setGoodsDTO(PromotionGoodsDTO goodsDTO) {
        this.goodsDTO = goodsDTO;
    }
}
