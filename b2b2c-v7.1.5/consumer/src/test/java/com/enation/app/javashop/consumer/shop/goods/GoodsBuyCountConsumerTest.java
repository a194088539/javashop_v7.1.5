package com.enation.app.javashop.consumer.shop.goods;

import com.enation.app.javashop.consumer.shop.goods.GoodsBuyCountConsumer;
import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;
import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.order.model.vo.OrderSkuVO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.constraints.AssertTrue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author fk
 * @version v1.0
 * @Description: 商品购买数量消费者
 * @date 2018/6/26 9:59
 * @since v7.0.0
 */
public class GoodsBuyCountConsumerTest extends BaseTest {

    @Autowired
    private GoodsBuyCountConsumer goodsBuyCountConsumer;
    @Autowired
    private DaoSupport daoSupport;

    @Test
    public void testEditBuyCount() {

        //添加一个商品
        GoodsDO goods = new GoodsDO();
        goods.setBuyCount(0);
        this.daoSupport.insert(goods);
        Integer goodsId = this.daoSupport.getLastId("");

        OrderStatusChangeMsg orderMessage = new OrderStatusChangeMsg();
        orderMessage.setNewStatus(OrderStatusEnum.ROG);
        OrderDO order = new OrderDO();
        List<OrderSkuVO> skuList = new ArrayList<>();
        OrderSkuVO sku = new OrderSkuVO();
        sku.setGoodsId(goodsId);
        sku.setNum(1);
        skuList.add(sku);
        order.setItemsJson(JsonUtil.objectToJson(skuList));
        orderMessage.setOrderDO(order);

        goodsBuyCountConsumer.orderChange(orderMessage);

        //查询这个商品的购买数量，是1
        Map map = this.daoSupport.queryForMap("select buy_count from es_goods where goods_id = ?", goodsId);

        Assert.assertTrue(map.get("buy_count").toString().equals("1"));

    }


}
