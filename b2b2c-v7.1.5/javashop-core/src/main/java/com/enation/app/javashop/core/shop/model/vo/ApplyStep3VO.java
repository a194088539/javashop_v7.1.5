package com.enation.app.javashop.core.shop.model.vo;

import javax.validation.constraints.NotEmpty;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * 申请开店第三步VO
 * @author zhangjiping
 * @version v1.0
 * @since v7.0
 * 2018年3月21日 下午4:01:02
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ApplyStep3VO {
	/**银行开户名*/
	@Column(name = "bank_account_name")
	@ApiModelProperty(name="bank_account_name",value="银行开户名",required=true)
	@NotEmpty(message="银行开户名必填")
	private String bankAccountName;
	/**银行开户账号*/
	@Column(name = "bank_number")
	@ApiModelProperty(name="bank_number",value="银行开户账号",required=true)
	@NotEmpty(message="银行开户账号必填")
	private String bankNumber;
	/**开户银行支行名称*/
	@Column(name = "bank_name")
	@ApiModelProperty(name="bank_name",value="开户银行支行名称",required=true)
	@NotEmpty(message="开户银行支行名称必填")
	private String bankName;
	/**开户银行所在省id*/
	@Column(name = "bank_province_id")
	@ApiModelProperty(name="bank_province_id",value="开户银行所在省id",required=false,hidden = true)
	private Integer bankProvinceId;
	/**开户银行所在市id*/
	@Column(name = "bank_city_id")
	@ApiModelProperty(name="bank_city_id",value="开户银行所在市id",required=false,hidden = true)
	private Integer bankCityId;
	/**开户银行所在县id*/
	@Column(name = "bank_county_id")
	@ApiModelProperty(name="bank_county_id",value="开户银行所在县id",required=false,hidden = true)
	private Integer bankCountyId;
	/**开户银行所在镇id*/
	@Column(name = "bank_town_id")
	@ApiModelProperty(name="bank_town_id",value="开户银行所在镇id",required=false,hidden = true)
	private Integer bankTownId;
	/**开户银行所在省*/
	@Column(name = "bank_province")
	@ApiModelProperty(name="bank_province",value="开户银行所在省",required=false,hidden = true)
	private String bankProvince;
	/**开户银行所在市*/
	@Column(name = "bank_city")
	@ApiModelProperty(name="bank_city",value="开户银行所在市",required=false,hidden = true)
	private String bankCity;
	/**开户银行所在县*/
	@Column(name = "bank_county")
	@ApiModelProperty(name="bank_county",value="开户银行所在县",required=false,hidden = true)
	private String bankCounty;
	/**开户银行所在镇*/
	@Column(name = "bank_town",allowNullUpdate = true)
	@ApiModelProperty(name="bank_town",value="开户银行所在镇",required=false,hidden = true)
	private String bankTown;
	/**开户银行许可证电子版*/
	@Column(name = "bank_img")
	@ApiModelProperty(name="bank_img",value="开户银行许可证电子版",required=true)
	private String bankImg;
	/**税务登记证号*/
	@Column(name = "taxes_certificate_num")
	@ApiModelProperty(name="taxes_certificate_num",value="税务登记证号",required=false)
	private String taxesCertificateNum;
	/**纳税人识别号*/
	@Column(name = "taxes_distinguish_num")
	@ApiModelProperty(name="taxes_distinguish_num",value="纳税人识别号",required=false)
	private String taxesDistinguishNum;
	/**税务登记证书*/
	@Column(name = "taxes_certificate_img")
	@ApiModelProperty(name="taxes_certificate_img",value="税务登记证书",required=false)
	private String taxesCertificateImg;
	/**申请开店进度*/
	@Column(name = "step")
	@ApiModelProperty(name="step",value="申请开店进度：1,2,3,4",required=false)
	private Integer step;
	public String getBankAccountName() {
		return bankAccountName;
	}
	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}
	public String getBankNumber() {
		return bankNumber;
	}
	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public Integer getBankProvinceId() {
		return bankProvinceId;
	}
	public void setBankProvinceId(Integer bankProvinceId) {
		this.bankProvinceId = bankProvinceId;
	}
	public Integer getBankCityId() {
		return bankCityId;
	}
	public void setBankCityId(Integer bankCityId) {
		this.bankCityId = bankCityId;
	}
	public Integer getBankCountyId() {
		return bankCountyId;
	}
	public void setBankCountyId(Integer bankCountyId) {
		this.bankCountyId = bankCountyId;
	}
	public Integer getBankTownId() {
		return bankTownId;
	}
	public void setBankTownId(Integer bankTownId) {
		this.bankTownId = bankTownId;
	}
	public String getBankProvince() {
		return bankProvince;
	}
	public void setBankProvince(String bankProvince) {
		this.bankProvince = bankProvince;
	}
	public String getBankCity() {
		return bankCity;
	}
	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}
	public String getBankCounty() {
		return bankCounty;
	}
	public void setBankCounty(String bankCounty) {
		this.bankCounty = bankCounty;
	}
	public String getBankTown() {
		return bankTown;
	}
	public void setBankTown(String bankTown) {
		this.bankTown = bankTown;
	}
	public String getBankImg() {
		return bankImg;
	}
	public void setBankImg(String bankImg) {
		this.bankImg = bankImg;
	}
	public String getTaxesCertificateNum() {
		return taxesCertificateNum;
	}
	public void setTaxesCertificateNum(String taxesCertificateNum) {
		this.taxesCertificateNum = taxesCertificateNum;
	}
	public String getTaxesDistinguishNum() {
		return taxesDistinguishNum;
	}
	public void setTaxesDistinguishNum(String taxesDistinguishNum) {
		this.taxesDistinguishNum = taxesDistinguishNum;
	}
	public String getTaxesCertificateImg() {
		return taxesCertificateImg;
	}
	public void setTaxesCertificateImg(String taxesCertificateImg) {
		this.taxesCertificateImg = taxesCertificateImg;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	@Override
	public String toString() {
		return "ApplyStep3VO [bankAccountName=" + bankAccountName + ", bankNumber=" + bankNumber + ", bankName="
				+ bankName + ", bankProvinceId=" + bankProvinceId + ", bankCityId=" + bankCityId + ", bankCountyId="
				+ bankCountyId + ", bankTownId=" + bankTownId + ", bankProvince=" + bankProvince + ", bankCity="
				+ bankCity + ", bankCounty=" + bankCounty + ", bankTown=" + bankTown + ", bankImg=" + bankImg
				+ ", taxesCertificateNum=" + taxesCertificateNum + ", taxesDistinguishNum=" + taxesDistinguishNum
				+ ", taxesCertificateImg=" + taxesCertificateImg + "]";
	}
}

