package com.enation.app.javashop.core.payment.plugin.unionpay;

import java.text.SimpleDateFormat;
import java.util.*;

import com.enation.app.javashop.core.payment.model.enums.ClientType;
import com.enation.app.javashop.core.payment.model.enums.TradeType;
import com.enation.app.javashop.core.payment.model.enums.UnionpayConfigItem;
import com.enation.app.javashop.core.payment.model.vo.*;
import com.enation.app.javashop.core.payment.plugin.unionpay.sdk.AcpService;
import com.enation.app.javashop.core.payment.plugin.unionpay.sdk.LogUtil;
import com.enation.app.javashop.core.payment.plugin.unionpay.sdk.SDKConfig;
import com.enation.app.javashop.core.payment.service.PaymentPluginManager;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.ThreadContextHolder;
import com.enation.app.javashop.framework.util.CurrencyUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


/**
 * @author fk
 * @version v2.0
 * @Description: 中国银联支付插件
 * @date 2018/4/12 10:25
 * @since v7.0.0
 */
@Component
public class UnionpayPlugin extends UnionpayPluginConfig implements PaymentPluginManager {

    public static String version = "5.0.0";

    public static String encoding = "UTF-8";

    @Autowired
    private Cache cache;

    @Override
    public String getPluginId() {

        return "unionpayPlugin";
    }

    @Override
    public String getPluginName() {
        return "中国银联";
    }

