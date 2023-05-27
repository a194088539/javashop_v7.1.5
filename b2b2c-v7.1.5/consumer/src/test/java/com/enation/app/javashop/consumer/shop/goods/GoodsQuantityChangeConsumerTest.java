package com.enation.app.javashop.consumer.shop.goods;

import com.enation.app.javashop.core.aftersale.model.dos.RefundDO;
import com.enation.app.javashop.core.aftersale.service.AfterSaleManager;
import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;
import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.model.dos.GoodsSkuDO;
import com.enation.app.javashop.core.goods.service.GoodsQueryManager;
import com.enation.app.javashop.core.goods.service.GoodsSkuManager;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.order.model.enums.PayStatusEnum;
import com.enation.app.javashop.core.trade.order.model.vo.OrderSkuVO;
import com.enation.app.javashop.core.trade.order.model.vo.TradeVO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fk
 * @version v1.0
 * @Description: 商品库存增加/扣减 单元测试
 * @date 2018/6/26 10:46
 * @since v7.0.0
 */
public class GoodsQuantityChangeConsumerTest extends BaseTest {

    @Autowired
    private GoodsQuantityChangeConsumer goodsQuantityChangeConsumer;
    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport tradeDaoSupport;

    @MockBean
    private AfterSaleManager afterSaleManager;
    @Autowired
    private GoodsQueryManager goodsQueryManager;
    private Integer goodsId;
    private Integer skuId;
    TradeVO tradeVO = null;

    @Autowired
    private GoodsQuantityChangeConsumer getGoodsQuantityChangeConsumer;
    @Autowired
    private GoodsSkuManager goodsSkuManager;

    private String ordersn = "123123123123123";

    @Before
    public void before() {
        //添加一个商品
        GoodsDO goods = new GoodsDO();
        goods.setBuyCount(0);
        goods.setQuantity(3);
        goods.setEnableQuantity(3);
        this.daoSupport.insert(goods);
        goodsId = this.daoSupport.getLastId("es_goods");
        //添加相应的sku
        GoodsSkuDO goodsSkuDO = new GoodsSkuDO();
        goodsSkuDO.setGoodsId(goodsId);
        goodsSkuDO.setQuantity(3);
        goodsSkuDO.setEnableQuantity(3);
        this.daoSupport.insert(goodsSkuDO);
        skuId = this.daoSupport.getLastId("es_goods_sku");

    }

    /**
     * 创建订单库存测试
     */
    @Test
    public void createOrderTest() throws Exception {
//        //添加对应的订单信息
//        List<CartSkuVO> cartSkuVOS = new ArrayList<>();
//        CartSkuVO cartSkuVO = new CartSkuVO();
//        cartSkuVO.setNum(2);
//        cartSkuVO.setSkuId(skuId);
//        cartSkuVO.setGoodsId(goodsId);
//        cartSkuVOS.add(cartSkuVO);
//
//        List<OrderDTO> orderDTOS = new ArrayList<>();
//        OrderDTO order = new OrderDTO();
//        order.setSkuList(cartSkuVOS);
//        order.setSn(ordersn);
//        orderDTOS.add(order);
//
//        tradeVO = new TradeVO();
//        tradeVO.setOrderList(orderDTOS);
//        tradeVO.setTradeSn("2018071111111111");
//        //下单测试扣减库存
//        tradeDaoSupport.execute("INSERT INTO es_order(sn,trade_sn,seller_id,seller_name,member_id,member_name,order_status,pay_status,ship_status,items_json,payment_type,comment_status) values('" + ordersn + "','2018071111111111','1732','测试店铺',2,'wangfeng1','CONFIRM','PAY_NO','SHIP_NO','[{\"seller_id\":1732,\"seller_name\":\"测试店铺\",\"goods_id\":11383,\"sku_id\":1909,\"sku_sn\":\"102112-1\",\"cat_id\":497,\"num\":2,\"goods_weight\":4.0,\"original_price\":0.0,\"purchase_price\":0.0,\"subtotal\":0.0,\"name\":\"赠品苹果pro\",\"goods_image\":\"http://javashop-statics.oss-cn-beijing.aliyuncs.com/test/null/A5C5C82115724103B98F163F037188C7.jpg_100x100\",\"spec_list\":[{\"spec_id\":1897,\"spec_value\":\"2cm\",\"seller_id\":null,\"spec_name\":\"长度\",\"sku_id\":null,\"big\":null,\"small\":null,\"thumbnail\":null,\"tiny\":null,\"spec_type\":0,\"spec_image\":\"\",\"spec_value_id\":3954}],\"point\":null,\"snapshot_id\":null,\"service_status\":\"NOT_APPLY\",\"single_list\":[],\"group_list\":[]}]','ONLINE','UNFINISHED')");
//        getGoodsQuantityChangeConsumer.onTradeIntoDb(tradeVO);
//        GoodsSkuDO goodsSkuDO = goodsSkuManager.getModel(skuId);
//        Assert.assertEquals(goodsSkuDO.getEnableQuantity().toString(), "1");
//        Assert.assertEquals(goodsSkuDO.getQuantity().toString(), "3");
//
//        GoodsDO goodsDO = goodsQueryManager.getModel(goodsId);
//        Assert.assertEquals(goodsDO.getEnableQuantity().toString(), "1");
//        Assert.assertEquals(goodsDO.getQuantity().toString(), "3");
//        //可用库存不足校验
//        getGoodsQuantityChangeConsumer.onTradeIntoDb(tradeVO);
//        goodsSkuDO = goodsSkuManager.getModel(skuId);
//        Assert.assertEquals(goodsSkuDO.getEnableQuantity().toString(), "1");
//        Assert.assertEquals(goodsSkuDO.getQuantity().toString(), "3");
//
//        goodsDO = goodsQueryManager.getModel(goodsId);
//        Assert.assertEquals(goodsDO.getEnableQuantity().toString(), "1");
//        Assert.assertEquals(goodsDO.getQuantity().toString(), "3");
    }

