package com.enation.app.javashop.seller.api.trade;

import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dos.TradeDO;
import com.enation.app.javashop.core.trade.order.model.dto.OrderDetailQueryParam;
import com.enation.app.javashop.core.trade.order.model.dto.OrderQueryParam;
import com.enation.app.javashop.core.trade.order.model.enums.*;
import com.enation.app.javashop.core.trade.order.model.vo.OrderDetailVO;
import com.enation.app.javashop.core.trade.order.model.vo.OrderSkuVO;
import com.enation.app.javashop.core.trade.order.service.*;
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
import java.util.Random;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    private TradeQueryManager tradeQueryManager;

    @Autowired
    private OrderQueryManager orderQueryManager;

    private OrderDO orderDO;

    private OrderDO orderDO3;

    private OrderDO orderDO4;

    @Before
    public void testData(){
        orderDO = new OrderDO();
        orderDO.setSn(this.tradeSnCreator.generateOrderSn());
        orderDO.setSellerId(3);
        orderDO.setMemberId(1);
        orderDO.setOrderStatus(OrderStatusEnum.CONFIRM.value());
        orderDO.setMemberName("buyer1");
        orderDO.setDisabled(0);
        orderDO.setPayStatus(PayStatusEnum.PAY_NO.value());
        orderDO.setShipStatus(ShipStatusEnum.SHIP_NO.value());
        orderDO.setCreateTime(DateUtil.getDateline());
        orderDO.setPaymentType(PaymentTypeEnum.ONLINE.value());
        orderDO.setCommentStatus(CommentStatusEnum.UNFINISHED.value());
        orderDO.setServiceStatus(OrderServiceStatusEnum.NOT_APPLY.value());
        orderDO.setOrderPrice(55.0);

        List<OrderSkuVO> skuVOList = new ArrayList<>();
        OrderSkuVO skuVO = new OrderSkuVO();
        skuVO.setName("测试商品");
        orderDO.setItemsJson(JsonUtil.objectToJson(skuVOList));
        this.daoSupport.insert(orderDO);

        OrderDO orderDO2 = new OrderDO();
        orderDO2.setSn(this.tradeSnCreator.generateOrderSn());
        orderDO2.setSellerId(4);
        orderDO2.setMemberId(2);
        orderDO2.setOrderStatus(OrderStatusEnum.CONFIRM.value());
        orderDO2.setMemberName("buyer2");
        orderDO2.setDisabled(0);
        orderDO2.setPayStatus(PayStatusEnum.PAY_NO.value());
        orderDO2.setShipStatus(ShipStatusEnum.SHIP_NO.value());
        orderDO2.setCreateTime(DateUtil.getDateline());
        orderDO2.setPaymentType(PaymentTypeEnum.ONLINE.value());
        orderDO2.setCommentStatus(CommentStatusEnum.UNFINISHED.value());
        orderDO2.setServiceStatus(OrderServiceStatusEnum.NOT_APPLY.value());

        List<OrderSkuVO> skuVOList2 = new ArrayList<>();
        OrderSkuVO skuVO2 = new OrderSkuVO();
        skuVO2.setName("测试商品222");
        orderDO2.setItemsJson(JsonUtil.objectToJson(skuVOList2));
        this.daoSupport.insert(orderDO2);


        //发货模拟数据
        orderDO3 = new OrderDO();
        orderDO3.setSn(this.tradeSnCreator.generateOrderSn());
        orderDO3.setSellerId(3);
        orderDO3.setMemberId(2);
        orderDO3.setOrderStatus(OrderStatusEnum.PAID_OFF.value());
        orderDO3.setMemberName("buyer2");
        orderDO3.setDisabled(0);
        orderDO3.setPayStatus(PayStatusEnum.PAY_YES.value());
        orderDO3.setShipStatus(ShipStatusEnum.SHIP_NO.value());
        orderDO3.setCreateTime(DateUtil.getDateline());
        orderDO3.setPaymentType(PaymentTypeEnum.ONLINE.value());
        orderDO3.setCommentStatus(CommentStatusEnum.UNFINISHED.value());
        orderDO3.setServiceStatus(OrderServiceStatusEnum.NOT_APPLY.value());

        List<OrderSkuVO> skuVOList3 = new ArrayList<>();
        OrderSkuVO skuVO3 = new OrderSkuVO();
        skuVO3.setName("测试商品222");
        orderDO3.setItemsJson(JsonUtil.objectToJson(skuVOList3));
        this.daoSupport.insert(orderDO3);


        //货到付款——已收货模拟数据
        orderDO4 = new OrderDO();
        orderDO4.setSn(this.tradeSnCreator.generateOrderSn());
        orderDO4.setSellerId(3);
        orderDO4.setMemberId(2);
        orderDO4.setOrderStatus(OrderStatusEnum.SHIPPED.value());
        orderDO4.setMemberName("buyer2");
        orderDO4.setDisabled(0);
        orderDO4.setPayStatus(PayStatusEnum.PAY_NO.value());
        orderDO4.setShipStatus(ShipStatusEnum.SHIP_YES.value());
        orderDO4.setCreateTime(DateUtil.getDateline());
        orderDO4.setPaymentType(PaymentTypeEnum.COD.value());
        orderDO4.setCommentStatus(CommentStatusEnum.UNFINISHED.value());
        orderDO4.setServiceStatus(OrderServiceStatusEnum.NOT_APPLY.value());
        orderDO4.setNeedPayMoney(100.0);

        List<OrderSkuVO> skuVOList4 = new ArrayList<>();
        OrderSkuVO skuVO4 = new OrderSkuVO();
        skuVO4.setName("测试商品222");
        orderDO4.setItemsJson(JsonUtil.objectToJson(skuVOList4));
        this.daoSupport.insert(orderDO4);

    }


    @Test
    public void testPage() throws Exception {

        //场景一
        String resultJson = mockMvc.perform(get("/seller/trade/orders")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("page_no","1").param("page_Size","10"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        Page page = JsonUtil.jsonToObject(resultJson, Page.class);
        List<Map> orderLineVOList =  page.getData();
        Integer sellerId = (Integer) orderLineVOList.get(0).get("seller_id");

        if(sellerId.intValue() != orderDO.getSellerId()){
            throw new RuntimeException("断言异常，测试脚本出现错误");
        }
    }


    @Test
    public void testOne() throws Exception {
        String resultJson = mockMvc.perform(get("/seller/trade/orders/"+orderDO.getSn())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        OrderDetailVO orderDetailVO = JsonUtil.jsonToObject(resultJson, OrderDetailVO.class);
        Assert.assertEquals(orderDO.getSn(),orderDetailVO.getSn());

    }


    /**
     * 订单发货测试
     * @throws Exception
     */
    @Test
    public void testDelivery() throws Exception {

        mockMvc.perform(post("/seller/trade/orders/"+orderDO3.getSn()+"/delivery")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("ship_no","123456789")
                .param("logi_id","1").param("logi_name","顺丰"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
    }


    /**
     * 货到付款订单确认收款测试
     * @throws Exception
     */
    @Test
    public void testPayOrder() throws Exception {

        mockMvc.perform(post("/seller/trade/orders/"+orderDO4.getSn()+"/pay")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("order_sn",orderDO4.getSn()).param("pay_price",orderDO4.getNeedPayMoney()+""))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        OrderDetailQueryParam queryParam = new OrderDetailQueryParam();
        queryParam.setSellerId(3);

        OrderDetailVO detailVO =  this.orderQueryManager.getModel(orderDO4.getSn(),queryParam);
        Assert.assertEquals(PayStatusEnum.PAY_YES.name(),detailVO.getPayStatus());

    }


    /**
     * 商家修改订单价格测试
     * @throws Exception
     */
    @Test
    public void testUpdatePrice() throws Exception {

        Double newPrice = 35.4;

        TradeDO tradeDO = new TradeDO();
        tradeDO.setTotalPrice(orderDO.getOrderPrice());
        when (this.tradeQueryManager.getModel(orderDO.getTradeSn())).thenReturn(tradeDO);

        mockMvc.perform(put("/seller/trade/orders/"+orderDO.getSn()+"/price")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("order_sn",orderDO.getSn()).param("order_price",newPrice+""))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        String resultJson2 = mockMvc.perform(get("/seller/trade/orders/"+orderDO.getSn())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("order_sn",orderDO.getSn()))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        OrderDetailVO orderDetailVO2 = JsonUtil.jsonToObject(resultJson2, OrderDetailVO.class);
        Assert.assertEquals(newPrice,orderDetailVO2.getOrderPrice());
    }


    /**
     * 修改收货地址信息
     * @throws Exception
     */
    @Test
    public void testUpdateOrderConsignee() throws Exception {

        mockMvc.perform(put("/seller/trade/orders/"+orderDO.getSn()+"/address")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("order_sn",orderDO.getSn()).param("ship_name","收货人姓名2")
                .param("remark","11").param("ship_addr","北京通州11")
                .param("ship_mobile","15110020385").param("ship_tel","")
                .param("receive_time","2018:01:15").param("region","316"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
    }


    /**
     * 读取订单流程图
     * @throws Exception
     */
    @Test
    public void testOrderFlow() throws Exception {

        String resultJson = mockMvc.perform(get("/seller/trade/orders/"+orderDO3.getSn()+"/flow")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
    }


}
