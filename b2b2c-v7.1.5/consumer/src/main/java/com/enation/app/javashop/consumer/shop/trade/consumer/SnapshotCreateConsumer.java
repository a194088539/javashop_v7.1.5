package com.enation.app.javashop.consumer.shop.trade.consumer;

import com.enation.app.javashop.consumer.core.event.OrderStatusChangeEvent;
import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.snapshot.service.GoodsSnapshotManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 生成商品交易快照
 * @author Snow create in 2018/5/22
 * @version v2.0
 * @since v7.0.0
 */
@Component
public class SnapshotCreateConsumer implements OrderStatusChangeEvent {

    @Autowired
    private GoodsSnapshotManager goodsSnapshotManager;

    @Override
    public void orderChange(OrderStatusChangeMsg orderMessage) {

        if(orderMessage.getNewStatus().equals(OrderStatusEnum.NEW)){
            OrderDO orderDO = orderMessage.getOrderDO();

            this.goodsSnapshotManager.add(orderDO);
        }
    }


}
