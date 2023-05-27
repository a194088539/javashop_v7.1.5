package com.enation.app.javashop.core.member.model.enums;

/**
 * @author zjp
 * @version v7.0
 * @Description 信任登录类型枚举类
 * @ClassName ConnectUserGenderEnum
 * @since v7.0 上午10:35 2018/6/6
 */
public enum ConnectTypeEnum {
    //QQ联合登录
    QQ("QQ"),
    //微博联合登录
    WEIBO("微博联合登录"),
    //微信联合登录
    WECHAT("微信联合登录"),
    // 微信小程序联合登录
    WECHAT_MINI("微信小程序联合登录"),
    //微信联合登录openid
    WECHAT_OPENID("微信联合登录 openid"),
    //支付宝登录
    ALIPAY("支付宝登录");

    private String description;

    ConnectTypeEnum(String des) {
        this.description = des;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }



}
