package com.enation.app.javashop.consumer.shop.promotion;

import com.enation.app.javashop.consumer.core.event.GoodsChangeEvent;
import com.enation.app.javashop.core.base.message.GoodsChangeMsg;
import com.enation.app.javashop.core.client.trade.PintuanGoodsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 拼团索引同步消费者
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-03-08
 */
@Component
public class PinTuanIndexConsumer implements GoodsChangeEvent {


    @Autowired
    private PintuanGoodsClient pintuanGoodsClient;


    @Override
    public void goodsChange(GoodsChangeMsg goodsChangeMsg) {

       Integer[] goodsIdAr= goodsChangeMsg.getGoodsIds();
        int operationType = goodsChangeMsg.getOperationType();

        //修改商品：同步此商品的所有sku
        if( GoodsChangeMsg.UPDATE_OPERATION==operationType){
            for (Integer goodsId : goodsIdAr) {
                pintuanGoodsClient.syncIndexByGoodsId(goodsId);
            }

        }

        //删除商品 则删除这个商品的所有sku的索引
        if( GoodsChangeMsg.INRECYCLE_OPERATION==operationType  ||GoodsChangeMsg.UNDER_OPERATION==operationType  ){
            for (Integer goodsId : goodsIdAr) {
                pintuanGoodsClient.deleteIndexByGoodsId(goodsId);
            }
        }


    }
}
