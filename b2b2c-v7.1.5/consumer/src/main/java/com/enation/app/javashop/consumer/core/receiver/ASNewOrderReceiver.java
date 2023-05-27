package com.enation.app.javashop.consumer.core.receiver;

import com.enation.app.javashop.consumer.core.event.ASNewOrderEvent;
import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;
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
 * 商家创建换货或补发商品售后服务新订单接收者
 * 针对的是用户申请换货和补发商品的售后服务，商家审核通过后要生成新订单
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-23
 */
@Component
public class ASNewOrderReceiver {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired(required = false)
    private List<ASNewOrderEvent> events;

    /**
     * 创建订单
     *
     * @param orderStatusChangeMsg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = AmqpExchange.AS_SELLER_CREATE_ORDER + "_QUEUE"),
            exchange = @Exchange(value = AmqpExchange.AS_SELLER_CREATE_ORDER, type = ExchangeTypes.FANOUT)
    ))
    public void createOrder(OrderStatusChangeMsg orderStatusChangeMsg) {
        if (events != null) {
            for (ASNewOrderEvent event : events) {
                try {
                    event.orderChange(orderStatusChangeMsg);
                } catch (Exception e) {
                    logger.error("处理商家创建新的换货、补发商品订单出错",e);
                }
            }
        }
    }

}
