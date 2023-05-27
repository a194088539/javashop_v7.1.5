package com.enation.app.javashop.core.shop.model.vo;

import com.enation.app.javashop.core.shop.model.dos.ShopRole;
import com.enation.app.javashop.framework.util.JsonUtil;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


/**
 * 店铺角色实体
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-08-02 15:22:20
 */

public class ShopRoleVO {


    /**
     * 角色主键
     */
    @ApiModelProperty(hidden = true)
    private Integer roleId;
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    /**
     * 角色描述
     */
    @ApiModelProperty(value = "角色描述")
    private String roleDescribe;
    /**
     * 角色所拥有的菜单权限
     */
    @ApiModelProperty(name = "menus", value = "角色所拥有的菜单权限", required = true)
    private List<ShopMenus> menus;

    public ShopRoleVO() {

    }

    public ShopRoleVO(ShopRole shopRole) {
        this.setRoleId(shopRole.getRoleId());
        this.setRoleName(shopRole.getRoleName());
        this.setRoleDescribe(shopRole.getRoleDescribe());
        this.setMenus(JsonUtil.jsonToList(shopRole.getAuthIds(), ShopMenus.class));
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescribe() {
        return roleDescribe;
    }

    public void setRoleDescribe(String roleDescribe) {
        this.roleDescribe = roleDescribe;
    }

    public List<ShopMenus> getMenus() {
        return menus;
    }

    public void setMenus(List<ShopMenus> menus) {
        this.menus = menus;
    }


    @Override
    public String toString() {
        return "ShopRoleVO{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", roleDescribe='" + roleDescribe + '\'' +
                ", menus=" + menus +
                '}';
    }
}