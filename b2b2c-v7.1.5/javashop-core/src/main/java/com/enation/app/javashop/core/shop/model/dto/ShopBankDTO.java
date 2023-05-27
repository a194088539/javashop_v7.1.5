package com.enation.app.javashop.core.shop.model.dto;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 店铺银行信息DTO
 * @author zjp
 * @version v7.0
 * @since v7.0 上午10:05 2018/5/3
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShopBankDTO {

    /**店铺Id*/
    @Column(name = "shop_id")
    @ApiModelProperty(name="shop_id",value="店铺Id",required=false)
    private Integer shopId;
    /**店铺佣金比例*/
    @Column(name = "shop_commission")
    @ApiModelProperty(name="shop_commission",value="店铺佣金比例",required=false)
    private Double shopCommission;
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
    @ApiModelProperty(name="bank_province_id",value="开户银行所在省id",required=true)
    @NotNull(message="开户银行所在省id必填")
    private Integer bankProvinceId;
    /**开户银行所在市id*/
    @Column(name = "bank_city_id")
    @ApiModelProperty(name="bank_city_id",value="开户银行所在市id",required=true)
    @NotNull(message="开户银行所在市id必填")
    private Integer bankCityId;
    /**开户银行所在县id*/
    @Column(name = "bank_county_id")
    @ApiModelProperty(name="bank_county_id",value="开户银行所在县id",required=true)
    @NotNull(message="开户银行所在县id必填")
    private Integer bankCountyId;
    /**开户银行所在镇id*/
    @Column(name = "bank_town_id")
    @ApiModelProperty(name="bank_town_id",value="开户银行所在镇id",required=false)
    private Integer bankTownId;
    /**开户银行所在省*/
    @Column(name = "bank_province")
    @ApiModelProperty(name="bank_province",value="开户银行所在省",required=true)
    @NotEmpty(message="开户银行所在省必填")
    private String bankProvince;
    /**开户银行所在市*/
    @Column(name = "bank_city")
    @ApiModelProperty(name="bank_city",value="开户银行所在市",required=true)
    @NotEmpty(message="开户银行所在市必填")
    private String bankCity;
    /**开户银行所在县*/
    @Column(name = "bank_county")
    @ApiModelProperty(name="bank_county",value="开户银行所在县",required=true)
    @NotEmpty(message="开户银行所在县必填")
    private String bankCounty;
    /**开户银行所在镇*/
    @Column(name = "bank_town")
    @ApiModelProperty(name="bank_town",value="开户银行所在镇",required=false)
    private String bankTown;
    /**店铺名称*/
    @Column(name = "shop_name")
    @ApiModelProperty(name="shop_name",value="店铺名称",required=false)
    private String shopName;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Double getShopCommission() {
        return shopCommission;
    }

    public void setShopCommission(Double shopCommission) {
        this.shopCommission = shopCommission;
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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    @Override
    public String toString() {
        return "ShopBankDTO{" +
                "shopId=" + shopId +
                ", shopCommission=" + shopCommission +
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
                '}';
    }
}
