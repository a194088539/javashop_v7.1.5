package com.enation.app.javashop.consumer.core.event;


/**
 * 拼团成功事件
 * @author fk
 * @version v2.0
 * @since v7.1.4
 * 2019年6月22日 上午10:24:31
 */
public interface PintuanSuccessEvent {

	/**
	 * 拼团成功
	 * @param pintuanOrderId
	 */
    void success(Integer pintuanOrderId);
}
