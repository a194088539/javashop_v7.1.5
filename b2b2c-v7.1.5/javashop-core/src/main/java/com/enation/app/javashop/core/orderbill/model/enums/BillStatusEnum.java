package com.enation.app.javashop.core.orderbill.model.enums;

/**
 * 
 * 结算单状态枚举
 * @author yanlin
 * @version v1.0
 * @since v7.0.0
 * @date 2018年4月15日 上午10:34:30
 */
public enum BillStatusEnum {

	/**
	 * 未确认状态
	 */
	NEW("未确认"),
	/**
	 * 已出账状态
	 */
	OUT("已出账"),
	/**
	 * 已对账
	 */
	RECON("已对账"),
	/**
	 * 已审核
	 */
	PASS("已审核"),
	/**
	 * 已付款
	 */
	PAY("已付款"),
	/**
	 * 已完成
	 */
	COMPLETE("已完成");
	private String description;

	BillStatusEnum(String description) {
		this.description = description;
	}

	public String description() {
		return description;
	}

}
