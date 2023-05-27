package com.enation.app.javashop.core.base.message;

import com.enation.app.javashop.core.member.model.dos.AskReplyDO;
import com.enation.app.javashop.core.member.model.dos.MemberAsk;

import java.io.Serializable;
import java.util.List;

/**
 * 会员商品咨询回复消息
 *
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-09-17
 */
public class AskReplyMessage implements Serializable {

    private static final long serialVersionUID = 1450422796376881941L;

    private List<AskReplyDO> askReplyDOList;

    private MemberAsk memberAsk;

    private Long sendTime;

    public AskReplyMessage() {

    }

    public AskReplyMessage(List<AskReplyDO> askReplyDOList, MemberAsk memberAsk, Long sendTime) {
        this.askReplyDOList = askReplyDOList;
        this.memberAsk = memberAsk;
        this.sendTime = sendTime;
    }

    public List<AskReplyDO> getAskReplyDOList() {
        return askReplyDOList;
    }

    public void setAskReplyDOList(List<AskReplyDO> askReplyDOList) {
        this.askReplyDOList = askReplyDOList;
    }

    public MemberAsk getMemberAsk() {
        return memberAsk;
    }

    public void setMemberAsk(MemberAsk memberAsk) {
        this.memberAsk = memberAsk;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

}
