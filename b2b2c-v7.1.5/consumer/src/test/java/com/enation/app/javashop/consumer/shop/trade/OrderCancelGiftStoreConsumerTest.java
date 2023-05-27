package com.enation.app.javashop.consumer.shop.trade;

import com.enation.app.javashop.consumer.shop.trade.consumer.OrderGiftStoreConsumer;
import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;
import com.enation.app.javashop.core.promotion.fulldiscount.model.dos.FullDiscountGiftDO;
import com.enation.app.javashop.core.promotion.fulldiscount.service.FullDiscountGiftManager;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderMetaDO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderMetaKeyEnum;
import com.enation.app.javashop.core.trade.order.model.enums.OrderServiceStatusEnum;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.order.service.OrderMetaManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单取消时增加订单赠品的可用库存测试
 *
 * @author Snow create in 2018/6/29
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
public class OrderCancelGiftStoreConsumerTest extends BaseTest {

    @Autowired
    private OrderGiftStoreConsumer orderCancelGiftStoreConsumer;

    @Autowired
    private FullDiscountGiftManager fullDiscountGiftManager;

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private OrderMetaManager orderMetaManager;

    private OrderStatusChangeMsg changeMsg;

    private FullDiscountGiftDO giftDO;


    @Before
    public void testData(){

        int sellerId = 3;
        int memberId = 1;

        List<FullDiscountGiftDO> giftDOList = new ArrayList<>();
        giftDO = new FullDiscountGiftDO();
        giftDO.setSellerId(3);
        giftDO.setGiftName("测试赠44545");
        giftDO.setGiftPrice(10.9);
        giftDO.setActualStore(100);
        giftDO.setEnableStore(100);

        this.daoSupport.insert(giftDO);
        int id = this.daoSupport.getLastId("es_full_discount_gift");
        giftDO.setGiftId(id);
        giftDOList.add(giftDO);

        OrderDO orderDO = new OrderDO();
        orderDO.setSn(DateUtil.getDateline()+"");

        OrderMetaDO giftMeta = new OrderMetaDO();
        giftMeta.setMetaKey(OrderMetaKeyEnum.GIFT.name());
        giftMeta.setMetaValue(JsonUtil.objectToJson(giftDOList));
        giftMeta.setOrderSn(orderDO.getSn());
        giftMeta.setStatus(OrderServiceStatusEnum.NOT_APPLY.name());
        this.orderMetaManager.add(giftMeta);

        changeMsg = new OrderStatusChangeMsg();
        changeMsg.setOldStatus(OrderStatusEnum.CONFIRM);
        changeMsg.setNewStatus(OrderStatusEnum.CANCELLED);

        changeMsg.setOrderDO(orderDO);
    }

    @Test
    public void testAdd() throws Exception {

        this.orderCancelGiftStoreConsumer.orderChange(changeMsg);
        FullDiscountGiftDO giftDO2 = this.fullDiscountGiftManager.getModel(giftDO.getGiftId());
        Integer store = 101;
        Assert.assertEquals(giftDO2.getEnableStore(),store);

    }


    /**
     * 清除数据
     */
    @After
    public void clear() {
        this.daoSupport.execute("delete from es_order_meta where order_sn = ?",changeMsg.getOrderDO().getSn());
        this.daoSupport.execute("delete from es_full_discount_gift where gift_id=?",giftDO.getGiftId());
    }


}
