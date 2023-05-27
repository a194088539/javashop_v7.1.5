package com.enation.app.javashop.consumer.core.receiver;

import com.enation.app.javashop.consumer.core.event.AfterSaleChangeEvent;
import com.enation.app.javashop.core.base.message.AfterSaleChangeMessage;
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
 * 售后服务单状态变化接收者
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-24
 */
@Component
public class AfterSaleChangeReceiver {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired(required = false)
    private List<AfterSaleChangeEvent> events;


    /**
     * 处理售后服务单状态变化消息
     * @param afterSaleChangeMessage
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = AmqpExchange.AS_STATUS_CHANGE + "_QUEUE"),
            exchange = @Exchange(value = AmqpExchange.AS_STATUS_CHANGE, type = ExchangeTypes.FANOUT)
    ))
    public void afterSaleChange(AfterSaleChangeMessage afterSaleChangeMessage) {
        if (events != null) {
            for (AfterSaleChangeEvent event : events) {
                try {
                    event.afterSaleChange(afterSaleChangeMessage);
                } catch (Exception e) {
                    logger.error("处理售后服务单状态变化消息出错",e);
                }
            }
        }
    }

}
