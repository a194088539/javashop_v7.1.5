package com.enation.app.javashop.core.aftersale.model.enums;

/**
 * 售后服务操作枚举类
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-12-09
 */
public enum  ServiceOperateTypeEnum {

    /** 商家审核操作 */
    SELLER_AUDIT("商家审核操作"),

    /** 商家入库操作 */
    STOCK_IN("审核入库操作"),

    /** 商家退款操作 */
    SELLER_REFUND("商家退款操作"),

    /** 平台退款操作 */
    ADMIN_REFUND("平台退款操作"),

    /** 买家填写物流信息操作 */
    FILL_LOGISTICS_INFO("买家填写物流信息操作"),

    /** 关闭操作 */
    CLOSE("关闭操作"),

    /** 创建新订单操作 */
    CREATE_NEW_ORDER("创建新订单操作");

    private String description;

    ServiceOperateTypeEnum(String des) {
        this.description = des;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }
}
