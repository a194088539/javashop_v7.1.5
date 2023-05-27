package com.enation.app.javashop.core.aftersale.service;

import com.enation.app.javashop.core.aftersale.model.dos.AfterSaleRefundDO;
import com.enation.app.javashop.core.aftersale.model.dos.RefundDO;
import com.enation.app.javashop.core.aftersale.model.dto.RefundQueryParam;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceOperateTypeEnum;
import com.enation.app.javashop.core.aftersale.model.vo.ApplyAfterSaleVO;
import com.enation.app.javashop.core.aftersale.model.vo.RefundApplyVO;
import com.enation.app.javashop.core.aftersale.model.vo.RefundDetailVO;
import com.enation.app.javashop.core.goods.model.enums.Permission;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderItemsDO;
import com.enation.app.javashop.framework.database.Page;

/**
 * 退款单业务接口
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-22
 */
public interface AfterSaleRefundManager {

    /**
     * 查询退款单列表
     * @param param 查询参数
     * @return
     */
    Page list(RefundQueryParam param);

    /**
     * 平台退款操作
     * 主要用于平台人工线下退款
     * @param serviceSn 售后服务单号
     * @param refundPrice 退款金额
     * @param remark 退款备注
     * @param typeEnum 操作类型
     */
    void adminRefund(String serviceSn, Double refundPrice, String remark, ServiceOperateTypeEnum typeEnum);

    /**
     * 在线支付订单商家退款操作
     * @param serviceSn 售后服务单号
     */
    void sellerRefund(String serviceSn);

    /**
     * 填充并创建退款单
     * @param refundPrice 退款金额
     * @param applyAfterSaleVO 售后服务单信息
     * @return
     */
    RefundDO fillRefund(Double refundPrice, ApplyAfterSaleVO applyAfterSaleVO);

    /**
     * 新增退款单
     * @param refundDO
     * @return 退款单主键ID
     */
    Integer addRefund(RefundDO refundDO);

    /**
     * 获取退款单
     * @param serviceSn 售后服务单号
     * @return
     */
    RefundDO getModel(String serviceSn);

    /**
     * 填充并入库申请售后服务时的退款相关信息
     * @param serviceSn 售后服务单号
     * @param returnNum 申请售后服务商品数量
     * @param orderDO 订单信息
     * @param itemsDO 订单项信息
     * @param refundDO 退款相关信息
     */
    void fillAfterSaleRefund(String serviceSn, Integer returnNum, OrderDO orderDO, OrderItemsDO itemsDO, AfterSaleRefundDO refundDO);

    /**
     * 填充并入库取消订单时的退款相关信息
     * @param serviceSn 售后服务单号
     * @param orderDO 订单信息
     * @param refundApplyVO 退款申请参数信息
     */
    void fillCancelOrderRefund(String serviceSn, OrderDO orderDO, RefundApplyVO refundApplyVO);

    /**
     * 添加申请售后服务时的退款相关信息
     * @param refundDO 退款相关信息
     */
    void addAfterSaleRefund(AfterSaleRefundDO refundDO);

    /**
     * 根据售后服务单号获取售后退款账户信息
     * @param serviceSn 售后服务单编号
     * @return
     */
    AfterSaleRefundDO getAfterSaleRefund(String serviceSn);

}
