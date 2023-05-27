package com.enation.app.javashop.core.member.model.dto;

import com.enation.app.javashop.core.member.model.vo.MemberVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 店员dto
 *
 * @author zh
 * @version v7.0
 * @date 18/8/10 下午3:51
 * @since v7.0
 */

public class ClerkDTO extends MemberVO {

    @ApiModelProperty(name = "role_id", value = "角色id", required = false)
    private Integer roleId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "ClerkDTO{" +
                "roleId=" + roleId +
                '}';
    }
}
