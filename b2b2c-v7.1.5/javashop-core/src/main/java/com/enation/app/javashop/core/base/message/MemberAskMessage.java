package com.enation.app.javashop.core.base.message;

import com.enation.app.javashop.core.member.model.dos.MemberAsk;

import java.io.Serializable;
import java.util.List;

/**
 * 会员商品咨询消息
 *
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-09-16
 */
public class MemberAskMessage implements Serializable {

    private static final long serialVersionUID = -6950240368106665641L;

    private List<MemberAsk> memberAsks;

    private Long sendTime;

    public List<MemberAsk> getMemberAsks() {
        return memberAsks;
    }

    public void setMemberAsks(List<MemberAsk> memberAsks) {
        this.memberAsks = memberAsks;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }
}
