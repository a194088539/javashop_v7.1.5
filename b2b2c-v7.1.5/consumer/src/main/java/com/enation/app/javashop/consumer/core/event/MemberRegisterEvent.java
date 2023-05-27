package com.enation.app.javashop.consumer.core.event;

import com.enation.app.javashop.core.base.message.MemberRegisterMsg;

/**
 * 会员注册事件
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月23日 上午10:22:56
 */
public interface MemberRegisterEvent {

    /**
     * 会员注册
     *
     * @param memberRegisterMsg
     */
    void memberRegister(MemberRegisterMsg memberRegisterMsg);
}
