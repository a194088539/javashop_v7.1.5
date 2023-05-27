package com.enation.app.javashop.consumer.shop.goods;

import com.enation.app.javashop.consumer.core.event.ShipTemplateChangeEvent;
import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.base.message.GoodsChangeMsg;
import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.member.ShipTemplateClient;
import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.model.vo.GoodsSkuVO;
import com.enation.app.javashop.core.goods.model.vo.ShipTemplateMsg;
import com.enation.app.javashop.core.goods.model.vo.TemplateScript;
import com.enation.app.javashop.core.shop.model.vo.ShipTemplateChildBuyerVO;
import com.enation.app.javashop.core.shop.model.vo.ShipTemplateVO;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.applet.Main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

/**
 * @author zh
 * @version v1.0
 * @Description: 商品运费模板变化消费者
 * @date 2018/6/2510:13
 * @since v7.0.0
 */
@Service
public class ShipTemplateChangeConsumer implements ShipTemplateChangeEvent {

    @Autowired
    private ShipTemplateClient shipTemplateClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private Cache cache;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void shipTemplateChange(ShipTemplateMsg shipTemplateMsg) {
        //获取运费模板
        ShipTemplateVO shipTemplateVO = shipTemplateClient.get(shipTemplateMsg.getTemplateId());
        //如果是新增运费模板或者修改运费模板都需要生成最新的script并且缓存到redis中
        if (ShipTemplateMsg.ADD_OPERATION == shipTemplateMsg.getOperationType() || ShipTemplateMsg.UPDATE_OPERATION == shipTemplateMsg.getOperationType()) {
            shipTemplateClient.cacheShipTemplateScript(shipTemplateVO);
        }
        //如果是修改运费模板，需要修改所有相关此模板的的sku信息
        if (ShipTemplateMsg.UPDATE_OPERATION == shipTemplateMsg.getOperationType()) {
            upSkuTemplateScript(shipTemplateVO);
        }

    }

    /**
     * 更新模板信息，同时更改sku绑定script信息
     *
     * @param shipTemplateVO 运费模板
     */
    private void upSkuTemplateScript(ShipTemplateVO shipTemplateVO) {
        //查询出此商家所有的商品的sku
        List<GoodsDO> goodsDOS = goodsClient.getSellerGoods(shipTemplateVO.getSellerId());
        //循环商品查找sku
        for (GoodsDO goodsDO : goodsDOS) {
            //如果此商品绑定的模板和个更新的模板是同一个模板
            if (goodsDO.getTemplateId().equals(shipTemplateVO.getId())) {
                //获取此商品的所有的sku信息
                List<GoodsSkuVO> goodsSkuVOS = goodsClient.listByGoodsId(goodsDO.getGoodsId());
                //循环更新每一个sku中的模板信息
                for (GoodsSkuVO goodsSkuVO : goodsSkuVOS) {
                    List<String> scripts = (List<String>) cache.get(CachePrefix.SHIP_SCRIPT.getPrefix() + shipTemplateVO.getId());
                    goodsSkuVO.setTemplateId(shipTemplateVO.getId());
                    goodsSkuVO.setScripts(scripts);
                    cache.put(CachePrefix.SKU.getPrefix() + goodsSkuVO.getSkuId(), goodsSkuVO);

                }


            }

        }
    }

}
