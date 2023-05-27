package com.enation.app.javashop.consumer.shop.trade.consumer;

import com.enation.app.javashop.consumer.core.event.OrderStatusChangeEvent;
import com.enation.app.javashop.consumer.core.event.TradeIntoDbEvent;
import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;
import com.enation.app.javashop.core.client.member.MemberClient;
import com.enation.app.javashop.core.member.model.dos.MemberCoupon;
import com.enation.app.javashop.core.promotion.coupon.model.dos.CouponDO;
import com.enation.app.javashop.core.promotion.coupon.model.vo.GoodsCouponPrice;
import com.enation.app.javashop.core.promotion.coupon.service.CouponManager;
import com.enation.app.javashop.core.trade.cart.model.vo.CouponVO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dto.OrderDTO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderMetaKeyEnum;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.order.model.vo.TradeVO;
import com.enation.app.javashop.core.trade.order.service.OrderMetaManager;
import com.enation.app.javashop.framework.util.CurrencyUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 确认收款发放促销活动赠送优惠券
 *
 * @author Snow create in 2018/5/22
 * @version v2.0
 * @since v7.0.0
 */
@Component
public class CouponConsumer implements OrderStatusChangeEvent, TradeIntoDbEvent {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private OrderMetaManager orderMetaManager;

    @Autowired
    private MemberClient memberClient;

    @Autowired
    private CouponManager couponManager;


    @Override
    public void orderChange(OrderStatusChangeMsg orderMessage) {

        if ((orderMessage.getNewStatus().name()).equals(OrderStatusEnum.PAID_OFF.name())) {

            OrderDO order = orderMessage.getOrderDO();

            //读取已发放的优惠券json
            String itemJson = this.orderMetaManager.getMetaValue(order.getSn(), OrderMetaKeyEnum.COUPON);
            List<CouponVO> couponList = JsonUtil.jsonToList(itemJson, CouponVO.class);
            if (couponList != null && couponList.size() > 0) {

                // 循环发放的优惠券
                for (CouponVO couponVO : couponList) {
                    CouponDO couponDO = this.couponManager.getModel(couponVO.getCouponId());
                    //优惠券可领取数量不足时,不赠送优惠券
                    if (CurrencyUtil.sub(couponDO.getCreateNum(), couponDO.getReceivedNum()) <= 0) {
                        continue;
                    }
                    this.memberClient.receiveBonus(order.getMemberId(), order.getMemberName(), couponVO.getCouponId());
                }
            }
        }
    }


    @Override
    public void onTradeIntoDb(TradeVO tradeVO) {
        try {

            List<OrderDTO> orderDTOList = tradeVO.getOrderList();

            //用于记录使用的优惠券id，重复的不记录，使用set的特性过滤
            Set<Integer> couponIdSet = new HashSet();
            for (OrderDTO orderDTO : orderDTOList) {

                List<GoodsCouponPrice> couponList = orderDTO.getGoodsCouponPrices();
                if (couponList != null) {
                    for (GoodsCouponPrice goodsCouponPrice : couponList) {
                        couponIdSet.add(goodsCouponPrice.getMemberCouponId());
                    }
                }
            }

            for (Integer id : couponIdSet) {
                //使用优惠券
                this.memberClient.usedCoupon(id, "");
                //查询该使用了的优惠券
                MemberCoupon memberCoupon = this.memberClient.getModel(tradeVO.getMemberId(), id);
                //修改店铺已经使用优惠券数量
                this.couponManager.addUsedNum(memberCoupon.getCouponId());
            }

            if (logger.isDebugEnabled()) {
                logger.debug("更改优惠券的状态完成");
            }

        } catch (Exception e) {
            logger.error("更改优惠券的状态出错", e);
        }
    }
}
