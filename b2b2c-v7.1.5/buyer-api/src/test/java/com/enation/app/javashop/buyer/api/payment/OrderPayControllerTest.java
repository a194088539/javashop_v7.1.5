package com.enation.app.javashop.buyer.api.payment;

import com.enation.app.javashop.buyer.model.JavashopAlipayConfig;
import com.enation.app.javashop.buyer.model.JavashopWeixinNativeConfig;
import com.enation.app.javashop.buyer.model.JavashopWeixinPcConfig;
import com.enation.app.javashop.buyer.model.JavashopWeixinReactConfig;
import com.enation.app.javashop.core.payment.model.dos.PaymentMethodDO;
import com.enation.app.javashop.core.payment.model.vo.Form;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v2.0
 * @Description: 支付单元测试
 * @date 2018/4/24 10:38
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class OrderPayControllerTest extends BaseTest {

    @Autowired
    private JavashopAlipayConfig alipayConfig;
    @Autowired
    private JavashopWeixinNativeConfig weixinNativeConfig;
    @Autowired
    private JavashopWeixinPcConfig weixinPcConfig;
    @Autowired
    private JavashopWeixinReactConfig weixinReactConfig;

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Before
    public void insertTestData() {

        //订单中加入一条数据
        String sql = "INSERT into es_order(sn,trade_sn,order_price) values (\"1234\",\"1234\",0.01)";
        this.daoSupport.execute(sql);
        //更改支付宝的配置参数
        String a = alipayConfig.getAlipayPublicKey();
        String b = alipayConfig.getAppId();
        String c = alipayConfig.getMerchantPrivateKey();
        this.daoSupport.execute("delete from es_payment_method where plugin_id = 'alipayDirectPlugin' or plugin_id = 'weixinPayPlugin'");
        String alipayConfig = "{\"key\":\"pc_config&wap_config&app_native_config&app_react_config\",\"name\":\"是否开启\",\"config_list\":[{\"name\":\"alipay_public_key\",\"text\":\"支付宝公钥\",\"value\":\"" + a + "\"},{\"name\":\"app_id\",\"text\":\"应用ID\",\"value\":\"" + b + "\"},{\"name\":\"merchant_private_key\",\"text\":\"商户私钥\",\"value\":\"" + c + "\"}],\"is_open\":1}";
        PaymentMethodDO payment = new PaymentMethodDO();
        payment.setMethodName("支付宝");
        payment.setPluginId("alipayDirectPlugin");
        payment.setPcConfig(alipayConfig);
        payment.setWapConfig(alipayConfig);
        payment.setAppNativeConfig(alipayConfig);
        payment.setAppReactConfig(alipayConfig);
        this.daoSupport.insert(payment);

        //添加微信的配置参数
        PaymentMethodDO weixinPayment = new PaymentMethodDO();
        weixinPayment.setMethodName("微信");
        weixinPayment.setPluginId("weixinPayPlugin");
        weixinPayment.setPcConfig(getConfig(weixinPcConfig.getMchid(), weixinPcConfig.getAppid(), weixinPcConfig.getKey(), weixinPcConfig.getAppSecret(), "123"));
        weixinPayment.setWapConfig(getConfig(weixinPcConfig.getMchid(), weixinPcConfig.getAppid(), weixinPcConfig.getKey(), weixinPcConfig.getAppSecret(), "123"));
        weixinPayment.setAppNativeConfig(getConfig(weixinNativeConfig.getMchid(), weixinNativeConfig.getAppid(), weixinNativeConfig.getKey(), weixinNativeConfig.getAppSecret(), "123"));
        weixinPayment.setAppReactConfig(getConfig(weixinReactConfig.getMchid(), weixinReactConfig.getAppid(), weixinReactConfig.getKey(), weixinReactConfig.getAppSecret(), "123"));

        this.daoSupport.insert(weixinPayment);

    }

    /**
     * 查询支付方式
     *
     * @throws Exception
     */
    @Test
    public void testQuery() throws Exception {

        // 状态200
        mockMvc.perform(get("/order/pay/PC")
                .header("Authorization", buyer1))
                .andExpect(status().isOk());
    }

    /**
     * 支付宝去支付
     *
     * @throws Exception
     */
    @Test
    public void testAlipayPay() throws Exception {

        // pc二维码模式状态200
        mockMvc.perform(get("/order/pay/order/1234")
                .param("payment_plugin_id", "alipayDirectPlugin")
                .param("pay_mode", "qr")
                .param("client_type", "PC")
                .header("Authorization", buyer1))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();


        // pc电脑支付
        mockMvc.perform(get("/order/pay/order/1234")
                .param("payment_plugin_id", "alipayDirectPlugin")
                .param("pay_mode", "normal")
                .param("client_type", "PC")
                .header("Authorization", buyer1))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // wap支付
        mockMvc.perform(get("/order/pay/order/1234")
                .param("payment_plugin_id", "alipayDirectPlugin")
                .param("pay_mode", "normal")
                .param("client_type", "WAP")
                .header("Authorization", buyer1))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //app原生支付
        String res = mockMvc.perform(get("/order/pay/order/1234")
                .param("payment_plugin_id", "alipayDirectPlugin")
                .param("pay_mode", "normal")
                .param("client_type", "NATIVE")
                .header("Authorization", buyer1))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Form form = JsonUtil.jsonToObject(res, Form.class);
        Assert.assertThat(form.getGatewayUrl(), containsString("alipay_sdk=alipay-sdk-java-dynamicVersionNo"));

        //appRN支付
        res = mockMvc.perform(get("/order/pay/order/1234")
                .param("payment_plugin_id", "alipayDirectPlugin")
                .param("pay_mode", "normal")
                .param("client_type", "REACT")
                .header("Authorization", buyer1))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        form = JsonUtil.jsonToObject(res, Form.class);
        Assert.assertThat(form.getGatewayUrl(), containsString("alipay_sdk=alipay-sdk-java-dynamicVersionNo"));

    }

    /**
     * 微信去支付
     *
     * @throws Exception
     */
    @Test
    public void testWeixinPay() throws Exception {

        //pc二维码支付
        mockMvc.perform(get("/order/pay/order/1234")
                .param("payment_plugin_id", "weixinPayPlugin")
                .param("pay_mode", "qr")
                .param("client_type", "PC")
                .header("Authorization", buyer1))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        // wap支付
        mockMvc.perform(get("/order/pay/order/1234")
                .param("payment_plugin_id", "weixinPayPlugin")
                .param("pay_mode", "normal")
                .param("client_type", "WAP")
                .header("Authorization", buyer1)
                .header("User-agent", "Mozilla/5.0"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();


        //app原生支付
        mockMvc.perform(get("/order/pay/order/1234")
                .param("payment_plugin_id", "weixinPayPlugin")
                .param("pay_mode", "normal")
                .param("client_type", "NATIVE")
                .header("Authorization", buyer1))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //appRN支付
        mockMvc.perform(get("/order/pay/order/1234")
                .param("payment_plugin_id", "weixinPayPlugin")
                .param("pay_mode", "normal")
                .param("client_type", "REACT")
                .header("Authorization", buyer1))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

    }

    /**
     * 生成相应的配置文件
     *
     * @param mchid
     * @param appid
     * @param key
     * @param appSecret
     * @param p12Path
     * @return
     */
    private String getConfig(String mchid, String appid, String key, String appSecret, String p12Path) {

        String res = "{\"key\":\"pc_config&wap_config\",\"name\":\"是否开启PC和WAP\",\"config_list\":[{\"name\":\"mchid\",\"text\":\"商户号MCHID\",\"value\":\"" + mchid + "\"},{\"name\":\"appid\",\"text\":\"APPID\",\"value\":\"" + appid + "\"},{\"name\":\"key\",\"text\":\"商户私钥\",\"value\":\"" + key + "\"},{\"name\":\"app_secret\",\"text\":\"商户私钥\",\"value\":\"" + appSecret + "\"},{\"name\":\"p12_path\",\"text\":\"证书路径\",\"value\":\"" + p12Path + "\"}],\"is_open\":1}";
        return res;
    }

}
