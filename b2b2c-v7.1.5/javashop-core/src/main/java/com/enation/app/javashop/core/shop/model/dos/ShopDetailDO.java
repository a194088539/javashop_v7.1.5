package com.enation.app.javashop.core.shop.model.dos;

import java.io.Serializable;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Size;


/**
 * 店铺详细实体
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-21 10:06:38
 */
@Table(name="es_shop_detail")
@ApiModel
public class ShopDetailDO implements Serializable {
			
    private static final long serialVersionUID = 1810092990127648L;
    
    /**店铺详细id*/
    @Id(name = "id")
    @ApiModelProperty(hidden=true)
    private Integer id;
    /**店铺id*/
    @Column(name = "shop_id")
    @ApiModelProperty(name="shop_id",value="店铺id",required=false)
    private Integer shopId;
    /**店铺所在省id*/
    @Column(name = "shop_province_id")
    @ApiModelProperty(name="shop_province_id",value="店铺所在省id",required=false)
    private Integer shopProvinceId;
    /**店铺所在市id*/
    @Column(name = "shop_city_id")
    @ApiModelProperty(name="shop_city_id",value="店铺所在市id",required=false)
    private Integer shopCityId;
    /**店铺所在县id*/
    @Column(name = "shop_county_id")
    @ApiModelProperty(name="shop_county_id",value="店铺所在县id",required=false)
    private Integer shopCountyId;
    /**店铺所在镇id*/
    @Column(name = "shop_town_id")
    @ApiModelProperty(name="shop_town_id",value="店铺所在镇id",required=false)
    private Integer shopTownId;
    /**店铺所在省*/
    @Column(name = "shop_province")
    @ApiModelProperty(name="shop_province",value="店铺所在省",required=false)
    private String shopProvince;
    /**店铺所在市*/
    @Column(name = "shop_city")
    @ApiModelProperty(name="shop_city",value="店铺所在市",required=false)
    private String shopCity;
    /**店铺所在县*/
    @Column(name = "shop_county")
    @ApiModelProperty(name="shop_county",value="店铺所在县",required=false)
    private String shopCounty;
    /**店铺所在镇*/
    @Column(name = "shop_town",allowNullUpdate = true)
    @ApiModelProperty(name="shop_town",value="店铺所在镇",required=false)
    private String shopTown;
    /**店铺详细地址*/
    @Column(name = "shop_add")
    @ApiModelProperty(name="shop_add",value="店铺详细地址",required=false)
    private String shopAdd;
    /**公司名称*/
    @Column(name = "company_name")
    @ApiModelProperty(name="company_name",value="公司名称",required=false)
    private String companyName;
    /**公司地址*/
    @Column(name = "company_address")
    @ApiModelProperty(name="company_address",value="公司地址",required=false)
    private String companyAddress;
    /**公司电话*/
    @Column(name = "company_phone")
    @ApiModelProperty(name="company_phone",value="公司电话",required=false)
    private String companyPhone;
    /**电子邮箱*/
    @Column(name = "company_email")
    @ApiModelProperty(name="company_email",value="电子邮箱",required=false)
    private String companyEmail;
    /**员工总数*/
    @Column(name = "employee_num")
    @ApiModelProperty(name="employee_num",value="员工总数",required=false)
    private Integer employeeNum;
    /**注册资金*/
    @Column(name = "reg_money")
    @ApiModelProperty(name="reg_money",value="注册资金",required=false)
    private Double regMoney;
    /**联系人姓名*/
    @Column(name = "link_name")
    @ApiModelProperty(name="link_name",value="联系人姓名",required=false)
    private String linkName;
    /**联系人电话*/
    @Column(name = "link_phone")
    @ApiModelProperty(name="link_phone",value="联系人电话",required=false)
    private String linkPhone;
    /**法人姓名*/
    @Column(name = "legal_name")
    @ApiModelProperty(name="legal_name",value="法人姓名",required=false)
    private String legalName;
    /**法人身份证*/
    @Column(name = "legal_id")
    @ApiModelProperty(name="legal_id",value="法人身份证",required=false)
    private String legalId;
    /**法人身份证照片*/
    @Column(name = "legal_img")
    @ApiModelProperty(name="legal_img",value="法人身份证照片",required=false)
    private String legalImg;
    /**营业执照号*/
    @Column(name = "license_num")
    @ApiModelProperty(name="license_num",value="营业执照号",required=false)
    private String licenseNum;
    /**营业执照所在省id*/
    @Column(name = "license_province_id")
    @ApiModelProperty(name="license_province_id",value="营业执照所在省id",required=false)
    private Integer licenseProvinceId;
    /**营业执照所在市id*/
    @Column(name = "license_city_id")
    @ApiModelProperty(name="license_city_id",value="营业执照所在市id",required=false)
    private Integer licenseCityId;
    /**营业执照所在县id*/
    @Column(name = "license_county_id")
    @ApiModelProperty(name="license_county_id",value="营业执照所在县id",required=false)
    private Integer licenseCountyId;
    /**营业执照所在镇id*/
    @Column(name = "license_town_id")
    @ApiModelProperty(name="license_town_id",value="营业执照所在镇id",required=false)
    private Integer licenseTownId;
    /**营业执照所在省*/
    @Column(name = "license_province")
    @ApiModelProperty(name="license_province",value="营业执照所在省",required=false)
    private String licenseProvince;
    /**营业执照所在市*/
    @Column(name = "license_city")
    @ApiModelProperty(name="license_city",value="营业执照所在市",required=false)
    private String licenseCity;
    /**营业执照所在县*/
    @Column(name = "license_county")
    @ApiModelProperty(name="license_county",value="营业执照所在县",required=false)
    private String licenseCounty;
    /**营业执照所在镇*/
    @Column(name = "license_town",allowNullUpdate = true)
    @ApiModelProperty(name="license_town",value="营业执照所在镇",required=false)
    private String licenseTown;
    /**营业执照详细地址*/
    @Column(name = "license_add")
    @ApiModelProperty(name="license_add",value="营业执照详细地址",required=false)
    private String licenseAdd;
    /**成立日期*/
    @Column(name = "establish_date")
    @ApiModelProperty(name="establish_date",value="成立日期",required=false)
    private Long establishDate;
    /**营业执照有效期开始*/
    @Column(name = "licence_start")
    @ApiModelProperty(name="licence_start",value="营业执照有效期开始",required=false)
    private Long licenceStart;
    /**营业执照有效期结束*/
    @Column(name = "licence_end")
    @ApiModelProperty(name="licence_end",value="营业执照有效期结束",required=false)
    private Long licenceEnd;
    /**法定经营范围*/
    @Column(name = "scope")
    @ApiModelProperty(name="scope",value="法定经营范围",required=false)
    private String scope;
    /**营业执照电子版*/
    @Column(name = "licence_img")
    @ApiModelProperty(name="licence_img",value="营业执照电子版",required=false)
    private String licenceImg;
    /**组织机构代码*/
    @Column(name = "organization_code")
    @ApiModelProperty(name="organization_code",value="组织机构代码",required=false)
    private String organizationCode;
    /**组织机构电子版*/
    @Column(name = "code_img")
    @ApiModelProperty(name="code_img",value="组织机构电子版",required=false)
    private String codeImg;
    /**一般纳税人证明电子版*/
    @Column(name = "taxes_img")
    @ApiModelProperty(name="taxes_img",value="一般纳税人证明电子版",required=false)
    private String taxesImg;
    /**银行开户名*/
    @Column(name = "bank_account_name")
    @ApiModelProperty(name="bank_account_name",value="银行开户名",required=false)
    private String bankAccountName;
    /**银行开户账号*/
    @Column(name = "bank_number")
    @ApiModelProperty(name="bank_number",value="银行开户账号",required=false)
    private String bankNumber;
    /**开户银行支行名称*/
    @Column(name = "bank_name")
    @ApiModelProperty(name="bank_name",value="开户银行支行名称",required=false)
    private String bankName;
    /**开户银行所在省id*/
    @Column(name = "bank_province_id")
    @ApiModelProperty(name="bank_province_id",value="开户银行所在省id",required=false)
    private Integer bankProvinceId;
    /**开户银行所在市id*/
    @Column(name = "bank_city_id")
    @ApiModelProperty(name="bank_city_id",value="开户银行所在市id",required=false)
    private Integer bankCityId;
    /**开户银行所在县id*/
    @Column(name = "bank_county_id")
    @ApiModelProperty(name="bank_county_id",value="开户银行所在县id",required=false)
    private Integer bankCountyId;
    /**开户银行所在镇id*/
    @Column(name = "bank_town_id")
    @ApiModelProperty(name="bank_town_id",value="开户银行所在镇id",required=false)
    private Integer bankTownId;
    /**开户银行所在省*/
    @Column(name = "bank_province")
    @ApiModelProperty(name="bank_province",value="开户银行所在省",required=false)
    private String bankProvince;
    /**开户银行所在市*/
    @Column(name = "bank_city")
    @ApiModelProperty(name="bank_city",value="开户银行所在市",required=false)
    private String bankCity;
    /**开户银行所在县*/
    @Column(name = "bank_county")
    @ApiModelProperty(name="bank_county",value="开户银行所在县",required=false)
    private String bankCounty;
    /**开户银行所在镇*/
    @Column(name = "bank_town",allowNullUpdate = true)
    @ApiModelProperty(name="bank_town",value="开户银行所在镇",required=false)
    private String bankTown;
    /**开户银行许可证电子版*/
    @Column(name = "bank_img")
    @ApiModelProperty(name="bank_img",value="开户银行许可证电子版",required=false)
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
    /**店铺经营类目*/
    @Column(name = "goods_management_category")
    @ApiModelProperty(name="goods_management_category",value="店铺经营类目",required=false)
    private String goodsManagementCategory;
    /**店铺等级*/
    @Column(name = "shop_level")
    @ApiModelProperty(name="shop_level",value="店铺等级",required=false)
    private Integer shopLevel;
    /**店铺等级申请*/
    @Column(name = "shop_level_apply")
    @ApiModelProperty(name="shop_level_apply",value="店铺等级申请",required=false)
    private Integer shopLevelApply;
    /**店铺相册已用存储量*/
    @Column(name = "store_space_capacity")
    @ApiModelProperty(name="store_space_capacity",value="店铺相册已用存储量",required=false)
    private Double storeSpaceCapacity;
    /**店铺logo*/
    @Column(name = "shop_logo")
    @ApiModelProperty(name="shop_logo",value="店铺logo",required=false)
    private String shopLogo;
    /**店铺横幅*/
    @Column(name = "shop_banner")
    @ApiModelProperty(name="shop_banner",value="店铺横幅",required=false)
    private String shopBanner;
    /**店铺简介*/
    @Column(name = "shop_desc")
    @Size(max=200,message = "店铺简介长度不符，不能超过200")
    @ApiModelProperty(name="shop_desc",value="店铺简介",required=false)
    private String shopDesc;
    /**是否推荐*/
    @Column(name = "shop_recommend")
    @ApiModelProperty(name="shop_recommend",value="是否推荐",required=false)
    private Integer shopRecommend;
    /**店铺主题id*/
    @Column(name = "shop_themeid")
    @ApiModelProperty(name="shop_themeid",value="店铺主题id",required=false)
    private Integer shopThemeid;
    /**店铺主题模版路径*/
    @Column(name = "shop_theme_path")
    @ApiModelProperty(name="shop_theme_path",value="店铺主题模版路径",required=false)
    private String shopThemePath;
    /**店铺主题id*/
    @Column(name = "wap_themeid")
    @ApiModelProperty(name="wap_themeid",value="店铺主题id",required=false)
    private Integer wapThemeid;
    /**wap店铺主题*/
    @Column(name = "wap_theme_path")
    @ApiModelProperty(name="wap_theme_path",value="wap店铺主题",required=false)
    private String wapThemePath;
    /**店铺信用*/
    @Column(name = "shop_credit")
    @ApiModelProperty(name="shop_credit",value="店铺信用",required=false)
    private Double shopCredit;
    /**店铺好评率*/
    @Column(name = "shop_praise_rate")
    @ApiModelProperty(name="shop_praise_rate",value="店铺好评率",required=false)
    private Double shopPraiseRate;
    /**店铺描述相符度*/
    @Column(name = "shop_description_credit")
    @ApiModelProperty(name="shop_description_credit",value="店铺描述相符度",required=false)
    private Double shopDescriptionCredit;
    /**服务态度分数*/
    @Column(name = "shop_service_credit")
    @ApiModelProperty(name="shop_service_credit",value="服务态度分数",required=false)
    private Double shopServiceCredit;
    /**发货速度分数*/
    @Column(name = "shop_delivery_credit")
    @ApiModelProperty(name="shop_delivery_credit",value="发货速度分数",required=false)
    private Double shopDeliveryCredit;
    /**店铺收藏数*/
    @Column(name = "shop_collect")
    @ApiModelProperty(name="shop_collect",value="店铺收藏数",required=false)
    private Integer shopCollect;
    /**店铺商品数*/
    @Column(name = "goods_num")
    @ApiModelProperty(name="goods_num",value="店铺商品数",required=false)
    private Integer goodsNum;
    /**店铺客服qq*/
    @Column(name = "shop_qq")
    @ApiModelProperty(name="shop_qq",value="店铺客服qq",required=false)
    private String shopQq;
    /**店铺佣金比例*/
    @Column(name = "shop_commission")
    @ApiModelProperty(name="shop_commission",value="店铺佣金比例",required=false)
    private Double shopCommission;
    /**货品预警数*/
    @Column(name = "goods_warning_count")
    @ApiModelProperty(name="goods_warning_count",value="货品预警数",required=false)
    private Integer goodsWarningCount;
    /**是否自营*/
    @Column(name = "self_operated")
    @ApiModelProperty(name="self_operated",value="是否自营",required=true)
    private Integer selfOperated;
    /**申请开店进度*/
    @Column(name = "step")
    @ApiModelProperty(name="step",value="申请开店进度：1,2,3,4",required=false)
    private Integer step;
    /**是否允许开具增值税普通发票 0：否，1：是*/
    @Column(name = "ordin_receipt_status")
    @ApiModelProperty(name="ordin_receipt_status",value="是否允许开具增值税普通发票 0：否，1：是",required=false, allowableValues = "0,1")
    private Integer ordinReceiptStatus;
    /**是否允许开具电子普通发票 0：否，1：是*/
    @Column(name = "elec_receipt_status")
    @ApiModelProperty(name="elec_receipt_status",value="是否允许开具电子普通发票 0：否，1：是",required=false, allowableValues = "0,1")
    private Integer elecReceiptStatus;
    /**是否允许开具增值税专用发票 0：否，1：是*/
    @Column(name = "tax_receipt_status")
    @ApiModelProperty(name="tax_receipt_status",value="是否允许开具增值税专用发票 0：否，1：是",required=false, allowableValues = "0,1")
    private Integer taxReceiptStatus;

