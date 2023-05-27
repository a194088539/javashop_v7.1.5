package com.enation.app.javashop.core.shop.model.vo;

import javax.validation.constraints.NotEmpty;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 店铺设置VO
 *
 * @author zhangjiping
 * @version v1.0
 * @since v7.0
 * 2018年3月21日 下午7:58:55
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShopSettingVO {
    /**
     * 店铺Id
     */
    @Column(name = "shop_id")
    @ApiModelProperty(name = "shop_id", value = "店铺Id", required = false)
    private Integer shopId;
    /**
     * 店铺所在省id
     */
    @Column(name = "shop_province_id")
    @ApiModelProperty(name = "shop_province_id", value = "店铺所在省id", required = false, hidden = true)
    private Integer shopProvinceId;
    /**
     * 店铺所在市id
     */
    @Column(name = "shop_city_id")
    @ApiModelProperty(name = "shop_city_id", value = "店铺所在市id", required = false, hidden = true)
    private Integer shopCityId;
    /**
     * 店铺所在县id
     */
    @Column(name = "shop_county_id")
    @ApiModelProperty(name = "shop_county_id", value = "店铺所在县id", required = false, hidden = true)
    private Integer shopCountyId;
    /**
     * 店铺所在镇id
     */
    @Column(name = "shop_town_id")
    @ApiModelProperty(name = "shop_town_id", value = "店铺所在镇id", required = false, hidden = true)
    private Integer shopTownId;
    /**
     * 店铺所在省
     */
    @Column(name = "shop_province")
    @ApiModelProperty(name = "shop_province", value = "店铺所在省", required = false, hidden = true)
    private String shopProvince;
    /**
     * 店铺所在市
     */
    @Column(name = "shop_city")
    @ApiModelProperty(name = "shop_city", value = "店铺所在市", required = false, hidden = true)
    private String shopCity;
    /**
     * 店铺所在县
     */
    @Column(name = "shop_county")
    @ApiModelProperty(name = "shop_county", value = "店铺所在县", required = false, hidden = true)
    private String shopCounty;
    /**
     * 店铺所在镇
     */
    @Column(name = "shop_town", allowNullUpdate = true)
    @ApiModelProperty(name = "shop_town", value = "店铺所在镇", required = false, hidden = true)
    private String shopTown;
    /**
     * 店铺详细地址
     */
    @Column(name = "shop_add")
    @ApiModelProperty(name = "shop_add", value = "店铺详细地址", required = true)
    private String shopAdd;
    /**
     * 联系人电话
     */
    @Column(name = "link_phone")
    @ApiModelProperty(name = "link_phone", value = "联系人电话", required = true)
    @NotEmpty(message = "联系人电话必填")
    private String linkPhone;
    /**
     * 店铺logo
     */
    @Column(name = "shop_logo")
    @ApiModelProperty(name = "shop_logo", value = "店铺logo", required = false)
    private String shopLogo;
    /**
     * 店铺横幅
     */
    @Column(name = "shop_banner")
    @ApiModelProperty(name = "shop_banner", value = "店铺横幅", required = false)
    private String shopBanner;
    /**
     * 店铺简介
     */
    @Column(name = "shop_desc")
    @ApiModelProperty(name = "shop_desc", value = "店铺简介", required = false)
    private String shopDesc;
    /**
     * 店铺客服qq
     */
    @Column(name = "shop_qq")
    @ApiModelProperty(name = "shop_qq", value = "店铺客服qq", required = false)
    private String shopQq;

    /**
     * 店铺名称
     */
    @Column(name = "shop_name")
    @ApiModelProperty(name = "shop_name", value = "店铺名称", required = false)
    private String shopName;

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

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
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

    public String getShopQq() {
        return shopQq;
    }

    public void setShopQq(String shopQq) {
        this.shopQq = shopQq;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    @Override
    public String toString() {
        return "ShopSettingVO [shopId=" + shopId + ", shopProvinceId=" + shopProvinceId + ", shopCityId=" + shopCityId
                + ", shopCountyId=" + shopCountyId + ", shopTownId=" + shopTownId + ", shopProvince=" + shopProvince
                + ", shopCity=" + shopCity + ", shopCounty=" + shopCounty + ", shopTown=" + shopTown + ", shopAdd="
                + shopAdd + ", linkPhone=" + linkPhone + ", shopLogo=" + shopLogo + ", shopBanner=" + shopBanner
                + ", shopDesc=" + shopDesc + ", shopQq=" + shopQq + "]";
    }
}

