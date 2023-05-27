package com.enation.app.javashop.seller.api.promotion;

import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.service.GoodsSkuManager;
import com.enation.app.javashop.core.promotion.PromotionErrorCode;
import com.enation.app.javashop.core.promotion.halfprice.model.dos.HalfPriceDO;
import com.enation.app.javashop.core.promotion.halfprice.model.vo.HalfPriceVO;
import com.enation.app.javashop.core.promotion.tool.support.PromotionCacheKeys;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.exception.SystemErrorCodeV1;
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
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 第二件半价测试类
 *
 * @author Snow create in 2018/4/16
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
public class HalfPriceControllerTest extends BaseTest {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport goodsDaoSupport;

    @Autowired
    private Cache cache;

    /** 正确数据  */
    private Map successData = new HashMap();

    /** 错误数据—参数验证  */
    private List<MultiValueMap<String, String>> errorDataList = new ArrayList();

    /** 错误数据—逻辑错误  */
    private List<MultiValueMap<String, String>> errorDataTwoList = new ArrayList();

    @MockBean
    private GoodsSkuManager goodsSkuManager;

    private List<Integer> goodsIds = new ArrayList<>();


    @Before
    public void testData(){

        String[] names = new String[]{"title","start_time","end_time","range_type","description","message"};

        String[] values0 = new String[]{"半价活动标题","2534947200","2850566400","2","描述","10"};
        for(int i  = 0; i<names.length-1; i++){
            successData.put(names[i],values0[i]);
        }

        GoodsDO goodsDO = new GoodsDO();
        goodsDO.setGoodsName("测试商品名称");
        goodsDO.setSn(DateUtil.getDateline()+"");
        goodsDO.setSellerId(3);
        goodsDO.setThumbnail("http://测试图片");
        this.goodsDaoSupport.insert(goodsDO);
        int goodsId = this.goodsDaoSupport.getLastId("es_goods");
        goodsDO.setGoodsId(goodsId);
        goodsIds.add(goodsId);

        StringBuffer goodsListJson = new StringBuffer();
        goodsListJson.append("[");
        goodsListJson.append(   "{");
        goodsListJson.append(       "\"goods_id\":"+goodsId+",");
        goodsListJson.append(       "\"goods_name\":\""+goodsDO.getGoodsName()+"\",");
        goodsListJson.append(       "\"thumbnail\":\""+goodsDO.getThumbnail()+"\"");
        goodsListJson.append(   "}" );
        goodsListJson.append("]");

        successData.put("goods_list", JSONArray.fromObject(goodsListJson.toString()));

        //必填项验证
        String[] values1 = new String[]{"","2534947200","2850566400","1","描述","请填写活动标题"};
        String[] values2 = new String[]{"半价活动标题","","2850566400","1","描述","请填写活动起始时间"};
        String[] values3 = new String[]{"半价活动标题","2534947200","","1","描述","请填写活动截止时间"};
        String[] values4 = new String[]{"半价活动标题","2534947200","-2850566400","1","描述","活动结束时间不正确"};
        String[] values5 = new String[]{"半价活动标题","2534947200","2850566400","","描述","请选择商品参与方式"};
        String[] values6 = new String[]{"半价活动标题","2534947200","2850566400","3","描述","商品参与方式值不正确"};
        String[] values7 = new String[]{"半价活动标题","-2534947200","2850566400","1","描述","活动起始时间不正确"};

        errorDataList = toMultiValueMaps(names,values1,values2,values3,values4,values5,values6,values7);

        //参数异常验证
        String[] values10 = new String[]{"半价活动标题","2850566400","2534947200","1","描述","活动起始时间不能大于活动结束时间"};
        String[] values11 = new String[]{"半价活动标题","2534947200","2850566400","2","描述","请选择要参与活动的商品"};
        String[] values12 = new String[]{"半价活动标题","1514736000","2534947200","1","描述","活动起始时间必须大于当前时间"};

        errorDataTwoList = toMultiValueMaps(names,values10,values11,values12);

    }

