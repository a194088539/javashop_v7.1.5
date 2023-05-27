package com.enation.app.javashop.core.aftersale.service;

import com.enation.app.javashop.core.aftersale.model.dos.*;
import com.enation.app.javashop.core.aftersale.model.dto.*;
import com.enation.app.javashop.core.aftersale.model.vo.*;

import java.util.List;

/**
 * 售后服务管理接口
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-24
 */
public interface AfterSaleManager {

    /**
     * 申请售后服务
     * @param applyAfterSaleVO
     */
    void apply(ApplyAfterSaleVO applyAfterSaleVO);

    /**
     * 申请取消订单
     * @param refundApplyVO
     */
    void applyCancelOrder(RefundApplyVO refundApplyVO);

    /**
     * 保存用户填写的退货物流信息
     * @param afterSaleExpressDO
     */
    void applyShip(AfterSaleExpressDO afterSaleExpressDO);

    /**
     * 商家审核售后服务
     * @param serviceSn 售后服务单号
     * @param auditStatus 审核状态 PASS：审核通过，REFUSE：审核未通过
     * @param refundPrice 退款金额
     * @param returnAddr 退货地址信息
     * @param auditRemark 审核备注
     */
    void audit(String serviceSn, String auditStatus, Double refundPrice, String returnAddr, String auditRemark);

    /**
     * 商家将申请售后服务退还的商品入库
     * @param serviceSn 售后服务单号
     * @param storageList 入库信息集合，是个数组
     * @param remark 商家入库备注信息
     */
    void putInWarehouse(String serviceSn, List<PutInWarehouseDTO> storageList, String remark);

    /**
     * 关闭售后服务单
     * @param serviceSn 售后服务单号
     * @param remark 备注
     */
    void closeAfterSale(String serviceSn, String remark);

    /**
     * 修改售后服务单中的店铺名称
     * 当商家修改店铺名称是调用此方法
     * @param shopId 店铺id
     * @param shopName 店铺名称
     */
    void editAfterSaleShopName(Integer shopId, String shopName);

    /**
     * 系统对拼团订单执行取消操作
     * 此方法针对的是拼团订单在拼团活动结束时没有成团的情况下使用
     * @param orderSn 订单编号
     * @param cancelReason 取消原因
     */
    void cancelPintuanOrder(String orderSn, String cancelReason);

    /**
     * 添加售后服务单基础信息
     * @param serviceDO 售后服务单基础信息
     */
    void addAfterSaleService(AfterSaleServiceDO serviceDO);

    /**
     * 修改售后服务单状态
     * @param serviceSn 售后服务单号
     * @param serviceStatus 售后服务单状态
     */
    void updateServiceStatus(String serviceSn, String serviceStatus);

    /**
     * 修改售后服务单状态和备注
     * @param serviceSn 售后服务单号
     * @param serviceStatus 售后服务单状态
     * @param auditRemark 商家审核备注
     * @param storageRemark 商家入库备注
     * @param refundRemark 平台退款备注
     * @param closeReason 售后服务单关闭原因
     */
    void updateServiceStatus(String serviceSn, String serviceStatus, String auditRemark, String storageRemark, String refundRemark, String closeReason);

    /**
     * 修改售后服务单新订单号
     * 换货或补发商品重新生成订单后调用
     * @param serviceSn 售后服务单号
     * @param newOrderSn 新生成的订单号
     */
    void setServiceNewOrderSn(String serviceSn, String newOrderSn);

}
