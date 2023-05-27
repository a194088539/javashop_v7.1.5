package com.enation.app.javashop.core.client.member.impl;

import com.enation.app.javashop.core.client.member.ShopNoticeLogClient;
import com.enation.app.javashop.core.shop.model.dos.ShopNoticeLogDO;
import com.enation.app.javashop.core.shop.service.ShopNoticeLogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author fk
 * @version v2.0
 * @Description: 店铺消息
 * @date 2018/8/14 10:22
 * @since v7.0.0
 */
@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class ShopNoticeLogClientDefaultImpl implements ShopNoticeLogClient {


    @Autowired
    private ShopNoticeLogManager shopNoticeLogManager;

    @Override
    public ShopNoticeLogDO add(ShopNoticeLogDO shopNoticeLog) {
        return shopNoticeLogManager.add(shopNoticeLog);
    }
}