    @Test
    public void testAdd() throws Exception {

        Map map;
        //场景1: 正确添加
        map = successData;
        String resultJson = mockMvc.perform(post("/seller/promotion/half-prices")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        HalfPriceVO halfPriceVO = JsonUtil.jsonToObject(resultJson,HalfPriceVO.class);

        //验证是否正确添加
        mockMvc.perform(get("/seller/promotion/half-prices/"+halfPriceVO.getHpId())
                .header("Authorization",seller1))
                .andExpect(status().is(200))
                .andExpect(jsonPath("title").value("半价活动标题"));


        //场景2：参数为空的验证
        for (MultiValueMap<String,String> params  : errorDataList){
            String message =  params.get("message").get(0);
            ErrorMessage error  = new ErrorMessage(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER,message);

            map = new HashMap<>();
            for (String key : params.keySet()){
                map.put(key,params.get(key).get(0));
            }

            mockMvc.perform(post("/seller/promotion/half-prices")
                    .header("Authorization",seller1)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JsonUtil.objectToJson(map)))
                    .andExpect(status().is(400))
                    .andExpect(  objectEquals( error ));
        }

        //场景3：参数异常验证
        for (MultiValueMap<String,String> params  : errorDataTwoList){
            String message =  params.get("message").get(0);
            ErrorMessage error  = new ErrorMessage("004",message);

            map = new HashMap<>();
            for (String key : params.keySet()){
                map.put(key,params.get(key).get(0));
            }

            mockMvc.perform(post("/seller/promotion/half-prices")
                    .header("Authorization",seller1)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JsonUtil.objectToJson(map)))
                    .andExpect(status().is(500))
                    .andExpect(  objectEquals( error ));
        }

        //场景4：活动商品冲突验证
        map = successData;
        ErrorMessage error  = new ErrorMessage(PromotionErrorCode.E402.code(),"当前时间内已存在此类活动");
        mockMvc.perform(post("/seller/promotion/half-prices")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(status().is(500))
                .andExpect( objectEquals( error ));

    }


    @Test
    public void testEdit() throws Exception {
        Map map;

        HalfPriceVO halfPriceVO = this.add();
        halfPriceVO.setTitle("修改第二件半价标题");

        //越权操作
        ErrorMessage error  = new ErrorMessage(SystemErrorCodeV1.NO_PERMISSION,"无权操作");
        mockMvc.perform(put("/seller/promotion/half-prices/"+halfPriceVO.getHpId())
                .header("Authorization",seller2)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(halfPriceVO)))
                .andExpect(status().is(401))
                .andExpect(  objectEquals( error ));

        //场景：正确修改
        String resultJson = mockMvc.perform(put("/seller/promotion/half-prices/"+halfPriceVO.getHpId())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(halfPriceVO)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        HalfPriceVO resultHalfPriceVO = JsonUtil.jsonToObject(resultJson,HalfPriceVO.class);
        //验证是否正确添加
        mockMvc.perform(get("/seller/promotion/half-prices/"+resultHalfPriceVO.getHpId())
                .header("Authorization",seller1))
                .andExpect(status().is(200))
                .andExpect(jsonPath("title").value(halfPriceVO.getTitle()));


        //场景2：参数为空的验证
        for (MultiValueMap<String,String> params  : errorDataList){
            String message =  params.get("message").get(0);
            ErrorMessage error3  = new ErrorMessage(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER,message);

            map = new HashMap<>();
            for (String key : params.keySet()){
                map.put(key,params.get(key).get(0));
            }

            mockMvc.perform(put("/seller/promotion/half-prices/"+halfPriceVO.getHpId())
                    .header("Authorization",seller1)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JsonUtil.objectToJson(map)))
                    .andExpect(status().is(400))
                    .andExpect(  objectEquals( error3 ));
        }

