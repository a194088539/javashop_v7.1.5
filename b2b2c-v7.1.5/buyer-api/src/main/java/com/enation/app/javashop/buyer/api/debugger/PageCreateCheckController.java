package com.enation.app.javashop.buyer.api.debugger;

import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
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
@RequestMapping("/debugger/page-create")
@ConditionalOnProperty(value = "javashop.debugger", havingValue = "true")
public class PageCreateCheckController {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @GetMapping(value = "/test")
    public String test( String pageType ) {

        this.amqpTemplate.convertAndSend(AmqpExchange.PAGE_CREATE, AmqpExchange.PAGE_CREATE+"_ROUTING", new String[]{pageType});


        return "ok";
    }

}
