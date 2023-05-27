package com.enation.app.javashop.core.promotion.seckill.model.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * 限时抢购查询参数
 * @author Snow create in 2018/6/21
 * @version v2.0
 * @since v7.0.0
 */
public class SeckillQueryParam {

    @ApiModelProperty(value = "页码")
    private Integer pageNo;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize;

    @ApiModelProperty(value = "限时抢购活动ID")
    private Integer seckillId;

    @ApiModelProperty(value = "限时抢购活动名称")
    private String seckillName;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "限时抢购日期-开始日期")
    private Long startTime;

    @ApiModelProperty(value = "限时抢购日期-结束日期")
    private Long endTime;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商家ID")
    private Integer sellerId;

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

    public Integer getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Integer seckillId) {
        this.seckillId = seckillId;
    }

    public String getSeckillName() {
        return seckillName;
    }

    public void setSeckillName(String seckillName) {
        this.seckillName = seckillName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SeckillQueryParam param = (SeckillQueryParam) o;
        return Objects.equals(pageNo, param.pageNo) &&
                Objects.equals(pageSize, param.pageSize) &&
                Objects.equals(seckillId, param.seckillId) &&
                Objects.equals(seckillName, param.seckillName) &&
                Objects.equals(status, param.status) &&
                Objects.equals(startTime, param.startTime) &&
                Objects.equals(endTime, param.endTime) &&
                Objects.equals(goodsName, param.goodsName) &&
                Objects.equals(sellerId, param.sellerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageNo, pageSize, seckillId, seckillName, status, startTime, endTime, goodsName, sellerId);
    }

    @Override
    public String toString() {
        return "SeckillQueryParam{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", seckillId=" + seckillId +
                ", seckillName='" + seckillName + '\'' +
                ", status='" + status + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", goodsName='" + goodsName + '\'' +
                ", sellerId=" + sellerId +
                '}';
    }
}
