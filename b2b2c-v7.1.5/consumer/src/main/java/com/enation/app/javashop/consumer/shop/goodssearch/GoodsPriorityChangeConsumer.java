package com.enation.app.javashop.consumer.shop.goodssearch;

import com.enation.app.javashop.consumer.core.event.GoodsPriorityChangeEvent;
import com.enation.app.javashop.core.base.message.GoodsChangeMsg;
import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.goodssearch.service.GoodsIndexManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
* @author liuyulei
 * @version 1.0
 * @Description: 商品优先级变更修改索引
 * @date 2019/6/13 17:18
 * @since v7.0
 */
@Service
public class GoodsPriorityChangeConsumer implements GoodsPriorityChangeEvent {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private GoodsIndexManager goodsIndexManager;

    @Override
    public void priorityChange(GoodsChangeMsg goodsChangeMsg) {

        if(goodsChangeMsg.getOperationType() == GoodsChangeMsg.GOODS_PRIORITY_CHANGE){
            Integer[] goodsIds = goodsChangeMsg.getGoodsIds();

            List<Map<String, Object>> list = goodsClient.getGoodsAndParams(goodsIds);

            if (list != null) {
                for (Map<String, Object> map : list) {
                    goodsIndexManager.updateIndex(map);
                }
            }
        }
    }
}
