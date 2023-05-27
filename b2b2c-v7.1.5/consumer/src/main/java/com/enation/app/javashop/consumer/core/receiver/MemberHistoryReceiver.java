package com.enation.app.javashop.consumer.core.receiver;

import com.enation.app.javashop.consumer.core.event.MemberHistoryEvent;
import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.member.model.dto.HistoryDTO;
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
 * 会员足迹
 *
 * @author zh
 * @version v7.0
 * @date 19/07/05 下午4:17
 * @since v7.0
 */
@Component
public class MemberHistoryReceiver {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired(required = false)
    private List<MemberHistoryEvent> events;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = AmqpExchange.MEMBER_HISTORY + "_QUEUE"),
            exchange = @Exchange(value = AmqpExchange.MEMBER_HISTORY, type = ExchangeTypes.FANOUT)
    ))
    public void createIndexPage(HistoryDTO historyDTO) {
        if (events != null) {
            for (MemberHistoryEvent event : events) {
                try {
                    event.addMemberHistory(historyDTO);
                } catch (Exception e) {
                    logger.error("记录会员足迹消息出错", e);
                }
            }
        }

    }
}
