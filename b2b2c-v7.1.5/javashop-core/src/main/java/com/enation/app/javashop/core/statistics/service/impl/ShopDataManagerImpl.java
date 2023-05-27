package com.enation.app.javashop.core.statistics.service.impl;

import com.enation.app.javashop.core.statistics.model.dto.ShopData;
import com.enation.app.javashop.core.statistics.service.ShopDataManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * ShopDataManagerImpl
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-07-24 上午6:05
 */
@Service
public class ShopDataManagerImpl implements ShopDataManager {

    @Autowired
    @Qualifier("sssDaoSupport")
    private DaoSupport daoSupport;

    /**
     * 修改店铺收藏数量
     *
     * @param shopData
     */
    @Override
    public void updateCollection(ShopData shopData) {
        daoSupport.execute("update es_sss_shop_data set favorite_num = ? where seller_id = ?", shopData.getFavoriteNum(), shopData.getSellerId());
    }

    @Override
    public void updateShopData(ShopData shopData) {
        Map where = new HashMap<>(2);
        where.put("seller_id", shopData.getSellerId());

        daoSupport.update("es_sss_shop_data", shopData, where);
    }

    /**
     * 添加店铺数据
     *
     * @param shopData
     */
    @Override
    public void add(ShopData shopData) {
        daoSupport.insert("es_sss_shop_data", shopData);

    }

    /**
     * 获取商品数据
     *
     * @param shopId
     * @return
     */
    @Override
    public ShopData get(Integer shopId) {
        return daoSupport.queryForObject("select * from es_sss_shop_data where seller_id = ?", ShopData.class, shopId);
    }
}
