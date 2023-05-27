package com.enation.app.javashop.core.shop.service;

import com.enation.app.javashop.core.shop.model.dos.ShopLogisticsSetting;
import com.enation.app.javashop.core.system.model.dto.KDNParams;
import java.util.List;

/**
 * 店铺物流公司管理类
 *
 * @author zhangjiping
 * @version v7.0.0
 * @since v7.0.0
 * 2018年3月29日 下午5:23:38
 */
public interface ShopLogisticsCompanyManager {
    /**
     * 获取店铺物流公司列表
     *
     * @return
     */
    List list();

    /**
     * 开启某个物流
     * @param logisticsId
     */
    void open(Integer logisticsId);

    /**
     * 添加本店的物流公司 电子面单信息
     * @param shopLogisticsSetting
     */
    void add(ShopLogisticsSetting shopLogisticsSetting);

    /**
     * 修改本店的物流公司 电子面单信息
     * @param shopLogisticsSetting
     * @param id
     */
    void edit(ShopLogisticsSetting shopLogisticsSetting, int id);

    /**
     * 设置某店铺物流参数
     * @param kdnParams 快递鸟参数
     */
    void setting(KDNParams kdnParams,Integer logisticsId);

    /**
     * 删除本店的物流公司
     * @param logisticsId
     */
    void delete(Integer logisticsId);

    /**
     * 查询物流公司
     * @param logisticsId
     * @param sellerId
     * @return
     */
    ShopLogisticsSetting query(Integer logisticsId,Integer sellerId);

    /**
     * 查询绑定该物流公司的列表
     * @param logisticsId
     * @return
     */
    List queryListByLogisticsId(Integer logisticsId);

    /**
     * 删除绑定某物流公司的所有店铺
     * @param logisticsId
     */
    void deleteByLogisticsId(Integer logisticsId);

}
