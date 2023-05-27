package com.enation.app.javashop.consumer.core.event;

import com.enation.app.javashop.core.base.message.ShopStatusChangeMsg;

/**
 * 店铺状态变更
 *
 * @author liushuai
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/9/9 下午11:04
 */
public interface ShopStatusChangeEvent {

    /**
     * 状态变更
     *
     * @param shopStatusChangeMsg 店铺信息
     */
    void changeStatus(ShopStatusChangeMsg shopStatusChangeMsg);
}
