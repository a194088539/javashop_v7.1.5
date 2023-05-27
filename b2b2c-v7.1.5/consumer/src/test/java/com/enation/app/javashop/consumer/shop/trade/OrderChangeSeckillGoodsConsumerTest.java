package com.enation.app.javashop.consumer.shop.trade;

import com.enation.app.javashop.consumer.shop.trade.consumer.OrderChangeSeckillGoodsConsumer;
import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;
import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.promotion.seckill.model.dos.SeckillApplyDO;
import com.enation.app.javashop.core.promotion.seckill.model.enums.SeckillGoodsApplyStatusEnum;
import com.enation.app.javashop.core.promotion.seckill.service.SeckillGoodsManager;
import com.enation.app.javashop.core.promotion.seckill.service.SeckillManager;
import com.enation.app.javashop.core.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.javashop.core.promotion.tool.support.PromotionCacheKeys;
import com.enation.app.javashop.core.trade.cart.model.vo.CartPromotionVo;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.order.model.vo.OrderSkuVO;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * 订单付款后扣减
 *
 * @author Snow create in 2018/7/16
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
public class OrderChangeSeckillGoodsConsumerTest extends BaseTest {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private OrderChangeSeckillGoodsConsumer consumer;

    @Autowired
    private SeckillGoodsManager seckillApplyManager;

    @Autowired
    private SeckillManager seckillManager;

    @Autowired
    private Cache cache;

    @MockBean
    private GoodsClient goodsClient;

    private SeckillApplyDO seckillApplyDO;

    private OrderStatusChangeMsg changeMsg;


    @Before
    public void testData() {

        int seckillId = 10;
        int goodsId = 1;

        CacheGoods goods = new CacheGoods();
        goods.setGoodsId(goodsId);
        goods.setGoodsName("测试商品");
        goods.setThumbnail("path");
        goods.setPrice(10.0);
        when (goodsClient.getFromCache(goods.getGoodsId())).thenReturn(goods);

        String redisKey = PromotionCacheKeys.getSeckillKey(DateUtil.toString(DateUtil.getDateline(), "yyyyMMdd"));
        this.cache.remove(redisKey);

        int memberId = 99;

        seckillApplyDO = new SeckillApplyDO();
        seckillApplyDO.setSellerId(3);
        seckillApplyDO.setSeckillId(seckillId);
        seckillApplyDO.setSoldQuantity(100);
        seckillApplyDO.setTimeLine(1);
        seckillApplyDO.setGoodsId(goodsId);
        seckillApplyDO.setStartDay(DateUtil.startOfTodDay());
        seckillApplyDO.setStatus(SeckillGoodsApplyStatusEnum.PASS.name());
        seckillApplyDO.setGoodsName("测试商品");
        this.daoSupport.insert(seckillApplyDO);
        int  applyId = this.daoSupport.getLastId("es_seckill_apply");
        seckillApplyDO.setApplyId(applyId);

        changeMsg = new OrderStatusChangeMsg();
        changeMsg.setOldStatus(OrderStatusEnum.CONFIRM);
        changeMsg.setNewStatus(OrderStatusEnum.PAID_OFF);

        //模拟订单
        OrderDO orderDO = new OrderDO();
        orderDO.setSn(DateUtil.getDateline()+"");
        orderDO.setMemberId(memberId);

        //模拟订单中的商品项
        List<OrderSkuVO> skuVOList = new ArrayList<>();
        OrderSkuVO skuVO = new OrderSkuVO();
        skuVO.setGoodsId(seckillApplyDO.getGoodsId());
        skuVO.setNum(1);

        //模拟商品参与的活动
        List<CartPromotionVo> singleList = new ArrayList<>();
        CartPromotionVo promotionGoodsVO = new CartPromotionVo();
        promotionGoodsVO.setPromotionType(PromotionTypeEnum.SECKILL.name());
        promotionGoodsVO.setActivityId(seckillApplyDO.getSeckillId());
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
        SeckillApplyDO applyDO = this.seckillApplyManager.getModel(seckillApplyDO.getApplyId());
        Integer soldQuantity = 99;
        Assert.assertEquals(applyDO.getSoldQuantity(),soldQuantity);
    }


//    @Test
//    public void testAdd2() throws Exception {
//
//        //60个线程并发
//        //每个sku减一个，一个商品1个sku
//        TestRunnable[] trs = new TestRunnable[60];
//
//        //前30个线程扣减
//        for (int i = 0; i < 60; i++) {
//            trs[i] = new QuantityTask(i);
//        }
//
//        MultiThreadedTestRunner runner = new MultiThreadedTestRunner(trs);
//        try {
//            runner.runTestRunnables();
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//
//        SeckillApplyDO applyDO = this.seckillApplyManager.getModel(seckillApplyDO.getApplyId());
//        System.out.println(applyDO.toString());
//
//    }
//
//    /**
//     * 扣减库存任务
//     */
//    class QuantityTask extends TestRunnable {
//        int i = 0;
//
//        public QuantityTask(int i) {
//            this.i = i;
//        }
//
//        @Override
//        public void runTest() throws Throwable {
//            consumer.orderChange(changeMsg);
//        }
//
//    }
}
