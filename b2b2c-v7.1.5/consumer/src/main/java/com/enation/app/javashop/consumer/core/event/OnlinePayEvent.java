package com.enation.app.javashop.consumer.core.event;

/**
 * 在线支付事件
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月23日 上午9:51:20
 */
public interface OnlinePayEvent {

	/**
	 * 在线支付
	 * @param memberId
	 */
    void onlinePay(Integer memberId);
}
