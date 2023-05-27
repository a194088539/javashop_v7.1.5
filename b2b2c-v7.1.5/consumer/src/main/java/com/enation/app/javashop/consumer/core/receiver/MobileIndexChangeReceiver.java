package com.enation.app.javashop.consumer.core.receiver;

import java.util.List;

import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.javashop.consumer.core.event.MobileIndexChangeEvent;
import com.enation.app.javashop.core.base.message.CmsManageMsg;

/**
 * 移动端首页生成
 * 
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018年3月23日 上午10:31:27
 */
@Component
public class MobileIndexChangeReceiver {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired(required = false)
	private List<MobileIndexChangeEvent> events;

	/**
	 * 生成首页
	 * @param operation
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = AmqpExchange.MOBILE_INDEX_CHANGE + "_QUEUE"),
			exchange = @Exchange(value = AmqpExchange.MOBILE_INDEX_CHANGE, type = ExchangeTypes.FANOUT)
	))
	public void mobileIndexChange(CmsManageMsg operation) {

		if (events != null) {
			for (MobileIndexChangeEvent event : events) {
				try {
					event.mobileIndexChange(operation);
				} catch (Exception e) {
					logger.error("移动端页面生成消息出错", e);
				}
			}
		}
	}

}
