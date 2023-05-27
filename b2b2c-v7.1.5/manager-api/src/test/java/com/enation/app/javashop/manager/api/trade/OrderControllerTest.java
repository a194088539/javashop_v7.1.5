package com.enation.app.javashop.manager.api.trade;

import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderLogDO;
import com.enation.app.javashop.core.trade.order.model.enums.*;
import com.enation.app.javashop.core.trade.order.model.vo.OrderDetailVO;
import com.enation.app.javashop.core.trade.order.model.vo.OrderSkuVO;
import com.enation.app.javashop.core.trade.order.service.OrderQueryManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private OrderQueryManager orderQueryManager;

    private OrderDO orderDO;

    private OrderLogDO logDO;

    @Before
    public void testData(){
        orderDO = new OrderDO();
        orderDO.setSn(createSn());
        orderDO.setSellerId(10);
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
        orderDO.setNeedPayMoney(100.0);
        orderDO.setOrderPrice(100.0);

        List<OrderSkuVO> skuVOList = new ArrayList<>();
        OrderSkuVO skuVO = new OrderSkuVO();
        skuVO.setName("测试商品");
        skuVO.setNum(1);
        skuVOList.add(skuVO);
        orderDO.setItemsJson(JsonUtil.objectToJson(skuVOList));
        this.daoSupport.insert(orderDO);

        OrderDO orderDO2 = new OrderDO();
        orderDO2.setSn(createSn());
        orderDO2.setSellerId(11);
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


        logDO = new OrderLogDO();
        logDO.setOrderSn(createSn());
        logDO.setMessage("测试");
        logDO.setOpName("admin");
        this.daoSupport.insert(logDO);

    }


    @Test
    public void testPage() throws Exception {

        //场景一
        String resultJson = mockMvc.perform(get("/admin/trade/orders")
                .header("Authorization",superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("page_no","1").param("page_Size","10"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

    }


    @Test
    public void testOne() throws Exception {
        String resultJson = mockMvc.perform(get("/admin/trade/orders/"+orderDO.getSn())
                .header("Authorization",superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("order_sn",orderDO.getSn()))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        OrderDetailVO orderDetailVO = JsonUtil.jsonToObject(resultJson, OrderDetailVO.class);
        Assert.assertEquals(orderDO.getSn(),orderDetailVO.getSn());

    }


    /**
     * 确认付款
     * @throws Exception
     */
    @Test
    public void testPay() throws Exception {

        mockMvc.perform(post("/admin/trade/orders/"+orderDO.getSn()+"/pay")
                .header("Authorization",superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("order_sn",orderDO.getSn()).param("pay_price",orderDO.getNeedPayMoney()+""))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        OrderDetailVO orderDetailVO = this.orderQueryManager.getModel(orderDO.getSn(),null);
        Assert.assertEquals(orderDetailVO.getPayStatus(),PayStatusEnum.PAY_YES.name());
        Assert.assertEquals(orderDetailVO.getOrderStatus(),OrderStatusEnum.PAID_OFF.name());
        Assert.assertEquals(orderDetailVO.getServiceStatus(),OrderServiceStatusEnum.EXPIRED.name());
    }

    /**
     * 取消订单
     * @throws Exception
     */
    @Test
    public void testCancelled() throws Exception {

        mockMvc.perform(post("/admin/trade/orders/"+orderDO.getSn()+"/cancelled")
                .header("Authorization",superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        OrderDetailVO orderDetailVO = this.orderQueryManager.getModel(orderDO.getSn(),null);
        Assert.assertEquals(orderDetailVO.getOrderStatus(),OrderStatusEnum.CANCELLED.name());
    }


    @Test
    public void testLog() throws Exception {

        String json = mockMvc.perform(get("/admin/trade/orders/"+logDO.getOrderSn()+"/log")
                .header("Authorization",superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("order_sn",orderDO.getSn()))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
    }


    /**
     * 生成订单号
     * @return
     */
    private static String createSn(){

        Random random = new Random();

        int result = random.nextInt(10);

        String sn = DateUtil.getDateline()+""+result;
        return sn;
    }


}
