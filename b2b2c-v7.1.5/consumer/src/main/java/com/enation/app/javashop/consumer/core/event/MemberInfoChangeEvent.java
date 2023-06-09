package com.enation.app.javashop.consumer.core.event;

/**
 * 会员资料修改事件
 *
 * @author zh
 * @version v2.0
 * @since v7.0.0
 * 2018年3月23日 上午10:24:31
 */
public interface MemberInfoChangeEvent {

    /**
     * 会员资料修改后事件
     *
     * @param memberId 会员id
     */
    void memberInfoChange(Integer memberId);
}
