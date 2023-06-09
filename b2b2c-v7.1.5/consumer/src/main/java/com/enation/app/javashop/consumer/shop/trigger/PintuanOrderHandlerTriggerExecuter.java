package com.enation.app.javashop.consumer.shop.trigger;

import com.enation.app.javashop.core.promotion.pintuan.service.PintuanOrderManager;
import com.enation.app.javashop.framework.trigger.Interface.TimeTriggerExecuter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 评团订单延时处理
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2019-03-07 下午5:38
 */
@Component("pintuanOrderHandlerTriggerExecuter")
public class PintuanOrderHandlerTriggerExecuter implements TimeTriggerExecuter {


    @Autowired
    private PintuanOrderManager pintuanOrderManager;


    /**
     * 执行任务
     *
     * @param object 任务参数
     */
    @Override
    public void execute(Object object) {

        //pintuanOrderManager.handle((int) object);


    }
}
