package com.enation.app.javashop.core.promotion.seckill.model.dos;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * 限时抢购入库实体
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-21 10:32:36
 */
@Table(name="es_seckill")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SeckillDO implements Serializable {

    private static final long serialVersionUID = 1621620877040645L;

    /**主键id*/
    @Id(name = "seckill_id")
    @ApiModelProperty(hidden=true)
    private Integer seckillId;

    /**活动名称*/
    @Column(name = "seckill_name")
    @NotEmpty(message = "请填写活动名称")
    @ApiModelProperty(value="活动名称",required=true)
    private String seckillName;

    /**活动日期*/
    @Column(name = "start_day")
    @NotNull(message = "请填写活动日期")
    @ApiModelProperty(value="活动日期",required=true)
    private Long startDay;

    /**报名截至时间*/
    @Column(name = "apply_end_time")
    @NotNull(message = "请填写报名截止时间")
    @ApiModelProperty(value="报名截至时间",required=true)
    private Long applyEndTime;

    /**申请规则*/
    @Column(name = "seckill_rule")
    @ApiModelProperty(value="申请规则",required=false)
    private String seckillRule;

    /** 已参与此活动的商家id集合 */
    @Column(name = "seller_ids")
    @ApiModelProperty(value="商家id集合以逗号分隔",required=false)
    private String sellerIds;

    /**状态*/
    @Column(name = "seckill_status")
    @ApiModelProperty(value="状态,EDITING：编辑中,RELEASE:已发布,OVER:已结束")
    private String seckillStatus;

    /**是否删除 DELETED：已删除，NORMAL：正常*/
    @Column(name = "delete_status")
    @ApiModelProperty(name="delete_status",value="是否删除 DELETED：已删除，NORMAL：正常",required=false)
    private String deleteStatus;

    @PrimaryKeyField
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

    public Long getStartDay() {
        return startDay;
    }

    public void setStartDay(Long startDay) {
        this.startDay = startDay;
    }

    public Long getApplyEndTime() {
        return applyEndTime;
    }

    public void setApplyEndTime(Long applyEndTime) {
        this.applyEndTime = applyEndTime;
    }

    public String getSeckillRule() {
        return seckillRule;
    }

    public void setSeckillRule(String seckillRule) {
        this.seckillRule = seckillRule;
    }

    public String getSeckillStatus() {
        return seckillStatus;
    }
    public void setSeckillStatus(String seckillStatus) {
        this.seckillStatus = seckillStatus;
    }

    public String getSellerIds() {
        if(sellerIds == null){
            sellerIds = "";
        }
        return sellerIds;
    }

    public void setSellerIds(String sellerIds) {
        this.sellerIds = sellerIds;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SeckillDO seckillDO = (SeckillDO) o;
        return Objects.equals(seckillId, seckillDO.seckillId) &&
                Objects.equals(seckillName, seckillDO.seckillName) &&
                Objects.equals(startDay, seckillDO.startDay) &&
                Objects.equals(applyEndTime, seckillDO.applyEndTime) &&
                Objects.equals(seckillRule, seckillDO.seckillRule) &&
                Objects.equals(sellerIds, seckillDO.sellerIds) &&
                Objects.equals(seckillStatus, seckillDO.seckillStatus) &&
                Objects.equals(deleteStatus, seckillDO.deleteStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seckillId, seckillName, startDay, applyEndTime, seckillRule, sellerIds, seckillStatus, deleteStatus);
    }

    @Override
    public String toString() {
        return "SeckillDO{" +
                "seckillId=" + seckillId +
                ", seckillName='" + seckillName + '\'' +
                ", startDay=" + startDay +
                ", applyEndTime=" + applyEndTime +
                ", seckillRule='" + seckillRule + '\'' +
                ", sellerIds='" + sellerIds + '\'' +
                ", seckillStatus='" + seckillStatus + '\'' +
                ", deleteStatus='" + deleteStatus + '\'' +
                '}';
    }
}
