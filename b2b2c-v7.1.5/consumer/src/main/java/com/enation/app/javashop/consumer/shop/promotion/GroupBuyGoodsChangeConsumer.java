package com.enation.app.javashop.consumer.shop.promotion;

import com.enation.app.javashop.consumer.core.event.GoodsChangeEvent;
import com.enation.app.javashop.core.base.message.GoodsChangeMsg;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyGoodsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
* @author liuyulei
 * @version 1.0
 * @Description:  修改团购商品信息
 * @date 2019/5/15 9:50
 * @since v7.0
 */
@Component
public class GroupBuyGoodsChangeConsumer implements GoodsChangeEvent {

    @Autowired
    private GroupbuyGoodsManager groupbuyGoodsManager;

    @Override
    public void goodsChange(GoodsChangeMsg goodsChangeMsg) {
        if (GoodsChangeMsg.UPDATE_OPERATION == goodsChangeMsg.getOperationType()) {
            this.groupbuyGoodsManager.updateGoodsInfo(goodsChangeMsg.getGoodsIds());
        }
    }
}
