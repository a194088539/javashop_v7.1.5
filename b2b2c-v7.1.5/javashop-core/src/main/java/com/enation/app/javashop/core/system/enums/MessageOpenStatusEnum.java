package com.enation.app.javashop.core.system.enums;

/**
 * @author zjp
 * @version v7.0
 * @Description 消息模版开启状态枚举类
 * @ClassName MessageOpenStatusEnum
 * @since v7.0 下午4:44 2018/7/5
 */
public enum MessageOpenStatusEnum {
    //开启中
    OPEN("开启中"),
    //关闭中
    CLOSED("关闭中");

    private String description;

    MessageOpenStatusEnum(String des) {
        this.description = des;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }
}
