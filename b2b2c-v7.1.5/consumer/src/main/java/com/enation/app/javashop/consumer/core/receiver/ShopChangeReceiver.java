package com.enation.app.javashop.consumer.core.receiver;

import com.enation.app.javashop.consumer.core.event.ShopChangeEvent;
import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.shop.model.vo.ShopChangeMsg;
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
 * 店铺变更消费者
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月23日 上午10:32:08
 */
@Component
public class ShopChangeReceiver {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired(required = false)
    private List<ShopChangeEvent> events;

    /**
     * 店铺变更
     *
     * @param shopNameChangeMsg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = AmqpExchange.SHOP_CHANGE + "_ROUTING"),
            exchange = @Exchange(value = AmqpExchange.SHOP_CHANGE, type = ExchangeTypes.FANOUT)
    ))
    public void shopChange(ShopChangeMsg shopNameChangeMsg) {

        if (events != null) {
            for (ShopChangeEvent event : events) {
                try {
                    event.shopChange(shopNameChangeMsg);
                } catch (Exception e) {
                    logger.error("店铺信息变更消息出错", e);
                }
            }
        }

    }

}
