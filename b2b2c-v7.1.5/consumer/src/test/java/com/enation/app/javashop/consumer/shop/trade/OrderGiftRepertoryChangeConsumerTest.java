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
import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;
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
 * 订单发货扣减赠品测试
 *
 * @author Snow create in 2018/7/2
 * @version v2.0
 * @since v7.0.0
 */
public class OrderGiftRepertoryChangeConsumerTest extends BaseTest {


    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    private OrderStatusChangeMsg changeMsg;

    @Autowired
    private OrderMetaManager orderMetaManager;
    @Autowired
    private OrderGiftStoreConsumer consumer;

    @Autowired
    private FullDiscountGiftManager fullDiscountGiftManager;

    private FullDiscountGiftDO giftDO;

    private OrderDO orderDO;

    @Before
    public void testData(){

        int memberId = 99;
        int sellerId = 3;

        //赠品
        List<FullDiscountGiftDO> giftVOList = new ArrayList<>();
        giftDO = new FullDiscountGiftDO();
        giftDO.setActualStore(100);
        giftDO.setEnableStore(100);
        giftDO.setGiftPrice(100.0);
        giftDO.setGiftName("测试赠品22");
        giftDO.setSellerId(sellerId);

        this.daoSupport.insert(giftDO);
        int id =this.daoSupport.getLastId("es_full_discount_gift");
        giftDO.setGiftId(id);

        giftVOList.add(giftDO);

        changeMsg = new OrderStatusChangeMsg();
        changeMsg.setOldStatus(OrderStatusEnum.PAID_OFF);
        changeMsg.setNewStatus(OrderStatusEnum.SHIPPED);

        orderDO = new OrderDO();
        orderDO.setSn(DateUtil.getDateline()+"");
        orderDO.setMemberId(memberId);

        OrderMetaDO giftMeta = new OrderMetaDO();
        giftMeta.setMetaKey(OrderMetaKeyEnum.GIFT.name());
        giftMeta.setMetaValue(JsonUtil.objectToJson(giftVOList));
        giftMeta.setOrderSn(orderDO.getSn());
        giftMeta.setStatus(OrderServiceStatusEnum.NOT_APPLY.name());
        this.orderMetaManager.add(giftMeta);

        changeMsg.setOrderDO(orderDO);

    }


    @Test
    @Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
    public void test(){
        this.consumer.orderChange(changeMsg);
        FullDiscountGiftDO giftDO2 = this.fullDiscountGiftManager.getModel(giftDO.getGiftId());
        Integer store = 99;
        Assert.assertEquals(giftDO2.getEnableStore(),store);
    }


    @Test
    public void testAdd2() throws Exception {

        //60个线程并发
        //每个sku减一个，一个商品1个sku
        TestRunnable[] trs = new TestRunnable[200];

        //前30个线程扣减
        for (int i = 0; i < 200; i++) {
            trs[i] = new QuantityTask(i);
        }

        MultiThreadedTestRunner runner = new MultiThreadedTestRunner(trs);
        try {
            runner.runTestRunnables();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        FullDiscountGiftDO giftDO2 = this.fullDiscountGiftManager.getModel(giftDO.getGiftId());
        Integer store = 0;
        Assert.assertEquals(giftDO2.getEnableStore(),store);
    }


    @Test
    public void testAdd3() throws Exception {

        //40个线程并发
        //每个sku减一个，一个商品1个sku
        TestRunnable[] trs = new TestRunnable[40];

        //前40个线程扣减
        for (int i = 0; i < 40; i++) {
            trs[i] = new QuantityTask(i);
        }

        MultiThreadedTestRunner runner = new MultiThreadedTestRunner(trs);
        try {
            runner.runTestRunnables();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        FullDiscountGiftDO giftDO2 = this.fullDiscountGiftManager.getModel(giftDO.getGiftId());
        Integer store = 60;
        Assert.assertEquals(giftDO2.getEnableStore(),store);
    }



    /**
     * 清除数据
     */
    @After
    public void clear() {
        this.daoSupport.execute("delete from es_full_discount_gift where gift_id = ?",giftDO.getGiftId());
        this.daoSupport.execute("delete from es_order_meta where order_sn = ?",orderDO.getSn());

    }


    /**
     * 扣减库存任务
     */
    class QuantityTask extends TestRunnable {
        int i = 0;

        public QuantityTask(int i) {
            this.i = i;
        }

        @Override
        public void runTest() throws Throwable {
            consumer.orderChange(changeMsg);
        }

    }

}
