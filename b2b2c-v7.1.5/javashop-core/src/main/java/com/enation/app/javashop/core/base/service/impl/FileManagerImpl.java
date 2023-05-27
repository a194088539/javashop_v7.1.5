package com.enation.app.javashop.core.base.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.base.model.dto.FileDTO;
import com.enation.app.javashop.core.base.model.vo.ConfigItem;
import com.enation.app.javashop.core.base.model.vo.FileVO;
import com.enation.app.javashop.core.base.plugin.upload.Uploader;
import com.enation.app.javashop.core.base.service.FileManager;
import com.enation.app.javashop.core.system.service.UploadFactory;
import com.enation.app.javashop.core.system.model.vo.UploaderVO;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 文件上传接口实现
 *
 * @author zh
 * @version v2.0
 * @since v7.0
 * 2018年3月19日 下午4:38:42
 */
@Service
public class FileManagerImpl implements FileManager {

    @Autowired
    private UploadFactory uploadFactory;
    @Autowired
    private Cache cache;

    @Override
    public FileVO upload(FileDTO input, String scene) {
        if (StringUtil.isEmpty(scene)) {
            scene = "normal";
        }
        Uploader uploader = uploadFactory.getUploader();
        return uploader.upload(input, scene, this.getconfig());
    }

    @Override
    public void deleteFile(String filePath) {
        Uploader uploader = uploadFactory.getUploader();
        uploader.deleteFile(filePath, this.getconfig());
    }

    /**
     * 获取存储方案配置
     *
     * @return
     */
    private Map getconfig() {
        UploaderVO upload = (UploaderVO) cache.get(CachePrefix.UPLOADER.getPrefix());
        if (StringUtil.isEmpty(upload.getConfig())) {
            return new HashMap<>(16);
        }
        Gson gson = new Gson();
        List<ConfigItem> list = gson.fromJson(upload.getConfig(), new TypeToken<List<ConfigItem>>() {
        }.getType());
        Map<String, String> result = new HashMap<>(16);
        if (list != null) {
            for (ConfigItem item : list) {
                result.put(item.getName(), StringUtil.toString(item.getValue()));
            }
        }
        return result;
    }


}
