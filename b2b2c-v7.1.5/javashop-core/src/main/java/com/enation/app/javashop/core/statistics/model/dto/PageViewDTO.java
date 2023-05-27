package com.enation.app.javashop.core.statistics.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 流量分析，店铺流量DO
 *
 * @author mengyuanming
 * @version 2.0
 * @since 7.0
 * 2018/6/8 17:57
 */
@ApiModel("店铺流量DO")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PageViewDTO {

    @ApiModelProperty(value = "周期类型", name = "cycle_type",allowableValues = "YEAR,MONTH")
    @NotNull(message = "日期类型不可为空")
    private String cycleType;

    @ApiModelProperty(value = "年份，默认当前年份", name = "year")
    private Integer year;

    @ApiModelProperty(value = "月份，默认当前月份", name = "month")
    private Integer month;

    @ApiModelProperty(value = "商家id", name = "seller_id")
    private Integer sellerId;

    public String getCycleType() {
        return cycleType;
    }

    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    @Override
    public String toString() {
        return "PageViewDO{" +
                "cycleType='" + cycleType + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", sellerId=" + sellerId +
                '}';
    }
}
