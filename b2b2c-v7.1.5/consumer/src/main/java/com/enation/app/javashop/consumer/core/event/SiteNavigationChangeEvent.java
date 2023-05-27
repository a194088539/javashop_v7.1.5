package com.enation.app.javashop.consumer.core.event;


/**
 * 站点导航变化事件
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018年6月13日 14:36:07
 */
public interface SiteNavigationChangeEvent {

	/**
	 * 站点导航变化后执行的方法
	 * @param clientType PC端 或者 MOBILE端
	 */
	void navigationChange(String clientType);
}
