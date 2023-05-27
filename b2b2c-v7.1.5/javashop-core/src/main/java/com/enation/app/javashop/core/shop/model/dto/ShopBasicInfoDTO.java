package com.enation.app.javashop.core.shop.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

/**
 * @author zjp
 * @version v7.0
 * @Description 店铺基本信息VO
 * @ClassName ShopBasicInfoDTO
 * @since v7.0 下午4:36 2018/6/6
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShopBasicInfoDTO {
    /**店铺Id*/
    @ApiModelProperty(name="shop_id",value="店铺Id")
    private Integer shopId;
    /**会员Id*/
    @ApiModelProperty(name="member_id",value="会员Id")
    private Integer memberId;
    /**会员名称*/
    @ApiModelProperty(name="member_name",value="会员名称")
    private String memberName;
    /**店铺名称*/
    @ApiModelProperty(name="shop_name",value="店铺名称")
    private String shopName;
    /**店铺状态*/
    @ApiModelProperty(name="shop_disable",value="店铺状态")
    private String shopDisable;
    /**店铺创建时间*/
    @ApiModelProperty(name="shop_createtime",value="店铺创建时间")
    private Long shopCreatetime;
    /**店铺关闭时间*/
    @ApiModelProperty(name="shop_endtime",value="店铺关闭时间")
    private Long shopEndtime;

    /**店铺所在省id*/
    @ApiModelProperty(name="shop_province_id",value="店铺所在省id")
    private Integer shopProvinceId;
    /**店铺所在市id*/
    @ApiModelProperty(name="shop_city_id",value="店铺所在市id")
    private Integer shopCityId;
    /**店铺所在县id*/
    @ApiModelProperty(name="shop_county_id",value="店铺所在县id")
    private Integer shopCountyId;
    /**店铺所在镇id*/
    @ApiModelProperty(name="shop_town_id",value="店铺所在镇id")
    private Integer shopTownId;
    /**店铺所在省*/
    @ApiModelProperty(name="shop_province",value="店铺所在省")
    private String shopProvince;
    /**店铺所在市*/
    @ApiModelProperty(name="shop_city",value="店铺所在市")
    private String shopCity;
    /**店铺所在县*/
    @ApiModelProperty(name="shop_county",value="店铺所在县")
    private String shopCounty;
    /**店铺所在镇*/
    @ApiModelProperty(name="shop_town",value="店铺所在镇")
    private String shopTown;
    /**店铺详细地址*/
    @ApiModelProperty(name="shop_add",value="店铺详细地址")
    private String shopAdd;
    /**店铺logo*/
    @ApiModelProperty(name="shop_logo",value="店铺logo")
    private String shopLogo;
    /**店铺横幅*/
    @ApiModelProperty(name="shop_banner",value="店铺横幅")
    private String shopBanner;
    /**店铺简介*/
    @ApiModelProperty(name="shop_desc",value="店铺简介")
    private String shopDesc;
    /**是否推荐*/
    @ApiModelProperty(name="shop_recommend",value="是否推荐")
    private Integer shopRecommend;
    /**店铺主题id*/
    @ApiModelProperty(name="shop_themeid",value="店铺主题id")
    private Integer shopThemeid;
    /**店铺主题模版路径*/
    @ApiModelProperty(name="shop_theme_path",value="店铺主题模版路径")
    private String shopThemePath;
    /**店铺主题id*/
    @ApiModelProperty(name="wap_themeid",value="店铺主题id")
    private Integer wapThemeid;
    /**wap店铺主题*/
    @ApiModelProperty(name="wap_theme_path",value="wap店铺主题")
    private String wapThemePath;
    /**店铺信用*/
    @ApiModelProperty(name="shop_credit",value="店铺信用")
    private Double shopCredit;
    /**店铺好评率*/
    @ApiModelProperty(name="shop_praise_rate",value="店铺好评率")
    private Double shopPraiseRate;
    /**店铺描述相符度*/
    @ApiModelProperty(name="shop_description_credit",value="店铺描述相符度")
    private Double shopDescriptionCredit;
    /**服务态度分数*/
    @ApiModelProperty(name="shop_service_credit",value="服务态度分数")
    private Double shopServiceCredit;
    /**发货速度分数*/
    @ApiModelProperty(name="shop_delivery_credit",value="发货速度分数")
    private Double shopDeliveryCredit;
    /**店铺收藏数*/
    @ApiModelProperty(name="shop_collect",value="店铺收藏数")
    private Integer shopCollect;
    /**店铺商品数*/
    @ApiModelProperty(name="goods_num",value="店铺商品数")
    private Integer goodsNum;
    /**店铺客服qq*/
    @ApiModelProperty(name="shop_qq",value="店铺客服qq")
    private String shopQq;
    /**是否自营*/
    @ApiModelProperty(name="self_operated",value="是否自营 1:是 0:否")
    private Integer selfOperated;
    /**联系人电话*/
    @ApiModelProperty(name="link_phone",value="联系人电话")
    @NotEmpty(message="联系人电话必填")
    private String linkPhone;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopDisable() {
        return shopDisable;
    }

    public void setShopDisable(String shopDisable) {
        this.shopDisable = shopDisable;
    }

    public Long getShopCreatetime() {
        return shopCreatetime;
    }

    public void setShopCreatetime(Long shopCreatetime) {
        this.shopCreatetime = shopCreatetime;
    }

    public Long getShopEndtime() {
        return shopEndtime;
    }

    public void setShopEndtime(Long shopEndtime) {
        this.shopEndtime = shopEndtime;
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

    public Integer getSelfOperated() {
        return selfOperated;
    }

    public void setSelfOperated(Integer selfOperated) {
        this.selfOperated = selfOperated;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    @Override
    public String toString() {
        return "ShopBasicInfoDTO{" +
                "shopId=" + shopId +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", shopName='" + shopName + '\'' +
                ", shopDisable='" + shopDisable + '\'' +
                ", shopCreatetime=" + shopCreatetime +
                ", shopEndtime=" + shopEndtime +
                ", shopProvinceId=" + shopProvinceId +
                ", shopCityId=" + shopCityId +
                ", shopCountyId=" + shopCountyId +
                ", shopTownId=" + shopTownId +
                ", shopProvince='" + shopProvince + '\'' +
                ", shopCity='" + shopCity + '\'' +
                ", shopCounty='" + shopCounty + '\'' +
                ", shopTown='" + shopTown + '\'' +
                ", shopAdd='" + shopAdd + '\'' +
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
                ", selfOperated=" + selfOperated +
                ", linkPhone='" + linkPhone + '\'' +
                '}';
    }
}
