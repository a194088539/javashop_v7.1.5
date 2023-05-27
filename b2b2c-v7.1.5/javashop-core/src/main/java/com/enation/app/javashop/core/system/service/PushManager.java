package com.enation.app.javashop.core.system.service;

/**
 * 推送业务类
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-04-28 13:49:38
 */
public interface PushManager {

    /**
     * 安卓端推送
     *
     * @param title 标题
     * @param goodsId 商id
     * @return 任务id
     */
    String pushGoodsForAndroid(String title, Integer goodsId);

    /**
     * ios端推送
     *
     * @param title 标题
     * @param goodsId 商品id
     * @return 任务id
     */
    String pushGoodsForIOS(String title, Integer goodsId);


}