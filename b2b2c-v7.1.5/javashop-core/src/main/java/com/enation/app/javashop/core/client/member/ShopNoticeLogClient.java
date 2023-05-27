package com.enation.app.javashop.core.client.member;

import com.enation.app.javashop.core.shop.model.dos.ShopNoticeLogDO;

/**
 * @author fk
 * @version v2.0
 * @Description: 店铺消息模板
 * @date 2018/8/14 10:21
 * @since v7.0.0
 */
public interface ShopNoticeLogClient {

    /**
     * 添加店铺站内消息
     * @param shopNoticeLog 店铺站内消息
     * @return ShopNoticeLog 店铺站内消息
     */
    ShopNoticeLogDO add(ShopNoticeLogDO shopNoticeLog);
}
