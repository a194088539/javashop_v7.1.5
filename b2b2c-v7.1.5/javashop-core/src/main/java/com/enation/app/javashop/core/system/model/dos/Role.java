package com.enation.app.javashop.core.system.model.dos;

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
 * 权限实体
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-06-20 20:11:12
 */
@Table(name = "es_role")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Role implements Serializable {

    private static final long serialVersionUID = 3095547080772844L;

    /**
     * 权限id
     */
    @Id(name = "id")
    @ApiModelProperty(hidden = true)
    private Integer id;
    /**
     * 权限名称
     */
    @Column(name = "role_name")
    @ApiModelProperty(name = "role_name", value = "权限名称", required = false)
    private String roleName;
    /**
     * 权限集合
     */
    @Column(name = "auth_ids")
    @ApiModelProperty(name = "auth_ids", value = "权限集合", required = false)
    private String authIds;

    @PrimaryKeyField
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Role that = (Role) o;
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (roleName != null ? !roleName.equals(that.roleName) : that.roleName != null) {
            return false;
        }
        return authIds != null ? authIds.equals(that.authIds) : that.authIds == null;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (roleName != null ? roleName.hashCode() : 0);
        result = 31 * result + (authIds != null ? authIds.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                ", authIds='" + authIds + '\'' +
                '}';
    }


}