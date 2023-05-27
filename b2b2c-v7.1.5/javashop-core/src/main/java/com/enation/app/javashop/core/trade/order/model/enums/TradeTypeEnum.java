package com.enation.app.javashop.core.trade.order.model.enums;

/**
 * 交易类型
 * @author Snow
 * @version 1.0
 * @since v7.0.0
 * 2017年4月5日下午5:12:55
 */
public enum TradeTypeEnum {

	/** 订单 */
	ORDER("订单"),

	/** 交易 */
	TRADE("交易");

	private String description;

	TradeTypeEnum(String description){
		  this.description=description;
	}
}
