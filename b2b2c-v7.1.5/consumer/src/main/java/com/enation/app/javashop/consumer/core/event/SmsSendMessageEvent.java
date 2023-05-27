package com.enation.app.javashop.consumer.core.event;


import com.enation.app.javashop.core.base.model.vo.SmsSendVO;

/**
 * 发送短信
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月23日 上午10:26:41
 */
public interface SmsSendMessageEvent {

	/**
	 * 发送短信
	 * @param smsSendVO	短信发送vo
	 */
	void send(SmsSendVO smsSendVO);
}
