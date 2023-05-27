package com.enation.app.javashop.core.promotion.pintuan;

import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.member.MemberAddressClient;
import com.enation.app.javashop.core.client.member.MemberClient;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.goods.model.vo.GoodsSkuVO;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.dos.MemberAddress;
import com.enation.app.javashop.core.promotion.pintuan.model.PintuanOrder;
import com.enation.app.javashop.core.promotion.pintuan.service.PintuanCartManager;
import com.enation.app.javashop.core.promotion.pintuan.service.PintuanOrderManager;
import com.enation.app.javashop.core.promotion.pintuan.service.impl.PintuanTradeManagerImpl;
import com.enation.app.javashop.core.trade.cart.model.vo.CartView;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dto.OrderDTO;
import com.enation.app.javashop.core.trade.order.model.dto.OrderQueryParam;
import com.enation.app.javashop.core.trade.order.model.enums.PaymentTypeEnum;
import com.enation.app.javashop.core.trade.order.model.vo.CheckoutParamVO;
import com.enation.app.javashop.core.trade.order.model.vo.TradeVO;
import com.enation.app.javashop.core.trade.order.service.CheckoutParamManager;
import com.enation.app.javashop.core.trade.order.service.OrderQueryManager;
import com.enation.app.javashop.core.trade.order.service.ShippingManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by kingapex on 2019-01-24.
 * 拼团订单测试
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-01-24
 */
@Rollback(false)
public class PinTuanOrderTest  extends BaseTest {

    @Autowired
    private PintuanCartManager pintuanCartManager;

    @Autowired
    private PintuanTradeManagerImpl pintuanTradeManagerImpl;

    @MockBean
    private CheckoutParamManager checkoutParamManager;

    @MockBean
    protected MemberAddressClient memberAddressClient;

    @MockBean
    protected GoodsClient goodsClient;

    @MockBean
    protected ShippingManager shippingManager;

    @MockBean
    private MemberClient memberClient;



    @Autowired
    private PintuanOrderManager pintuanOrderManager;

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport tradeDaoSupport;

    @Autowired
    private OrderQueryManager orderQueryManager;

    Integer skuId =1;
    Integer goodsId=1;
    Integer addressId  = 1;
    Integer memberId =16;
    Integer sellerId = 1;


    private void clean() {
        tradeDaoSupport.execute(" TRUNCATE TABLE es_pintuan_order ");
        tradeDaoSupport.execute(" TRUNCATE TABLE es_pintuan_child_order ");
    }


    @Test
    public void testList() {

        OrderQueryParam param = new OrderQueryParam();
        param.setMemberId(memberId);
        param.setPageNo(1);
        param.setPageSize(1);
        List lineList  = orderQueryManager.list(param).getData();
        System.out.println(lineList);

    }

    @Test
    public void testCreateOrder() {


        clean();

        //模拟会员
        mockMember1();

        //模拟sku的返回
        mockSku();

        //模拟结算参数
        mockParam();

        //模拟收货地址
        mockAddress();

        Integer areaId =1;

        pintuanCartManager.addSku(skuId, 2);

        CartView cartView = pintuanCartManager.getCart();

        //配置区域校验模拟为 “合法”
        when(shippingManager.checkArea(cartView.getCartList(), areaId)).thenReturn(new ArrayList<>());

        //创建拼团
        TradeVO tradeVO = pintuanTradeManagerImpl.createTrade("WAP",0);

        OrderDTO orderDTO = tradeVO.getOrderList().get(0);
        String orderSn = orderDTO.getSn();

        OrderDO orderDO = new OrderDO();
        orderDO.setSn(orderSn);
        orderDO.setMemberId(orderDTO.getMemberId());

        //支付这个订单
        pintuanOrderManager.payOrder(orderDO);

        //参团
        mockMember2();
        pintuanCartManager.addSku(skuId, 1);
        PintuanOrder pintuanOrder = pintuanOrderManager.getMainOrderBySn(orderSn);
        tradeVO = pintuanTradeManagerImpl.createTrade("WAP", pintuanOrder.getOrderId());

        orderDTO = tradeVO.getOrderList().get(0);
        orderSn = orderDTO.getSn();

        orderDO = new OrderDO();
        orderDO.setSn(orderSn);
        orderDO.setMemberId(orderDTO.getMemberId());
        //支付这个订单
        pintuanOrderManager.payOrder(orderDO);
    }



