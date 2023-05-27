package com.enation.app.javashop.core.aftersale.model.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 售后服务导出实体
 * 用于管理端和商家端导出售后服务信息时使用
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-11-21
 */
public class AfterSaleExportVO implements Serializable {

    private static final long serialVersionUID = -4015545762774462005L;

    @ApiModelProperty(name = "service_sn", value = "售后服务单号")
    private String serviceSn;

    @ApiModelProperty(name = "order_sn", value = "订单编号")
    private String orderSn;

    @ApiModelProperty(name="create_time",value="申请时间")
    private Long createTime;

    @ApiModelProperty(name = "member_name", value = "申请会员")
    private String memberName;

    @ApiModelProperty(name="seller_name",value="所属商家")
    private String sellerName;

    @ApiModelProperty(name="mobile",value="会员联系方式")
    private String mobile;

    @ApiModelProperty(name = "service_type", value = "售后类型 RETURN_GOODS：退货，CHANGE_GOODS：换货，SUPPLY_AGAIN_GOODS：补发货品，ORDER_CANCEL：取消订单（订单确认付款且未收货之前）")
    private String serviceType;

    @ApiModelProperty(name = "service_status", value = "售后单状态 APPLY：申请，PASS：审核通过，REFUSE：审核拒绝，WAIT_FOR_MANUAL：待人工处理，STOCK_IN：退货入库，REFUNDING：退款中，REFUNDFAIL：退款失败，COMPLETE：完成")
    private String serviceStatus;

    @ApiModelProperty(name = "service_type_text", value = "售后类型 退货，换货，补发货品，取消订单")
    private String serviceTypeText;

    @ApiModelProperty(name = "service_status_text", value = "售后单状态 待审核，审核通过，审核未通过，已退还商品，待人工处理，入库，退款中，退款失败，完成")
    private String serviceStatusText;

    @ApiModelProperty(name = "reason", value = "申请原因")
    private String reason;

    @ApiModelProperty(name = "problem_desc", value = "问题描述")
    private String problemDesc;

    @ApiModelProperty(name = "apply_vouchers", value = "申请凭证")
    private String applyVouchers;

    @ApiModelProperty(name = "audit_remark", value = "审核备注")
    private String auditRemark;

    @ApiModelProperty(name = "stock_remark", value = "入库备注")
    private String stockRemark;

    @ApiModelProperty(name = "refund_remark", value = "退款备注")
    private String refundRemark;

    @ApiModelProperty(name = "refund_price", value = "申请退款金额")
    private Double refundPrice;

    @ApiModelProperty(name = "agree_price", value = "商家同意退款金额")
    private Double agreePrice;

    @ApiModelProperty(name = "actual_price", value = "实际退款金额")
    private Double actualPrice;

    @ApiModelProperty(name = "refund_time", value = "退款时间")
    private Long refundTime;

    @ApiModelProperty(name = "refund_way", value = "退款方式 ORIGINAL：原路退回，OFFLINE：账户退款")
    private String refundWay;

    @ApiModelProperty(name = "refund_way_text", value = "退款方式 原路退回，账户退款")
    private String refundWayText;

    @ApiModelProperty(name = "account_type", value = "账号类型")
    private String accountType;

    @ApiModelProperty(name = "account_type_text", value = "账号类型 支付宝，微信，银行卡")
    private String accountTypeText;

    @ApiModelProperty(name = "return_account", value = "退款账号")
    private String returnAccount;

    @ApiModelProperty(name = "bank_name", value = "银行名称")
    private String bankName;

    @ApiModelProperty(name = "bank_account_number", value = "银行账户")
    private String bankAccountNumber;

    @ApiModelProperty(name = "bank_account_name", value = "银行开户名")
    private String bankAccountName;

    @ApiModelProperty(name = "bank_deposit_name", value = "银行开户行")
    private String bankDepositName;

    @ApiModelProperty(name = "goods_info", value = "申请售后商品信息")
    private String goodsInfo;

    @ApiModelProperty(name = "express_info", value = "售后物流信息")
    private String expressInfo;

    @ApiModelProperty(name = "rog_info", value = "收货地址信息")
    private String rogInfo;

    public String getServiceSn() {
        return serviceSn;
    }

    public void setServiceSn(String serviceSn) {
        this.serviceSn = serviceSn;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getServiceTypeText() {
        return serviceTypeText;
    }

    public void setServiceTypeText(String serviceTypeText) {
        this.serviceTypeText = serviceTypeText;
    }

    public String getServiceStatusText() {
        return serviceStatusText;
    }

    public void setServiceStatusText(String serviceStatusText) {
        this.serviceStatusText = serviceStatusText;
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

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public String getStockRemark() {
        return stockRemark;
    }

    public void setStockRemark(String stockRemark) {
        this.stockRemark = stockRemark;
    }

    public String getRefundRemark() {
        return refundRemark;
    }

    public void setRefundRemark(String refundRemark) {
        this.refundRemark = refundRemark;
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

    public Long getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Long refundTime) {
        this.refundTime = refundTime;
    }

    public String getRefundWay() {
        return refundWay;
    }

    public void setRefundWay(String refundWay) {
        this.refundWay = refundWay;
    }

    public String getRefundWayText() {
        return refundWayText;
    }

    public void setRefundWayText(String refundWayText) {
        this.refundWayText = refundWayText;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountTypeText() {
        return accountTypeText;
    }

    public void setAccountTypeText(String accountTypeText) {
        this.accountTypeText = accountTypeText;
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

    public String getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(String goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public String getExpressInfo() {
        return expressInfo;
    }

    public void setExpressInfo(String expressInfo) {
        this.expressInfo = expressInfo;
    }

    public String getRogInfo() {
        return rogInfo;
    }

    public void setRogInfo(String rogInfo) {
        this.rogInfo = rogInfo;
    }

    @Override
    public String toString() {
        return "AfterSaleExportVO{" +
                "serviceSn='" + serviceSn + '\'' +
                ", orderSn='" + orderSn + '\'' +
                ", createTime=" + createTime +
                ", memberName='" + memberName + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", serviceStatus='" + serviceStatus + '\'' +
                ", serviceTypeText='" + serviceTypeText + '\'' +
                ", serviceStatusText='" + serviceStatusText + '\'' +
                ", reason='" + reason + '\'' +
                ", problemDesc='" + problemDesc + '\'' +
                ", applyVouchers='" + applyVouchers + '\'' +
                ", auditRemark='" + auditRemark + '\'' +
                ", stockRemark='" + stockRemark + '\'' +
                ", refundRemark='" + refundRemark + '\'' +
                ", refundPrice=" + refundPrice +
                ", agreePrice=" + agreePrice +
                ", actualPrice=" + actualPrice +
                ", refundTime=" + refundTime +
                ", refundWay='" + refundWay + '\'' +
                ", refundWayText='" + refundWayText + '\'' +
                ", accountType='" + accountType + '\'' +
                ", accountTypeText='" + accountTypeText + '\'' +
                ", returnAccount='" + returnAccount + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                ", bankAccountName='" + bankAccountName + '\'' +
                ", bankDepositName='" + bankDepositName + '\'' +
                ", goodsInfo='" + goodsInfo + '\'' +
                ", expressInfo='" + expressInfo + '\'' +
                ", rogInfo='" + rogInfo + '\'' +
                '}';
    }
}
