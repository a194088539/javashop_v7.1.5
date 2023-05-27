package com.enation.app.javashop.consumer.shop.trade;

import com.enation.app.javashop.consumer.shop.trade.consumer.OrderPayLogConsumer;
import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;
import com.enation.app.javashop.core.payment.model.dos.PaymentMethodDO;
import com.enation.app.javashop.core.payment.service.PaymentMethodManager;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dos.PayLog;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.order.model.enums.PayStatusEnum;
import com.enation.app.javashop.core.trade.order.service.PayLogManager;
import com.enation.app.javashop.core.trade.order.service.TradeSnCreator;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.when;

/**
 * 订单支付发送消息测试用例
 * @author Snow create in 2018/7/18
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
public class OrderPayLogConsumerTest extends BaseTest {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private PayLogManager payLogManager;

    @Autowired
    private TradeSnCreator tradeSnCreator;

    @Autowired
    private OrderPayLogConsumer consumer;

    @MockBean
    private PaymentMethodManager paymentMethodManager;

    private OrderStatusChangeMsg changeMsg;

    private String orderSn;


    @Before
    public void testData(){

        orderSn = this.tradeSnCreator.generateOrderSn();
        String alipay = "alipay";

        OrderDO orderDO = new OrderDO();
        orderDO.setSn(orderSn);
        orderDO.setPayMoney(100.0);
        orderDO.setPaymentTime(123456798l);
        orderDO.setMemberName("测试xlp");
        orderDO.setPaymentPluginId(alipay);

        PayLog payLog = new PayLog();
        payLog.setOrderSn(orderSn);
        this.daoSupport.insert(payLog);

        changeMsg = new OrderStatusChangeMsg();
        changeMsg.setOldStatus(OrderStatusEnum.CONFIRM);
        changeMsg.setNewStatus(OrderStatusEnum.PAID_OFF);
        changeMsg.setOrderDO(orderDO);

        PaymentMethodDO paymentMethodDO = new PaymentMethodDO();
        paymentMethodDO.setMethodName("支付宝");
        when (paymentMethodManager.getByPluginId(alipay)).thenReturn(paymentMethodDO);

    }

    @Test
    public void test() throws Exception {
        this.consumer.orderChange(changeMsg);
        PayLog payLog = this.payLogManager.getModel(orderSn);
        Assert.assertEquals(payLog.getPayStatus(), PayStatusEnum.PAY_YES.name());
    }

}
