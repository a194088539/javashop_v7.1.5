package com.enation.app.javashop.consumer.core.receiver;

import com.enation.app.javashop.consumer.core.event.SiteNavigationChangeEvent;
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
 * 站点导航栏变化
 * 
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018年3月23日 上午10:32:15
 */
@Component
public class SiteNavigationChangeReceiver {

	@Autowired(required=false)
	private List<SiteNavigationChangeEvent> events;

	protected final Log logger = LogFactory.getLog(this.getClass());

	/**
	 * 导航栏变化
	 * @param clientType PC端 或者 MOBILE端
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = AmqpExchange.SITE_NAVIGATION_CHANGE + "_QUEUE"),
			exchange = @Exchange(value = AmqpExchange.SITE_NAVIGATION_CHANGE, type = ExchangeTypes.FANOUT)
	))
	public void change(String clientType){
		if(events!=null){
			for(SiteNavigationChangeEvent event : events){
				try {
					event.navigationChange(clientType);
				} catch (Exception e) {
					logger.error("导航栏变化出错", e);
				}
			}
		}
	}
}
