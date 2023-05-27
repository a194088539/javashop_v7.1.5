package com.enation.app.javashop.core.promotion.groupbuy.model.dos;

import java.io.Serializable;
import java.util.Objects;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 团购活动表实体
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-21 11:52:14
 */
@Table(name="es_groupbuy_active")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GroupbuyActiveDO implements Serializable {

    private static final long serialVersionUID = 8396241558782003L;

    /**活动Id*/
    @Id(name = "act_id")
    @ApiModelProperty(hidden=true)
    private Integer actId;

    /**活动名称*/
    @Column(name = "act_name")
    @ApiModelProperty(name="act_name",value="活动名称",required=false)
    private String actName;

    /**活动开启时间*/
    @Column(name = "start_time")
    @ApiModelProperty(name="start_time",value="活动开启时间",required=false)
    private Long startTime;

    /**团购结束时间*/
    @Column(name = "end_time")
    @ApiModelProperty(name="end_time",value="团购结束时间",required=false)
    private Long endTime;

    /**团购报名截止时间*/
    @Column(name = "join_end_time")
    @ApiModelProperty(name="join_end_time",value="团购报名截止时间",required=false)
    private Long joinEndTime;

    /**团购添加时间*/
    @Column(name = "add_time")
    @ApiModelProperty(name="add_time",value="团购添加时间",required=false)
    private Long addTime;

    /**团购活动标签Id*/
    @Column(name = "act_tag_id")
    @ApiModelProperty(name="act_tag_id",value="团购活动标签Id",required=false)
    private Integer actTagId;

    /**参与团购商品数量*/
    @Column(name = "goods_num")
    @ApiModelProperty(name="goods_num",value="参与团购商品数量",required=false)
    private Integer goodsNum;

    /**是否删除 DELETED：已删除，NORMAL：正常*/
    @Column(name = "delete_status")
    @ApiModelProperty(name="delete_status",value="是否删除 DELETED：已删除，NORMAL：正常",required=false)
    private String deleteStatus;

    /**删除原因*/
    @Column(name = "delete_reason")
    @ApiModelProperty(name="delete_reason",value="删除原因",required=false)
    private String deleteReason;

    /**删除日期*/
    @Column(name = "delete_time")
    @ApiModelProperty(name="delete_time",value="删除日期",required=false)
    private Long deleteTime;

    /**删除操作人*/
    @Column(name = "delete_name")
    @ApiModelProperty(name="delete_name",value="删除操作人",required=false)
    private String deleteName;

    @PrimaryKeyField
    public Integer getActId() {
        return actId;
    }
    public void setActId(Integer actId) {
        this.actId = actId;
    }

    public String getActName() {
        return actName;
    }
    public void setActName(String actName) {
        this.actName = actName;
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

    public Long getJoinEndTime() {
        return joinEndTime;
    }
    public void setJoinEndTime(Long joinEndTime) {
        this.joinEndTime = joinEndTime;
    }

    public Long getAddTime() {
        return addTime;
    }
    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Integer getActTagId() {
        return actTagId;
    }
    public void setActTagId(Integer actTagId) {
        this.actTagId = actTagId;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }
    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

    public Long getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Long deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getDeleteName() {
        return deleteName;
    }

    public void setDeleteName(String deleteName) {
        this.deleteName = deleteName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupbuyActiveDO that = (GroupbuyActiveDO) o;
        return Objects.equals(actId, that.actId) &&
                Objects.equals(actName, that.actName) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(joinEndTime, that.joinEndTime) &&
                Objects.equals(addTime, that.addTime) &&
                Objects.equals(actTagId, that.actTagId) &&
                Objects.equals(goodsNum, that.goodsNum) &&
                Objects.equals(deleteStatus, that.deleteStatus) &&
                Objects.equals(deleteReason, that.deleteReason) &&
                Objects.equals(deleteTime, that.deleteTime) &&
                Objects.equals(deleteName, that.deleteName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actId, actName, startTime, endTime, joinEndTime, addTime, actTagId, goodsNum, deleteStatus, deleteReason, deleteTime, deleteName);
    }
}
