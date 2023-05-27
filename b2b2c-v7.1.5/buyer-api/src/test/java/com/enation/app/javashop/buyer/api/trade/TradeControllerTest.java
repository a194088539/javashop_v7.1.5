package com.enation.app.javashop.buyer.api.trade;

import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.member.MemberAddressClient;
import com.enation.app.javashop.core.client.member.MemberClient;
import com.enation.app.javashop.core.client.member.ShipTemplateClient;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.goods.model.vo.GoodsSkuVO;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.dos.MemberAddress;
import com.enation.app.javashop.core.member.model.dos.MemberCoupon;
import com.enation.app.javashop.core.member.service.MemberCouponManager;
import com.enation.app.javashop.core.shop.model.vo.ShipTemplateChildBuyerVO;
import com.enation.app.javashop.core.shop.model.vo.ShipTemplateVO;
import com.enation.app.javashop.core.trade.TradeErrorCode;
import com.enation.app.javashop.core.trade.cart.model.vo.PriceDetailVO;
import com.enation.app.javashop.core.trade.order.model.vo.CashierVO;
import com.enation.app.javashop.core.trade.order.model.vo.TradeVO;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 交易控制器测试
 *
 * @author Snow create in 2018/5/8
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager", rollbackFor = Exception.class)
public class TradeControllerTest extends BaseTest {

    @MockBean
    private GoodsClient goodsClient;

    @MockBean
    private MemberCouponManager memberCouponManager;

    @MockBean
    private MemberAddressClient memberAddressClient;

    @MockBean
    private ShipTemplateClient shipTemplateClient;

    @MockBean
    private MemberClient memberClient;

    private GoodsSkuVO goodsSkuVO1;

    private MemberAddress memberAddress;


