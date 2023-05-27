package com.enation.app.javashop.core.aftersale.model.dos;

import java.io.Serializable;
import java.util.Objects;

import com.enation.app.javashop.core.aftersale.model.vo.ApplyAfterSaleVO;
import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Table;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 售后退款单实体
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-21
 */
@Table(name="es_refund")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RefundDO extends AfterSaleBase implements Serializable {

    private static final long serialVersionUID = 3043923149260205L;

    /**
     * 退款方式(原路退回，线下支付)
     */
    @Column(name = "refund_way")
    @ApiModelProperty(name="refund_way", value="退款方式 ORIGINAL：原路退回，OFFLINE：线下支付")
    private String refundWay;
    /**
     * 退款账户类型
     */
    @Column(name = "account_type")
    @ApiModelProperty(name="account_type",value="退款账户类型")
    private String accountType;
    /**
     * 退款账户
     */
    @Column(name = "return_account")
    @ApiModelProperty(name="return_account",value="退款账户")
    private String returnAccount;
    /**
     * 银行名称
     */
    @Column(name = "bank_name")
    @ApiModelProperty(name="bank_name",value="银行名称")
    private String bankName;
    /**
     * 银行账号
     */
    @Column(name = "bank_account_number")
    @ApiModelProperty(name="bank_account_number",value="银行账号")
    private String bankAccountNumber;
    /**
     * 银行开户名
     */
    @Column(name = "bank_account_name")
    @ApiModelProperty(name="bank_account_name",value="银行开户名")
    private String bankAccountName;
    /**
     * 银行开户行
     */
    @Column(name = "bank_deposit_name")
    @ApiModelProperty(name="bank_deposit_name",value="银行开户行")
    private String bankDepositName;
    /**
     * 退款单状态 APPLY：新申请，REFUNDING：退款中，REFUNDFAIL：退款失败，COMPLETE：完成
     */
    @Column(name = "refund_status")
    @ApiModelProperty(name="refund_status",value="退款单状态 APPLY：新申请，REFUNDING：退款中，REFUNDFAIL：退款失败，COMPLETE：完成")
    private String refundStatus;
    /**
     * 申请退款金额
     */
    @Column(name = "refund_price")
    @ApiModelProperty(name="refund_price",value="申请退款金额")
    private Double refundPrice;
    /**
     * 商家同意退款金额
     */
    @Column(name = "agree_price")
    @ApiModelProperty(name = "agree_price", value = "商家同意退款金额")
    private Double agreePrice;
    /**
     * 实际退款金额
     */
    @Column(name = "actual_price")
    @ApiModelProperty(name="actual_price",value="实际退款金额")
    private Double actualPrice;
    /**
     * 订单付款类型 ONLINE：在线支付，COD：货到付款
     */
    @Column(name = "payment_type")
    @ApiModelProperty(name="payment_type",value="订单付款类型 ONLINE：在线支付，COD：货到付款")
    private String paymentType;
    /**
     * 支付结果交易号
     */
    @Column(name = "pay_order_no")
    @ApiModelProperty(name="pay_order_no",value="支付结果交易号")
    private String payOrderNo;
    /**
     * 退款失败原因
     */
    @Column(name = "refund_fail_reason")
    @ApiModelProperty(name = "refund_fail_reason",value = "退款失败原因")
    private String refundFailReason;
    /**
     * 退款时间
     */
    @Column(name = "refund_time")
    @ApiModelProperty(name = "refund_time",value = "退款时间")
    private Long refundTime;

    public String getRefundWay() {
        return refundWay;
    }

    public void setRefundWay(String refundWay) {
        this.refundWay = refundWay;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getReturnAccount() {
        return returnAccount;
    }

    public void setReturnAccount(String returnAccount) {
        this.returnAccount = returnAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankDepositName() {
        return bankDepositName;
    }

    public void setBankDepositName(String bankDepositName) {
        this.bankDepositName = bankDepositName;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Double getRefundPrice() {
        return refundPrice;
    }

    public void setRefundPrice(Double refundPrice) {
        this.refundPrice = refundPrice;
    }

    public Double getAgreePrice() {
        return agreePrice;
    }

    public void setAgreePrice(Double agreePrice) {
        this.agreePrice = agreePrice;
    }

    public Double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    public String getRefundFailReason() {
        return refundFailReason;
    }

    public void setRefundFailReason(String refundFailReason) {
        this.refundFailReason = refundFailReason;
    }

    public Long getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Long refundTime) {
        this.refundTime = refundTime;
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
        RefundDO refundDO = (RefundDO) o;
        return Objects.equals(refundWay, refundDO.refundWay) &&
                Objects.equals(accountType, refundDO.accountType) &&
                Objects.equals(returnAccount, refundDO.returnAccount) &&
                Objects.equals(bankName, refundDO.bankName) &&
                Objects.equals(bankAccountNumber, refundDO.bankAccountNumber) &&
                Objects.equals(bankAccountName, refundDO.bankAccountName) &&
                Objects.equals(bankDepositName, refundDO.bankDepositName) &&
                Objects.equals(refundStatus, refundDO.refundStatus) &&
                Objects.equals(refundPrice, refundDO.refundPrice) &&
                Objects.equals(agreePrice, refundDO.agreePrice) &&
                Objects.equals(actualPrice, refundDO.actualPrice) &&
                Objects.equals(paymentType, refundDO.paymentType) &&
                Objects.equals(payOrderNo, refundDO.payOrderNo) &&
                Objects.equals(refundFailReason, refundDO.refundFailReason) &&
                Objects.equals(refundTime, refundDO.refundTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), refundWay, accountType, returnAccount, bankName, bankAccountNumber, bankAccountName, bankDepositName, refundStatus, refundPrice, agreePrice, actualPrice, paymentType, payOrderNo, refundFailReason, refundTime);
    }

    @Override
    public String toString() {
        return "RefundDO{" +
                "refundWay='" + refundWay + '\'' +
                ", accountType='" + accountType + '\'' +
                ", returnAccount='" + returnAccount + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                ", bankAccountName='" + bankAccountName + '\'' +
                ", bankDepositName='" + bankDepositName + '\'' +
                ", refundStatus='" + refundStatus + '\'' +
                ", refundPrice=" + refundPrice +
                ", agreePrice=" + agreePrice +
                ", actualPrice=" + actualPrice +
                ", paymentType='" + paymentType + '\'' +
                ", payOrderNo='" + payOrderNo + '\'' +
                ", refundFailReason='" + refundFailReason + '\'' +
                ", refundTime=" + refundTime +
                '}';
    }
}
