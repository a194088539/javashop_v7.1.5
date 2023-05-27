package com.enation.app.javashop.core.client.trade;


/**
 * @author fk
 * @version v2.0
 * @Description: 微信零钱接口client
 * @date 2018/8/13 16:01
 * @since v7.0.0
 */
public interface WechatSmallchangeClient {

    /**
     * 自动发送零钱红包
     *
     * @param openId
     * @param price  申请金额
     * @param ip     ip
     * @param sn     流水号
     */
    boolean autoSend(String openId, Double price, String ip, String sn);
}
