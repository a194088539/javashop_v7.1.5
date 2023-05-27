package com.enation.app.javashop.consumer.core.event;

import com.enation.app.javashop.core.member.model.vo.MemberLoginMsg;

/**
 * 会员登录事件
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月23日 上午10:25:27
 */
public interface MemberLoginEvent {

    /**
     * 会员登录
     *
     * @param memberLoginMsg
     */
    void memberLogin(MemberLoginMsg memberLoginMsg);
}
