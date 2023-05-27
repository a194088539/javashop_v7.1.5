package com.enation.app.javashop.core.pagedata.model.enums;

/**
 * 支付客户端类型
 * @author fk
 * @version v6.4
 * @since v6.4 2017年10月17日 上午10:49:25
 */
public enum ClientType {

	/**
	 * pc客户端
	 */
	PC,
	/**
	 * MOBILE
	 */
	MOBILE;

	ClientType() {

	}

	public String value() {
		return this.name();
	}
	

}
