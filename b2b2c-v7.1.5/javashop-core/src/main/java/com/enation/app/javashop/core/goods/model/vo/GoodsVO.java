package com.enation.app.javashop.core.goods.model.vo;

import com.enation.app.javashop.core.goods.model.dos.GoodsGalleryDO;
import com.enation.app.javashop.core.promotion.exchange.model.dos.ExchangeDO;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author fk
 * @version v1.0
 * @Description: 商家查询商品使用
 * @date 2018/5/21 14:36
 * @since v7.0.0
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GoodsVO implements Serializable {

    private static final long serialVersionUID = 3922135264669953741L;
    @ApiModelProperty(value = "id")
    private Integer goodsId;

    @ApiModelProperty(name = "category_id", value = "分类id")
    private Integer categoryId;

    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @ApiModelProperty(name = "shop_cat_id", value = "店铺分类id")
    private Integer shopCatId;

    @ApiModelProperty(name = "brand_id", value = "品牌id")
    private Integer brandId;

    @ApiModelProperty(name = "goods_name", value = "商品名称")
    private String goodsName;

    @ApiModelProperty(name = "sn", value = "商品编号")
    private String sn;

    @ApiModelProperty(name = "price", value = "商品价格")
    private Double price;

    @ApiModelProperty(name = "cost", value = "成本价格")
    private Double cost;

    @ApiModelProperty(name = "mktprice", value = "市场价格")
    private Double mktprice;

    @ApiModelProperty(name = "weight", value = "重量")
    private Double weight;

    @ApiModelProperty(name = "goods_transfee_charge", value = "谁承担运费0：买家承担，1：卖家承担")
    private Integer goodsTransfeeCharge;

    @ApiModelProperty(name = "intro", value = "详情")
    private String intro;

    @ApiModelProperty(name = "have_spec", value = "是否有规格0没有1有")
    private Integer haveSpec;

    @ApiModelProperty(name = "quantity", value = "库存")
    private Integer quantity;

    @ApiModelProperty(name = "market_enable", value = "是否上架，1上架 0下架")
    private Integer marketEnable;

    @ApiModelProperty(name = "goods_gallery_list", value = "商品相册")
    private List<GoodsGalleryDO> goodsGalleryList;

    @ApiModelProperty(name = "page_title", value = "seo标题", required = false)
    private String pageTitle;

    @ApiModelProperty(name = "meta_keywords", value = "seo关键字", required = false)
    private String metaKeywords;

    @ApiModelProperty(name = "meta_description", value = "seo描述", required = false)
    private String metaDescription;

    @ApiModelProperty(name = "template_id", value = "运费模板id,不需要运费模板时值是0")
    private Integer templateId;

    @ApiModelProperty(value = "商品是否审核，0 待审核，1 审核通过 2 未通过")
    private Integer isAuth;

    @ApiModelProperty(value = "可用库存")
    private Integer enableQuantity;

    @ApiModelProperty(value = "审核备注")
    private String authMessage;

    @ApiModelProperty(name = "goods_type", value = "商品类型NORMAL普通POINT积分")
    private String goodsType;

    @ApiModelProperty(name = "exchange", value = "积分兑换对象")
    private ExchangeDO exchange;
    @ApiModelProperty(name = "category_ids", value = "分类id数组")
    private Integer[] categoryIds;

    @ApiModelProperty(name = "promotion_tip", value = "商品参与的促销活动提示")
    private String promotionTip;

    public GoodsVO() {
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getShopCatId() {
        return shopCatId;
    }

    public void setShopCatId(Integer shopCatId) {
        this.shopCatId = shopCatId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getMktprice() {
        return mktprice;
    }

    public void setMktprice(Double mktprice) {
        this.mktprice = mktprice;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getGoodsTransfeeCharge() {
        return goodsTransfeeCharge;
    }

    public void setGoodsTransfeeCharge(Integer goodsTransfeeCharge) {
        this.goodsTransfeeCharge = goodsTransfeeCharge;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Integer getHaveSpec() {
        return haveSpec;
    }

    public void setHaveSpec(Integer haveSpec) {
        this.haveSpec = haveSpec;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<GoodsGalleryDO> getGoodsGalleryList() {
        return goodsGalleryList;
    }

    public void setGoodsGalleryList(List<GoodsGalleryDO> goodsGalleryList) {
        this.goodsGalleryList = goodsGalleryList;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(Integer isAuth) {
        this.isAuth = isAuth;
    }

    public Integer getMarketEnable() {
        return marketEnable;
    }

    public void setMarketEnable(Integer marketEnable) {
        this.marketEnable = marketEnable;
    }

    public Integer getEnableQuantity() {
        return enableQuantity;
    }

    public void setEnableQuantity(Integer enableQuantity) {
        this.enableQuantity = enableQuantity;

    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getAuthMessage() {
        return authMessage;
    }

    public void setAuthMessage(String authMessage) {
        this.authMessage = authMessage;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public ExchangeDO getExchange() {
        return exchange;
    }

    public void setExchange(ExchangeDO exchange) {
        this.exchange = exchange;
    }

    public Integer[] getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(Integer[] categoryIds) {
        this.categoryIds = categoryIds;
    }

    public String getPromotionTip() {
        return promotionTip;
    }

    public void setPromotionTip(String promotionTip) {
        this.promotionTip = promotionTip;
    }

    @Override
    public String toString() {
        return "GoodsVO{" +
                "goodsId=" + goodsId +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", shopCatId=" + shopCatId +
                ", brandId=" + brandId +
                ", goodsName='" + goodsName + '\'' +
                ", sn='" + sn + '\'' +
                ", price=" + price +
                ", cost=" + cost +
                ", mktprice=" + mktprice +
                ", weight=" + weight +
                ", goodsTransfeeCharge=" + goodsTransfeeCharge +
                ", intro='" + intro + '\'' +
                ", haveSpec=" + haveSpec +
                ", quantity=" + quantity +
                ", marketEnable=" + marketEnable +
                ", goodsGalleryList=" + goodsGalleryList +
                ", pageTitle='" + pageTitle + '\'' +
                ", metaKeywords='" + metaKeywords + '\'' +
                ", metaDescription='" + metaDescription + '\'' +
                ", templateId=" + templateId +
                ", isAuth=" + isAuth +
                ", enableQuantity=" + enableQuantity +
                ", authMessage='" + authMessage + '\'' +
                ", goodsType='" + goodsType + '\'' +
                ", promotionTip='" + promotionTip + '\'' +
                ", exchange=" + exchange +
                ", categoryIds=" + Arrays.toString(categoryIds) +
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
        GoodsVO goodsVO = (GoodsVO) o;

        return new EqualsBuilder()
                .append(goodsId, goodsVO.goodsId)
                .append(categoryId, goodsVO.categoryId)
                .append(categoryName, goodsVO.categoryName)
                .append(shopCatId, goodsVO.shopCatId)
                .append(brandId, goodsVO.brandId)
                .append(goodsName, goodsVO.goodsName)
                .append(sn, goodsVO.sn)
                .append(price, goodsVO.price)
                .append(cost, goodsVO.cost)
                .append(mktprice, goodsVO.mktprice)
                .append(weight, goodsVO.weight)
                .append(goodsTransfeeCharge, goodsVO.goodsTransfeeCharge)
                .append(intro, goodsVO.intro)
                .append(haveSpec, goodsVO.haveSpec)
                .append(quantity, goodsVO.quantity)
                .append(marketEnable, goodsVO.marketEnable)
                .append(goodsGalleryList, goodsVO.goodsGalleryList)
                .append(pageTitle, goodsVO.pageTitle)
                .append(metaKeywords, goodsVO.metaKeywords)
                .append(metaDescription, goodsVO.metaDescription)
                .append(templateId, goodsVO.templateId)
                .append(isAuth, goodsVO.isAuth)
                .append(enableQuantity, goodsVO.enableQuantity)
                .append(authMessage, goodsVO.authMessage)
                .append(goodsType, goodsVO.goodsType)
                .append(exchange, goodsVO.exchange)
                .append(categoryIds, goodsVO.categoryIds)
                .append(promotionTip, goodsVO.promotionTip)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(goodsId)
                .append(categoryId)
                .append(categoryName)
                .append(shopCatId)
                .append(brandId)
                .append(goodsName)
                .append(sn)
                .append(price)
                .append(cost)
                .append(mktprice)
                .append(weight)
                .append(goodsTransfeeCharge)
                .append(intro)
                .append(haveSpec)
                .append(quantity)
                .append(marketEnable)
                .append(goodsGalleryList)
                .append(pageTitle)
                .append(metaKeywords)
                .append(metaDescription)
                .append(templateId)
                .append(isAuth)
                .append(enableQuantity)
                .append(authMessage)
                .append(goodsType)
                .append(exchange)
                .append(categoryIds)
                .append(promotionTip)
                .toHashCode();
    }

}


