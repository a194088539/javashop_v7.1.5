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

import com.enation.app.javashop.consumer.core.event.OrderStatusChangeEvent;
import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;

/**
 * 订单状态改变消费者
 * 
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018年3月23日 上午10:31:42
 */
@Component
public class OrderStatusChangeReceiver {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired(required = false)
	private List<OrderStatusChangeEvent> events;

	/**
	 * 订单状态改变
	 * @param orderMessage
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = AmqpExchange.ORDER_STATUS_CHANGE + "_QUEUE"),
			exchange = @Exchange(value = AmqpExchange.ORDER_STATUS_CHANGE, type = ExchangeTypes.FANOUT)
	))
	public void orderChange(OrderStatusChangeMsg orderMessage) {
		if (events != null) {
			for (OrderStatusChangeEvent event : events) {
				try {
					event.orderChange(orderMessage);
				} catch (Exception e) {
					logger.error("订单状态改变消息出错", e);
				}
			}
		}

	}

}