        //场景3：参数异常验证
        for (MultiValueMap<String,String> params  : errorDataTwoList){
            String message =  params.get("message").get(0);
            ErrorMessage error4  = new ErrorMessage(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER,message);

            map = new HashMap<>();
            for (String key : params.keySet()){
                map.put(key,params.get(key).get(0));
            }

            mockMvc.perform(put("/seller/promotion/half-prices/"+halfPriceVO.getHpId())
                    .header("Authorization",seller1)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JsonUtil.objectToJson(map)))
                    .andExpect(status().is(500))
                    .andExpect(  objectEquals( error4 ));
        }


        //场景4：活动已经开始
        //手工修改活动的开始时间为 2018年1月1号
        long oldTime = 1514736000l;
        HalfPriceDO halfPriceDO = (HalfPriceDO) this.cache.get(getHalfPriceCacheKey(halfPriceVO.getHpId()));
        halfPriceDO.setStartTime(oldTime);
        this.daoSupport.update(halfPriceDO,halfPriceDO.getHpId());
        this.cache.put(getHalfPriceCacheKey(halfPriceVO.getHpId()),halfPriceDO);

        ErrorMessage error2  = new ErrorMessage(PromotionErrorCode.E400.code(),"活动已经开始，不能进行编辑删除操作");
        mockMvc.perform(put("/seller/promotion/half-prices/"+halfPriceVO.getHpId())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(halfPriceVO)))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error2 ));
    }

    @Test
    public void testDelete() throws Exception {
        HalfPriceVO halfPriceVO = this.add();

        //越权操作
        ErrorMessage error  = new ErrorMessage(SystemErrorCodeV1.NO_PERMISSION,"无权操作");
        mockMvc.perform(delete("/seller/promotion/half-prices/"+halfPriceVO.getHpId())
                .header("Authorization",seller2)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(401))
                .andExpect(  objectEquals( error ));


        //活动已经开始
        //手工修改活动的开始时间为 2018年1月1号
        long oldTime = 1514736000l;
        HalfPriceDO halfPriceDO = (HalfPriceDO) this.cache.get(getHalfPriceCacheKey(halfPriceVO.getHpId()));
        halfPriceDO.setStartTime(oldTime);
        this.daoSupport.update(halfPriceDO,halfPriceDO.getHpId());
        this.cache.put(getHalfPriceCacheKey(halfPriceVO.getHpId()),halfPriceDO);

        ErrorMessage error2  = new ErrorMessage(PromotionErrorCode.E400.code(),"活动已经开始，不能进行编辑删除操作");
        mockMvc.perform(delete("/seller/promotion/half-prices/"+halfPriceVO.getHpId())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error2 ));

        //正确删除
        long futureTime = 2534947222l;
        HalfPriceDO halfPriceDO2 = (HalfPriceDO) this.cache.get(getHalfPriceCacheKey(halfPriceVO.getHpId()));
        halfPriceDO2.setStartTime(futureTime);
        this.daoSupport.update(halfPriceDO2,halfPriceDO2.getHpId());
        this.cache.put(getHalfPriceCacheKey(halfPriceVO.getHpId()),halfPriceDO2);

        mockMvc.perform(delete("/seller/promotion/half-prices/"+halfPriceVO.getHpId())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testGetOne() throws Exception {

        HalfPriceVO halfPriceVO = this.add();

        //场景一
        String resultJson = mockMvc.perform(get("/seller/promotion/half-prices/"+halfPriceVO.getHpId())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        HalfPriceVO resultHalfPriceVO = JsonUtil.jsonToObject(resultJson,HalfPriceVO.class);
        Assert.assertEquals(halfPriceVO.getTitle(),resultHalfPriceVO.getTitle());

        //场景2
        ErrorMessage error  = new ErrorMessage(SystemErrorCodeV1.NO_PERMISSION,"无权操作");
        mockMvc.perform(get("/seller/promotion/half-prices/"+halfPriceVO.getHpId())
                .header("Authorization",seller2)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(401))
                .andExpect(  objectEquals( error ));
    }


    @Test
    public void testGetPage() throws Exception {
        this.add();
        //场景一
        String resultJson = mockMvc.perform(get("/seller/promotion/half-prices/")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("page_no","1").param("page_size","10")
                .param("keywords","标"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

    }


    @After
    public void cleanDate(){
        for (Integer goodsId:goodsIds){
            this.goodsDaoSupport.execute("delete from es_goods where goods_id = ?",goodsId);
        }
    }


    /**
     * 私有的第二件半价添加方法
     * @return
     * @throws Exception
     */
    private HalfPriceVO add() throws Exception {
        HalfPriceVO halfPriceVO ;
        //场景1: 正确添加
        String resultJson = mockMvc.perform(post("/seller/promotion/half-prices")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(successData)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        halfPriceVO = JsonUtil.jsonToObject(resultJson,HalfPriceVO.class);
        return halfPriceVO;
    }


    /**
     * 读取第二件半价活动的缓存的key
     * @param id
     * @return
     */
    private String getHalfPriceCacheKey(Integer id){
        String key = PromotionCacheKeys.getHalfPriceKey(id);
        return key;
    }


}
