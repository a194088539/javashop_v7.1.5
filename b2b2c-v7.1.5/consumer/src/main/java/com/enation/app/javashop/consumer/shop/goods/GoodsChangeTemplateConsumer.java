package com.enation.app.javashop.consumer.shop.goods;

import com.enation.app.javashop.consumer.core.event.GoodsChangeEvent;
import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.base.message.GoodsChangeMsg;
import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.member.ShipTemplateClient;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.goods.model.vo.GoodsSkuVO;
import com.enation.app.javashop.core.shop.model.vo.ShipTemplateVO;
import com.enation.app.javashop.framework.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品变化的时候对运费模板的copy操作
 *
 * @author zh
 * @version v7.0
 * @date 2019/9/18 8:20 AM
 * @since v7.0
 */
@Service
public class GoodsChangeTemplateConsumer implements GoodsChangeEvent {

    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private Cache cache;

    @Autowired
    private ShipTemplateClient shipTemplateClient;


    @Override
    public void goodsChange(GoodsChangeMsg goodsChangeMsg) {
        //如果是进行添加或者修修改商品操作，需要将sku和模板进行绑定
        if (goodsChangeMsg.getOperationType() == GoodsChangeMsg.ADD_OPERATION || goodsChangeMsg.getOperationType() == GoodsChangeMsg.UPDATE_OPERATION) {
            //获取商品id
            Integer goodsId = goodsChangeMsg.getGoodsIds()[0];
            //查询此商品绑定的运费模板
            CacheGoods cacheGoods = goodsClient.getFromCache(goodsId);
            //如果此商品绑定运费模板的情况下，给sku绑定脚本
            if (!cacheGoods.getTemplateId().equals(0)) {
                //获取当前商品的skuid
                List<GoodsSkuVO> skus = goodsClient.listByGoodsId(goodsId);
                for (GoodsSkuVO goodsSkuVO : skus) {

                    List<String> scripts = shipTemplateClient.getScripts(cacheGoods.getTemplateId());
                    goodsSkuVO.setScripts(scripts);
                    goodsSkuVO.setTemplateId(cacheGoods.getTemplateId());
                    //重新存入缓存
                    cache.put(CachePrefix.SKU.getPrefix() + goodsSkuVO.getSkuId(), goodsSkuVO);
                }
            }
        }

    }

}
