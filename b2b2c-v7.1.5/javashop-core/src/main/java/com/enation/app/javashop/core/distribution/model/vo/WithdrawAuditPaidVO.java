package com.enation.app.javashop.core.distribution.model.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Arrays;
import java.util.Objects;

/**
 * 提现申请审核和设置已转账参数实体
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 */
public class WithdrawAuditPaidVO {
    @ApiModelProperty(name = "apply_ids", value = "提现申请id组", required = true)
    private Integer[] applyIds;

    @ApiModelProperty(name = "remark",	value = "审核或转账备注")
    private String remark;

    public Integer[] getApplyIds() {
        return applyIds;
    }

    public void setApplyIds(Integer[] applyIds) {
        this.applyIds = applyIds;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WithdrawAuditPaidVO that = (WithdrawAuditPaidVO) o;
        return Arrays.equals(applyIds, that.applyIds) &&
                Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(remark);
        result = 31 * result + Arrays.hashCode(applyIds);
        return result;
    }

    @Override
    public String toString() {
        return "WithdrawAuditPaidVO{" +
                "applyIds=" + Arrays.toString(applyIds) +
                ", remark='" + remark + '\'' +
                '}';
    }
}