    /**
     * 付款前 取消订单测试
     *
     * @throws Exception
     */
    @Test
    public void cancelOrderTest() throws Exception {
        this.createOrderTest();
        OrderStatusChangeMsg orderStatusChangeMsg = new OrderStatusChangeMsg();
        orderStatusChangeMsg.setNewStatus(OrderStatusEnum.CANCELLED);
        OrderDO order = new OrderDO();
        order.setSn(ordersn);
        order.setMemberId(2);
        order.setPayStatus(PayStatusEnum.PAY_NO.name());
        List<OrderSkuVO> skuList = new ArrayList<>();
        OrderSkuVO sku = new OrderSkuVO();
        sku.setGoodsId(goodsId);
        sku.setNum(2);
        sku.setSkuId(skuId);
        skuList.add(sku);
        order.setItemsJson(JsonUtil.objectToJson(skuList));
        orderStatusChangeMsg.setOrderDO(order);
        goodsQuantityChangeConsumer.orderChange(orderStatusChangeMsg);

        GoodsSkuDO goodsSkuDO = goodsSkuManager.getModel(skuId);
        Assert.assertEquals(goodsSkuDO.getEnableQuantity().toString(), "3");
        Assert.assertEquals(goodsSkuDO.getQuantity().toString(), "3");

        GoodsDO goodsDO = goodsQueryManager.getModel(goodsId);
        Assert.assertEquals(goodsDO.getEnableQuantity().toString(), "3");
        Assert.assertEquals(goodsDO.getQuantity().toString(), "3");

    }


    /**
     * 订单发货测试库存扣减
     */
    @Test
    public void testOrderShip() throws Exception {
        this.createOrderTest();
        OrderStatusChangeMsg orderMessage = new OrderStatusChangeMsg();
        orderMessage.setNewStatus(OrderStatusEnum.SHIPPED);
        OrderDO order = new OrderDO();
        List<OrderSkuVO> skuList = new ArrayList<>();
        OrderSkuVO sku = new OrderSkuVO();
        sku.setGoodsId(goodsId);
        sku.setNum(2);
        sku.setSkuId(skuId);
        skuList.add(sku);
        order.setItemsJson(JsonUtil.objectToJson(skuList));
        orderMessage.setOrderDO(order);
        goodsQuantityChangeConsumer.orderChange(orderMessage);

        GoodsSkuDO goodsSkuDO = goodsSkuManager.getModel(skuId);
        Assert.assertEquals(goodsSkuDO.getEnableQuantity().toString(), "1");
        Assert.assertEquals(goodsSkuDO.getQuantity().toString(), "1");

        GoodsDO goodsDO = goodsQueryManager.getModel(goodsId);
        Assert.assertEquals(goodsDO.getEnableQuantity().toString(), "1");
        Assert.assertEquals(goodsDO.getQuantity().toString(), "1");
        //库存不足扣减
        goodsQuantityChangeConsumer.orderChange(orderMessage);

        goodsSkuDO = goodsSkuManager.getModel(skuId);
        Assert.assertEquals(goodsSkuDO.getEnableQuantity().toString(), "1");
        Assert.assertEquals(goodsSkuDO.getQuantity().toString(), "1");

        goodsDO = goodsQueryManager.getModel(goodsId);
        Assert.assertEquals(goodsDO.getEnableQuantity().toString(), "1");
        Assert.assertEquals(goodsDO.getQuantity().toString(), "1");
    }

