package com.enation.app.javashop.core.statistics.service;

import com.enation.app.javashop.core.statistics.model.dto.GoodsData;

/**
 * 商品收集manager
 *
 * @author chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/8 下午4:11
 */

public interface GoodsDataManager {
    /**
     * 新增商品
     *
     * @param goodsIds 商品id
     */
    void addGoods(Integer[] goodsIds);

    /**
     * 修改商品
     *
     * @param goodsIds 商品id
     */
    void updateGoods(Integer[] goodsIds);

    /**
     * 删除商品
     *
     * @param goodsIds 商品id
     */
    void deleteGoods(Integer[] goodsIds);

    /**
     * 修改商品收藏数量
     * @param goodsData
     */
    void updateCollection(GoodsData goodsData);


    /**
     * 获取商品
     * @param goodsId
     * @return
     */
    GoodsData get(Integer goodsId);

    /**
     * 下架所有商品
     * @param sellerId
     */
    void underAllGoods(Integer sellerId);

}
