package com.enation.app.javashop.core.client.system.impl;

import com.enation.app.javashop.core.client.system.MessageTemplateClient;
import com.enation.app.javashop.core.system.enums.MessageCodeEnum;
import com.enation.app.javashop.core.system.model.dos.MessageTemplateDO;
import com.enation.app.javashop.core.system.service.MessageTemplateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @version v7.0
 * @Description:
 * @Author: zjp
 * @Date: 2018/7/27 09:44
 */
@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class MessageTemplateClientDefaultImpl implements MessageTemplateClient {
    @Autowired
    private MessageTemplateManager messageTemplateManager;
    @Override
    public MessageTemplateDO getModel(MessageCodeEnum messageCodeEnum) {
        return messageTemplateManager.getModel(messageCodeEnum);
    }
}
