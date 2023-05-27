package com.enation.app.javashop.core.orderbill.model.dos;

import java.io.Serializable;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 结算单实体
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-04-26 16:21:26
 */
@Table(name = "es_bill")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Bill implements Serializable {

    private static final long serialVersionUID = 2147807368020181L;

    /**
     * 主键ID
     */
    @Id(name = "bill_id")
    @ApiModelProperty(hidden = true)
    private Integer billId;
    /**
     * 结算单编号
     */
    @Column(name = "bill_sn")
    @ApiModelProperty(name = "bill_sn", value = "结算单编号", required = false)
    private String billSn;
    /**
     * 结算开始时间
     */
    @Column(name = "start_time")
    @ApiModelProperty(name = "start_time", value = "结算开始时间", required = false)
    private Long startTime;
    /**
     * 结算结束时间
     */
    @Column(name = "end_time")
    @ApiModelProperty(name = "end_time", value = "结算结束时间", required = false)
    private Long endTime;
    /**
     * 结算总金额
     */
    @Column(name = "price")
    @ApiModelProperty(name = "price", value = "结算总金额", required = false)
    private Double price;
    /**
     * 佣金
     */
    @Column(name = "commi_price")
    @ApiModelProperty(name = "commi_price", value = "佣金", required = false)
    private Double commiPrice;
    /**
     * 优惠金额
     */
    @Column(name = "discount_price")
    @ApiModelProperty(name = "discount_price", value = "优惠金额", required = false)
    private Double discountPrice;
    /**
     * 状态
     */
    @Column(name = "status")
    @ApiModelProperty(name = "status", value = "状态", required = false)
    private String status;
    /**
     * 账单类型 0月结 1日结 2季度结 3自定义
     */
    @Column(name = "bill_type")
    @ApiModelProperty(name = "bill_type", value = "账单类型", required = false)
    private Integer billType;
    /**
     * 店铺id
     */
    @Column(name = "seller_id")
    @ApiModelProperty(name = "seller_id", value = "店铺id", required = false)
    private Integer sellerId;
    /**
     * 付款时间
     */
    @Column(name = "pay_time")
    @ApiModelProperty(name = "pay_time", value = "付款时间", required = false)
    private Long payTime;
    /**
     * 出账日期
     */
    @Column(name = "create_time")
    @ApiModelProperty(name = "create_time", value = "出账日期", required = false)
    private Long createTime;
    /**
     * 结算金额
     */
    @Column(name = "bill_price")
    @ApiModelProperty(name = "bill_price", value = "结算金额", required = false)
    private Double billPrice;
    /**
     * 退单金额
     */
    @Column(name = "refund_price")
    @ApiModelProperty(name = "refund_price", value = "退单金额", required = false)
    private Double refundPrice;

    @Column(name = "refund_commi_price")
    @ApiModelProperty(name = "refund_commi_price", value = "退还佣金金额", required = false)
    private Double refundCommiPrice;

    /**
     * 账单号
     */
    @Column(name = "sn")
    @ApiModelProperty(name = "sn", value = "账单号", required = false)
    private String sn;
    /**
     * 店铺名称
     */
    @Column(name = "shop_name")
    @ApiModelProperty(name = "shop_name", value = "店铺名称", required = false)
    private String shopName;
    /**
     * 银行开户名
     */
    @Column(name = "bank_account_name")
    @ApiModelProperty(name = "bank_account_name", value = "银行开户名", required = false)
    private String bankAccountName;
    /**
     * 公司银行账号
     */
    @Column(name = "bank_account_number")
    @ApiModelProperty(name = "bank_account_number", value = "公司银行账号", required = false)
    private String bankAccountNumber;
    /**
     * 开户银行支行名称
     */
    @Column(name = "bank_name")
    @ApiModelProperty(name = "bank_name", value = "开户银行支行名称", required = false)
    private String bankName;
    /**
     * 支行联行号
     */
    @Column(name = "bank_code")
    @ApiModelProperty(name = "bank_code", value = "支行联行号", required = false)
    private String bankCode;
    /**
     * 开户银行地址
     */
    @Column(name = "bank_address")
    @ApiModelProperty(name = "bank_address", value = "开户银行地址", required = false)
    private String bankAddress;
    /**
     * 货到付款收入金额
     */
    @Column(name = "cod_price")
    @ApiModelProperty(name = "cod_price", value = "货到付款收入金额", required = false)
    private Double codPrice;

    /**
     * 货到付款后退款金额
     */
    @Column(name = "cod_refund_price")
    @ApiModelProperty(name = "cod_refund_price", value = "货到付款后退款金额", required = false)
    private Double codRefundPrice;



    @Column(name = "distribution_rebate")
    @ApiModelProperty(name = "distribution_rebate", value = "分销返现支出", required = false)
    private Double distributionRebate;

    @Column(name = "distribution_return_rebate")
    @ApiModelProperty(name = "distribution_return_rebate", value = "分销返现支出返还", required = false)
    private Double distributionReturnRebate;

    /**
     * 站点优惠券的佣金
     */
    @Column(name = "site_coupon_commi")
    @ApiModelProperty(name = "site_coupon_commi", value = "站点优惠券的佣金", required = false)
    private Double siteCouponCommi;

    /**
     * 结算周期内订单付款总金额
     */
    @Column(name = "order_total_price")
    @ApiModelProperty(name = "order_total_price", value = "结算周期内订单付款总金额", required = false)
    private Double orderTotalPrice;

    /**
     * 结算周期内订单退款总金额
     */
    @Column(name = "refund_total_price")
    @ApiModelProperty(name = "refund_total_price", value = "结算周期内订单退款总金额", required = false)
    private Double refundTotalPrice;

    @PrimaryKeyField
    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public String getBillSn() {
        return billSn;
    }

    public void setBillSn(String billSn) {
        this.billSn = billSn;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getCommiPrice() {
        return commiPrice;
    }

    public void setCommiPrice(Double commiPrice) {
        this.commiPrice = commiPrice;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Double getBillPrice() {
        return billPrice;
    }

    public void setBillPrice(Double billPrice) {
        this.billPrice = billPrice;
    }

    public Double getRefundPrice() {
        return refundPrice;
    }

    public void setRefundPrice(Double refundPrice) {
        this.refundPrice = refundPrice;
    }

    public Double getRefundCommiPrice() {
        return refundCommiPrice;
    }

    public void setRefundCommiPrice(Double refundCommiPrice) {
        this.refundCommiPrice = refundCommiPrice;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public Double getCodPrice() {
        return codPrice;
    }

    public void setCodPrice(Double codPrice) {
        this.codPrice = codPrice;
    }

    public Double getCodRefundPrice() {
        return codRefundPrice;
    }

    public void setCodRefundPrice(Double codRefundPrice) {
        this.codRefundPrice = codRefundPrice;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Double getDistributionRebate() {
        return distributionRebate;
    }

    public void setDistributionRebate(Double distributionRebate) {
        this.distributionRebate = distributionRebate;
    }

    public Double getDistributionReturnRebate() {
        return distributionReturnRebate;
    }

    public void setDistributionReturnRebate(Double distributionReturnRebate) {
        this.distributionReturnRebate = distributionReturnRebate;
    }

    public Double getSiteCouponCommi() {
        return siteCouponCommi;
    }

    public void setSiteCouponCommi(Double siteCouponCommi) {
        this.siteCouponCommi = siteCouponCommi;
    }

    public Double getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(Double orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public Double getRefundTotalPrice() {
        return refundTotalPrice;
    }

    public void setRefundTotalPrice(Double refundTotalPrice) {
        this.refundTotalPrice = refundTotalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Bill bill = (Bill) o;

        if (billId != null ? !billId.equals(bill.billId) : bill.billId != null) {
            return false;
        }
        if (billSn != null ? !billSn.equals(bill.billSn) : bill.billSn != null) {
            return false;
        }
        if (startTime != null ? !startTime.equals(bill.startTime) : bill.startTime != null) {
            return false;
        }
        if (endTime != null ? !endTime.equals(bill.endTime) : bill.endTime != null) {
            return false;
        }
        if (price != null ? !price.equals(bill.price) : bill.price != null) {
            return false;
        }
        if (commiPrice != null ? !commiPrice.equals(bill.commiPrice) : bill.commiPrice != null) {
            return false;
        }
        if (discountPrice != null ? !discountPrice.equals(bill.discountPrice) : bill.discountPrice != null) {
            return false;
        }
        if (status != null ? !status.equals(bill.status) : bill.status != null) {
            return false;
        }
        if (billType != null ? !billType.equals(bill.billType) : bill.billType != null) {
            return false;
        }
        if (sellerId != null ? !sellerId.equals(bill.sellerId) : bill.sellerId != null) {
            return false;
        }
        if (payTime != null ? !payTime.equals(bill.payTime) : bill.payTime != null) {
            return false;
        }
        if (createTime != null ? !createTime.equals(bill.createTime) : bill.createTime != null) {
            return false;
        }
        if (billPrice != null ? !billPrice.equals(bill.billPrice) : bill.billPrice != null) {
            return false;
        }
        if (refundPrice != null ? !refundPrice.equals(bill.refundPrice) : bill.refundPrice != null) {
            return false;
        }
        if (refundCommiPrice != null ? !refundCommiPrice.equals(bill.refundCommiPrice) : bill.refundCommiPrice != null) {
            return false;
        }
        if (sn != null ? !sn.equals(bill.sn) : bill.sn != null) {
            return false;
        }
        if (shopName != null ? !shopName.equals(bill.shopName) : bill.shopName != null) {
            return false;
        }
        if (bankAccountName != null ? !bankAccountName.equals(bill.bankAccountName) : bill.bankAccountName != null) {
            return false;
        }
        if (bankAccountNumber != null ? !bankAccountNumber.equals(bill.bankAccountNumber) : bill.bankAccountNumber != null) {
            return false;
        }
        if (bankName != null ? !bankName.equals(bill.bankName) : bill.bankName != null) {
            return false;
        }
        if (bankCode != null ? !bankCode.equals(bill.bankCode) : bill.bankCode != null) {
            return false;
        }
        if (bankAddress != null ? !bankAddress.equals(bill.bankAddress) : bill.bankAddress != null) {
            return false;
        }
        if (codPrice != null ? !codPrice.equals(bill.codPrice) : bill.codPrice != null) {
            return false;
        }
        if (codRefundPrice != null ? !codRefundPrice.equals(bill.codRefundPrice) : bill.codRefundPrice != null) {
            return false;
        }
        if (distributionRebate != null ? !distributionRebate.equals(bill.distributionRebate) : bill.distributionRebate != null) {
            return false;
        }
        if (distributionReturnRebate != null ? distributionReturnRebate.equals(bill.distributionReturnRebate) : bill.distributionReturnRebate == null) {
            return false;
        }
        if (orderTotalPrice != null ? !orderTotalPrice.equals(bill.orderTotalPrice) : bill.orderTotalPrice != null) {
            return false;
        }

        return refundTotalPrice != null ? refundTotalPrice.equals(bill.refundTotalPrice) : bill.refundTotalPrice == null;
    }

    @Override
    public int hashCode() {
        int result = billId != null ? billId.hashCode() : 0;
        result = 31 * result + (billSn != null ? billSn.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (commiPrice != null ? commiPrice.hashCode() : 0);
        result = 31 * result + (discountPrice != null ? discountPrice.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (billType != null ? billType.hashCode() : 0);
        result = 31 * result + (sellerId != null ? sellerId.hashCode() : 0);
        result = 31 * result + (payTime != null ? payTime.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (billPrice != null ? billPrice.hashCode() : 0);
        result = 31 * result + (refundPrice != null ? refundPrice.hashCode() : 0);
        result = 31 * result + (refundCommiPrice != null ? refundCommiPrice.hashCode() : 0);
        result = 31 * result + (sn != null ? sn.hashCode() : 0);
        result = 31 * result + (shopName != null ? shopName.hashCode() : 0);
        result = 31 * result + (bankAccountName != null ? bankAccountName.hashCode() : 0);
        result = 31 * result + (bankAccountNumber != null ? bankAccountNumber.hashCode() : 0);
        result = 31 * result + (bankName != null ? bankName.hashCode() : 0);
        result = 31 * result + (bankCode != null ? bankCode.hashCode() : 0);
        result = 31 * result + (bankAddress != null ? bankAddress.hashCode() : 0);
        result = 31 * result + (codPrice != null ? codPrice.hashCode() : 0);
        result = 31 * result + (codRefundPrice != null ? codRefundPrice.hashCode() : 0);
        result = 31 * result + (distributionRebate != null ? distributionRebate.hashCode() : 0);
        result = 31 * result + (distributionReturnRebate != null ? distributionReturnRebate.hashCode() : 0);
        result = 31 * result + (orderTotalPrice != null ? orderTotalPrice.hashCode() : 0);
        result = 31 * result + (refundTotalPrice != null ? refundTotalPrice.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "billId=" + billId +
                ", billSn='" + billSn + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", price=" + price +
                ", commiPrice=" + commiPrice +
                ", discountPrice=" + discountPrice +
                ", status='" + status + '\'' +
                ", billType=" + billType +
                ", sellerId=" + sellerId +
                ", payTime=" + payTime +
                ", createTime=" + createTime +
                ", billPrice=" + billPrice +
                ", refundPrice=" + refundPrice +
                ", refundCommiPrice=" + refundCommiPrice +
                ", sn='" + sn + '\'' +
                ", shopName='" + shopName + '\'' +
                ", bankAccountName='" + bankAccountName + '\'' +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", bankAddress='" + bankAddress + '\'' +
                ", codPrice=" + codPrice +
                ", codRefundPrice=" + codRefundPrice +
                ", distributionRebate=" + distributionRebate +
                ", distributionReturnRebate=" + distributionReturnRebate +
                ", orderTotalPrice=" + orderTotalPrice +
                ", refundTotalPrice=" + refundTotalPrice +
                '}';
    }
}