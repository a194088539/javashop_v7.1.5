package com.enation.app.javashop.core.base.plugin.waybill;

import java.util.List;

import com.enation.app.javashop.core.base.model.vo.ConfigItem;

/**
 * 电子面单参数借口
 *
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月10日 下午2:29:05
 */
public interface WayBillPlugin {


    /**
     * 配置各个电子面单的参数
     *
     * @return 在页面加载的电子面单参数
     */
    List<ConfigItem> definitionConfigItem();

    /**
     * 获取插件ID
     *
     * @return
     */
    String getPluginId();

    /**
     * 创建电子面单
     *
     * @param orderSn 订单编号
     * @param logisticsId
     * @return
     * @throws Exception
     */
    String createPrintData(String orderSn,Integer logisticsId) throws Exception;

    /**
     * 获取插件名称
     *
     * @return 插件名称
     */
    String getPluginName();

    /**
     * 电子面单是否开启
     *
     * @return 0 不开启  1 开启
     */
    Integer getOpen();

}
