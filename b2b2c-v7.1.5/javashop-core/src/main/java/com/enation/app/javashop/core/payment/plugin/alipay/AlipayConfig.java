package com.enation.app.javashop.core.payment.plugin.alipay;

/**
 * @author fk
 * @version v2.0
 * @Description: 支付宝配置相关
 * @date 2018/4/12 10:25
 * @since v7.0.0
 */
public class AlipayConfig {


    /**
     * 签名方式
     */
    public static String signType = "RSA2";

    /**
     * 字符编码格式
     */
    public static String charset = "utf-8";


    /**
     * 支付宝网关
     */
    public static String gatewayUrl = "https://openapi.alipay.com/gateway.do";


}

