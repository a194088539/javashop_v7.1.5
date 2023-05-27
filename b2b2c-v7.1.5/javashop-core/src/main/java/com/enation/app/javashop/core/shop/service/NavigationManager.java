package com.enation.app.javashop.core.shop.service;

import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.shop.model.dos.NavigationDO;

import java.util.List;

/**
 * 店铺导航管理业务层
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-28 20:44:54
 */
public interface NavigationManager	{

	/**
	 * 查询店铺导航管理列表
	 * @param page
	 * @param pageSize
	 * @param shopId
	 * @return
	 */
	Page list(int page,int pageSize,Integer shopId);
	/**
	 * 添加店铺导航管理
	 * @param navigation 店铺导航管理
	 * @return Navigation 店铺导航管理
	 */
	NavigationDO add(NavigationDO navigation);

	/**
	* 修改店铺导航管理
	* @param navigation 店铺导航管理
	* @param id 店铺导航管理主键
	* @return Navigation 店铺导航管理
	*/
	NavigationDO edit(NavigationDO navigation,Integer id);
	
	/**
	 * 删除店铺导航管理
	 * @param id 店铺导航管理主键
	 */
	void delete(Integer id);
	
	/**
	 * 获取店铺导航管理
	 * @param id 店铺导航管理主键
	 * @return Navigation  店铺导航管理
	 */
	NavigationDO getModel(Integer id);

	/**
	 * 获取店铺导航集合
	 * @param shopId 店铺id
	 * @return
	 */
	List<NavigationDO> list(Integer shopId,Boolean isShow);

}