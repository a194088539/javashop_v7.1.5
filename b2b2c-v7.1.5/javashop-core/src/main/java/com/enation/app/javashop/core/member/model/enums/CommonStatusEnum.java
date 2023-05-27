package com.enation.app.javashop.core.member.model.enums;

/**
 * 是否状态枚举类
 * 适用于是否匿名、是否已读等
 * @author duanmingyu
 * @version V1.0
 * @since V7.1.5
 * 2019-09-16
 */
public enum CommonStatusEnum {

    /**
     * 是
     */
    YES("是"),

    /**
     * 否
     */
    NO("否");

    private String description;

    CommonStatusEnum(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
    public String value(){
        return this.name();
    }
}
