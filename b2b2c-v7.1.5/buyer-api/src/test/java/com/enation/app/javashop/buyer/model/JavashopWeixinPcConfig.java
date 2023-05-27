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
public class JavashopWeixinPcConfig {

    @Value("${javashop.weixin.pc.mchid:''}")
    private String mchid;

    @Value("${javashop.weixin.pc.appid:''}")
    private String appid;

    @Value("${javashop.weixin.pc.key:''}")
    private String key;

    @Value("${javashop.weixin.pc.app-secret:''}")
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
