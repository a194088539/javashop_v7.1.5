package com.enation.app.javashop.core.aftersale.model.enums;

/**
 * 售后服务单创建渠道枚举类
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-12-11
 */
public enum CreateChannelEnum {

    /** 正常渠道创建 */
    NORMAL("正常渠道创建"),

    /** 拼团失败自动创建 */
    PINTUAN("拼团失败自动创建");

    private String description;

    CreateChannelEnum(String des) {
        this.description = des;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }

}
