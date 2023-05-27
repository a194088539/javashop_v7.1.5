package com.enation.app.javashop.consumer.core.event;

import com.enation.app.javashop.core.trade.order.model.vo.TradeVO;

/**
 * 交易入库事件
 * @author Snow create in 2018/6/26
 * @version v2.0
 * @since v7.0.0
 */
public interface TradeIntoDbEvent {


    /**
     * 交易入库
     * @param tradeVO
     */
    void onTradeIntoDb(TradeVO tradeVO);
}
