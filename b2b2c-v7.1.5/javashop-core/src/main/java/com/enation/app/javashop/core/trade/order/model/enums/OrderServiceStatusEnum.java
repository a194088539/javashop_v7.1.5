package com.enation.app.javashop.core.trade.order.model.enums;

/**
 * 订单申请售后服务的状态
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-24
 */
public enum OrderServiceStatusEnum {

    /** 新订单 */
    NEW("新订单"),

    /** 已确认 */
    CONFIRM("已确认"),

    /** 已付款 */
    PAID_OFF("已付款"),

    /** 未申请 */
    NOT_APPLY("未申请"),

    /** 申请取消 */
    APPLY("申请取消"),

    /** 审核通过 */
    PASS("审核通过"),

    /** 已取消（为了取消订单的售后状态与订单状态对应，当前状态意思不是已完成，而是已取消） */
    COMPLETE("已取消"),

    /** 已失效或不允许申请售后 */
    EXPIRED("已失效不允许申请售后");


    private String description;

    OrderServiceStatusEnum(String description){
        this.description=description;
    }

    public String description(){
        return this.description;
    }

    public String value(){
        return this.name();
    }
}
