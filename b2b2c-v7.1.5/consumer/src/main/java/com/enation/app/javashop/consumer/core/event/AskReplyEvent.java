package com.enation.app.javashop.consumer.core.event;

import com.enation.app.javashop.core.base.message.AskReplyMessage;

/**
 * 会员商品咨询回复事件
 *
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-09-17
 */
public interface AskReplyEvent {

    /**
     * 会员回复商品咨询后执行
     *
     * @param askReplyMessage
     */
    void askReply(AskReplyMessage askReplyMessage);
}
