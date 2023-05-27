package com.enation.app.javashop.core.trade.order.model.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 发货单vo
 *
 * @author zh
 * @version v7.0
 * @date 2019/11/5 9:30 AM
 * @since v7.0
 */

public class InvoiceVO {

    @ApiModelProperty(value = "商城logo")
    private String logo;

    @ApiModelProperty(value = "站点名称")
    private String siteName;

    @ApiModelProperty(value = "站点地址")
    private String siteAddress;

    @ApiModelProperty(value = "会员名称")
    private String memberName;

    @ApiModelProperty(value = "收货人")
    private String consignee;

    @ApiModelProperty(value = "订单创建日期")
    private long orderCreateTime;

    @ApiModelProperty(value = "打印日期")
    private long createTime;

    @ApiModelProperty(value = "订单编号")
    private String sn;

    @ApiModelProperty(value = "sku列表")
    private List<OrderSkuVO> orderSkuList;

    @ApiModelProperty(value = "电话号码")
    private String phone;

    @ApiModelProperty(value = "收货地区")
    private String region;

    @ApiModelProperty(value = "收货地址")
    private String address;


    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public long getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(long orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public List<OrderSkuVO> getOrderSkuList() {
        return orderSkuList;
    }

    public void setOrderSkuList(List<OrderSkuVO> orderSkuList) {
        this.orderSkuList = orderSkuList;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public String toString() {
        return "InvoiceVO{" +
                "logo='" + logo + '\'' +
                ", siteName='" + siteName + '\'' +
                ", siteAddress='" + siteAddress + '\'' +
                ", memberName='" + memberName + '\'' +
                ", consignee='" + consignee + '\'' +
                ", orderCreateTime=" + orderCreateTime +
                ", createTime=" + createTime +
                ", sn='" + sn + '\'' +
                ", orderSkuList=" + orderSkuList +
                ", phone='" + phone + '\'' +
                ", region='" + region + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
