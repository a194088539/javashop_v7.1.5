package com.enation.app.javashop.buyer.api.trade;

import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.goods.model.vo.GoodsSkuVO;
import com.enation.app.javashop.core.member.service.MemberCouponManager;
import com.enation.app.javashop.core.promotion.fulldiscount.model.vo.FullDiscountVO;
import com.enation.app.javashop.core.promotion.fulldiscount.service.FullDiscountManager;
import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyGoodsVO;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyGoodsManager;
import com.enation.app.javashop.core.promotion.halfprice.model.vo.HalfPriceVO;
import com.enation.app.javashop.core.promotion.minus.model.vo.MinusVO;
import com.enation.app.javashop.core.promotion.minus.service.MinusManager;
import com.enation.app.javashop.core.promotion.seckill.model.vo.SeckillGoodsVO;
import com.enation.app.javashop.core.promotion.seckill.service.SeckillManager;
import com.enation.app.javashop.core.promotion.tool.model.dto.PromotionGoodsDTO;
import com.enation.app.javashop.core.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.javashop.core.promotion.tool.model.vo.PromotionVO;
import com.enation.app.javashop.core.promotion.tool.service.PromotionGoodsManager;
import com.enation.app.javashop.core.trade.TradeErrorCode;
import com.enation.app.javashop.core.trade.cart.model.vo.CartSkuVO;
import com.enation.app.javashop.core.trade.cart.model.vo.CartVO;
import com.enation.app.javashop.core.trade.cart.model.vo.GroupPromotionVO;
import com.enation.app.javashop.core.trade.cart.model.vo.PriceDetailVO;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.exception.SystemErrorCodeV1;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
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
 * 购物车测试脚本
 * @author Snow create in 2018/5/3
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
public class CartControllerTest extends BaseTest {

    //mock 商品对象
    private GoodsSkuVO goodsSkuVO1;
    private GoodsSkuVO goodsSkuVO2;
    private GoodsSkuVO goodsSkuVO3;
    private GoodsSkuVO goodsSkuVO4;
    private GoodsSkuVO goodsSkuVO5;

    private GoodsSkuVO goodsSkuVO6;
    private GoodsSkuVO goodsSkuVO7;
    private GoodsSkuVO goodsSkuVO8;
    private GoodsSkuVO goodsSkuVO9;
    private GoodsSkuVO goodsSkuVO10;
    private GoodsSkuVO goodsSkuVO11;

    @MockBean
    private GoodsClient goodsClient;

    @MockBean
    private PromotionGoodsManager promotionGoodsManager;

    @MockBean
    private MinusManager minusManager;

    @MockBean
    private GroupbuyGoodsManager groupbuyGoodsManager;

    @MockBean
    private SeckillManager seckillManager;

    @MockBean
    private FullDiscountManager fullDiscountManager;

    @MockBean
    private MemberCouponManager memberCouponManager;

    @Before
    public void testData() throws Exception {

        goodsSkuVO1 = this.getGoodsSkuVO(1,1,0,3,100,0.5d);

    }


