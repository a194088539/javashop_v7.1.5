package com.enation.app.javashop.core.system.enums;

/**
 *
 * 删除状态枚举
 * 适用于任何功能的删除操作
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019年8月30日
 */
public enum DeleteStatusEnum {
    /**
     * 已删除
     */
    DELETED("已删除"),
    /**
     * 正常
     */
    NORMAL("正常");

    private String description;

    DeleteStatusEnum(String des) {
        this.description = des;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }
}
