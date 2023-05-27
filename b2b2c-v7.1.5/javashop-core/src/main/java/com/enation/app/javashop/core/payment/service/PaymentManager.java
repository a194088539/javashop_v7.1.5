package com.enation.app.javashop.core.payment.service;

import com.enation.app.javashop.core.payment.model.dto.PayParam;
import com.enation.app.javashop.core.payment.model.enums.ClientType;
import com.enation.app.javashop.core.payment.model.enums.TradeType;
import com.enation.app.javashop.core.payment.model.vo.Form;
import com.enation.app.javashop.core.payment.model.vo.PayBill;

/**
 * 全局统一支付接口
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-04-10
 */
public interface PaymentManager {

    /**
     * 创建支付账单并支付
     * @param payBill
     * @return
     */
    Form pay(PayBill payBill);



    /**
     * 同步回调方法
     *
     * @param tradeType
     * @param paymentPluginId
     */
    void payReturn(TradeType tradeType, String paymentPluginId);


    /**
     * 支付异步回调
     *
     * @param tradeType
     * @param paymentPluginId
     * @param clientType
     * @return
     */
    String payCallback(TradeType tradeType, String paymentPluginId, ClientType clientType);

    /**
     * 查询支付结果并更新订单状态
     *
     * @param param
     * @return
     */
    String queryResult(PayParam param);


}
