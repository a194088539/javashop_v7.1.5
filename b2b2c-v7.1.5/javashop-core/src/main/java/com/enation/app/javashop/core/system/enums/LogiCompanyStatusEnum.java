package com.enation.app.javashop.core.system.enums;

/**
 *
 * 物流公司禁用状态枚举
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019年8月31日
 */
public enum LogiCompanyStatusEnum {
    /**
     * 开启
     */
    OPEN("开启"),
    /**
     * 禁用
     */
    CLOSE("禁用");

    private String description;

    LogiCompanyStatusEnum(String des) {
        this.description = des;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }
}
