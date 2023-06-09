package com.enation.app.javashop.core.promotion.seckill.model.dos;

import java.io.Serializable;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * 限时抢购时刻实体
 * @author Snow
 * @version v2.0.0
 * @since v7.0.0
 * 2018-04-02 18:24:47
 */
@Table(name="es_seckill_range")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SeckillRangeDO implements Serializable {

    private static final long serialVersionUID = 9388211981272440L;

    /**主键*/
    @Id(name = "range_id")
    @ApiModelProperty(hidden=true)
    private Integer rangeId;

    /**限时抢购活动id*/
    @Column(name = "seckill_id")
    @ApiModelProperty(name="seckill_id",value="限时抢购活动id",required=false)
    private Integer seckillId;

    /**整点时刻*/
    @Column(name = "range_time")
    @ApiModelProperty(name="range_time",value="整点时刻",required=false)
    private Integer rangeTime;

    @PrimaryKeyField
    public Integer getRangeId() {
        return rangeId;
    }
    public void setRangeId(Integer rangeId) {
        this.rangeId = rangeId;
    }

    public Integer getSeckillId() {
        return seckillId;
    }
    public void setSeckillId(Integer seckillId) {
        this.seckillId = seckillId;
    }

    public Integer getRangeTime() {
        return rangeTime;
    }
    public void setRangeTime(Integer rangeTime) {
        this.rangeTime = rangeTime;
    }

    @Override
    public String toString() {
        return "SeckillRangeDO{" +
                "rangeId=" + rangeId +
                ", seckillId=" + seckillId +
                ", rangeTime=" + rangeTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (o == null || getClass() != o.getClass()){
            return false;
        }

        SeckillRangeDO that = (SeckillRangeDO) o;

        return new EqualsBuilder()
                .append(rangeId, that.rangeId)
                .append(seckillId, that.seckillId)
                .append(rangeTime, that.rangeTime)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(rangeId)
                .append(seckillId)
                .append(rangeTime)
                .toHashCode();
    }
}
