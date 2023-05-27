package com.enation.app.javashop.consumer.shop.goods;

import com.enation.app.javashop.consumer.core.event.GoodsChangeEvent;
import com.enation.app.javashop.core.base.message.GoodsChangeMsg;
import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.member.ShopClient;
import com.enation.app.javashop.core.client.system.PageDataClient;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.pagedata.model.PageData;
import com.enation.app.javashop.core.payment.model.enums.ClientType;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fk
 * @version v2.0
 * @Description: 店铺商品数量消费者
 * @date 2018/9/14 14:20
 * @since v7.0.0
 */
@Component
public class ShopGoodsCountConsumer implements GoodsChangeEvent {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private ShopClient shopClient;

    @Override
    public void goodsChange(GoodsChangeMsg goodsChangeMsg) {

        //删除操作不需要重新统计店铺的商品数量
        if (GoodsChangeMsg.DEL_OPERATION == goodsChangeMsg.getOperationType()) {
            return;
        }

        //获取商品id
        Integer[] goodsIds = goodsChangeMsg.getGoodsIds();
        if (goodsIds.length > 0) {
            Integer goodsId = goodsIds[0];
            //获取商品店铺id
            CacheGoods goods = goodsClient.getFromCache(goodsId);
            Integer sellerGoodsCount = goodsClient.getSellerGoodsCount(goods.getSellerId());
            //更新店铺信息
            shopClient.updateShopGoodsNum(goods.getSellerId(), sellerGoodsCount);
        }

    }
}
