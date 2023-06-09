package com.enation.app.javashop.core.payment.plugin.weixin.signaturer;

/**
 * 微信签名参数枚举
 *
 * @author liushuai
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2019/2/21 下午3:53
 */

public enum WechatTypeEnmu {

    /**
     * PC
     */
    PC("PC"),
    /**
     * WAP
     */
    WAP("WAP"),
    /**
     * 原生
     */
    REACT("原生"),
    /**
     * NAAPP
     */
    NATIVE("NAAPP"),
    /**
     * 小程序
     */
    MINI("小程序");
    private String text;

    WechatTypeEnmu(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
