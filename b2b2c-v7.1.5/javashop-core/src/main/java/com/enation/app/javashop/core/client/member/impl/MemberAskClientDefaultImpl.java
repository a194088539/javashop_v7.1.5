package com.enation.app.javashop.core.client.member.impl;

import com.enation.app.javashop.core.client.member.MemberAskClient;
import com.enation.app.javashop.core.member.model.dos.AskMessageDO;
import com.enation.app.javashop.core.member.service.AskMessageManager;
import com.enation.app.javashop.core.member.service.MemberAskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author fk
 * @version v1.0
 * @Description: 评论对外接口实现
 * @date 2018/7/26 11:30
 * @since v7.0.0
 */
@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class MemberAskClientDefaultImpl implements MemberAskClient {

    @Autowired
    private MemberAskManager memberAskManager;

    @Autowired
    private AskMessageManager askMessageManager;

    @Override
    public Integer getNoReplyCount(Integer sellerId) {

        return memberAskManager.getNoReplyCount(sellerId);
    }

    @Override
    public void sendMessage(AskMessageDO askMessageDO) {
        this.askMessageManager.addAskMessage(askMessageDO);
    }
}
