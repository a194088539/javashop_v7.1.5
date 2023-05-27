package com.enation.app.javashop.core.shop.model.enums;

/**
 * @author zjp
 * @version v7.0
 * @Description 店铺站内消息枚举类
 * @ClassName ShopNoticeTypeEnum
 * @since v7.0 下午2:21 2018/7/10
 */
public enum ShopNoticeTypeEnum {
    /**
     * 订单
     */
    ORDER("订单"),
    /**
     * 商品
     */
    GOODS("商品"),
    /**
     * 售后
     */
    AFTERSALE("售后"),
    /**
     * 其他
     */
    OTHER("其他");

    private String description;

    ShopNoticeTypeEnum(String des) {
        this.description = des;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }
}
