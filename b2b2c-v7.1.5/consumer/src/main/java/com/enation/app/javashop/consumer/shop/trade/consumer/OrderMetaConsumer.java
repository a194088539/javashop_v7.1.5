package com.enation.app.javashop.consumer.shop.trade.consumer;

import com.enation.app.javashop.consumer.core.event.TradeIntoDbEvent;
import com.enation.app.javashop.core.promotion.coupon.model.vo.GoodsCouponPrice;
import com.enation.app.javashop.core.promotion.fulldiscount.model.dos.FullDiscountGiftDO;
import com.enation.app.javashop.core.trade.cart.model.vo.CouponVO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderMetaDO;
import com.enation.app.javashop.core.trade.order.model.dos.PayLog;
import com.enation.app.javashop.core.trade.order.model.dto.OrderDTO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderMetaKeyEnum;
import com.enation.app.javashop.core.trade.order.model.enums.OrderServiceStatusEnum;
import com.enation.app.javashop.core.trade.order.model.enums.PayStatusEnum;
import com.enation.app.javashop.core.trade.order.model.vo.TradeVO;
import com.enation.app.javashop.core.trade.order.service.OrderMetaManager;
import com.enation.app.javashop.core.trade.order.service.PayLogManager;
import com.enation.app.javashop.core.trade.order.service.TradeSnCreator;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单其他信息入库
 *
 * @author Snow create in 2018/6/26
 * @version v2.0
 * @since v7.0.0
 */
@Component
public class OrderMetaConsumer implements TradeIntoDbEvent {


    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private OrderMetaManager orderMetaManager;

    @Autowired
    private PayLogManager payLogManager;

    @Autowired
    private TradeSnCreator tradeSnCreator;

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void onTradeIntoDb(TradeVO tradeVO) {

        List<OrderDTO> orderList = tradeVO.getOrderList();
        //获取使用的优惠券，根据商家匹配到相应商家中
        for (OrderDTO orderDTO : orderList) {
            addCashBack(orderDTO);
            addUsePoint(orderDTO);
            addGiftPoint(orderDTO);
            addGiftCoupon(orderDTO);
            addCouponPrice(orderDTO);
            addGift(orderDTO);
            addLog(orderDTO);
            addFullMinusPrice(orderDTO);
        }

    }



    /**
     * 记录返现金额
     *
     * @param orderDTO
     */
    private void addCashBack(OrderDTO orderDTO) {
        try {
            //记录返现金额
            double cashBack = orderDTO.getPrice().getCashBack();
            OrderMetaDO cashBackMeta = new OrderMetaDO();
            cashBackMeta.setMetaKey(OrderMetaKeyEnum.CASH_BACK.name());
            cashBackMeta.setMetaValue(cashBack + "");
            cashBackMeta.setOrderSn(orderDTO.getSn());
            this.orderMetaManager.add(cashBackMeta);
            if (logger.isDebugEnabled()) {
                logger.debug("返现金额入库完成");
            }
        } catch (Exception e) {
            logger.error("返现金额入库出错", e);
        }

    }


    /**
     * 记录使用的积分
     *
     * @param orderDTO
     */
    private void addUsePoint(OrderDTO orderDTO) {
        try {

            //使用的积分
            Long consumerPoint = orderDTO.getPrice().getExchangePoint();
            if (consumerPoint > 0) {
                //使用积分的记录
                OrderMetaDO pointMeta = new OrderMetaDO();
                pointMeta.setMetaKey(OrderMetaKeyEnum.POINT.name());
                pointMeta.setMetaValue(consumerPoint + "");
                pointMeta.setOrderSn(orderDTO.getSn());
                pointMeta.setStatus(OrderServiceStatusEnum.NOT_APPLY.name());
                this.orderMetaManager.add(pointMeta);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("记录使用的积分");
            }

        } catch (Exception e) {
            logger.error("记录使用的积分出错", e);
        }

    }


    /**
     * 赠送积分的记录
     *
     * @param orderDTO
     */
    private void addGiftPoint(OrderDTO orderDTO) {
        try {
            //赠送积分的记录
            OrderMetaDO giftPointMeta = new OrderMetaDO();
            giftPointMeta.setMetaKey(OrderMetaKeyEnum.GIFT_POINT.name());
            giftPointMeta.setMetaValue(orderDTO.getGiftPoint() + "");
            giftPointMeta.setOrderSn(orderDTO.getSn());
            giftPointMeta.setStatus(OrderServiceStatusEnum.NOT_APPLY.name());
            this.orderMetaManager.add(giftPointMeta);

            if (logger.isDebugEnabled()) {
                logger.debug("赠送积分完成");
            }
        } catch (Exception e) {
            logger.error("赠送积分的记录出错", e);
        }

    }


