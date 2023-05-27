package com.enation.app.javashop.core.shop.model.vo;

import javax.validation.constraints.NotEmpty;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * 申请开店第四部VO
 * @author zhangjiping
 * @version v1.0
 * @since v7.0
 * 2018年3月21日 下午4:08:19
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ApplyStep4VO {
	/**店铺名称*/
	@Column(name = "shop_name")
	@ApiModelProperty(name="shop_name",value="店铺名称",required=true)
	@NotEmpty(message="店铺名称必填")
	private String shopName;

	/**店铺经营类目*/
	@Column(name = "goods_management_category")
	@ApiModelProperty(name="goods_management_category",value="店铺经营类目",required=true)
	@NotEmpty(message="店铺经营类目必填")
	private String goodsManagementCategory;
	/**店铺所在省id*/
	@Column(name = "shop_province_id")
	@ApiModelProperty(name="shop_province_id",value="店铺所在省id",required=false,hidden = true)
	private Integer shopProvinceId;
	/**店铺所在市id*/
	@Column(name = "shop_city_id")
	@ApiModelProperty(name="shop_city_id",value="店铺所在市id",required=false,hidden = true)
	private Integer shopCityId;
	/**店铺所在县id*/
	@Column(name = "shop_county_id")
	@ApiModelProperty(name="shop_county_id",value="店铺所在县id",required=false,hidden = true)
	private Integer shopCountyId;
	/**店铺所在镇id*/
	@Column(name = "shop_town_id")
	@ApiModelProperty(name="shop_town_id",value="店铺所在镇id",required=false,hidden = true)
	private Integer shopTownId;
	/**店铺所在省*/
	@Column(name = "shop_province")
	@ApiModelProperty(name="shop_province",value="店铺所在省",required=false,hidden = true)
	private String shopProvince;
	/**店铺所在市*/
	@Column(name = "shop_city")
	@ApiModelProperty(name="shop_city",value="店铺所在市",required=false,hidden = true)
	private String shopCity;
	/**店铺所在县*/
	@Column(name = "shop_county")
	@ApiModelProperty(name="shop_county",value="店铺所在县",required=false,hidden = true)
	private String shopCounty;
	/**店铺所在镇*/
	@Column(name = "shop_town",allowNullUpdate = true)
	@ApiModelProperty(name="shop_town",value="店铺所在镇",required=false,hidden = true)
	private String shopTown;
	/**申请开店进度*/
	@Column(name = "step")
	@ApiModelProperty(name="step",value="申请开店进度：1,2,3,4",required=false)
	private Integer step;
	/**店铺详细地址*/
	@Column(name = "shop_add",allowNullUpdate = true)
	@ApiModelProperty(name="shop_add",value="店铺详细地址",required=false,hidden = true)
	private String shopAdd;

	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getGoodsManagementCategory() {
		return goodsManagementCategory;
	}
	public void setGoodsManagementCategory(String goodsManagementCategory) {
		this.goodsManagementCategory = goodsManagementCategory;
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

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public String getShopAdd() {
		return shopAdd;
	}

	public void setShopAdd(String shopAdd) {
		this.shopAdd = shopAdd;
	}

	@Override
	public String toString() {
		return "ApplyStep4VO{" +
				"shopName='" + shopName + '\'' +
				", goodsManagementCategory='" + goodsManagementCategory + '\'' +
				", shopProvinceId=" + shopProvinceId +
				", shopCityId=" + shopCityId +
				", shopCountyId=" + shopCountyId +
				", shopTownId=" + shopTownId +
				", shopProvince='" + shopProvince + '\'' +
				", shopCity='" + shopCity + '\'' +
				", shopCounty='" + shopCounty + '\'' +
				", shopTown='" + shopTown + '\'' +
				", step=" + step +
				", shopAdd='" + shopAdd + '\'' +
				'}';
	}
}