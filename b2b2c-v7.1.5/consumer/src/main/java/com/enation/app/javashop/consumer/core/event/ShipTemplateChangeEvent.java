package com.enation.app.javashop.consumer.core.event;

import com.enation.app.javashop.core.goods.model.vo.ShipTemplateMsg;

/**
 * 更新模板新增事件
 *
 * @author zh
 * @version v2.0
 * @since v7.0.0
 * 2019年9月16日 上午10:24:31
 */
public interface ShipTemplateChangeEvent {

    /**
     * 运费模板变化后需要执行的方法
     *
     * @param shipTemplateMsg 模板消息
     */
    void shipTemplateChange(ShipTemplateMsg shipTemplateMsg);
}
