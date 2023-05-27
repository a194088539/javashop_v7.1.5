package com.enation.app.javashop.buyer.api.debugger;

import com.enation.app.javashop.core.payment.model.enums.TradeType;
import com.enation.app.javashop.core.payment.service.PaymentCallbackDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 调试支付回调器
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-04-16
 */

@Service
@ConditionalOnProperty(value = "javashop.debugger", havingValue = "true")
public class DebuggerCallbackDevice implements PaymentCallbackDevice {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 调试支付回调器
     * @param outTradeNo
     * @param returnTradeNo
     * @param payPrice
     */
    @Override
    public void paySuccess(String outTradeNo, String returnTradeNo, double payPrice) {
        logger.debug("支付回调：outTradeNo：【"+outTradeNo+"】returnTradeNo：【"+returnTradeNo+"】payPrice：【"+payPrice+"】");

    }

    /**
     * 定义交易类型
     * @return
     */
    @Override
    public TradeType tradeType() {

        return TradeType.debugger;
    }

}
