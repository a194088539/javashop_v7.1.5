package com.enation.app.javashop.core.promotion.groupbuy.model.vo;

import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyGoodsDO;
import com.enation.app.javashop.framework.database.annotation.Column;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Objects;

/**
 * 团购商品VO
 *
 * @author Snow create in 2018/6/11
 * @version v2.0
 * @since v7.0.0
 */
@ApiModel
public class GroupbuyGoodsVO extends GroupbuyGoodsDO {

    @ApiModelProperty(name = "gb_status_text", value = "审核状态值")
    private String gbStatusText;

    @ApiModelProperty(name = "start_time", value = "活动开始时间")
    private Long startTime;

    @ApiModelProperty(name = "end_time", value = "活动截止时间")
    private Long endTime;

    @ApiModelProperty(name = "title", value = "活动标题")
    private String title;

    @ApiModelProperty(name = "enable_quantity", value = "可用库存")
    private Integer enableQuantity;

    @ApiModelProperty(name = "quantity", value = "库存")
    private Integer quantity;

    @ApiModelProperty(name = "isEnable", value = "活动是否在进行中")
    private Integer isEnable;

    @ApiModelProperty(name="delete_status",value="是否删除 DELETED：已删除，NORMAL：正常")
    private String deleteStatus;

    @ApiModelProperty(name="delete_reason",value="删除原因")
    private String deleteReason;

    @ApiModelProperty(name="delete_time",value="删除日期")
    private Long deleteTime;

    @ApiModelProperty(name="delete_name",value="删除操作人")
    private String deleteName;

    public String getGbStatusText() {
        return gbStatusText;
    }

    public void setGbStatusText(String gbStatusText) {
        this.gbStatusText = gbStatusText;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getEnableQuantity() {
        return enableQuantity;
    }

    public void setEnableQuantity(Integer enableQuantity) {
        this.enableQuantity = enableQuantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
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
        if (!super.equals(o)) {
            return false;
        }
        GroupbuyGoodsVO that = (GroupbuyGoodsVO) o;
        return Objects.equals(gbStatusText, that.gbStatusText) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(title, that.title) &&
                Objects.equals(enableQuantity, that.enableQuantity) &&
                Objects.equals(quantity, that.quantity) &&
                Objects.equals(isEnable, that.isEnable) &&
                Objects.equals(deleteStatus, that.deleteStatus) &&
                Objects.equals(deleteReason, that.deleteReason) &&
                Objects.equals(deleteTime, that.deleteTime) &&
                Objects.equals(deleteName, that.deleteName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gbStatusText, startTime, endTime, title, enableQuantity, quantity, isEnable, deleteStatus, deleteReason, deleteTime, deleteName);
    }

    @Override
    public String toString() {
        return "GroupbuyGoodsVO{" +
                "gbStatusText='" + gbStatusText + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", title='" + title + '\'' +
                ", enableQuantity=" + enableQuantity +
                ", quantity=" + quantity +
                ", isEnable=" + isEnable +
                ", deleteStatus='" + deleteStatus + '\'' +
                ", deleteReason='" + deleteReason + '\'' +
                ", deleteTime=" + deleteTime +
                ", deleteName='" + deleteName + '\'' +
                '}';
    }
}
