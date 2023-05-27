package com.enation.app.javashop.consumer.shop.pagecreate;

import com.enation.app.javashop.consumer.core.event.GoodsChangeEvent;
import com.enation.app.javashop.consumer.shop.pagecreate.service.PageCreator;
import com.enation.app.javashop.core.base.message.GoodsChangeMsg;
import com.enation.app.javashop.core.pagecreate.model.PageCreatePrefixEnum;
import com.enation.app.javashop.core.payment.model.enums.ClientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 商品页面生成
 *
 * @author zh
 * @version v1.0
 * @since v6.4.0 2017年8月29日 下午3:40:14
 */
@Component
public class GoodsChangeConsumer implements GoodsChangeEvent {

    @Autowired
    private PageCreator pageCreator;


    /**
     * 生成商品静态页
     *
     * @param goodsChangeMsg 商品变化对象
     */
    @Override
    public void goodsChange(GoodsChangeMsg goodsChangeMsg) {

        try {

            Integer[] goodsIds = goodsChangeMsg.getGoodsIds();


            for (int i = 0; i < goodsIds.length; i++) {
                String pageName = PageCreatePrefixEnum.GOODS.getHandlerGoods(goodsIds[i]);
                /** 生成静态页面 */
                pageCreator.createOne(pageName, ClientType.PC.name(), "/" + ClientType.PC.name() + pageName);
                pageCreator.createOne(pageName, ClientType.WAP.name(), "/" + ClientType.WAP.name() + pageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
