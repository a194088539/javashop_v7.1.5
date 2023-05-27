package com.enation.app.javashop.consumer.core.event;

import com.enation.app.javashop.core.base.message.CategoryChangeMsg;

/**
 * 商品分类变化事件
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月23日 上午10:24:31
 */
public interface CategoryChangeEvent {

	/**
	 * 商品分类变化后需要执行的方法
	 * @param categoryChangeMsg
	 */
    void categoryChange(CategoryChangeMsg categoryChangeMsg);
}
