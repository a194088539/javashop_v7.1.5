package com.enation.app.javashop.core.payment.plugin.weixin.executor;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.member.model.vo.Auth2Token;
import com.enation.app.javashop.core.payment.model.enums.TradeType;
import com.enation.app.javashop.core.payment.model.vo.Form;
import com.enation.app.javashop.core.payment.model.vo.FormItem;
import com.enation.app.javashop.core.payment.model.vo.PayBill;
import com.enation.app.javashop.core.payment.plugin.weixin.WeixinPuginConfig;
import com.enation.app.javashop.core.payment.plugin.weixin.WeixinUtil;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.ThreadContextHolder;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author fk
 * @version v2.0
 * @Description: 微信wap端
 * @date 2018/4/1810:12
 * @since v7.0.0
 */
@Service
public class WeixinPaymentJsapiExecutor extends WeixinPuginConfig {

    @Autowired
    private Cache cache;

    /**
     * 支付
     *
     * @param bill
     * @return
     */
    public Form onPay(PayBill bill) {
        Map<String, String> params = new TreeMap<>();
        params.put("spbill_create_ip", "0:0:0:0:0:0:0:1");
        params.put("trade_type", "JSAPI");

        Buyer buyer = UserContext.getBuyer();
        Auth2Token token = (Auth2Token) cache.get(CachePrefix.CONNECT_LOGIN.getPrefix() + buyer.getUuid());
        //获取openid
        String openid = token.getOpneId();
        params.put("openid", openid);

        String result = "";

        Form form = new Form();

        try {

            Map<String,String> map = super.createUnifiedOrder(bill,params);

            // 返回结果
            String returnCode = map.get("return_code");
            if(logger.isDebugEnabled()){
                logger.debug("微信支付返回结果："+returnCode);
            }
            if (SUCCESS.equals(returnCode)) {
                // 业务码
                String resultCode = map.get("result_code");

                if (SUCCESS.equals(resultCode)) {
                    // 预支付订单id
                    String prepayId = map.get("prepay_id");
                    if(logger.isDebugEnabled()){
                        logger.debug("微信支付id："+prepayId);
                    }
                    Map<String, String> weixinparams = new TreeMap();
                    weixinparams.put("appId", map.get("appid"));
                    weixinparams.put("nonceStr", StringUtil.getRandStr(10));
                    weixinparams.put("timeStamp", String.valueOf(DateUtil.getDateline()));
                    weixinparams.put("package", "prepay_id=" + prepayId);
                    weixinparams.put("signType", "MD5");
                    String sign = WeixinUtil.createSign(weixinparams, map.get("key"));
                    weixinparams.put("paySign", sign);

                    List<FormItem> items = this.getItems(weixinparams);
                    form.setFormItems(items);
                    String outTradeNo = bill.getBillSn();
                    if(logger.isDebugEnabled()){
                        logger.debug("微信返回map："+weixinparams);
                    }
                    result = this.getPayScript(prepayId, map.get("appid"), map.get("key"), outTradeNo, bill.getTradeType());
                    if(logger.isDebugEnabled()){
                        logger.debug("微信返回结果："+result);
                    }
                } else {

                    String errCode = map.get("err_code");
                    String errCodeDes = map.get("err_code_des");
                    result = "<script>alert('支付意外错误，请联系技术人员:"
                            + errCode + "【" + errCodeDes + "】')</script>";
                    logger.error(result);
                }
            } else {
                result = "<script>alert('支付意外错误，请联系技术人员:" + returnCode + "')</script>";
                if ("FAIL".equals(returnCode)) {
                    // 错误信息
                    String returnMsg = map.get("return_msg");
                    this.logger.error("微信端返回错误" + returnCode + "["
                            + returnMsg + "]");
                }
            }
        } catch (Exception e) {
            this.logger.error("微信生成支付二维码错误", e);
            return null;
        }

        form.setGatewayUrl(result);

        return form;

    }

    private List<FormItem> getItems(Map<String, String> weixinparams) {

        List<FormItem> items = new ArrayList<>();
        for (String key : weixinparams.keySet()) {

            FormItem item = new FormItem();
            item.setItemName(key);
            item.setItemValue(weixinparams.get(key));

            items.add(item);
        }

        return items;
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
        if(logger.isDebugEnabled()){
            logger.info("支付成功页面跳转："+"http://" + serverName + portstr + contextPath + "/" + tradeType + "_" + outTradeNo + "_payment-wap-result.html");
        }
        return "http://" + serverName + portstr + contextPath + "/" + tradeType + "_" + outTradeNo + "_payment-wap-result.html";
    }

    /**
     * 生成支付的脚本
     *
     * @param prepayId
     * @param appid
     * @param weixinkey
     * @param outTradeNo
     * @param tradeType
     * @return
     */
    private String getPayScript(String prepayId, String appid, String weixinkey, String outTradeNo, TradeType tradeType) {

        Map<String, String> params = new TreeMap();
        params.put("appId", appid);
        params.put("nonceStr", StringUtil.getRandStr(10));
        params.put("timestamp", String.valueOf(DateUtil.getDateline()));
        params.put("package", "prepay_id=" + prepayId);
        params.put("signType", "MD5");
        String sign = WeixinUtil.createSign(params, weixinkey);
        params.put("paySign", sign);


        StringBuffer payStr = new StringBuffer();
        payStr.append("WeixinJSBridge.invoke('getBrandWCPayRequest',{");

        int i = 0;
        for (String key : params.keySet()) {
            String value = params.get(key);

            if (i != 0) {
                payStr.append(",");
            }

            payStr.append("'" + key + "':'" + value + "'");
            i++;

        }

        payStr.append("}");

        payStr.append(",function(res){  if( 'get_brand_wcpay_request:ok'==res.err_msg ) { "
                + "alert('支付成功'); "
                + "location.href='" + getPayWapSuccessUrl(tradeType.name(), outTradeNo) + "?operation=success';"
                + "}else{ alert('支付失败'); "
                + "location.href='" + getPayWapSuccessUrl(tradeType.name(), outTradeNo) + "?operation=fail';"
                + "} "
                + "}");

        payStr.append(");");

        return payStr.toString();
    }
}
