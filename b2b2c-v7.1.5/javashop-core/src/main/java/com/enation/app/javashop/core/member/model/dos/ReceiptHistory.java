package com.enation.app.javashop.core.member.model.dos;

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
 * 会员开票历史记录实体
 *
 * @author duanmingyu
 * @version v7.1.4
 * @since v7.0.0
 * 2019-06-20
 */
@Table(name = "es_receipt_history")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReceiptHistory implements Serializable {

    private static final long serialVersionUID = 7024661438767317L;

    /**
     * 主键ID
     */
    @Id(name = "history_id")
    @ApiModelProperty(hidden = true)
    private Integer historyId;
    /**
     * 订单编号
     */
    @Column(name = "order_sn")
    @ApiModelProperty(name = "order_sn", value = "订单编号", required = false)
    private String orderSn;
    /**
     * 订单金额
     */
    @Column(name = "order_price")
    @ApiModelProperty(name = "order_price", value = "订单金额", required = false)
    private Double orderPrice;
    /**
     * 商家ID
     */
    @Column(name = "seller_id")
    @ApiModelProperty(name = "seller_id", value = "商家ID", required = false)
    private Integer sellerId;
    /**
     * 商家名称
     */
    @Column(name = "seller_name")
    @ApiModelProperty(name = "seller_name", value = "商家名称", required = false)
    private String sellerName;
    /**
     * 会员ID
     */
    @Column(name = "member_id")
    @ApiModelProperty(name = "member_id", value = "会员ID", required = false)
    private Integer memberId;

    /**
     * 会员名称
     */
    @Column(name = "uname")
    @ApiModelProperty(name = "uname", value = "会员名称", required = false)
    private String uname;

    /**
     * 开票状态 0：未开，1：已开
     */
    @Column(name = "status")
    @ApiModelProperty(name = "status", value = "开票状态 0：未开，1：已开", required = false)
    private Integer status;
    /**
     * 开票方式 针对增值税专用发票，暂时只有"订单完成后开票"一种方式
     */
    @Column(name = "receipt_method")
    @ApiModelProperty(name = "receipt_method", value = "开票方式", required = false)
    private String receiptMethod;
    /**
     * 发票类型 ELECTRO：电子普通发票，VATORDINARY：增值税普通发票，VATOSPECIAL：增值税专用发票
     */
    @Column(name = "receipt_type")
    @ApiModelProperty(name = "receipt_type", value = "发票类型", required = false, example = "ELECTRO：电子普通发票，VATORDINARY：增值税普通发票，VATOSPECIAL：增值税专用发票")
    private String receiptType;
    /**
     * 物流公司ID
     */
    @Column(name = "logi_id")
    @ApiModelProperty(name = "logi_id", value = "物流公司ID", required = false)
    private Integer logiId;
    /**
     * 物流公司名称
     */
    @Column(name = "logi_name")
    @ApiModelProperty(name = "logi_name", value = "物流公司名称", required = false)
    private String logiName;
    /**
     * 快递单号
     */
    @Column(name = "logi_code")
    @ApiModelProperty(name = "logi_code", value = "快递单号", required = false)
    private String logiCode;
    /**
     * 发票抬头
     */
    @Column(name = "receipt_title")
    @ApiModelProperty(name = "receipt_title", value = "发票抬头", required = false)
    private String receiptTitle;
    /**
     * 发票内容
     */
    @Column(name = "receipt_content")
    @ApiModelProperty(name = "receipt_content", value = "发票内容", required = false)
    private String receiptContent;
    /**
     * 纳税人识别号
     */
    @Column(name = "tax_no")
    @ApiModelProperty(name = "tax_no", value = "纳税人识别号", required = false)
    private String taxNo;
    /**
     * 注册地址
     */
    @Column(name = "reg_addr")
    @ApiModelProperty(name = "reg_addr", value = "注册地址", required = false)
    private String regAddr;
    /**
     * 注册电话
     */
    @Column(name = "reg_tel")
    @ApiModelProperty(name = "reg_tel", value = "注册电话", required = false)
    private String regTel;
    /**
     * 开户银行
     */
    @Column(name = "bank_name")
    @ApiModelProperty(name = "bank_name", value = "开户银行", required = false)
    private String bankName;
    /**
     * 银行账户
     */
    @Column(name = "bank_account")
    @ApiModelProperty(name = "bank_account", value = "银行账户", required = false)
    private String bankAccount;
    /**
     * 收票人姓名
     */
    @Column(name = "member_name")
    @ApiModelProperty(name = "member_name", value = "收票人姓名", required = false)
    private String memberName;
    /**
     * 收票人手机号
     */
    @Column(name = "member_mobile")
    @ApiModelProperty(name = "member_mobile", value = "收票人手机号", required = false)
    private String memberMobile;
    /**
     * 收票人邮箱
     */
    @Column(name = "member_email")
    @ApiModelProperty(name = "member_email", value = "收票人邮箱", required = false)
    private String memberEmail;
    /**
     * 收票地址--所属省份ID
     */
    @Column(name = "province_id")
    @ApiModelProperty(name = "province_id", value = "收票地址--所属省份ID", required = false)
    private Integer provinceId;

    /**
     * 收票地址--所属城市ID
     */
    @Column(name = "city_id")
    @ApiModelProperty(name = "city_id", value = "收票地址--所属城市ID", required = false)
    private Integer cityId;

    /**
     * 收票地址--所属区县ID
     */
    @Column(name = "county_id")
    @ApiModelProperty(name = "county_id", value = "收票地址--所属区县ID", required = false)
    private Integer countyId;

    /**
     * 收票地址--所属乡镇ID
     */
    @Column(name = "town_id")
    @ApiModelProperty(name = "town_id", value = "收票地址--所属乡镇ID", required = false)
    private Integer townId;

    /**
     * 收票地址--所属省份
     */
    @Column(name = "province")
    @ApiModelProperty(name = "province", value = "收票地址--所属省份", required = false)
    private String province;

    /**
     * 收票地址--所属城市
     */
    @Column(name = "city")
    @ApiModelProperty(name = "city", value = "收票地址--所属城市", required = false)
    private String city;

    /**
     * 收票地址--所属区县
     */
    @Column(name = "county")
    @ApiModelProperty(name = "county", value = "收票地址--所属区县", required = false)
    private String county;

    /**
     * 收票地址--所属乡镇
     */
    @Column(name = "town")
    @ApiModelProperty(name = "town", value = "收票地址--所属乡镇", required = false)
    private String town;

    /**
     * 收票地址--详细地址
     */
    @Column(name = "detail_addr")
    @ApiModelProperty(name = "detail_addr", value = "收票地址--详细地址", required = false)
    private String detailAddr;
    /**
     * 开票时间
     */
    @Column(name = "add_time")
    @ApiModelProperty(name = "add_time", value = "开票时间", required = false)
    private Long addTime;

    /**
     * 商品数据json
     */
    @Column(name = "goods_json")
    @ApiModelProperty(name = "goods_json", value = "商品数据json", required = false)
    private String goodsJson;

    /**
     * 商品数据json
     */
    @Column(name = "order_status")
    @ApiModelProperty(name = "order_status", value = "订单状态，NEW或者CONFIRM，出库成功的状态才可会被下一步", required = false)
    private String orderStatus;

    @PrimaryKeyField
    public Integer getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReceiptMethod() {
        return receiptMethod;
    }

    public void setReceiptMethod(String receiptMethod) {
        this.receiptMethod = receiptMethod;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public Integer getLogiId() {
        return logiId;
    }

    public void setLogiId(Integer logiId) {
        this.logiId = logiId;
    }

    public String getLogiName() {
        return logiName;
    }

    public void setLogiName(String logiName) {
        this.logiName = logiName;
    }

    public String getLogiCode() {
        return logiCode;
    }

    public void setLogiCode(String logiCode) {
        this.logiCode = logiCode;
    }

    public String getReceiptTitle() {
        return receiptTitle;
    }

    public void setReceiptTitle(String receiptTitle) {
        this.receiptTitle = receiptTitle;
    }

    public String getReceiptContent() {
        return receiptContent;
    }

    public void setReceiptContent(String receiptContent) {
        this.receiptContent = receiptContent;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getRegAddr() {
        return regAddr;
    }

    public void setRegAddr(String regAddr) {
        this.regAddr = regAddr;
    }

    public String getRegTel() {
        return regTel;
    }

    public void setRegTel(String regTel) {
        this.regTel = regTel;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberMobile() {
        return memberMobile;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getCountyId() {
        return countyId;
    }

    public void setCountyId(Integer countyId) {
        this.countyId = countyId;
    }

    public Integer getTownId() {
        return townId;
    }

    public void setTownId(Integer townId) {
        this.townId = townId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getDetailAddr() {
        return detailAddr;
    }

    public void setDetailAddr(String detailAddr) {
        this.detailAddr = detailAddr;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public String getGoodsJson() {
        return goodsJson;
    }

    public void setGoodsJson(String goodsJson) {
        this.goodsJson = goodsJson;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReceiptHistory that = (ReceiptHistory) o;
        return Objects.equals(historyId, that.historyId) &&
                Objects.equals(orderSn, that.orderSn) &&
                Objects.equals(orderPrice, that.orderPrice) &&
                Objects.equals(sellerId, that.sellerId) &&
                Objects.equals(sellerName, that.sellerName) &&
                Objects.equals(memberId, that.memberId) &&
                Objects.equals(status, that.status) &&
                Objects.equals(receiptMethod, that.receiptMethod) &&
                Objects.equals(receiptType, that.receiptType) &&
                Objects.equals(logiId, that.logiId) &&
                Objects.equals(logiName, that.logiName) &&
                Objects.equals(logiCode, that.logiCode) &&
                Objects.equals(receiptTitle, that.receiptTitle) &&
                Objects.equals(receiptContent, that.receiptContent) &&
                Objects.equals(taxNo, that.taxNo) &&
                Objects.equals(regAddr, that.regAddr) &&
                Objects.equals(regTel, that.regTel) &&
                Objects.equals(bankName, that.bankName) &&
                Objects.equals(bankAccount, that.bankAccount) &&
                Objects.equals(memberName, that.memberName) &&
                Objects.equals(memberMobile, that.memberMobile) &&
                Objects.equals(memberEmail, that.memberEmail) &&
                Objects.equals(provinceId, that.provinceId) &&
                Objects.equals(cityId, that.cityId) &&
                Objects.equals(countyId, that.countyId) &&
                Objects.equals(townId, that.townId) &&
                Objects.equals(province, that.province) &&
                Objects.equals(city, that.city) &&
                Objects.equals(county, that.county) &&
                Objects.equals(town, that.town) &&
                Objects.equals(detailAddr, that.detailAddr) &&
                Objects.equals(addTime, that.addTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(historyId, orderSn, orderPrice, sellerId, sellerName, memberId, status, receiptMethod, receiptType, logiId, logiName, logiCode, receiptTitle, receiptContent, taxNo, regAddr, regTel, bankName, bankAccount, memberName, memberMobile, memberEmail, provinceId, cityId, countyId, townId, province, city, county, town, detailAddr, addTime);
    }

    @Override
    public String toString() {
        return "ReceiptHistory{" +
                "historyId=" + historyId +
                ", orderSn='" + orderSn + '\'' +
                ", orderPrice=" + orderPrice +
                ", sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                ", memberId=" + memberId +
                ", status=" + status +
                ", receiptMethod='" + receiptMethod + '\'' +
                ", receiptType='" + receiptType + '\'' +
                ", logiId='" + logiId +
                ", logiName='" + logiName + '\'' +
                ", logiCode='" + logiCode + '\'' +
                ", receiptTitle='" + receiptTitle + '\'' +
                ", receiptContent='" + receiptContent + '\'' +
                ", taxNo='" + taxNo + '\'' +
                ", regAddr='" + regAddr + '\'' +
                ", regTel='" + regTel + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", memberName='" + memberName + '\'' +
                ", memberMobile='" + memberMobile + '\'' +
                ", memberEmail='" + memberEmail + '\'' +
                ", provinceId=" + provinceId +
                ", cityId=" + cityId +
                ", countyId=" + countyId +
                ", townId=" + townId +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", town='" + town + '\'' +
                ", detailAddr='" + detailAddr + '\'' +
                ", addTime=" + addTime +
                '}';
    }
}