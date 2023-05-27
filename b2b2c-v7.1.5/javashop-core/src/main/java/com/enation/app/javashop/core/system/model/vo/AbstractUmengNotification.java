package com.enation.app.javashop.core.system.model.vo;

import net.sf.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;

/**
 * 推送Model基类 用于拼接推送字符串消息
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-05-24 17:09:03
 */
public abstract class AbstractUmengNotification {
    /**
     * 根Json字符串
     */
    protected final JSONObject rootJson = new JSONObject();

    /**
     * 推送秘钥
     */
    protected String appMasterSecret;

    /**
     * 处于根部的JSON字段的Key值
     */
    protected static final HashSet<String> ROOT_KEYS = new HashSet<String>(Arrays.asList("appkey", "timestamp", "type", "device_tokens", "alias", "alias_type", "file_id",
            "filter", "production_mode", "feedback", "description", "thirdparty_id"));

    /**
     * 处于POLICY中的字段Key值
     */
    protected static final HashSet<String> POLICY_KEYS = new HashSet<String>(Arrays.asList("start_time", "expire_time", "max_send_num"));

    /**
     * 写入字段 抽象方法 子类自由实现
     *
     * @param key   索引
     * @param value 值
     * @return 是否添加成功
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public abstract boolean setPredefinedKeyValue(String key, Object value) throws Exception;

    /**
     * 设置推送秘钥
     *
     * @param secret 秘钥
     * @author LDD
     * @since v6.4
     */
    public void setAppMasterSecret(String secret) {
        appMasterSecret = secret;
    }

    /**
     * 获取需要推送的字符串
     *
     * @return 推送字符串
     * @author LDD
     * @since v6.4
     */
    public String getPostBody() {
        return rootJson.toString();
    }

    /**
     * 获取推送秘钥
     *
     * @return 推送秘钥
     * @author LDD
     * @since v6.4
     */
    public final String getAppMasterSecret() {
        return appMasterSecret;
    }

    /**
     * 设置推送模式（true正式，false测试）
     *
     * @param prod 模式标识
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setProductionMode(Boolean prod) throws Exception {
        setPredefinedKeyValue("production_mode", prod.toString());
    }

    /**
     * 开启正式模式
     *
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setProductionMode() throws Exception {
        setProductionMode(true);
    }


    /**
     * 开启测试模式
     *
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setTestMode() throws Exception {
        setProductionMode(false);
    }

    /**
     * 发送消息描述
     *
     * @param description 描述
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setDescription(String description) throws Exception {
        setPredefinedKeyValue("description", description);
    }

    /**
     * 定时发送时间，若不填写表示立即发送。格式: "YYYY-MM-DD hh:mm:ss"。
     *
     * @param startTime 发送时间
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setStartTime(String startTime) throws Exception {
        setPredefinedKeyValue("start_time", startTime);
    }

    /**
     * 消息过期时间,格式: "YYYY-MM-DD hh:mm:ss"
     *
     * @param expireTime 消息过期时间
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setExpireTime(String expireTime) throws Exception {
        setPredefinedKeyValue("expire_time", expireTime);
    }

    /**
     * 发送限速，每秒发送的最大条数
     *
     * @param num 每秒发送的最大条数
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setMaxSendNum(Integer num) throws Exception {
        setPredefinedKeyValue("max_send_num", num);
    }

}