    /**
     * 赠优惠券入库
     *
     * @param orderDTO
     */
    private void addGiftCoupon(OrderDTO orderDTO) {
        try {


            //赠优惠券入库
            List<CouponVO> couponList = orderDTO.getGiftCouponList();
            OrderMetaDO couponMeta = new OrderMetaDO();
            couponMeta.setMetaKey(OrderMetaKeyEnum.COUPON.name());
            couponMeta.setMetaValue(JsonUtil.objectToJson(couponList));
            couponMeta.setOrderSn(orderDTO.getSn());
            couponMeta.setStatus(OrderServiceStatusEnum.NOT_APPLY.name());
            this.orderMetaManager.add(couponMeta);

            if (logger.isDebugEnabled()) {
                logger.debug("赠优惠券完成");
            }

        } catch (Exception e) {
            logger.error("赠优惠券出错", e);
        }

    }


    /**
     * 记录使用优惠券的金额
     *
     * @param orderDTO
     */
    private void addCouponPrice(OrderDTO orderDTO) {
        try {
            List<GoodsCouponPrice> couponList = orderDTO.getGoodsCouponPrices();
            //记录使用优惠券的金额
            OrderMetaDO orderMeta = new OrderMetaDO();
            orderMeta.setMetaKey(OrderMetaKeyEnum.COUPON_PRICE.name());
            orderMeta.setMetaValue(JsonUtil.objectToJson(couponList));
            orderMeta.setOrderSn(orderDTO.getSn());
            orderMetaManager.add(orderMeta);

            if (logger.isDebugEnabled()) {
                logger.debug("使用优惠券的金额完成");
            }

        } catch (Exception e) {
            logger.error("使用优惠券的金额出错", e);
        }

    }


    /**
     * 记录使用优惠券的金额
     *
     * @param orderDTO
     */
    private void addFullMinusPrice(OrderDTO orderDTO) {
        try {

            //记录使用优惠券的金额
            OrderMetaDO orderMeta = new OrderMetaDO();
            orderMeta.setMetaKey(OrderMetaKeyEnum.FULL_MINUS.name());
            orderMeta.setMetaValue("" + orderDTO.getPrice().getFullMinus());
            orderMeta.setOrderSn(orderDTO.getSn());
            orderMetaManager.add(orderMeta);
            if (logger.isDebugEnabled()) {
                logger.debug("订单满减金额入库完成");
            }
        } catch (Exception e) {
            logger.error("订单满减金额入库出错", e);
        }

    }


    /***
     * 赠品入库
     * @param orderDTO
     */
    private void addGift(OrderDTO orderDTO) {

        try {
            //赠品入库
            List<FullDiscountGiftDO> giftList = orderDTO.getGiftList();
            OrderMetaDO giftMeta = new OrderMetaDO();
            giftMeta.setMetaKey(OrderMetaKeyEnum.GIFT.name());
            giftMeta.setMetaValue(JsonUtil.objectToJson(giftList));
            giftMeta.setOrderSn(orderDTO.getSn());
            giftMeta.setStatus(OrderServiceStatusEnum.NOT_APPLY.name());
            this.orderMetaManager.add(giftMeta);

            if (logger.isDebugEnabled()) {
                logger.debug("赠品入库完成");
            }

        } catch (Exception e) {
            logger.error("赠品入库出错", e);
        }


    }


    /**
     * 记录日志
     *
     * @param orderDTO
     */
    private void addLog(OrderDTO orderDTO) {
        try {
            //付款单
            PayLog payLog = new PayLog();
            payLog.setPayLogSn(tradeSnCreator.generatePayLogSn());
            payLog.setOrderSn(orderDTO.getSn());
            payLog.setPayMemberName(orderDTO.getMemberName());
            payLog.setPayStatus(PayStatusEnum.PAY_NO.name());
            payLog.setPayWay(orderDTO.getPaymentType());

            this.payLogManager.add(payLog);

            if (logger.isDebugEnabled()) {
                logger.debug("日志入库完成");
            }

        } catch (Exception e) {
            logger.error("日志入库出错", e);
        }

    }

}
