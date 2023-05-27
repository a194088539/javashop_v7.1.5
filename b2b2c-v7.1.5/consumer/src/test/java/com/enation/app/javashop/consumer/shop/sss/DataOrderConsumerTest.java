package com.enation.app.javashop.consumer.shop.sss;

import com.enation.app.javashop.consumer.shop.sss.DataOrderConsumer;
import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;
import com.enation.app.javashop.core.goods.model.dos.CategoryDO;
import com.enation.app.javashop.core.goods.service.CategoryManager;
import com.enation.app.javashop.core.statistics.model.dto.OrderData;
import com.enation.app.javashop.core.statistics.model.dto.OrderGoodsData;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderItemsDO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.order.model.enums.PayStatusEnum;
import com.enation.app.javashop.core.trade.order.service.OrderQueryManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * 统计订单测试类
 *
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018/5/2 上午11:29
 */
@Rollback(true)
public class DataOrderConsumerTest extends BaseTest {


    @Autowired
    private DataOrderConsumer dataOrderConsumer;

    @Autowired
    @Qualifier("sssDaoSupport")
    private DaoSupport daoSupport;

    @MockBean
    private OrderQueryManager orderQueryManager;
    @MockBean
    private CategoryManager categoryManager;

    @Before
    public void init() {

        this.daoSupport.execute("TRUNCATE TABLE es_sss_order_data");
        this.daoSupport.execute("TRUNCATE TABLE es_sss_order_goods_data");

        List<OrderItemsDO> list = new ArrayList<>();
        OrderItemsDO orderItemsDO = new OrderItemsDO();
        orderItemsDO.setCatId(3333);
        orderItemsDO.setGoodsId(1332);
        orderItemsDO.setName("orderitem name");
        orderItemsDO.setNum(3);
        orderItemsDO.setPrice(99.99);
        list.add(orderItemsDO);
        when(orderQueryManager.orderItems(anyString())).thenReturn(list);
        CategoryDO categoryDO = new CategoryDO();
        categoryDO.setCategoryPath("|0|1|2");
        when(categoryManager.getModel(anyInt())).thenReturn(categoryDO);
    }

    @Test
    public void testOrder() throws Exception{

        //准备工作
        OrderDO order = new OrderDO();

        order.setSn("18888888");
        order.setSellerId(132);
        order.setSellerName("seller");
        order.setMemberName("buyer");
        order.setMemberId(123);
        order.setOrderStatus("COD");
        order.setPayStatus("PAY_YES");
        order.setOrderPrice(99.66);
        order.setGoodsNum(1);
        order.setCreateTime(1421412412L);
        order.setShipProvinceId(33);
        order.setShipCityId(244);


        OrderStatusChangeMsg orderStatusChangeMsg = new OrderStatusChangeMsg();
        orderStatusChangeMsg.setOrderDO(order);
        orderStatusChangeMsg.setNewStatus(OrderStatusEnum.PAID_OFF);
        dataOrderConsumer.orderChange(orderStatusChangeMsg);
        OrderData orderData = daoSupport.queryForObject("select * from es_sss_order_data where sn = ?", OrderData.class, order.getSn());
        List<OrderGoodsData> orderGoodsData = daoSupport.queryForList("select * from es_sss_order_goods_data where order_sn = ?", OrderGoodsData.class, order.getSn());

        OrderData actual = new OrderData();
        actual.setId(1);
        actual.setSn("18888888");
        actual.setBuyerId(123);
        actual.setBuyerName("buyer");
        actual.setSellerId(132);
        actual.setSellerName("seller");
        actual.setOrderStatus("COD");
        actual.setPayStatus("PAY_YES");
        actual.setOrderPrice(99.66);
        actual.setGoodsNum(3);
        actual.setShipProvinceId(33);
        actual.setShipCityId(244);
        actual.setCreateTime(1421412412L);
        Assert.assertEquals(actual.toString(),orderData.toString());

        OrderGoodsData actualGoodsData = new OrderGoodsData();
        actualGoodsData.setId(1);
        actualGoodsData.setOrderSn("18888888");
        actualGoodsData.setGoodsId(1332);
        actualGoodsData.setGoodsName("orderitem name");
        actualGoodsData.setGoodsNum(3);
        actualGoodsData.setPrice(99.99);
        actualGoodsData.setSubTotal(299.97);
        actualGoodsData.setCategoryPath("|0|1|2");
        actualGoodsData.setCategoryId(3333);
        actualGoodsData.setCreateTime(1421412412L);
        actualGoodsData.setIndustryId(1);

        Assert.assertEquals(actualGoodsData.toString(),orderGoodsData.get(0).toString());

        order.setPayStatus(PayStatusEnum.PAY_YES.value());
        order.setOrderStatus(OrderStatusEnum.PAID_OFF.value());
        orderStatusChangeMsg.setOrderDO(order);
        orderStatusChangeMsg.setNewStatus(OrderStatusEnum.COMPLETE);

        dataOrderConsumer.orderChange(orderStatusChangeMsg);
        orderData = daoSupport.queryForObject("select * from es_sss_order_data where sn = ?", OrderData.class, order.getSn());
        Assert.assertEquals(new OrderData(order).toString(),orderData.toString());
    }


}
