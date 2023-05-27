package com.enation.app.javashop.consumer.core.receiver;


import com.enation.app.javashop.consumer.core.event.PintuanSuccessEvent;
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
 * 拼团消息
 *
 * @author fk
 * @version v2.0
 * @since v7.1.4 2019年6月20日 上午10:31:49
 */
@Component
public class PintuanReceiver {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired(required = false)
    private List<PintuanSuccessEvent> events;

    /**
     * 拼团成功消息
     * @param pintuanOrderId
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = AmqpExchange.PINTUAN_SUCCESS + "_QUEUE"),
            exchange = @Exchange(value = AmqpExchange.PINTUAN_SUCCESS, type = ExchangeTypes.FANOUT)
    ))
    public void  receiveMessage(Integer pintuanOrderId){

        if (events != null) {
            for (PintuanSuccessEvent event : events) {
                try {
                    event.success(pintuanOrderId);
                } catch (Exception e) {
                    logger.error("拼团成功消息", e);
                }
            }
        }

    }

}
