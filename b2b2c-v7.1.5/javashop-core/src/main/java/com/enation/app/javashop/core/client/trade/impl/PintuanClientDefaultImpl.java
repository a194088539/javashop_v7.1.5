package com.enation.app.javashop.core.client.trade.impl;

import com.enation.app.javashop.core.client.trade.PintuanClient;
import com.enation.app.javashop.core.promotion.pintuan.model.Pintuan;
import com.enation.app.javashop.core.promotion.pintuan.service.PintuanManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PintuanClientDefaultImpl
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2019-02-18 上午11:44
 */
@Service
@ConditionalOnProperty(value = "javashop.product", havingValue = "stand")
public class PintuanClientDefaultImpl implements PintuanClient {
    @Autowired
    private PintuanManager pintuanManager;

    /**
     * 获取拼团
     *
     * @param id 拼团主键
     * @return Pintuan  拼团
     */
    @Override
    public Pintuan getModel(Integer id) {
        return pintuanManager.getModel(id);
    }

    /**
     * 停止一个活动
     *
     * @param promotionId
     */
    @Override
    public void closePromotion(Integer promotionId) {
        pintuanManager.closePromotion(promotionId);
    }

    /**
     * 开始一个活动
     *
     * @param promotionId
     */
    @Override
    public void openPromotion(Integer promotionId) {
        pintuanManager.openPromotion(promotionId);
    }

    @Override
    public List<Pintuan> get(String status) {
        return pintuanManager.get(status);
    }
}
