package com.enation.app.javashop.consumer.shop.trade.consumer;

import com.enation.app.javashop.consumer.core.event.OrderStatusChangeEvent;
import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;
import com.enation.app.javashop.core.goods.model.enums.QuantityType;
import com.enation.app.javashop.core.promotion.fulldiscount.model.dos.FullDiscountGiftDO;
import com.enation.app.javashop.core.promotion.fulldiscount.service.FullDiscountGiftManager;
import com.enation.app.javashop.core.trade.order.model.enums.OrderMetaKeyEnum;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.order.service.OrderMetaManager;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单取消时增加订单赠品的可用库存
 * @author Snow create in 2018/5/21
 * @version v2.0
 * @since v7.0.0
 */
@Component
public class OrderGiftStoreConsumer implements OrderStatusChangeEvent{

    @Autowired
    private FullDiscountGiftManager fullDiscountGiftManager;

    @Autowired
    private OrderMetaManager orderMetaManager;

    @Override
    public void orderChange(OrderStatusChangeMsg orderMessage) {

        //已确认状态减少赠品可用库存
        if(orderMessage.getNewStatus().name().equals(OrderStatusEnum.CONFIRM.name())){

            List<FullDiscountGiftDO> giftDOList = this.getList(orderMessage);
            this.fullDiscountGiftManager.reduceGiftQuantity(giftDOList, QuantityType.enable);
        }

        // 发货减少赠品真实库存
        if (orderMessage.getNewStatus().name().equals(OrderStatusEnum.SHIPPED.name())) {

            List<FullDiscountGiftDO> giftDOList = this.getList(orderMessage);
            this.fullDiscountGiftManager.reduceGiftQuantity(giftDOList, QuantityType.actual);
        }

        //取消状态增加赠品可用库存
        if(orderMessage.getNewStatus().name().equals(OrderStatusEnum.CANCELLED.name())){

            List<FullDiscountGiftDO> giftDOList = this.getList(orderMessage);
            this.fullDiscountGiftManager.addGiftEnableQuantity(giftDOList);
        }

    }

    /**
     * 查询赠品信息
     * @param orderMessage
     * @return
     */
    private List<FullDiscountGiftDO> getList(OrderStatusChangeMsg orderMessage){

        String fullDiscountGiftJson = this.orderMetaManager.getMetaValue(orderMessage.getOrderDO().getSn(), OrderMetaKeyEnum.GIFT);
        if(StringUtil.isEmpty(fullDiscountGiftJson)){
            return new ArrayList<>();
        }
        List<FullDiscountGiftDO> giftDOList = JsonUtil.jsonToList(fullDiscountGiftJson, FullDiscountGiftDO.class);

        return giftDOList;
    }
}
