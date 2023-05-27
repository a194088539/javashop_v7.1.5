package com.enation.app.javashop.core.system.model.vo;

/**
 * Ios广播Model类
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-06-07 17:09:03
 */
public class IOSBroadcast extends AbstractIosNotification {

    /**
     * 构造方法
     *
     * @param appkey          IosAppKey
     * @param appMasterSecret Ios推送秘钥
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public IOSBroadcast(String appkey, String appMasterSecret) throws Exception {
        setAppMasterSecret(appMasterSecret);
        setPredefinedKeyValue("appkey", appkey);
        this.setPredefinedKeyValue("type", "broadcast");
    }
}