    @Override
    public List<ClientConfig> definitionClientConfig() {

        List<ClientConfig> resultList = new ArrayList<>();

        ClientConfig config = new ClientConfig();

        List<PayConfigItem> configList = new ArrayList<>();
        for (UnionpayConfigItem value : UnionpayConfigItem.values()) {
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

        super.setConfig(bill);

        /**
         * 组装请求报文
         */
        Map<String, String> data = new HashMap<String, String>(16);
        // 版本号
        data.put("version", version);
        // 字符集编码 默认"UTF-8"
        data.put("encoding", "UTF-8");
        // 签名方法 01 RSA
        data.put("signMethod", "01");
        // 交易类型 01-消费
        data.put("txnType", "01");
        // 交易子类型 01:自助消费 02:订购 03:分期付款
        data.put("txnSubType", "01");
        // 业务类型
        data.put("bizType", "000201");
        // 渠道类型，07-PC，08-手机
        data.put("channelType", "07");
        // 前台通知地址 ，控件接入方式无作用
        data.put("frontUrl", this.getReturnUrl(bill));
        // 后台通知地址
        data.put("backUrl", this.getCallBackUrl(bill.getTradeType(), bill.getClientType()));
        // 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
        data.put("accessType", "0");
        // 商户号码，请改成自己的商户号
        data.put("merId", UnionpayConfig.merId);
        // 商户订单号，8-40位数字字母
        String outTradeNo = bill.getBillSn();

        int length = outTradeNo.length();
        //保证不小于8位，且不大于40位
        outTradeNo = outTradeNo.substring((length - 40) < 0 ? 0 : length - 40, length);

        data.put("orderId", outTradeNo);
        // 订单发送时间，取系统时间
        data.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        // 交易金额，单位分
        Double txnAmt = CurrencyUtil.mul(bill.getOrderPrice(), 100);

        data.put("txnAmt", "" + txnAmt.intValue());
        // 交易币种
        data.put("currencyCode", "156");

        //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        Map<String, String> submitFromData = AcpService.sign(data, encoding);

        // 交易请求url 从配置文件读取
        String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();
        //组织返回数据
        return this.getFormData(requestFrontUrl, submitFromData);
    }

    @Override
    public void onReturn(TradeType tradeType) {

        HttpServletRequest request = ThreadContextHolder.getHttpRequest();
        //商户订单号
        String orderSn = request.getParameter("orderId");
        try {
            //应答码
            String respCode = request.getParameter("respCode");
            //应答信息
            String respMsg = request.getParameter("respMsg");
            //流水号
            String queryId = request.getParameter("queryId");
            //系统追踪号
            String tradeno = request.getParameter("traceNo");
            //交易金额
            String settleAmt = request.getParameter("settleAmt");

            double payPrice = StringUtil.toDouble(settleAmt, 0d);
            // 传回来的是分，转为元
            payPrice = CurrencyUtil.mul(payPrice, 0.01);

            //交易成功
            if ("00".equals(respCode)) {
                if (JavashopUnionpayUtil.validaeData()) {

                    this.paySuccess(orderSn, queryId, tradeType, payPrice);

                } else {
                    throw new RuntimeException("验证失败");
                }
            } else {
                throw new RuntimeException("验证失败，错误信息:" + respMsg);
            }

        } catch (Exception e) {
            throw new RuntimeException("验证失败");
        }

    }

    @Override
    public String onCallback(TradeType tradeType, ClientType clientType) {
        HttpServletRequest request = ThreadContextHolder.getHttpRequest();
        //商户订单号
        String orderSn = request.getParameter("orderId");
        try {
            //应答码
            String respCode = request.getParameter("respCode");
            //应答信息
            String respMsg = request.getParameter("respMsg");
            //流水号
            String queryId = request.getParameter("queryId");
            //系统追踪号
            String tradeno = request.getParameter("traceNo");
            //交易金额
            String settleAmt = request.getParameter("settleAmt");

            double payPrice = StringUtil.toDouble(settleAmt, 0d);
            // 传回来的是分，转为元
            payPrice = CurrencyUtil.mul(payPrice, 0.01);

            //交易成功
            if ("00".equals(respCode)) {
                if (JavashopUnionpayUtil.validaeData()) {

                    this.paySuccess(orderSn, queryId, tradeType, payPrice);
                    return orderSn;
                } else {
                    throw new RuntimeException("验证失败");
                }
            } else {
                throw new RuntimeException("验证失败，错误信息:" + respMsg);
            }

        } catch (Exception e) {
            throw new RuntimeException("验证失败");
        }

    }


    @Override
    public String onQuery(PayBill bill) {
        return null;
    }

    @Override
    public boolean onTradeRefund(RefundBill bill) {

        Map<String, String> config = bill.getConfigMap();

        /**
         * 组装请求报文
         */
        Map<String, String> data = new HashMap<String, String>(16);
        // 版本号
        data.put("version", "5.0.0");
        // 字符集编码 默认"UTF-8"
        data.put("encoding", "UTF-8");
        // 签名方法 01 RSA
        data.put("signMethod", "01");
        // 交易类型 04-退货
        data.put("txnType", "04");
        // 交易子类型 01:自助消费 02:订购 03:分期付款
        data.put("txnSubType", "00");
        // 业务类型
        data.put("bizType", "000201");
        // 渠道类型，07-PC，08-手机
        data.put("channelType", "07");
        /**
         * 因中国银联5.0退款接口只有在退款成功的情况下才会回调，如果退款失败则无法回调，所以不通过异步回调获取退款状态
         * 通过查询退款状态交易查询接口获取退款状态
         */
        // 后台通知地址
        data.put("backUrl", "http://www.specialurl.com");
        // 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
        data.put("accessType", "0");
        // 商户号码，请改成自己的商户号
        data.put("merId", UnionpayConfig.merId);
        // 商户订单号，8-40位数字字母
        // 因为 银联要求的订单号不能有-号，在javashop中的子订单是有这个符号的，所以改用orderid
        String refundSn = bill.getRefundSn();

        data.put("orderId", refundSn);
        // 订单发送时间，取系统时间
        String txnTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        cache.put("{refundtime}_" + refundSn, txnTime);
        data.put("txnTime", txnTime);
        // 交易金额，单位分
        Double txnAmt = CurrencyUtil.mul(bill.getRefundPrice(), 100);

        data.put("txnAmt", "" + txnAmt.intValue());
        // 交易币种
        data.put("currencyCode", "156");
        // ****原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取
        data.put("origQryId", bill.getReturnTradeNo());
        // 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        Map<String, String> reqData = AcpService.sign(data, encoding);

        // 交易请求url 从配置文件读取
        String url = SDKConfig.getConfig().getBackRequestUrl();
        // 这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
        Map<String, String> rspData = AcpService.post(reqData, url, encoding);

        // 应答码规范参考open.unionpay.com帮助中心 下载 产品接口规范 《平台接入接口规范-第5部分-附录》
        if (!rspData.isEmpty()) {
            if (AcpService.validate(rspData, "UTF-8")) {
                LogUtil.writeLog("验证签名成功");
                String respCode = rspData.get("respCode");
                if ("00".equals(respCode)) {
                    // 交易已受理，等待接收后台通知更新订单状态,也可以主动发起 查询交易确定交易状态。
                    return true;
                } else // 后续需发起交易状态查询交易确定交易状态
                {
                    return "03".equals(respCode) || "04".equals(respCode) || "05".equals(respCode);
                }
            } else {
                LogUtil.writeErrorLog("验证签名失败");
            }
        } else {
            // 未返回正确的http状态
            LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
        }

        return false;

    }

    @Override
    public String queryRefundStatus(RefundBill bill) {
        Map<String, String> config = bill.getConfigMap();

        /**
         * 组装请求报文
         */
        Map<String, String> data = new HashMap<String, String>(16);
        // 版本号
        data.put("version", "5.0.0");
        // 字符集编码 默认"UTF-8"
        data.put("encoding", "UTF-8");
        // 签名方法 01 RSA
        data.put("signMethod", "01");
        // 交易类型 00-默认货
        data.put("txnType", "00");
        // 交易子类型 01:自助消费 02:订购 03:分期付款
        data.put("txnSubType", "00");
        // 业务类型
        data.put("bizType", "000201");
        // 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
        data.put("accessType", "0");
        // 商户号码，请改成自己的商户号
        data.put("merId", UnionpayConfig.merId);
        // 商户订单号，8-40位数字字母
        //因为 银联要求的订单号不能有-号，在javashop中的子订单是有这个符号的，所以改用orderid
        String refundSn = bill.getRefundSn();
        data.put("orderId", refundSn);
        //****订单发送时间，被查询的交易的订单发送时间
        data.put("txnTime", (String) cache.get("refundtime_" + refundSn));

        //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        Map<String, String> reqData = AcpService.sign(data, encoding);

        // 交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.singleQueryUrl
        String url = SDKConfig.getConfig().getSingleQueryUrl();

        //这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
        Map<String, String> rspData = AcpService.post(reqData, url, encoding);

        //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
        if (!rspData.isEmpty()) {
            if (AcpService.validate(rspData, encoding)) {
                LogUtil.writeLog("验证签名成功");
                //如果查询交易成功
                if ("00".equals(rspData.get("respCode"))) {
                    //处理被查询交易的应答码逻辑
                    String origRespCode = rspData.get("origRespCode");
                    if ("00".equals(origRespCode)) {
                        cache.remove("{refundtime}_" + refundSn);
                        return "";
                    } else if ("03".equals(origRespCode) ||
                            "04".equals(origRespCode) ||
                            "05".equals(origRespCode)) {
                        //需再次发起交易状态查询交易
                    } else {
                        //其他应答码为失败请排查原因
                        return "";
                    }
                } else {//查询交易本身失败，或者未查到原交易，检查查询交易报文要素
                    return "";
                }
            } else {
                LogUtil.writeErrorLog("验证签名失败");
            }
        } else {
            //未返回正确的http状态
            LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
        }
        return "";
    }

    @Override
    public Integer getIsRetrace() {
        return 0;
    }
}
