package com.enation.app.javashop.core.consumer;


import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 测试
 *
 * @author chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/6/8 下午3:19
 */
public class TestReceiver extends BaseTest {


    private String cacheKey = "my_test_receiver";

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private Cache cache;

    @Test
    public void test() throws Exception {

        String putMessage = "TEST_MESSAGE"+ DateUtil.getDateline();
        this.amqpTemplate.convertAndSend(AmqpExchange.TEST_EXCHANGE,
                AmqpExchange.TEST_EXCHANGE + "_ROUTING",
                putMessage);

        Thread.sleep(3000);
        String cacheMessage = (String) cache.get(cacheKey);

        Assert.assertEquals(cacheMessage, putMessage);

        cacheMessage = (String) cache.get(cacheKey + 2);
        Assert.assertEquals(cacheMessage, putMessage);

        cache.remove(cacheKey);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = AmqpExchange.TEST_EXCHANGE + "_QUEUE"),
            exchange = @Exchange(value = AmqpExchange.TEST_EXCHANGE, type = ExchangeTypes.FANOUT)
    ))
    public void receiver(String message) {
        cache.put(cacheKey, message);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = AmqpExchange.TEST_EXCHANGE + "_QUEUE2"),
            exchange = @Exchange(value = AmqpExchange.TEST_EXCHANGE, type = ExchangeTypes.FANOUT)
    ))
    public void receiver2(String message) {
        cache.put(cacheKey + 2, message);
    }

}
