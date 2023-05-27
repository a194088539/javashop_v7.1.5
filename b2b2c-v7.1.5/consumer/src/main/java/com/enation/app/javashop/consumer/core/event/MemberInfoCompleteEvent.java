package com.enation.app.javashop.consumer.core.event;

/**
 * 会员完善个人信息
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月23日 上午10:25:18
 */
public interface MemberInfoCompleteEvent {

	/**
	 * 会员完善个人信息
	 * @param memberId
	 */
    void memberInfoComplete(Integer memberId);
}
