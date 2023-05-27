package com.enation.app.javashop.core.member.model.enums;

/**
 * @author zjp
 * @version v7.0
 * @Description 信任登录用户性别枚举类
 * @ClassName ConnectUserGenderEnum
 * @since v7.0 上午10:35 2018/6/6
 */
public enum ConnectUserGenderEnum {
    //QQ联合登录
    MALE("男"),
    //微博联合登录
    FEMALE("女"),
    //微信联合登录
    SECRECY("保密");

    private String description;

    ConnectUserGenderEnum(String des) {
        this.description = des;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }
}
