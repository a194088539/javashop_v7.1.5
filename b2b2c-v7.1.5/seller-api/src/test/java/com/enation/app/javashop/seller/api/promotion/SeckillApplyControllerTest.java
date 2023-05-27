package com.enation.app.javashop.seller.api.promotion;

import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.goods.service.GoodsSkuManager;
import com.enation.app.javashop.core.promotion.PromotionErrorCode;
import com.enation.app.javashop.core.promotion.seckill.model.dos.SeckillApplyDO;
import com.enation.app.javashop.core.promotion.seckill.model.vo.SeckillVO;
import com.enation.app.javashop.core.promotion.seckill.service.SeckillManager;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 限时抢购申请测试
 *
 * @author Snow create in 2018/4/26
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager", rollbackFor = Exception.class)
public class SeckillApplyControllerTest extends BaseTest {


    @Autowired
    private Cache cache;

    /**
     * 限时抢购活动信息
     */
    private SeckillVO seckillVO;

    /**
     * 正确数据
     */
    private List<SeckillApplyDO> applyDOList1 = new ArrayList<>();

    /**
     * 错误数据—参数验证
     */
    private Map<String, List> map2 = new HashMap();

    /**
     * 错误数据—添加一个限时抢购活动中，不存在的的时刻
     */
    private Map<String, List> map3 = new HashMap();

    /**
     * 错误数据—添加一个限时抢购活动中，不存在的的活动
     */
    private Map<String, List> map4 = new HashMap();

    /**
     * 错误数据—逻辑错误
     */
    private List<MultiValueMap<String, String>> errorDataTwoList = new ArrayList();


    @Autowired
    private SeckillManager seckillManager;

    @MockBean
    private GoodsClient goodsClient;


    @Before
    @Transactional(value = "tradeTransactionManager", rollbackFor = Exception.class)
    public void testData() {

        //限时抢购活动的信息
        List<Integer> rangeList = new ArrayList<>();
        rangeList.add(1);
        rangeList.add(2);
        rangeList.add(3);

        seckillVO = new SeckillVO();
        seckillVO.setSeckillName("限时抢购活动名称");
        seckillVO.setStartDay(2534947200l);
        seckillVO.setApplyEndTime(2450566400l);
        seckillVO.setSeckillRule("活动规则");
        seckillVO.setRangeList(rangeList);
        seckillVO = this.seckillManager.add(seckillVO);


        //场景1：需要的参数
        SeckillApplyDO applyDO1 = new SeckillApplyDO();
        applyDO1.setSeckillId(seckillVO.getSeckillId());
        applyDO1.setTimeLine(1);
        applyDO1.setStartDay(seckillVO.getStartDay());
        applyDO1.setGoodsId(1);
        applyDO1.setGoodsName("商品名称");
        applyDO1.setPrice(11.0);
        applyDO1.setSoldQuantity(100);
        applyDOList1.add(applyDO1);


        //场景2：需要的参数
        for (int i = 0; i < 7; i++) {
            List<SeckillApplyDO> applyDOList2 = new ArrayList();

            SeckillApplyDO applyDO2 = new SeckillApplyDO();
            applyDO2.setSeckillId(seckillVO.getSeckillId());
            applyDO2.setTimeLine(1);
            applyDO2.setStartDay(12312313l);
            applyDO2.setGoodsId(i);
            applyDO2.setGoodsName("商品名称");
            applyDO2.setPrice(11.0);
            applyDO2.setSoldQuantity(100);

            String message = "";
            switch (i) {
                case 0:
                    applyDO2.setSeckillId(null);
                    message = "限时抢购活动ID参数异常";
                    break;
                case 1:
                    applyDO2.setTimeLine(null);
                    message = "时刻参数异常";
                    break;
                case 2:
                    applyDO2.setStartDay(null);
                    message = "活动开始时间参数异常";
                    break;
                case 3:
                    applyDO2.setGoodsId(null);
                    message = "商品ID参数异常";
                    break;
                case 4:
                    applyDO2.setGoodsName(null);
                    message = "商品名称参数异常";
                    break;
                case 5:
                    applyDO2.setPrice(null);
                    message = "抢购价参数异常";
                    break;
                case 6:
                    applyDO2.setSoldQuantity(null);
                    message = "售空数量参数异常";
                    break;
            }

            applyDOList2.add(applyDO2);
            map2.put(message, applyDOList2);
        }

        //场景3
        List<SeckillApplyDO> applyDOList3 = new ArrayList<>();
        SeckillApplyDO applyDO3 = new SeckillApplyDO();
        applyDO3.setSeckillId(seckillVO.getSeckillId());
        applyDO3.setTimeLine(4);
        applyDO3.setStartDay(seckillVO.getStartDay());
        applyDO3.setGoodsId(1);
        applyDO3.setGoodsName("商品名称");
        applyDO3.setPrice(11.0);
        applyDO3.setSoldQuantity(100);
        applyDOList3.add(applyDO3);
        map3.put("时刻参数异常", applyDOList3);


        //场景4
        List<SeckillApplyDO> applyDOList4 = new ArrayList<>();
        SeckillApplyDO applyDO4 = new SeckillApplyDO();
        applyDO4.setSeckillId(99999999);
        applyDO4.setTimeLine(4);
        applyDO4.setStartDay(seckillVO.getStartDay());
        applyDO4.setGoodsId(1);
        applyDO4.setGoodsName("商品名称");
        applyDO4.setPrice(11.0);
        applyDO4.setSoldQuantity(100);
        applyDOList4.add(applyDO4);
        map4.put("活动不存在", applyDOList4);

        CacheGoods goods = new CacheGoods();
        goods.setThumbnail("path");
        goods.setGoodsId(applyDO3.getGoodsId());
        goods.setGoodsName(applyDO3.getGoodsName());
        goods.setPrice(applyDO3.getPrice());
        goods.setEnableQuantity(101);
        when (goodsClient.getFromCache(applyDO3.getGoodsId())).thenReturn(goods);

    }


