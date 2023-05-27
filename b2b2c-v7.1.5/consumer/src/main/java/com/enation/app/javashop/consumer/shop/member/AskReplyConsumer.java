package com.enation.app.javashop.consumer.shop.member;

import com.enation.app.javashop.consumer.core.event.AskReplyEvent;
import com.enation.app.javashop.core.base.message.AskReplyMessage;
import com.enation.app.javashop.core.client.member.MemberAskClient;
import com.enation.app.javashop.core.member.model.dos.AskMessageDO;
import com.enation.app.javashop.core.member.model.dos.AskReplyDO;
import com.enation.app.javashop.core.member.model.dos.MemberAsk;
import com.enation.app.javashop.core.member.model.enums.AskMsgTypeEnum;
import com.enation.app.javashop.core.member.model.enums.CommonStatusEnum;
import com.enation.app.javashop.core.system.enums.DeleteStatusEnum;
import com.enation.app.javashop.framework.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员商品咨询回复消费者
 * 会员回复商品咨询后给提出咨询的会员发送消息
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-09-17
 */
@Service
public class AskReplyConsumer implements AskReplyEvent {

    @Autowired
    private MemberAskClient memberAskClient;

    @Override
    public void askReply(AskReplyMessage askReplyMessage) {

        List<AskReplyDO> askReplyDOList = askReplyMessage.getAskReplyDOList();
        MemberAsk memberAsk = askReplyMessage.getMemberAsk();

        if (askReplyDOList != null && askReplyDOList.size() != 0 && memberAsk != null) {

            AskMessageDO askMessageDO = new AskMessageDO();
            askMessageDO.setMemberId(memberAsk.getMemberId());
            askMessageDO.setAsk(memberAsk.getContent());
            askMessageDO.setAskId(memberAsk.getAskId());
            askMessageDO.setAskMember(memberAsk.getMemberName());
            askMessageDO.setAskAnonymous(memberAsk.getAnonymous());
            askMessageDO.setGoodsId(memberAsk.getGoodsId());
            askMessageDO.setGoodsName(memberAsk.getGoodsName());
            askMessageDO.setGoodsImg(memberAsk.getGoodsImg());
            askMessageDO.setIsDel(DeleteStatusEnum.NORMAL.value());
            askMessageDO.setIsRead(CommonStatusEnum.NO.value());
            askMessageDO.setSendTime(askReplyMessage.getSendTime());
            askMessageDO.setReceiveTime(DateUtil.getDateline());
            askMessageDO.setMsgType(AskMsgTypeEnum.REPLY.value());

            for (AskReplyDO askReplyDO : askReplyDOList) {
                if (askReplyDO == null) {
                    break;
                }

                askMessageDO.setReplyId(askReplyDO.getId());
                askMessageDO.setReply(askReplyDO.getContent());
                askMessageDO.setReplyMember(askReplyDO.getMemberName());
                askMessageDO.setReplyAnonymous(askReplyDO.getAnonymous());
                this.memberAskClient.sendMessage(askMessageDO);
            }
        }

    }
}
