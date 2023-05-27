package com.enation.app.javashop.core.aftersale.service;

import com.enation.app.javashop.core.aftersale.model.dos.AfterSaleChangeDO;

/**
 * 售后服务退货地址业务接口
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-12-03
 */
public interface AfterSaleChangeManager {

    /**
     * 新增售后服务退货地址信息
     * @param afterSaleChangeDO
     */
    void add(AfterSaleChangeDO afterSaleChangeDO);

    /**
     * 填充售后服务退货地址信息
     * @param serviceSn 售后服务单号
     * @param afterSaleChangeDO 退货地址信息
     * @return
     */
    AfterSaleChangeDO fillChange(String serviceSn, AfterSaleChangeDO afterSaleChangeDO);

    /**
     * 根据售后服务单号获取收货地址相关信息
     * @param serviceSn 售后服务单编号
     * @return
     */
    AfterSaleChangeDO getModel(String serviceSn);
}
