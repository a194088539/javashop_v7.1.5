package com.enation.app.javashop.core.trade.order.service;

/**
 * 交易价格业务接口
 * @author kingapex
 * @version v2.0
 * @since v7.0.0
 * 2017年3月23日上午10:01:30
 */
public interface TradePriceManager {


	/**
	 * 未付款的订单，商家修改订单金额，同时修改交易价格
	 * @param tradeSn		交易编号
	 * @param tradePrice
	 * @param discountPrice
	 */
	void updatePrice(String tradeSn, Double tradePrice, Double discountPrice);

}
