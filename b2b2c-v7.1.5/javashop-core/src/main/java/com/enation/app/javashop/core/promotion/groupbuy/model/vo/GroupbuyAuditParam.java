package com.enation.app.javashop.core.promotion.groupbuy.model.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Arrays;
import java.util.Objects;

/**
 * 团购活动审核商品参数实体
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 */
public class GroupbuyAuditParam {
    @ApiModelProperty(name = "act_id", value = "团购活动ID", required = true)
    private Integer actId;

    @ApiModelProperty(name = "gb_ids", value = "待审核团购商品ID组", required = true)
    private Integer[] gbIds;

    @ApiModelProperty(name = "status", value = "审核状态 1：通过，2：不通过", required = true, allowableValues = "1,2")
    private Integer status;

    public Integer getActId() {
        return actId;
    }

    public void setActId(Integer actId) {
        this.actId = actId;
    }

    public Integer[] getGbIds() {
        return gbIds;
    }

    public void setGbIds(Integer[] gbIds) {
        this.gbIds = gbIds;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupbuyAuditParam that = (GroupbuyAuditParam) o;
        return Objects.equals(actId, that.actId) &&
                Arrays.equals(gbIds, that.gbIds) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(actId, status);
        result = 31 * result + Arrays.hashCode(gbIds);
        return result;
    }

    @Override
    public String toString() {
        return "GroupbuyAuditParam{" +
                "actId=" + actId +
                ", gbIds=" + Arrays.toString(gbIds) +
                ", status=" + status +
                '}';
    }
}
