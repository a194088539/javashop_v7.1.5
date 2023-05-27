package com.enation.app.javashop.core.client.statistics;

import com.enation.app.javashop.core.statistics.model.dto.ShopData;

/**
 * 店铺收藏数据管理
 *
 * @author chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/8 下午4:11
 */
public interface ShopDataClient {

    /**
     * 修改店铺收藏数量
     * @param shopData
     */
    void updateCollection(ShopData shopData);

    /**
     * 更新店铺信息
     * @param shopData
     */
    void updateShopData(ShopData shopData);

    /**
     * 添加店铺数据
     * @param shopData
     */
    void add(ShopData shopData);


}
