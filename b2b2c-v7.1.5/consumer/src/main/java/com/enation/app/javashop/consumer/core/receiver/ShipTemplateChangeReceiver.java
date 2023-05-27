package com.enation.app.javashop.consumer.core.receiver;

import com.enation.app.javashop.consumer.core.event.CategoryChangeEvent;
import com.enation.app.javashop.consumer.core.event.ShipTemplateChangeEvent;
import com.enation.app.javashop.core.base.message.CategoryChangeMsg;
import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.goods.model.vo.ShipTemplateMsg;
import com.enation.app.javashop.core.shop.model.dos.ShipTemplateChild;
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
 * 运费模板变化消息
 *
 * @author zh
 * @version v2.0
 * @since v7.0.0
 * 2019年9月16日 上午10:29:42
 */
@Component
public class ShipTemplateChangeReceiver {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired(required = false)
    private List<ShipTemplateChangeEvent> events;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = AmqpExchange.SHIP_TEMPLATE_CHANGE + "_QUEUE"),
            exchange = @Exchange(value = AmqpExchange.SHIP_TEMPLATE_CHANGE, type = ExchangeTypes.FANOUT)
    ))
    public void categoryChange(ShipTemplateMsg shipTemplateMsg) {

        if (events != null) {
            for (ShipTemplateChangeEvent event : events) {
                try {
                    event.shipTemplateChange(shipTemplateMsg);
                } catch (Exception e) {
                    logger.error("处理商品分类变化消息出错", e);
                }
            }
        }

    }
}
