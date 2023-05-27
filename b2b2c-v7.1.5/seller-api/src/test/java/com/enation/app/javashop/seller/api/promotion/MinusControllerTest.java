package com.enation.app.javashop.seller.api.promotion;

import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.promotion.PromotionErrorCode;
import com.enation.app.javashop.core.promotion.minus.model.dos.MinusDO;
import com.enation.app.javashop.core.promotion.minus.model.vo.MinusVO;
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
 * 单品立减—单元测试
 *
 * @author Snow create in 2018/4/9
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager", rollbackFor = Exception.class)
public class MinusControllerTest extends BaseTest {


    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport goodsDaoSupport;

    @Autowired
    private Cache cache;

    /**
     * 正确数据
     */
    private Map successData = new HashMap();

    /**
     * 错误数据—参数验证
     */
    private List<MultiValueMap<String, String>> errorDataList = new ArrayList();

    /**
     * 错误数据—逻辑错误
     */
    private List<MultiValueMap<String, String>> errorDataTwoList = new ArrayList();

    private List<Integer> goodsIds = new ArrayList<>();

    private GoodsDO goodsDO;

    @Before
    public void testData() {

        String[] names = new String[]{"title", "start_time", "end_time", "range_type", "description", "single_reduction_value", "message"};

        String[] values0 = new String[]{"标题", "2534947200", "2850566400", "2", "描述", "10"};
        for (int i = 0; i < names.length - 1; i++) {
            successData.put(names[i], values0[i]);
        }

        goodsDO = new GoodsDO();
        goodsDO.setGoodsName("单品立减测试商品名称");
        goodsDO.setSn(DateUtil.getDateline() + "");
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
        String[] values1 = new String[]{"", "2534947200", "2850566400", "1", "描述", "10", "标题必填"};
        String[] values2 = new String[]{"标题", "", "2850566400", "1", "描述", "10", "活动起始时间必填"};
        String[] values3 = new String[]{"标题", "2534947200", "", "1", "描述", "10", "活动结束时间必填"};
        String[] values5 = new String[]{"标题", "2534947200", "2850566400", "", "描述", "10", "请选择商品参与方式"};
        String[] values6 = new String[]{"标题", "2534947200", "2850566400", "3", "描述", "10", "商品参与方式值不正确"};
        String[] values7 = new String[]{"标题", "2534947200", "2850566400", "1", "描述", "-10", "立减金额不能为负数"};
        String[] values8 = new String[]{"标题", "-2534947200", "2850566400", "1", "描述", "10", "活动起始时间不正确"};
        String[] values4 = new String[]{"标题", "2534947200", "-2850566400", "1", "描述", "10", "活动结束时间不正确"};

        errorDataList = toMultiValueMaps(names, values1, values2, values3, values4, values5, values6, values7, values8);

        //参数异常验证
        String[] values10 = new String[]{"标题", "2850566400", "2534947200", "1", "描述", "10", "活动起始时间不能大于活动结束时间"};
        String[] values11 = new String[]{"标题", "2534947200", "2850566400", "2", "描述", "10", "请选择要参与活动的商品"};
        String[] values12 = new String[]{"标题", "1514736000", "2534947200", "1", "描述", "10", "活动起始时间必须大于当前时间"};

        errorDataTwoList = toMultiValueMaps(names, values10, values11, values12);

    }


