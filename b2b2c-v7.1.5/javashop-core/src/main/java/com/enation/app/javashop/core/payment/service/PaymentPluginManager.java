package com.enation.app.javashop.core.payment.service;

import com.enation.app.javashop.core.payment.model.enums.ClientType;
import com.enation.app.javashop.core.payment.model.enums.TradeType;
import com.enation.app.javashop.core.payment.model.vo.ClientConfig;
import com.enation.app.javashop.core.payment.model.vo.Form;
import com.enation.app.javashop.core.payment.model.vo.PayBill;
import com.enation.app.javashop.core.payment.model.vo.RefundBill;

import java.util.List;

/**
 * 支付插件
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-04-11 16:06:57
 */
public interface PaymentPluginManager {


    /**
     * 获取支付插件id
     *
     * @return
     */
    String getPluginId();

    /**
     * 支付名称
     *
     * @return
     */
    String getPluginName();

    /**
     * 自定义客户端配置文件
     *
     * @return
     */
    List<ClientConfig> definitionClientConfig();

    /**
     * 支付
     *
     * @param bill
     * @return
     */
    Form pay(PayBill bill);

    /**
     * 同步回调
     *
     * @param tradeType
     */
    void onReturn(TradeType tradeType);

    /**
     * 异步回调
     *
     * @param tradeType
     * @param clientType
     * @return
     */
    String onCallback(TradeType tradeType, ClientType clientType);

    /**
     * 主动查询支付结果
     *
     * @param bill
     * @return
     */
    String onQuery(PayBill bill);


    /**
     * 退款，原路退回
     *
     * @param bill
     * @return
     */
    boolean onTradeRefund(RefundBill bill);

    /**
     * 查询退款状态
     *
     * @param bill
     * @return
     */
    String queryRefundStatus(RefundBill bill);

    /**
     * 是否支持原路退回   0 不支持  1支持
     *
     * @return
     */
    Integer getIsRetrace();

}
