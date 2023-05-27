package com.enation.app.javashop.core.aftersale.model.dos;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 售后服务单实体
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-15
 */
@Table(name = "es_as_order")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AfterSaleServiceDO extends AfterSaleBase implements Serializable {

    private static final long serialVersionUID = 8020639820681620361L;

    /**
     * 售后类型 RETURN_GOODS：退货，CHANGE_GOODS：换货，SUPPLY_AGAIN_GOODS：补发货品，ORDER_CANCEL：取消订单（订单确认付款且未收货之前）
     */
    @Column(name = "service_type")
    @ApiModelProperty(name = "service_type", value = "售后类型 RETURN_GOODS：退货，CHANGE_GOODS：换货，SUPPLY_AGAIN_GOODS：补发货品，ORDER_CANCEL：取消订单（订单确认付款且未收货之前）")
    private String serviceType;
    /**
     * 售后单状态 APPLY：申请，PASS：审核通过，REFUSE：审核拒绝，WAIT_FOR_MANUAL：待人工处理，STOCK_IN：退货入库，REFUNDING：退款中，REFUNDFAIL：退款失败，COMPLETE：完成
     */
    @Column(name = "service_status")
    @ApiModelProperty(name = "service_status", value = "售后单状态 APPLY：申请，PASS：审核通过，REFUSE：审核拒绝，WAIT_FOR_MANUAL：待人工处理，STOCK_IN：退货入库，REFUNDING：退款中，REFUNDFAIL：退款失败，COMPLETE：完成")
    private String serviceStatus;
    /**
     * 申请原因
     */
    @Column(name = "reason")
    @ApiModelProperty(name = "reason", value = "申请原因")
    private String reason;
    /**
     * 申请凭证
     */
    @Column(name = "apply_vouchers")
    @ApiModelProperty(name = "apply_vouchers", value = "申请凭证")
    private String applyVouchers;
    /**
     * 问题描述
     */
    @Column(name = "problem_desc")
    @ApiModelProperty(name = "problem_desc", value = "问题描述")
    private String problemDesc;
    /**
     * 审核备注
     */
    @Column(name = "audit_remark")
    @ApiModelProperty(name = "audit_remark", value = "审核备注")
    private String auditRemark;
    /**
     * 入库备注
     */
    @Column(name = "stock_remark")
    @ApiModelProperty(name = "stock_remark", value = "入库备注")
    private String stockRemark;
    /**
     * 退款备注
     */
    @Column(name = "refund_remark")
    @ApiModelProperty(name = "refund_remark", value = "退款备注")
    private String refundRemark;
    /**
     * 退货地址信息
     */
    @Column(name = "return_addr")
    @ApiModelProperty(name = "return_addr", value = "退货地址信息")
    private String returnAddr;
    /**
     * 生成的新订单编号
     */
    @Column(name = "new_order_sn")
    @ApiModelProperty(name = "new_order_sn", value = "新订单编号")
    private String newOrderSn;

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getApplyVouchers() {
        return applyVouchers;
    }

    public void setApplyVouchers(String applyVouchers) {
        this.applyVouchers = applyVouchers;
    }

    public String getProblemDesc() {
        return problemDesc;
    }

    public void setProblemDesc(String problemDesc) {
        this.problemDesc = problemDesc;
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

    public String getReturnAddr() {
        return returnAddr;
    }

    public void setReturnAddr(String returnAddr) {
        this.returnAddr = returnAddr;
    }

    public String getNewOrderSn() {
        return newOrderSn;
    }

    public void setNewOrderSn(String newOrderSn) {
        this.newOrderSn = newOrderSn;
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
        AfterSaleServiceDO that = (AfterSaleServiceDO) o;
        return Objects.equals(serviceType, that.serviceType) &&
                Objects.equals(serviceStatus, that.serviceStatus) &&
                Objects.equals(reason, that.reason) &&
                Objects.equals(applyVouchers, that.applyVouchers) &&
                Objects.equals(problemDesc, that.problemDesc) &&
                Objects.equals(auditRemark, that.auditRemark) &&
                Objects.equals(stockRemark, that.stockRemark) &&
                Objects.equals(refundRemark, that.refundRemark) &&
                Objects.equals(returnAddr, that.returnAddr) &&
                Objects.equals(newOrderSn, that.newOrderSn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), serviceType, serviceStatus, reason, applyVouchers, problemDesc, auditRemark, stockRemark, refundRemark, returnAddr, newOrderSn);
    }

    @Override
    public String toString() {
        return "AfterSaleServiceDO{" +
                "serviceType='" + serviceType + '\'' +
                ", serviceStatus='" + serviceStatus + '\'' +
                ", reason='" + reason + '\'' +
                ", applyVouchers='" + applyVouchers + '\'' +
                ", problemDesc='" + problemDesc + '\'' +
                ", auditRemark='" + auditRemark + '\'' +
                ", stockRemark='" + stockRemark + '\'' +
                ", refundRemark='" + refundRemark + '\'' +
                ", returnAddr='" + returnAddr + '\'' +
                ", newOrderSn='" + newOrderSn + '\'' +
                '}';
    }
}
