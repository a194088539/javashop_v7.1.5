package com.enation.app.javashop.core.aftersale.service;

import com.enation.app.javashop.core.aftersale.model.dos.AfterSaleGalleryDO;

import java.util.List;

/**
 * 售后服务图片业务接口
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-12-03
 */
public interface AfterSaleGalleryManager {

    /**
     * 新增售后图片信息
     * @param galleryDO
     */
    void add(AfterSaleGalleryDO galleryDO);

    /**
     * 填充并入库售后服务图片信息
     * @param serviceSn 售后服务单信息
     * @param images 上传的图片集合
     */
    void fillImage(String serviceSn, List<AfterSaleGalleryDO> images);

    /**
     * 根据售后服务单号获取售后图片相关信息
     * @param serviceSn 售后服务单编号
     * @return
     */
    List<AfterSaleGalleryDO> listImages(String serviceSn);

}
