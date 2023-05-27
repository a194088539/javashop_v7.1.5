package com.enation.app.javashop.core.client.system;

/**
 * @version v7.0
 * @Description: 存储方案Client
 * @Author: zjp
 * @Date: 2018/7/27 16:26
 */
public interface UploadFactoryClient {
    /**
     *
     * @param url
     * @param width
     * @param height
     * @return
     */
     String getUrl(String url, Integer width, Integer height);
}
