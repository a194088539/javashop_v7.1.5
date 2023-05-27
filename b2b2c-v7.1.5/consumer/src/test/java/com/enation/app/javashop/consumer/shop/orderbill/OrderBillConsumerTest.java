package com.enation.app.javashop.consumer.shop.orderbill;

import com.enation.app.javashop.core.aftersale.model.dos.RefundDO;
import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.client.trade.OrderClient;
import com.enation.app.javashop.core.trade.sdk.model.OrderDetailDTO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


/**
 * @author fk
 * @version v1.0
 * @Description:  结算单消费者单元测试
 * @date 2018/6/26 11:34
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager", rollbackFor = Exception.class)
public class OrderBillConsumerTest extends BaseTest {

    @Autowired
    private OrderBillConsumer orderBillConsumer;
    @MockBean
    private OrderClient orderClient;
    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    String orderSn = "123456";

    @Before
    public void before(){
        this.daoSupport.execute("delete from es_bill_item ");

        OrderDetailDTO orderDetail = new OrderDetailDTO();
        orderDetail.setSn(orderSn);
        orderDetail.setMemberId(1 );
        orderDetail.setSellerId(1);
        orderDetail.setOrderPrice(10d);
        orderDetail.setDiscountPrice(0d);

        Mockito.when(orderClient.getModel(orderSn)).thenReturn(orderDetail);
    }

    /**
     * 测试订单收货生成结算单
     */
    @Test
    public void testOrderRog(){

        OrderStatusChangeMsg orderMessage = new OrderStatusChangeMsg();
        orderMessage.setNewStatus(OrderStatusEnum.ROG);
        OrderDO order = new OrderDO();
        order.setSn(orderSn);
        orderMessage.setOrderDO(order);

        orderBillConsumer.orderChange(orderMessage);

        Map map = this.daoSupport.queryForMap("select * from es_bill_item where order_sn = ?",orderSn);
        Assert.assertTrue(map.get("price").toString().equals("10"));
        Assert.assertTrue(map.get("item_type").toString().equals("PAYMENT"));
    }

    /**
     * 测试退款单审核通过
     */
    @Test
    public void testRefundAuth(){

        RefundDO refundDO = new RefundDO();
        refundDO.setOrderSn(orderSn);
        refundDO.setSn("B12345675");
        refundDO.setRefundPrice(11d);
//        RefundChangeMsg refundChangeMsg = new RefundChangeMsg(refundDO,RefundStatusEnum.PASS);
//        orderBillConsumer.refund(refundChangeMsg);

        Map map = this.daoSupport.queryForMap("select * from es_bill_item where refund_sn = ?","B12345675");
        Assert.assertTrue(map.get("price").toString().equals("11"));
        Assert.assertTrue(map.get("item_type").toString().equals("REFUND"));
    }




}