    @PrimaryKeyField
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShopId() {
        return shopId;
    }
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getShopProvinceId() {
        return shopProvinceId;
    }
    public void setShopProvinceId(Integer shopProvinceId) {
        this.shopProvinceId = shopProvinceId;
    }

    public Integer getShopCityId() {
        return shopCityId;
    }
    public void setShopCityId(Integer shopCityId) {
        this.shopCityId = shopCityId;
    }

    public Integer getShopCountyId() {
        return shopCountyId;
    }
    public void setShopCountyId(Integer shopCountyId) {
        this.shopCountyId = shopCountyId;
    }

    public Integer getShopTownId() {
        return shopTownId;
    }
    public void setShopTownId(Integer shopTownId) {
        this.shopTownId = shopTownId;
    }

    public String getShopProvince() {
        return shopProvince;
    }
    public void setShopProvince(String shopProvince) {
        this.shopProvince = shopProvince;
    }

    public String getShopCity() {
        return shopCity;
    }
    public void setShopCity(String shopCity) {
        this.shopCity = shopCity;
    }

    public String getShopCounty() {
        return shopCounty;
    }
    public void setShopCounty(String shopCounty) {
        this.shopCounty = shopCounty;
    }

    public String getShopTown() {
        return shopTown;
    }
    public void setShopTown(String shopTown) {
        this.shopTown = shopTown;
    }

