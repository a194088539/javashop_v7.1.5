package com.enation.app.javashop.consumer.core.receiver;

import com.enation.app.javashop.consumer.job.execute.EveryHourExecute;
import com.enation.app.javashop.core.base.JobAmqpExchange;
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
 * 每小时执行调用
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-07-25 上午8:26
 */
@Component
public class EveryHourExecuteReceiver {
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired(required = false)
    private List<EveryHourExecute> everyHourExecutes;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = JobAmqpExchange.EVERY_HOUR_EXECUTE + "_QUEUE"),
            exchange = @Exchange(value = JobAmqpExchange.EVERY_HOUR_EXECUTE, type = ExchangeTypes.FANOUT)
    ))
    public void everyHour() {

        if (everyHourExecutes != null) {
            for (EveryHourExecute everyHourExecute : everyHourExecutes) {
                try {
                    everyHourExecute.everyHour();
                } catch (Exception e) {
                    logger.error("每小时任务异常：", e);
                }
            }
        }
    }


}
