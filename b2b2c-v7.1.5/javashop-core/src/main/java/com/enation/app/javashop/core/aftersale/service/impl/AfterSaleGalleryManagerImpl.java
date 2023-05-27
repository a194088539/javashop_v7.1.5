package com.enation.app.javashop.core.aftersale.service.impl;

import com.enation.app.javashop.core.aftersale.model.dos.AfterSaleGalleryDO;
import com.enation.app.javashop.core.aftersale.service.AfterSaleGalleryManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 售后服务图片业务接口实现
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-12-03
 */
@Service
public class AfterSaleGalleryManagerImpl implements AfterSaleGalleryManager {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Override
    public void add(AfterSaleGalleryDO galleryDO) {
        this.daoSupport.insert(galleryDO);
    }

    @Override
    public void fillImage(String serviceSn, List<AfterSaleGalleryDO> images) {
        //如果售后图片信息集合不为空并且长度不为0
        if (images != null && images.size() != 0) {
            //售后图片循环入库
            for (AfterSaleGalleryDO image : images) {
                image.setServiceSn(serviceSn);
                this.add(image);
            }
        }
    }

    @Override
    public List<AfterSaleGalleryDO> listImages(String serviceSn) {
        String sql = "select * from es_as_gallery where service_sn = ?";
        return this.daoSupport.queryForList(sql, AfterSaleGalleryDO.class, serviceSn);
    }
}
