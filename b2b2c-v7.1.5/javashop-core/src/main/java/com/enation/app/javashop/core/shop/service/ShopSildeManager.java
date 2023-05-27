package com.enation.app.javashop.core.shop.service;


import java.util.List;

import com.enation.app.javashop.core.shop.model.dos.ShopSildeDO;

/**
 * 店铺幻灯片业务层
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-28 18:50:58
 */
public interface ShopSildeManager	{

	/**
	 * 查询店铺幻灯片列表
	 * @param shopId
	 * @return
	 */
	List<ShopSildeDO> list(Integer shopId);
	/**
	 * 添加店铺幻灯片
	 * @param shopSilde 店铺幻灯片
	 * @return ShopSilde 店铺幻灯片
	 */
	ShopSildeDO add(ShopSildeDO shopSilde);

	/**
	 * 批量修改店铺幻灯片
	 * @param list 幻灯片
	 */
	void edit(List<ShopSildeDO> list);
	
	/**
	 * 删除店铺幻灯片
	 * @param id 店铺幻灯片主键
	 */
	void delete(Integer id);
	
	/**
	 * 获取店铺幻灯片
	 * @param id 店铺幻灯片主键
	 * @return ShopSilde  店铺幻灯片
	 */
	ShopSildeDO getModel(Integer id);
	
	/**
	* 修改店铺幻灯片
	* @param shopSilde 店铺幻灯片
	* @param id 店铺幻灯片主键
	* @return ShopSilde 店铺幻灯片
	*/
	ShopSildeDO edit(ShopSildeDO shopSilde,Integer id);

}