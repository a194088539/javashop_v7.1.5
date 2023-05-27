package com.enation.app.javashop.consumer.shop.trade;

import com.enation.app.javashop.consumer.shop.trade.consumer.OrderRogTransactionConsumer;
import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.order.model.vo.OrderSkuVO;
import com.enation.app.javashop.core.trade.order.service.TransactionRecordManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单确认收货增加交易记录消费者测试
 * @author Snow create in 2018/7/2
 * @version v2.0
 * @since v7.0.0
 */
public class OrderRogTransactionConsumerTest extends BaseTest {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    private OrderStatusChangeMsg changeMsg;

    @Autowired
    private OrderRogTransactionConsumer consumer;

    @Autowired
    private TransactionRecordManager transactionRecordManager;

    @Before
    public void testData(){

        int memberId = 99;
        changeMsg = new OrderStatusChangeMsg();
        changeMsg.setOldStatus(OrderStatusEnum.SHIPPED);
        changeMsg.setNewStatus(OrderStatusEnum.ROG);

        OrderDO orderDO = new OrderDO();
        orderDO.setSn(DateUtil.getDateline()+"");
        orderDO.setMemberId(memberId);
        orderDO.setMemberName("测试会员名称");

        List<OrderSkuVO> skuVOList = new ArrayList<>();
        OrderSkuVO skuVO = new OrderSkuVO();
        skuVO.setGoodsId(1);
        skuVO.setNum(50);
        skuVO.setPurchasePrice(10.0);
        skuVOList.add(skuVO);
        orderDO.setItemsJson(JsonUtil.objectToJson(skuVOList));

        changeMsg.setOrderDO(orderDO);

    }

    @Test
    public void test(){
        this.consumer.orderChange(changeMsg);
        List list = this.transactionRecordManager.listAll(changeMsg.getOrderDO().getSn());
        if(list == null && list.isEmpty()){
            throw new RuntimeException("订单确认收货，增加交易记录消费者测试出错");
        }
    }

}