    @Test
    public void testAdd() throws Exception {

        Map map;

        //场景1: 正确添加
        map = successData;
        String resultJson = mockMvc.perform(post("/seller/promotion/minus")
                .header("Authorization", seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        MinusVO minusVO = JsonUtil.jsonToObject(resultJson, MinusVO.class);

        //验证是否正确添加
        mockMvc.perform(get("/seller/promotion/minus/" + minusVO.getMinusId())
                .header("Authorization", seller1))
                .andExpect(status().is(200))
                .andExpect(jsonPath("title").value("标题"));

        //场景2：参数为空的验证
        for (MultiValueMap<String,String> params  : errorDataList){
            String message =  params.get("message").get(0);
            ErrorMessage error  = new ErrorMessage(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER,message);

            map = new HashMap<>();
            for (String key : params.keySet()){
                map.put(key,params.get(key).get(0));
            }

            mockMvc.perform(post("/seller/promotion/minus")
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

            mockMvc.perform(post("/seller/promotion/minus")
                    .header("Authorization",seller1)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JsonUtil.objectToJson(map)))
                    .andExpect(status().is(500))
                    .andExpect(  objectEquals( error ));
        }

        //场景4：活动商品冲突验证
        map = successData;
        ErrorMessage error  = new ErrorMessage(PromotionErrorCode.E402.code(),"当前时间内已存在此类活动");
        mockMvc.perform(post("/seller/promotion/minus")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(status().is(500))
                .andExpect( objectEquals( error ));

    }


    @Test
    public void testEdit() throws Exception {

        Map map;

        MinusVO minusVO = this.add();
        minusVO.setTitle("edit标题");

        //越权操作
        ErrorMessage error = new ErrorMessage(SystemErrorCodeV1.NO_PERMISSION, "无权操作");
        mockMvc.perform(put("/seller/promotion/minus/" + minusVO.getMinusId())
                .header("Authorization", seller2)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(minusVO)))
                .andExpect(status().is(401))
                .andExpect(objectEquals(error));

        //场景1：正确修改
        String resultJson = mockMvc.perform(put("/seller/promotion/minus/" + minusVO.getMinusId())
                .header("Authorization", seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(minusVO)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        MinusVO resultMinusVO = JsonUtil.jsonToObject(resultJson, MinusVO.class);
        //验证是否正确添加
        mockMvc.perform(get("/seller/promotion/minus/" + resultMinusVO.getMinusId())
                .header("Authorization", seller1))
                .andExpect(status().is(200))
                .andExpect(jsonPath("title").value(minusVO.getTitle()));


        //场景2：参数为空的验证
        for (MultiValueMap<String, String> params : errorDataList) {
            String message = params.get("message").get(0);
            ErrorMessage error3 = new ErrorMessage(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, message);

            map = new HashMap<>();
            for (String key : params.keySet()) {
                map.put(key, params.get(key).get(0));
            }

            mockMvc.perform(put("/seller/promotion/minus/" + minusVO.getMinusId())
                    .header("Authorization", seller1)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JsonUtil.objectToJson(map)))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error3));
        }

        //场景3：参数异常验证
        for (MultiValueMap<String, String> params : errorDataTwoList) {
            String message = params.get("message").get(0);
            ErrorMessage error4 = new ErrorMessage(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, message);

            map = new HashMap<>();
            for (String key : params.keySet()) {
                map.put(key, params.get(key).get(0));
            }

            mockMvc.perform(put("/seller/promotion/minus/" + minusVO.getMinusId())
                    .header("Authorization", seller1)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JsonUtil.objectToJson(map)))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error4));
        }

        //场景4：活动已经开始
        //手工修改活动的开始时间为 2018年1月1号
        long oldTime = 1514736000l;
        MinusDO minusDO = (MinusDO) this.cache.get(getMinusCacheKey(minusVO.getMinusId()));
        minusDO.setStartTime(oldTime);
        this.daoSupport.update(minusDO, minusDO.getMinusId());
        this.cache.put(getMinusCacheKey(minusDO.getMinusId()), minusDO);

        ErrorMessage error2 = new ErrorMessage(PromotionErrorCode.E400.code(), "活动已经开始，不能进行编辑删除操作");
        mockMvc.perform(put("/seller/promotion/minus/" + minusVO.getMinusId())
                .header("Authorization", seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(minusVO)))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error2));
    }

    @Test
    public void testDelete() throws Exception {

        MinusVO minusVO = this.add();

        //越权操作
        ErrorMessage error = new ErrorMessage(SystemErrorCodeV1.NO_PERMISSION, "无权操作");
        mockMvc.perform(delete("/seller/promotion/minus/" + minusVO.getMinusId())
                .header("Authorization", seller2)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(401))
                .andExpect(objectEquals(error));


        //活动已经开始
        //手工修改活动的开始时间为 2018年1月1号
        long oldTime = 1514736000l;
        MinusDO minusDO = (MinusDO) this.cache.get(getMinusCacheKey(minusVO.getMinusId()));
        minusDO.setStartTime(oldTime);
        this.daoSupport.update(minusDO, minusDO.getMinusId());
        this.cache.put(getMinusCacheKey(minusDO.getMinusId()), minusDO);

        ErrorMessage error2 = new ErrorMessage(PromotionErrorCode.E400.code(), "活动已经开始，不能进行编辑删除操作");
        mockMvc.perform(delete("/seller/promotion/minus/" + minusVO.getMinusId())
                .header("Authorization", seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error2));


        //正确删除
        long futureTime = 2534947222l;
        MinusDO minusDO2 = (MinusDO) this.cache.get(getMinusCacheKey(minusVO.getMinusId()));
        minusDO2.setStartTime(futureTime);
        this.daoSupport.update(minusDO2, minusDO2.getMinusId());
        this.cache.put(getMinusCacheKey(minusDO2.getMinusId()), minusDO2);

        mockMvc.perform(delete("/seller/promotion/minus/" + minusVO.getMinusId())
                .header("Authorization", seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
    }


    @Test
    public void testGetOne() throws Exception {
        MinusVO minusVO = this.add();

        //场景一
        String resultJson = mockMvc.perform(get("/seller/promotion/minus/" + minusVO.getMinusId())
                .header("Authorization", seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        MinusVO resultMinusVO = JsonUtil.jsonToObject(resultJson, MinusVO.class);
        Assert.assertEquals(minusVO.getTitle(), resultMinusVO.getTitle());

        //场景2
        ErrorMessage error = new ErrorMessage(SystemErrorCodeV1.NO_PERMISSION, "无权操作");
        mockMvc.perform(get("/seller/promotion/minus/" + minusVO.getMinusId())
                .header("Authorization", seller2)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(401))
                .andExpect(objectEquals(error));

    }


    @Test
    public void testGetPage() throws Exception {

        this.add();

        //场景一
        String resultJson = mockMvc.perform(get("/seller/promotion/minus/")
                .header("Authorization", seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("page_no", "1").param("page_size", "10")
                .param("keywords", "标"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

    }

    @After
    public void cleanDate() {
        for (Integer goodsId : goodsIds) {
            this.goodsDaoSupport.execute("delete from es_goods where goods_id = ?", goodsId);
        }
    }


    /**
     * 私有的单品立减添加方法
     *
     * @return
     * @throws Exception
     */
    private MinusVO add() throws Exception {
        MinusVO minusVO;
        //场景1: 正确添加
        String resultJson = mockMvc.perform(post("/seller/promotion/minus")
                .header("Authorization", seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(successData)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        minusVO = JsonUtil.jsonToObject(resultJson, MinusVO.class);

        return minusVO;
    }


    /**
     * 读取单品立减活动的缓存的key
     *
     * @param minusId
     * @return
     */
    private String getMinusCacheKey(Integer minusId) {
        String minusKey = PromotionCacheKeys.getMinusKey(minusId);
        return minusKey;
    }


}
