package com.enation.app.javashop.core.promotion.groupbuy.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 团购活动与团购商品查询参数对象
 * @author Snow create in 2018/5/28
 * @version v2.0
 * @since v7.0.0
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GroupbuyQueryParam implements Serializable {

    @ApiModelProperty(value = "团购活动ID")
    private Integer actId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "店铺id")
    private Integer sellerId;

    @ApiModelProperty(value = "会员ID")
    private Integer memberId;

    @ApiModelProperty(value = "团购商品审核状态 0：待审核，1：审核通过，2：审核未通过")
    private Integer gbStatus;

    @ApiModelProperty(value = "团购活动开始时间")
    private Long startTime;

    @ApiModelProperty(value = "团购活动结束时间")
    private Long endTime;

    @ApiModelProperty(value = "分类ID")
    private Integer catId;

    @ApiModelProperty( value = "第几页")
    private Integer page;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize;

    @ApiModelProperty(value = "团购活动名称")
    private String actName;

    @ApiModelProperty(value = "团购活动状态")
    private String actStatus;

    @ApiModelProperty(value = "团购名称")
    private String gbName;

    @ApiModelProperty(value = "客户端类型")
    private String clientType;

    public Integer getActId() {
        return actId;
    }

    public void setActId(Integer actId) {
        this.actId = actId;
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

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getGbStatus() {
        return gbStatus;
    }

    public void setGbStatus(Integer gbStatus) {
        this.gbStatus = gbStatus;
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

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getActStatus() {
        return actStatus;
    }

    public void setActStatus(String actStatus) {
        this.actStatus = actStatus;
    }

    public String getGbName() {
        return gbName;
    }

    public void setGbName(String gbName) {
        this.gbName = gbName;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupbuyQueryParam param = (GroupbuyQueryParam) o;
        return Objects.equals(actId, param.actId) &&
                Objects.equals(goodsName, param.goodsName) &&
                Objects.equals(sellerId, param.sellerId) &&
                Objects.equals(memberId, param.memberId) &&
                Objects.equals(gbStatus, param.gbStatus) &&
                Objects.equals(startTime, param.startTime) &&
                Objects.equals(endTime, param.endTime) &&
                Objects.equals(catId, param.catId) &&
                Objects.equals(page, param.page) &&
                Objects.equals(pageSize, param.pageSize) &&
                Objects.equals(actName, param.actName) &&
                Objects.equals(actStatus, param.actStatus) &&
                Objects.equals(gbName, param.gbName) &&
                Objects.equals(clientType, param.clientType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actId, goodsName, sellerId, memberId, gbStatus, startTime, endTime, catId, page, pageSize, actName, actStatus, gbName, clientType);
    }

    @Override
    public String toString() {
        return "GroupbuyQueryParam{" +
                "actId=" + actId +
                ", goodsName='" + goodsName + '\'' +
                ", sellerId=" + sellerId +
                ", memberId=" + memberId +
                ", gbStatus=" + gbStatus +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", catId=" + catId +
                ", page=" + page +
                ", pageSize=" + pageSize +
                ", actName='" + actName + '\'' +
                ", actStatus='" + actStatus + '\'' +
                ", gbName='" + gbName + '\'' +
                ", clientType='" + clientType + '\'' +
                '}';
    }
}
