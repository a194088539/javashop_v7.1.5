package com.enation.app.javashop.core.promotion.pintuan.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * 拼团活动搜索参数实体
 * @author duanmingyu
 * @since v7.1.5
 * @version v1.0
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PintuanQueryParam {

    @ApiModelProperty(value = "页码")
    private Integer pageNo;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize;

    @ApiModelProperty(value = "拼团活动名称")
    private String name;

    @ApiModelProperty(value = "商家ID")
    private Integer sellerId;

    @ApiModelProperty(value = "拼团日期-开始日期")
    private Long startTime;

    @ApiModelProperty(value = "拼团日期-结束日期")
    private Long endTime;

    @ApiModelProperty(value = "状态")
    private String status;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
        PintuanQueryParam that = (PintuanQueryParam) o;
        return Objects.equals(pageNo, that.pageNo) &&
                Objects.equals(pageSize, that.pageSize) &&
                Objects.equals(name, that.name) &&
                Objects.equals(sellerId, that.sellerId) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageNo, pageSize, name, sellerId, startTime, endTime, status);
    }

    @Override
    public String toString() {
        return "PintuanQueryParam{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", name='" + name + '\'' +
                ", sellerId=" + sellerId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status='" + status + '\'' +
                '}';
    }
}
