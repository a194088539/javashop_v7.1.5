package com.enation.app.javashop.core.payment.service;

import com.enation.app.javashop.core.payment.model.enums.TradeType;

/**
 * 支付回调器
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-04-16
 */

public interface PaymentCallbackDevice {


    /**
     * 第三方平台支付成功后回调的方法
     * @param outTradeNo
     * @param returnTradeNo
     * @param payPrice
     */
    void paySuccess(String outTradeNo, String returnTradeNo, double payPrice) ;


    /**
     * 定义此回调器支持的交易类型
     * @return
     */
    TradeType tradeType();
}
