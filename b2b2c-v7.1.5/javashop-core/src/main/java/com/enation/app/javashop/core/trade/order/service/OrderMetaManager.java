package com.enation.app.javashop.core.trade.order.service;

import com.enation.app.javashop.core.trade.order.model.dos.OrderMetaDO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderMetaKeyEnum;

import java.util.List;

/**
 * 订单元信息
 * @author Snow create in 2018/6/27
 * @version v2.0
 * @since v7.0.0
 */
public interface OrderMetaManager {

    /**
     * 添加
     * @param orderMetaDO
     */
    void add(OrderMetaDO orderMetaDO);

    /**
     * 读取订单元信息
     * @param orderSn
     * @param metaKey
     * @return
     */
    String getMetaValue(String orderSn,OrderMetaKeyEnum metaKey);

    /**
     * 读取order meta列表
     * @param orderSn
     * @return
     */
    List<OrderMetaDO> list(String orderSn);

    /**
     * 修改订单元信息
     * @param orderSn
     * @param metaKey
     * @return
     */
    void updateMetaValue(String orderSn,OrderMetaKeyEnum metaKey, String metaValue);

    /**
     * 获取一条订单元信息
     * @param orderSn 订单编号
     * @param metaKey 扩展-键
     * @return
     */
    OrderMetaDO getModel(String orderSn, OrderMetaKeyEnum metaKey);
}
