package com.enation.app.javashop.core.aftersale.service;

import com.enation.app.javashop.core.aftersale.model.dos.AfterSaleLogDO;

import java.util.List;

/**
 * 售后日志业务接口
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-16
 */
public interface AfterSaleLogManager {

    /**
     * 新增售后日志
     * @param serviceSn 售后服务单号
     * @param logDetail 日志详细
     * @param operator 操作人
     */
    void add(String serviceSn, String logDetail, String operator);

    /**
     * 根据售后服务单号获取售后日志信息集合
     * @param serviceSn 售后服务单号
     * @return
     */
    List<AfterSaleLogDO> list(String serviceSn);

}
