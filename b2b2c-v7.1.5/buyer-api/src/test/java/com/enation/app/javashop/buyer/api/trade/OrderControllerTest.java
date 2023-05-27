package com.enation.app.javashop.buyer.api.trade;

import com.enation.app.javashop.core.promotion.fulldiscount.model.dos.FullDiscountGiftDO;
import com.enation.app.javashop.core.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.javashop.core.trade.cart.model.vo.CartPromotionVo;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.enums.*;
import com.enation.app.javashop.core.trade.order.model.vo.OrderDetailVO;
import com.enation.app.javashop.core.trade.order.model.vo.OrderSkuVO;
import com.enation.app.javashop.core.trade.order.service.OrderMetaManager;
import com.enation.app.javashop.core.trade.order.service.TradeSnCreator;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 订单测试
 * @author Snow create in 2018/6/13
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
public class OrderControllerTest extends BaseTest {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private TradeSnCreator tradeSnCreator;

    @MockBean
    private OrderMetaManager orderMetaManager;

    private OrderDO orderDO;

    //待收货的订单数据
    private OrderDO orderDO3;

    //未付款的订单数据
    private OrderDO orderDO4;

    //待收货的订单数据 含积分商品
    private OrderDO orderDO5;

    //待收货的订单数据 含赠品
    private OrderDO orderDO6;

    private String tradeSn = DateUtil.getDateline()+"";

