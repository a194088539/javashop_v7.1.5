package com.enation.app.javashop.core.client.trade;

import com.enation.app.javashop.core.promotion.tool.model.vo.PromotionVO;

import java.util.List;

/**
 * 促销活动客户端
 *
 * @author zh
 * @version v7.0
 * @date 19/3/28 上午11:10
 * @since v7.0
 */
public interface PromotionGoodsClient {
    /**
     * 删除促销活动商品
     *
     * @param goodsId    商品id
     * @param type       活动类型
     * @param activityId 活动id
     */
    void delPromotionGoods(Integer goodsId, String type, Integer activityId);


    /**
     * 删除促销活动商品
     *
     * @param goodsId
     */
    void delPromotionGoods(Integer goodsId);


    /**
     * 根据商品id读取商品参与的所有活动（有效的活动）
     *
     * @param goodsId
     * @return 返回活动的集合
     */
    List<PromotionVO> getPromotion(Integer goodsId);
}
