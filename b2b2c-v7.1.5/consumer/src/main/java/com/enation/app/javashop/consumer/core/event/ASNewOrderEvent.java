package com.enation.app.javashop.consumer.core.event;

import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;

/**
 * 商家创建换货或补发商品售后服务新订单事件
 * 针对的是用户申请换货和补发商品的售后服务，商家审核通过后要生成新订单
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-23
 */
public interface ASNewOrderEvent {

    /**
     * 商家创建订单后执行
     * @param orderStatusChangeMsg
     */
    void orderChange(OrderStatusChangeMsg orderStatusChangeMsg);

}