    public String getShopAdd() {
        return shopAdd;
    }
    public void setShopAdd(String shopAdd) {
        this.shopAdd = shopAdd;
    }

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

    public String getGoodsManagementCategory() {
        return goodsManagementCategory;
    }
    public void setGoodsManagementCategory(String goodsManagementCategory) {
        this.goodsManagementCategory = goodsManagementCategory;
    }

    public Integer getShopLevel() {
        return shopLevel;
    }
    public void setShopLevel(Integer shopLevel) {
        this.shopLevel = shopLevel;
    }

    public Integer getShopLevelApply() {
        return shopLevelApply;
    }
    public void setShopLevelApply(Integer shopLevelApply) {
        this.shopLevelApply = shopLevelApply;
    }
    public Double getStoreSpaceCapacity() {
        return storeSpaceCapacity;
    }
    public void setStoreSpaceCapacity(Double storeSpaceCapacity) {
        this.storeSpaceCapacity = storeSpaceCapacity;
    }

    public String getShopLogo() {
        return shopLogo;
    }
    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getShopBanner() {
        return shopBanner;
    }
    public void setShopBanner(String shopBanner) {
        this.shopBanner = shopBanner;
    }

    public String getShopDesc() {
        return shopDesc;
    }
    public void setShopDesc(String shopDesc) {
        this.shopDesc = shopDesc;
    }

