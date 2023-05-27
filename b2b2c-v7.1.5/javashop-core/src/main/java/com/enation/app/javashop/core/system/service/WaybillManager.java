package com.enation.app.javashop.core.system.service;

import com.enation.app.javashop.core.system.model.dos.WayBillDO;
import com.enation.app.javashop.core.system.model.vo.WayBillVO;
import com.enation.app.javashop.framework.database.Page;

/**
 * 电子面单业务层
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-06-08 16:26:05
 */
public interface WaybillManager {

    /**
     * 查询电子面单列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return Page
     */
    Page list(int page, int pageSize);

    /**
     * 添加电子面单
     *
     * @param wayBill 电子面单
     * @return WayBillDO 电子面单
     */
    WayBillDO add(WayBillVO wayBill);

    /**
     * 修改电子面单
     *
     * @param wayBill 电子面单
     * @return WayBillDO 电子面单
     */
    WayBillVO edit(WayBillVO wayBill);

    /**
     * 获取电子面单
     *
     * @param id 电子面单主键
     * @return WayBillDO  电子面单
     */
    WayBillDO getModel(Integer id);

    /**
     * 开启电子面单
     *
     * @param bean beanid
     */
    void open(String bean);


    /**
     * 根据beanid获取电子面单方案
     *
     * @param bean
     * @return
     */
    WayBillDO getWayBillByBean(String bean);


    /**
     * 根据beanid获取电子面单方案
     *
     * @param bean beanid
     * @return 电子面单vo
     */
    WayBillVO getWaybillConfig(String bean);

    /**
     * 生成电子面单
     * @param orderSn
     * @param logisticsId
     * @return
     */
    String createPrintData(String orderSn,Integer logisticsId);

}