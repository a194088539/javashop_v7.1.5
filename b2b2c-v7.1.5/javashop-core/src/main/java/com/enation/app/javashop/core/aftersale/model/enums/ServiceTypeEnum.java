package com.enation.app.javashop.core.aftersale.model.enums;

/**
 * 售后服务类型枚举类
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-15
 */
public enum ServiceTypeEnum {

    /** 退货 */
    RETURN_GOODS("退货"),

    /** 换货 */
    CHANGE_GOODS("换货"),

    /** 补发商品 */
    SUPPLY_AGAIN_GOODS("补发商品"),

    /** 取消订单 */
    ORDER_CANCEL("取消订单");

    private String description;

    ServiceTypeEnum(String des) {
        this.description = des;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }
}
