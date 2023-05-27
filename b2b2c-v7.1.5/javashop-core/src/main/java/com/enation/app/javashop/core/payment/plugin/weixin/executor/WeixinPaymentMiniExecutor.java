package com.enation.app.javashop.core.payment.plugin.weixin.executor;

import com.enation.app.javashop.core.payment.model.vo.Form;
import com.enation.app.javashop.core.payment.model.vo.PayBill;
import com.enation.app.javashop.core.payment.plugin.weixin.WeixinPuginConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fk
 * @version v2.0
 * @Description: 微信wap端
 * @date 2018/4/1810:12
 * @since v7.0.0
 */
@Service
public class WeixinPaymentMiniExecutor extends WeixinPuginConfig {

    @Autowired
    private WeixinPaymentJsapiExecutor weixinPaymentJsapiExecutor;

    /**
     * 支付
     *
     * @param bill
     * @return
     */
    public Form onPay(PayBill bill) {
        return weixinPaymentJsapiExecutor.onPay(bill);
    }


}
