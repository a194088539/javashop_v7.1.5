package com.enation.app.javashop.core.aftersale.service;

import com.enation.app.javashop.core.aftersale.model.dos.AfterSaleGoodsDO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderItemsDO;

import java.util.List;

/**
 * 售后服务商品业务接口
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-12-03
 */
public interface AfterSaleGoodsManager {

    /**
     * 新增售后服务商品信息
     * @param afterSaleGoodsDO
     */
    void add(AfterSaleGoodsDO afterSaleGoodsDO);

    /**
     * 填充售后服务商品信息实体
     * @param serviceSn 售后服务单号
     * @param returnNum 申请售后数量
     * @param itemsDO 订单项信息
     */
    AfterSaleGoodsDO fillGoods(String serviceSn, Integer returnNum, OrderItemsDO itemsDO);

    /**
     * 根据售后服务单号获取售后商品信息集合
     * @param serviceSn 售后服务单编号
     * @return
     */
    List<AfterSaleGoodsDO> listGoods(String serviceSn);

    /**
     * 修改售后服务商品的入库数量
     * @param serviceSn 售后服务单号
     * @param skuId 商品skuID
     * @param num 入库数量
     */
    void updateStorageNum(String serviceSn, Integer skuId, Integer num);
}
