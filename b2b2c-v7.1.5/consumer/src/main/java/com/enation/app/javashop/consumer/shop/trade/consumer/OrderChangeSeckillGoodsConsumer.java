package com.enation.app.javashop.consumer.shop.trade.consumer;

import com.enation.app.javashop.consumer.core.event.OrderStatusChangeEvent;
import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderOutStatusEnum;
import com.enation.app.javashop.core.trade.order.model.enums.OrderOutTypeEnum;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.order.service.OrderOutStatusManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单付款扣减限时抢购商品库存
 *
 * @author Snow create in 2018/7/12
 * @version v2.0
 * @since v7.0.0
 */
@Component
public class OrderChangeSeckillGoodsConsumer implements OrderStatusChangeEvent {

    @Autowired
    private OrderOutStatusManager orderOutStatusManager;

    @Override
    public void orderChange(OrderStatusChangeMsg orderMessage) {

        OrderDO orderDO = orderMessage.getOrderDO();
        //如果订单已确认
        if (orderMessage.getNewStatus().name().equals(OrderStatusEnum.CONFIRM.name())) {

            this.orderOutStatusManager.edit(orderDO.getSn(), OrderOutTypeEnum.SECKILL_GOODS, OrderOutStatusEnum.SUCCESS);
            return;
        }

        //出库失败
        if(orderMessage.getNewStatus().name().equals(OrderStatusEnum.INTODB_ERROR.name())){
            this.orderOutStatusManager.edit(orderDO.getSn(), OrderOutTypeEnum.SECKILL_GOODS, OrderOutStatusEnum.FAIL);
        }


    }

}
