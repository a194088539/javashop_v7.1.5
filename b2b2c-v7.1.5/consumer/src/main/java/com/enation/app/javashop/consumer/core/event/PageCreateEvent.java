package com.enation.app.javashop.consumer.core.event;

/**
 * 页面生成事件
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月23日 上午9:52:43
 */
public interface PageCreateEvent {

	/**
	 * 生成
	 * @param choosePages
	 */
	void createPage(String[] choosePages);
}
