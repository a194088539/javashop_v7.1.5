package com.enation.app.javashop.core.shop.model.enums;
/**
 * 
 * 店铺状态枚举
 * @author zhangjiping
 * @version v1.0
 * @since v7.0
 * 2018年3月21日 上午11:04:25
 */
public enum ShopStatusEnum {
	/**
	 * 开启中
	 */
	OPEN("开启中"),
	/**
	 * 店铺关闭
	 */
	CLOSED("店铺关闭"),
	/**
	 * 申请开店
	 */
	APPLY("申请开店"),
	/**
	 * 审核拒绝
	 */
	REFUSED("审核拒绝"),
	/**
	 * 申请中
	 */
	APPLYING("申请中");
	
	private String description;

	ShopStatusEnum(String des) {
		this.description = des;
	}

	public String description() {
		return this.description;
	}

	public String value() {
		return this.name();
	}
}
