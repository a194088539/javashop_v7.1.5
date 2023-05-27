package com.enation.app.javashop.manager.api.promotion;

import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyActiveDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyGoodsDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.enums.GroupBuyGoodsStatusEnum;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyGoodsManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
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
 * 团购活动测试类
 *
 * @author Snow create in 2018/4/24
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager", rollbackFor = Exception.class)
public class GroupbuyActiveControllerTest extends BaseTest {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

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


    @Autowired
    private GroupbuyGoodsManager groupbuyGoodsManager;


    @Before
    public void testData() {
        String[] names = new String[]{"act_name", "start_time", "end_time", "join_end_time", "message"};
        String[] values0 = new String[]{"团购活动名称", "2534947200", "2850566400", "2434947200", "正确添加"};

        for (int i = 0; i < names.length - 1; i++) {
            successData.put(names[i], values0[i]);
        }

    }

    @Test
    public void testAdd() throws Exception {
        Map map;

        //场景1: 正确添加
        map = successData;
        String resultJson = mockMvc.perform(post("/admin/promotion/group-buy-actives")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        GroupbuyActiveDO activeDO = JsonUtil.jsonToObject(resultJson, GroupbuyActiveDO.class);

        //验证是否正确添加
        mockMvc.perform(get("/admin/promotion/group-buy-actives/" + activeDO.getActId())
                .header("Authorization", superAdmin))
                .andExpect(status().is(200))
                .andExpect(jsonPath("act_name").value("团购活动名称"));
    }


    @Test
    public void testEdit() throws Exception {
        GroupbuyActiveDO activeDO = this.add();
        activeDO.setActName("修改过的团购活动名称");

        //场景1：正确修改
        String resultJson = mockMvc.perform(put("/admin/promotion/group-buy-actives/" + activeDO.getActId())
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(activeDO)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        GroupbuyActiveDO resultActiveDO = JsonUtil.jsonToObject(resultJson, GroupbuyActiveDO.class);
        //验证是否正确添加
        mockMvc.perform(get("/admin/promotion/group-buy-actives/" + resultActiveDO.getActId())
                .header("Authorization", superAdmin))
                .andExpect(status().is(200))
                .andExpect(jsonPath("act_name").value(activeDO.getActName()));
    }


    @Test
    public void testDelete() throws Exception {
        GroupbuyActiveDO activeDO = this.add();

        mockMvc.perform(delete("/admin/promotion/group-buy-actives/" + activeDO.getActId())
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
    }


    @Test
    public void testGetOne() throws Exception {
        GroupbuyActiveDO activeDO = this.add();

        //场景一
        String resultJson = mockMvc.perform(get("/admin/promotion/group-buy-actives/" + activeDO.getActId())
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        GroupbuyActiveDO resultActiveDO = JsonUtil.jsonToObject(resultJson, GroupbuyActiveDO.class);
        Assert.assertEquals(activeDO.getActName(), resultActiveDO.getActName());
    }


    @Test
    public void testGetPage() throws Exception {

        this.add();

        //场景一
       String json =  mockMvc.perform(get("/admin/promotion/group-buy-actives")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("page_no", "1").param("page_size", "10"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

       Page page = JsonUtil.jsonToObject(json,Page.class);
       if(page.getData()==null || page.getData().isEmpty() ){
           throw new RuntimeException("读取团购活动列表测试脚本出错");
       }
    }


    /**
     * 审核商品
     *
     * @throws Exception
     */
    @Test
    public void testReviewGoods() throws Exception {

        GroupbuyActiveDO activeDO = this.add();

        GroupbuyGoodsDO goodsDO = new GroupbuyGoodsDO();
        goodsDO.setGbTitle("团购商品活动");
        goodsDO.setGbStatus(GroupBuyGoodsStatusEnum.PENDING.status());
        goodsDO.setGoodsName("团购测试商品");
        goodsDO.setGoodsId(1);
        goodsDO.setThumbnail("path");
        goodsDO.setActId(activeDO.getActId());
        this.daoSupport.insert(goodsDO);
        int id = this.daoSupport.getLastId("es_groupbuy_goods");
        goodsDO.setGbId(id);

        //场景一
        mockMvc.perform(post("/admin/promotion/group-buy-actives/review/" + goodsDO.getActId())
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("gb_id", goodsDO.getGbId() + "").param("status", "1"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        GroupbuyGoodsDO groupbuyGoodsDO = this.groupbuyGoodsManager.getModel(goodsDO.getGbId());
        Assert.assertEquals(groupbuyGoodsDO.getGbStatus(), GroupBuyGoodsStatusEnum.APPROVED.status());

    }


    @Test
    public void testReviewGoods2() throws Exception {

        GroupbuyActiveDO activeDO = this.add();
        GroupbuyGoodsDO goodsDO = new GroupbuyGoodsDO();
        goodsDO.setGbTitle("团购商品活动");
        goodsDO.setGbStatus(GroupBuyGoodsStatusEnum.PENDING.status());
        goodsDO.setGoodsName("团购测试商品");
        goodsDO.setGoodsId(99999);
        goodsDO.setThumbnail("path");
        goodsDO.setActId(activeDO.getActId());
        this.daoSupport.insert(goodsDO);
        int id = this.daoSupport.getLastId("es_groupbuy_goods");
        goodsDO.setGbId(id);

        //场景一
        mockMvc.perform(post("/admin/promotion/group-buy-actives/review/" + goodsDO.getActId())
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("gb_id", goodsDO.getGbId() + "").param("status", "2"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        GroupbuyGoodsDO groupbuyGoodsDO = this.groupbuyGoodsManager.getModel(goodsDO.getGbId());
        Assert.assertEquals(groupbuyGoodsDO.getGbStatus(), GroupBuyGoodsStatusEnum.NOT_APPROVED.status());

    }


    /**
     * 公共添加的方法
     *
     * @return
     * @throws Exception
     */
    private GroupbuyActiveDO add() throws Exception {

        //场景1: 正确添加
        Map map = successData;
        String resultJson = mockMvc.perform(post("/admin/promotion/group-buy-actives")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        GroupbuyActiveDO activeDO = JsonUtil.jsonToObject(resultJson, GroupbuyActiveDO.class);

        return activeDO;
    }

}
