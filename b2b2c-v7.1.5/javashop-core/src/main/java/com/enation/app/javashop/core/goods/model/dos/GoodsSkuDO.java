package com.enation.app.javashop.core.goods.model.dos;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import java.io.Serializable;


/**
 * 商品sku实体
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-03-21 11:48:40
 */
@Table(name = "es_goods_sku")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GoodsSkuDO implements Serializable {

    private static final long serialVersionUID = 5102510694003249L;

    /**
     * 主键
     */
    @Id(name = "sku_id")
    @ApiModelProperty(hidden = true)
    private Integer skuId;
    /**
     * 商品id
     */
    @Column(name = "goods_id")
    @ApiModelProperty(name = "goods_id", value = "商品id", hidden = true)
    private Integer goodsId;
    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    @ApiModelProperty(name = "goods_name", value = "商品名称", hidden = true)
    private String goodsName;
    /**
     * 商品编号
     */
    @Column(name = "sn")
    @ApiModelProperty(name = "sn", value = "商品编号", required = false)
    @Length(max = 30,message = "商品规格编号太长，不能超过30个字符")
    private String sn;
    /**
     * 库存
     */
    @Column(name = "quantity")
    @ApiModelProperty(name = "quantity", value = "库存", required = false)
    @Max(value = 99999999, message = "库存不能超过99999999")
    private Integer quantity;
    /**
     * 可用库存
     */
    @Column(name = "enable_quantity")
    @ApiModelProperty(name = "enable_quantity", value = "可用库存")
    private Integer enableQuantity;
    /**
     * 商品价格
     */
    @Column(name = "price")
    @ApiModelProperty(name = "price", value = "商品价格", required = false)
    @Max(value = 99999999, message = "价格不能超过99999999")
    private Double price;
    /**
     * 规格信息json
     */
    @Column(name = "specs")
    @ApiModelProperty(name = "specs", value = "规格信息json", hidden = true)
    @JsonIgnore
    private String specs;
    /**
     * 成本价格
     */
    @Column(name = "cost")
    @ApiModelProperty(name = "cost", value = "成本价格", required = true)
    @Max(value = 99999999, message = "成本价格不能超过99999999")
    private Double cost;
    /**
     * 重量
     */
    @Column(name = "weight")
    @ApiModelProperty(name = "weight", value = "重量", required = true)
    @Max(value = 99999999, message = "重量不能超过99999999")
    private Double weight;
    /**
     * 卖家id
     */
    @Column(name = "seller_id")
    @ApiModelProperty(name = "seller_id", value = "卖家id", hidden = true)
    private Integer sellerId;
    /**
     * 卖家名称
     */
    @Column(name = "seller_name")
    @ApiModelProperty(name = "seller_name", value = "卖家名称", hidden = true)
    private String sellerName;
    /**
     * 分类id
     */
    @Column(name = "category_id")
    @ApiModelProperty(name = "category_id", value = "分类id", hidden = true)
    private Integer categoryId;
    /**
     * 缩略图
     */
    @Column(name = "thumbnail")
    @ApiModelProperty(name = "thumbnail", value = "缩略图", hidden = true)
    private String thumbnail;


    @Column(name = "hash_code")
    @ApiModelProperty(name = "hash_code", value = "hash_code", hidden = true)
    private Integer hashCode;


    private Integer templateId;

    public GoodsSkuDO() {
    }

    @PrimaryKeyField
    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

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

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getEnableQuantity() {
        return enableQuantity;
    }

    public void setEnableQuantity(Integer enableQuantity) {
        this.enableQuantity = enableQuantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getHashCode() {
        return hashCode;
    }

    public void setHashCode(Integer hashCode) {
        this.hashCode = hashCode;
    }

    @Override
    public String toString() {
        return "GoodsSkuDO{" +
                "skuId=" + skuId +
                ", goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", sn='" + sn + '\'' +
                ", quantity=" + quantity +
                ", enableQuantity=" + enableQuantity +
                ", price=" + price +
                ", specs='" + specs + '\'' +
                ", cost=" + cost +
                ", weight=" + weight +
                ", sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                ", categoryId=" + categoryId +
                ", thumbnail='" + thumbnail + '\'' +
                ", hashCode=" + hashCode +
                ", templateId=" + templateId +
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
        GoodsSkuDO that = (GoodsSkuDO) o;

        return new EqualsBuilder()
                .append(getSkuId(), that.getSkuId())
                .append(getGoodsId(), that.getGoodsId())
                .append(getGoodsName(), that.getGoodsName())
                .append(getSn(), that.getSn())
                .append(getQuantity(), that.getQuantity())
                .append(getEnableQuantity(), that.getEnableQuantity())
                .append(getPrice(), that.getPrice())
                .append(getSpecs(), that.getSpecs())
                .append(getCost(), that.getCost())
                .append(getWeight(), that.getWeight())
                .append(getSellerId(), that.getSellerId())
                .append(getSellerName(), that.getSellerName())
                .append(getCategoryId(), that.getCategoryId())
                .append(getThumbnail(), that.getThumbnail())
                .append(hashCode, that.hashCode)
                .append(getTemplateId(), that.getTemplateId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getSkuId())
                .append(getGoodsId())
                .append(getGoodsName())
                .append(getSn())
                .append(getQuantity())
                .append(getEnableQuantity())
                .append(getPrice())
                .append(getSpecs())
                .append(getCost())
                .append(getWeight())
                .append(getSellerId())
                .append(getSellerName())
                .append(getCategoryId())
                .append(getThumbnail())
                .append(hashCode)
                .append(getTemplateId())
                .toHashCode();
    }

}