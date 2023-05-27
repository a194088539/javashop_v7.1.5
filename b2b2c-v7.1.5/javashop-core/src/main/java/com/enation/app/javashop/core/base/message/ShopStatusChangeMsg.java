package com.enation.app.javashop.core.base.message;

import com.enation.app.javashop.core.shop.model.enums.ShopStatusEnum;

import java.io.Serializable;

/**
 * 店铺状态修改消息
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018/9/9 下午11:01
 * @Description:
 *
 */
public class ShopStatusChangeMsg implements Serializable{


	private static final long serialVersionUID = 958600762323161940L;
	/**
	 * 店铺id
	 */
	private Integer sellerId;

	/**
	 * 操作类型
	 */
	private ShopStatusEnum statusEnum;


	public ShopStatusChangeMsg(Integer sellerId, ShopStatusEnum statusEnum) {
		super();
		this.sellerId = sellerId;
		this.statusEnum = statusEnum;
	}

	public Integer getSellerId() {
		return sellerId;
	}

	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}

	public ShopStatusEnum getStatusEnum() {
		return statusEnum;
	}

	public void setStatusEnum(ShopStatusEnum statusEnum) {
		this.statusEnum = statusEnum;
	}
}
