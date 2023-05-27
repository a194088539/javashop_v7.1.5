package com.enation.app.javashop.core.client.trade.impl;

import com.enation.app.javashop.core.client.trade.WechatSmallchangeClient;
import com.enation.app.javashop.core.payment.service.WechatSmallchangeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author fk
 * @version v2.0
 * @Description: 微信零钱接口client
 * @date 2018/8/13 16:01
 * @since v7.0.0
 */
@Service
@ConditionalOnProperty(value = "javashop.product", havingValue = "stand")
public class WechatSmallchangeClientDefaultImpl implements WechatSmallchangeClient {

    @Autowired
    private WechatSmallchangeManager wechatSmallchangeManager;

    @Override
    public boolean autoSend(String openId, Double price, String ip, String sn) {

        return wechatSmallchangeManager.autoSend(openId, price, ip, sn);
    }
}
