package com.enation.app.javashop.core.pagedata.service;

import com.enation.app.javashop.core.pagedata.model.FocusPicture;

import java.util.List;

/**
 * 焦点图业务层
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-05-21 15:23:23
 */
public interface FocusPictureManager {

	/**
	 * 查询焦点图列表
	 * @param clientType 客户端类型
	 * @return List
	 */
	List list(String clientType);
	/**
	 * 添加焦点图
	 * @param cmsFocusPicture 焦点图
	 * @return FocusPicture 焦点图
	 */
	FocusPicture add(FocusPicture cmsFocusPicture);

	/**
	* 修改焦点图
	* @param cmsFocusPicture 焦点图
	* @param id 焦点图主键
	* @return FocusPicture 焦点图
	*/
	FocusPicture edit(FocusPicture cmsFocusPicture, Integer id);
	
	/**
	 * 删除焦点图
	 * @param id 焦点图主键
	 */
	void delete(Integer id);
	
	/**
	 * 获取焦点图
	 * @param id 焦点图主键
	 * @return FocusPicture  焦点图
	 */
	FocusPicture getModel(Integer id);

}