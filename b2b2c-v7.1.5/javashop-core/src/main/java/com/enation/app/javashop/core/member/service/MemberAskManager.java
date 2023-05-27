package com.enation.app.javashop.core.member.service;

import com.enation.app.javashop.core.member.model.dos.MemberAsk;
import com.enation.app.javashop.core.member.model.dto.AskQueryParam;
import com.enation.app.javashop.core.member.model.vo.BatchAuditVO;
import com.enation.app.javashop.core.member.model.vo.MemberAskVO;
import com.enation.app.javashop.framework.database.Page;

import java.util.List;

/**
 * 咨询业务层
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-05-04 17:41:18
 */
public interface MemberAskManager	{

	/**
	 * 查询咨询列表
	 * @param param 查询条件
	 * @return Page
	 */
	Page list(AskQueryParam param);
	/**
	 * 添加咨询
	 * @param askContent 咨询
	 * @param goodsId 商品id
	 * @param anonymous 是否匿名 YES:是，NO:否
	 * @return MemberAsk 咨询
	 */
	MemberAsk add(String askContent, Integer goodsId, String anonymous);

	/**
	 * 获取会员商品咨询详情
	 * @param askId 会员商品咨询主键id
	 * @return MemberAsk  咨询
	 */
	MemberAsk getModel(Integer askId);

	/**
	 * 获取会员商品咨询详情
	 * 包含回复信息和商品信息
	 * @param askId 会员商品咨询主键id
	 * @return
	 */
	MemberAskVO getModelVO(Integer askId);

	/**
	 * 获取商品会员咨询分页列表数据
	 * @param pageNo 页数
	 * @param pageSize 每页记录数
	 * @param goodsId 商品id
	 * @return
	 */
	Page listGoodsAsks(Integer pageNo, Integer pageSize, Integer goodsId);

	/**
	 * 获取与会员商品咨询相关的其它咨询
	 * @param askId
	 * @param goodsId
	 * @return
	 */
	List<MemberAsk> listRelationAsks(Integer askId, Integer goodsId);

	/**
	 * 批量审核会员商品咨询
	 * @param batchAuditVO
	 */
	void batchAudit(BatchAuditVO batchAuditVO);

	/**
	 * 删除会员商品咨询
	 * @param askId 会员商品咨询主键
	 */
	void delete(Integer askId);

	/**
	 * 商家回复会员商品咨询
	 * @param replyContent 回复内容
	 * @param askId 会员商品咨询id
	 * @return
	 */
	MemberAsk reply(String replyContent, Integer askId);

	/**
	 * 修改会员商品咨询回复数量
	 * @param askId 会员商品咨询ID
	 * @param num 数量
	 */
	void updateReplyNum(Integer askId, Integer num);

	/**
	 * 卖家获取未回复的咨询数量
	 * @param sellerId
	 * @return
	 */
	Integer getNoReplyCount(Integer sellerId);

}