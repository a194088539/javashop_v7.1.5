package com.enation.app.javashop.core.payment.plugin.weixin.executor;

import com.enation.app.javashop.core.payment.model.vo.Form;
import com.enation.app.javashop.core.payment.model.vo.PayBill;
import com.enation.app.javashop.core.payment.plugin.weixin.WeixinPuginConfig;
import com.enation.app.javashop.framework.context.ThreadContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author fk
 * @version v2.0
 * @Description: 微信wap端
 * @date 2018/4/1810:12
 * @since v7.0.0
 */
@Service
public class WeixinPaymentWapExecutor extends WeixinPuginConfig {

    /**
     * 支付
     *
     * @param bill
     * @return
     */
    public Form onPay(PayBill bill) {

        Map<String, String> params = new TreeMap<>();
        params.put("spbill_create_ip", getIpAddress());
        params.put("trade_type", "MWEB");

        try {

            Map<String, String> map = super.createUnifiedOrder(bill,params);
            Form form = new Form();
            // 返回结果
            String resultCode = map.get("result_code");
            if (SUCCESS.equals(resultCode)) {
                String codeUrl = map.get("mweb_url");
                String outTradeNo = bill.getBillSn();
                form.setGatewayUrl("<script> location.href='" + codeUrl + "&redirect_url=" + getPayWapSuccessUrl(bill.getTradeType().name(), outTradeNo) + "';</script>");
                return form;
            }
        } catch (Exception e) {
            this.logger.error("生成参数失败", e);

        }
        return null;

    }


    /**
     * 获取支付成功调取页面
     *
     * @param tradeType
     * @param outTradeNo
     * @return
     */
    private String getPayWapSuccessUrl(String tradeType, String outTradeNo) {

        HttpServletRequest request = ThreadContextHolder.getHttpRequest();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        String portstr = "";
        if (port != 80) {
            portstr = ":" + port;
        }
        String contextPath = request.getContextPath();

        return "http://" + serverName + portstr + contextPath + "/" + tradeType + "_" + outTradeNo + "_payment-wap-result.html";
    }
}
