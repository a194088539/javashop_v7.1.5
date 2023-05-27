package com.enation.app.javashop.consumer.shop.shop;

import com.enation.app.javashop.consumer.core.event.ShopChangeEvent;
import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.trade.ExchangeGoodsClient;
import com.enation.app.javashop.core.client.trade.PromotionGoodsClient;
import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.model.enums.GoodsType;
import com.enation.app.javashop.core.promotion.exchange.model.dos.ExchangeDO;
import com.enation.app.javashop.core.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.javashop.core.shop.model.vo.ShopChangeMsg;
import com.enation.app.javashop.core.shop.model.vo.ShopVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 是否为自营变化处理
 *
 * @author zh
 * @version v7.0
 * @date 19/3/27 下午2:26
 * @since v7.0
 */
@Component
public class ShopSelfOperatedChanageConsumer implements ShopChangeEvent {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private ExchangeGoodsClient exchangeGoodsClient;

    @Autowired
    private PromotionGoodsClient promotionGoodsClient;


    protected final Log logger = LogFactory.getLog(this.getClass());

    @Override
    public void shopChange(ShopChangeMsg shopChangeMsg) {
        try {
            //原店铺数据
            ShopVO originalShop = shopChangeMsg.getOriginalShop();
            //更新后店铺数据
            ShopVO shop = shopChangeMsg.getNewShop();
            //如果原店铺是自营改变为普通商家则进行处理
            if (originalShop.getSelfOperated().equals(1) && shop.getSelfOperated().equals(0)) {
                //查询此店铺的所有积分商品
                List<GoodsDO> goods = goodsClient.listPointGoods(shop.getShopId());
                for (GoodsDO goodsDO : goods) {
                    ExchangeDO exchangeDO = exchangeGoodsClient.getModelByGoods(goodsDO.getGoodsId());
                    if (exchangeDO != null) {
                        //删除此店铺的所有积分商品
                        exchangeGoodsClient.del(goodsDO.getGoodsId());
                        //删除此店铺的所有积分活动
                        promotionGoodsClient.delPromotionGoods(goodsDO.getGoodsId(), PromotionTypeEnum.EXCHANGE.name(), exchangeDO.getExchangeId());
                    }
                }
                //更改所有的商品为普通商品
                goodsClient.updateGoodsType(shop.getShopId(), GoodsType.NORMAL.name());
            }
        } catch (Exception e) {
            logger.error("处理店铺类型改变出错" + e.getMessage());
        }
    }
}
