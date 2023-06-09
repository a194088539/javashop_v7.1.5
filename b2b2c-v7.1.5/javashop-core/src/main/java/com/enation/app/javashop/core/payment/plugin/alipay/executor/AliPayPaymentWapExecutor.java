package com.enation.app.javashop.core.payment.plugin.alipay.executor;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.enation.app.javashop.core.payment.model.vo.Form;
import com.enation.app.javashop.core.payment.model.vo.PayBill;
import com.enation.app.javashop.core.payment.plugin.alipay.AlipayPluginConfig;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author fk
 * @version v1.0
 * @Description: 支付宝wap端
 * @date 2018/4/1714:55
 * @since v7.0.0
 */
@Service
public class AliPayPaymentWapExecutor extends AlipayPluginConfig {


    /**
     * 支付
     *
     * @param bill
     * @return
     */
    public Form onPay(PayBill bill) {

        try {
            AlipayClient alipayClient =  super.buildClient(bill.getClientType());

            //设置请求参数
            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
            alipayRequest.setNotifyUrl(this.getCallBackUrl(bill.getTradeType(), bill.getClientType()));
            alipayRequest.setReturnUrl(this.getReturnUrl(bill));
            Map<String, String> sParaTemp =  createParam(bill);
            ObjectMapper json = new ObjectMapper();
            String bizContent =  json.writeValueAsString( sParaTemp );

            //填充业务参数
            alipayRequest.setBizContent(bizContent );
            return JsonUtil.jsonToObject(alipayClient.pageExecute(alipayRequest).getBody(), Form.class);

        } catch ( Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }

}