    public Integer getShopRecommend() {
        return shopRecommend;
    }
    public void setShopRecommend(Integer shopRecommend) {
        this.shopRecommend = shopRecommend;
    }

    public Integer getShopThemeid() {
        return shopThemeid;
    }
    public void setShopThemeid(Integer shopThemeid) {
        this.shopThemeid = shopThemeid;
    }

    public String getShopThemePath() {
        return shopThemePath;
    }
    public void setShopThemePath(String shopThemePath) {
        this.shopThemePath = shopThemePath;
    }

    public Integer getWapThemeid() {
        return wapThemeid;
    }
    public void setWapThemeid(Integer wapThemeid) {
        this.wapThemeid = wapThemeid;
    }

    public String getWapThemePath() {
        return wapThemePath;
    }
    public void setWapThemePath(String wapThemePath) {
        this.wapThemePath = wapThemePath;
    }

    public Double getShopCredit() {
        return shopCredit;
    }

    public void setShopCredit(Double shopCredit) {
        this.shopCredit = shopCredit;
    }

    public Double getShopPraiseRate() {
        return shopPraiseRate;
    }
    public void setShopPraiseRate(Double shopPraiseRate) {
        this.shopPraiseRate = shopPraiseRate;
    }

    public Double getShopDescriptionCredit() {
        return shopDescriptionCredit;
    }