    /**
     * 场景1_将一个商品添加购物车
     */
    @Test
    public void testAdd1() throws Exception {

        /** 场景1_添加购物车_正确添加一个商品 */
        mockMvc.perform(post("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sku_id",goodsSkuVO1.getSkuId()+"")
                .param("num","1")
                .param("activity_id","0"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        String resultJson = mockMvc.perform(get("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("show_type","all"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        //返回购物车
        List<CartVO> resultCartVOList = JsonUtil.jsonToList(resultJson,CartVO.class);

        //预期的
        List<CartVO> expectedList = new ArrayList<>();
        CartVO cartVO = new CartVO();

        List<GroupPromotionVO> groupPromotionVOList = new ArrayList<>();
        GroupPromotionVO groupPromotionVO = new GroupPromotionVO();

        List<CartSkuVO> cartSkuVOList = new ArrayList<>();
        CartSkuVO cartSkuVO = new CartSkuVO();
        cartSkuVO.setSellerId(goodsSkuVO1.getSellerId());
        cartSkuVO.setGoodsId(goodsSkuVO1.getGoodsId());
        cartSkuVO.setSkuId(goodsSkuVO1.getSkuId());
        cartSkuVO.setNum(1);
        cartSkuVO.setGoodsWeight(goodsSkuVO1.getWeight());
        cartSkuVO.setOriginalPrice(100.0);
        cartSkuVO.setPurchasePrice(100.0);
        cartSkuVO.setSubtotal(100.0);
        cartSkuVO.setName("测试商品");
        cartSkuVO.setChecked(1);
        cartSkuVO.setIsFreeFreight(0);
        cartSkuVO.setServiceStatus("NOT_APPLY");
        cartSkuVO.setSingleList(new ArrayList<>());
        cartSkuVO.setGroupList(new ArrayList<>());
        cartSkuVO.setEnableQuantity(100);
        cartSkuVO.setLastModify(0L);
        cartSkuVOList.add(cartSkuVO);

        groupPromotionVO.setSkuList(cartSkuVOList);
        groupPromotionVO.setIsGroup(2);
        groupPromotionVO.setSubtotal(0.0);
        groupPromotionVOList.add(groupPromotionVO);
        cartVO.setPromotionList(groupPromotionVOList);
        cartVO.setChecked(1);
        expectedList.add(cartVO);
        Assert.assertEquals(expectedList,resultCartVOList);


    }

    /**
     * 场景2_添加购物车_单品立减
     */
    @Test
    public void testAdd2() throws Exception {

        //单品立减活动的商品
        goodsSkuVO6 = this.getGoodsSkuVO(1,1,0,3,100,0.5d);
        this.addMinus(goodsSkuVO6);

        mockMvc.perform(post("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sku_id",goodsSkuVO6.getSkuId()+"")
                .param("num","1"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("show_type","all"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        String resultPriceJson2 = mockMvc.perform(get("/trade/carts/price")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("show_type","all"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        PriceDetailVO resultPriceDetailVO2 = JsonUtil.jsonToObject(resultPriceJson2, PriceDetailVO.class);
        PriceDetailVO priceDetailVO2 = new PriceDetailVO();
        priceDetailVO2.setGoodsPrice(100.0);
        priceDetailVO2.setTotalPrice(90.0);
        priceDetailVO2.setFreightPrice(0.0);
//        priceDetailVO2.setDiscountPrice(10.0);
        priceDetailVO2.setIsFreeFreight(0);
        priceDetailVO2.setExchangePoint(0L);
        Assert.assertEquals(priceDetailVO2,resultPriceDetailVO2);
    }

    /**
     * 场景3_添加购物车_第二件半价
     */
    @Test
    public void testAdd3() throws Exception {

        //第二件半价活动的商品
        goodsSkuVO7 = this.getGoodsSkuVO(1,1,0,3,100,0.5d);
        this.addHalfPrice(goodsSkuVO7);

        mockMvc.perform(post("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sku_id",goodsSkuVO7.getSkuId()+"")
                .param("num","2"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        String resultJson3 = mockMvc.perform(get("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("show_type","all"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        String resultPriceJson3 = mockMvc.perform(get("/trade/carts/price")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("show_type","all"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        PriceDetailVO resultPriceDetailVO2 = JsonUtil.jsonToObject(resultPriceJson3, PriceDetailVO.class);
        PriceDetailVO priceDetailVO2 = new PriceDetailVO();
        priceDetailVO2.setGoodsPrice(200.0);
        priceDetailVO2.setTotalPrice(150.0);
        priceDetailVO2.setFreightPrice(0.0);
        priceDetailVO2.setDiscountPrice(50.0);
        priceDetailVO2.setIsFreeFreight(0);
        priceDetailVO2.setExchangePoint(0L);
        Assert.assertEquals(priceDetailVO2,resultPriceDetailVO2);
    }

    /**
     * 场景4_添加购物车_团购商品
     */
    @Test
    public void testAdd4() throws Exception {

        //团购商品
        goodsSkuVO8 = this.getGoodsSkuVO(1,1,0,3,100,0.5d);
        this.addGroupbuy(goodsSkuVO8);

        mockMvc.perform(post("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sku_id",goodsSkuVO8.getSkuId()+"")
                .param("num","2"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        String resultJson4 = mockMvc.perform(get("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("show_type","all"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        String resultPriceJson4 = mockMvc.perform(get("/trade/carts/price")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("show_type","all"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        PriceDetailVO resultPriceDetailVO4 = JsonUtil.jsonToObject(resultPriceJson4, PriceDetailVO.class);
        PriceDetailVO priceDetailVO4 = new PriceDetailVO();
        priceDetailVO4.setGoodsPrice(200.0);
        priceDetailVO4.setTotalPrice(160.0);
        priceDetailVO4.setFreightPrice(0.0);
        priceDetailVO4.setDiscountPrice(40.0);
        priceDetailVO4.setIsFreeFreight(0);
        priceDetailVO4.setExchangePoint(0L);
        Assert.assertEquals(priceDetailVO4,resultPriceDetailVO4);
    }

    /**
     * 场景5_添加购物车_限时抢购
     */
    @Test
    public void testAdd5() throws Exception {

        //限时抢购商品
        goodsSkuVO9 = this.getGoodsSkuVO(1,1,0,3,100,0.5d);
        this.addSeckill(goodsSkuVO9);

        mockMvc.perform(post("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sku_id",goodsSkuVO9.getSkuId()+"")
                .param("num","2"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("show_type","all"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        String resultPriceJson5 = mockMvc.perform(get("/trade/carts/price")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("show_type","all"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        PriceDetailVO resultPriceDetailVO5 = JsonUtil.jsonToObject(resultPriceJson5, PriceDetailVO.class);
        PriceDetailVO priceDetailVO5 = new PriceDetailVO();
        priceDetailVO5.setGoodsPrice(200.0);
        priceDetailVO5.setTotalPrice(120.0);
        priceDetailVO5.setFreightPrice(0.0);
        priceDetailVO5.setDiscountPrice(80.0);
        priceDetailVO5.setIsFreeFreight(0);
        priceDetailVO5.setExchangePoint(0L);
        Assert.assertEquals(priceDetailVO5,resultPriceDetailVO5);
    }


    /**
     * 场景6_添加购物车_满优惠活动
     */
    @Test
    public void testAdd6() throws Exception {


        //满优惠商品
        goodsSkuVO10 = this.getGoodsSkuVO(1,1,0,3,100,0.5d);
        goodsSkuVO11 = this.getGoodsSkuVO(1,1,0,3,100,0.5d);
        this.addFullDiscount(goodsSkuVO10);

        mockMvc.perform(post("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sku_id",goodsSkuVO10.getSkuId()+"")
                .param("num","1"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        this.addFullDiscount(goodsSkuVO11);
        mockMvc.perform(post("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sku_id",goodsSkuVO11.getSkuId()+"")
                .param("num","1"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("show_type","all"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        String resultPriceJson5 = mockMvc.perform(get("/trade/carts/price")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("show_type","all"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        PriceDetailVO resultPriceDetailVO5 = JsonUtil.jsonToObject(resultPriceJson5, PriceDetailVO.class);
        PriceDetailVO priceDetailVO5 = new PriceDetailVO();
        priceDetailVO5.setGoodsPrice(200.0);
        priceDetailVO5.setTotalPrice(190.0);
        priceDetailVO5.setFreightPrice(0.0);
        priceDetailVO5.setDiscountPrice(10.0);
        priceDetailVO5.setIsFreeFreight(0);
        priceDetailVO5.setExchangePoint(0L);
        Assert.assertEquals(priceDetailVO5,resultPriceDetailVO5);
    }


    /**
     * 场景7_添加购物车_其他验证
     */
    @Test
    public void testAdd7() throws Exception {

        goodsSkuVO2 = this.getGoodsSkuVO(0,1,0,3,100,0.5d);
        goodsSkuVO3 = this.getGoodsSkuVO(1,0,0,3,100,0.5d);
        goodsSkuVO4 = this.getGoodsSkuVO(1,1,0,3,10,0.5d);

        //场景_添加购物车_商品已下架1
        ErrorMessage error  = new ErrorMessage(TradeErrorCode.E451.code(),"此商品已下架");
        mockMvc.perform(post("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sku_id",goodsSkuVO2.getSkuId()+"")
                .param("num","1"))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));



        //场景_添加购物车_此商品被删除
        ErrorMessage error3  = new ErrorMessage(TradeErrorCode.E451.code(),"此商品已下架");
        mockMvc.perform(post("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sku_id",goodsSkuVO3.getSkuId()+"")
                .param("num","1"))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error3 ));



        //场景_添加购物车_商品库存不足
        ErrorMessage error2  = new ErrorMessage(TradeErrorCode.E451.code(),"商品库存不足");
        mockMvc.perform(post("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sku_id",goodsSkuVO4.getSkuId()+"")
                .param("num","12"))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error2 ));
    }



    /**
     * 修改购物车商品数量
     */
    @Test
    public void testUpdate() throws Exception {


        //添加购物车
        mockMvc.perform(post("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sku_id",goodsSkuVO1.getSkuId()+"")
                .param("num","1")
                .param("activity_id","0"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        //场景_更新购物车中的多个产品
        mockMvc.perform(post("/trade/carts/sku/"+goodsSkuVO1.getSkuId())
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sku_id",goodsSkuVO1.getSkuId()+"")
                .param("num","20"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();


        String resultJson = mockMvc.perform(get("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("show_type","all"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        //返回购物车
        List<CartVO> resultCartVOList = JsonUtil.jsonToList(resultJson,CartVO.class);

        //预期的
        List<CartVO> expectedList = new ArrayList<>();
        CartVO cartVO = new CartVO();

        List<GroupPromotionVO> groupPromotionVOList = new ArrayList<>();
        GroupPromotionVO groupPromotionVO = new GroupPromotionVO();

        List<CartSkuVO> cartSkuVOList = new ArrayList<>();
        CartSkuVO cartSkuVO = new CartSkuVO();
        cartSkuVO.setSellerId(goodsSkuVO1.getSellerId());
        cartSkuVO.setGoodsId(goodsSkuVO1.getGoodsId());
        cartSkuVO.setSkuId(goodsSkuVO1.getSkuId());
        cartSkuVO.setNum(20);
        cartSkuVO.setGoodsWeight(goodsSkuVO1.getWeight());
        cartSkuVO.setOriginalPrice(100.0d);
        cartSkuVO.setPurchasePrice(100.0d);
        cartSkuVO.setSubtotal(2000d);
        cartSkuVO.setName("测试商品");
        cartSkuVO.setChecked(1);
        cartSkuVO.setIsFreeFreight(0);
        cartSkuVO.setServiceStatus("NOT_APPLY");
        cartSkuVO.setSingleList(new ArrayList<>());
        cartSkuVO.setGroupList(new ArrayList<>());
        cartSkuVO.setEnableQuantity(100);
        cartSkuVO.setLastModify(0L);
        cartSkuVOList.add(cartSkuVO);

        groupPromotionVO.setSkuList(cartSkuVOList);
        groupPromotionVO.setIsGroup(2);
        groupPromotionVO.setSubtotal(0.0);
        groupPromotionVOList.add(groupPromotionVO);

        cartVO.setPromotionList(groupPromotionVOList);
        cartVO.setChecked(1);
        expectedList.add(cartVO);

        Assert.assertEquals(expectedList,resultCartVOList);
    }




    /**
     * 修改购物车中不存在的商品
     */
    @Test
    public void testUpdate2() throws Exception {

        //场景_更新购物车中的多个产品
        ErrorMessage error  = new ErrorMessage(TradeErrorCode.E451.code(),"商品不存在");

        mockMvc.perform(post("/trade/carts/sku/" + 2222)
                .header("Authorization", buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sku_id", 2222 + "")
                .param("num", "20"))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));
    }


    /**
     * 设置全部商品为选中或不选中
     */
    @Test
    public void testChecked() throws Exception {

        //添加购物车
        mockMvc.perform(post("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sku_id",goodsSkuVO1.getSkuId()+"")
                .param("num","1")
                .param("activity_id","0"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        //场景_设置全部商品为选中或不选中
        mockMvc.perform(post("/trade/carts/checked")
            .header("Authorization",buyer1)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .param("checked","0"))
            .andExpect(status().is(200))
            .andReturn().getResponse().getContentAsString();


        String resultJson = mockMvc.perform(get("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("show_type","all"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        //返回购物车
        List<CartVO> resultCartVOList = JsonUtil.jsonToList(resultJson,CartVO.class);

        //预期的
        List<CartVO> expectedList = new ArrayList<>();
        CartVO cartVO = new CartVO();

        List<GroupPromotionVO> groupPromotionVOList = new ArrayList<>();
        GroupPromotionVO groupPromotionVO = new GroupPromotionVO();

        List<CartSkuVO> cartSkuVOList = new ArrayList<>();
        CartSkuVO cartSkuVO = new CartSkuVO();
        cartSkuVO.setSellerId(goodsSkuVO1.getSellerId());
        cartSkuVO.setGoodsId(goodsSkuVO1.getGoodsId());
        cartSkuVO.setSkuId(goodsSkuVO1.getSkuId());
        cartSkuVO.setNum(1);
        cartSkuVO.setGoodsWeight(goodsSkuVO1.getWeight());
        cartSkuVO.setOriginalPrice(100.0d);
        cartSkuVO.setPurchasePrice(100.0d);
        cartSkuVO.setSubtotal(100.0d);
        cartSkuVO.setName("测试商品");
        cartSkuVO.setChecked(0);
        cartSkuVO.setIsFreeFreight(0);
        cartSkuVO.setServiceStatus("NOT_APPLY");
        cartSkuVO.setSingleList(new ArrayList<>());
        cartSkuVO.setGroupList(new ArrayList<>());
        cartSkuVO.setEnableQuantity(100);
        cartSkuVO.setLastModify(0L);
        cartSkuVOList.add(cartSkuVO);

        groupPromotionVO.setSkuList(cartSkuVOList);
        groupPromotionVO.setIsGroup(2);
        groupPromotionVO.setSubtotal(0.0);
        groupPromotionVO.setSpreadPrice(0.0);
        groupPromotionVO.setDiscountPrice(0.0);
        groupPromotionVOList.add(groupPromotionVO);

        cartVO.setPromotionList(groupPromotionVOList);
        cartVO.setChecked(0);
        expectedList.add(cartVO);

        Assert.assertEquals(expectedList,resultCartVOList);


        //场景_设置全部商品为选中或不选中
        ErrorMessage error  = new ErrorMessage(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER,"是否选中参数异常");
        mockMvc.perform(post("/trade/carts/checked")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("checked","20"))
                .andExpect(status().is(400))
                .andExpect( objectEquals( error ));


    }


    /**
     * 设置全部商品为选中或不选中
     */
    @Test
    public void testSellerChecked() throws Exception {

        goodsSkuVO5 = this.getGoodsSkuVO(1,1,0,4,100,0.5d);


        //添加购物车
        mockMvc.perform(post("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sku_id",goodsSkuVO1.getSkuId()+"")
                .param("num","1")
                .param("activity_id","0"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(post("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sku_id",goodsSkuVO5.getSkuId()+"")
                .param("num","1")
                .param("activity_id","0"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();


        //场景_设置某个商家的全部商品为选中或不选中
        mockMvc.perform(post("/trade/carts/seller/"+goodsSkuVO1.getSellerId())
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("seller_id",goodsSkuVO1.getSellerId()+"")
                .param("checked","0"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();


        String resultJson = mockMvc.perform(get("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("show_type","all"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        //返回购物车
        List<CartVO> resultCartVOList = JsonUtil.jsonToList(resultJson,CartVO.class);

    }



    /****************************************************
     *      内部方法
     *
     ****************************************************/



    /**
     * 根据参数返回不同的商品数据
     * @param marketEnable      0下架 1上架
     * @param disabled          0 删除 1 未删除
     * @param goodsTransfeeCharge   0：买家承担，1：卖家承担
     * @param sellerId          商家id
     * @param enableQuantity    可用库存
     * @param weight            重量(kg)
     * @return
     */
    private GoodsSkuVO getGoodsSkuVO(int marketEnable,int disabled,int goodsTransfeeCharge,int sellerId,int enableQuantity,double weight){

        GoodsSkuVO goodsSkuVO = new GoodsSkuVO();
        goodsSkuVO.setSkuId((int)(Math.random()*1000+1000));
        goodsSkuVO.setGoodsId((int)(Math.random()*2000+1000));
        //0下架 1上架
        goodsSkuVO.setMarketEnable(marketEnable);
        //0 删除 1 未删除
        goodsSkuVO.setDisabled(disabled);
        //0：买家承担，1：卖家承担
        goodsSkuVO.setGoodsTransfeeCharge(goodsTransfeeCharge);
        goodsSkuVO.setEnableQuantity(enableQuantity);
        goodsSkuVO.setGoodsName("测试商品");
        goodsSkuVO.setSellerId(sellerId);
        goodsSkuVO.setPrice(100.0);
        goodsSkuVO.setWeight(weight);
        goodsSkuVO.setSpecList(new ArrayList<>());
        when (goodsClient.getSkuFromCache(goodsSkuVO.getSkuId())).thenReturn(goodsSkuVO);

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
        //1为已上架
        cacheGoods.setMarketEnable(goodsSkuVO.getMarketEnable());
        cacheGoods.setSellerId(goodsSkuVO.getSellerId());
        cacheGoods.setLastModify(0l);
        when (goodsClient.getFromCache(cacheGoods.getGoodsId())).thenReturn(cacheGoods);

        return goodsSkuVO;
    }

    /**
     * 将商品参与单品立减活动（立减10元）
     * @param goodsSkuVO
     */
    public void addMinus(GoodsSkuVO goodsSkuVO){
        //        List<PromotionVO> list = promotionGoodsManager.getPromotion(sku.getGoodsId());
        List<PromotionVO> list = new ArrayList<>();

        List<PromotionGoodsDTO> goodsDTOList = new ArrayList<>();
        PromotionGoodsDTO promotionGoodsDTO = new PromotionGoodsDTO();
        promotionGoodsDTO.setGoodsId(goodsSkuVO.getGoodsId());
        promotionGoodsDTO.setGoodsName(goodsSkuVO.getGoodsName());
        promotionGoodsDTO.setThumbnail(goodsSkuVO.getThumbnail());

        MinusVO minusVO = new MinusVO();
        minusVO.setSellerId(1);
        minusVO.setStartTime(DateUtil.getDateline());
        minusVO.setTitle("单品立减10元");
        minusVO.setGoodsList(goodsDTOList);
        minusVO.setEndTime(2850566400l);
        minusVO.setSingleReductionValue(10.0);
        this.minusManager.add(minusVO);

        PromotionVO promotionVO = new PromotionVO();
        promotionVO.setActivityId(1);
        promotionVO.setGoodsId(goodsSkuVO.getGoodsId());
        promotionVO.setMinusVO(minusVO);
        promotionVO.setPromotionType(PromotionTypeEnum.MINUS.name());

        list.add(promotionVO);

        when (promotionGoodsManager.getPromotion(goodsSkuVO.getGoodsId())).thenReturn(list);
        when (minusManager.getFromDB(promotionVO.getActivityId())).thenReturn(minusVO);

    }

    /**
     * 将商品参与第二件半价活动（只有第二件是半价）
     * @param goodsSkuVO
     */
    public void addHalfPrice(GoodsSkuVO goodsSkuVO){
        List<PromotionVO> list = new ArrayList<>();

        List<PromotionGoodsDTO> goodsDTOList = new ArrayList<>();
        PromotionGoodsDTO promotionGoodsDTO = new PromotionGoodsDTO();
        promotionGoodsDTO.setGoodsId(goodsSkuVO.getGoodsId());
        promotionGoodsDTO.setGoodsName(goodsSkuVO.getGoodsName());
        promotionGoodsDTO.setThumbnail(goodsSkuVO.getThumbnail());

        HalfPriceVO halfPriceVO = new HalfPriceVO();
        halfPriceVO.setSellerId(1);
        halfPriceVO.setTitle("第二件半价活动");
        halfPriceVO.setStartTime(DateUtil.getDateline());
        halfPriceVO.setEndTime(2850566400l);
        halfPriceVO.setGoodsList(goodsDTOList);

        PromotionVO promotionVO = new PromotionVO();
        promotionVO.setActivityId(2);
        promotionVO.setGoodsId(goodsSkuVO.getGoodsId());
        promotionVO.setHalfPriceVO(halfPriceVO);
        promotionVO.setPromotionType(PromotionTypeEnum.HALF_PRICE.name());

        list.add(promotionVO);

        when (promotionGoodsManager.getPromotion(goodsSkuVO.getGoodsId())).thenReturn(list);
    }


    /**
     * 将商品参与团购活动（团购价80元）
     * @param goodsSkuVO
     */
    public void addGroupbuy(GoodsSkuVO goodsSkuVO){

        List<PromotionVO> list = new ArrayList<>();

        GroupbuyGoodsVO groupbuyGoodsVO = new GroupbuyGoodsVO();
        groupbuyGoodsVO.setSellerId(1);
        groupbuyGoodsVO.setAddTime(DateUtil.getDateline());
        groupbuyGoodsVO.setGbTitle("团购活动");
        groupbuyGoodsVO.setPrice(80.0);
        groupbuyGoodsVO.setGoodsNum(100);
        groupbuyGoodsVO.setOriginalPrice(100.0);
        groupbuyGoodsVO.setGoodsId(goodsSkuVO.getGoodsId());

        PromotionVO promotionVO = new PromotionVO();
        promotionVO.setActivityId(3);
        promotionVO.setGoodsId(goodsSkuVO.getGoodsId());
        promotionVO.setGroupbuyGoodsVO(groupbuyGoodsVO);
        promotionVO.setPromotionType(PromotionTypeEnum.GROUPBUY.name());

        list.add(promotionVO);

        when (promotionGoodsManager.getPromotion(goodsSkuVO.getGoodsId())).thenReturn(list);

        when (groupbuyGoodsManager.getModel(promotionVO.getActivityId(),goodsSkuVO.getGoodsId())).thenReturn(groupbuyGoodsVO);

    }


    /**
     * 将商品参与限时抢购（抢购价60.0元）
     * @param goodsSkuVO
     */
    public void addSeckill(GoodsSkuVO goodsSkuVO) {
        List<PromotionVO> list = new ArrayList<>();

        SeckillGoodsVO seckillGoodsVO = new SeckillGoodsVO();
        seckillGoodsVO.setSeckillPrice(60.0);
        seckillGoodsVO.setSkuId(goodsSkuVO.getSkuId());
        seckillGoodsVO.setGoodsId(goodsSkuVO.getGoodsId());
        seckillGoodsVO.setIsStart(1);
        seckillGoodsVO.setSoldQuantity(100);
        seckillGoodsVO.setRemainQuantity(100);
        seckillGoodsVO.setSoldNum(1);

        PromotionVO promotionVO = new PromotionVO();
        promotionVO.setActivityId(3);
        promotionVO.setSeckillGoodsVO(seckillGoodsVO);
        promotionVO.setPromotionType(PromotionTypeEnum.SECKILL.name());

        list.add(promotionVO);

        when (promotionGoodsManager.getPromotion(goodsSkuVO.getGoodsId())).thenReturn(list);

        when (seckillManager.getSeckillGoods(seckillGoodsVO.getGoodsId())).thenReturn(seckillGoodsVO);

    }


    /**
     * 将商品参与满优惠（满100减10元）
     * @param goodsSkuVO
     */
    public void addFullDiscount(GoodsSkuVO goodsSkuVO) {
        List<PromotionVO> list = new ArrayList<>();

        List<PromotionGoodsDTO> goodsDTOList = new ArrayList<>();
        PromotionGoodsDTO goodsDTO = new PromotionGoodsDTO();
        goodsDTO.setGoodsId(goodsSkuVO.getGoodsId());
        goodsDTO.setThumbnail(goodsSkuVO.getThumbnail());
        goodsDTO.setGoodsName(goodsSkuVO.getGoodsName());
        goodsDTOList.add(goodsDTO);

        FullDiscountVO fullDiscountVO = new FullDiscountVO();
        fullDiscountVO.setSellerId(5);
        fullDiscountVO.setTitle("满100减10元");
        fullDiscountVO.setStartTime(DateUtil.getDateline());
        fullDiscountVO.setEndTime(2850566400l);
        fullDiscountVO.setIsFullMinus(1);
        fullDiscountVO.setMinusValue(10.0);
        fullDiscountVO.setGoodsList(goodsDTOList);
        fullDiscountVO.setFdId(5);
        fullDiscountVO.setFullMoney(100.0);

        PromotionVO promotionVO = new PromotionVO();
        promotionVO.setActivityId(fullDiscountVO.getFdId());
        promotionVO.setFullDiscountVO(fullDiscountVO);
        promotionVO.setPromotionType(PromotionTypeEnum.FULL_DISCOUNT.name());

        list.add(promotionVO);

        when (promotionGoodsManager.getPromotion(goodsSkuVO.getGoodsId())).thenReturn(list);

        when (fullDiscountManager.getModel(fullDiscountVO.getFdId())).thenReturn(fullDiscountVO);
    }




    @After
    public void cleanCache() throws Exception {
        mockMvc.perform(delete("/trade/carts")
                .header("Authorization",buyer1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
    }


}
