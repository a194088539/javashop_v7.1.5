package com.enation.app.javashop.core.goods.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 商品查询条件
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月21日 下午3:46:04
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GoodsQueryParam {
    /**
     * 页码
     */
    @ApiModelProperty(name = "page_no", value = "页码", required = false)
    private Integer pageNo;
    /**
     * 分页数
     */
    @ApiModelProperty(name = "page_size", value = "分页数", required = false)
    private Integer pageSize;
    /**
     * 是否上架 0代表已下架，1代表已上架
     */
    @ApiModelProperty(name = "market_enable", value = "是否上架 0代表已下架，1代表已上架")
    @Min(value = 0 , message = "审核状态不正确")
    @Max(value = 2 , message = "审核状态不正确")
    private Integer marketEnable;
    /**
     * 店铺分类
     */
    @ApiModelProperty(name = "shop_cat_path", value = "店铺分类Path0|10|")
    private String shopCatPath;
    /**
     * 关键字
     */
    @ApiModelProperty(name = "keyword", value = "关键字")
    private String keyword;
    /**
     * 商品名称
     */
    @ApiModelProperty(name = "goods_name", value = "商品名称")
    private String goodsName;
    /**
     * 商品编号
     */
    @ApiModelProperty(name = "goods_sn", value = "商品编号")
    private String goodsSn;
    /**
     * 店铺名称
     */
    @ApiModelProperty(name = "seller_name", value = "店铺名称")
    private String sellerName;
    /**
     * 卖家id
     */
    @ApiModelProperty(name = "seller_id", value = "卖家id")
    private Integer sellerId;
    /**
     * 商品分类路径
     */
    @ApiModelProperty(name = "category_path", value = "商品分类路径，例如0|10|")
    private String categoryPath;
    /**
     * 商品审核状态
     */
    @ApiModelProperty(name = "is_auth", value = "商品审核状态 0：待审核，1：审核通过，2：审核拒绝")
    @Min(value = 0 , message = "审核状态不正确")
    @Max(value = 2 , message = "审核状态不正确")
    private Integer isAuth;
    /**
     * 商品是否已放入回收站
     */
    @ApiModelProperty(name = "disabled", value = "商品是否已放入回收站 0：是，1：否")
    @Min(value = 0 , message = "值不正确")
    @Max(value = 0 , message = "值不正确")
    private Integer disabled;
    /**
     * 商品类型
     */
    @ApiModelProperty(name = "goods_type", value = "商品类型 NORMAL：正常商品，POINT：积分商品")
    private String  goodsType;
    /**
     * 商品品牌
     */
    @ApiModelProperty(name = "brand_id", value = "商品品牌ID")
    private Integer brandId;
    /**
     * 商品价格--价格区间起始值
     */
    @ApiModelProperty(name = "brand_id", value = "商品价格--价格区间起始值")
    private Double start_price;
    /**
     * 商品价格--价格区间结束值
     */
    @ApiModelProperty(name = "brand_id", value = "商品品牌ID")
    private Double end_price;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getMarketEnable() {
        return marketEnable;
    }

    public void setMarketEnable(Integer marketEnable) {
        this.marketEnable = marketEnable;
    }


    public String getShopCatPath() {
        return shopCatPath;
    }

    public void setShopCatPath(String shopCatPath) {
        this.shopCatPath = shopCatPath;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsSn() {
        return goodsSn;
    }

    public void setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getCategoryPath() {
        return categoryPath;
    }

    public void setCategoryPath(String categoryPath) {
        this.categoryPath = categoryPath;
    }

    public Integer getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(Integer isAuth) {
        this.isAuth = isAuth;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Double getStart_price() {
        return start_price;
    }

    public void setStart_price(Double start_price) {
        this.start_price = start_price;
    }

    public Double getEnd_price() {
        return end_price;
    }

    public void setEnd_price(Double end_price) {
        this.end_price = end_price;
    }

    @Override
    public String toString() {
        return "GoodsQueryParam{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", marketEnable=" + marketEnable +
                ", shopCatPath='" + shopCatPath + '\'' +
                ", keyword='" + keyword + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsSn='" + goodsSn + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", sellerId=" + sellerId +
                ", categoryPath='" + categoryPath + '\'' +
                ", isAuth=" + isAuth +
                ", disabled=" + disabled +
                ", goodsType='" + goodsType + '\'' +
                ", brandId=" + brandId +
                ", start_price=" + start_price +
                ", end_price=" + end_price +
                '}';
    }
}
