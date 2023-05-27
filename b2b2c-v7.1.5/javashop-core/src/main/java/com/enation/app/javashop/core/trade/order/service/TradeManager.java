package com.enation.app.javashop.core.trade.order.service;

import com.enation.app.javashop.core.trade.cart.model.enums.CheckedWay;
import com.enation.app.javashop.core.trade.order.model.vo.TradeVO;

/**
 * 交易管理
 * @author Snow create in 2018/4/9
 * @version v2.0
 * @since v7.0.0
 */
public interface TradeManager {

    /**
     * 交易创建
     * @param clientType  客户的类型
     * @param way 检查获取方式
     * @return
     */
    TradeVO createTrade(String clientType, CheckedWay way);



}
