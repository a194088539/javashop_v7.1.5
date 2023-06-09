package com.enation.app.javashop.core.client.system;

import com.enation.app.javashop.core.system.enums.MessageCodeEnum;
import com.enation.app.javashop.core.system.model.dos.MessageTemplateDO;

/**
 * @version v7.0
 * @Description: 消息模版client
 * @Author: zjp
 * @Date: 2018/7/27 09:42
 */
public interface MessageTemplateClient {
    /**
     * 获取消息模版
     * @param messageCodeEnum 消息模版编码
     * @return MessageTemplateDO  消息模版
     */
    MessageTemplateDO getModel(MessageCodeEnum messageCodeEnum);

}