    public void setShopDescriptionCredit(Double shopDescriptionCredit) {
        this.shopDescriptionCredit = shopDescriptionCredit;
    }

    public Double getShopServiceCredit() {
        return shopServiceCredit;
    }

    public void setShopServiceCredit(Double shopServiceCredit) {
        this.shopServiceCredit = shopServiceCredit;
    }

    public Double getShopDeliveryCredit() {
        return shopDeliveryCredit;
    }

    public void setShopDeliveryCredit(Double shopDeliveryCredit) {
        this.shopDeliveryCredit = shopDeliveryCredit;
    }

    public Integer getShopCollect() {
        return shopCollect;
    }
    public void setShopCollect(Integer shopCollect) {
        this.shopCollect = shopCollect;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }
    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getShopQq() {
        return shopQq;
    }
    public void setShopQq(String shopQq) {
        this.shopQq = shopQq;
    }

    public Double getShopCommission() {
        return shopCommission;
    }
    public void setShopCommission(Double shopCommission) {
        this.shopCommission = shopCommission;
    }

    public Integer getGoodsWarningCount() {
        return goodsWarningCount;
    }
    public void setGoodsWarningCount(Integer goodsWarningCount) {
        this.goodsWarningCount = goodsWarningCount;
    }
	public Integer getSelfOperated() {
		return selfOperated;
	}
	public void setSelfOperated(Integer selfOperated) {
		this.selfOperated = selfOperated;
	}

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getOrdinReceiptStatus() {
        return ordinReceiptStatus;
    }

