package com.enation.app.javashop.core.goods.service;

import com.enation.app.javashop.core.goods.model.dos.SpecValuesDO;
import com.enation.app.javashop.core.goods.model.enums.Permission;

import java.util.List;

/**
 * 规格值业务层
 * 
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-20 10:23:53
 */
public interface SpecValuesManager {

	/**
	 * 添加规格值
	 * 
	 * @param specValues
	 *            规格值
	 * @return SpecValues 规格值
	 */
	SpecValuesDO add(SpecValuesDO specValues);

	/**
	 * 修改规格值
	 * 
	 * @param specValues
	 *            规格值
	 * @param id
	 *            规格值主键
	 * @return SpecValues 规格值
	 */
	SpecValuesDO edit(SpecValuesDO specValues, Integer id);

	/**
	 * 获取规格值
	 * 
	 * @param id
	 *            规格值主键
	 * @return SpecValues 规格值
	 */
	SpecValuesDO getModel(Integer id);

	/**
	 * 获取某规格的规格值
	 * 
	 * @param specId
	 * @param permission
	 * @return
	 */
	List<SpecValuesDO> listBySpecId(Integer specId, Permission permission);

	/**
	 * 添加某规格的规格值
	 * 
	 * @param specId
	 * @param valueList
	 * @return
	 */
	List<SpecValuesDO> saveSpecValue(Integer specId, String[] valueList);

}