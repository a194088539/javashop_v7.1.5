package com.enation.app.javashop.consumer.core.event;

import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;

/**
 * 订单状态变化事件
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月23日 上午9:52:32
 */
public interface OrderStatusChangeEvent {

    /**
     * 订单状态改变，需要执行的操作
     *
     * @param orderMessage
     */
    void orderChange(OrderStatusChangeMsg orderMessage);
}
