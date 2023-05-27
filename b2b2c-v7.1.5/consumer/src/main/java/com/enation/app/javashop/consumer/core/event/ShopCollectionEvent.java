package com.enation.app.javashop.consumer.core.event;

/**
 * 店铺变更
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月23日 上午10:26:32
 */
public interface ShopCollectionEvent {

	/**
	 * 店铺变更的消费
	 * @param shopId
	 */
	void shopChange(Integer shopId);
}
