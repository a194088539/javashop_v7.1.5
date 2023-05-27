package com.enation.app.javashop.core.client.trade.impl;

import com.enation.app.javashop.core.client.trade.OrderClient;
import com.enation.app.javashop.core.promotion.coupon.model.vo.GoodsCouponPrice;
import com.enation.app.javashop.core.promotion.fulldiscount.model.dos.FullDiscountGiftDO;
import com.enation.app.javashop.core.trade.cart.model.dos.OrderPermission;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderItemsDO;
import com.enation.app.javashop.core.trade.order.model.enums.CommentStatusEnum;
import com.enation.app.javashop.core.trade.order.model.enums.OrderMetaKeyEnum;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.order.model.vo.OrderDetailVO;
import com.enation.app.javashop.core.trade.order.model.vo.OrderSkuVO;
import com.enation.app.javashop.core.trade.order.model.vo.OrderStatusNumVO;
import com.enation.app.javashop.core.trade.order.service.OrderMetaManager;
import com.enation.app.javashop.core.trade.order.service.OrderOperateManager;
import com.enation.app.javashop.core.trade.order.service.OrderQueryManager;
import com.enation.app.javashop.core.trade.sdk.model.OrderDetailDTO;
import com.enation.app.javashop.core.trade.sdk.model.OrderSkuDTO;
import com.enation.app.javashop.framework.util.BeanUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单相关SDK
 *
 * @author Snow create in 2018/5/28
 * @version v2.0
 * @since v7.0.0
 */
@Service
@ConditionalOnProperty(value = "javashop.product", havingValue = "stand")
public class OrderClientImpl implements OrderClient {

    @Autowired
    private OrderQueryManager orderQueryManager;

    @Autowired
    private OrderOperateManager orderOperateManager;

    @Autowired
    private OrderMetaManager orderMetaManager;

    @Override
    public OrderDetailDTO getModel(String orderSn) {
        OrderDetailVO orderDetailVO = this.orderQueryManager.getModel(orderSn, null);
        OrderDetailDTO detailDTO = new OrderDetailDTO();
        BeanUtils.copyProperties(orderDetailVO, detailDTO);
        detailDTO.setOrderSkuList(new ArrayList<>());

        for (OrderSkuVO skuVO : orderDetailVO.getOrderSkuList()) {
            OrderSkuDTO skuDTO = new OrderSkuDTO();
            BeanUtil.copyProperties(skuVO, skuDTO);
            detailDTO.getOrderSkuList().add(skuDTO);
        }

        String json = this.orderMetaManager.getMetaValue(detailDTO.getSn(), OrderMetaKeyEnum.GIFT);
        List<FullDiscountGiftDO> giftList = JsonUtil.jsonToList(json, FullDiscountGiftDO.class);
        detailDTO.setGiftList(giftList);

        //该订单使用优惠券情况
        String couponJson = this.orderMetaManager.getMetaValue(detailDTO.getSn(), OrderMetaKeyEnum.COUPON_PRICE);

        if (StringUtil.notEmpty(couponJson) && !"null".equals(couponJson)
                && !"0.0".equals(couponJson)) {
            List<GoodsCouponPrice> couponList = JsonUtil.jsonToList(couponJson, GoodsCouponPrice.class);
            detailDTO.setGoodsCouponPrices(couponList);
        }

        return detailDTO;
    }

    /**
     * 读取一个订单详细<br/>
     *
     * @param orderSn 订单编号 必传
     * @return
     */
    @Override
    public OrderDetailVO getOrderVO(String orderSn) {
        return this.orderQueryManager.getModel(orderSn, null);
    }

    @Override
    public void updateOrderCommentStatus(String sn, String statusEnum) {
        CommentStatusEnum commentStatusEnum = CommentStatusEnum.valueOf(statusEnum);
        this.orderOperateManager.updateCommentStatus(sn, commentStatusEnum);
    }


    @Override
    public List<OrderDetailDTO> getOrderByTradeSn(String tradeSn) {
        List<OrderDetailDTO> orderDetailDTOList = this.orderQueryManager.getOrderByTradeSn(tradeSn);
        return orderDetailDTOList;
    }


    @Override
    public void payOrder(String sn, Double price, String returnTradeNo, String permission) {
        OrderPermission orderPermission = OrderPermission.valueOf(permission);
        this.orderOperateManager.payOrder(sn, price,returnTradeNo, orderPermission);
    }


    @Override
    public Integer getOrderNumByMemberID(Integer memberId) {
        Integer num = this.orderQueryManager.getOrderNumByMemberId(memberId);
        return num;
    }


    @Override
    public Integer getOrderCommentNumByMemberID(Integer memberId, String commentStatus) {
        return this.orderQueryManager.getOrderCommentNumByMemberId(memberId, commentStatus);
    }

    @Override
    public OrderStatusNumVO getOrderStatusNum(Integer memberId, Integer sellerId) {

        return this.orderQueryManager.getOrderStatusNum(memberId, sellerId);
    }

    @Override
    public boolean updateOrderStatus(String sn, OrderStatusEnum orderStatusEnum) {
        orderOperateManager.updateOrderStatus(sn, orderStatusEnum);
        return true;
    }

    @Override
    public boolean updateTradeStatus(String sn, OrderStatusEnum orderStatus) {
        orderOperateManager.updateTradeStatus(sn, orderStatus);
        return true;
    }

    @Override
    public void addOrderItemRefundPrice(OrderDO orderDO) {
        orderOperateManager.updateItemRefundPrice(this.getOrderVO(orderDO.getSn()));
    }

    @Override

    public void updateItemsCommentStatus(String orderSn, Integer goodsId, CommentStatusEnum commentStatus) {
        orderOperateManager.updateItemsCommentStatus(orderSn, goodsId, commentStatus);
    }

    @Override
    public List<OrderItemsDO> orderItems(String orderSn) {
        return orderQueryManager.orderItems(orderSn);
    }

}
