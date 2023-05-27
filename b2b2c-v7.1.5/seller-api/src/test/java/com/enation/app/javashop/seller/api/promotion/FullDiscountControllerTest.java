package com.enation.app.javashop.seller.api.promotion;

import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.promotion.PromotionErrorCode;
import com.enation.app.javashop.core.promotion.fulldiscount.model.dos.FullDiscountDO;
import com.enation.app.javashop.core.promotion.fulldiscount.model.vo.FullDiscountVO;
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
 * 满优惠测试脚本
 *
 * @author Snow create in 2018/4/17
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
public class FullDiscountControllerTest extends BaseTest {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport goodsDaoSupport;

    @Autowired
    private Cache cache;

    /** 正确数据 seller1店铺  */
    private Map successData = new HashMap();

    /** 正确数据 seller2店铺  */
    private Map successData2 = new HashMap();

    /** 错误数据—参数验证  */
    private List<MultiValueMap<String, String>> errorDataList = new ArrayList();

    /** 错误数据—逻辑错误  */
    private List<MultiValueMap<String, String>> errorDataTwoList = new ArrayList();

    private List<Integer> goodsIds = new ArrayList<>();


    @Before
    public void testData(){
        String[] names = new String[]{
                "title","start_time","end_time","range_type","description",
                "full_money","is_full_minus","minus_value","is_discount","discount_value",
                "is_free_ship","is_send_gift","gift_id","is_send_bonus","bonus_id",
                "message"};

        String[] values0 = new String[]{
                "满优惠活动标题","2534947200","2850566400","2","测试描述", "100.0","1","10","0","0", "0","0","0","0","0", "正确添加"
        };

        for(int i  = 0; i<names.length-1; i++){
            successData.put(names[i],values0[i]);
        }

        GoodsDO goodsDO = new GoodsDO();
        goodsDO.setGoodsName("满优惠测试商品名称");
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

        successData.put("goods_list",JSONArray.fromObject(goodsListJson.toString()));


        //第二个店铺的正确数据
        for(int i  = 0; i<names.length-1; i++){
            successData2.put(names[i],values0[i]);
        }

        GoodsDO goodsDO2 = new GoodsDO();
        goodsDO2.setGoodsName("满优惠测试商品名称");
        goodsDO2.setSn(DateUtil.getDateline()+"");
        goodsDO2.setSellerId(4);
        goodsDO2.setThumbnail("http://测试图片");
        this.goodsDaoSupport.insert(goodsDO2);
        int goodsId2 = this.goodsDaoSupport.getLastId("es_goods");
        goodsDO2.setGoodsId(goodsId2);
        goodsIds.add(goodsId2);

        StringBuffer goodsListJson2 = new StringBuffer();
        goodsListJson2.append("[");
        goodsListJson2.append(   "{");
        goodsListJson2.append(       "\"goods_id\":"+goodsId2+",");
        goodsListJson2.append(       "\"name\":\""+goodsDO2.getGoodsName()+"\",");
        goodsListJson2.append(       "\"thumbnail\":\""+goodsDO2.getThumbnail()+"\"");
        goodsListJson2.append(   "}" );
        goodsListJson2.append("]");

        successData2.put("goods_list",JSONArray.fromObject(goodsListJson2.toString()));


        String[] values1 = new String[]{"","2534947200","2850566400","1","测试描述", "100.0","1","10","0","0", "0","0","0","0","0", "请填写活动标题"};
        String[] values2 = new String[]{"满优惠活动标题","","2850566400","1","测试描述", "100.0","1","10","0","0", "0","0","0","0","0", "请填写活动起始时间"};
        String[] values3 = new String[]{"满优惠活动标题","2534947200","","1","测试描述", "100.0","1","10","0","0", "0","0","0","0","0", "请填写活动截止时间"};
        String[] values4 = new String[]{"满优惠活动标题","2534947200","2850566400","1","测试描述", "","1","10","0","0", "0","0","0","0","0", "请填写优惠门槛"};
        String[] values5 = new String[]{"满优惠活动标题","2534947200","2850566400","1","测试描述", "100","22","10","0","0", "0","0","0","0","0", "减现金参数有误"};
        String[] values6 = new String[]{"满优惠活动标题","2534947200","2850566400","1","测试描述", "100","-2","10","0","0", "0","0","0","0","0", "减现金参数有误"};
        String[] values7 = new String[]{"满优惠活动标题","2534947200","2850566400","1","测试描述", "100","0","0","22","0", "0","0","0","0","0", "打折参数有误"};
        String[] values8 = new String[]{"满优惠活动标题","2534947200","2850566400","1","测试描述", "100","0","0","0","0", "22","0","0","0","0", "免邮费参数有误"};
        String[] values9 = new String[]{"满优惠活动标题","2534947200","2850566400","1","测试描述", "100","0","0","0","0", "0","22","0","0","0", "送赠品参数有误"};
        String[] values10 = new String[]{"满优惠活动标题","2534947200","2850566400","1","测试描述", "100","0","0","0","0", "0","0","0","22","0", "送优惠券参数有误"};
        errorDataList = toMultiValueMaps(names,values1,values2,values3,values4,values5,values6,values7,values8,values9,values10);

        String[] values11 = new String[]{"满优惠活动标题","2534947200","2850566400","1","测试描述", "100","1","","0","0", "0","0","0","0","0", "请填写减多少元"};
        String[] values12 = new String[]{"满优惠活动标题","2534947200","2850566400","1","测试描述", "100","1","500","0","0", "0","0","0","0","0", "减少的金额不能大于门槛金额"};
        String[] values13 = new String[]{"满优惠活动标题","2534947200","2850566400","1","测试描述", "100","0","","1","", "0","0","0","0","0", "请填写打折数值"};
        String[] values14 = new String[]{"满优惠活动标题","2534947200","2850566400","1","测试描述", "100","0","0","0","0", "0","1","","0","0", "请选择赠品"};
        String[] values15 = new String[]{"满优惠活动标题","2534947200","2850566400","1","测试描述", "100","0","0","0","0", "0","0","","1","", "请选择优惠券"};

        errorDataTwoList = toMultiValueMaps(names,values11,values12,values13,values14,values15);

//        String[] names = new String[]{"title","start_time","end_time","range_type","description", "full_money","is_full_minus","minus_value",
//                "is_discount","discount_value", "is_free_ship","is_send_gift","gift_id","is_send_bonus","bonus_id", "message"};

    }


