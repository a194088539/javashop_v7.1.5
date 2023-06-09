package com.enation.app.javashop.core.goods.model.vo;

import java.io.Serializable;
import java.util.List;

import com.enation.app.javashop.core.goods.model.dos.GoodsGalleryDO;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 商品详情页使用
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月29日 上午9:54:05
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GoodsShowDetail extends CacheGoods implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3531885212488390703L;
    @ApiModelProperty(name = "分类名称")
    private String categoryName;
    @ApiModelProperty(name = "是否下架或删除，1正常  0 下架或删除")
    private Integer goodsOff;
    @ApiModelProperty(name = "商品参数")
    private List<GoodsParamsGroupVO> paramList;
    @ApiModelProperty(name = "商品相册")
    private List<GoodsGalleryDO> galleryList;

    /**
     * seo标题
     */
    @ApiModelProperty(name = "page_title", value = "seo标题", required = false)
    private String pageTitle;
    /**
     * seo关键字
     */
    @ApiModelProperty(name = "meta_keywords", value = "seo关键字", required = false)
    private String metaKeywords;
    /**
     * seo描述
     */
    @ApiModelProperty(name = "meta_description", value = "seo描述", required = false)
    private String metaDescription;

    /** 谁承担运费0：买家承担，1：卖家承担 */
    @ApiModelProperty(name = "goods_transfee_charge", value = "谁承担运费0：买家承担，1：卖家承担")
    private Integer goodsTransfeeCharge;

    @Override
    public Integer getGoodsTransfeeCharge() {
        return goodsTransfeeCharge;
    }

    @Override
    public void setGoodsTransfeeCharge(Integer goodsTransfeeCharge) {
        this.goodsTransfeeCharge = goodsTransfeeCharge;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getGoodsOff() {
        return goodsOff;
    }

    public void setGoodsOff(Integer goodsOff) {
        this.goodsOff = goodsOff;
    }

    public List<GoodsParamsGroupVO> getParamList() {
        return paramList;
    }

    public void setParamList(List<GoodsParamsGroupVO> paramList) {
        this.paramList = paramList;
    }

    public List<GoodsGalleryDO> getGalleryList() {
        return galleryList;
    }

    public void setGalleryList(List<GoodsGalleryDO> galleryList) {
        this.galleryList = galleryList;
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
}
