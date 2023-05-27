package com.enation.app.javashop.core.promotion.groupbuy.service;

import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyAuditParam;
import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyQueryParam;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyActiveDO;

import java.util.List;

/**
 * 团购活动表业务层
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-21 11:52:14
 */
public interface GroupbuyActiveManager	{

	/**
	 * 查询团购活动表列表
	 * @param param 搜索参数
	 * @return Page
	 */
	Page list(GroupbuyQueryParam param);

	/**
	 * 添加团购活动表
	 * @param groupbuyActive 团购活动表
	 * @return GroupbuyActive 团购活动表
	 */
	GroupbuyActiveDO add(GroupbuyActiveDO groupbuyActive);

	/**
	* 修改团购活动表
	* @param groupbuyActive 团购活动表
	* @param id 团购活动表主键
	* @return GroupbuyActive 团购活动表
	*/
	GroupbuyActiveDO edit(GroupbuyActiveDO groupbuyActive, Integer id);

	/**
	 * 删除团购活动表
	 * @param id 团购活动表主键
	 * @param deleteReason 删除原因
	 * @param operaName 操作人
	 */
	void delete(Integer id, String deleteReason, String operaName);

	/**
	 * 获取团购活动表
	 * @param id 团购活动表主键
	 * @return GroupbuyActive  团购活动表
	 */
	GroupbuyActiveDO getModel(Integer id);

	/**
	 * 批量审核商品
	 * @param param
	 */
	void batchAuditGoods(GroupbuyAuditParam param);

	/**
	 * 读取正在进行的活动列表
	 * @return
	 */
	List<GroupbuyActiveDO> getActiveList();


	/**
	 * 验证操作权限<br/>
	 * 如有问题直接抛出权限异常
	 * @param id
	 */
	void verifyAuth(Integer id);

}
