package com.enation.app.javashop.consumer.shop.aftersale;

import com.enation.app.javashop.consumer.core.event.ASNewOrderEvent;
import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderMetaDO;
import com.enation.app.javashop.core.trade.order.model.dos.PayLog;
import com.enation.app.javashop.core.trade.order.model.enums.OrderMetaKeyEnum;
import com.enation.app.javashop.core.trade.order.model.enums.OrderServiceStatusEnum;
import com.enation.app.javashop.core.trade.order.model.enums.PayStatusEnum;
import com.enation.app.javashop.core.trade.order.service.OrderMetaManager;
import com.enation.app.javashop.core.trade.order.service.PayLogManager;
import com.enation.app.javashop.core.trade.order.service.TradeSnCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 新订单其他信息入库
 * 针对的是用户申请换货和补发商品的售后服务，商家审核通过后要生成新订单
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-23
 */
@Component
public class NewOrderMetaConsumer implements ASNewOrderEvent {

    @Autowired
    private OrderMetaManager orderMetaManager;

    @Autowired
    private PayLogManager payLogManager;

    @Autowired
    private TradeSnCreator tradeSnCreator;

    @Override
    public void orderChange(OrderStatusChangeMsg orderStatusChangeMsg) {
        OrderDO orderDO = orderStatusChangeMsg.getOrderDO();
        if (orderDO == null) {
            return;
        }

        OrderMetaDO orderMetaDO = new OrderMetaDO();
        orderMetaDO.setOrderSn(orderDO.getSn());

        //记录返现金额
        orderMetaDO.setMetaKey(OrderMetaKeyEnum.CASH_BACK.name());
        orderMetaDO.setMetaValue("0.00");
        this.orderMetaManager.add(orderMetaDO);

        //记录使用优惠券的金额
        orderMetaDO.setMetaKey(OrderMetaKeyEnum.COUPON_PRICE.name());
        orderMetaDO.setMetaValue("[]");
        this.orderMetaManager.add(orderMetaDO);

        //记录满减金额
        orderMetaDO.setMetaKey(OrderMetaKeyEnum.FULL_MINUS.name());
        orderMetaDO.setMetaValue("0.00");
        this.orderMetaManager.add(orderMetaDO);

        //记录赠送的积分
        orderMetaDO.setMetaKey(OrderMetaKeyEnum.GIFT_POINT.name());
        orderMetaDO.setMetaValue("");
        orderMetaDO.setStatus(OrderServiceStatusEnum.NOT_APPLY.value());
        this.orderMetaManager.add(orderMetaDO);

        //记录赠送的优惠券
        orderMetaDO.setMetaKey(OrderMetaKeyEnum.COUPON.name());
        orderMetaDO.setMetaValue("[]");
        orderMetaDO.setStatus(OrderServiceStatusEnum.NOT_APPLY.value());
        this.orderMetaManager.add(orderMetaDO);

        //记录赠送的赠品
        orderMetaDO.setMetaKey(OrderMetaKeyEnum.GIFT.name());
        orderMetaDO.setMetaValue("[]");
        orderMetaDO.setStatus(OrderServiceStatusEnum.NOT_APPLY.value());
        this.orderMetaManager.add(orderMetaDO);

        //记录付款日志
        PayLog payLog = new PayLog();
        payLog.setPayLogSn(tradeSnCreator.generatePayLogSn());
        payLog.setOrderSn(orderDO.getSn());
        payLog.setPayMemberName(orderDO.getMemberName());
        payLog.setPayStatus(PayStatusEnum.PAY_NO.name());
        payLog.setPayWay(orderDO.getPaymentType());
        this.payLogManager.add(payLog);
    }
}
