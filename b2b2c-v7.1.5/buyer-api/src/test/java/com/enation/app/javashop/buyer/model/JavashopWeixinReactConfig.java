package com.enation.app.javashop.buyer.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author fk
 * @version v2.0
 * @Description: 微信配置参数
 * @date 2018/4/2410:25
 * @since v7.0.0
 */
@Configuration
public class JavashopWeixinReactConfig {

    @Value("${javashop.weixin.react.mchid:''}")
    private String mchid;

    @Value("${javashop.weixin.react.appid:''}")
    private String appid;

    @Value("${javashop.weixin.react.key:''}")
    private String key;

    @Value("${javashop.weixin.react.app-secret:''}")
    private String appSecret;

    public String getMchid() {
        return mchid;
    }

    public String getAppid() {
        return appid;
    }

    public String getKey() {
        return key;
    }

    public String getAppSecret() {
        return appSecret;
    }
}
