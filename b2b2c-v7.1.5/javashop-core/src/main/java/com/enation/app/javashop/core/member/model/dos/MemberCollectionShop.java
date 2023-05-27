package com.enation.app.javashop.core.member.model.dos;

import java.io.Serializable;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;

/**
 * 会员收藏店铺表实体
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-07-18 14:23:58
 */
@Table(name = "es_member_collection_shop")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberCollectionShop implements Serializable {

    private static final long serialVersionUID = 1827422941070835L;

    /**
     * 收藏id
     */
    @Id(name = "id")
    @ApiModelProperty(hidden = true)
    private Integer id;
    /**
     * 会员id
     */
    @Column(name = "member_id")
    @Min(message = "必须为数字", value = 0)
    @ApiModelProperty(name = "member_id", value = "会员id", required = false)
    private Integer memberId;
    /**
     * 店铺id
     */
    @Column(name = "shop_id")
    @Min(message = "必须为数字", value = 0)
    @ApiModelProperty(name = "shop_id", value = "店铺id", required = false)
    private Integer shopId;
    /**
     * 店铺名称
     */
    @Column(name = "shop_name")
    @ApiModelProperty(name = "shop_name", value = "店铺名称", required = false)
    private String shopName;
    /**
     * 收藏时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name = "create_time", value = "收藏时间", required = false)
    private Long createTime;
    /**
     * 店铺logo
     */
    @Column(name = "logo")
    @ApiModelProperty(name = "logo", value = "店铺logo", required = false)
    private String logo;
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
    @Column(name = "shop_region")
    @ApiModelProperty(name = "shop_region", value = "店铺所在县", required = false)
    private String shopRegion;
    /**
     * 店铺所在镇
     */
    @Column(name = "shop_town")
    @ApiModelProperty(name = "shop_town", value = "店铺所在镇", required = false)
    private String shopTown;
    /**
     * 店铺好评率
     */
    @Column(name = "shop_praise_rate")
    @ApiModelProperty(name = "shop_praise_rate", value = "店铺好评率", required = false)
    private Double shopPraiseRate;
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

    @PrimaryKeyField
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberCollectionShop that = (MemberCollectionShop) o;
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (memberId != null ? !memberId.equals(that.memberId) : that.memberId != null) {
            return false;
        }
        if (shopId != null ? !shopId.equals(that.shopId) : that.shopId != null) {
            return false;
        }
        if (shopName != null ? !shopName.equals(that.shopName) : that.shopName != null) {
            return false;
        }
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) {
            return false;
        }
        if (logo != null ? !logo.equals(that.logo) : that.logo != null) {
            return false;
        }
        if (shopProvince != null ? !shopProvince.equals(that.shopProvince) : that.shopProvince != null) {
            return false;
        }
        if (shopCity != null ? !shopCity.equals(that.shopCity) : that.shopCity != null) {
            return false;
        }
        if (shopRegion != null ? !shopRegion.equals(that.shopRegion) : that.shopRegion != null) {
            return false;
        }
        if (shopTown != null ? !shopTown.equals(that.shopTown) : that.shopTown != null) {
            return false;
        }
        if (shopPraiseRate != null ? !shopPraiseRate.equals(that.shopPraiseRate) : that.shopPraiseRate != null) {
            return false;
        }
        if (shopDescriptionCredit != null ? !shopDescriptionCredit.equals(that.shopDescriptionCredit) : that.shopDescriptionCredit != null) {
            return false;
        }
        if (shopServiceCredit != null ? !shopServiceCredit.equals(that.shopServiceCredit) : that.shopServiceCredit != null) {
            return false;
        }
        return shopDeliveryCredit != null ? shopDeliveryCredit.equals(that.shopDeliveryCredit) : that.shopDeliveryCredit == null;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (memberId != null ? memberId.hashCode() : 0);
        result = 31 * result + (shopId != null ? shopId.hashCode() : 0);
        result = 31 * result + (shopName != null ? shopName.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (logo != null ? logo.hashCode() : 0);
        result = 31 * result + (shopProvince != null ? shopProvince.hashCode() : 0);
        result = 31 * result + (shopCity != null ? shopCity.hashCode() : 0);
        result = 31 * result + (shopRegion != null ? shopRegion.hashCode() : 0);
        result = 31 * result + (shopTown != null ? shopTown.hashCode() : 0);
        result = 31 * result + (shopPraiseRate != null ? shopPraiseRate.hashCode() : 0);
        result = 31 * result + (shopDescriptionCredit != null ? shopDescriptionCredit.hashCode() : 0);
        result = 31 * result + (shopServiceCredit != null ? shopServiceCredit.hashCode() : 0);
        result = 31 * result + (shopDeliveryCredit != null ? shopDeliveryCredit.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MemberCollectionShop{" +
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
                '}';
    }


}