    @Before
    public void testData(){
        orderDO = new OrderDO();
        orderDO.setSn(tradeSnCreator.generateOrderSn());
        orderDO.setMemberId(1);
        orderDO.setOrderStatus(OrderStatusEnum.CONFIRM.value());
        orderDO.setMemberName("buyer1");
        orderDO.setDisabled(0);
        orderDO.setTradeSn(tradeSn);
        orderDO.setPayStatus(PayStatusEnum.PAY_NO.value());
        orderDO.setShipStatus(ShipStatusEnum.SHIP_NO.value());
        orderDO.setCreateTime(DateUtil.getDateline());
        orderDO.setPaymentType(PaymentTypeEnum.ONLINE.value());
        orderDO.setCommentStatus(CommentStatusEnum.UNFINISHED.value());
        orderDO.setServiceStatus(OrderServiceStatusEnum.NOT_APPLY.value());

        List<OrderSkuVO> skuVOList = new ArrayList<>();
        OrderSkuVO skuVO = new OrderSkuVO();
        skuVO.setName("测试商品");
        skuVO.setNum(22);
        skuVOList.add(skuVO);
        orderDO.setItemsJson(JsonUtil.objectToJson(skuVOList));
        this.daoSupport.insert(orderDO);

        OrderDO orderDO2 = new OrderDO();
        orderDO2.setSn(tradeSnCreator.generateOrderSn());
        orderDO2.setMemberId(2);
        orderDO2.setOrderStatus(OrderStatusEnum.CONFIRM.value());
        orderDO2.setMemberName("buyer2");
        orderDO2.setDisabled(0);
        orderDO2.setTradeSn(tradeSn);
        orderDO2.setPayStatus(PayStatusEnum.PAY_NO.value());
        orderDO2.setShipStatus(ShipStatusEnum.SHIP_NO.value());
        orderDO2.setCreateTime(DateUtil.getDateline());
        orderDO2.setPaymentType(PaymentTypeEnum.ONLINE.value());
        orderDO2.setCommentStatus(CommentStatusEnum.UNFINISHED.value());
        orderDO2.setServiceStatus(OrderServiceStatusEnum.NOT_APPLY.value());

        List<OrderSkuVO> skuVOList2 = new ArrayList<>();
        OrderSkuVO skuVO2 = new OrderSkuVO();
        skuVO2.setName("测试商品222");
        skuVOList2.add(skuVO2);
        orderDO2.setItemsJson(JsonUtil.objectToJson(skuVOList2));
        this.daoSupport.insert(orderDO2);


        //待收货的订单
        orderDO3 = new OrderDO();
        orderDO3.setSn(tradeSnCreator.generateOrderSn());
        orderDO3.setMemberId(1);
        orderDO3.setOrderStatus(OrderStatusEnum.SHIPPED.value());
        orderDO3.setMemberName("buyer1");
        orderDO3.setDisabled(0);
        orderDO3.setPayStatus(PayStatusEnum.PAY_YES.value());
        orderDO3.setShipStatus(ShipStatusEnum.SHIP_YES.value());
        orderDO3.setCreateTime(DateUtil.getDateline());
        orderDO3.setPaymentType(PaymentTypeEnum.ONLINE.value());
        orderDO3.setCommentStatus(CommentStatusEnum.UNFINISHED.value());
        orderDO3.setServiceStatus(OrderServiceStatusEnum.NOT_APPLY.value());

        List<OrderSkuVO> skuVOList3 = new ArrayList<>();
        OrderSkuVO skuVO3 = new OrderSkuVO();
        skuVO3.setName("测试商品");
        skuVO3.setNum(22);
        skuVOList3.add(skuVO3);
        orderDO3.setItemsJson(JsonUtil.objectToJson(skuVOList3));
        this.daoSupport.insert(orderDO3);


        //待收货的订单
        orderDO4 = new OrderDO();
        orderDO4.setSn(tradeSnCreator.generateOrderSn());
        orderDO4.setMemberId(1);
        orderDO4.setOrderStatus(OrderStatusEnum.CONFIRM.value());
        orderDO4.setMemberName("buyer1");
        orderDO4.setDisabled(0);
        orderDO4.setPayStatus(PayStatusEnum.PAY_NO.value());
        orderDO4.setShipStatus(ShipStatusEnum.SHIP_NO.value());
        orderDO4.setCreateTime(DateUtil.getDateline());
        orderDO4.setPaymentType(PaymentTypeEnum.ONLINE.value());
        orderDO4.setCommentStatus(CommentStatusEnum.UNFINISHED.value());
        orderDO4.setServiceStatus(OrderServiceStatusEnum.NOT_APPLY.value());

        List<OrderSkuVO> skuVOList4 = new ArrayList<>();
        OrderSkuVO skuVO4 = new OrderSkuVO();
        skuVO4.setName("测试商品");
        skuVO4.setNum(22);
        skuVOList4.add(skuVO4);
        orderDO4.setItemsJson(JsonUtil.objectToJson(skuVOList4));
        this.daoSupport.insert(orderDO4);


        //待收货的订单
        orderDO5 = new OrderDO();
        orderDO5.setSn(tradeSnCreator.generateOrderSn());
        orderDO5.setMemberId(1);
        orderDO5.setOrderStatus(OrderStatusEnum.SHIPPED.value());
        orderDO5.setMemberName("buyer1");
        orderDO5.setDisabled(0);
        orderDO5.setPayStatus(PayStatusEnum.PAY_YES.value());
        orderDO5.setShipStatus(ShipStatusEnum.SHIP_YES.value());
        orderDO5.setCreateTime(DateUtil.getDateline());
        orderDO5.setPaymentType(PaymentTypeEnum.ONLINE.value());
        orderDO5.setCommentStatus(CommentStatusEnum.UNFINISHED.value());
        orderDO5.setServiceStatus(OrderServiceStatusEnum.NOT_APPLY.value());

        List<OrderSkuVO> skuVOList5 = new ArrayList<>();
        OrderSkuVO skuVO5 = new OrderSkuVO();
        skuVO5.setName("测试商品");
        skuVO5.setNum(22);

        List<CartPromotionVo> promotionGoodsVOList = new ArrayList<>();
        CartPromotionVo promotionGoodsVO = new CartPromotionVo();
        promotionGoodsVO.setIsCheck(1);
        promotionGoodsVO.setActivityId(1);
        promotionGoodsVO.setPromotionType(PromotionTypeEnum.EXCHANGE.name());
        promotionGoodsVOList.add(promotionGoodsVO);

        skuVO5.setSingleList(promotionGoodsVOList);
        skuVOList5.add(skuVO5);
        orderDO5.setItemsJson(JsonUtil.objectToJson(skuVOList5));
        this.daoSupport.insert(orderDO5);

        List list = new ArrayList();
        String giftJson = JsonUtil.objectToJson(list);
        when (orderMetaManager.getMetaValue(orderDO5.getSn(),OrderMetaKeyEnum.GIFT)).thenReturn(giftJson);


        //待收货的订单
        orderDO6 = new OrderDO();
        orderDO6.setSn(tradeSnCreator.generateOrderSn());
        orderDO6.setMemberId(1);
        orderDO6.setOrderStatus(OrderStatusEnum.SHIPPED.value());
        orderDO6.setMemberName("buyer1");
        orderDO6.setDisabled(0);
        orderDO6.setPayStatus(PayStatusEnum.PAY_YES.value());
        orderDO6.setShipStatus(ShipStatusEnum.SHIP_YES.value());
        orderDO6.setCreateTime(DateUtil.getDateline());
        orderDO6.setPaymentType(PaymentTypeEnum.ONLINE.value());
        orderDO6.setCommentStatus(CommentStatusEnum.UNFINISHED.value());
        orderDO6.setServiceStatus(OrderServiceStatusEnum.NOT_APPLY.value());

        List<OrderSkuVO> skuVOList6 = new ArrayList<>();
        OrderSkuVO skuVO6 = new OrderSkuVO();
        skuVO6.setName("测试商品");
        skuVO6.setNum(22);

        skuVOList6.add(skuVO6);
        orderDO6.setItemsJson(JsonUtil.objectToJson(skuVOList6));
        this.daoSupport.insert(orderDO6);

        List<FullDiscountGiftDO> giftList = new ArrayList();
        FullDiscountGiftDO giftDO = new FullDiscountGiftDO();
        giftDO.setGiftName("123123");
        giftDO.setGiftId(1);
        giftDO.setSellerId(3);
        giftDO.setGiftImg("path");
        giftList.add(giftDO);
        String giftJson2 = JsonUtil.objectToJson(giftList);
        when (orderMetaManager.getMetaValue(orderDO6.getSn(),OrderMetaKeyEnum.GIFT)).thenReturn(giftJson2);
    }