    /**
     * 退款入库操作
     */
    @Test
    public void testRefundStockIn() throws Exception {
        this.createOrderTest();
        this.tradeDaoSupport.execute("update es_order set service_status='APPLY' where sn = " + ordersn + "");
        String sn = "00001";
        RefundDO refundDO = new RefundDO();
        refundDO.setOrderSn(ordersn);
        refundDO.setSn(sn);
//        refundDO.setRefuseType(RefuseTypeEnum.RETURN_MONEY.name());
//        RefundChangeMsg refundChangeMsg = new RefundChangeMsg(refundDO, RefundStatusEnum.PASS);

//        List<RefundGoodsDO> refundGoodsList = new ArrayList<>();
//        RefundGoodsDO refundGoods = new RefundGoodsDO();
//        refundGoods.setGoodsId(goodsId);
//        refundGoods.setSkuId(skuId);
//        refundGoods.setReturnNum(2);
//        refundGoodsList.add(refundGoods);
//        Mockito.when(afterSaleManager.getRefundGoods(sn)).thenReturn(refundGoodsList);
//        goodsQuantityChangeConsumer.refund(refundChangeMsg);


        GoodsSkuDO goodsSkuDO = goodsSkuManager.getModel(skuId);
        Assert.assertEquals(goodsSkuDO.getEnableQuantity().toString(), "3");
        Assert.assertEquals(goodsSkuDO.getQuantity().toString(), "3");

        GoodsDO goodsDO = goodsQueryManager.getModel(goodsId);
        Assert.assertEquals(goodsDO.getEnableQuantity().toString(), "3");
        Assert.assertEquals(goodsDO.getQuantity().toString(), "3");
    }


    /**
     * 退款入库操作
     */
    @Test
    public void testRefundGoodsStockIn() throws Exception {
        this.testOrderShip();
        this.tradeDaoSupport.execute("update es_order set service_status='APPLY' where sn = " + ordersn + "");
        String sn = "00001";
        RefundDO refundDO = new RefundDO();
        refundDO.setOrderSn(ordersn);
        refundDO.setSn(sn);
//        refundDO.setRefuseType(RefuseTypeEnum.RETURN_GOODS.name());
//        RefundChangeMsg refundChangeMsg = new RefundChangeMsg(refundDO, RefundStatusEnum.STOCK_IN);

//        List<RefundGoodsDO> refundGoodsList = new ArrayList<>();
//        RefundGoodsDO refundGoods = new RefundGoodsDO();
//        refundGoods.setGoodsId(goodsId);
//        refundGoods.setSkuId(skuId);
//        refundGoods.setReturnNum(2);
//        refundGoodsList.add(refundGoods);
//        Mockito.when(afterSaleManager.getRefundGoods(sn)).thenReturn(refundGoodsList);
//        goodsQuantityChangeConsumer.refund(refundChangeMsg);


        GoodsSkuDO goodsSkuDO = goodsSkuManager.getModel(skuId);
        Assert.assertEquals(goodsSkuDO.getEnableQuantity().toString(), "3");
        Assert.assertEquals(goodsSkuDO.getQuantity().toString(), "3");

        GoodsDO goodsDO = goodsQueryManager.getModel(goodsId);
        Assert.assertEquals(goodsDO.getEnableQuantity().toString(), "3");
        Assert.assertEquals(goodsDO.getQuantity().toString(), "3");
    }

    @After
    public void after() throws Exception {
        tradeDaoSupport.execute("delete from es_order where trade_sn = 2018071111111111");
    }


}
