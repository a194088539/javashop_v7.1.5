package com.enation.app.javashop.core.system.service.impl;

import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.base.service.SettingManager;
import com.enation.app.javashop.core.system.SystemErrorCode;
import com.enation.app.javashop.core.system.enums.DisplayTypeEnum;
import com.enation.app.javashop.core.system.model.vo.AbstractUmengNotification;
import com.enation.app.javashop.core.system.model.vo.AndroidBroadcast;
import com.enation.app.javashop.core.system.model.vo.AppPushSetting;
import com.enation.app.javashop.core.system.model.vo.IOSBroadcast;
import com.enation.app.javashop.core.system.service.PushManager;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息推送业务类
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-05-24 17:09:04
 */
@Service
public class PushManagerImpl implements PushManager {

    @Autowired
    private SettingManager settingManager;

    /**
     * 用户代理
     */
    private String userAgent = "Mozilla/5.0";

    /**
     * 网络Client
     */
    private HttpClient client = HttpClientBuilder.create().build();

    /**
     * BaseUrl
     */
    private final String host = "http://msg.umeng.com";

    /**
     * 推送消息的url
     */
    private final String postPath = "/api/add";


    @Override
    public String pushGoodsForAndroid(String title, Integer goodsId) {
        //获取推送设置
        String appPushSettingJson = settingManager.get(SettingGroup.PUSH);
        AppPushSetting appPushSetting = JsonUtil.jsonToObject(appPushSettingJson,AppPushSetting.class);

        if (StringUtil.isEmpty(appPushSetting.getAndroidPushKey()) || StringUtil.isEmpty(appPushSetting.getAndroidPushSecret())) {
            throw new ServiceException(SystemErrorCode.E908.code(), "推送参数为空");
        }
        try {
            AndroidBroadcast broadcast = new AndroidBroadcast(appPushSetting.getAndroidPushKey(), appPushSetting.getAndroidPushSecret());
            broadcast.setProductionMode();
            //必填参数 但是暂时不需要
            broadcast.setTitle("必填参数 但是暂时不需要");
            broadcast.setText(title);
            broadcast.setTicker("android");
            broadcast.setExtraField("goodsId", goodsId + "");
            broadcast.goActivityAfterOpen(appPushSetting.getAndroidGoodsActivity());
            broadcast.setDisplayType(DisplayTypeEnum.NOTIFICATION);
            //执行推送操作
            return this.push(broadcast);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String pushGoodsForIOS(String title, Integer goodsId) {
        //获取推送设置
        String appPushSettingJson = settingManager.get(SettingGroup.PUSH);
        AppPushSetting appPushSetting = JsonUtil.jsonToObject(appPushSettingJson,AppPushSetting.class);
        if (StringUtil.isEmpty(appPushSetting.getIosPushKey()) || StringUtil.isEmpty(appPushSetting.getIosPushSecret())) {
            throw new ServiceException(SystemErrorCode.E908.code(), "推送参数为空");
        }
        Map<String, String> alter = new HashMap<String, String>(16);
        alter.put("body", title);
        try {
            IOSBroadcast broadcast = new IOSBroadcast(appPushSetting.getIosPushKey(), appPushSetting.getIosPushSecret());
            broadcast.setAlert(JSONObject.fromObject(alter));
            broadcast.setBadge(1);
            broadcast.setSound("default");
            broadcast.setProductionMode(true);
            broadcast.setCustomizedField("goodsd", goodsId);
            return this.push(broadcast);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    private String push(AbstractUmengNotification msg) throws Exception {
        //获取时间戳
        String timestamp = Integer.toString((int) (System.currentTimeMillis() / 1000));
        //放入推送时间字段
        msg.setPredefinedKeyValue("timestamp", timestamp);
        //拼接推送url
        String url = host + postPath;
        //获取需要推送的Json字符串
        String postBody = msg.getPostBody();
        //签名加密
        String sign = DigestUtils.md5Hex(("POST" + url + postBody + msg.getAppMasterSecret()).getBytes("utf8"));
        //拼接签名
        url = url + "?sign=" + sign;
        //拼装post请求
        HttpPost post = new HttpPost(url);
        //设置Header
        post.setHeader("User-Agent", userAgent);
        //拼装请求体
        StringEntity se = new StringEntity(postBody, "UTF-8");
        post.setEntity(se);
        //执行推送，并获取推送结果
        HttpResponse response = client.execute(post);
        //获取状态吗
        int status = response.getStatusLine().getStatusCode();
        //拼接返回结果
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        JSONObject json = JSONObject.fromObject(result.toString());
        //推送成功时，获取任务ID作为返回值
        if (status == 200) {
            return json.getJSONObject("data").getString("task_id");
        } else {
            String errorCode = json.getJSONObject("data").getString("error_code");
            if ("2004".equals(errorCode)) {
                errorCode = "错误代码:" + errorCode + "  未配置Ip白名单，请到友盟后台进行配置 AppKey:" + json.getJSONObject("data").getString("appkey") + " IP:" + json.getJSONObject("data").getString("ip");
            } else {
                errorCode = "错误代码:" + errorCode + "  请到http://dev.umeng.com/push/ios/api-doc#4_8_2查询错误";
            }
            throw new ServiceException(SystemErrorCode.E909.code(), errorCode);
        }
    }
}
