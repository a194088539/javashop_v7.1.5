package com.enation.app.javashop.consumer.shop.trade;

import com.enation.app.javashop.consumer.shop.trade.consumer.CouponConsumer;
import com.enation.app.javashop.core.base.message.OrderStatusChangeMsg;
import com.enation.app.javashop.core.promotion.coupon.model.dos.CouponDO;
import com.enation.app.javashop.core.promotion.coupon.service.CouponManager;
import com.enation.app.javashop.core.trade.cart.model.vo.CouponVO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderMetaDO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderMetaKeyEnum;
import com.enation.app.javashop.core.trade.order.model.enums.OrderServiceStatusEnum;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.order.service.OrderMetaManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

/**
 * 确认收款发放促销活动赠送优惠券测试
 *
 * @author Snow create in 2018/7/2
 * @version v2.0
 * @since v7.0.0
 */
public class OrderPaySendBonusConsumerTest extends BaseTest {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    private OrderStatusChangeMsg changeMsg;

    @Autowired
    private CouponConsumer consumer;

    @Autowired
    private OrderMetaManager orderMetaManager;

    @Autowired
    private CouponManager couponManager;

    private CouponDO couponDO;

    @Before
    public void testData(){

        int memberId = 99;
        int sellerId = 88;

        couponDO = new CouponDO();
        couponDO.setSellerId(sellerId);
        couponDO.setReceivedNum(0);
        couponDO.setUsedNum(0);
        couponDO.setTitle("满100减10元");
        couponDO.setCouponThresholdPrice(100.0);
        couponDO.setCouponPrice(10.0);
        this.daoSupport.insert(couponDO);
        int  couponId = this.daoSupport.getLastId("es_coupon");
        couponDO.setCouponId(couponId);

        //赠优惠券
        List<CouponVO> couponVOList = new ArrayList<>();
        CouponVO couponVO = new CouponVO();
        couponVO.setAmount(10.0);
        couponVO.setUseTerm(couponDO.getTitle());
        couponVO.setCouponId(couponDO.getCouponId());
        couponVO.setSellerId(sellerId);
        couponVO.setEndTime(2834947200l);
        couponVOList.add(couponVO);

        OrderDO orderDO = new OrderDO();
        orderDO.setSn(DateUtil.getDateline()+"");
        orderDO.setMemberId(memberId);

        OrderMetaDO giftMeta = new OrderMetaDO();
        giftMeta.setMetaKey(OrderMetaKeyEnum.COUPON.name());
        giftMeta.setMetaValue(JsonUtil.objectToJson(couponVOList));
        giftMeta.setOrderSn(orderDO.getSn());
        giftMeta.setStatus(OrderServiceStatusEnum.NOT_APPLY.name());
        this.orderMetaManager.add(giftMeta);

        changeMsg = new OrderStatusChangeMsg();
        changeMsg.setOldStatus(OrderStatusEnum.CONFIRM);
        changeMsg.setNewStatus(OrderStatusEnum.PAID_OFF);

        changeMsg.setOrderDO(orderDO);

    }


    @Test
    public void test(){
        this.consumer.orderChange(changeMsg);

        CouponDO couponDO2 = this.couponManager.getModel(couponDO.getCouponId());
        Integer num =1;
        Assert.assertEquals(couponDO2.getReceivedNum(),num);

    }
}
