package com.enation.app.javashop.consumer.core.event;

import com.enation.app.javashop.core.base.message.MemberAskMessage;

/**
 * 会员商品咨询事件
 *
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-09-16
 */
public interface MemberAskSendMessageEvent {

    /**
     * 会员商品咨询后执行
     *
     * @param memberAskMessage
     */
    void goodsAsk(MemberAskMessage memberAskMessage);
}
