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

import com.enation.app.javashop.consumer.core.event.GoodsIndexInitEvent;

/**
 * 商品索引
 * 
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018年3月23日 上午10:30:12
 */
@Component
public class GoodsIndexInitReceiver {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired(required = false)
	private List<GoodsIndexInitEvent> events;

	/**
	 * 初始化商品索引
	 * 
	 * @param str
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = AmqpExchange.INDEX_CREATE + "_QUEUE"),
			exchange = @Exchange(value = AmqpExchange.INDEX_CREATE, type = ExchangeTypes.FANOUT)
	))
	public void initGoodsIndex(String str) {

		if (events != null) {
			for (GoodsIndexInitEvent event : events) {
				try {
					event.createGoodsIndex();
				} catch (Exception e) {
					logger.error("初始化商品索引出错", e);
				}
			}
		}

	}

}
