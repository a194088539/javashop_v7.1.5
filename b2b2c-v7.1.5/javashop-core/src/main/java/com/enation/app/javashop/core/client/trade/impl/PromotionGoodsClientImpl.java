package com.enation.app.javashop.core.client.trade.impl;

import com.enation.app.javashop.core.client.trade.PromotionGoodsClient;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyGoodsManager;
import com.enation.app.javashop.core.promotion.pintuan.service.PintuanGoodsManager;
import com.enation.app.javashop.core.promotion.seckill.service.SeckillGoodsManager;
import com.enation.app.javashop.core.promotion.tool.model.vo.PromotionVO;
import com.enation.app.javashop.core.promotion.tool.service.PromotionGoodsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 促销商品客户端实现
 *
 * @author zh
 * @version v7.0
 * @date 19/3/28 上午11:30
 * @since v7.0
 */
@Service
@ConditionalOnProperty(value = "javashop.product", havingValue = "stand")
public class PromotionGoodsClientImpl implements PromotionGoodsClient {

    @Autowired
    private PromotionGoodsManager promotionGoodsManager;

    @Autowired
    private SeckillGoodsManager seckillGoodsManager;

    @Autowired
    private PintuanGoodsManager pintuanGoodsManager;

    @Autowired
    private GroupbuyGoodsManager groupbuyGoodsManager;


    @Override
    public void delPromotionGoods(Integer goodsId, String type, Integer activityId) {
        promotionGoodsManager.delete(goodsId, activityId, type);
    }


    @Override
    public void delPromotionGoods(Integer goodsId) {
        //删除促销商品
        promotionGoodsManager.delete(goodsId);
        //删除限时抢购商品
        seckillGoodsManager.deleteSeckillGoods(goodsId);
        //删除拼团商品
        pintuanGoodsManager.deletePinTuanGoods(goodsId);
        //删除团购商品
        groupbuyGoodsManager.deleteGoods(goodsId);

    }

    @Override
    public List<PromotionVO> getPromotion(Integer goodsId) {
        return promotionGoodsManager.getPromotion(goodsId);
    }
}
