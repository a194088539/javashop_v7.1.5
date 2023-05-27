package com.enation.app.javashop.core.base.message;

import com.enation.app.javashop.core.member.model.dos.MemberComment;

import java.io.Serializable;
import java.util.List;

/**
 * 商品评论消息
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018年3月23日 上午10:37:41
 */
public class GoodsCommentMsg implements Serializable{

	private static final long serialVersionUID = 8978542323509463579L;

	/**
	 * 添加
	 */
	public final static int ADD = 1;

	/**
	 * 删除
	 */
	public final static int DELETE = 2;

	/**
	 * 评论对象集合
	 */
	private List<MemberComment> comment;

	/**
	 * 是否为系统自动评论
	 */
	private boolean autoComment;

	/**
	 * 操作类型
	 */
	private Integer operaType;

	public List<MemberComment> getComment() {
		return comment;
	}

	public void setComment(List<MemberComment> comment) {
		this.comment = comment;
	}

	public boolean isAutoComment() {
		return autoComment;
	}

	public void setAutoComment(boolean autoComment) {
		this.autoComment = autoComment;
	}

	public Integer getOperaType() {
		return operaType;
	}

	public void setOperaType(Integer operaType) {
		this.operaType = operaType;
	}
}
