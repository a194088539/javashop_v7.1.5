package com.enation.app.javashop.core.trade.order.service;

import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderItemsDO;
import com.enation.app.javashop.core.trade.order.model.dto.OrderDetailQueryParam;
import com.enation.app.javashop.core.trade.order.model.dto.OrderQueryParam;
import com.enation.app.javashop.core.trade.order.model.vo.*;
import com.enation.app.javashop.core.trade.sdk.model.OrderDetailDTO;
import com.enation.app.javashop.framework.database.Page;

import java.util.List;

/**
 * 订单相关
 *
 * @author Snow create in 2018/5/14
 * @version v2.0
 * @since v7.0.0
 */
public interface OrderQueryManager {


    /**
     * 查询订单表列表
     *
     * @param paramDTO 参数对象
     * @return Page
     */
    Page list(OrderQueryParam paramDTO);

    /**
     * 按条件导出订单数据
     *
     * @param paramDTO 参数对象
     * @return Page
     */
    List<OrderLineVO> export(OrderQueryParam paramDTO);

    /**
     * 读取一个订单详细<br/>
     *
     * @param orderSn    订单编号 必传
     * @param queryParam 参数
     * @return
     */
    OrderDetailVO getModel(String orderSn, OrderDetailQueryParam queryParam);

    /**
     * 根据订单ID读取一个订单详细
     *
     * @param orderId
     * @return
     */
    OrderDO getModel(Integer orderId);

    /**
     * 根据订单编号读取一个订单详细
     * @param orderSn
     * @return
     */
    OrderDO getOrder(String orderSn);

    /**
     * 读取订单状态的订单数
     *
     * @param memberId
     * @param sellerId
     * @return
     */
    OrderStatusNumVO getOrderStatusNum(Integer memberId, Integer sellerId);


    /**
     * 获取某订单的订单项
     *
     * @param orderSn
     * @return
     */
    List<OrderItemsDO> orderItems(String orderSn);

    /**
     * 根据订单sn读取，订单的流程
     *
     * @param orderSn 订单编号
     * @return
     */
    List<OrderFlowNode> getOrderFlow(String orderSn);


    /**
     * 读取会员所有的订单数量
     *
     * @param memberId
     * @return
     */
    Integer getOrderNumByMemberId(Integer memberId);


    /**
     * 读取会员(评论状态)订单数量
     *
     * @param memberId
     * @param commentStatus 评论状态
     * @return
     */
    Integer getOrderCommentNumByMemberId(Integer memberId, String commentStatus);


    /**
     * 读取订单列表根据交易编号——系统内部使用 OrderClient
     *
     * @param tradeSn
     * @return
     */
    List<OrderDetailDTO> getOrderByTradeSn(String tradeSn);


    /**
     * 读取订单列表根据交易编号
     *
     * @param tradeSn
     * @param memberId
     * @return
     */
    List<OrderDetailVO> getOrderByTradeSn(String tradeSn, Integer memberId);

    /**
     * 查询一个订单的详细
     *
     * @param orderSn
     * @return
     */
    OrderDetailDTO getModel(String orderSn);

    /**
     * 获取订单可退款总额
     *
     * @param orderSn 订单编号
     * @return
     */
    double getOrderRefundPrice(String orderSn);

    /**
     * 获取几个月之内购买过相关商品的订单数据
     * 获取的订单数据只限已完成和已收货并且未删除的订单
     *
     * @param goodsId  商品id
     * @param memberId 会员id
     * @param month    月数 例：2代表2个月之内
     * @return
     */
    List<OrderDO> listOrderByGoods(Integer goodsId, Integer memberId, Integer month);


    /**
     * 根据订单orderId查询发货单相关信息
     *
     * @param orderId 订单id
     * @return 发货单信息
     */
    InvoiceVO getInvoice(Integer orderId);

}