    private void mockMember2() {

        Buyer buyer = new Buyer();
        buyer.setUid(2);
        buyer.setUsername("testuser2");

        UserContext.mockBuyer = buyer;

        Member member = new Member();
        member.setUname("testuser2");
        member.setMemberId(2);
        member.setFace("//xx/xx.jpg");
        when(memberClient.getModel(2)).thenReturn(member);
    }

    private void mockMember1() {

        Buyer buyer = new Buyer();
        buyer.setUid(memberId);
        buyer.setUsername("testuser1");

        UserContext.mockBuyer = buyer;

        Member member = new Member();
        member.setUname("testuser1");
        member.setMemberId(memberId);
        member.setFace("//xx/xx.jpg");
        when(memberClient.getModel(memberId)).thenReturn(member);
    }

    /**
     * 模拟sku
     */
    private void mockSku() {
        GoodsSkuVO goodsSkuVO = new GoodsSkuVO();

        goodsSkuVO.setSkuId( skuId);
        goodsSkuVO.setGoodsId( goodsId );

        //0下架 1上架
        goodsSkuVO.setMarketEnable(1);
        //0 删除 1 未删除
        goodsSkuVO.setDisabled(1);
        //0：买家承担，1：卖家承担
        goodsSkuVO.setGoodsTransfeeCharge(1);
        goodsSkuVO.setEnableQuantity(10);
        goodsSkuVO.setGoodsName("测试商品");
        goodsSkuVO.setSellerId(1);
        goodsSkuVO.setPrice(100.0);
        goodsSkuVO.setWeight(100D);
        goodsSkuVO.setSn("111-11");
        goodsSkuVO.setSpecList(new ArrayList<>());

        when(goodsClient.getSkuFromCache(skuId)).thenReturn(goodsSkuVO);

        CacheGoods goods = new CacheGoods();
        goods.setGoodsId(goodsId);
        goods.setGoodsName("测试商品");
        goods.setSellerId(sellerId);
        goods.setThumbnail("//xx/xx.jpg");
        goods.setPrice(100.00);
        goods.setEnableQuantity(100);
        goods.setIsAuth(1);
        goods.setSn("1111");
        List<GoodsSkuVO> skuVOList =new ArrayList<>();
        skuVOList.add(goodsSkuVO);
        goods.setSkuList(skuVOList);

        when(goodsClient.getFromCache(goodsId)).thenReturn(goods);
    }


    /**
     * 模拟结算参数
     */
    private void mockParam() {
        CheckoutParamVO param = new CheckoutParamVO();
        param.setPaymentType(PaymentTypeEnum.ONLINE);
        param.setAddressId(addressId);
        param.setClientType("WAP");
        when(checkoutParamManager.getParam()).thenReturn(param);
    }

    /**
     * 模拟收货地址
     */
    private void mockAddress() {
        MemberAddress memberAddress = new MemberAddress();
        memberAddress.setAddrId(addressId);
        memberAddress.setMemberId(memberId);
        memberAddress.setProvinceId(1);
        memberAddress.setProvince("中国");
        memberAddress.setCityId(22);
        memberAddress.setCity("北京");
        memberAddress.setCountyId(333);
        memberAddress.setCounty("通州");
        memberAddress.setName("王峰");
        memberAddress.setMobile("15689565656");
        memberAddress.setTown("某像素");
        memberAddress.setTownId(1);

        when(memberAddressClient.getModel(addressId)).thenReturn(memberAddress);

    }
}
