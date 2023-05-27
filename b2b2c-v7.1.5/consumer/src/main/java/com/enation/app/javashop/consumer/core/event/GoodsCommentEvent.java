package com.enation.app.javashop.consumer.core.event;

import com.enation.app.javashop.core.base.message.GoodsCommentMsg;

/**
 * 商品评论事件
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月23日 上午10:24:49
 */
public interface GoodsCommentEvent {

    /**
     * 商品评论后执行
     *
     * @param goodsCommentMsg
     */
    void goodsComment(GoodsCommentMsg goodsCommentMsg);
}
