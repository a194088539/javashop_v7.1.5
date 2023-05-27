package com.enation.app.javashop.core.aftersale.model.dos;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 售后商品实体
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-15
 */
@Table(name = "es_as_goods")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AfterSaleGoodsDO implements Serializable {

    private static final long serialVersionUID = 2333537241953078549L;

    /**
     * 主键ID
     */
    @Id(name = "id")
    @ApiModelProperty(hidden=true)
    private Integer id;
    /**
     * 售后服务单号
     */
    @Column(name = "service_sn")
    @ApiModelProperty(name = "service_sn", value = "售后服务单号", required = false)
    private String serviceSn;
    /**
     * 商品ID
     */
    @Column(name = "goods_id")
    @ApiModelProperty(name = "goods_id", value = "商品ID", required = false)
    private Integer goodsId;
    /**
     * 商品SKUID
     */
    @Column(name = "sku_id")
    @ApiModelProperty(name = "sku_id", value = "商品SKUID", required = false)
    private Integer skuId;
    /**
     * 发货数量
     */
    @Column(name = "ship_num")
    @ApiModelProperty(name = "ship_num", value = "发货数量", required = false)
    private Integer shipNum;
    /**
     * 商品成交价
     */
    @Column(name = "price")
    @ApiModelProperty(name = "price", value = "商品成交价", required = false)
    private Double price;
    /**
     * 退还数量
     */
    @Column(name = "return_num")
    @ApiModelProperty(name = "return_num", value = "退还数量", required = false)
    private Integer returnNum;
    /**
     * 入库数量
     */
    @Column(name = "storage_num")
    @ApiModelProperty(name = "storage_num", value = "入库数量", required = false)
    private Integer storageNum;
    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    @ApiModelProperty(name = "goods_name", value = "商品名称", required = false)
    private String goodsName;
    /**
     * 商品编号
     */
    @Column(name = "goods_sn")
    @ApiModelProperty(name = "goods_sn", value = "商品编号", required = false)
    private String goodsSn;
    /**
     * 商品缩略图
     */
    @Column(name = "goods_image")
    @ApiModelProperty(name = "goods_image", value = "商品缩略图", required = false)
    private String goodsImage;
    /**
     * 商品规格信息
     */
    @Column(name = "spec_json")
    @ApiModelProperty(name = "spec_json", value = "商品规格信息", required = false)
    private String specJson;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceSn() {
        return serviceSn;
    }

    public void setServiceSn(String serviceSn) {
        this.serviceSn = serviceSn;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getShipNum() {
        return shipNum;
    }

    public void setShipNum(Integer shipNum) {
        this.shipNum = shipNum;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getReturnNum() {
        return returnNum;
    }

    public void setReturnNum(Integer returnNum) {
        this.returnNum = returnNum;
    }

    public Integer getStorageNum() {
        return storageNum;
    }

    public void setStorageNum(Integer storageNum) {
        this.storageNum = storageNum;
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

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public String getSpecJson() {
        return specJson;
    }

    public void setSpecJson(String specJson) {
        this.specJson = specJson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AfterSaleGoodsDO that = (AfterSaleGoodsDO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(serviceSn, that.serviceSn) &&
                Objects.equals(goodsId, that.goodsId) &&
                Objects.equals(skuId, that.skuId) &&
                Objects.equals(shipNum, that.shipNum) &&
                Objects.equals(price, that.price) &&
                Objects.equals(returnNum, that.returnNum) &&
                Objects.equals(storageNum, that.storageNum) &&
                Objects.equals(goodsName, that.goodsName) &&
                Objects.equals(goodsSn, that.goodsSn) &&
                Objects.equals(goodsImage, that.goodsImage) &&
                Objects.equals(specJson, that.specJson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceSn, goodsId, skuId, shipNum, price, returnNum, storageNum, goodsName, goodsSn, goodsImage, specJson);
    }

    @Override
    public String toString() {
        return "AfterSaleGoodsDO{" +
                "id=" + id +
                ", serviceSn='" + serviceSn + '\'' +
                ", goodsId=" + goodsId +
                ", skuId=" + skuId +
                ", shipNum=" + shipNum +
                ", price=" + price +
                ", returnNum=" + returnNum +
                ", storageNum=" + storageNum +
                ", goodsName='" + goodsName + '\'' +
                ", goodsSn='" + goodsSn + '\'' +
                ", goodsImage='" + goodsImage + '\'' +
                ", specJson='" + specJson + '\'' +
                '}';
    }
}
