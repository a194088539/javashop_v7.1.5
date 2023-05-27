package com.enation.app.javashop.core.member.model.vo;

import java.io.Serializable;

/**
 * 会员登录消息
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年10月18日 下午9:39:06
 */
public class MemberLoginMsg implements Serializable{

	private static final long serialVersionUID = 8173084471934834777L;

	/**
	 * 会员id
	 */
	private Integer memberId;
	/**
	 * 上次登录时间
	 */
	private Long lastLoginTime;

	/**
	 * 会员还是商家登录 1 会员  2 商家
	 */
	private Integer memberOrSeller;



	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public Long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Integer getMemberOrSeller() {
		return memberOrSeller;
	}

	public void setMemberOrSeller(Integer memberOrSeller) {
		this.memberOrSeller = memberOrSeller;
	}

	@Override
	public String toString() {
		return "MemberLoginMsg{" +
				"memberId=" + memberId +
				", lastLoginTime=" + lastLoginTime +
				", memberOrSeller=" + memberOrSeller +
				'}';
	}
}
