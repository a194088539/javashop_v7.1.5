package com.enation.app.javashop.core.goodssearch.model;

import com.enation.app.javashop.framework.elasticsearch.EsSettings;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * Created by kingapex on 2018/7/19.
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/7/19
 */
@Document(indexName = "#{esConfig.indexName}_"+  EsSettings.GOODS_INDEX_NAME, type = EsSettings.GOODS_TYPE_NAME)
public class GoodsIndex {
    public GoodsIndex() {

    }

    @Id
    private Integer goodsId;

    @Field(type = FieldType.text, analyzer = "ik_max_word")
    private String goodsName;

    @Field(type = FieldType.text)
    private String thumbnail;

    @Field(type = FieldType.text)
    private String small;

    @Field(type = FieldType.Integer)
    private Integer buyCount;

    @Field(type = FieldType.Integer)
    private Integer sellerId;

    @Field(type = FieldType.text)
    private String sellerName;

    @Field(type = FieldType.Integer)
    private Integer shopCatId;

    @Field(type = FieldType.text)
    private String shopCatPath;

    @Field(type = FieldType.Integer)
    private Integer commentNum;

    @Field(type = FieldType.Double)
    private Double grade;

    @Field(type = FieldType.Double)
    private double discountPrice;

    @Field(type = FieldType.Double)
    private double price;

    @Field(type = FieldType.Integer)
    private Integer brand;

    @Field(type = FieldType.Integer)
    private Integer categoryId;

    @Field(type = FieldType.text)
    private String categoryPath;

    @Field(type = FieldType.Integer)
    private Integer disabled;

    @Field(type = FieldType.Integer)
    private Integer marketEnable;

    @Field(type = FieldType.Integer)
    private Integer isAuth;

    @Field(type = FieldType.text)
    private String intro;

    @Field(type = FieldType.Integer)
    private Integer priority;

    /**
     * 是否自营商品 0否 1是
     */
    @Field(type = FieldType.Integer)
    private Integer selfOperated;

    @Field(type = FieldType.Nested, index = true, store = true)
    private List<Param> params;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getBrand() {
        return brand;
    }

    public void setBrand(Integer brand) {
        this.brand = brand;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryPath() {
        return categoryPath;
    }

    public void setCategoryPath(String categoryPath) {
        this.categoryPath = categoryPath;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public Integer getMarketEnable() {
        return marketEnable;
    }

    public void setMarketEnable(Integer marketEnable) {
        this.marketEnable = marketEnable;
    }

    public Integer getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(Integer isAuth) {
        this.isAuth = isAuth;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    public Integer getShopCatId() {
        return shopCatId;
    }

    public void setShopCatId(Integer shopCatId) {
        this.shopCatId = shopCatId;
    }

    public String getShopCatPath() {
        return shopCatPath;
    }

    public void setShopCatPath(String shopCatPath) {
        this.shopCatPath = shopCatPath;
    }

    public Integer getSelfOperated() {
        return selfOperated;
    }

    public void setSelfOperated(Integer selfOperated) {
        this.selfOperated = selfOperated;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "GoodsIndex{" +
                "goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", small='" + small + '\'' +
                ", buyCount=" + buyCount +
                ", sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                ", shopCatId=" + shopCatId +
                ", shopCatPath='" + shopCatPath + '\'' +
                ", commentNum=" + commentNum +
                ", grade=" + grade +
                ", discountPrice=" + discountPrice +
                ", price=" + price +
                ", brand=" + brand +
                ", categoryId=" + categoryId +
                ", categoryPath='" + categoryPath + '\'' +
                ", disabled=" + disabled +
                ", marketEnable=" + marketEnable +
                ", isAuth=" + isAuth +
                ", intro='" + intro + '\'' +
                ", priority=" + priority +
                ", selfOperated=" + selfOperated +
                ", params=" + params +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GoodsIndex that = (GoodsIndex) o;

        return new EqualsBuilder()
                .append(discountPrice, that.discountPrice)
                .append(price, that.price)
                .append(goodsId, that.goodsId)
                .append(goodsName, that.goodsName)
                .append(thumbnail, that.thumbnail)
                .append(small, that.small)
                .append(buyCount, that.buyCount)
                .append(sellerId, that.sellerId)
                .append(sellerName, that.sellerName)
                .append(shopCatId, that.shopCatId)
                .append(shopCatPath, that.shopCatPath)
                .append(commentNum, that.commentNum)
                .append(grade, that.grade)
                .append(brand, that.brand)
                .append(categoryId, that.categoryId)
                .append(categoryPath, that.categoryPath)
                .append(disabled, that.disabled)
                .append(marketEnable, that.marketEnable)
                .append(isAuth, that.isAuth)
                .append(intro, that.intro)
                .append(priority, that.priority)
                .append(selfOperated, that.selfOperated)
                .append(params, that.params)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(goodsId)
                .append(goodsName)
                .append(thumbnail)
                .append(small)
                .append(buyCount)
                .append(sellerId)
                .append(sellerName)
                .append(shopCatId)
                .append(shopCatPath)
                .append(commentNum)
                .append(grade)
                .append(discountPrice)
                .append(price)
                .append(brand)
                .append(categoryId)
                .append(categoryPath)
                .append(disabled)
                .append(marketEnable)
                .append(isAuth)
                .append(intro)
                .append(priority)
                .append(selfOperated)
                .append(params)
                .toHashCode();
    }

}