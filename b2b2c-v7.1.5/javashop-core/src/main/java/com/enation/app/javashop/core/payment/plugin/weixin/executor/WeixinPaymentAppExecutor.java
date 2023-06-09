package com.enation.app.javashop.core.payment.plugin.weixin.executor;

import com.enation.app.javashop.core.payment.model.vo.Form;
import com.enation.app.javashop.core.payment.model.vo.PayBill;
import com.enation.app.javashop.core.payment.plugin.weixin.WeixinPuginConfig;
import com.enation.app.javashop.core.payment.plugin.weixin.WeixinUtil;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.stereotype.Service;

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
public class WeixinPaymentAppExecutor extends WeixinPuginConfig {


    /**
     * 支付
     *
     * @param bill
     * @return
     */
    public Form onPay(PayBill bill) {

        try {
            Map<String, String> params = new TreeMap<>();
            params.put("spbill_create_ip", getIpAddress());
            params.put("trade_type", "APP");

            Map<String, String> map = super.createUnifiedOrder(bill,params);
            String resultCode = map.get("result_code");
            Form form = new Form();
            if (SUCCESS.equals(resultCode)) {
                String prepayId = map.get("prepay_id");
                Map<String, String> result = new TreeMap();
                result.put("appid", map.get("appid"));
                result.put("partnerid", map.get("mchid"));
                result.put("prepayid", prepayId);
                result.put("package", "Sign=WXPay");
                result.put("noncestr", StringUtil.getRandStr(10));
                result.put("timestamp", DateUtil.getDateline() + "");
                result.put("sign", WeixinUtil.createSign(result, map.get("key")));
                form.setGatewayUrl(WeixinUtil.mapToXml(result));
                return form;
            }
        } catch (Exception e) {
            this.logger.error("返回参数转换失败", e);
        }
        return null;
    }


}
