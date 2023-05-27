package com.enation.app.javashop.consumer.shop.goodssearch;

import com.enation.app.javashop.consumer.core.event.GoodsWordsChangeEvent;
import com.enation.app.javashop.core.client.goods.GoodsWordsClient;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author liuyulei
 * @version 1.0
 * @Description: 变更商品提示词中商品数量
 * @date 2019/6/13 17:18
 * @since v7.0
 */
@Service
public class GoodsWordsChangeConsumer implements GoodsWordsChangeEvent {

    @Autowired
    private GoodsWordsClient goodsWordsClient;


    @Override
    public void wordsChange(String words) {

        if(!StringUtil.isEmpty(words)){
            this.goodsWordsClient.updateGoodsNum(words);
        }
    }
}
