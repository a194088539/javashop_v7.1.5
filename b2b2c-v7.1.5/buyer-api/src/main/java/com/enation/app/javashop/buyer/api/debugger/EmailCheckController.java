package com.enation.app.javashop.buyer.api.debugger;

import com.enation.app.javashop.core.base.model.vo.EmailVO;
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
@RequestMapping("/debugger/email")
@ConditionalOnProperty(value = "javashop.debugger", havingValue = "true")
public class EmailCheckController {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @GetMapping(value = "/test")
    public String test( String email ) {

        EmailVO emailVO = new EmailVO();
        emailVO.setContent("测试邮件");
        emailVO.setTitle("测试邮件");
        emailVO.setEmail(email);

        this.amqpTemplate.convertAndSend(AmqpExchange.EMAIL_SEND_MESSAGE, "emailSendMessageMsg", emailVO);


        return "ok";
    }

}
