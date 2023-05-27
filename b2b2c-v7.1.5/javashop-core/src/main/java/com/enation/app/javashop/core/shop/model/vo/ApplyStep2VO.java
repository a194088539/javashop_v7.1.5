package com.enation.app.javashop.core.shop.model.vo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 申请开店第二步VO
 *
 * @author zhangjiping
 * @version v1.0
 * @since v7.0
 * 2018年3月21日 下午3:42:23
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ApplyStep2VO {
	/**
	 * 法人姓名
	 */
	@Column(name = "legal_name")
	@ApiModelProperty(name = "legal_name", value = "法人姓名", required = true)
	@NotEmpty(message = "法人姓名必填")
	private String legalName;
	/**
	 * 法人身份证
	 */
	@Column(name = "legal_id")
	@ApiModelProperty(name = "legal_id", value = "法人身份证", required = true)
	@Size(min = 18, max = 18, message = "身份证长度不符")
	private String legalId;
	/**
	 * 法人身份证照片
	 */
	@Column(name = "legal_img")
	@ApiModelProperty(name = "legal_img", value = "法人身份证照片", required = true)
	@NotEmpty(message = "法人身份证照片必填")
	private String legalImg;
	/**
	 * 营业执照号
	 */
	@Column(name = "license_num")
	@ApiModelProperty(name = "license_num", value = "营业执照号", required = true)
	@NotEmpty(message = "营业执照号必填")
	private String licenseNum;
	/**
	 * 营业执照所在省id
	 */
	@Column(name = "license_province_id")
	@ApiModelProperty(name = "license_province_id", value = "营业执照所在省id", required = false, hidden = true)
	private Integer licenseProvinceId;
	/**
	 * 营业执照所在市id
	 */
	@Column(name = "license_city_id")
	@ApiModelProperty(name = "license_city_id", value = "营业执照所在市id", required = false, hidden = true)
	private Integer licenseCityId;
	/**
	 * 营业执照所在县id
	 */
	@Column(name = "license_county_id")
	@ApiModelProperty(name = "license_county_id", value = "营业执照所在县id", required = false, hidden = true)
	private Integer licenseCountyId;
	/**
	 * 营业执照所在镇id
	 */
	@Column(name = "license_town_id")
	@ApiModelProperty(name = "license_town_id", value = "营业执照所在镇id", required = false, hidden = true)
	private Integer licenseTownId;
	/**
	 * 营业执照所在省
	 */
	@Column(name = "license_province")
	@ApiModelProperty(name = "license_province", value = "营业执照所在省", required = false, hidden = true)
	private String licenseProvince;
	/**
	 * 营业执照所在市
	 */
	@Column(name = "license_city")
	@ApiModelProperty(name = "license_city", value = "营业执照所在市", required = false, hidden = true)
	private String licenseCity;
	/**
	 * 营业执照所在县
	 */
	@Column(name = "license_county")
	@ApiModelProperty(name = "license_county", value = "营业执照所在县", required = false, hidden = true)
	private String licenseCounty;
	/**
	 * 营业执照所在镇
	 */
	@Column(name = "license_town",allowNullUpdate = true)
	@ApiModelProperty(name = "license_town", value = "营业执照所在镇", required = false, hidden = true)
	private String licenseTown;
	/**
	 * 营业执照详细地址
	 */
	@Column(name = "license_add")
	@ApiModelProperty(name = "license_add", value = "营业执照详细地址", required = true)
	@NotEmpty(message = "营业执照详细地址必填")
	private String licenseAdd;
	/**
	 * 成立日期
	 */
	@Column(name = "establish_date")
	@ApiModelProperty(name = "establish_date", value = "成立日期", required = true)
	@NotNull(message = "成立日期必填")
	private Long establishDate;
	/**
	 * 营业执照有效期开始
	 */
	@Column(name = "licence_start")
	@ApiModelProperty(name = "licence_start", value = "营业执照有效期开始", required = true)
	@NotNull(message = "营业执照有效期开始时间必填")
	private Long licenceStart;
	/**
	 * 营业执照有效期结束
	 */
	@Column(name = "licence_end")
	@ApiModelProperty(name = "licence_end", value = "营业执照有效期结束", required = true)
	@NotNull(message = "营业执照有效期结束必填")
	private Long licenceEnd;
	/**
	 * 法定经营范围
	 */
	@Column(name = "scope")
	@ApiModelProperty(name = "scope", value = "法定经营范围", required = true)
	@NotEmpty(message = "法定经营范围必填")
	private String scope;
	/**
	 * 营业执照电子版
	 */
	@Column(name = "licence_img")
	@ApiModelProperty(name = "licence_img", value = "营业执照电子版", required = true)
	@NotEmpty(message = "营业执照电子版必填")
	private String licenceImg;
	/**
	 * 组织机构代码
	 */
	@Column(name = "organization_code")
	@ApiModelProperty(name = "organization_code", value = "组织机构代码", required = false)
	private String organizationCode;
	/**
	 * 组织机构电子版
	 */
	@Column(name = "code_img")
	@ApiModelProperty(name = "code_img", value = "组织机构电子版", required = false)
	private String codeImg;
	/**
	 * 一般纳税人证明电子版
	 */
	@Column(name = "taxes_img")
	@ApiModelProperty(name = "taxes_img", value = "一般纳税人证明电子版", required = false)
	private String taxesImg;
	/**
	 * 申请开店进度
	 */
	@Column(name = "step")
	@ApiModelProperty(name = "step", value = "申请开店进度：1,2,3,4", required = false, hidden = true)
	private Integer step;

	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	public String getLegalId() {
		return legalId;
	}

	public void setLegalId(String legalId) {
		this.legalId = legalId;
	}

	public String getLegalImg() {
		return legalImg;
	}

	public void setLegalImg(String legalImg) {
		this.legalImg = legalImg;
	}

	public String getLicenseNum() {
		return licenseNum;
	}

	public void setLicenseNum(String licenseNum) {
		this.licenseNum = licenseNum;
	}

	public Integer getLicenseProvinceId() {
		return licenseProvinceId;
	}

	public void setLicenseProvinceId(Integer licenseProvinceId) {
		this.licenseProvinceId = licenseProvinceId;
	}

	public Integer getLicenseCityId() {
		return licenseCityId;
	}

	public void setLicenseCityId(Integer licenseCityId) {
		this.licenseCityId = licenseCityId;
	}

	public Integer getLicenseCountyId() {
		return licenseCountyId;
	}

	public void setLicenseCountyId(Integer licenseCountyId) {
		this.licenseCountyId = licenseCountyId;
	}

	public Integer getLicenseTownId() {
		return licenseTownId;
	}

	public void setLicenseTownId(Integer licenseTownId) {
		this.licenseTownId = licenseTownId;
	}

	public String getLicenseProvince() {
		return licenseProvince;
	}

	public void setLicenseProvince(String licenseProvince) {
		this.licenseProvince = licenseProvince;
	}

	public String getLicenseCity() {
		return licenseCity;
	}

	public void setLicenseCity(String licenseCity) {
		this.licenseCity = licenseCity;
	}

	public String getLicenseCounty() {
		return licenseCounty;
	}

	public void setLicenseCounty(String licenseCounty) {
		this.licenseCounty = licenseCounty;
	}

	public String getLicenseTown() {
		return licenseTown;
	}

	public void setLicenseTown(String licenseTown) {
		this.licenseTown = licenseTown;
	}

	public String getLicenseAdd() {
		return licenseAdd;
	}

	public void setLicenseAdd(String licenseAdd) {
		this.licenseAdd = licenseAdd;
	}

	public Long getEstablishDate() {
		return establishDate;
	}

	public void setEstablishDate(Long establishDate) {
		this.establishDate = establishDate;
	}

	public Long getLicenceStart() {
		return licenceStart;
	}

	public void setLicenceStart(Long licenceStart) {
		this.licenceStart = licenceStart;
	}

	public Long getLicenceEnd() {
		return licenceEnd;
	}

	public void setLicenceEnd(Long licenceEnd) {
		this.licenceEnd = licenceEnd;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getLicenceImg() {
		return licenceImg;
	}

	public void setLicenceImg(String licenceImg) {
		this.licenceImg = licenceImg;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getCodeImg() {
		return codeImg;
	}

	public void setCodeImg(String codeImg) {
		this.codeImg = codeImg;
	}

	public String getTaxesImg() {
		return taxesImg;
	}

	public void setTaxesImg(String taxesImg) {
		this.taxesImg = taxesImg;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	@Override
	public String toString() {
		return "ApplyStep2VO{" +
				"legalName='" + legalName + '\'' +
				", legalId='" + legalId + '\'' +
				", legalImg='" + legalImg + '\'' +
				", licenseNum='" + licenseNum + '\'' +
				", licenseProvinceId=" + licenseProvinceId +
				", licenseCityId=" + licenseCityId +
				", licenseCountyId=" + licenseCountyId +
				", licenseTownId=" + licenseTownId +
				", licenseProvince='" + licenseProvince + '\'' +
				", licenseCity='" + licenseCity + '\'' +
				", licenseCounty='" + licenseCounty + '\'' +
				", licenseTown='" + licenseTown + '\'' +
				", licenseAdd='" + licenseAdd + '\'' +
				", establishDate=" + establishDate +
				", licenceStart=" + licenceStart +
				", licenceEnd=" + licenceEnd +
				", scope='" + scope + '\'' +
				", licenceImg='" + licenceImg + '\'' +
				", organizationCode='" + organizationCode + '\'' +
				", codeImg='" + codeImg + '\'' +
				", taxesImg='" + taxesImg + '\'' +
				", step=" + step +
				'}';
	}
}