package com.enation.app.javashop.core.aftersale.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 申请售后服务--申请退货VO
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-18
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReturnGoodsVO extends AfterSaleApplyVO implements Serializable {

    private static final long serialVersionUID = -2105785005872712137L;

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
        if (!super.equals(o)) {
            return false;
        }
        ReturnGoodsVO that = (ReturnGoodsVO) o;
        return Objects.equals(accountType, that.accountType) &&
                Objects.equals(returnAccount, that.returnAccount) &&
                Objects.equals(bankName, that.bankName) &&
                Objects.equals(bankAccountNumber, that.bankAccountNumber) &&
                Objects.equals(bankAccountName, that.bankAccountName) &&
                Objects.equals(bankDepositName, that.bankDepositName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), accountType, returnAccount, bankName, bankAccountNumber, bankAccountName, bankDepositName);
    }

    @Override
    public String toString() {
        return "ReturnGoodsVO{" +
                "accountType='" + accountType + '\'' +
                ", returnAccount='" + returnAccount + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                ", bankAccountName='" + bankAccountName + '\'' +
                ", bankDepositName='" + bankDepositName + '\'' +
                '}';
    }
}
