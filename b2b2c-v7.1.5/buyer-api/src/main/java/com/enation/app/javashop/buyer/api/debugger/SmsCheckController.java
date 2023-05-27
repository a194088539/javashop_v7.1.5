package com.enation.app.javashop.buyer.api.debugger;

import com.enation.app.javashop.core.base.model.vo.SmsSendVO;
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
 * 2019-04-23
 */
@RestController
@RequestMapping("/debugger/sms")
@ConditionalOnProperty(value = "javashop.debugger", havingValue = "true")
public class SmsCheckController {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @GetMapping(value = "/test")
    public String test( String mobile ) {

        SmsSendVO smsSendVO = new SmsSendVO();
        smsSendVO.setContent("您的注册验证码为：897383");
        smsSendVO.setMobile(mobile);

        amqpTemplate.convertAndSend(AmqpExchange.SEND_MESSAGE, AmqpExchange.SEND_MESSAGE + "_QUEUE",
                smsSendVO);

        return "ok";
    }
}