    @Before
    public void testData() throws Exception {

        memberAddress = new MemberAddress();
        memberAddress.setMemberId(1);
        memberAddress.setAddrId(1);
        memberAddress.setProvinceId(1);
        memberAddress.setProvince("中国");
        memberAddress.setCityId(22);
        memberAddress.setCity("北京");
        memberAddress.setCountyId(333);
        memberAddress.setCounty("通州");
        when(memberAddressClient.getModel(memberAddress.getAddrId())).thenReturn(memberAddress);


        Member member = new Member();
        member.setUname("test");
        member.setConsumPoint(10L);
        member.setMemberId(1);
        when(memberClient.getModel(member.getMemberId())).thenReturn(member);

        goodsSkuVO1 = this.getGoodsSkuVO(1, 1, 0, 3, 100, 5.0);

        /** 加入购物车 */
        mockMvc.perform(post("/trade/carts")
                .header("Authorization", buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sku_id", goodsSkuVO1.getSkuId() + "")
                .param("num", "1")
                .param("activity_id", "0"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();


        /** 设置结算参数 */
        //设置收货地址
        mockMvc.perform(post("/trade/checkout-params/address-id/" + memberAddress.getAddrId())
                .header("Authorization", buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("address_id", "1"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        //设置支付方式
        mockMvc.perform(post("/trade/checkout-params/payment-type")
                .header("Authorization", buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("payment_type", "ONLINE"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

    }


    /**
     * 提交订单
     *
     * @throws Exception
     */
    @Test
    public void testCreate() throws Exception {

        //读取价格
        String priceJson = mockMvc.perform(get("/trade/price")
                .header("Authorization", buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        //运费
        Double freightPrice = 30.0;
        PriceDetailVO priceDetailVO = JsonUtil.jsonToObject(priceJson, PriceDetailVO.class);
        Assert.assertEquals(freightPrice, priceDetailVO.getFreightPrice());

        //创建订单
        String json = mockMvc.perform(post("/trade/create")
                .header("Authorization", buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        TradeVO tradeVO = JsonUtil.jsonToObject(json, TradeVO.class);
        String tradeSn = tradeVO.getTradeSn();

        String resultJson = mockMvc.perform(get("/trade/orders/cashier")
                .header("Authorization", buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("trade_sn", tradeSn))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        CashierVO cashierVO = JsonUtil.jsonToObject(resultJson, CashierVO.class);
        Assert.assertEquals(cashierVO.getNeedPayPrice(), tradeVO.getPriceDetail().getTotalPrice());

        //购物车为空校验
        ErrorMessage error = new ErrorMessage(TradeErrorCode.E452.code(), "购物车为空");
        mockMvc.perform(post("/trade/create")
                .header("Authorization", buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

    }


    /**
     * 使用优惠券，验证结算页价格
     *
     * @throws Exception
     */
    @Test
    public void testCoupon() throws Exception {

        MemberCoupon memberCoupon = new MemberCoupon();
        memberCoupon.setCouponPrice(20.0);
        memberCoupon.setCouponThresholdPrice(50.0);
        memberCoupon.setStartTime(1550566400l);
        memberCoupon.setEndTime(2850566400l);
        memberCoupon.setMcId(1);
        memberCoupon.setMemberId(1);

        when(this.memberCouponManager.getModel(memberCoupon.getMemberId(), memberCoupon.getMcId())).thenReturn(memberCoupon);

        mockMvc.perform(post("/trade/" + goodsSkuVO1.getSellerId() + "/seller/" + memberCoupon.getMcId() + "/coupon")
                .header("Authorization", buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

    }


    /**
     * 根据参数返回不同的商品数据
     *
     * @param marketEnable        0下架 1上架
     * @param disabled            0 删除 1 未删除
     * @param goodsTransfeeCharge 0：买家承担，1：卖家承担
     * @param sellerId            商家id
     * @param enableQuantity      可用库存
     * @param weight              重量(kg)
     * @return
     */
    private GoodsSkuVO getGoodsSkuVO(int marketEnable, int disabled, int goodsTransfeeCharge, int sellerId, int enableQuantity, double weight) {

        GoodsSkuVO goodsSkuVO = new GoodsSkuVO();
        goodsSkuVO.setSkuId((int) (Math.random() * 1000 + 1000));
        goodsSkuVO.setGoodsId((int) (Math.random() * 2000 + 1000));
        //0下架 1上架
        goodsSkuVO.setMarketEnable(marketEnable);
        //0 删除 1 未删除
        goodsSkuVO.setDisabled(disabled);
        //0：买家承担，1：卖家承担
        goodsSkuVO.setGoodsTransfeeCharge(goodsTransfeeCharge);
        goodsSkuVO.setEnableQuantity(enableQuantity);
        goodsSkuVO.setGoodsName("测试商品");
        goodsSkuVO.setSellerId(sellerId);
        goodsSkuVO.setSellerName("测试店铺");
        goodsSkuVO.setPrice(100.0d);
        goodsSkuVO.setWeight(weight);
        goodsSkuVO.setSellerId(3);
        goodsSkuVO.setSpecList(new ArrayList<>());
        when(goodsClient.getSkuFromCache(goodsSkuVO.getSkuId())).thenReturn(goodsSkuVO);

        CacheGoods cacheGoods = new CacheGoods();
        cacheGoods.setGoodsId(goodsSkuVO.getGoodsId());
        List<GoodsSkuVO> skuList = new ArrayList<>();
        skuList.add(goodsSkuVO);
        cacheGoods.setSkuList(skuList);
        cacheGoods.setEnableQuantity(goodsSkuVO.getEnableQuantity());
        cacheGoods.setQuantity(100);
        cacheGoods.setGoodsName("测试商品");
        //0 未审核  1 通过 2 不通过
        cacheGoods.setIsAuth(1);
        cacheGoods.setPrice(goodsSkuVO.getPrice());
        cacheGoods.setSn("abc123456");
        //0：买家承担，1：卖家承担
        cacheGoods.setGoodsTransfeeCharge(goodsSkuVO.getGoodsTransfeeCharge());
        cacheGoods.setTemplateId(1);
        cacheGoods.setSellerId(3);
        //1为已上架
        cacheGoods.setMarketEnable(goodsSkuVO.getMarketEnable());
        cacheGoods.setSellerId(goodsSkuVO.getSellerId());
        when(goodsClient.getFromCache(cacheGoods.getGoodsId())).thenReturn(cacheGoods);


        ShipTemplateVO temp = new ShipTemplateVO();
        temp.setId(1);
        temp.setName("测试模板");
        temp.setType(1);
        ShipTemplateChildBuyerVO child = new ShipTemplateChildBuyerVO();

        child.setFirstCompany(1D);
        child.setFirstPrice(10.0);
        child.setContinuedCompany(1D);
        child.setContinuedPrice(5.0);
        child.setAreaId("1");
        List<ShipTemplateChildBuyerVO> items = new ArrayList<>();
        items.add(child);
        temp.setItems(items);

        when(this.shipTemplateClient.get(1)).thenReturn(temp);

        return goodsSkuVO;
    }


    @After
    public void cleanCache() throws Exception {
        this.cleanCartCache();
    }


    /**
     * 清空购物车的缓存数据
     *
     * @throws Exception
     */
    private void cleanCartCache() throws Exception {
        mockMvc.perform(delete("/trade/carts")
                .header("Authorization", buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
    }


}
