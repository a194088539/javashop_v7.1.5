package com.enation.app.javashop.core.shop.model.vo;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.text.DecimalFormat;

/**
 * 店铺vo
 *
 * @author zhangjiping
 * @version v1.0
 * @since v7.0
 * 2018年3月21日 上午10:32:52
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShopInfoVO {

    /**
     * 店铺Id
     */
    @Column(name = "shop_id")
    @ApiModelProperty(name = "shop_id", value = "店铺Id", required = false)
    private Integer shopId;

    /**
     * 店铺logo
     */
    @Column(name = "shop_logo")
    @ApiModelProperty(name = "shop_logo", value = "店铺logo", required = false)
    private String shopLogo;
    /**
     * 店铺名称
     */
    @Column(name = "shop_name")
    @ApiModelProperty(name = "shop_name", value = "店铺名称", required = true)
    @NotEmpty(message = "店铺名称必填")
    private String shopName;


    /**
     * 法人身份证
     */
    @Column(name = "legal_id")
    @ApiModelProperty(name = "legal_id", value = "法人身份证", required = true)
    @Size(min = 18, max = 18, message = "身份证长度不符")
    private String legalId;

    /**
     * 店铺详细地址
     */
    @Column(name = "shop_add")
    @ApiModelProperty(name = "shop_add", value = "店铺详细地址", required = false)
    private String shopAdd;


    /**
     * 联系人电话
     */
    @Column(name = "link_phone")
    @ApiModelProperty(name = "link_phone", value = "联系人电话", required = true)
    @NotEmpty(message = "联系人电话必填")
    private String linkPhone;


    /**
     * 店铺客服qq
     */
    @Column(name = "shop_qq")
    @ApiModelProperty(name = "shop_qq", value = "店铺客服qq", required = false)
    private String shopQq;

    /**
     * 店铺简介
     */
    @Column(name = "shop_desc")
    @ApiModelProperty(name = "shop_desc", value = "店铺简介", required = false)
    private String shopDesc;


    /**
     * 是否自营
     */
    @Column(name = "self_operated")
    @ApiModelProperty(name = "self_operated", value = "是否自营 1:是 0:否", required = true)
    private Integer selfOperated;


    /**
     * 店铺信用
     */
    @Column(name = "shop_credit")
    @ApiModelProperty(name = "shop_credit", value = "店铺信用", required = false)
    private Double shopCredit;


    /**
     * 店铺所在省
     */
    @Column(name = "shop_province")
    @ApiModelProperty(name = "shop_province", value = "店铺所在省", required = false)
    private String shopProvince;
    /**
     * 店铺所在市
     */
    @Column(name = "shop_city")
    @ApiModelProperty(name = "shop_city", value = "店铺所在市", required = false)
    private String shopCity;
    /**
     * 店铺所在县
     */
    @Column(name = "shop_county")
    @ApiModelProperty(name = "shop_county", value = "店铺所在县", required = false)
    private String shopCounty;
    /**
     * 店铺所在镇
     */
    @Column(name = "shop_town", allowNullUpdate = true)
    @ApiModelProperty(name = "shop_town", value = "店铺所在镇", required = false)
    private String shopTown;


    /**
     * 店铺所在省id
     */
    @Column(name = "shop_province_id")
    @ApiModelProperty(name = "shop_province_id", value = "店铺所在省id", required = false)
    private Integer shopProvinceId;
    /**
     * 店铺所在市id
     */
    @Column(name = "shop_city_id")
    @ApiModelProperty(name = "shop_city_id", value = "店铺所在市id", required = false)
    private Integer shopCityId;
    /**
     * 店铺所在县id
     */
    @Column(name = "shop_county_id")
    @ApiModelProperty(name = "shop_county_id", value = "店铺所在县id", required = false)
    private Integer shopCountyId;
    /**
     * 店铺所在镇id
     */
    @Column(name = "shop_town_id")
    @ApiModelProperty(name = "shop_town_id", value = "店铺所在镇id", required = false)
    private Integer shopTownId;

    /**
     * 店铺描述相符度
     */
    @Column(name = "shop_description_credit")
    @ApiModelProperty(name = "shop_description_credit", value = "店铺描述相符度", required = false)
    private Double shopDescriptionCredit;


    /**
     * 服务态度分数
     */
    @Column(name = "shop_service_credit")
    @ApiModelProperty(name = "shop_service_credit", value = "服务态度分数", required = false)
    private Double shopServiceCredit;

    /**
     * 发货速度分数
     */
    @Column(name = "shop_delivery_credit")
    @ApiModelProperty(name = "shop_delivery_credit", value = "发货速度分数", required = false)
    private Double shopDeliveryCredit;


    /**
     * 货品预警数
     */
    @Column(name = "goods_warning_count")
    @ApiModelProperty(name = "goods_warning_count", value = "货品预警数", required = false)
    private Integer goodsWarningCount;

    /**
     * 是否允许开具增值税普通发票 0：否，1：是
     */
    @Column(name = "ordin_receipt_status")
    @ApiModelProperty(name = "ordin_receipt_status", value = "是否允许开具增值税普通发票 0：否，1：是", required = false, allowableValues = "0,1")
    private Integer ordinReceiptStatus;
    /**
     * 是否允许开具电子普通发票 0：否，1：是
     */
    @Column(name = "elec_receipt_status")
    @ApiModelProperty(name = "elec_receipt_status", value = "是否允许开具电子普通发票 0：否，1：是", required = false, allowableValues = "0,1")
    private Integer elecReceiptStatus;
    /**
     * 是否允许开具增值税专用发票 0：否，1：是
     */
    @Column(name = "tax_receipt_status")
    @ApiModelProperty(name = "tax_receipt_status", value = "是否允许开具增值税专用发票 0：否，1：是", required = false, allowableValues = "0,1")
    private Integer taxReceiptStatus;


    public ShopInfoVO(ShopVO shopVO) {
        this.setGoodsWarningCount(shopVO.getGoodsWarningCount());
        this.setLegalId(shopVO.getLegalId());
        this.setLinkPhone(shopVO.getLinkPhone());
        this.setSelfOperated(shopVO.getSelfOperated());
        this.setShopAdd(shopVO.getShopAdd());
        this.setShopCity(shopVO.getShopCity());
        this.setShopCityId(shopVO.getShopCityId());
        this.setShopCounty(shopVO.getShopCounty());
        this.setShopCountyId(shopVO.getShopCountyId());
        this.setShopCredit(shopVO.getShopCredit());
        this.setShopDeliveryCredit(shopVO.getShopDeliveryCredit());
        this.setShopDesc(shopVO.getShopDesc());
        this.setShopDescriptionCredit(shopVO.getShopDescriptionCredit());
        this.setShopId(shopVO.getShopId());
        this.setShopLogo(shopVO.getShopLogo());
        this.setShopName(shopVO.getShopName());
        this.setShopProvince(shopVO.getShopProvince());
        this.setShopProvinceId(shopVO.getShopProvinceId());
        this.setShopQq(shopVO.getShopQq());
        this.setShopServiceCredit(shopVO.getShopServiceCredit());
        this.setShopTown(shopVO.getShopTown());
        this.setShopTownId(shopVO.getShopTownId());
        this.setOrdinReceiptStatus(shopVO.getOrdinReceiptStatus());
        this.setElecReceiptStatus(shopVO.getElecReceiptStatus());
        this.setTaxReceiptStatus(shopVO.getTaxReceiptStatus());

    }

    public ShopInfoVO() {

    }


    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getLegalId() {
        return legalId;
    }

    public void setLegalId(String legalId) {
        this.legalId = legalId;
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

    public String getShopQq() {
        return shopQq;
    }

    public void setShopQq(String shopQq) {
        this.shopQq = shopQq;
    }

    public String getShopDesc() {
        return shopDesc;
    }

    public void setShopDesc(String shopDesc) {
        this.shopDesc = shopDesc;
    }

    public Integer getSelfOperated() {
        return selfOperated;
    }

    public void setSelfOperated(Integer selfOperated) {
        this.selfOperated = selfOperated;
    }

    public Double getShopCredit() {
        return shopCredit;
    }

    public void setShopCredit(Double shopCredit) {
        this.shopCredit = shopCredit;
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

    public Double getShopDescriptionCredit() {
        return shopDescriptionCredit;
    }

    public void setShopDescriptionCredit(Double shopDescriptionCredit) {
        this.shopDescriptionCredit = shopDescriptionCredit;
    }

    public Double getShopServiceCredit() {
        return shopServiceCredit;
    }

    public void setShopDeliveryCredit(Double shopDeliveryCredit) {
        this.shopDeliveryCredit = shopDeliveryCredit;
    }

    public Integer getGoodsWarningCount() {
        return goodsWarningCount;
    }

    public void setGoodsWarningCount(Integer goodsWarningCount) {
        this.goodsWarningCount = goodsWarningCount;
    }

    public String getShopDescriptionCreditText() {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(shopDescriptionCredit == null ? 0 : shopDescriptionCredit);
    }

    public String getShopServiceCreditText() {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(shopServiceCredit == null ? 0 : shopServiceCredit);
    }

    public void setShopServiceCredit(Double shopServiceCredit) {
        this.shopServiceCredit = shopServiceCredit;
    }

    public Double getShopDeliveryCredit() {
        return shopDeliveryCredit;
    }

    public String getShopDeliveryCreditText() {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(shopDeliveryCredit == null ? 0 : shopDeliveryCredit);
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
}