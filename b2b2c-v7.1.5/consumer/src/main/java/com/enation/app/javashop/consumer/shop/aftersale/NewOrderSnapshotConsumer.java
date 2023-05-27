package com.enation.app.javashop.consumer.shop.aftersale;

import com.enation.app.javashop.consumer.core.event.ASNewOrderEvent;
import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.snapshot.service.GoodsSnapshotManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 新订单生成商品交易快照
 * 针对的是用户申请换货和补发商品的售后服务，商家审核通过后要生成新订单
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-23
 */
@Component
public class NewOrderSnapshotConsumer implements ASNewOrderEvent {

    @Autowired
    private GoodsSnapshotManager goodsSnapshotManager;

    @Override
    public void orderChange(OrderStatusChangeMsg orderStatusChangeMsg) {
        if(orderStatusChangeMsg.getNewStatus().equals(OrderStatusEnum.PAID_OFF)){
            OrderDO orderDO = orderStatusChangeMsg.getOrderDO();
            if (orderDO == null) {
                return;
            }

            this.goodsSnapshotManager.add(orderDO);
        }
    }
}
