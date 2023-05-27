package com.enation.app.javashop.consumer.core.event;

import com.enation.app.javashop.core.base.message.AfterSaleChangeMessage;

/**
 * 售后服务单状态变化事件
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-24
 */
public interface AfterSaleChangeEvent {

    /**
     * 售后服务单状态变化后执行
     * @param afterSaleChangeMessage
     */
    void afterSaleChange(AfterSaleChangeMessage afterSaleChangeMessage);

}
