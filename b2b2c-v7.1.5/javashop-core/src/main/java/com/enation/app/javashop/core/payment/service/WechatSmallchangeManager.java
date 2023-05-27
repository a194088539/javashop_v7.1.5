package com.enation.app.javashop.core.payment.service;

/**
 * 微信零钱接口
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2019-06-11 下午4:24
 */
public interface WechatSmallchangeManager {


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
