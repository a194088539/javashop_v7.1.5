package com.enation.app.javashop.core.aftersale.model.enums;

/**
 * 售后退款单状态枚举类
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-21
 */
public enum RefundStatusEnum {
    /**
     * 待退款
     */
    APPLY("待退款"),

    /**
     * 退款中
     */
    REFUNDING("退款中"),

    /**
     * 退款失败
     */
    REFUNDFAIL("退款失败"),

    /**
     * 完成
     */
    COMPLETED("完成");

    private String description;

    RefundStatusEnum(String des) {
        this.description = des;
    }

    public String description() {
    return this.description;
}

    public String value() {
        return this.name();
    }
}
