package com.enation.app.javashop.consumer.core.receiver;

import java.util.List;

import com.enation.app.javashop.consumer.core.event.MemberRegisterEvent;
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

import com.enation.app.javashop.core.base.message.MemberRegisterMsg;

/**
 * 会员注册消费者
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018年3月23日 上午10:31:20
 */
@Component
public class MemberRegisterReceiver {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired(required = false)
    private List<MemberRegisterEvent> events;

    /**
     * 会员注册
     *
     * @param vo
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = AmqpExchange.MEMEBER_REGISTER + "REGISTER_QUEUE"),
            exchange = @Exchange(value = AmqpExchange.MEMEBER_REGISTER, type = ExchangeTypes.FANOUT)
    ))
    public void memberRegister(MemberRegisterMsg vo) {

        if (events != null) {
            for (MemberRegisterEvent event : events) {
                try {
                    event.memberRegister(vo);
                } catch (Exception e) {
                    logger.error("会员注册消息出错", e);
                }
            }
        }

    }
}
