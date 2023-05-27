package com.enation.app.javashop.consumer.core.event;

import com.enation.app.javashop.core.base.model.vo.EmailVO;

/**
 * 发送邮件事件
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月23日 上午9:55:27
 */
public interface SendEmailEvent {

    /**
     * 发送邮件
     *
     * @param emailVO 邮件
     */
    void sendEmail(EmailVO emailVO);
}
