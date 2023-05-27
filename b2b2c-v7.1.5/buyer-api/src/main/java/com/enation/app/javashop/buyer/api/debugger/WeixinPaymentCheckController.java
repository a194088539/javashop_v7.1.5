package com.enation.app.javashop.buyer.api.debugger;

import com.enation.app.javashop.core.payment.model.enums.ClientType;
import com.enation.app.javashop.core.payment.model.enums.PayMode;
import com.enation.app.javashop.core.payment.model.enums.TradeType;
import com.enation.app.javashop.core.payment.model.vo.Form;
import com.enation.app.javashop.core.payment.model.vo.PayBill;
import com.enation.app.javashop.core.payment.service.PaymentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-04-15
 */
@RestController
@RequestMapping("/debugger/payment")
@ConditionalOnProperty(value = "javashop.debugger", havingValue = "true")
public class WeixinPaymentCheckController {

    @Autowired
    private PaymentManager paymentManager;


    @GetMapping(value = "/weixin/pc/qr/iframe")
    public String qrPayFrame(  ) {
        StringBuffer html = new StringBuffer();

        html.append("<iframe id=\"iframe-qrcode\" width=\"200px\" height=\"200px\" scrolling=\"no\" src=\"payment/weixin/pc/qr/image\"></iframe>");

        return html.toString();
    }

    @GetMapping(value = "/weixin/pc/qr/image")
    public String qrPay(  ) {

        PayBill payBill = createBill();
        payBill.setClientType(ClientType.PC);
        payBill.setPayMode(PayMode.qr.name());

        StringBuffer html = new StringBuffer();
        Form form = paymentManager.pay(payBill);
        html.append("<form action='"+ form.getGatewayUrl() +"' method='POST' >");


        if (form.getFormItems() != null) {
            form.getFormItems().forEach(formItem -> {

                html.append("<input type='hidden' style='width:1000px' name='" + formItem.getItemName() + "' value='" + formItem.getItemValue() + "' />");

            });
        }

        html.append("<input type='submit' value='pay'/>");

        html.append("</form>");
        return html.toString();
    }




    private PayBill createBill() {

        PayBill payBill = new PayBill();
        payBill.setTradeType(TradeType.debugger);
        payBill.setOrderPrice(0.01);
        payBill.setPluginId("weixinPayPlugin");
        payBill.setSn("123456");

        return payBill;
    }

}
