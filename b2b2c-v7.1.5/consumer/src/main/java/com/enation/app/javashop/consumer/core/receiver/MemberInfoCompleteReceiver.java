package com.enation.app.javashop.consumer.core.receiver;

import java.util.List;

import com.enation.app.javashop.consumer.core.event.MemberInfoCompleteEvent;
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

/**
 * 会员完善个人信息
 * 
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018年3月23日 上午10:30:58
 */
@Component
public class MemberInfoCompleteReceiver {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired(required = false)
	private List<MemberInfoCompleteEvent> events;

	/**
	 * 会员完善个人信息
	 * 
	 * @param memberId
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = AmqpExchange.MEMBER_INFO_COMPLETE + "_QUEUE"),
			exchange = @Exchange(value = AmqpExchange.MEMBER_INFO_COMPLETE, type = ExchangeTypes.FANOUT)
	))
	public void memberInfoComplete(Integer memberId) {

		if (events != null) {
			for (MemberInfoCompleteEvent event : events) {
				try {
					event.memberInfoComplete(memberId);
				} catch (Exception e) {
					logger.error("会员完善信息出错", e);
				}
			}
		}

	}
}
