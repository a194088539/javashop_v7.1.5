package com.enation.app.javashop.core.aftersale.model.dos;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 售后退款/退货信息实体
 * 用于用户新申请退货、退款存放的退款账户相关信息
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-15
 */
@Table(name = "es_as_refund")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AfterSaleRefundDO implements Serializable {

    private static final long serialVersionUID = -1374141503656012554L;

    /**
     * 主键ID
     */
    @Id(name = "id")
    @ApiModelProperty(hidden=true)
    private Integer id;
    /**
     * 售后服务单号
     */
    @Column(name = "service_sn")
    @ApiModelProperty(name = "service_sn", value = "售后服务单号", required = false)
    private String serviceSn;
    /**
     * 申请退款金额
     */
    @Column(name = "refund_price")
    @ApiModelProperty(name = "refund_price", value = "申请退款金额", required = false)
    private Double refundPrice;
    /**
     * 商家同意退款金额
     */
    @Column(name = "agree_price")
    @ApiModelProperty(name = "agree_price", value = "商家同意退款金额", required = false)
    private Double agreePrice;
    /**
     * 实际退款金额
     */
    @Column(name = "actual_price")
    @ApiModelProperty(name = "actual_price", value = "实际退款金额", required = false)
    private Double actualPrice;
    /**
     * 退款方式 ORIGINAL：原路退回，OFFLINE：线下支付
     */
    @Column(name = "refund_way")
    @ApiModelProperty(name = "refund_way", value = "退款方式 ORIGINAL：原路退回，OFFLINE：线下支付", required = false)
    private String refundWay;
    /**
     * 账号类型
     */
    @Column(name = "account_type")
    @ApiModelProperty(name = "account_type", value = "账号类型", required = false)
    private String accountType;
    /**
     * 退款账号
     */
    @Column(name = "return_account")
    @ApiModelProperty(name = "return_account", value = "退款账号", required = false)
    private String returnAccount;
    /**
     * 银行名称
     */
    @Column(name = "bank_name")
    @ApiModelProperty(name = "bank_name", value = "银行名称", required = false)
    private String bankName;
    /**
     * 银行账户
     */
    @Column(name = "bank_account_number")
    @ApiModelProperty(name = "bank_account_number", value = "银行账户", required = false)
    private String bankAccountNumber;
    /**
     * 银行开户名
     */
    @Column(name = "bank_account_name")
    @ApiModelProperty(name = "bank_account_name", value = "银行开户名", required = false)
    private String bankAccountName;
    /**
     * 银行开户行
     */
    @Column(name = "bank_deposit_name")
    @ApiModelProperty(name = "bank_deposit_name", value = "银行开户行", required = false)
    private String bankDepositName;
    /**
     * 订单支付方式返回的交易号
     */
    @Column(name = "pay_order_no")
    @ApiModelProperty(name = "pay_order_no", value = "订单支付方式返回的交易号", required = false)
    private String payOrderNo;
    /**
     * 退款时间
     */
    @Column(name = "refund_time")
    @ApiModelProperty(name = "refund_time", value = "退款时间", required = false)
    private Long refundTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceSn() {
        return serviceSn;
    }

    public void setServiceSn(String serviceSn) {
        this.serviceSn = serviceSn;
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

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
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
        AfterSaleRefundDO that = (AfterSaleRefundDO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(serviceSn, that.serviceSn) &&
                Objects.equals(refundPrice, that.refundPrice) &&
                Objects.equals(agreePrice, that.agreePrice) &&
                Objects.equals(actualPrice, that.actualPrice) &&
                Objects.equals(refundWay, that.refundWay) &&
                Objects.equals(accountType, that.accountType) &&
                Objects.equals(returnAccount, that.returnAccount) &&
                Objects.equals(bankName, that.bankName) &&
                Objects.equals(bankAccountNumber, that.bankAccountNumber) &&
                Objects.equals(bankAccountName, that.bankAccountName) &&
                Objects.equals(bankDepositName, that.bankDepositName) &&
                Objects.equals(payOrderNo, that.payOrderNo) &&
                Objects.equals(refundTime, that.refundTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceSn, refundPrice, agreePrice, actualPrice, refundWay, accountType, returnAccount, bankName, bankAccountNumber, bankAccountName, bankDepositName, payOrderNo, refundTime);
    }

    @Override
    public String toString() {
        return "AfterSaleRefundDO{" +
                "id=" + id +
                ", serviceSn='" + serviceSn + '\'' +
                ", refundPrice=" + refundPrice +
                ", agreePrice=" + agreePrice +
                ", actualPrice=" + actualPrice +
                ", refundWay='" + refundWay + '\'' +
                ", accountType='" + accountType + '\'' +
                ", returnAccount='" + returnAccount + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                ", bankAccountName='" + bankAccountName + '\'' +
                ", bankDepositName='" + bankDepositName + '\'' +
                ", payOrderNo='" + payOrderNo + '\'' +
                ", refundTime=" + refundTime +
                '}';
    }
}
