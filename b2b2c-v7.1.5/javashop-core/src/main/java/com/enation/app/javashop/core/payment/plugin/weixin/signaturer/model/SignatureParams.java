package com.enation.app.javashop.core.payment.plugin.weixin.signaturer.model;

import java.io.Serializable;

/**
 * 签名参数
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2019-02-21 下午2:48
 */
public class SignatureParams  implements Serializable {


    /**
     * appId
     */
    private String appId;


    /**
     * ticket
     */
    private WechatJsapiTicket wechatJsapiTicket;

    /**
     * token
     */
    private WechatCgiAccessToken wechatCgiAccessToken;


    public WechatJsapiTicket getWechatJsapiTicket() {
        return wechatJsapiTicket;
    }

    public void setWechatJsapiTicket(WechatJsapiTicket wechatJsapiTicket) {
        this.wechatJsapiTicket = wechatJsapiTicket;
    }

    public WechatCgiAccessToken getWechatCgiAccessToken() {
        return wechatCgiAccessToken;
    }

    public void setWechatCgiAccessToken(WechatCgiAccessToken wechatCgiAccessToken) {
        this.wechatCgiAccessToken = wechatCgiAccessToken;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
