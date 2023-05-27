package com.enation.app.javashop.core.client.system.impl;

import com.enation.app.javashop.core.base.plugin.upload.Uploader;
import com.enation.app.javashop.core.client.system.UploadFactoryClient;
import com.enation.app.javashop.core.system.service.UploadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @version v7.0
 * @Description:
 * @Author: zjp
 * @Date: 2018/7/27 16:27
 */
@Service
@ConditionalOnProperty(value = "javashop.product", havingValue = "stand")
public class UploadFactoryClientDefaultImpl implements UploadFactoryClient {
    @Autowired
    private UploadFactory uploadFactory;

    @Override
    public String getUrl(String url, Integer width, Integer height) {
        Uploader uploader = uploadFactory.getUploader();
        return uploader.getThumbnailUrl(url, width, height);
    }
}
