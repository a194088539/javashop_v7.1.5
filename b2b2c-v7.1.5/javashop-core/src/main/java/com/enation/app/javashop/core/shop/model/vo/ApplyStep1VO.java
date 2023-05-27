package com.enation.app.javashop.core.shop.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 申请开店第一步VO
 * @author zhangjiping
 * @version v1.0
 * @since v7.0
 * 2018年3月21日 下午2:56:51
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ApplyStep1VO {
	/**公司名称*/
	@Column(name = "company_name")
    @ApiModelProperty(name="company_name",value="公司名称",required=true)
    @NotEmpty(message="公司名称必填")
    private String companyName;
	
    /**公司地址*/
	@Column(name = "company_address")
    @ApiModelProperty(name="company_address",value="公司地址",required=true)
    @NotEmpty(message="公司地址必填")
    private String companyAddress;
	
    /**公司电话*/
	@Column(name = "company_phone")
    @ApiModelProperty(name="company_phone",value="公司电话",required=true)
    @NotEmpty(message="公司电话必填")
    private String companyPhone;
	
    /**电子邮箱*/
	@Column(name = "company_email")
    @ApiModelProperty(name="company_email",value="电子邮箱",required=true)
    @NotEmpty(message="电子邮箱必填")
    private String companyEmail;
    
    /**员工总数*/
	@Column(name = "employee_num")
    @ApiModelProperty(name="employee_num",value="员工总数",required=true)
    @NotNull(message="员工总数必填")
	@Min(value = 0,message = "员工总数必须大于零")
    private Integer employeeNum;
    
    /**注册资金*/
    @Column(name = "reg_money")
    @ApiModelProperty(name="reg_money",value="注册资金",required=true)
    @NotNull(message="注册资金必填")
    @Min(value = 0,message = "注册资金必须大于零")
    private Double regMoney;
    
    /**联系人姓名*/
    @Column(name = "link_name")
    @ApiModelProperty(name="link_name",value="联系人姓名",required=true)
    @NotEmpty(message="联系人姓名必填")
    private String linkName;
    
    /**联系人电话*/
    @Column(name = "link_phone")
    @ApiModelProperty(name="link_phone",value="联系人电话",required=true)
    @NotEmpty(message="联系人电话必填")
    private String linkPhone;

	/**申请开店进度*/
	@Column(name = "step")
	@ApiModelProperty(name="step",value="申请开店进度：1,2,3,4",required=false,hidden = true)
	private Integer step;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	public String getCompanyEmail() {
		return companyEmail;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public Integer getEmployeeNum() {
		return employeeNum;
	}

	public void setEmployeeNum(Integer employeeNum) {
		this.employeeNum = employeeNum;
	}

	public Double getRegMoney() {
		return regMoney;
	}

	public void setRegMoney(Double regMoney) {
		this.regMoney = regMoney;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	@Override
	public String toString() {
		return "ApplyStep1VO [companyName=" + companyName + ", companyAddress=" + companyAddress + ", companyPhone="
				+ companyPhone + ", companyEmail=" + companyEmail + ", employeeNum=" + employeeNum + ", regMonety="
				+ regMoney + ", linkName=" + linkName + ", linkPhone=" + linkPhone + "]";
	}


}
