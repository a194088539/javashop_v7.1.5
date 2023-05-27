package com.enation.app.javashop.core.system.service;

import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.system.model.ComplainTopic;

import java.util.List;

/**
 * 投诉主题业务层
 * @author fk
 * @version v2.0
 * @since v2.0
 * 2019-11-26 16:06:44
 */
public interface ComplainTopicManager	{

	/**
	 * 查询投诉主题列表
	 * @param page 页码
	 * @param pageSize 每页数量
	 * @return Page 
	 */
	Page list(int page,int pageSize);
	/**
	 * 添加投诉主题
	 * @param complainTopic 投诉主题
	 * @return ComplainTopic 投诉主题
	 */
	ComplainTopic add(ComplainTopic complainTopic);

	/**
	* 修改投诉主题
	* @param complainTopic 投诉主题
	* @param id 投诉主题主键
	* @return ComplainTopic 投诉主题
	*/
	ComplainTopic edit(ComplainTopic complainTopic,Integer id);
	
	/**
	 * 删除投诉主题
	 * @param id 投诉主题主键
	 */
	void delete(Integer id);
	
	/**
	 * 获取投诉主题
	 * @param id 投诉主题主键
	 * @return ComplainTopic  投诉主题
	 */
	ComplainTopic getModel(Integer id);

	/**
	 * 查询所有投诉主题
	 * @return
	 */
    List<ComplainTopic> list();
}