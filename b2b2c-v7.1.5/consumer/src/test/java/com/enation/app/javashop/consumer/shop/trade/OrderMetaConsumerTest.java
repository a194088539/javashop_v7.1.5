package com.enation.app.javashop.consumer.shop.trade;

import com.enation.app.javashop.consumer.shop.trade.consumer.OrderMetaConsumer;
import com.enation.app.javashop.core.member.model.dos.MemberCoupon;
import com.enation.app.javashop.core.member.service.MemberCouponManager;
import com.enation.app.javashop.core.promotion.coupon.model.dos.CouponDO;
import com.enation.app.javashop.core.promotion.fulldiscount.model.dos.FullDiscountGiftDO;
import com.enation.app.javashop.core.trade.cart.model.vo.CouponVO;
import com.enation.app.javashop.core.trade.cart.model.vo.PriceDetailVO;
import com.enation.app.javashop.core.trade.order.model.dos.PayLog;
import com.enation.app.javashop.core.trade.order.model.dto.OrderDTO;
import com.enation.app.javashop.core.trade.order.model.enums.PayStatusEnum;
import com.enation.app.javashop.core.trade.order.model.vo.TradeVO;
import com.enation.app.javashop.core.trade.order.service.PayLogManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单创建其他订单信息入库
 * @author Snow create in 2018/6/27
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
public class OrderMetaConsumerTest extends BaseTest {

    @Autowired
    private OrderMetaConsumer orderMetaConsumer;

    @Autowired
    private MemberCouponManager memberCouponManager;

    @Autowired
    private PayLogManager payLogManager;

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;

    private List<Integer> memberIds = new ArrayList();

    /**
     * 模拟数据
     */
    private TradeVO tradeVO;

    private Integer mcId;

    private OrderDTO orderDTO;


    @Before
    public void testData(){

        int sellerId = 3;
        int memberId = 1;

        CouponDO couponDO = new CouponDO();
        couponDO.setSellerId(sellerId);
        couponDO.setReceivedNum(100);
        couponDO.setUsedNum(0);
        couponDO.setTitle("满100减10元");
        couponDO.setCouponThresholdPrice(100.0);
        couponDO.setCouponPrice(10.0);
        this.daoSupport.insert(couponDO);
        int  couponId = this.daoSupport.getLastId("es_coupon");
        couponDO.setCouponId(couponId);

        MemberCoupon memberCoupon = new MemberCoupon();
        memberCoupon.setMemberId(memberId);
        memberCoupon.setCouponId(couponId);
        memberCoupon.setCouponThresholdPrice(couponDO.getCouponThresholdPrice());
        memberCoupon.setCouponPrice(couponDO.getCouponPrice());
        memberCoupon.setUsedStatus(0);
        this.memberDaoSupport.insert(memberCoupon);
        int memberCouponId = this.memberDaoSupport.getLastId("es_member_coupon");
        mcId = memberCouponId;
        memberCoupon.setMcId(memberCouponId);


        tradeVO = new TradeVO();
        List<OrderDTO> orderDTOList = new ArrayList<>();
        orderDTO = new OrderDTO();

        orderDTO.setSn(DateUtil.getDateline()+"");
        orderDTO.setMemberId(memberId);
        orderDTO.setMemberName("buyer1");

        //使用积分
        PriceDetailVO priceDetailVO = new PriceDetailVO();
        priceDetailVO.setExchangePoint(0L);
        orderDTO.setPrice(priceDetailVO);

        //赠送积分
        orderDTO.setGiftPoint(5);

        //赠优惠券
        List<CouponVO> couponVOList = new ArrayList<>();
        CouponVO couponVO = new CouponVO();
        couponVO.setAmount(10.0);
        couponVO.setUseTerm(couponDO.getTitle());
        couponVO.setMemberCouponId(memberCoupon.getMcId());
        couponVO.setSellerId(sellerId);
        couponVO.setEndTime(2834947200l);
        couponVOList.add(couponVO);
        orderDTO.setGiftCouponList(couponVOList);

        //赠品
        List<FullDiscountGiftDO> giftList = new ArrayList<>();
        FullDiscountGiftDO giftDO = new FullDiscountGiftDO();
        giftDO.setGiftId(1);
        giftDO.setGiftPrice(100.0);
        giftDO.setGiftName("赠品111");
        giftDO.setSellerId(sellerId);
        giftList.add(giftDO);
        orderDTO.setGiftList(giftList);

        //使用的优惠券
        List<CouponVO> couponVOList2 = new ArrayList<>();
        CouponVO couponVO2 = new CouponVO();
        couponVO2.setSellerId(sellerId);
        couponVO2.setMemberCouponId(memberCoupon.getMcId());
        couponVO2.setUseTerm(couponDO.getTitle());
        couponVO2.setEndTime(2834947200l);
        couponVO2.setAmount(couponDO.getCouponPrice());
        couponVOList2.add(couponVO2);
        //orderDTO.setCouponList(couponVOList2);

        orderDTOList.add(orderDTO);
        tradeVO.setOrderList(orderDTOList);

    }


    @Test
    public void testAdd() throws Exception {

        this.orderMetaConsumer.onTradeIntoDb(tradeVO);
        //断言是否已经修改了使用状态
        MemberCoupon memberCoupon = this.memberCouponManager.getModel(1,mcId);
        Integer status =1;
        Assert.assertEquals(memberCoupon.getUsedStatus(),status);

        PayLog payLog = this.payLogManager.getModel(orderDTO.getSn());
        Assert.assertEquals(payLog.getPayStatus(), PayStatusEnum.PAY_NO.name());

    }

    @After
    public void cleanDate() {
        for (Integer memberId : memberIds) {
            this.memberDaoSupport.execute("delete from es_member where member_id = ?", memberId);
        }
    }

}
