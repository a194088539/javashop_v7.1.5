package com.enation.app.javashop.core.aftersale.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 取消订单VO
 * 当订单已付款未收货时，用户申请取消订单时使用
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-22
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RefundApplyVO implements Serializable {

    private static final long serialVersionUID = -7411639410731062677L;

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
     * 接收取消结果短信通知的手机号码
     */
    @ApiModelProperty(name = "mobile", value = "手机号码", required = true, dataType = "String")
    private String mobile;
    /**
     * 退款金额
     */
    @ApiModelProperty(name = "refund_price", value = "退款金额", required = true)
    private Double refundPrice;
    /**
     * 账号类型 ALIPAY：支付宝，WEIXINPAY：微信，BANKTRANSFER：银行转账
     */
    @ApiModelProperty(name = "account_type", value = "账号类型 ALIPAY：支付宝，WEIXINPAY：微信，BANKTRANSFER：银行转账", dataType = "String", allowableValues = "ALIPAY,WEIXINPAY,BANKTRANSFER")
    private String accountType;
    /**
     * 退款账号
     */
    @ApiModelProperty(name = "return_account", value = "退款账号", dataType = "String")
    private String returnAccount;
    /**
     * 银行名称
     */
    @ApiModelProperty(name = "bank_name", value = "银行名称", dataType = "String")
    private String bankName;
    /**
     * 银行账户
     */
    @ApiModelProperty(name = "bank_account_number", value = "银行账户", dataType = "String")
    private String bankAccountNumber;
    /**
     * 银行开户名
     */
    @ApiModelProperty(name = "bank_account_name", value = "银行开户名", dataType = "String")
    private String bankAccountName;
    /**
     * 银行开户行
     */
    @ApiModelProperty(name = "bank_deposit_name", value = "银行开户行", dataType = "String")
    private String bankDepositName;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Double getRefundPrice() {
        return refundPrice;
    }

    public void setRefundPrice(Double refundPrice) {
        this.refundPrice = refundPrice;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RefundApplyVO that = (RefundApplyVO) o;
        return Objects.equals(orderSn, that.orderSn) &&
                Objects.equals(reason, that.reason) &&
                Objects.equals(mobile, that.mobile) &&
                Objects.equals(refundPrice, that.refundPrice) &&
                Objects.equals(accountType, that.accountType) &&
                Objects.equals(returnAccount, that.returnAccount) &&
                Objects.equals(bankName, that.bankName) &&
                Objects.equals(bankAccountNumber, that.bankAccountNumber) &&
                Objects.equals(bankAccountName, that.bankAccountName) &&
                Objects.equals(bankDepositName, that.bankDepositName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderSn, reason, mobile, refundPrice, accountType, returnAccount, bankName, bankAccountNumber, bankAccountName, bankDepositName);
    }

    @Override
    public String toString() {
        return "RefundApplyVO{" +
                "orderSn='" + orderSn + '\'' +
                ", reason='" + reason + '\'' +
                ", mobile='" + mobile + '\'' +
                ", refundPrice=" + refundPrice +
                ", accountType='" + accountType + '\'' +
                ", returnAccount='" + returnAccount + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                ", bankAccountName='" + bankAccountName + '\'' +
                ", bankDepositName='" + bankDepositName + '\'' +
                '}';
    }
}
