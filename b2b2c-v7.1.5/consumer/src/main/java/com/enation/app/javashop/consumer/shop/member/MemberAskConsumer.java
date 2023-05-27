package com.enation.app.javashop.consumer.shop.member;

import com.enation.app.javashop.consumer.core.event.MemberAskSendMessageEvent;
import com.enation.app.javashop.core.base.message.MemberAskMessage;
import com.enation.app.javashop.core.client.member.MemberAskClient;
import com.enation.app.javashop.core.member.model.dos.AskMessageDO;
import com.enation.app.javashop.core.member.model.dos.MemberAsk;
import com.enation.app.javashop.core.member.model.enums.AskMsgTypeEnum;
import com.enation.app.javashop.core.member.model.enums.CommonStatusEnum;
import com.enation.app.javashop.core.system.enums.DeleteStatusEnum;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.service.OrderQueryManager;
import com.enation.app.javashop.framework.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员商品咨询消费者
 * 会员提出咨询后给购买过此商品的会员发送消息
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-09-16
 */
@Service
public class MemberAskConsumer implements MemberAskSendMessageEvent {

    @Autowired
    private MemberAskClient memberAskClient;

    @Autowired
    private OrderQueryManager orderQueryManager;

    @Override
    public void goodsAsk(MemberAskMessage memberAskMessage) {
        List<MemberAsk> memberAskList = memberAskMessage.getMemberAsks();

        if (memberAskList != null && memberAskList.size() != 0) {
            for (MemberAsk memberAsk : memberAskList) {
                if (memberAsk == null) {
                    break;
                }

                //获取3个月之内购买过相关商品的订单数据，获取的订单数据只限已完成和已收货并且未删除的订单
                List<OrderDO> orderDOList = orderQueryManager.listOrderByGoods(memberAsk.getGoodsId(), memberAsk.getMemberId(), 3);

                if (orderDOList != null && orderDOList.size() != 0) {
                    AskMessageDO askMessageDO = new AskMessageDO();
                    askMessageDO.setAsk(memberAsk.getContent());
                    askMessageDO.setAskId(memberAsk.getAskId());
                    askMessageDO.setAskMember(memberAsk.getMemberName());
                    askMessageDO.setAskAnonymous(memberAsk.getAnonymous());
                    askMessageDO.setGoodsId(memberAsk.getGoodsId());
                    askMessageDO.setGoodsName(memberAsk.getGoodsName());
                    askMessageDO.setGoodsImg(memberAsk.getGoodsImg());
                    askMessageDO.setIsDel(DeleteStatusEnum.NORMAL.value());
                    askMessageDO.setIsRead(CommonStatusEnum.NO.value());
                    askMessageDO.setSendTime(memberAskMessage.getSendTime());
                    askMessageDO.setReceiveTime(DateUtil.getDateline());
                    askMessageDO.setMsgType(AskMsgTypeEnum.ASK.value());

                    //循环订单获取会员信息，然后给会员发送消息
                    for (OrderDO orderDO : orderDOList) {
                        askMessageDO.setMemberId(orderDO.getMemberId());
                        this.memberAskClient.sendMessage(askMessageDO);
                    }
                }
            }
        }
    }
}
