package com.enation.app.javashop.core.aftersale.model.vo;

import com.enation.app.javashop.core.base.context.Region;
import com.enation.app.javashop.core.base.context.RegionFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 申请售后服务VO
 * 主要用于用户提交售后服务申请时参数的传递
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-18
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AfterSaleApplyVO implements Serializable {

    private static final long serialVersionUID = 8028022122776504592L;

    /**
     * 订单编号
     */
    @ApiModelProperty(name = "order_sn", value = "订单编号", required = true, dataType = "String")
    private String orderSn;
    /**
     * 申请原因
     */
    @ApiModelProperty(name = "reason", value = "申请原因", required = true, dataType = "String")
    private String reason;
    /**
     * 问题描述
     */
    @ApiModelProperty(name = "problem_desc", value = "问题描述", required = true, dataType = "String")
    private String problemDesc;
    /**
     * 申请凭证
     */
    @ApiModelProperty(name = "apply_vouchers", value = "申请凭证", dataType = "String")
    private String applyVouchers;
    /**
     * 提交数量
     */
    @ApiModelProperty(name = "return_num", value = "提交数量", required = true, dataType = "Integer")
    private Integer returnNum;
    /**
     * 商品SKUID
     */
    @ApiModelProperty(name = "sku_id", value = "商品SKUID", required = true, dataType = "Integer")
    private Integer skuId;
    /**
     * 收货人姓名
     */
    @ApiModelProperty(name = "ship_name", value = "收货人姓名", required = true, dataType = "String")
    private String shipName;
    /**
     * 收货人手机号
     */
    @ApiModelProperty(name = "ship_mobile", value = "收货人手机号", required = true, dataType = "String")
    private String shipMobile;
    /**
     * 收货地址详细
     */
    @ApiModelProperty(name = "ship_addr", value = "收货地址详细", required = true, dataType = "String")
    private String shipAddr;
    /**
     * 收货地区信息
     */
    @RegionFormat
    @ApiModelProperty(name = "region", value = "地区")
    private Region region;
    /**
     * 售后图片信息集合
     */
    @ApiModelProperty(name = "images", value = "售后图片集合")
    private List<String> images;

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getProblemDesc() {
        return problemDesc;
    }

    public void setProblemDesc(String problemDesc) {
        this.problemDesc = problemDesc;
    }

    public String getApplyVouchers() {
        return applyVouchers;
    }

    public void setApplyVouchers(String applyVouchers) {
        this.applyVouchers = applyVouchers;
    }

    public Integer getReturnNum() {
        return returnNum;
    }

    public void setReturnNum(Integer returnNum) {
        this.returnNum = returnNum;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipMobile() {
        return shipMobile;
    }

    public void setShipMobile(String shipMobile) {
        this.shipMobile = shipMobile;
    }

    public String getShipAddr() {
        return shipAddr;
    }

    public void setShipAddr(String shipAddr) {
        this.shipAddr = shipAddr;
    }

    @JsonIgnore
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AfterSaleApplyVO that = (AfterSaleApplyVO) o;
        return Objects.equals(orderSn, that.orderSn) &&
                Objects.equals(reason, that.reason) &&
                Objects.equals(problemDesc, that.problemDesc) &&
                Objects.equals(applyVouchers, that.applyVouchers) &&
                Objects.equals(returnNum, that.returnNum) &&
                Objects.equals(skuId, that.skuId) &&
                Objects.equals(shipName, that.shipName) &&
                Objects.equals(shipMobile, that.shipMobile) &&
                Objects.equals(shipAddr, that.shipAddr) &&
                Objects.equals(region, that.region) &&
                Objects.equals(images, that.images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderSn, reason, problemDesc, applyVouchers, returnNum, skuId, shipName, shipMobile, shipAddr, region, images);
    }

    @Override
    public String toString() {
        return "AfterSaleApplyVO{" +
                "orderSn='" + orderSn + '\'' +
                ", reason='" + reason + '\'' +
                ", problemDesc='" + problemDesc + '\'' +
                ", applyVouchers='" + applyVouchers + '\'' +
                ", returnNum=" + returnNum +
                ", skuId=" + skuId +
                ", shipName='" + shipName + '\'' +
                ", shipMobile='" + shipMobile + '\'' +
                ", shipAddr='" + shipAddr + '\'' +
                ", region=" + region +
                ", images=" + images +
                '}';
    }
}
