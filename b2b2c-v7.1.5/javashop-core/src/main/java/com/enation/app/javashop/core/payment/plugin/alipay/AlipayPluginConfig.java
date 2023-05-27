package com.enation.app.javashop.core.payment.plugin.alipay;

import com.alipay.api.AlipayClient;
import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.client.system.SettingClient;
import com.enation.app.javashop.core.payment.model.enums.ClientType;
import com.enation.app.javashop.core.payment.model.vo.PayBill;
import com.enation.app.javashop.core.payment.plugin.alipay.executor.JavaShopPayClient;
import com.enation.app.javashop.core.payment.service.AbstractPaymentPlugin;
import com.enation.app.javashop.core.system.model.vo.SiteSetting;
import com.enation.app.javashop.framework.logs.Debugger;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;


/**
 * @author fk
 * @version v2.0
 * @Description: 支付宝配置相关
 * @date 2018/4/12 10:25
 * @since v7.0.0
 */
public class AlipayPluginConfig extends AbstractPaymentPlugin {

    @Autowired
    private Debugger debugger;

    @Autowired
    private SettingClient settingClient;

    @Override
    protected String getPluginId() {

        return "alipayDirectPlugin";
    }


    /**
     * 构建alipay client
     * @param clientType
     * @return
     */
    protected AlipayClient buildClient(ClientType clientType) {

        Map<String, String> config = this.getConfig(clientType );

        return buildClient(config);
    }


    protected AlipayClient buildClient(Map<String, String> config ){

        String appId = config.get("app_id");
        String merchantPrivateKey = config.get("merchant_private_key");
        String alipayPublicKey = config.get("alipay_public_key");

        debugger.log("使用如下参数构建client:");
        debugger.log(config.toString());

        AlipayClient alipayClient = new JavaShopPayClient(AlipayConfig.gatewayUrl, appId, merchantPrivateKey, "json", AlipayConfig.charset, alipayPublicKey, AlipayConfig.signType);

        return alipayClient;
    }

    /**
     * @param bill
     * @return
     */
    protected Map<String, String> createParam(PayBill bill) {

        // 商户网站订单
        String outTradeNo = bill.getBillSn();
        Double payMoney = bill.getOrderPrice();

        // 订单名称
        String subject = getSiteName() + "订单";

        Map<String, String> sParaTemp = new HashMap<String, String>(16);
        sParaTemp.put("out_trade_no", outTradeNo);
        sParaTemp.put("product_code", "FAST_INSTANT_TRADE_PAY");
        sParaTemp.put("total_amount", payMoney + "");
        sParaTemp.put("subject", subject);
        sParaTemp.put("body", "");

        return sParaTemp;
    }

    /**
     * 获取站点名称
     * @return
     */
    protected String getSiteName() {
        String siteSettingJson = this.settingClient.get(SettingGroup.SITE);

        SiteSetting siteSetting = JsonUtil.jsonToObject(siteSettingJson, SiteSetting.class);
        return siteSetting.getSiteName();
    }

}
