package com.enation.app.javashop.consumer.shop.sss;

import com.enation.app.javashop.consumer.core.event.ShopCollectionEvent;
import com.enation.app.javashop.consumer.core.event.ShopStatusChangeEvent;
import com.enation.app.javashop.core.base.message.ShopStatusChangeMsg;
import com.enation.app.javashop.core.client.member.ShopClient;
import com.enation.app.javashop.core.client.statistics.GoodsDataClient;
import com.enation.app.javashop.core.client.statistics.ShopDataClient;
import com.enation.app.javashop.core.shop.model.enums.ShopStatusEnum;
import com.enation.app.javashop.core.shop.model.vo.ShopVO;
import com.enation.app.javashop.core.statistics.model.dto.ShopData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 店铺数据收集
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-07-23 下午5:50
 */
@Component
public class DataShopConsumer implements ShopCollectionEvent, ShopStatusChangeEvent {

    protected final Log logger = LogFactory.getLog(this.getClass());


    @Autowired
    private ShopClient shopClient;

    @Autowired
    private ShopDataClient shopDataClient;

    @Autowired
    private GoodsDataClient goodsDataClient;

    /**
     * 店铺变更
     *
     * @param shopId
     */
    @Override
    public void shopChange(Integer shopId) {

        try {
            ShopVO shop = shopClient.getShop(shopId);
            ShopData shopData = new ShopData(shop);
            this.shopDataClient.add(shopData);
        } catch (Exception e) {
            logger.error("店铺消息收集失败", e);
        }
    }

    /**
     * 状态变更
     *
     * @param shopStatusChangeMsg 店铺信息
     */
    @Override
    public void changeStatus(ShopStatusChangeMsg shopStatusChangeMsg) {
        if (shopStatusChangeMsg.getStatusEnum().equals(ShopStatusEnum.CLOSED)) {
            goodsDataClient.underAllGoods(shopStatusChangeMsg.getSellerId());
        }
    }
}
