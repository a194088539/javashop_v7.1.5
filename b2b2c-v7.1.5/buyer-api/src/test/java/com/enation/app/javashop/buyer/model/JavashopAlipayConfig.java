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
public class JavashopAlipayConfig {

    @Value("${javashop.alipay.merchant-private-key:''}")
    private String merchantPrivateKey;

    @Value("${javashop.alipay.app-id:''}")
    private String appId;

    @Value("${javashop.alipay.alipay-public-key:''}")
    private String alipayPublicKey;

    public String getMerchantPrivateKey() {
        return merchantPrivateKey;
    }

    public String getAppId() {
        return appId;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }
}
