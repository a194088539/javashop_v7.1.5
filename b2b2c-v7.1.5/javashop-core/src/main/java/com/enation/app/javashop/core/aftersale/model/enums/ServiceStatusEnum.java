package com.enation.app.javashop.core.aftersale.model.enums;

/**
 * 售后服务单状态枚举类
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-15
 */
public enum ServiceStatusEnum {

    /** 待审核 */
    APPLY("待审核"),

    /** 审核通过 */
    PASS("审核通过"),

    /** 审核未通过 */
    REFUSE("审核未通过"),

    /** 已退还商品 */
    FULL_COURIER("已退还商品"),

    /** 待人工处理 */
    WAIT_FOR_MANUAL("待人工处理"),

    /** 已入库 */
    STOCK_IN("已入库"),

    /** 退款中 */
    REFUNDING("退款中"),

    /** 退款失败 */
    REFUNDFAIL("退款失败"),

    /** 已完成 */
    COMPLETED("已完成"),

    /** 已关闭 */
    CLOSED("已关闭"),

    /** 异常状态 */
    ERROR_EXCEPTION("异常状态");

    private String description;

    ServiceStatusEnum(String des) {
        this.description = des;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }
}
