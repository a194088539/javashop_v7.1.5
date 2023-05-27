package com.enation.app.javashop.core.client.statistics.impl;

import com.enation.app.javashop.core.client.statistics.ShopDataClient;
import com.enation.app.javashop.core.statistics.model.dto.ShopData;
import com.enation.app.javashop.core.statistics.service.ShopDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * ShopDataClientDefaultImpl
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-08-14 下午2:44
 */
@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class ShopDataClientDefaultImpl implements ShopDataClient {
    @Autowired
    private ShopDataManager shopDataManager;
    /**
     * 修改店铺收藏数量
     *
     * @param shopData
     */
    @Override
    public void updateCollection(ShopData shopData) {
        shopDataManager.updateCollection(shopData);
    }

    @Override
    public void updateShopData(ShopData shopData) {
        shopDataManager.updateShopData(shopData);
    }

    /**
     * 添加店铺收藏数量
     *
     * @param shopData
     */
    @Override
    public void add(ShopData shopData) {
        shopDataManager.add(shopData);
    }
}
