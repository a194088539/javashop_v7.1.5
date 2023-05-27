package com.enation.app.javashop.consumer.core.receiver;

import com.enation.app.javashop.consumer.core.event.GoodsPriorityChangeEvent;
import com.enation.app.javashop.core.base.message.GoodsChangeMsg;
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

import java.util.List;

/**
* @author liuyulei
 * @version 1.0
 * @Description: 商品优先级变化消息接收者
 * @date 2019/6/13 17:15
 * @since v7.0
 */
@Component
public class GoodsPriorityChangeReceiver {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired(required = false)
	private List<GoodsPriorityChangeEvent> events;

	/**
	 * 商品变化
	 * 
	 * @param goodsChangeMsg
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = AmqpExchange.GOODS_PRIORITY_CHANGE + "_QUEUE"),
			exchange = @Exchange(value = AmqpExchange.GOODS_PRIORITY_CHANGE, type = ExchangeTypes.FANOUT)
	))
	public void goodsChange(GoodsChangeMsg goodsChangeMsg) {

		if (events != null) {
			for (GoodsPriorityChangeEvent event : events) {
				try {
					event.priorityChange(goodsChangeMsg);
				} catch (Exception e) {
					logger.error("处理商品优先级变化消息出错", e);
				}
			}
		}

	}
}
