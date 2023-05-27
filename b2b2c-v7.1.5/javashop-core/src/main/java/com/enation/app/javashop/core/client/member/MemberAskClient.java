package com.enation.app.javashop.core.client.member;

import com.enation.app.javashop.core.member.model.dos.AskMessageDO;

/**
 * @author fk
 * @version v1.0
 * @Description: 评论对外接口
 * @date 2018/7/26 11:30
 * @since v7.0.0
 */
public interface MemberAskClient {

    /**
     * 卖家获取未回复的咨询数量
     * @param sellerId
     * @return
     */
    Integer getNoReplyCount(Integer sellerId);

    /**
     * 发送会员商品咨询消息
     * @param askMessageDO
     */
    void sendMessage(AskMessageDO askMessageDO);
}
