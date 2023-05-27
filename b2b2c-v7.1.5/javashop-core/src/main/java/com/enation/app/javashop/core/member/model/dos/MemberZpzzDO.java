package com.enation.app.javashop.core.member.model.dos;

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
 * 会员增票资质实体
 *
 * @author duanmingyu
 * @version v7.1.4
 * @since v7.0.0
 * 2019-06-18
 */
@Table(name = "es_member_zpzz")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberZpzzDO implements Serializable {

    private static final long serialVersionUID = 4727610826309392575L;

    /**
     * 主键ID
     */
    @Id(name = "id")
    @ApiModelProperty(hidden = true)
    private Integer id;

    /**
     * 会员ID
     */
    @Column(name = "member_id")
    @ApiModelProperty(name = "member_id", value = "会员ID", required = false)
    private Integer memberId;

    /**
     * 会员登陆用户名
     */
    @Column(name = "uname")
    @ApiModelProperty(name = "uname", value = "会员登陆用户名", required = false)
    private String uname;

    /**
     * 状态
     */
    @Column(name = "status")
    @ApiModelProperty(name = "status", value = "状态", required = false, example = "NEW_APPLY：新申请，AUDIT_PASS：审核通过，AUDIT_REFUSE：审核未通过")
    private String status;

    /**
     * 单位名称
     */
    @Column(name = "company_name")
    @ApiModelProperty(name = "company_name", value = "单位名称", required = false)
    private String companyName;

    /**
     * 纳税人识别码
     */
    @Column(name = "taxpayer_code")
    @ApiModelProperty(name = "taxpayer_code", value = "纳税人识别码", required = false)
    private String taxpayerCode;

    /**
     * 公司注册地址
     */
    @Column(name = "register_address")
    @ApiModelProperty(name = "register_address", value = "公司注册地址", required = false)
    private String registerAddress;

    /**
     * 公司注册电话
     */
    @Column(name = "register_tel")
    @ApiModelProperty(name = "register_tel", value = "公司注册电话", required = false)
    private String registerTel;

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
     * 平台审核备注
     */
    @Column(name = "audit_remark")
    @ApiModelProperty(name = "audit_remark", value = "平台审核备注", required = false)
    private String auditRemark;

    /**
     * 申请时间
     */
    @Column(name = "apply_time")
    @ApiModelProperty(name = "apply_time", value = "申请时间", required = false)
    private Long applyTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTaxpayerCode() {
        return taxpayerCode;
    }

    public void setTaxpayerCode(String taxpayerCode) {
        this.taxpayerCode = taxpayerCode;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public String getRegisterTel() {
        return registerTel;
    }

    public void setRegisterTel(String registerTel) {
        this.registerTel = registerTel;
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

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public Long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberZpzzDO that = (MemberZpzzDO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(memberId, that.memberId) &&
                Objects.equals(uname, that.uname) &&
                Objects.equals(status, that.status) &&
                Objects.equals(companyName, that.companyName) &&
                Objects.equals(taxpayerCode, that.taxpayerCode) &&
                Objects.equals(registerAddress, that.registerAddress) &&
                Objects.equals(registerTel, that.registerTel) &&
                Objects.equals(bankName, that.bankName) &&
                Objects.equals(bankAccount, that.bankAccount) &&
                Objects.equals(auditRemark, that.auditRemark) &&
                Objects.equals(applyTime, that.applyTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, memberId, uname, status, companyName, taxpayerCode, registerAddress, registerTel, bankName, bankAccount, auditRemark, applyTime);
    }

    @Override
    public String toString() {
        return "MemberZpzzDO{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", uname='" + uname + '\'' +
                ", status='" + status + '\'' +
                ", companyName='" + companyName + '\'' +
                ", taxpayerCode='" + taxpayerCode + '\'' +
                ", registerAddress='" + registerAddress + '\'' +
                ", registerTel='" + registerTel + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", auditRemark='" + auditRemark + '\'' +
                ", applyTime=" + applyTime +
                '}';
    }
}
