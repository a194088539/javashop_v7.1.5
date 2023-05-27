package com.enation.app.javashop.core.shop.service;

import com.enation.app.javashop.core.shop.model.dos.ShopMenu;
import com.enation.app.javashop.core.shop.model.vo.ShopMenuVO;
import com.enation.app.javashop.core.shop.model.vo.ShopMenusVO;
import com.enation.app.javashop.framework.database.Page;

import java.util.List;

/**
 * 菜单管理店铺业务层
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-08-03 17:17:26
 */
public interface ShopMenuManager {

    /**
     * 查询菜单管理店铺列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return Page
     */
    Page list(int page, int pageSize);

    /**
     * 添加菜单管理店铺
     *
     * @param shopMenuVO 菜单管理店铺
     * @return ShopMenu 菜单管理店铺
     */
    ShopMenu add(ShopMenuVO shopMenuVO);

    /**
     * 修改菜单管理店铺
     *
     * @param shopMenu 菜单管理店铺
     * @param id       菜单管理店铺主键
     * @return ShopMenu 菜单管理店铺
     */
    ShopMenu edit(ShopMenu shopMenu, Integer id);

    /**
     * 删除菜单管理店铺
     *
     * @param id 菜单管理店铺主键
     */
    void delete(Integer id);

    /**
     * 获取菜单管理店铺
     *
     * @param id 菜单管理店铺主键
     * @return ShopMenu  菜单管理店铺
     */
    ShopMenu getModel(Integer id);

    /**
     * 根据id获取菜单集合
     *
     * @param id 菜单的id
     * @return
     */
    List<ShopMenusVO> getMenuTree(Integer id);

    /**
     * 获取菜单管理
     *
     * @param identifier 菜单的唯一标识
     * @return MenuVO  菜单管理
     */
    ShopMenu getMenuByIdentifier(String identifier);

}