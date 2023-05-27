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

import com.enation.app.javashop.consumer.core.event.OnlinePayEvent;

/**
 * 在线支付
 * 
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018年3月23日 上午10:31:35
 */
@Component
public class OnlinePayReceiver {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired(required = false)
	private List<OnlinePayEvent> events;

	/**
	 * 在线支付
	 * 
	 * @param memberId
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = AmqpExchange.ONLINE_PAY + "_QUEUE"),
			exchange = @Exchange(value = AmqpExchange.ONLINE_PAY, type = ExchangeTypes.FANOUT)
	))
	public void onlinePay(Integer memberId) {
		if (events != null) {
			for (OnlinePayEvent event : events) {
				try {
					event.onlinePay(memberId);
				} catch (Exception e) {
					logger.error("在线支付出错", e);
				}
			}
		}

	}
}
