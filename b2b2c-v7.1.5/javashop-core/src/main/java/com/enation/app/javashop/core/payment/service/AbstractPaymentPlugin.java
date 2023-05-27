package com.enation.app.javashop.core.payment.service;

import com.alipay.api.AlipayApiException;
import com.enation.app.javashop.core.base.DomainHelper;
import com.enation.app.javashop.core.payment.model.enums.ClientType;
import com.enation.app.javashop.core.payment.model.enums.TradeType;
import com.enation.app.javashop.core.payment.model.vo.Form;
import com.enation.app.javashop.core.payment.model.vo.FormItem;
import com.enation.app.javashop.core.payment.model.vo.PayBill;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.database.DaoSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.*;

/**
 * 支付插件父类<br>
 * 具有读取配置的能力
 *
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月3日下午11:38:38
 */
public abstract class AbstractPaymentPlugin {

    protected final Log logger = LogFactory.getLog(getClass());
    /**
     * 测试环境 0  生产环境  1
     */
    protected int isTest = 0;


    public static final String SUCCESS = "SUCCESS";

    public static final String REFUND_ERROR_MESSAGE = "{REFUND_ERROR_MESSAGE}";

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private PaymentMethodManager paymentMethodManager;

    @Autowired
    private PaymentBillManager paymentBillManager;

    @Autowired
    private DomainHelper domainHelper;

    @Autowired
    private Cache cache;


    /**
     * 获取插件的配置方式
     *
     * @return
     */
    protected Map<String, String> getConfig(ClientType clientType) {
        return paymentMethodManager.getConfig(clientType.getDbColumn(), this.getPluginId());
    }


    /**
     * 获取插件id
     *
     * @return
     */
    protected abstract String getPluginId();


    /**
     * 获取同步通知url
     *
     * @param bill 交易
     * @return
     */
    protected String getReturnUrl(PayBill bill) {
        String tradeType = bill.getTradeType().name();
        String payMode = bill.getPayMode();
        return domainHelper.getCallback() + "/order/pay/return/" + tradeType + "/" + payMode + "/" + this.getPluginId();
    }

    /**
     * 获取异步通知url
     *
     * @param tradeType
     * @return
     */
    protected String getCallBackUrl(TradeType tradeType, ClientType clientType) {
        return domainHelper.getCallback() + "/order/pay/callback/" + tradeType + "/" + this.getPluginId() + "/" + clientType;
    }

    /**
     * 支付回调后执行方法
     *
     * @param billSn        支付账号单
     * @param returnTradeNo 第三方平台回传支付单号
     * @param tradeType
     * @param payPrice
     */
    protected void paySuccess(String billSn, String returnTradeNo, TradeType tradeType, double payPrice) {
        //调用账单接口完成相关交易及流程的状态变更
        this.paymentBillManager.paySuccess(billSn, returnTradeNo, tradeType, payPrice);
    }

    /**
     * 组织数据
     *
     * @param actionUrl 表单提交地址
     * @param paramMap
     * @return
     * @throws AlipayApiException
     */
    protected Form getFormData(String actionUrl, Map<String, String> paramMap) {
        if (paramMap != null && !paramMap.isEmpty()) {
            Form form = new Form();
            List<FormItem> formItems = new ArrayList<>();
            Set<String> keys = paramMap.keySet();
            Iterator var3 = keys.iterator();
            while (var3.hasNext()) {
                FormItem item = new FormItem();
                String key = (String) var3.next();
                String value = paramMap.get(key);
                item.setItemName(key);
                item.setItemValue(value);
                formItems.add(item);
            }
            form.setFormItems(formItems);
            form.setGatewayUrl(actionUrl);
            return form;
        }
        return null;
    }


}
