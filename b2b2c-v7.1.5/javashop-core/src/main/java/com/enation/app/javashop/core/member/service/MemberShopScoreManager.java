package com.enation.app.javashop.core.member.service;

import com.enation.app.javashop.core.member.model.dos.MemberShopScore;
import com.enation.app.javashop.core.member.model.dto.MemberShopScoreDTO;
import com.enation.app.javashop.framework.database.Page;

import java.util.List;

/**
 * 店铺评分业务层
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-05-03 10:38:00
 */
public interface MemberShopScoreManager	{

	/**
	 * 查询店铺评分列表
	 * @param page 页码
	 * @param pageSize 每页数量
	 * @return Page
	 */
	Page list(int page, int pageSize);
	/**
	 * 添加店铺评分
	 * @param memberShopScore 店铺评分
	 * @return MemberShopScore 店铺评分
	 */
	MemberShopScore add(MemberShopScore memberShopScore);

	/**
	* 修改店铺评分
	* @param memberShopScore 店铺评分
	* @param id 店铺评分主键
	* @return MemberShopScore 店铺评分
	*/
	MemberShopScore edit(MemberShopScore memberShopScore, Integer id);
	
	/**
	 * 删除店铺评分
	 * @param id 店铺评分主键
	 */
	void delete(Integer id);
	
	/**
	 * 获取店铺评分
	 * @param id 店铺评分主键
	 * @return MemberShopScore  店铺评分
	 */
	MemberShopScore getModel(Integer id);

	/**
	 * 查询每个店铺的评分
	 * @return
	 */
	List<MemberShopScoreDTO> queryEveryShopScore();

	/**
	 * 根据会员id和订单编号获取店铺评分信息
	 * @param memberId 会员id
	 * @param orderSn 订单编号
	 * @return
	 */
	MemberShopScore getModel(Integer memberId, String orderSn);
}