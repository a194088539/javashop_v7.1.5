package com.enation.app.javashop.manager.api.system;

import com.enation.app.javashop.core.system.model.dos.Regions;
import com.enation.app.javashop.core.system.service.RegionsManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 地区管理单元测试
 *
 * @author zh
 * @version v7.0
 * @date 18/6/4 下午3:43
 * @since v7.0
 */
@Transactional(value = "systemTransactionManager", rollbackFor = Exception.class)
public class RegionsControllerTest extends BaseTest {

    List<MultiValueMap<String, String>> list = null;

    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport systemDaoSupport;

    Regions regions = null;

    @Autowired
    private RegionsManager regionsManager;

    @Before
    public void dataPreparation() {

    }


    /**
     * 添加地区测试方法
     *
     * @throws Exception
     */
    @Test
    public void addTest() throws Exception {
        String[] names = new String[]{"parent_id", "local_name", "zipcode", "cod", "message"};
        String[] values1 = new String[]{"", "测试地区", "0351", "1", "父id不能为空"};
        String[] values2 = new String[]{"-1", "测试地区", "0351", "1", "必须为数字"};
        String[] values3 = new String[]{"1", "", "0351", "1", "地区名称不能为空"};
        String[] values4 = new String[]{"1", "测试地区", "0351", "", "是否支持货到付款不能为空"};
        String[] values5 = new String[]{"1", "测试地区", "0351", "-1", "是否支持货到付款,1支持,0不支持"};
        String[] values6 = new String[]{"1", "测试地区", "0351", "22", "是否支持货到付款,1支持,0不支持"};

        //参数非法校验
        list = toMultiValueMaps(names, values1, values2, values3, values4, values5, values6);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/admin/systems/regions")
                    .header("Authorization", superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //父地区id无效校验
        ErrorMessage error = new ErrorMessage("003", "当前地区父地区id无效");
        mockMvc.perform(post("/admin/systems/regions")
                .header("Authorization", superAdmin)
                .param("parent_id", "123123")
                .param("local_name", "测试地区")
                .param("zipcode", "0351")
                .param("cod", "1"))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));
        //正确性校验
        String json = mockMvc.perform(post("/admin/systems/regions")
                .header("Authorization", superAdmin)
                .param("parent_id", "0")
                .param("local_name", "测试地区")
                .param("zipcode", "0351")
                .param("cod", "1"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        regions = JsonUtil.jsonToObject(json, Regions.class);
        String sql = "select parent_id,local_name,zipcode,cod from es_regions where id = ?";
        Map dbMap = this.systemDaoSupport.queryForMap(sql, regions.getId());
        Map map = new HashMap();
        map.put("parent_id", 0);
        map.put("local_name", "测试地区");
        map.put("zipcode", "0351");
        map.put("cod", "1");
        Assert.assertEquals(dbMap, map);
    }

    /**
     * 修改地区
     *
     * @throws Exception
     */
    @Test
    public void editTest() throws Exception {
        this.addTest();
        String[] names = new String[]{"parent_id", "local_name", "zipcode", "cod", "message"};
        String[] values1 = new String[]{"", "测试地区", "0351", "1", "父id不能为空"};
        String[] values2 = new String[]{"-1", "测试地区", "0351", "1", "必须为数字"};
        String[] values3 = new String[]{"1", "", "0351", "1", "地区名称不能为空"};
        String[] values4 = new String[]{"1", "测试地区", "0351", "", "是否支持货到付款不能为空"};
        String[] values5 = new String[]{"1", "测试地区", "0351", "-1", "是否支持货到付款,1支持,0不支持"};
        String[] values6 = new String[]{"1", "测试地区", "0351", "22", "是否支持货到付款,1支持,0不支持"};

        //参数非法校验
        list = toMultiValueMaps(names, values1, values2, values3, values4, values5, values6);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(put("/admin/systems/regions/" + regions.getId())
                    .header("Authorization", superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //父地区id无效校验
        ErrorMessage error = new ErrorMessage("003", "当前地区父地区id无效");
        mockMvc.perform(put("/admin/systems/regions/" + regions.getId())
                .header("Authorization", superAdmin)
                .param("parent_id", "123123")
                .param("local_name", "测试地区")
                .param("zipcode", "0351")
                .param("cod", "1"))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));
        //地区不存在校验
        error = new ErrorMessage("003", "当前地区不存在");
        mockMvc.perform(put("/admin/systems/regions/1231222")
                .header("Authorization", superAdmin)
                .param("parent_id", "123123")
                .param("local_name", "测试地区")
                .param("zipcode", "0351")
                .param("cod", "1"))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));

        //正确性校验
        String json = mockMvc.perform(put("/admin/systems/regions/" + regions.getId())
                .header("Authorization", superAdmin)
                .param("parent_id", "0")
                .param("local_name", "测试地区2")
                .param("zipcode", "0352")
                .param("cod", "1"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        regions = JsonUtil.jsonToObject(json, Regions.class);
        String sql = "select parent_id,local_name,zipcode,cod from es_regions where id = ?";
        Map dbMap = this.systemDaoSupport.queryForMap(sql, regions.getId());
        Map map = new HashMap();
        map.put("parent_id", 0);
        map.put("local_name", "测试地区2");
        map.put("zipcode", "0352");
        map.put("cod", "1");
        Assert.assertEquals(dbMap, map);

    }

    /**
     * 删除地区测试
     *
     * @throws Exception
     */
    @Test
    public void deleteTest() throws Exception {
        //地区不存在校验
        ErrorMessage error = new ErrorMessage("003", "该地区不存在");
        mockMvc.perform(delete("/admin/systems/regions/123123123")
                .header("Authorization", superAdmin))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));
        //正确测试
        this.addTest();
        mockMvc.perform(delete("/admin/systems/regions/" + regions.getId())
                .header("Authorization", superAdmin))
                .andExpect(status().is(200));
        Assert.assertNull(regionsManager.getModel(regions.getId()));

    }


}