    @Test
    public void testPage() throws Exception {

        //场景1，不传递参数
        String resultJson = mockMvc.perform(get("/trade/orders")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();


        //场景一
        String resultJson2 = mockMvc.perform(get("/trade/orders")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("page_no","1").param("page_Size","10")
                .param("goods_name","测试商品").param("order_status","ALL"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        Page page2 = JsonUtil.jsonToObject(resultJson2, Page.class);
        List<Map> orderLineVOList2 =  page2.getData();
        Integer memberId2 = (Integer) orderLineVOList2.get(0).get("member_id");
        if(memberId2.intValue() != 1){
            throw new RuntimeException("断言异常，测试脚本出现错误");
        }

    }


    @Test
    public void testOne() throws Exception {
        String resultJson = mockMvc.perform(get("/trade/orders/"+orderDO.getSn())
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("order_sn",orderDO.getSn()))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        OrderDetailVO orderDetailVO = JsonUtil.jsonToObject(resultJson, OrderDetailVO.class);
        Assert.assertEquals(orderDO.getSn(),orderDetailVO.getSn());

    }


    @Test
    public void testOrderList() throws Exception {

        //场景1，不传递参数
        String resultJson = mockMvc.perform(get("/trade/orders/"+tradeSn+"/list")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        Integer num = 2;
        List list = JsonUtil.jsonToList(resultJson,OrderDetailVO.class);
        Integer resultNum = list.size();
        Assert.assertEquals(resultNum,num);

    }



    /**
     * 测试订单状态数量
     * @throws Exception
     */
    @Test
    public void testStatusNum() throws Exception {
        String resultJson = mockMvc.perform(get("/trade/orders/status-num")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

    }

    /**
     * 确认收货
     * @throws Exception
     */
    @Test
    public void testRog() throws Exception {

        mockMvc.perform(post("/trade/orders/"+orderDO3.getSn()+"/rog")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        String resultJson = mockMvc.perform(get("/trade/orders/"+orderDO3.getSn())
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("order_sn",orderDO3.getSn()))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        OrderDetailVO orderDetailVO = JsonUtil.jsonToObject(resultJson, OrderDetailVO.class);
        Assert.assertEquals(OrderStatusEnum.ROG.name(),orderDetailVO.getOrderStatus());



        mockMvc.perform(post("/trade/orders/"+orderDO5.getSn()+"/rog")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        String resultJson2 = mockMvc.perform(get("/trade/orders/"+orderDO5.getSn())
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        OrderDetailVO orderDetailVO2 = JsonUtil.jsonToObject(resultJson2, OrderDetailVO.class);
        Assert.assertEquals(OrderStatusEnum.ROG.name(),orderDetailVO2.getOrderStatus());
        Assert.assertEquals(OrderServiceStatusEnum.EXPIRED.name(),orderDetailVO2.getServiceStatus());

        for(OrderSkuVO orderSkuVO:orderDetailVO2.getOrderSkuList()){
            Assert.assertEquals(OrderServiceStatusEnum.EXPIRED.name(),orderSkuVO.getServiceStatus());
        }



        mockMvc.perform(post("/trade/orders/"+orderDO6.getSn()+"/rog")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        String resultJson3 = mockMvc.perform(get("/trade/orders/"+orderDO6.getSn())
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        OrderDetailVO orderDetailVO3 = JsonUtil.jsonToObject(resultJson3, OrderDetailVO.class);
        Assert.assertEquals(OrderStatusEnum.ROG.name(),orderDetailVO3.getOrderStatus());
        Assert.assertEquals(OrderServiceStatusEnum.NOT_APPLY.name(),orderDetailVO3.getServiceStatus());

        for(OrderSkuVO orderSkuVO:orderDetailVO3.getOrderSkuList()){
            Assert.assertEquals(OrderServiceStatusEnum.EXPIRED.name(),orderSkuVO.getServiceStatus());
        }

    }



    /**
     * 取消订单
     * @throws Exception
     */
    @Test
    public void testCancel() throws Exception {
        mockMvc.perform(post("/trade/orders/"+orderDO4.getSn()+"/cancel")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        String resultJson = mockMvc.perform(get("/trade/orders/"+orderDO4.getSn())
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        OrderDetailVO orderDetailVO = JsonUtil.jsonToObject(resultJson, OrderDetailVO.class);
        Assert.assertEquals(OrderStatusEnum.CANCELLED.name(),orderDetailVO.getOrderStatus());
    }


}
