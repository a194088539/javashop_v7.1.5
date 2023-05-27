package com.enation.app.javashop.core.shop.model.dto;

/**
 * @author fk
 * @version v2.0
 * @Description: 店铺评分传输对象
 * @date 2018/10/13 9:44
 * @since v7.0.0
 */
public class ShopScoreDTO {

    /**
     * 描述评分
     */
    private Double shopDescriptionCredit;
    /**
     * 服务评分
     */
    private Double shopServiceCredit;
    /**
     * 物流评分
     */
    private Double shopDeliveryCredit;
    /**
     * 店铺信用
     */
    private Double shopCredit;
    /**
     * 店铺id
     */
    private Integer shopId;

    public ShopScoreDTO(Double shopDescriptionCredit, Double shopServiceCredit, Double shopDeliveryCredit, Double shopCredit, Integer shopId) {
        this.shopDescriptionCredit = shopDescriptionCredit;
        this.shopServiceCredit = shopServiceCredit;
        this.shopDeliveryCredit = shopDeliveryCredit;
        this.shopCredit = shopCredit;
        this.shopId = shopId;
    }

    public ShopScoreDTO() {
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

    public Double getShopCredit() {
        return shopCredit;
    }

    public void setShopCredit(Double shopCredit) {
        this.shopCredit = shopCredit;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }
}
