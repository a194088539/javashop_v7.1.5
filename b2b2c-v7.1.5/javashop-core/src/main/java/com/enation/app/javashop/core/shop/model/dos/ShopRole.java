package com.enation.app.javashop.core.shop.model.dos;

import java.io.Serializable;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 店铺角色实体
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-08-02 15:22:20
 */
@Table(name = "es_shop_role")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShopRole implements Serializable {

    private static final long serialVersionUID = 9796665181888605L;

    /**
     * 角色主键
     */
    @Id(name = "role_id")
    @ApiModelProperty(hidden = true)
    private Integer roleId;
    /**
     * 角色名称
     */
    @Column(name = "role_name")
    @ApiModelProperty(name = "role_name", value = "角色名称", required = false)
    private String roleName;
    /**
     * 角色
     */
    @Column(name = "auth_ids")
    @ApiModelProperty(name = "auth_ids", value = "角色 ", required = false)
    private String authIds;
    /**
     * 角色描述
     */
    @Column(name = "role_describe")
    @ApiModelProperty(name = "role_describe", value = "角色描述", required = false)
    private String roleDescribe;
    /**
     * 店铺id
     */
    @Column(name = "shop_id")
    @ApiModelProperty(name = "shop_id", value = "店铺id", required = false)
    private Integer shopId;

    @PrimaryKeyField
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

    public String getAuthIds() {
        return authIds;
    }

    public void setAuthIds(String authIds) {
        this.authIds = authIds;
    }

    public String getRoleDescribe() {
        return roleDescribe;
    }

    public void setRoleDescribe(String roleDescribe) {
        this.roleDescribe = roleDescribe;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShopRole that = (ShopRole) o;
        if (roleId != null ? !roleId.equals(that.roleId) : that.roleId != null) {
            return false;
        }
        if (roleName != null ? !roleName.equals(that.roleName) : that.roleName != null) {
            return false;
        }
        if (authIds != null ? !authIds.equals(that.authIds) : that.authIds != null) {
            return false;
        }
        if (roleDescribe != null ? !roleDescribe.equals(that.roleDescribe) : that.roleDescribe != null) {
            return false;
        }
        return shopId != null ? shopId.equals(that.shopId) : that.shopId == null;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
        result = 31 * result + (roleName != null ? roleName.hashCode() : 0);
        result = 31 * result + (authIds != null ? authIds.hashCode() : 0);
        result = 31 * result + (roleDescribe != null ? roleDescribe.hashCode() : 0);
        result = 31 * result + (shopId != null ? shopId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ShopRole{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", authIds='" + authIds + '\'' +
                ", roleDescribe='" + roleDescribe + '\'' +
                ", shopId=" + shopId +
                '}';
    }

}