    @Test
    @Transactional(value = "tradeTransactionManager", rollbackFor = Exception.class)
    public void testAdd() throws Exception {


        //场景1: 正确申请
        mockMvc.perform(post("/seller/promotion/seckill-applys")
                .header("Authorization", seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(applyDOList1)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        //场景2：验证参数
        for (Map.Entry<String, List> entry : map2.entrySet()) {
            List<SeckillApplyDO> currList = entry.getValue();

            String message = entry.getKey();
            ErrorMessage error = new ErrorMessage("004", message);

            mockMvc.perform(post("/seller/promotion/seckill-applys")
                    .header("Authorization", seller1)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JsonUtil.objectToJson(currList)))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }


        //场景3：验证参数
        for (Map.Entry<String, List> entry : map3.entrySet()) {
            List<SeckillApplyDO> currList = entry.getValue();
            String message = entry.getKey();
            ErrorMessage error = new ErrorMessage("004", message);

            mockMvc.perform(post("/seller/promotion/seckill-applys")
                    .header("Authorization", seller1)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JsonUtil.objectToJson(currList)))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }


        //场景4：验证参数
        for (Map.Entry<String, List> entry : map4.entrySet()) {
            List<SeckillApplyDO> applyDOList2 = entry.getValue();
            String message = entry.getKey();
            ErrorMessage error = new ErrorMessage(PromotionErrorCode.E400.code(), message);

            mockMvc.perform(post("/seller/promotion/seckill-applys")
                    .header("Authorization", seller1)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JsonUtil.objectToJson(applyDOList2)))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }

    }


    /**
     * 读取限时抢购活动申请列表
     *
     * @throws Exception
     */
    @Test
    @Transactional(value = "tradeTransactionManager", rollbackFor = Exception.class)
    public void testGetPage() throws Exception {

        List<SeckillApplyDO> list = this.add();

        //场景一
        String json = mockMvc.perform(get("/seller/promotion/seckill-applys")
                .header("Authorization", seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("page_no", "1").param("page_size", "10")
                .param("seckill_id", list.get(0).getSeckillId() + ""))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        Page page = JsonUtil.jsonToObject(json, Page.class);
        if (page.getDataTotal() == 0) {
            throw new RuntimeException("读取出错");
        }

        //场景二
        String json2 = mockMvc.perform(get("/seller/promotion/seckill-applys")
                .header("Authorization", seller2)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("page_no", "1").param("page_size", "10")
                .param("seckill_id", list.get(0).getSeckillId() + ""))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        Page page2 = JsonUtil.jsonToObject(json2, Page.class);

    }


    /**
     * 读取限时抢购活动列表
     *
     * @throws Exception
     */
    @Test
    @Transactional(value = "tradeTransactionManager", rollbackFor = Exception.class)
    public void testGetSeckillPage() throws Exception {

        this.add();

        //场景一
        String json = mockMvc.perform(get("/seller/promotion/seckill-applys/seckill")
                .header("Authorization", seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("page_no", "1").param("page_size", "10")
                .param("keywords", "活动"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        Page page = JsonUtil.jsonToObject(json, Page.class);
        if (page.getDataTotal() == 0) {
            throw new RuntimeException("读取出错");
        }

    }


    private List<SeckillApplyDO> add() throws Exception {

        String resultJson = mockMvc.perform(post("/seller/promotion/seckill-applys")
                .header("Authorization", seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(applyDOList1)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        List<SeckillApplyDO> list = JsonUtil.jsonToList(resultJson, SeckillApplyDO.class);
        return list;

    }

}
