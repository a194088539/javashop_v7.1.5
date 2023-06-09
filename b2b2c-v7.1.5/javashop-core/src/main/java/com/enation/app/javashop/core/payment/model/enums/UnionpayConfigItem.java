package com.enation.app.javashop.core.payment.model.enums;

/**
 * @author fk
 * @version v2.0
 * @Description: 中国银联客户端使用配置参数
 * @date 2018/4/11 17:05
 * @since v7.0.0
 */
public enum UnionpayConfigItem {

	/**
	 * 中国银联商户代码
	 */
	mer_id("中国银联商户代码"),
	/**
	 * 签名证书路径
	 */
	sign_cert("签名证书路径"),
	/**
	 * 签名证书密码
	 */
	pwd("签名证书密码"),
	/**
	 * 验证签名证书目录
	 */
	validate_cert("验证签名证书目录"),
	/**
	 * 敏感信息加密证书路径
	 */
	encrypt_cert("敏感信息加密证书路径");

	private String text;

	UnionpayConfigItem(String text) {
		this.text = text;

	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String value() {
		return this.name();
	}
	

}
