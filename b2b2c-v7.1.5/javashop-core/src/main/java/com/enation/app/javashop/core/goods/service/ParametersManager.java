package com.enation.app.javashop.core.goods.service;

import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.goods.model.dos.ParametersDO;

/**
 * 参数业务层
 * 
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-20 16:14:31
 */
public interface ParametersManager {

	/**
	 * 查询参数列表
	 * 
	 * @param page
	 *            页码
	 * @param pageSize
	 *            每页数量
	 * @return Page
	 */
	Page list(int page, int pageSize);

	/**
	 * 添加参数
	 * 
	 * @param parameters
	 *            参数
	 * @return Parameters 参数
	 */
	ParametersDO add(ParametersDO parameters);

	/**
	 * 修改参数
	 * 
	 * @param parameters
	 *            参数
	 * @param id
	 *            参数主键
	 * @return Parameters 参数
	 */
	ParametersDO edit(ParametersDO parameters, Integer id);

	/**
	 * 删除参数
	 * 
	 * @param id
	 *            参数主键
	 */
	void delete(Integer id);

	/**
	 * 获取参数
	 * 
	 * @param id
	 *            参数主键
	 * @return Parameters 参数
	 */
	ParametersDO getModel(Integer id);

	/**
	 * 参数排序
	 * 
	 * @param paramId
	 * @param sortType
	 */
	void paramSort(Integer paramId, String sortType);

	/**
	 * 删除参数，使用参数组
	 * 
	 * @param groupId
	 */
	void deleteByGroup(Integer groupId);

}