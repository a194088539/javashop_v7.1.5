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

import com.enation.app.javashop.consumer.core.event.SendEmailEvent;
import com.enation.app.javashop.core.base.model.vo.EmailVO;

/**
 * 发送邮件
 * 
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018年3月23日 上午10:31:58
 */
@Component
public class SendEmailReceiver {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired(required = false)
	private List<SendEmailEvent> events;

	/**
	 * 发送邮件
	 * 
	 * @param emailVO
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = AmqpExchange.EMAIL_SEND_MESSAGE + "_QUEUE"),
			exchange = @Exchange(value = AmqpExchange.EMAIL_SEND_MESSAGE, type = ExchangeTypes.FANOUT)
	))
	public void sendEmail(EmailVO emailVO) {
		if (events != null) {
			for (SendEmailEvent event : events) {
				try {
					event.sendEmail(emailVO);
				} catch (Exception e) {
					logger.error("发送邮件出错", e);
				}
			}
		}

	}
}
