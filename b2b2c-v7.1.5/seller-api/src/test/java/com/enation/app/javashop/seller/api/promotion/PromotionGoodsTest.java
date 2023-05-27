package com.enation.app.javashop.seller.api.promotion;

import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.promotion.PromotionErrorCode;
import com.enation.app.javashop.core.promotion.exchange.service.ExchangeGoodsManager;
import com.enation.app.javashop.core.promotion.fulldiscount.model.vo.FullDiscountVO;
import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyActiveDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyGoodsDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.enums.GroupBuyGoodsStatusEnum;
import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyAuditParam;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyActiveManager;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyGoodsManager;
import com.enation.app.javashop.core.promotion.halfprice.model.vo.HalfPriceVO;
import com.enation.app.javashop.core.promotion.minus.model.vo.MinusVO;
import com.enation.app.javashop.core.promotion.seckill.model.dos.SeckillApplyDO;
import com.enation.app.javashop.core.promotion.seckill.model.dto.SeckillAuditParam;
import com.enation.app.javashop.core.promotion.seckill.model.enums.SeckillGoodsApplyStatusEnum;
import com.enation.app.javashop.core.promotion.seckill.model.enums.SeckillStatusEnum;
import com.enation.app.javashop.core.promotion.seckill.model.vo.SeckillVO;
import com.enation.app.javashop.core.promotion.seckill.service.SeckillGoodsManager;
import com.enation.app.javashop.core.promotion.seckill.service.SeckillManager;
import com.enation.app.javashop.core.promotion.tool.model.vo.ConflictGoodsVO;
import com.enation.app.javashop.core.promotion.tool.model.vo.PromotionVO;
import com.enation.app.javashop.core.promotion.tool.service.PromotionGoodsManager;
import com.enation.app.javashop.core.promotion.tool.support.PromotionCacheKeys;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessageWithData;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import net.sf.json.JSONArray;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 促销工具测试
 * @author Snow create in 2018/7/4
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
public class PromotionGoodsTest extends BaseTest {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport goodsDaoSupport;

    @Autowired
    private PromotionGoodsManager promotionGoodsManager;

    @MockBean
    private GoodsClient goodsClient;

    @Autowired
    private GroupbuyGoodsManager groupbuyGoodsManager;

    @Autowired
    private GroupbuyActiveManager groupbuyActiveManager;

    @Autowired
    private ExchangeGoodsManager exchangeGoodsManager;

    @Autowired
    private SeckillManager seckillManager;

    @Autowired
    private SeckillGoodsManager seckillApplyManager;

    @Autowired
    private Cache cache;

    private List<Integer> goodsIds = new ArrayList<>();

    //单品立减部分商品参与
    private GoodsDO goodsDO;

    //活动开始时间，当前时间加3秒
    private long startTime;

    //休眠时间4秒,毫秒为单位
    private long sleepTime = 4*1000;


    @Before
    public void testData() throws Exception {

        goodsDO = new GoodsDO();
        goodsDO.setGoodsName("测试商品名称");
        goodsDO.setSn(DateUtil.getDateline()+"");
        goodsDO.setSellerId(3);
        goodsDO.setThumbnail("http://测试图片");
        goodsDO.setPrice(100.0);
        goodsDO.setQuantity(100);
        this.goodsDaoSupport.insert(goodsDO);
        int goodsId = this.goodsDaoSupport.getLastId("es_goods");
        goodsDO.setGoodsId(goodsId);
        goodsIds.add(goodsId);

        CacheGoods cacheGoods = new CacheGoods();
        cacheGoods.setGoodsId(goodsId);
        cacheGoods.setSellerId(goodsDO.getSellerId());

        when (this.goodsClient.getFromCache(cacheGoods.getGoodsId())).thenReturn(cacheGoods);


        startTime = DateUtil.getDateline()+3;

    }

