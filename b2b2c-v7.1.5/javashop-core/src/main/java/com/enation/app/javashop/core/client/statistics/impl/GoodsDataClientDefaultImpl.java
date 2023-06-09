package com.enation.app.javashop.core.client.statistics.impl;

import com.enation.app.javashop.core.client.statistics.GoodsDataClient;
import com.enation.app.javashop.core.statistics.model.dto.GoodsData;
import com.enation.app.javashop.core.statistics.service.GoodsDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * GoodsDataClientDefaultImpl
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-08-14 下午2:37
 */
@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class GoodsDataClientDefaultImpl implements GoodsDataClient {

    @Autowired
    private GoodsDataManager goodsDataManager;
    /**
     * 新增商品
     *
     * @param goodsIds 商品id
     */
    @Override
    public void addGoods(Integer[] goodsIds) {
        goodsDataManager.addGoods(goodsIds);
    }

    /**
     * 修改商品
     *
     * @param goodsIds 商品id
     */
    @Override
    public void updateGoods(Integer[] goodsIds) {
        goodsDataManager.updateGoods(goodsIds);
    }

    /**
     * 删除商品
     *
     * @param goodsIds 商品id
     */
    @Override
    public void deleteGoods(Integer[] goodsIds) {
        goodsDataManager.deleteGoods(goodsIds);
    }

    /**
     * 修改商品收藏数量
     *
     * @param goodsData
     */
    @Override
    public void updateCollection(GoodsData goodsData) {
        goodsDataManager.updateCollection(goodsData);
    }

    /**
     * 下架所有商品
     *
     * @param sellerId
     */
    @Override
    public void underAllGoods(Integer sellerId) {
        goodsDataManager.underAllGoods(sellerId);
    }


}
