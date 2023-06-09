package com.enation.app.javashop.core.payment.service.impl;

import com.enation.app.javashop.core.client.trade.OrderClient;
import com.enation.app.javashop.core.payment.model.enums.TradeType;
import com.enation.app.javashop.core.payment.service.PaymentCallbackDevice;
import com.enation.app.javashop.core.trade.cart.model.dos.OrderPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单支付回调器
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-04-16
 */

@Service
public class OrderCallbackDevice implements PaymentCallbackDevice {


    @Autowired
    private OrderClient orderClient;

    /**
     * 调用订单client完成对订单支付状态的修改
     * @param outTradeNo
     * @param returnTradeNo
     * @param payPrice
     */
    @Override
    public void paySuccess(String outTradeNo, String returnTradeNo, double payPrice) {
        orderClient.payOrder(outTradeNo,payPrice, returnTradeNo,OrderPermission.client.name());
    }

    /**
     * 定义交易类型
     * @return
     */
    @Override
    public TradeType tradeType() {

        return TradeType.order;
    }

}
