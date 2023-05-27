package com.enation.app.javashop.core.member.model.dto;

import com.enation.app.javashop.core.base.context.Region;
import com.enation.app.javashop.core.base.context.RegionFormat;
import com.enation.app.javashop.framework.database.annotation.Column;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 会员开票历史记录DTO
 *
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-09-16
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReceiptHistoryDTO implements Serializable {

    private static final long serialVersionUID = -1942168604735027195L;

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
    @ApiModelProperty(name = "receipt_type", value = "发票类型 ELECTRO：电子普通发票，VATORDINARY：增值税普通发票，VATOSPECIAL：增值税专用发票", required = true, example = "ELECTRO：电子普通发票，VATORDINARY：增值税普通发票，VATOSPECIAL：增值税专用发票")
    private String receiptType;
    /**
     * 发票抬头
     */
    @Column(name = "receipt_title")
    @ApiModelProperty(name = "receipt_title", value = "发票抬头", required = true)
    private String receiptTitle;
    /**
     * 发票内容
     */
    @Column(name = "receipt_content")
    @ApiModelProperty(name = "receipt_content", value = "发票内容", required = true)
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
     * 收票地址--所属地区信息
     */
    @RegionFormat
    @ApiModelProperty(name = "region", value = "收票地址--所属地区信息")
    private Region region;
    /**
     * 收票地址--详细地址
     */
    @Column(name = "detail_addr")
    @ApiModelProperty(name = "detail_addr", value = "收票地址--详细地址", required = false)
    private String detailAddr;

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

    @JsonIgnore
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getDetailAddr() {
        return detailAddr;
    }

    public void setDetailAddr(String detailAddr) {
        this.detailAddr = detailAddr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReceiptHistoryDTO that = (ReceiptHistoryDTO) o;
        return Objects.equals(receiptMethod, that.receiptMethod) &&
                Objects.equals(receiptType, that.receiptType) &&
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
                Objects.equals(region, that.region) &&
                Objects.equals(detailAddr, that.detailAddr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receiptMethod, receiptType, receiptTitle, receiptContent, taxNo, regAddr, regTel, bankName, bankAccount, memberName, memberMobile, memberEmail, region, detailAddr);
    }

    @Override
    public String toString() {
        return "ReceiptHistoryDTO{" +
                "receiptMethod='" + receiptMethod + '\'' +
                ", receiptType='" + receiptType + '\'' +
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
                ", region=" + region +
                ", detailAddr='" + detailAddr + '\'' +
                '}';
    }
}
