package com.enation.app.javashop.consumer.core.event;

import com.enation.app.javashop.core.base.message.GoodsChangeMsg;

/**
 * @author liuyulei
 * @version 1.0
 * @Description: TODO商品优先级变化
 * @date 2019/6/13 17:15
 * @since v7.0
 */
public interface GoodsPriorityChangeEvent {


    /**
     * 商品优先级变化
     * @param goodsChangeMsg
     */
    void priorityChange(GoodsChangeMsg goodsChangeMsg);
}
