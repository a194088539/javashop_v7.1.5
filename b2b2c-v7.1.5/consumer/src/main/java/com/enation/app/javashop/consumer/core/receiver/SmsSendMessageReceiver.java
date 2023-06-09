package com.enation.app.javashop.consumer.core.receiver;

import java.util.List;

import com.enation.app.javashop.consumer.core.event.SmsSendMessageEvent;
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

import com.enation.app.javashop.core.base.model.vo.SmsSendVO;

/**
 * 发送短信
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018年3月23日 上午10:32:15
 */
@Component
public class SmsSendMessageReceiver {

    @Autowired(required = false)
    private List<SmsSendMessageEvent> events;

    protected final Log logger = LogFactory.getLog(this.getClass());

    /**
     * 短信消息
     *
     * @param smsSendVO 短信vo
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = AmqpExchange.SEND_MESSAGE + "_QUEUE"),
            exchange = @Exchange(value = AmqpExchange.SEND_MESSAGE, type = ExchangeTypes.FANOUT)
    ))
    public void sendMessage(SmsSendVO smsSendVO) {
        if (events != null) {
            for (SmsSendMessageEvent event : events) {
                try {
                    event.send(smsSendVO);
                } catch (Exception e) {
                    logger.error("发送短信出错", e);
                }
            }
        }
    }
}
