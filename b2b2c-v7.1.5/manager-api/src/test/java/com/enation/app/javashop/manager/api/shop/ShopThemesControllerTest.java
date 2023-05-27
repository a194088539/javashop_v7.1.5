package com.enation.app.javashop.manager.api.shop;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import com.enation.app.javashop.core.shop.model.dos.ShopThemesDO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;

@Transactional(value = "memberTransactionManager", rollbackFor = Exception.class)
public class ShopThemesControllerTest extends BaseTest {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport daoSupport;

    int id = 0;
    int id1 = 0;

    String[] names = new String[]{"name", "image_path", "is_default", "type", "mark", "message"};
    String[] names1 = new String[]{"name", "image_path", "is_default", "type", "mark"};

    @Before
    public void insertData() {

        this.daoSupport.execute("delete from es_shop_themes");
        Map map = new HashMap();
        map.put("name", "测试店铺模版");
        map.put("image_path", "/shop/themes");
        map.put("is_default", 1);
        map.put("mark", "mark");
        map.put("type", "PC");
        this.daoSupport.insert("es_shop_themes", map);
        id = this.daoSupport.getLastId("es_shop_themes");

        map.put("name", "测试店铺模版2");
        map.put("image_path", "/shop/themes2");
        map.put("is_default", 0);
        map.put("mark", "mark1");
        map.put("type", "PC");
        this.daoSupport.insert("es_shop_themes", map);
        id1 = this.daoSupport.getLastId("es_shop_themes");

        this.daoSupport.execute("delete from es_shop_detail");
        map.clear();
        map.put("shop_id", 100);
        this.daoSupport.insert("es_shop_detail", map);

    }

    /**
     * 获取店铺模版列表
     *
     * @throws Exception
     */
    @Test
    public void testList() throws Exception {

        ErrorMessage error = new ErrorMessage("004", "模版类型必填");
        mockMvc.perform(get("/admin/shops/themes")
                .header("Authorization", superAdmin)
                .param("page_no", "1")
                .param("page_size", "10"))
                .andExpect(status().is(400))
                .andExpect(objectEquals(error));

        //正确性校验
        mockMvc.perform(get("/admin/shops/themes")
                .header("Authorization", superAdmin)
                .param("page_no", "1")
                .param("type", "PC")
                .param("page_size", "10"))
                .andExpect(jsonPath("$.data.[0].type").value("PC"))
                .andExpect(jsonPath("$.data.[0].id").value(id));

    }

