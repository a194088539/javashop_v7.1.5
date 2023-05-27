package com.enation.app.javashop.consumer.core.receiver;

import com.enation.app.javashop.consumer.core.event.SearchKeywordEvent;
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
* @author liuyulei
 * @version 1.0
 * @Description: 关键字搜索历史消息
 * @date 2019/5/27 12:00
 * @since v7.0
 */
@Component
public class SearchKeywordReceiver {

    @Autowired(required = false)
    private List<SearchKeywordEvent> events;


    protected final Log logger = LogFactory.getLog(this.getClass());

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = AmqpExchange.SEARCH_KEYWORDS + "_QUEUE"),
            exchange = @Exchange(value = AmqpExchange.SEARCH_KEYWORDS, type = ExchangeTypes.FANOUT)
    ))
    public void refund(String keywords) {

        if (events != null) {
            for (SearchKeywordEvent event : events) {
                try {
                    event.updateOrAdd(keywords);
                } catch (Exception e) {
                    logger.error("处理搜索关键字消息出错", e);
                }
            }
        }
    }
}
