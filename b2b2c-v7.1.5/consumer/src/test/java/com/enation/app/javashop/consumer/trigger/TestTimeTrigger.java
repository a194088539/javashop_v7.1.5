package com.enation.app.javashop.consumer.trigger;

import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.trigger.Interface.TimeTriggerExecuter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TestTimeTrigger
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2019-02-19 下午3:01
 */
@Component
public class TestTimeTrigger implements TimeTriggerExecuter {

    @Autowired
    private Cache cache;
    /**
     * 执行任务
     *
     * @param object 任务参数
     */
    @Override
    public void execute(Object object) {
        cache.put("rabbitmq_test_value",object);
    }
}
