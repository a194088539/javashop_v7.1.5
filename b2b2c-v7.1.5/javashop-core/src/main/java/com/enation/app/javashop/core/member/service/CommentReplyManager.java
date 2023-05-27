package com.enation.app.javashop.core.member.service;

import com.enation.app.javashop.core.goods.model.enums.Permission;
import com.enation.app.javashop.core.member.model.dos.CommentReply;
import com.enation.app.javashop.framework.database.Page;

/**
 * 评论回复业务层
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-05-03 16:34:50
 */
public interface CommentReplyManager	{

	/**
	 * 查询评论回复列表
	 * @param page 页码
	 * @param pageSize 每页数量
	 * @return Page
	 */
	Page list(int page, int pageSize);

	/**
	 * 删除评论回复
	 * @param id 评论回复主键
	 */
	void delete(Integer id);
	
	/**
	 * 获取评论回复
	 * @param id 评论回复主键
	 * @return CommentReply  评论回复
	 */
	CommentReply getModel(Integer id);

	/**
	 * 查询评论的相关回复
	 * @param commentId
	 * @return
	 */
	CommentReply getReply(Integer commentId);

	/**
	 * 回复评论
	 * @param commentId
	 * @param reply
	 * @param permission
	 * @return
	 */
	CommentReply replyComment(Integer commentId, String reply,Permission permission);
}