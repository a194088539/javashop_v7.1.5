package com.enation.app.javashop.consumer.shop.trade.consumer;

import com.enation.app.javashop.consumer.core.event.ASNewOrderEvent;
import com.enation.app.javashop.consumer.core.event.OrderStatusChangeEvent;
import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;
import com.enation.app.javashop.core.payment.model.dos.PaymentMethodDO;
import com.enation.app.javashop.core.payment.service.PaymentMethodManager;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dos.PayLog;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.order.model.enums.PayStatusEnum;
import com.enation.app.javashop.core.trade.order.service.PayLogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单支付后，修改付款单
 *
 * @author Snow create in 2018/7/23
 * @version v2.0
 * @since v7.0.0
 */
@Component
public class OrderPayLogConsumer implements OrderStatusChangeEvent, ASNewOrderEvent {

    @Autowired
    private PayLogManager payLogManager;

    @Autowired
    private PaymentMethodManager paymentMethodManager;

    @Override
    public void orderChange(OrderStatusChangeMsg orderMessage) {

        //订单已付款
        if(orderMessage.getNewStatus().name().equals(OrderStatusEnum.PAID_OFF.name())){

            OrderDO orderDO = orderMessage.getOrderDO();
            PayLog payLog = this.payLogManager.getModel(orderDO.getSn());

            // 查询支付方式
            PaymentMethodDO paymentMethod = this.paymentMethodManager.getByPluginId(orderDO.getPaymentPluginId());
            if( paymentMethod == null ){
                paymentMethod = new PaymentMethodDO();
                paymentMethod.setMethodName("管理员确认收款");
            }

            payLog.setPayType(paymentMethod.getMethodName());
            payLog.setPayTime(orderDO.getPaymentTime());
            payLog.setPayMoney(orderDO.getPayMoney());
            payLog.setPayStatus(PayStatusEnum.PAY_YES.name());
            payLog.setPayOrderNo(orderDO.getPayOrderNo());

            this.payLogManager.edit(payLog,payLog.getPayLogId());
        }

    }
}
