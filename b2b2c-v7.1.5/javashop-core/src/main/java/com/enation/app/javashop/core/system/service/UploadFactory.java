package com.enation.app.javashop.core.system.service;

import java.util.List;

import com.enation.app.javashop.core.system.model.vo.UploaderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.base.plugin.upload.Uploader;
import com.enation.app.javashop.core.system.model.dos.UploaderDO;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ResourceNotFoundException;


/**
 * 存储方案VO
 *
 * @author zh
 * @version v1.0
 * @since v1.0
 * 2018年3月22日 上午9:39:08
 */
@Component
public class UploadFactory {


    @Autowired
    private List<Uploader> uploads;

    @Autowired
    private Cache cache;

    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport systemDaoSupport;


    /**
     * 获取存储方案对象
     *
     * @return 实例化的存储方案对象
     */
    public Uploader getUploader() {
        UploaderVO uploaderVo = (UploaderVO) cache.get(CachePrefix.UPLOADER.getPrefix().toString());
        //如果为空则要到库中读取
        if (uploaderVo == null) {
            //由数据库中查询存储方案
            String sql = "SELECT * FROM es_uploader WHERE open = 1";
            UploaderDO upload = this.systemDaoSupport.queryForObject(sql, UploaderDO.class);
            if (upload == null) {
                throw new ResourceNotFoundException("未找到开启的存储方案");
            }
            uploaderVo = new UploaderVO();
            uploaderVo.setConfig(upload.getConfig());
            uploaderVo.setBean(upload.getBean());
            cache.put(CachePrefix.UPLOADER.getPrefix(), uploaderVo);
        }
        return this.findByBeanid(uploaderVo.getBean());
    }

    /**
     * 根据beanid获取出存储方案
     *
     * @param beanid
     * @return
     */
    private Uploader findByBeanid(String beanid) {
        for (Uploader iUploader : uploads) {
            if (iUploader.getPluginId().equals(beanid)) {
                return iUploader;
            }
        }
        //如果走到这里，说明找不到可用的存储方案
        throw new ResourceNotFoundException("未找到可用的文件存储方案");
    }


}
