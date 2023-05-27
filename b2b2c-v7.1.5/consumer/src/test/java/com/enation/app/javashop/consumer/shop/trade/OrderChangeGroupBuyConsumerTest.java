package com.enation.app.javashop.consumer.shop.trade;

import com.enation.app.javashop.consumer.shop.trade.consumer.OrderChangeGroupBuyConsumer;
import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;
import com.enation.app.javashop.core.promotion.PromotionErrorCode;
import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyGoodsDO;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyGoodsManager;
import com.enation.app.javashop.core.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.javashop.core.trade.cart.model.vo.CartPromotionVo;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.order.model.vo.OrderSkuVO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ServiceException;
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

import java.util.ArrayList;
import java.util.List;

/**
 * 团购商品库存扣减测试
 * @author Snow create in 2018/7/2
 * @version v2.0
 * @since v7.0.0
 */
public class OrderChangeGroupBuyConsumerTest extends BaseTest {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    private OrderStatusChangeMsg changeMsg;

    @Autowired
    private OrderChangeGroupBuyConsumer consumer;

    @Autowired
    private GroupbuyGoodsManager groupbuyGoodsManager;

    private GroupbuyGoodsDO goodsDO;

    @Before
    public void testData(){

        int memberId = 99;

        goodsDO = new GroupbuyGoodsDO();
        goodsDO.setGoodsId(1);
        goodsDO.setBuyNum(2);
        goodsDO.setGoodsNum(100);
        this.daoSupport.insert(goodsDO);
        int id =this.daoSupport.getLastId("es_groupbuy_goods");
        goodsDO.setGbId(id);

        changeMsg = new OrderStatusChangeMsg();
        changeMsg.setOldStatus(OrderStatusEnum.NEW);
        changeMsg.setNewStatus(OrderStatusEnum.CONFIRM);

        //模拟订单
        OrderDO orderDO = new OrderDO();
        orderDO.setSn(DateUtil.getDateline()+"");
        orderDO.setMemberId(memberId);

        //模拟订单中的商品项
        List<OrderSkuVO> skuVOList = new ArrayList<>();
        OrderSkuVO skuVO = new OrderSkuVO();
        skuVO.setGoodsId(goodsDO.getGoodsId());
        skuVO.setNum(1);

        //模拟商品参与的活动
        List<CartPromotionVo> singleList = new ArrayList<>();
        CartPromotionVo promotionGoodsVO = new CartPromotionVo();
        promotionGoodsVO.setPromotionType(PromotionTypeEnum.GROUPBUY.name());
        promotionGoodsVO.setActivityId(goodsDO.getGbId());
        promotionGoodsVO.setIsCheck(1);

        singleList.add(promotionGoodsVO);
        skuVO.setSingleList(singleList);

        skuVOList.add(skuVO);
        orderDO.setItemsJson(JsonUtil.objectToJson(skuVOList));

        changeMsg.setOrderDO(orderDO);

    }


    @Test
    public void test(){

        this.consumer.orderChange(changeMsg);
        GroupbuyGoodsDO groupbuyGoodsDO = this.groupbuyGoodsManager.getModel(goodsDO.getGbId());
        Integer num  = 99;
        Assert.assertEquals(groupbuyGoodsDO.getGoodsNum(),num);

    }


    @Test
    public void testAdd2() throws Exception {

        //60个线程并发
        //每个sku减一个，一个商品1个sku
        TestRunnable[] trs = new TestRunnable[60];

        //前30个线程扣减
        for (int i = 0; i < 60; i++) {
            trs[i] = new QuantityTask(i);
        }

        MultiThreadedTestRunner runner = new MultiThreadedTestRunner(trs);
        try {
            runner.runTestRunnables();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        GroupbuyGoodsDO groupbuyGoodsDO = this.groupbuyGoodsManager.getModel(goodsDO.getGbId());
        Integer num  = 40;
        Assert.assertEquals(groupbuyGoodsDO.getGoodsNum(),num);
    }


    @Test
    public void testAdd3() throws Exception {
        //101个线程并发
        //每个sku减一个，一个商品1个sku
        TestRunnable[] trs = new TestRunnable[101];

        //前200个线程扣减
        for (int i = 0; i < 101; i++) {
            trs[i] = new QuantityTask(i);
        }

        MultiThreadedTestRunner runner = new MultiThreadedTestRunner(trs);
        try {
            runner.runTestRunnables();
        } catch (Throwable e) {
            throw new ServiceException(PromotionErrorCode.E403.code(),"团购商品库存不足");
        }

        GroupbuyGoodsDO groupbuyGoodsDO = this.groupbuyGoodsManager.getModel(goodsDO.getGbId());
        Integer num  = 0;
        Assert.assertEquals(num,groupbuyGoodsDO.getGoodsNum());
    }


    /**
     * 清除数据
     */
    @After
    public void clear() {

        this.daoSupport.execute("delete from es_groupbuy_goods where gb_id = ?",goodsDO.getGbId());
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
