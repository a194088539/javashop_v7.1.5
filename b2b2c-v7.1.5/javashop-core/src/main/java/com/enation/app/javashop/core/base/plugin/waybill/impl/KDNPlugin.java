package com.enation.app.javashop.core.base.plugin.waybill.impl;

import com.enation.app.javashop.core.base.model.vo.ConfigItem;
import com.enation.app.javashop.core.base.plugin.waybill.WayBillPlugin;
import com.enation.app.javashop.core.base.plugin.waybill.vo.Commodity;
import com.enation.app.javashop.core.base.plugin.waybill.vo.Information;
import com.enation.app.javashop.core.base.plugin.waybill.vo.WayBillJson;
import com.enation.app.javashop.core.client.member.ShopClient;
import com.enation.app.javashop.core.client.system.LogiCompanyClient;
import com.enation.app.javashop.core.client.trade.OrderClient;
import com.enation.app.javashop.core.shop.model.dos.ShopLogisticsSetting;
import com.enation.app.javashop.core.shop.model.vo.ShopVO;
import com.enation.app.javashop.core.shop.service.ShopLogisticsCompanyManager;
import com.enation.app.javashop.core.system.SystemErrorCode;
import com.enation.app.javashop.core.system.model.dos.LogisticsCompanyDO;
import com.enation.app.javashop.core.system.service.WaybillManager;
import com.enation.app.javashop.core.trade.sdk.model.OrderDetailDTO;
import com.enation.app.javashop.core.trade.sdk.model.OrderSkuDTO;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.util.Base64;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 快递鸟电子面板插件
 *
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月14日 上午10:39:03
 */
@SuppressWarnings("unchecked")
@Component("kdnPlugin")
public class KDNPlugin implements WayBillPlugin {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private LogiCompanyClient logiCompanyClient;
    @Autowired
    private OrderClient orderclient;
    @Autowired
    private ShopClient shopClient;

    @Autowired
    private ShopLogisticsCompanyManager shopLogisticsCompanyManager;

    @Autowired
    private WaybillManager waybillManager;

    @Override
    public List<ConfigItem> definitionConfigItem() {
        List<ConfigItem> list = new ArrayList<>();
        ConfigItem sellerMchidItem = new ConfigItem();
        sellerMchidItem.setName("EBusinessID");
        sellerMchidItem.setText("电商ID");
        sellerMchidItem.setType("text");

        ConfigItem selleAppidItem = new ConfigItem();
        selleAppidItem.setName("AppKey");
        selleAppidItem.setText("密钥");
        selleAppidItem.setType("text");

        ConfigItem sellerKeyItem = new ConfigItem();
        sellerKeyItem.setName("ReqURL");
        sellerKeyItem.setText("请求url");
        sellerKeyItem.setType("text");

        list.add(sellerMchidItem);
        list.add(selleAppidItem);
        list.add(sellerKeyItem);
        return list;
    }

    @Override
    public String getPluginId() {
        return "kdnPlugin";
    }