    @Test
    public void testMinusPart() throws Exception {

        Map successData = new HashMap();
        String[] names = new String[]{"title","start_time","end_time","range_type","description","single_reduction_value","message"};
        String[] values0 = new String[]{"标题",startTime+"","2850566400","2","描述","10"};
        for(int i  = 0; i<names.length-1; i++){
            successData.put(names[i],values0[i]);
        }

        StringBuffer goodsListJson = new StringBuffer();
        goodsListJson.append("[");
        goodsListJson.append(   "{");
        goodsListJson.append(       "\"goods_id\":"+goodsDO.getGoodsId()+",");
        goodsListJson.append(       "\"goods_name\":\""+goodsDO.getGoodsName()+"\",");
        goodsListJson.append(       "\"thumbnail\":\""+goodsDO.getThumbnail()+"\"");
        goodsListJson.append(   "}" );
        goodsListJson.append("]");

        successData.put("goods_list",JSONArray.fromObject(goodsListJson.toString()));

        String resultJson = mockMvc.perform(post("/seller/promotion/minus")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(successData)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        MinusVO minusVO = JsonUtil.jsonToObject(resultJson,MinusVO.class);

        Thread.sleep(sleepTime);

        List<PromotionVO> promotionVOList = this.promotionGoodsManager.getPromotion(goodsDO.getGoodsId());
        Assert.assertEquals(minusVO.getTitle(),promotionVOList.get(0).getMinusVO().getTitle());
        Assert.assertEquals(minusVO.getSingleReductionValue(),promotionVOList.get(0).getMinusVO().getSingleReductionValue());
    }

//    @Test
//    public void testMinusTotal() throws Exception {
//
//        Map successData = new HashMap();
//        String[] names = new String[]{"title","start_time","end_time","range_type","description","single_reduction_value","message"};
//        String[] values0 = new String[]{"标题",startTime+"","2850566400","1","描述","10"};
//        for(int i  = 0; i<names.length-1; i++){
//            successData.put(names[i],values0[i]);
//        }
//
//        String resultJson = mockMvc.perform(post("/seller/promotion/minus")
//                .header("Authorization",seller1)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(JsonUtil.objectToJson(successData)))
//                .andExpect(status().is(200))
//                .andReturn().getResponse().getContentAsString();
//        MinusVO minusVO = JsonUtil.jsonToObject(resultJson,MinusVO.class);
//        Thread.sleep(sleepTime);
//
//        List<PromotionVO> promotionVOList = this.promotionGoodsManager.getPromotion(goodsDO.getGoodsId());
//        Assert.assertEquals(minusVO.getTitle(),promotionVOList.get(0).getMinusVO().getTitle());
//        Assert.assertEquals(minusVO.getSingleReductionValue(),promotionVOList.get(0).getMinusVO().getSingleReductionValue());
//
//    }


    @Test
    public void testHalfPricePart() throws Exception {
        Map successData = new HashMap();
        String[] names = new String[]{"title","start_time","end_time","range_type","description"};
        String[] values0 = new String[]{"第二件半价活动",startTime+"","2850566400","2","描述"};
        for(int i  = 0; i<names.length-1; i++){
            successData.put(names[i],values0[i]);
        }

        StringBuffer goodsListJson = new StringBuffer();
        goodsListJson.append("[");
        goodsListJson.append(   "{");
        goodsListJson.append(       "\"goods_id\":"+goodsDO.getGoodsId()+",");
        goodsListJson.append(       "\"goods_name\":\""+goodsDO.getGoodsName()+"\",");
        goodsListJson.append(       "\"thumbnail\":\""+goodsDO.getThumbnail()+"\"");
        goodsListJson.append(   "}" );
        goodsListJson.append("]");

        successData.put("goods_list", JSONArray.fromObject(goodsListJson.toString()));


        String resultJson =  mockMvc.perform(post("/seller/promotion/half-prices")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(successData)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        HalfPriceVO halfPriceVO = JsonUtil.jsonToObject(resultJson,HalfPriceVO.class);

        Thread.sleep(sleepTime);

        List<PromotionVO> promotionVOList = this.promotionGoodsManager.getPromotion(goodsDO.getGoodsId());
        Assert.assertEquals(halfPriceVO.getTitle(),promotionVOList.get(0).getHalfPriceVO().getTitle());
    }


//    @Test
//    public void testHalfPriceTotal() throws Exception {
//
//        Map successData = new HashMap();
//        String[] names = new String[]{"title","start_time","end_time","range_type","description"};
//        String[] values0 = new String[]{"第二件半价活动",startTime+"","2850566400","1","描述"};
//        for(int i  = 0; i<names.length-1; i++){
//            successData.put(names[i],values0[i]);
//        }
//
//        String resultJson =  mockMvc.perform(post("/seller/promotion/half-prices")
//                .header("Authorization",seller1)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(JsonUtil.objectToJson(successData)))
//                .andExpect(status().is(200))
//                .andReturn().getResponse().getContentAsString();
//        HalfPriceVO halfPriceVO = JsonUtil.jsonToObject(resultJson,HalfPriceVO.class);
//
//        Thread.sleep(sleepTime);
//
//        List<PromotionVO> promotionVOList = this.promotionGoodsManager.getPromotion(goodsDO.getGoodsId());
//        Assert.assertEquals(halfPriceVO.getTitle(),promotionVOList.get(0).getHalfPriceVO().getTitle());
//    }



    @Test
    public void testFullDiscountPart() throws Exception {
        Map successData = new HashMap();
        String[] names = new String[]{
                "title","start_time","end_time","range_type","description",
                "full_money","is_full_minus","minus_value","is_discount","discount_value",
                "is_free_ship","is_send_gift","gift_id","is_send_bonus","bonus_id"};

        String[] values0 = new String[]{
                "满优惠活动标题",startTime+"","2850566400","2","测试描述", "100.0","1","10","0","0", "0","0","0","0","0"
        };

        for(int i  = 0; i<names.length-1; i++){
            successData.put(names[i],values0[i]);
        }

        StringBuffer goodsListJson = new StringBuffer();
        goodsListJson.append("[");
        goodsListJson.append(   "{");
        goodsListJson.append(       "\"goods_id\":"+goodsDO.getGoodsId()+",");
        goodsListJson.append(       "\"goods_name\":\""+goodsDO.getGoodsName()+"\",");
        goodsListJson.append(       "\"thumbnail\":\""+goodsDO.getThumbnail()+"\"");
        goodsListJson.append(   "}" );
        goodsListJson.append("]");

        successData.put("goods_list",JSONArray.fromObject(goodsListJson.toString()));

        String json =mockMvc.perform(post("/seller/promotion/full-discounts")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(successData)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        FullDiscountVO fullDiscountVO = JsonUtil.jsonToObject(json,FullDiscountVO.class);
        Thread.sleep(sleepTime);

        List<PromotionVO> promotionVOList = this.promotionGoodsManager.getPromotion(goodsDO.getGoodsId());
        Assert.assertEquals(fullDiscountVO.getTitle(),promotionVOList.get(0).getFullDiscountVO().getTitle());
    }

//    @Test
//    public void testFullDiscountTotal() throws Exception {
//        Map successData = new HashMap();
//        String[] names = new String[]{
//                "title","start_time","end_time","range_type","description",
//                "full_money","is_full_minus","minus_value","is_discount","discount_value",
//                "is_free_ship","is_send_gift","gift_id","is_send_bonus","bonus_id"};
//
//        String[] values0 = new String[]{
//                "满优惠活动标题",startTime+"","2850566400","1","测试描述", "100.0","1","10","0","0", "0","0","0","0","0"
//        };
//
//        for(int i  = 0; i<names.length-1; i++){
//            successData.put(names[i],values0[i]);
//        }
//
//        String json =mockMvc.perform(post("/seller/promotion/full-discounts")
//                .header("Authorization",seller1)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(JsonUtil.objectToJson(successData)))
//                .andExpect(status().is(200))
//                .andReturn().getResponse().getContentAsString();
//
//        Thread.sleep(sleepTime);
//
//        FullDiscountVO fullDiscountVO = JsonUtil.jsonToObject(json,FullDiscountVO.class);
//        List<PromotionVO> promotionVOList = this.promotionGoodsManager.getPromotion(goodsDO.getGoodsId());
//        Assert.assertEquals(fullDiscountVO.getTitle(),promotionVOList.get(0).getFullDiscountVO().getTitle());
//    }


    @Test
    public void testGroupbuy() throws Exception {

        //添加活动
        GroupbuyActiveDO activeDO = new GroupbuyActiveDO();
        activeDO.setStartTime(DateUtil.getDateline());
        activeDO.setEndTime(2850566400l);
        activeDO.setActName("双11团购活动");
        activeDO.setJoinEndTime(2450566400l);
        this.groupbuyActiveManager.add(activeDO);
        int  id = this.daoSupport.getLastId("es_groupbuy_active");
        activeDO.setActId(id);

        //商品参与活动
        GroupbuyGoodsDO groupbuyGoodsDO = new GroupbuyGoodsDO();
        groupbuyGoodsDO.setSellerId(3);
        groupbuyGoodsDO.setActId(activeDO.getActId());
        groupbuyGoodsDO.setAddTime(DateUtil.getDateline());
        groupbuyGoodsDO.setGbTitle("团购商品2222");
        groupbuyGoodsDO.setPrice(80.0);
        groupbuyGoodsDO.setGoodsNum(100);
        groupbuyGoodsDO.setOriginalPrice(100.0);
        groupbuyGoodsDO.setGoodsId(goodsDO.getGoodsId());
        this.groupbuyGoodsManager.add(groupbuyGoodsDO);
        int gbId = this.daoSupport.getLastId("es_groupbuy_goods");
        groupbuyGoodsDO.setGbId(gbId);

        Thread.sleep(sleepTime);

        //审核商品
        GroupbuyAuditParam param = new GroupbuyAuditParam();
        Integer[] acIds = new Integer[1];
        acIds[0] = gbId;
        param.setActId(activeDO.getActId());
        param.setGbIds(acIds);
        param.setStatus(GroupBuyGoodsStatusEnum.APPROVED.status());
        this.groupbuyActiveManager.batchAuditGoods(param);

        List<PromotionVO> promotionVOList = this.promotionGoodsManager.getPromotion(goodsDO.getGoodsId());
        Assert.assertEquals(groupbuyGoodsDO.getGbTitle(),promotionVOList.get(0).getGroupbuyGoodsVO().getGbTitle());
        Assert.assertEquals(groupbuyGoodsDO.getPrice(),promotionVOList.get(0).getGroupbuyGoodsVO().getPrice());


        //待完善
        startTime += 10;

        Map successData = new HashMap();
        String[] names = new String[]{"title","start_time","end_time","range_type","description","single_reduction_value","message"};
        String[] values0 = new String[]{"标题",startTime+"","2850566400","2","描述","10"};
        for(int i  = 0; i<names.length-1; i++){
            successData.put(names[i],values0[i]);
        }

        StringBuffer goodsListJson = new StringBuffer();
        goodsListJson.append("[");
        goodsListJson.append(   "{");
        goodsListJson.append(       "\"goods_id\":"+goodsDO.getGoodsId()+",");
        goodsListJson.append(       "\"goods_name\":\""+goodsDO.getGoodsName()+"\",");
        goodsListJson.append(       "\"thumbnail\":\""+goodsDO.getThumbnail()+"\"");
        goodsListJson.append(   "}" );
        goodsListJson.append("]");

        successData.put("goods_list",JSONArray.fromObject(goodsListJson.toString()));


        List<ConflictGoodsVO> goodsVOList = new ArrayList<>();
        ConflictGoodsVO goodsVO = new ConflictGoodsVO();
        goodsVO.setName(goodsDO.getGoodsName());
        goodsVO.setThumbnail(goodsDO.getThumbnail());
        goodsVO.setTitle(activeDO.getActName());
        goodsVO.setStartTime(activeDO.getStartTime());
        goodsVO.setEndTime(activeDO.getEndTime());

        goodsVOList.add(goodsVO);


        ErrorMessageWithData error  = new ErrorMessageWithData(PromotionErrorCode.E401.code(),"活动商品冲突列表",JsonUtil.objectToJson(goodsVOList));
        mockMvc.perform(post("/seller/promotion/minus")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(successData)))
                .andExpect(status().is(500))
                .andExpect( objectEquals( error ));

    }



    @Test
    public void testSeckill() throws Exception {

        CacheGoods goods = new CacheGoods();
        goods.setThumbnail("path");
        goods.setGoodsId(goodsDO.getGoodsId());
        goods.setGoodsName(goodsDO.getGoodsName());
        goods.setPrice(goodsDO.getPrice());
        goods.setEnableQuantity(101);
        when (goodsClient.getFromCache(goodsDO.getGoodsId())).thenReturn(goods);

        //添加活动
        SeckillVO seckillVO = new SeckillVO();
        seckillVO.setApplyEndTime(2450566400l);
        seckillVO.setStartDay(startTime);
        seckillVO.setSeckillName("限时抢购活动");
        seckillVO.setSeckillStatus(SeckillStatusEnum.RELEASE.name());
        seckillVO.setSeckillRule("123132");
        List<Integer> rangeList = new ArrayList<>();
        Integer time = 1;
        rangeList.add(time);
        seckillVO.setRangeList(rangeList);
        this.seckillManager.add(seckillVO);

        //选择商品申请活动
        List<SeckillApplyDO> list = new ArrayList<>();
        SeckillApplyDO applyDO = new SeckillApplyDO();
        applyDO.setSeckillId(seckillVO.getSeckillId());
        applyDO.setGoodsId(goodsDO.getGoodsId());
        applyDO.setGoodsName(goodsDO.getGoodsName());
        applyDO.setPrice(55.5);
        applyDO.setTimeLine(time);
        applyDO.setStartDay(startTime);
        applyDO.setStatus(SeckillGoodsApplyStatusEnum.APPLY.name());
        applyDO.setSoldQuantity(100);
        list.add(applyDO);
        this.seckillApplyManager.addApply(list);

        //审核商品
        SeckillAuditParam param = new SeckillAuditParam();
        Integer[] applyIds = new Integer[1];
        applyIds[0] = list.get(0).getApplyId();
        param.setApplyIds(applyIds);
        param.setStatus(SeckillGoodsApplyStatusEnum.PASS.name());
        param.setFailReason("");
        this.seckillManager.batchAuditGoods(param);

        Thread.sleep(sleepTime);

        List<PromotionVO> promotionVOList = this.promotionGoodsManager.getPromotion(goodsDO.getGoodsId());
        Assert.assertEquals(applyDO.getPrice(),promotionVOList.get(0).getSeckillGoodsVO().getSeckillPrice());
    }


    @After
    public void cleanDate(){
        for (Integer goodsId:goodsIds){
            this.goodsDaoSupport.execute("delete from es_goods where goods_id = ?",goodsId);
        }

        String redisKey = PromotionCacheKeys.getSeckillKey(DateUtil.toString(DateUtil.getDateline(), "yyyyMMdd"));
        this.cache.remove(redisKey);

    }

}
