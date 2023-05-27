package com.enation.app.javashop.core.system.model.vo;

import net.sf.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;

/**
 * IOS 推送消息Model基类
 * 作用:拼接Ios推送需要的字符串信息
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-06-07 17:09:03
 */
public abstract class AbstractIosNotification extends AbstractUmengNotification {

    /**
     * Ios标注APS中的key值
     */
    protected static final HashSet<String> APS_KEYS = new HashSet<String>(Arrays.asList("alert", "badge", "sound", "content-available"));

    /**
     * 写入Json值
     *
     * @param key   索引
     * @param value 值
     * @return 是否写入成功
     * @author LDD
     * @since v6.4
     */
    @Override
    public boolean setPredefinedKeyValue(String key, Object value) throws Exception {
        //如果是根部的字段，直接写入
        if (ROOT_KEYS.contains(key)) {
            rootJson.put(key, value);
        } else if (APS_KEYS.contains(key)) {
            JSONObject apsJson = null;
            JSONObject payloadJson = null;
            //判断rootJson中是否有payload，有则取出没有则创建
            if (rootJson.has("payload")) {
                payloadJson = rootJson.getJSONObject("payload");
            } else {
                payloadJson = new JSONObject();
            }
            //判断rootJson中是否有aps，有则取出没有则创建
            if (payloadJson.has("aps")) {
                apsJson = payloadJson.getJSONObject("aps");
                apsJson.put(key, value);
                payloadJson.put("aps", apsJson);
            } else {
                apsJson = new JSONObject();
                apsJson.put(key, value);
                payloadJson.put("aps", apsJson);
            }
            rootJson.put("payload", payloadJson);
            //判断是否是POLICY中的字段*/
        } else if (POLICY_KEYS.contains(key)) {
            JSONObject policyJson = null;
            if (rootJson.has("policy")) {
                policyJson = rootJson.getJSONObject("policy");
            } else {
                policyJson = new JSONObject();
                rootJson.put("policy", policyJson);
            }
            policyJson.put(key, value);
        } else {
            //如果属于以下字段 抛出空值异常 如果不输入抛出无效key异常
            if ("payload".equals(key) || "aps".equals(key) || "policy".equals(key)) {
                throw new Exception("You don't need to set value for " + key + " , just set values for the sub keys in it.");
            } else {
                throw new Exception("Unknownd key: " + key);
            }
        }

        return true;
    }

    /**
     * 设置自定义值
     *
     * @param key   索引
     * @param value 值
     * @return 是否添加成功
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public boolean setCustomizedField(String key, Object value) throws Exception {
        JSONObject payloadJson = null;
        if (rootJson.has("payload")) {
            payloadJson = rootJson.getJSONObject("payload");
        } else {
            payloadJson = new JSONObject();
            rootJson.put("payload", payloadJson);
        }
        payloadJson.put(key, value);
        return true;
    }

    /**
     * 设置苹果alter值
     *
     * @param token token值
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setAlert(JSONObject token) throws Exception {
        setPredefinedKeyValue("alert", token);
    }

    /**
     * 设置角标
     *
     * @param badge 角标数
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setBadge(Integer badge) throws Exception {
        setPredefinedKeyValue("badge", badge);
    }

    /**
     * 设置铃声
     *
     * @param sound 铃声
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setSound(String sound) throws Exception {
        setPredefinedKeyValue("sound", sound);
    }

    /**
     * 设置内容
     *
     * @param contentAvailable 内容
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setContentAvailable(Integer contentAvailable) throws Exception {
        setPredefinedKeyValue("content-available", contentAvailable);
    }
}
