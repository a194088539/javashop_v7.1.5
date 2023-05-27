package com.enation.app.javashop.core.shop.service;

import com.enation.app.javashop.core.shop.model.dos.ShopRole;
import com.enation.app.javashop.core.shop.model.vo.ShopRoleVO;
import com.enation.app.javashop.framework.database.Page;

import java.util.List;
import java.util.Map;

/**
 * 店铺角色业务层
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-08-02 15:22:20
 */
public interface ShopRoleManager {

    /**
     * 查询店铺角色列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return Page
     */
    Page list(int page, int pageSize);

    /**
     * 添加店铺角色
     *
     * @param shopRoleVO 店铺角色
     * @return ShopRoleVO 店铺角色
     */
    ShopRoleVO add(ShopRoleVO shopRoleVO);

    /**
     * 修改店铺角色
     *
     * @param shopRoleVO 店铺角色
     * @param id         店铺角色主键
     * @return ShopRole 店铺角色
     */
    ShopRoleVO edit(ShopRoleVO shopRoleVO, Integer id);

    /**
     * 删除店铺角色
     *
     * @param id 店铺角色主键
     */
    void delete(Integer id);

    /**
     * 获取店铺角色
     *
     * @param id 店铺角色主键
     * @return ShopRole  店铺角色
     */
    ShopRole getModel(Integer id);

    /**
     * 获取所有角色的权限对照表
     *
     * @return 权限对照表
     */
    Map<String, List<String>> getRoleMap(Integer sellerId);

    /**
     * 根据角色id获取所属菜单
     *
     * @param id 角色id
     * @return 菜单唯一标识
     */
    List<String> getRoleMenu(Integer id);

    /**
     * 根据id获取角色
     *
     * @param id
     * @return
     */
    ShopRoleVO getRole(Integer id);

}