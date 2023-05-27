package com.enation.app.javashop.core.aftersale.model.dto;

import com.enation.app.javashop.core.promotion.fulldiscount.model.dos.FullDiscountGiftDO;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 申请售后订单以及商品信息实体
 * 主要用于用户申请售后服务时，页面相关信息的获取
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-17
 */
public class AfterSaleOrderDTO implements Serializable {

    private static final long serialVersionUID = 4649144804521392291L;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号",name = "order_sn")
    private String orderSn;
    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id",name = "good_id")
    private Integer goodId;
    /**
     * 产品id
     */
    @ApiModelProperty(value = "产品id" ,name = "sku_id")
    private Integer skuId;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称",name = "goods_name")
    private String goodsName;
    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片",name = "goods_img")
    private String goodsImg;
    /**
     * 商品单价
     */
    @ApiModelProperty(value = "商品单价",name = "goods_price")
    private Double goodsPrice;
    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量",name = "buy_num")
    private Integer buyNum;
    /**
     * 收货地址省份ID
     */
    @ApiModelProperty(name = "province_id", value = "收货地址省份ID")
    private Integer provinceId;
    /**
     * 收货地址城市ID
     */
    @ApiModelProperty(name = "city_id", value = "收货地址城市ID")
    private Integer cityId;
    /**
     * 收货地址县(区)ID
     */
    @ApiModelProperty(name = "county_id", value = "收货地址县(区)ID")
    private Integer countyId;
    /**
     * 收货地址乡(镇)ID
     */
    @ApiModelProperty(name = "town_id", value = "收货地址乡(镇)ID")
    private Integer townId;
    /**
     * 收货地址省份名称
     */
    @ApiModelProperty(name = "province", value = "收货地址省份名称")
    private String province;
    /**
     * 收货地址城市名称
     */
    @ApiModelProperty(name = "city", value = "收货地址城市名称")
    private String city;
    /**
     * 收货地址县(区)名称
     */
    @ApiModelProperty(name = "county", value = "收货地址县(区)名称")
    private String county;
    /**
     * 收货地址城镇名称
     */
    @ApiModelProperty(name = "town", value = "收货地址城镇名称")
    private String town;
    /**
     * 收货地址详细
     */
    @ApiModelProperty(name = "ship_addr", value = "收货地址详细")
    private String shipAddr;
    /**
     * 收货人姓名
     */
    @ApiModelProperty(name = "ship_name", value = "收货人姓名")
    private String shipName;
    /**
     * 收货人手机号
     */
    @ApiModelProperty(name = "ship_mobile", value = "收货人手机号")
    private String shipMobile;
    /**
     * 订单赠品数据集合
     */
    @ApiModelProperty(name = "gift_list", value = "订单赠品数据集合")
    private List<FullDiscountGiftDO> giftList;
    /**
     * 是否支持原路退回
     */
    @ApiModelProperty(name = "is_retrace", value = "是否支持原路退回")
    private Boolean isRetrace;
    /**
     * 是否有发票
     */
    @ApiModelProperty(name = "is_receipt", value = "是否有发票")
    private Boolean isReceipt;
    /**
     * 是否允许申请退货
     */
    @ApiModelProperty(name = "allow_return_goods", value = "是否允许申请退货")
    private Boolean allowReturnGoods;
    /**
     * 商家名称
     */
    @ApiModelProperty(name = "seller_name", value = "商家名称")
    private String sellerName;

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Integer getGoodId() {
        return goodId;
    }

    public void setGoodId(Integer goodId) {
        this.goodId = goodId;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Integer getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getCountyId() {
        return countyId;
    }

    public void setCountyId(Integer countyId) {
        this.countyId = countyId;
    }

    public Integer getTownId() {
        return townId;
    }

    public void setTownId(Integer townId) {
        this.townId = townId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getShipAddr() {
        return shipAddr;
    }

    public void setShipAddr(String shipAddr) {
        this.shipAddr = shipAddr;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipMobile() {
        return shipMobile;
    }

    public void setShipMobile(String shipMobile) {
        this.shipMobile = shipMobile;
    }

    public List<FullDiscountGiftDO> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<FullDiscountGiftDO> giftList) {
        this.giftList = giftList;
    }

    public Boolean getIsRetrace() {
        return isRetrace;
    }

    public void setIsRetrace(Boolean isRetrace) {
        this.isRetrace = isRetrace;
    }

    public Boolean getIsReceipt() {
        return isReceipt;
    }

    public void setIsReceipt(Boolean isReceipt) {
        this.isReceipt = isReceipt;
    }

    public Boolean getAllowReturnGoods() {
        return allowReturnGoods;
    }

    public void setAllowReturnGoods(Boolean allowReturnGoods) {
        this.allowReturnGoods = allowReturnGoods;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    @Override
    public String toString() {
        return "AfterSaleOrderDTO{" +
                "orderSn='" + orderSn + '\'' +
                ", goodId=" + goodId +
                ", skuId=" + skuId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsImg='" + goodsImg + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", buyNum=" + buyNum +
                ", provinceId=" + provinceId +
                ", cityId=" + cityId +
                ", countyId=" + countyId +
                ", townId=" + townId +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", town='" + town + '\'' +
                ", shipAddr='" + shipAddr + '\'' +
                ", shipName='" + shipName + '\'' +
                ", shipMobile='" + shipMobile + '\'' +
                ", giftList=" + giftList +
                ", isRetrace=" + isRetrace +
                ", isReceipt=" + isReceipt +
                ", allowReturnGoods=" + allowReturnGoods +
                ", sellerName='" + sellerName + '\'' +
                '}';
    }
}
