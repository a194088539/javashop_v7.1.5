package com.enation.app.javashop.core.shop.model.vo;

import com.enation.app.javashop.framework.validation.annotation.Mobile;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 店员实体
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-08-04 18:48:39
 */
public class ClerkVO implements Serializable {
    /**
     * 店员名称
     */
    @ApiModelProperty(value = "账号名称", required = true)
    @NotNull(message = "账号名称不能为空")
    private String uname;
    /**
     * 账号密码
     */
    @ApiModelProperty(value = "账号密码")
    private String password;
    /**
     * 手机号码
     */
    @Mobile(message = "手机格式不正确")
    @ApiModelProperty(value = "手机号码", required = true)
    private String mobile;
    /**
     * 权限id
     */
    @NotNull(message = "权限不能为空")
    @ApiModelProperty(name = "role_id", value = "权限id,如果是店主则传0", required = true)
    private Integer roleId;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(value = "邮箱", required = true)
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "ClerkVO{" +
                "uname='" + uname + '\'' +
                ", password='" + password + '\'' +
                ", mobile='" + mobile + '\'' +
                ", roleId=" + roleId +
                ", email='" + email + '\'' +
                '}';
    }
}