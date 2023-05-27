package com.enation.app.javashop.manager.api.promotion;

import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.promotion.PromotionErrorCode;
import com.enation.app.javashop.core.promotion.seckill.model.dos.SeckillApplyDO;
import com.enation.app.javashop.core.promotion.seckill.model.dos.SeckillDO;
import com.enation.app.javashop.core.promotion.seckill.model.enums.SeckillGoodsApplyStatusEnum;
import com.enation.app.javashop.core.promotion.seckill.model.enums.SeckillStatusEnum;
import com.enation.app.javashop.core.promotion.seckill.model.vo.SeckillVO;
import com.enation.app.javashop.core.promotion.seckill.service.SeckillGoodsManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.exception.SystemErrorCodeV1;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import net.sf.json.JSONArray;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 限时抢购活动的测试
 *
 * @author Snow create in 2018/4/26
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager", rollbackFor = Exception.class)
public class SeckillControllerTest extends BaseTest {


    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private SeckillGoodsManager seckillApplyManager;

    @MockBean
    private GoodsClient goodsClient;

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


    private SeckillApplyDO applyDO;

    @Before
    public void testData() {

        StringBuffer goodsListJson = new StringBuffer();
        goodsListJson.append("[");
        goodsListJson.append("1");
        goodsListJson.append(",2");
        goodsListJson.append(",3");
        goodsListJson.append("]");
        successData.put("range_list", JSONArray.fromObject(goodsListJson.toString()));

        String[] names = new String[]{"seckill_name", "start_day", "apply_end_time", "seckill_rule", "message"};
        String[] values0 = new String[]{"限时抢购活动名称", "2534947200", "2450566400", "活动规则", "正确添加"};

        for (int i = 0; i < names.length - 1; i++) {
            successData.put(names[i], values0[i]);
        }

        String[] values1 = new String[]{"", "2534947200", "2450566400", "活动规则", "请填写活动名称"};
        String[] values2 = new String[]{"限时抢购活动名称", "", "2450566400", "活动规则", "请填写活动日期"};
        String[] values3 = new String[]{"限时抢购活动名称", "2534947200", "", "活动规则", "请填写报名截止时间"};
        errorDataList = toMultiValueMaps(names, values1, values2, values3);

        String[] values4 = new String[]{"限时抢购活动名称", "2534947200", "2850566400", "活动规则", "报名截止时间不能大于活动时间"};
        errorDataTwoList = toMultiValueMaps(names, values4);

        //审核模拟数据
        CacheGoods cacheGoods = new CacheGoods();
        cacheGoods.setGoodsId(99);
        cacheGoods.setGoodsName("限时抢购测试商品");
        cacheGoods.setPrice(99.9);
        cacheGoods.setThumbnail("path");

        applyDO = new SeckillApplyDO();
        applyDO.setStatus(SeckillGoodsApplyStatusEnum.APPLY.name());
        applyDO.setGoodsId(cacheGoods.getGoodsId());
        applyDO.setSoldQuantity(100);
        applyDO.setPrice(cacheGoods.getPrice());
        applyDO.setStartDay(DateUtil.getDateline() + 100000);
        applyDO.setTimeLine(10);
        this.daoSupport.insert(applyDO);
        int id = this.daoSupport.getLastId("es_seckill_apply");
        applyDO.setApplyId(id);

        when(goodsClient.getFromCache(cacheGoods.getGoodsId())).thenReturn(cacheGoods);

    }


