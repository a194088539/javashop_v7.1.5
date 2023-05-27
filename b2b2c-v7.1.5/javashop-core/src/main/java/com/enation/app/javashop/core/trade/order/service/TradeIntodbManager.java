package com.enation.app.javashop.core.trade.order.service;

import com.enation.app.javashop.core.trade.cart.model.enums.CheckedWay;
import com.enation.app.javashop.core.trade.order.model.vo.TradeVO;

/**
 * 交易入库业务接口
 *
 * @author Snow create in 2018/5/9
 * @version v2.0
 * @since v7.0.0
 */
public interface TradeIntodbManager {

    /**
     * 入库处理
     * @param tradeVO
     * @param way 清除缓存源，BUY_BOW,立即购买，CART,购物车
     */
    void intoDB(TradeVO tradeVO, CheckedWay way);


}