    /**
     * 添加店铺模版
     *
     * @throws Exception
     */
    @Test
    public void testAdd() throws Exception {


        String[] values = new String[]{"", "/shop/thmems1", "0", "PC", "mark", "模版名称必填"};
        String[] values1 = new String[]{"测试店铺模版1", "", "0", "PC", "mark", "模版图片路径不能为空"};
        String[] values2 = new String[]{"测试店铺模版1", "/shop/thmems1", "", "PC", "mark", "是否为默认必填"};
        String[] values3 = new String[]{"测试店铺模版1", "/shop/thmems1", "0", "", "mark", "模版类型必填"};
        String[] values4 = new String[]{"测试店铺模版1", "/shop/thmems1", "0", "PC", "", "模版标识不能为空"};


        List<MultiValueMap<String, String>> list = toMultiValueMaps(names, values, values1, values2, values3, values4);


        //参数性校验
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/admin/shops/themes")
                    .header("Authorization", superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }

        String[] values5 = new String[]{"测试店铺模版1", "/shop/thmems1", "0", "PC", "mark"};
        String[] values6 = new String[]{"测试店铺模版1", "/shop/thmems1", "0", "PC", "mark2"};
        List<MultiValueMap<String, String>> theme = toMultiValueMaps(names1, values5, values6);

        //模版标示重复性校验
        ErrorMessage error = new ErrorMessage("E225", "店铺模版标识重复");
        mockMvc.perform(post("/admin/shops/themes")
                .header("Authorization", superAdmin)
                .params(theme.get(0)))
                .andExpect(objectEquals(error));


        //正确性校验
        String json = mockMvc.perform(post("/admin/shops/themes")
                .header("Authorization", superAdmin)
                .params(theme.get(1)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        ShopThemesDO shopThemesDO = JsonUtil.jsonToObject(json, ShopThemesDO.class);
        Map themes = this.daoSupport.queryForMap("select name,image_path,is_default,type,mark from es_shop_themes where id =  ? ", shopThemesDO.getId());
        Map expected = this.formatMap(theme.get(1));
        Map actual = this.formatMap(themes);
        Assert.assertEquals(expected, actual);
    }

    /**
     * 编辑店铺模版
     *
     * @throws Exception
     */
    @Test
    public void testEdit() throws Exception {

        String[] names = new String[]{"name", "image_path", "is_default", "type", "mark", "message"};
        String[] values = new String[]{"", "/shop/thmems1", "0", "PC", "mark", "模版名称必填"};
        String[] values1 = new String[]{"测试店铺模版1", "", "0", "PC", "mark", "模版图片路径不能为空"};
        String[] values2 = new String[]{"测试店铺模版1", "/shop/thmems1", "", "PC", "mark", "是否为默认必填"};
        String[] values3 = new String[]{"测试店铺模版1", "/shop/thmems1", "0", "", "mark", "模版类型必填"};
        String[] values4 = new String[]{"测试店铺模版1", "/shop/thmems1", "0", "PC", "", "模版标识不能为空"};

        List<MultiValueMap<String, String>> list = toMultiValueMaps(names, values, values1, values2, values3, values4);

        String[] values5 = new String[]{"测试店铺模版1", "/shop/thmems1", "0", "PC", "mark1"};
        String[] values6 = new String[]{"测试店铺模版1", "/shop/thmems1", "0", "PC", "mark2"};
        List<MultiValueMap<String, String>> theme = toMultiValueMaps(names1, values5, values6);

        //参数性校验
        ErrorMessage error = null;
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            error = new ErrorMessage("004", message);
            mockMvc.perform(put("/admin/shops/themes/" + id)
                    .header("Authorization", superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }

        //数据无效性校验
        error = new ErrorMessage("E202", "模版不存在");
        mockMvc.perform(put("/admin/shops/themes/-1")
                .header("Authorization", superAdmin)
                .params(theme.get(0)))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //模版标示重复性校验
        error = new ErrorMessage("E225", "店铺模版标识重复");
        mockMvc.perform(post("/admin/shops/themes")
                .header("Authorization", superAdmin)
                .params(theme.get(0)))
                .andExpect(objectEquals(error));

        //正确性校验
        String json = mockMvc.perform(put("/admin/shops/themes/" + id)
                .header("Authorization", superAdmin)
                .params(theme.get(1)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        ShopThemesDO shopThemesDO = JsonUtil.jsonToObject(json, ShopThemesDO.class);

        Map themes = this.daoSupport.queryForMap("select name,image_path,mark,is_default,type from es_shop_themes where id =  ? ", id);
        Map expected = this.formatMap(theme.get(1));
        Map actual = this.formatMap(themes);
        Assert.assertEquals(expected, actual);
    }

    /**
     * 删除店铺模版
     *
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {

        //数据无效性校验
        ErrorMessage error = new ErrorMessage("E202", "模版不存在");
        mockMvc.perform(delete("/admin/shops/themes/-1")
                .header("Authorization", superAdmin))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //默认模版不可删除校验
        error = new ErrorMessage("E205", "默认模版不能删除");
        mockMvc.perform(delete("/admin/shops/themes/" + id)
                .header("Authorization", superAdmin))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //正确性校验
        mockMvc.perform(delete("/admin/shops/themes/" + id1)
                .header("Authorization", superAdmin))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        int count = this.daoSupport.queryForInt("select count(*) from es_shop_themes where id = ? ", id1);
        Assert.assertEquals(0, count);
    }

    /**
     * 获取店铺模版
     *
     * @throws Exception
     */
    @Test
    public void testGet() throws Exception {
        //数据无效性校验
        ErrorMessage error = new ErrorMessage("E202", "模版不存在");
        mockMvc.perform(get("/admin/shops/themes/-1")
                .header("Authorization", superAdmin))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //正确性校验
        ShopThemesDO shopThemesDO = new ShopThemesDO();
        shopThemesDO.setId(id);
        shopThemesDO.setType("PC");
        mockMvc.perform(get("/admin/shops/themes/" + id)
                .header("Authorization", superAdmin))
                .andExpect(status().is(200))
                .andExpect(objectEquals(shopThemesDO));
    }
}
