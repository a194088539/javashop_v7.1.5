package com.enation.app.javashop.core.trade.converter;

import com.enation.app.javashop.core.promotion.tool.model.vo.PromotionVO;
import com.enation.app.javashop.core.trade.cart.model.vo.CartPromotionVo;
import org.springframework.beans.BeanUtils;

/**
 * 转换promotion包下，此包使用到的model及字段
 * @author Snow create in 2018/3/20
 * @version v2.0
 * @since v7.0.0
 */
public class TradePromotionConverter {

    public static CartPromotionVo promotionGoodsVOConverter(PromotionVO promotionVO){
        CartPromotionVo convertPromotionGoodsVO = new CartPromotionVo();
        BeanUtils.copyProperties(promotionVO,convertPromotionGoodsVO);
        return convertPromotionGoodsVO;
    }
}
