package com.enation.app.javashop.manager.api.promotion;

import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyCatDO;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
 * 团购分类测试类
 *
 * @author Snow create in 2018/4/24
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager", rollbackFor = Exception.class)
public class GroupbuyCatControllerTest extends BaseTest {

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


    @Before
    public void testData() {
        String[] names = new String[]{"cat_name", "cat_order", "message"};
        String[] values0 = new String[]{"团购分类名称", "1", "正确添加"};

        for (int i = 0; i < names.length - 1; i++) {
            successData.put(names[i], values0[i]);
        }

    }

    @Test
    public void testAdd() throws Exception {
        Map map;

        //场景1: 正确添加
        map = successData;
        String resultJson = mockMvc.perform(post("/admin/promotion/group-buy-cats")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        GroupbuyCatDO catDO = JsonUtil.jsonToObject(resultJson, GroupbuyCatDO.class);

        //验证是否正确添加
        mockMvc.perform(get("/admin/promotion/group-buy-cats/" + catDO.getCatId())
                .header("Authorization", superAdmin))
                .andExpect(status().is(200))
                .andExpect(jsonPath("cat_name").value("团购分类名称"));
    }


    @Test
    public void testEdit() throws Exception {
        GroupbuyCatDO catDO = this.add();
        catDO.setCatName("修改过的分类名称");

        //场景1：正确修改
        String resultJson = mockMvc.perform(put("/admin/promotion/group-buy-cats/" + catDO.getCatId())
                .header("Authorization",superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(catDO)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        GroupbuyCatDO resultCatDO = JsonUtil.jsonToObject(resultJson, GroupbuyCatDO.class);
        //验证是否正确添加
        mockMvc.perform(get("/admin/promotion/group-buy-cats/" + resultCatDO.getCatId())
                .header("Authorization",superAdmin))
                .andExpect(status().is(200))
                .andExpect(jsonPath("cat_name").value(catDO.getCatName()));
    }

    @Test
    public void testDelete() throws Exception {
        GroupbuyCatDO catDO = this.add();

        mockMvc.perform(delete("/admin/promotion/group-buy-cats/" + catDO.getCatId())
                .header("Authorization",superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
    }


    @Test
    public void testGetOne() throws Exception {
        GroupbuyCatDO catDO = this.add();

        //场景一
        String resultJson = mockMvc.perform(get("/admin/promotion/group-buy-cats/" + catDO.getCatId())
                .header("Authorization",superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        GroupbuyCatDO resultCatDO = JsonUtil.jsonToObject(resultJson, GroupbuyCatDO.class);
        Assert.assertEquals(catDO.getCatName(), resultCatDO.getCatName());
    }


    @Test
    public void testGetPage() throws Exception {
        GroupbuyCatDO catDO = this.add();
        //场景一
        String resultJson = mockMvc.perform(get("/admin/promotion/group-buy-cats")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("page_no", "1").param("page_size", "10"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

    }


    /**
     * 公共添加的方法
     *
     * @return
     * @throws Exception
     */
    private GroupbuyCatDO add() throws Exception {
        //场景1: 正确添加
        String resultJson = mockMvc.perform(post("/admin/promotion/group-buy-cats")
                .header("Authorization",superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(successData)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        GroupbuyCatDO catDO = JsonUtil.jsonToObject(resultJson, GroupbuyCatDO.class);
        return catDO;
    }
}
