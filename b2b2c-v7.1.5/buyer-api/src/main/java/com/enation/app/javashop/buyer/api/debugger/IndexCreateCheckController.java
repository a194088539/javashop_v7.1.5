package com.enation.app.javashop.buyer.api.debugger;

import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.system.progress.model.TaskProgressConstant;
import com.enation.app.javashop.core.system.progress.service.ProgressManager;
import com.enation.app.javashop.framework.exception.ResourceNotFoundException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-04-24
 */
@RestController
@RequestMapping("/debugger/index-create")
@ConditionalOnProperty(value = "javashop.debugger", havingValue = "true")
public class IndexCreateCheckController {
    @Autowired
    private AmqpTemplate amqpTemplate;


    @Autowired
    private ProgressManager progressManager;

    @GetMapping(value = "/test")
    public String test( String pageType ) {

        if (progressManager.getProgress(TaskProgressConstant.GOODS_INDEX) != null) {
            throw new ResourceNotFoundException("有索引任务正在进行中，需等待本次任务完成后才能再次生成。");
        }
        /** 发送索引生成消息 */
        this.amqpTemplate.convertAndSend(AmqpExchange.INDEX_CREATE, AmqpExchange.INDEX_CREATE+"_ROUTING","1");


        return "ok";
    }

}
