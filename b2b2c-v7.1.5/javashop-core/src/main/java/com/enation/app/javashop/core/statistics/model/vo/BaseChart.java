package com.enation.app.javashop.core.statistics.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * BaseChart
 *
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018-04-11 上午11:12
 */
public class BaseChart extends PropertyNamingStrategy implements Serializable{


    @ApiModelProperty(value = "x轴 刻度")
    protected String[] xAxis;


    @ApiModelProperty(value = "y轴 刻度")
    protected String[] yAxis;


    public String[] getxAxis() {
        return xAxis;
    }

    public void setxAxis(String[] xAxis) {
        this.xAxis = xAxis;
    }

    public String[] getyAxis() {
        return yAxis;
    }

    public void setyAxis(String[] yAxis) {
        this.yAxis = yAxis;
    }
}
