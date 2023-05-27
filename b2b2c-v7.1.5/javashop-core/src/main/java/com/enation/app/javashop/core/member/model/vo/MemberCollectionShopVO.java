package com.enation.app.javashop.core.member.model.vo;

import com.enation.app.javashop.core.goods.model.vo.GoodsVO;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import java.util.List;


/**
 * 会员收藏店铺表实体
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-30 20:34:23
 */

public class MemberCollectionShopVO {
    /**
     * 收藏id
     */
    private Integer id;
    /**
     * 会员id
     */
    @ApiModelProperty(name = "member_id", value = "会员id", required = false)
    private Integer memberId;
    /**
     * 店铺id
     */
    @Min(message = "必须为数字", value = 0)
    @ApiModelProperty(name = "shop_id", value = "店铺id", required = false)
    private Integer shopId;
    /**
     * 店铺名称
     */
    @ApiModelProperty(name = "shop_name", value = "店铺名称", required = true)
    private String shopName;
    /**
     * 收藏时间
     */
    @ApiModelProperty(name = "create_time", value = "收藏时间", required = true)
    private Long createTime;
    /**
     * 店铺logo
     */
    @ApiModelProperty(name = "logo", value = "店铺logo", required = false)
    private String logo;
    /**
     * 店铺所在省
     */
    @ApiModelProperty(name = "shop_province", value = "店铺所在省", required = false)
    private String shopProvince;
    /**
     * 店铺所在市
     */
    @ApiModelProperty(name = "shop_city", value = "店铺所在市", required = false)
    private String shopCity;
    /**
     * 店铺所在县
     */
    @ApiModelProperty(name = "shop_region", value = "店铺所在县", required = false)
    private String shopRegion;
    /**
     * 店铺所在镇
     */
    @ApiModelProperty(name = "shop_town", value = "店铺所在镇", required = false)
    private String shopTown;

    /**
     * 店铺好评率
     */
    @ApiModelProperty(name = "shop_praise_rate", value = "店铺好评率", required = false)
    private Double shopPraiseRate;
    /**
     * 店铺描述相符度
     */
    @ApiModelProperty(name = "shop_description_credit", value = "店铺描述相符度", required = false)
    private Double shopDescriptionCredit;
    /**
     * 服务态度分数
     */
    @ApiModelProperty(name = "shop_service_credit", value = "服务态度分数", required = false)
    private Double shopServiceCredit;
    /**
     * 发货速度分数
     */
    @ApiModelProperty(name = "shop_delivery_credit", value = "发货速度分数", required = false)
    private Double shopDeliveryCredit;
    /**
     * 店铺所在镇
     */
    private List<GoodsVO> goodsList;

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

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public String getShopRegion() {
        return shopRegion;
    }

    public void setShopRegion(String shopRegion) {
        this.shopRegion = shopRegion;
    }

    public String getShopTown() {
        return shopTown;
    }

    public void setShopTown(String shopTown) {
        this.shopTown = shopTown;
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

    public List<GoodsVO> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsVO> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public String toString() {
        return "MemberCollectionShopVO{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", createTime=" + createTime +
                ", logo='" + logo + '\'' +
                ", shopProvince='" + shopProvince + '\'' +
                ", shopCity='" + shopCity + '\'' +
                ", shopRegion='" + shopRegion + '\'' +
                ", shopTown='" + shopTown + '\'' +
                ", shopPraiseRate=" + shopPraiseRate +
                ", shopDescriptionCredit=" + shopDescriptionCredit +
                ", shopServiceCredit=" + shopServiceCredit +
                ", shopDeliveryCredit=" + shopDeliveryCredit +
                ", goodsList=" + goodsList +
                '}';
    }
}