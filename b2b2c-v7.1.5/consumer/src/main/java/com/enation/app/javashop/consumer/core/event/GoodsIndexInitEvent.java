package com.enation.app.javashop.consumer.core.event;

/**
 * 商品索引生成事件
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月23日 上午10:24:59
 */
public interface GoodsIndexInitEvent {

	/**
	 * 创建商品索引
	 */
    void createGoodsIndex();
}