    @Override
    public String createPrintData(String orderSn, Integer logisticsId) throws Exception {

        //获取订单信息
        OrderDetailDTO orderDetailDTO = orderclient.getModel(orderSn);
        //获取店铺信息
        ShopVO shopVO = shopClient.getShop(orderDetailDTO.getSellerId());


        //获取物流公司信息
        LogisticsCompanyDO logisticsCompanyDO = logiCompanyClient.getModel(logisticsId);

        String config = waybillManager.getWayBillByBean(this.getPluginId()).getConfig();

        List<Map<String, Object>> jsonObject = JsonUtil.toList(config);
        String eBusinessID = null;
        String appKey = null;
        String reqURL = null;

        for (Map<String, Object> map : jsonObject) {

            if (map.containsValue("EBusinessID")) {
                eBusinessID = map.get("value").toString();
            }

            if (map.containsValue("AppKey")) {
                appKey = map.get("value").toString();
            }

            if (map.containsValue("ReqURL")) {
                reqURL = map.get("value").toString();
            }
        }

        //支付方式的对接,获取订单的支付方式
        //邮费支付方式:1-现付，2-到付，3-月结，4-第三方支付
        Integer payType = 1;
        WayBillJson wayBillJson = new WayBillJson();
        ShopLogisticsSetting shopLogisticsSetting = shopLogisticsCompanyManager.query(logisticsCompanyDO.getId(), orderDetailDTO.getSellerId());


        if (shopLogisticsSetting != null && !StringUtil.isEmpty(shopLogisticsSetting.getConfig())) {
            if (!StringUtil.isEmpty(shopLogisticsSetting.getParams().getCustomerName())) {
                wayBillJson.setCustomerName(shopLogisticsSetting.getParams().getCustomerName());
            }

            if (!StringUtil.isEmpty(shopLogisticsSetting.getParams().getCustomerPwd())) {
                wayBillJson.setCustomerPwd(shopLogisticsSetting.getParams().getCustomerPwd());
            }

            if (!StringUtil.isEmpty(shopLogisticsSetting.getParams().getMonthCode())) {
                wayBillJson.setMonthCode(shopLogisticsSetting.getParams().getMonthCode());
            }


            if (!StringUtil.isEmpty(shopLogisticsSetting.getParams().getSendSite())) {
                wayBillJson.setSendSite(shopLogisticsSetting.getParams().getSendSite());
            }
            if (!StringUtil.isEmpty(shopLogisticsSetting.getParams().getSendStaff())) {
                wayBillJson.setSendStaff(shopLogisticsSetting.getParams().getSendStaff());
            }
        }


        //发送者赋值
        Information senders = new Information();
        senders.setName(shopVO.getMemberName());
        senders.setMobile(shopVO.getLinkPhone());
        senders.setProvinceName(shopVO.getShopProvince());
        senders.setCityName(shopVO.getShopCity());
        senders.setExpAreaName(shopVO.getShopCounty());
        senders.setAddress(shopVO.getShopAdd());

        //如果为空，则
        if(StringUtil.isEmpty(shopVO.getShopAdd())){
            throw new ServiceException(SystemErrorCode.E911.code(), "发货地址不能为空，请前往商家中心设置详细地址");
        }

        //接收者赋值
        Information receivers = new Information();
        receivers.setName(orderDetailDTO.getShipName());
        receivers.setMobile(orderDetailDTO.getShipMobile());
        receivers.setProvinceName(orderDetailDTO.getShipProvince());
        receivers.setCityName(orderDetailDTO.getShipCity());
        receivers.setExpAreaName(orderDetailDTO.getShipCounty());
        receivers.setAddress(orderDetailDTO.getShipAddr());

        List<OrderSkuDTO> list = orderDetailDTO.getOrderSkuList();
        List<Commodity> commoditys = new ArrayList<>();


        for (int i = 0; i < list.size(); i++) {
            Commodity commodity = new Commodity();
            commodity.setGoodsName(replaceString(list.get(i).getName()));
            commodity.setGoodsquantity(list.get(i).getNum());
            commodity.setGoodsWeight(list.get(i).getGoodsWeight());
            commodity.setGoodsPrice(list.get(i).getPurchasePrice());
            commoditys.add(commodity);
        }

        wayBillJson.setOrderCode(orderDetailDTO.getSn());
        wayBillJson.setShipperCode(logisticsCompanyDO.getKdcode());
        wayBillJson.setPayType(payType);
        wayBillJson.setExpType("1");

        wayBillJson.setCost(orderDetailDTO.getShippingPrice());
        wayBillJson.setOtherCost(1.0);
        wayBillJson.setWeight(orderDetailDTO.getWeight());
        wayBillJson.setQuantity(orderDetailDTO.getGoodsNum());
        wayBillJson.setVolume(0.0);
        wayBillJson.setRemark(orderDetailDTO.getRemark());
        wayBillJson.setRemark("小心轻放");
        wayBillJson.setIsReturnPrintTemplate("1");
        wayBillJson.setSender(senders);
        wayBillJson.setReceiver(receivers);
        wayBillJson.setCommodity(commoditys);


        String requestData = JsonUtil.objectToJson(wayBillJson);
        Map<String, String> params = new HashMap<>(16);

        params.put("RequestData", URLEncoder.encode(requestData, "UTF-8"));
        params.put("EBusinessID", eBusinessID);
        params.put("RequestType", "1007");

        String dataSign = this.encrypt(requestData, appKey, "UTF-8");
        params.put("DataSign", URLEncoder.encode(dataSign, "UTF-8"));
        params.put("DataType", "2");


        String result = this.sendPost(reqURL, params);
        logger.debug(result);
        if(StringUtil.isEmpty(result) || "error".equals(result)){
            throw new ServiceException(SystemErrorCode.E912.code(), "电子面单参数异常，请联系管理员！");
        }
        return result;

    }

    /**
     * 替换特殊字符
     * 电子面单接口不允许出现以下特殊字符：' " # & + < > % \
     * @param name
     * @return
     */
    private String replaceString(String name) {
        String allow = "*";
        String regEx="['\"#&+<>%\\\\]";
        Pattern p = Pattern.compile(regEx);
        //这里把想要替换的字符串传进来
        Matcher m = p.matcher(name);
        String newString = m.replaceAll(allow).trim();
        return newString;
    }

    @Override
    public String getPluginName() {
        return "快递鸟";
    }

    @Override
    public Integer getOpen() {
        return 0;
    }

    @SuppressWarnings("unused")
    private String encrypt(String content, String keyValue, String charset) throws Exception {
        if (keyValue != null) {
            return Base64.encode(StringUtil.md5(content + keyValue, charset).getBytes(charset));
        }
        return Base64.encode(StringUtil.md5(content, charset).getBytes(charset));
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url    发送请求的 URL
     * @param params 请求的参数集合
     * @return 远程资源的响应结果
     */
    @SuppressWarnings("unused")
    private String sendPost(String url, Map<String, String> params) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // POST方法
            conn.setRequestMethod("POST");
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            if (params != null) {
                StringBuilder param = new StringBuilder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (param.length() > 0) {
                        param.append("&");
                    }
                    param.append(entry.getKey());
                    param.append("=");
                    param.append(entry.getValue());
                }
                out.write(param.toString());
            }
            // flush输出流的缓冲
            out.flush();


            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }
}