    @Test
    public void testAdd() throws Exception {
        Map map;

        //场景1: 正确添加
        map = successData;
        String resultJson = mockMvc.perform(post("/admin/promotion/seckills")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        SeckillVO seckillVO = JsonUtil.jsonToObject(resultJson, SeckillVO.class);
        Assert.assertEquals(seckillVO.getSeckillName(), "限时抢购活动名称");


        //场景2：参数为空的验证
        for (MultiValueMap<String, String> params : errorDataList) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, message);

            map = new HashMap<>();
            for (String key : params.keySet()) {
                map.put(key, params.get(key).get(0));
            }

            mockMvc.perform(post("/admin/promotion/seckills")
                    .header("Authorization", superAdmin)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JsonUtil.objectToJson(map)))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }

        //场景3：参数异常验证
        for (MultiValueMap<String, String> params : errorDataTwoList) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);

            map = new HashMap<>();
            for (String key : params.keySet()) {
                map.put(key, params.get(key).get(0));
            }

            mockMvc.perform(post("/admin/promotion/seckills")
                    .header("Authorization", superAdmin)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JsonUtil.objectToJson(map)))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }

        //场景4：活动时间冲突验证
        map = successData;
        ErrorMessage error = new ErrorMessage(PromotionErrorCode.E402.code(), "当前时间内已存在此类活动");
        mockMvc.perform(post("/admin/promotion/seckills")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));


        //场景5，时刻重复
        successData = new HashMap();
        StringBuffer goodsListJson = new StringBuffer();
        goodsListJson.append("[");
        goodsListJson.append("1");
        goodsListJson.append(",1");
        goodsListJson.append(",3");
        goodsListJson.append("]");
        successData.put("range_list", JSONArray.fromObject(goodsListJson.toString()));

        String[] names1 = new String[]{"seckill_name", "start_day", "apply_end_time", "seckill_rule", "message"};
        String[] values1 = new String[]{"限时抢购活动名称", "2634947200", "2610566400", "活动规则", "正确添加"};

        for (int i = 0; i < names1.length - 1; i++) {
            successData.put(names1[i], values1[i]);
        }

        ErrorMessage error1 = new ErrorMessage(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "抢购区间的值不能重复");
        mockMvc.perform(post("/admin/promotion/seckills")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(successData)))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error1));


        //场景6，时刻超出23点
        successData = new HashMap();
        StringBuffer goodsListJson2 = new StringBuffer();
        goodsListJson2.append("[");
        goodsListJson2.append("1");
        goodsListJson2.append(",2");
        goodsListJson2.append(",33");
        goodsListJson2.append("]");
        successData.put("range_list", JSONArray.fromObject(goodsListJson2.toString()));

        String[] names2 = new String[]{"seckill_name", "start_day", "apply_end_time", "seckill_rule"};
        String[] values2 = new String[]{"限时抢购活动名称", "2634947200", "2610566400", "活动规则"};

        for (int i = 0; i < names2.length - 1; i++) {
            successData.put(names2[i], values2[i]);
        }

        ErrorMessage error2 = new ErrorMessage(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "抢购区间必须在0点到23点的整点时刻");
        mockMvc.perform(post("/admin/promotion/seckills")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(successData)))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error2));


    }


    @Test
    public void testEdit() throws Exception {

        SeckillVO seckillVO = this.add();
        seckillVO.setSeckillName("修改过的限时抢购活动名称");

        //场景1：正确修改
        String resultJson = mockMvc.perform(put("/admin/promotion/seckills/" + seckillVO.getSeckillId())
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(seckillVO)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        SeckillVO resultSeckillVO = JsonUtil.jsonToObject(resultJson, SeckillVO.class);
        Assert.assertEquals(seckillVO.getSeckillName(), resultSeckillVO.getSeckillName());
    }


    @Test
    public void testDelete() throws Exception {
        SeckillVO seckillVO = this.add();

        mockMvc.perform(delete("/admin/promotion/seckills/" + seckillVO.getSeckillId())
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
    }


    @Test
    public void testGetOne() throws Exception {
        SeckillVO seckillVO = this.add();

        //场景一
        String resultJson = mockMvc.perform(get("/admin/promotion/seckills/" + seckillVO.getSeckillId())
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        SeckillVO resultSeckillVO = JsonUtil.jsonToObject(resultJson, SeckillVO.class);
        Assert.assertEquals(seckillVO.getSeckillName(), resultSeckillVO.getSeckillName());
    }


    @Test
    public void testGetPage() throws Exception {

        //场景一
        String resultJson = mockMvc.perform(get("/admin/promotion/seckills")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("page_no", "1").param("page_size", "10"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
    }


    /**
     * 发布限时抢购活动
     *
     * @throws Exception
     */
    @Test
    public void testRelease() throws Exception {

        Map map = successData;
        //场景1: 直接发布
        String resultJson = mockMvc.perform(post("/admin/promotion/seckills/0/release")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        SeckillVO seckillVO = JsonUtil.jsonToObject(resultJson, SeckillVO.class);
        Assert.assertEquals(seckillVO.getSeckillStatus(), SeckillStatusEnum.RELEASE.name());

    }


    /**
     * 先添加在保存发布限时抢购活动
     *
     * @throws Exception
     */
    @Test
    public void testRelease2() throws Exception {

        //先添加
        SeckillDO seckillDO = this.add();

        //场景1: 再发布
        String resultJson = mockMvc.perform(post("/admin/promotion/seckills/" + seckillDO.getSeckillId() + "/release")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(seckillDO)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        SeckillVO seckillVO2 = JsonUtil.jsonToObject(resultJson, SeckillVO.class);
        Assert.assertEquals(seckillVO2.getSeckillStatus(), SeckillStatusEnum.RELEASE.name());
    }


    /**
     * 审核商品测试
     *
     * @throws Exception
     */
    @Test
    public void testReviewGoods() throws Exception {

        mockMvc.perform(post("/admin/promotion/seckills/review/" + applyDO.getApplyId())
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("status", "yes"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        SeckillApplyDO applyDO1 = this.seckillApplyManager.getModel(applyDO.getApplyId());
        Assert.assertEquals(applyDO1.getStatus(), SeckillGoodsApplyStatusEnum.PASS.name());

    }

    /**
     * 审核商品不通过测试
     *
     * @throws Exception
     */
    @Test
    public void testReviewGoodsFail() throws Exception {

        mockMvc.perform(post("/admin/promotion/seckills/review/" + applyDO.getApplyId())
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("status", "no").param("fail_reason", "失败原因"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        SeckillApplyDO applyDO1 = this.seckillApplyManager.getModel(applyDO.getApplyId());
        Assert.assertEquals(applyDO1.getStatus(), SeckillGoodsApplyStatusEnum.FAIL.name());

    }


    /**
     * 限时抢购添加
     *
     * @return
     * @throws Exception
     */
    private SeckillVO add() throws Exception {

        String resultJson = mockMvc.perform(post("/admin/promotion/seckills")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(successData)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        SeckillVO seckillVO = JsonUtil.jsonToObject(resultJson, SeckillVO.class);
        Assert.assertEquals(seckillVO.getSeckillName(), "限时抢购活动名称");

        return seckillVO;

    }


    public static void main(String[] args) {
        String date = DateUtil.toString(DateUtil.getDateline(), "yyyy-MM-dd");
        long startTime = DateUtil.getDateline(date+" 00:00:00","yyyy-MM-dd HH:mm:ss");
        long endTime = DateUtil.getDateline(date+" 23:59:59","yyyy-MM-dd HH:mm:ss");

        System.out.println(DateUtil.getDateline()+600);
    }

}