    public void setOrdinReceiptStatus(Integer ordinReceiptStatus) {
        this.ordinReceiptStatus = ordinReceiptStatus;
    }

    public Integer getElecReceiptStatus() {
        return elecReceiptStatus;
    }

    public void setElecReceiptStatus(Integer elecReceiptStatus) {
        this.elecReceiptStatus = elecReceiptStatus;
    }

    public Integer getTaxReceiptStatus() {
        return taxReceiptStatus;
    }

    public void setTaxReceiptStatus(Integer taxReceiptStatus) {
        this.taxReceiptStatus = taxReceiptStatus;
    }

    @Override
    public String toString() {
        return "ShopDetailDO{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", shopProvinceId=" + shopProvinceId +
                ", shopCityId=" + shopCityId +
                ", shopCountyId=" + shopCountyId +
                ", shopTownId=" + shopTownId +
                ", shopProvince='" + shopProvince + '\'' +
                ", shopCity='" + shopCity + '\'' +
                ", shopCounty='" + shopCounty + '\'' +
                ", shopTown='" + shopTown + '\'' +
                ", shopAdd='" + shopAdd + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyAddress='" + companyAddress + '\'' +
                ", companyPhone='" + companyPhone + '\'' +
                ", companyEmail='" + companyEmail + '\'' +
                ", employeeNum=" + employeeNum +
                ", regMoney=" + regMoney +
                ", linkName='" + linkName + '\'' +
                ", linkPhone='" + linkPhone + '\'' +
                ", legalName='" + legalName + '\'' +
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
                ", bankAccountName='" + bankAccountName + '\'' +
                ", bankNumber='" + bankNumber + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankProvinceId=" + bankProvinceId +
                ", bankCityId=" + bankCityId +
                ", bankCountyId=" + bankCountyId +
                ", bankTownId=" + bankTownId +
                ", bankProvince='" + bankProvince + '\'' +
                ", bankCity='" + bankCity + '\'' +
                ", bankCounty='" + bankCounty + '\'' +
                ", bankTown='" + bankTown + '\'' +
                ", bankImg='" + bankImg + '\'' +
                ", taxesCertificateNum='" + taxesCertificateNum + '\'' +
                ", taxesDistinguishNum='" + taxesDistinguishNum + '\'' +
                ", taxesCertificateImg='" + taxesCertificateImg + '\'' +
                ", goodsManagementCategory='" + goodsManagementCategory + '\'' +
                ", shopLevel=" + shopLevel +
                ", shopLevelApply=" + shopLevelApply +
                ", storeSpaceCapacity=" + storeSpaceCapacity +
                ", shopLogo='" + shopLogo + '\'' +
                ", shopBanner='" + shopBanner + '\'' +
                ", shopDesc='" + shopDesc + '\'' +
                ", shopRecommend=" + shopRecommend +
                ", shopThemeid=" + shopThemeid +
                ", shopThemePath='" + shopThemePath + '\'' +
                ", wapThemeid=" + wapThemeid +
                ", wapThemePath='" + wapThemePath + '\'' +
                ", shopCredit=" + shopCredit +
                ", shopPraiseRate=" + shopPraiseRate +
                ", shopDescriptionCredit=" + shopDescriptionCredit +
                ", shopServiceCredit=" + shopServiceCredit +
                ", shopDeliveryCredit=" + shopDeliveryCredit +
                ", shopCollect=" + shopCollect +
                ", goodsNum=" + goodsNum +
                ", shopQq='" + shopQq + '\'' +
                ", shopCommission=" + shopCommission +
                ", goodsWarningCount=" + goodsWarningCount +
                ", selfOperated=" + selfOperated +
                ", step=" + step +
                ", ordinReceiptStatus=" + ordinReceiptStatus +
                ", elecReceiptStatus=" + elecReceiptStatus +
                ", taxReceiptStatus=" + taxReceiptStatus +
                '}';
    }
}