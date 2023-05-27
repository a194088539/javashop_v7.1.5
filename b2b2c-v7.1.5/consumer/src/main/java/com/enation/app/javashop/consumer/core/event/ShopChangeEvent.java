package com.enation.app.javashop.consumer.core.event;

import com.enation.app.javashop.core.shop.model.vo.ShopChangeMsg;

/**
 * 店铺变更
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月23日 上午10:26:32
 */
public interface ShopChangeEvent {

    /**
     * 店铺名称改变消息
     *
     * @param shopChangeMsg 店铺名称改变消息
     */
    void shopChange(ShopChangeMsg shopChangeMsg);
}
