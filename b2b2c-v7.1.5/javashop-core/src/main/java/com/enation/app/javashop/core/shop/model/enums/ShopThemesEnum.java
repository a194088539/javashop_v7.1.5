package com.enation.app.javashop.core.shop.model.enums;
/**
 * 
 * 店铺模版枚举
 * @author zhangjiping
 * @version v1.0
 * @since v7.0
 * 2018年3月21日 上午11:04:25
 */
public enum ShopThemesEnum {
	/**
	 * PC模版
	 */
	PC("PC模版"),
	/**
	 * WAP模版
	 */
	WAP("WAP模版");
	
	private String description;

	ShopThemesEnum(String des) {
		this.description = des;
	}

	public String description() {
		return this.description;
	}

	public String value() {
		return this.name();
	}
}
