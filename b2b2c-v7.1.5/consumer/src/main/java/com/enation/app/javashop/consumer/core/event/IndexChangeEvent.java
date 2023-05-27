package com.enation.app.javashop.consumer.core.event;

import com.enation.app.javashop.core.base.message.CmsManageMsg;

/**
 * 商品分类变化事件
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月23日 上午10:22:23
 */
public interface IndexChangeEvent {

	/**
	 * 创建首页
	 * @param cmsManageMsg
	 */
    void createIndexPage(CmsManageMsg cmsManageMsg);
}
