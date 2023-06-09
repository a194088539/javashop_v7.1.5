package com.enation.app.javashop.core.client.distribution.impl;

import com.enation.app.javashop.core.client.distribution.DistributionGoodsClient;
import com.enation.app.javashop.core.distribution.model.dos.DistributionGoods;
import com.enation.app.javashop.core.distribution.service.DistributionGoodsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * DistributionGoodsClientDefaultImpl
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-08-14 下午1:32
 */
@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class DistributionGoodsClientDefaultImpl implements DistributionGoodsClient {
    @Autowired
    private DistributionGoodsManager distributionGoodsManager;


    /**
     * 获取某商品设置
     * @param goodsId
     * @return
     */
    @Override
    public DistributionGoods getModel(Integer goodsId) {
        return distributionGoodsManager.getModel(goodsId);
    }

    /**
     * 修改分销商品提现设置
     *
     * @param distributionGoods
     * @return
     */
    @Override
    public DistributionGoods edit(DistributionGoods distributionGoods) {
        return distributionGoodsManager.edit(distributionGoods);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {
        distributionGoodsManager.delete(id);
    }
}
