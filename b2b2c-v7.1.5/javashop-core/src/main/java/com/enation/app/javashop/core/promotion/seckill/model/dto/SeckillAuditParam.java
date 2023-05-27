package com.enation.app.javashop.core.promotion.seckill.model.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.Arrays;
import java.util.Objects;

/**
 * 限时抢购审核商品参数实体
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 */
public class SeckillAuditParam {

    @ApiModelProperty(name = "apply_ids", value = "限时抢购申请ID组", required = true)
    private Integer[] applyIds;

    @ApiModelProperty(name = "status", value = "审核状态 PASS为通过, FAIL为不通过", required = true, allowableValues = "PASS,FAIL")
    private String status;

    @ApiModelProperty(name = "fail_reason",	value = "驳回原因")
    private String failReason;

    public Integer[] getApplyIds() {
        return applyIds;
    }

    public void setApplyIds(Integer[] applyIds) {
        this.applyIds = applyIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SeckillAuditParam that = (SeckillAuditParam) o;
        return Arrays.equals(applyIds, that.applyIds) &&
                Objects.equals(status, that.status) &&
                Objects.equals(failReason, that.failReason);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(status, failReason);
        result = 31 * result + Arrays.hashCode(applyIds);
        return result;
    }

    @Override
    public String toString() {
        return "SeckillAuditParam{" +
                "applyIds=" + Arrays.toString(applyIds) +
                ", status='" + status + '\'' +
                ", failReason='" + failReason + '\'' +
                '}';
    }
}
