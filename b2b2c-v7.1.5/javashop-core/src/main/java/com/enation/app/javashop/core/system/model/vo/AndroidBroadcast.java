package com.enation.app.javashop.core.system.model.vo;


/**
 * Android广播model类
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-06-07 17:09:03
 */
public class AndroidBroadcast extends AbstractAndroidNotification {

    /**
     * 构造方法
     *
     * @param appkey          Android appKey
     * @param appMasterSecret Android 推送秘钥
     * @throws Exception 空手指异常
     * @author LDD
     * @since v6.4
     */
    public AndroidBroadcast(String appkey, String appMasterSecret) throws Exception {
        setAppMasterSecret(appMasterSecret);
        setPredefinedKeyValue("appkey", appkey);
        this.setPredefinedKeyValue("type", "broadcast");
    }
}
