package com.enation.app.javashop.core.payment.plugin.chinapay;

import com.chinapay.secss.SecssUtil;
import com.enation.app.javashop.core.payment.model.enums.ChinapayConfigItem;
import com.enation.app.javashop.core.payment.model.enums.ClientType;
import com.enation.app.javashop.core.payment.model.enums.TradeType;
import com.enation.app.javashop.core.payment.model.vo.*;
import com.enation.app.javashop.core.payment.service.AbstractPaymentPlugin;
import com.enation.app.javashop.core.payment.service.PaymentPluginManager;
import com.enation.app.javashop.framework.context.ThreadContextHolder;
import com.enation.app.javashop.framework.util.CurrencyUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author fk
 * @version v1.0
 * @Description: 银联在线支付
 * @date 2018/6/13 16:54
 * @since v7.0.0
 */
@Component
@ComponentScan("com.chinapay.secss")
public class ChinapayPlugin extends AbstractPaymentPlugin implements PaymentPluginManager {


    @Override
    public String getPluginId() {

        return "chinapayPlugin";
    }

    @Override
    public String getPluginName() {

        return "银联在线支付";
    }

    @Override
    public List<ClientConfig> definitionClientConfig() {

        List<ClientConfig> resultList = new ArrayList<>();

        ClientConfig config = new ClientConfig();

        List<PayConfigItem> configList = new ArrayList<>();
        for (ChinapayConfigItem value : ChinapayConfigItem.values()) {
            PayConfigItem item = new PayConfigItem();
            item.setName(value.name());
            item.setText(value.getText());
            configList.add(item);
        }

        config.setKey(ClientType.PC.getDbColumn() + "&" + ClientType.WAP.getDbColumn() + "&" + ClientType.NATIVE.getDbColumn() + "&" + ClientType.REACT.getDbColumn());
        config.setConfigList(configList);
        config.setName("是否开启");

        resultList.add(config);

        return resultList;
    }

    @Override
    public Form pay(PayBill bill) {

        Map<String, String> config = this.getConfig(bill.getClientType());
        String merId = config.get(ChinapayConfigItem.mer_id.name());
        String merPath = config.get(ChinapayConfigItem.merchant_private_key.name());
        String payUrl;
        if (isTest == 1) {
            payUrl = "https://payment.chinapay.com/CTITS/service/rest/page/nref/000000000017/0/0/0/0/0";
        } else {
            payUrl = "https://newpayment-test.chinapay.com/CTITS/service/rest/page/nref/000000000017/0/0/0/0/0";
        }

        Map<String, String> param = new TreeMap<String, String>();
        SecssUtil secssUtil = getSecssUtil(merPath);
        // 签名
        param.put("Version", "20140728");
        param.put("MerId", merId);
        param.put("MerOrderNo", bill.getBillSn());
        String transDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        param.put("TranDate", transDate);
        String tranTime = new SimpleDateFormat("hhmmss").format(new Date());
        param.put("TranTime", tranTime);
        Double txnAmt = CurrencyUtil.mul(bill.getOrderPrice(), 100);
        param.put("OrderAmt", "" + txnAmt.intValue());
        param.put("BusiType", "0001");
        param.put("MerPageUrl", this.getReturnUrl(bill));
        param.put("MerBgUrl", this.getCallBackUrl(bill.getTradeType(), bill.getClientType()));
        param.put("RemoteAddr", "127.0.0.1");
        // 签名
        secssUtil.sign(param);
        param.put("Signature", secssUtil.getSign());

        System.out.println(String.format("sendMap = %s", param));

        String html = "<div style='margin:50px auto;width:500px'>正在跳转到银联在线支付平台，请稍等...</div>";
        //跳转到银联页面支付
        return this.getFormData(payUrl, param);
    }

    @Override
    public void onReturn(TradeType tradeType) {

        HttpServletRequest request = ThreadContextHolder.getHttpRequest();
        String queryId = request.getParameter("AcqSeqId");
        //商户订单号
        String merOrderNo = request.getParameter("MerOrderNo");
        //交易金额
        String settleAmt = request.getParameter("OrderAmt");
        //订单支付状态  0000 为支付成功状态，0001 为未支付
        String orderStatus = request.getParameter("OrderStatus");

        double payPrice = StringUtil.toDouble(settleAmt, 0d);
        // 传回来的是分，转为元
        payPrice = CurrencyUtil.mul(payPrice, 0.01);

        if ("0000".equals(orderStatus)) {
            this.paySuccess(merOrderNo, queryId, tradeType, payPrice);
        }

    }

    @Override
    public String onCallback(TradeType tradeType, ClientType clientType) {

        HttpServletRequest request = ThreadContextHolder.getHttpRequest();
        String queryId = request.getParameter("AcqSeqId");
        //商户订单号
        String merOrderNo = request.getParameter("MerOrderNo");
        //交易金额
        String settleAmt = request.getParameter("OrderAmt");
        //订单支付状态  0000 为支付成功状态，0001 为未支付
        String orderStatus = request.getParameter("OrderStatus");

        double payPrice = StringUtil.toDouble(settleAmt, 0d);
        // 传回来的是分，转为元
        payPrice = CurrencyUtil.mul(payPrice, 0.01);

        if ("0000".equals(orderStatus)) {
            this.paySuccess(merOrderNo, queryId, tradeType, payPrice);
        }
        return null;
    }

    @Override
    public String onQuery(PayBill bill) {
        return null;
    }

    @Override
    public boolean onTradeRefund(RefundBill bill) {
        return false;
    }

    @Override
    public String queryRefundStatus(RefundBill bill) {
        return null;
    }

    @Override
    public Integer getIsRetrace() {
        return 0;
    }


    /**
     * 加载安全秘钥 .
     *
     * @param merPath 配置文件路经
     * @return SecssUtil .
     */
    protected SecssUtil getSecssUtil(String merPath) {
        SecssUtil secssUtil = new SecssUtil();
        secssUtil.init(merPath + "/security.properties");
        return secssUtil;
    }

    /**
     * 生成自动提交表单
     *
     * @param actionUrl
     * @param paramMap
     * @return
     */
    private String generateAutoSubmitForm(String actionUrl, Map<String, String> paramMap) {

        StringBuilder html = new StringBuilder();
        html.append("<script language=\"javascript\">window.onload=function(){document.pay_form.submit();}</script>\n");
        html.append("<form id=\"pay_form\" name=\"pay_form\" action=\"").append(actionUrl).append("\" method=\"post\">\n");

        for (String key : paramMap.keySet()) {
            html.append("<input type=\"hidden\" name=\"" + key + "\" id=\"" + key + "\" value=\"" + paramMap.get(key) + "\">\n");
        }
        html.append("</form>\n");
        return html.toString();
    }
}
