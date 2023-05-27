package com.enation.app.javashop.core.shop.service;

import com.enation.app.javashop.core.shop.model.dos.ShopCatDO;

import java.util.List;

/**
 * 店铺分组业务层
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-04-24 11:18:37
 */
public interface ShopCatManager	{

	/**
	 * 查询店铺分组列表
	 * @param shopId
	 * @return
	 */
	List list(Integer shopId,String display);
	/**
	 * 添加店铺分组
	 * @param shopCatDO 店铺分组
	 * @return ShopCatDO 店铺分组
	 */
	ShopCatDO add(ShopCatDO shopCatDO);

	/**
	* 修改店铺分组
	* @param shopCatDO 店铺分组
	* @param id 店铺分组主键
	* @return ShopCatDO 店铺分组
	*/
	ShopCatDO edit(ShopCatDO shopCatDO, Integer id);
	
	/**
	 * 删除店铺分组
	 * @param id 店铺分组主键
	 */
	void delete(Integer id);
	
	/**
	 * 获取店铺分组
	 * @param id 店铺分组主键
	 * @return ShopCatDO  店铺分组
	 */
	ShopCatDO getModel(Integer id);

	/**
	 * 获取当前分组所有的子(包括当前分组)
	 * @param catPath
	 * @return 子类集合
	 */
	List getChildren(String catPath);

}