    @Test
    public void testAdd() throws Exception{
        Map map;

        //场景1: 正确添加
        map = successData;
        String resultJson = mockMvc.perform(post("/seller/promotion/full-discounts")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        FullDiscountVO fullDiscountVO = JsonUtil.jsonToObject(resultJson,FullDiscountVO.class);

        //验证是否正确添加
        mockMvc.perform(get("/seller/promotion/full-discounts/"+fullDiscountVO.getFdId())
                .header("Authorization",seller1))
                .andExpect(status().is(200))
                .andExpect(jsonPath("title").value("满优惠活动标题"));


        map = successData2;
        String json2 = mockMvc.perform(post("/seller/promotion/full-discounts")
                .header("Authorization",seller2)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        FullDiscountVO fullDiscountVO2 = JsonUtil.jsonToObject(json2,FullDiscountVO.class);

        //验证是否正确添加
        mockMvc.perform(get("/seller/promotion/full-discounts/"+fullDiscountVO2.getFdId())
                .header("Authorization",seller2))
                .andExpect(status().is(200))
                .andExpect(jsonPath("title").value("满优惠活动标题"));


        //参数为空的场景
        for (MultiValueMap<String,String> params  : errorDataList){
            String message =  params.get("message").get(0);
            ErrorMessage error  = new ErrorMessage(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER,message);

            map = new HashMap<>();
            for (String key : params.keySet()){
                map.put(key,params.get(key).get(0));
            }

            mockMvc.perform(post("/seller/promotion/full-discounts")
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

            mockMvc.perform(post("/seller/promotion/full-discounts")
                    .header("Authorization",seller1)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JsonUtil.objectToJson(map)))
                    .andExpect(status().is(500))
                    .andExpect(  objectEquals( error ));
        }

        //场景4：活动冲突验证
        map = successData;
        ErrorMessage error  = new ErrorMessage(PromotionErrorCode.E402.code(),"当前时间内已存在此类活动");
        mockMvc.perform(post("/seller/promotion/full-discounts")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(status().is(500))
                .andExpect( objectEquals( error ));

    }


    @Test
    public void testEdit() throws Exception{

        FullDiscountVO fullDiscountVO = this.add();
        fullDiscountVO.setTitle("修改过的满优惠活动标题");

        //越权操作
        ErrorMessage error  = new ErrorMessage(SystemErrorCodeV1.NO_PERMISSION,"无权操作");
        mockMvc.perform(put("/seller/promotion/full-discounts/"+fullDiscountVO.getFdId())
                .header("Authorization",seller2)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(fullDiscountVO)))
                .andExpect(status().is(401))
                .andExpect(  objectEquals( error ));

        //场景1：正确修改
        String resultJson = mockMvc.perform(put("/seller/promotion/full-discounts/"+fullDiscountVO.getFdId())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(fullDiscountVO)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        FullDiscountVO fullDiscountVO1 = JsonUtil.jsonToObject(resultJson,FullDiscountVO.class);
        //验证是否正确添加
        mockMvc.perform(get("/seller/promotion/full-discounts/"+fullDiscountVO1.getFdId())
                .header("Authorization",seller1))
                .andExpect(status().is(200))
                .andExpect(jsonPath("title").value(fullDiscountVO.getTitle()));


        //活动已经开始
        //手工修改活动的开始时间为 2018年1月1号
        long oldTime = 1514736000l;
        FullDiscountDO fullDiscountDO = (FullDiscountDO) this.cache.get(getCacheKey(fullDiscountVO.getFdId()));
        fullDiscountDO.setStartTime(oldTime);

        this.daoSupport.update(fullDiscountDO,fullDiscountDO.getFdId());
        this.cache.put(getCacheKey(fullDiscountVO.getFdId()),fullDiscountDO);

        ErrorMessage error2  = new ErrorMessage(PromotionErrorCode.E400.code(),"活动已经开始，不能进行编辑删除操作");
        mockMvc.perform(delete("/seller/promotion/full-discounts/"+fullDiscountVO.getFdId())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error2 ));

    }


    @Test
    public void testDelete() throws Exception{

        FullDiscountVO fullDiscountVO = this.add();

        //越权操作
        ErrorMessage error  = new ErrorMessage(SystemErrorCodeV1.NO_PERMISSION,"无权操作");
        mockMvc.perform(delete("/seller/promotion/full-discounts/"+fullDiscountVO.getFdId())
                .header("Authorization",seller2)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(401))
                .andExpect(  objectEquals( error ));


        //活动已经开始
        //手工修改活动的开始时间为 2018年1月1号
        long oldTime = 1514736000l;
        FullDiscountDO fullDiscountDO = (FullDiscountDO) this.cache.get(getCacheKey(fullDiscountVO.getFdId()));
        fullDiscountDO.setStartTime(oldTime);

        this.daoSupport.update(fullDiscountDO,fullDiscountDO.getFdId());
        this.cache.put(getCacheKey(fullDiscountVO.getFdId()),fullDiscountDO);

        ErrorMessage error2  = new ErrorMessage(PromotionErrorCode.E400.code(),"活动已经开始，不能进行编辑删除操作");
        mockMvc.perform(delete("/seller/promotion/full-discounts/"+fullDiscountVO.getFdId())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error2 ));


        //正确删除
        long futureTime = 2534947222l;
        FullDiscountDO fullDiscountDO2 = (FullDiscountDO) this.cache.get(getCacheKey(fullDiscountVO.getFdId()));
        fullDiscountDO2.setStartTime(futureTime);
        this.daoSupport.update(fullDiscountDO2,fullDiscountDO2.getFdId());
        this.cache.put(getCacheKey(fullDiscountVO.getFdId()),fullDiscountDO2);

        mockMvc.perform(delete("/seller/promotion/full-discounts/"+fullDiscountVO.getFdId())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

    }


    @Test
    public void testGetOne() throws Exception {

        FullDiscountVO fullDiscountVO = this.add();

        //场景一
        String resultJson = mockMvc.perform(get("/seller/promotion/full-discounts/"+fullDiscountVO.getFdId())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        FullDiscountVO fullDiscountVO1 = JsonUtil.jsonToObject(resultJson,FullDiscountVO.class);
        Assert.assertEquals(fullDiscountVO.getTitle(),fullDiscountVO1.getTitle());

        //场景2
        ErrorMessage error  = new ErrorMessage(SystemErrorCodeV1.NO_PERMISSION,"无权操作");
        mockMvc.perform(get("/seller/promotion/full-discounts/"+fullDiscountVO.getFdId())
                .header("Authorization",seller2)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(401))
                .andExpect(  objectEquals( error ));

    }


    @Test
    public void testGetPage() throws Exception {
        this.add();

        //场景一
        String resultJson = mockMvc.perform(get("/seller/promotion/full-discounts/")
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
     * 私有的满优惠添加方法
     * @return
     * @throws Exception
     */
    private FullDiscountVO add() throws Exception {

        FullDiscountVO fullDiscountVO ;
        String resultJson = mockMvc.perform(post("/seller/promotion/full-discounts")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(successData)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        fullDiscountVO = JsonUtil.jsonToObject(resultJson,FullDiscountVO.class);
        return fullDiscountVO;
    }

    /**
     * 满优惠的缓存key
     * @param id
     * @return
     */
    private String getCacheKey(Integer id){
        String key = PromotionCacheKeys.getFullDiscountKey(id);
        return key;
    }


}
