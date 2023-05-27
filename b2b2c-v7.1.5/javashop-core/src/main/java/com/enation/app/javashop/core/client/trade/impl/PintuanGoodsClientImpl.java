package com.enation.app.javashop.core.client.trade.impl;

import com.enation.app.javashop.core.client.trade.PintuanGoodsClient;
import com.enation.app.javashop.core.promotion.pintuan.service.PinTuanSearchManager;
import com.enation.app.javashop.core.promotion.pintuan.service.PintuanGoodsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 拼团默认实现类
 *
 * @author zh
 * @version v7.0
 * @date 19/3/5 下午2:22
 * @since v7.0
 */
@Service
@ConditionalOnProperty(value = "javashop.product", havingValue = "stand")
public class PintuanGoodsClientImpl implements PintuanGoodsClient {

    @Autowired
    private PintuanGoodsManager pintuanGoodsManager;

    @Autowired
    private PinTuanSearchManager pinTuanSearchManager;

    @Override
    public boolean createGoodsIndex(Integer promotionId) {
        return  pintuanGoodsManager.addIndex(promotionId);
    }

    @Override
    public void deleteIndexByGoodsId(Integer goodsId) {
        pinTuanSearchManager.deleteByGoodsId(goodsId);
    }

    @Override
    public void syncIndexByGoodsId(Integer goodsId) {
        pinTuanSearchManager.syncIndexByGoodsId(goodsId);
    }

    @Override
    public void delete(Integer goodsId) {
        this.pintuanGoodsManager.delete(goodsId);
    }
}
