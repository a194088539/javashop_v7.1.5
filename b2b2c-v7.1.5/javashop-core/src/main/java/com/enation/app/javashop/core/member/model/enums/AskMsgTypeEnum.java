package com.enation.app.javashop.core.member.model.enums;

/**
 * 咨询消息类型
 *
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-09-16
 */
public enum AskMsgTypeEnum {

    /**
     * 提问消息
     */
    ASK("提问消息"),

    /**
     * 回复消息
     */
    REPLY("回复消息");

    private String description;

    AskMsgTypeEnum(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
    public String value(){
        return this.name();
    }
}
