package com.enation.app.javashop.core.trade.order.service;

import com.enation.app.javashop.core.trade.order.model.dos.TradeDO;

/**
 * 交易查询接口
 * @author Snow create in 2018/5/21
 * @version v2.0
 * @since v7.0.0
 */
public interface TradeQueryManager {

    /**
     * 根据交易单号查询交易对象
     * @param tradeSn
     * @return
     */
    TradeDO getModel(String tradeSn);

}
