package com.enation.app.javashop.core.support;

/**
 * @author ：liuyulei
 * @date ：Created in 2019/9/17 9:08
 * @description：流程类型
 * @version: v1.0
 * @since: v7.1.4
 */
public enum FlowTypeEnum {

    /**
     * 取消订单
     */
    ORDER_CANCEL("取消订单"),

    /**
     * 退货
     */
    RETURN_GOODS("退货"),
    /**
     * 换货
     */
    CHANGE_GOODS("换货"),
     /**
     * 补发商品
     */
     SUPPLY_AGAIN_GOODS("补发商品"),

     /**
     * 线上支付
     */
     ONLINE("线上支付"),

    /**
     * 货到付款
     */
     COD("货到付款"),

    /**
     * 拼团
     */
    PINTUAN("拼团");

    private String description;

    FlowTypeEnum(String des) {
        this.description = des;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }
}
