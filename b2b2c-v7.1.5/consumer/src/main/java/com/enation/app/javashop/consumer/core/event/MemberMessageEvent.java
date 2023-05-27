package com.enation.app.javashop.consumer.core.event;

/**
 * 站内消息
 * @author fk
 * @version v2.0
 * @since v7.0 2018年3月23日 上午10:16:59
 */
public interface MemberMessageEvent {

	/**
	 * 会员站内消息消费
	 * 
	 * @param messageId
	 */
    void memberMessage(int messageId);
}
