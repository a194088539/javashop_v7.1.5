package com.enation.app.javashop.core.member.model.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Arrays;
import java.util.Objects;

/**
 * 会员商品咨询审核参数实体
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-09-17
 */
public class BatchAuditVO {

    @ApiModelProperty(name = "ids", value = "要审核的数据主键id组", required = true)
    private Integer[] ids;

    @ApiModelProperty(name = "auth_status", value = "审核状态 PASS_AUDIT:审核通过,REFUSE_AUDIT:审核拒绝", required = true, allowableValues = "PASS_AUDIT,REFUSE_AUDIT")
    private String authStatus;

    public Integer[] getIds() {
        return ids;
    }

    public void setIds(Integer[] ids) {
        this.ids = ids;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BatchAuditVO that = (BatchAuditVO) o;
        return Arrays.equals(ids, that.ids) &&
                Objects.equals(authStatus, that.authStatus);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(authStatus);
        result = 31 * result + Arrays.hashCode(ids);
        return result;
    }

    @Override
    public String toString() {
        return "BatchAuditVO{" +
                "ids=" + Arrays.toString(ids) +
                ", authStatus='" + authStatus + '\'' +
                '}';
    }
}
