package com.enation.app.javashop.manager.api.system;

import com.enation.app.javashop.core.system.model.dos.Menu;
import com.enation.app.javashop.core.system.service.MenuManager;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 菜单管理测试类
 *
 * @author zh
 * @version v7.0
 * @date 18/6/19 下午5:30
 * @since v7.0
 */
@Transactional(value = "systemTransactionManager", rollbackFor = Exception.class)
public class MenuControllerTest extends BaseTest {

    List<MultiValueMap<String, String>> list = null;

    Integer id = 0;

    Integer twoGradeId = 0;

    Integer threeGradeId = 0;

    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport systemDaoSupport;

    @Autowired
    private MenuManager menuManager;

    @Before
    public void before() {
        String sql = "delete from es_menu ";
        this.systemDaoSupport.execute(sql);
    }

    /**
     * 菜单添加测试类
     *
     * @throws Exception
     */
    @Test
    public void addTest() throws Exception {

        //基本参数校验
        String[] names = new String[]{"parent_id", "title", "url", "identifier", "auth_regular", "message"};
        String[] values1 = new String[]{"", "测试标题", "http://test.com", "测试", "测试", "父菜单id不能为空"};
        String[] values2 = new String[]{"0", "", "http://test.com", "测试", "测试", "菜单标题不能为空"};
        String[] values4 = new String[]{"0", "测试标题", "http://test.com", "", "测试", "菜单唯一标识不能为空"};
        String[] values5 = new String[]{"0", "测试标题", "http://test.com", "测试", "", "权限表达式不能为空"};
        list = toMultiValueMaps(names, values1, values2, values4, values5);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/admin/systems/menus")
                    .header("Authorization", superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //父菜单参数错误校验
        String[] values6 = new String[]{"-1", "测试标题", "http://test.com", "测试", "测试", "父菜单必须为数字且不能为负数"};
        list = toMultiValueMaps(names, values6);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/admin/systems/menus")
                    .header("Authorization", superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //父菜单不存在校验
        ErrorMessage error = new ErrorMessage("003", "父级菜单不存在");
        mockMvc.perform(post("/admin/systems/menus")
                .header("Authorization", superAdmin)
                .param("parent_id", "43")
                .param("title", "测试标题")
                .param("url", "http://test.com")
                .param("identifier", "测试")
                .param("auth_regular", "测试"))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));
        //正确校验
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("parent_id", "0");
        map.add("title", "测试标题");
        map.add("url", "http://test.com");
        map.add("identifier", "测试");
        map.add("auth_regular", "测试");
        String json = mockMvc.perform(post("/admin/systems/menus")
                .header("Authorization", superAdmin)
                .params(map))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        Menu menu = JsonUtil.jsonToObject(json, Menu.class);
        id = menu.getId();
        Map dbMap = systemDaoSupport.queryForMap("select parent_id,title,url,identifier,auth_regular from es_menu where id = ?", menu.getId());
        Assert.assertEquals(formatMap(map), formatMap(dbMap));

        //菜单唯一标识重复校验
        error = new ErrorMessage("913", "菜单唯一标识重复");
        mockMvc.perform(post("/admin/systems/menus")
                .header("Authorization", superAdmin)
                .param("parent_id", "0")
                .param("title", "测试标题")
                .param("url", "http://test.com")
                .param("identifier", "测试")
                .param("auth_regular", "测试"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));


        //添加二级菜单
        map = new LinkedMultiValueMap<>();
        map.add("parent_id", id.toString());
        map.add("title", "测试标题");
        map.add("url", "http://test.com");
        map.add("identifier", "测试_1");
        map.add("auth_regular", "测试");
        json = mockMvc.perform(post("/admin/systems/menus")
                .header("Authorization", superAdmin)
                .params(map))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        Menu twoGrade = JsonUtil.jsonToObject(json, Menu.class);
        twoGradeId = twoGrade.getId();
        //添加三级菜单
        map = new LinkedMultiValueMap<>();
        map.add("parent_id", twoGrade.getId().toString());
        map.add("title", "测试标题");
        map.add("url", "http://test.com");
        map.add("identifier", "测试_2");
        map.add("auth_regular", "测试");
        json = mockMvc.perform(post("/admin/systems/menus")
                .header("Authorization", superAdmin)
                .params(map))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        Menu threeGrade = JsonUtil.jsonToObject(json, Menu.class);
        threeGradeId = threeGrade.getId();
        //校验级别是否超出限制
        error = new ErrorMessage("914", "菜单级别最多为3级");
        mockMvc.perform(post("/admin/systems/menus")
                .header("Authorization", superAdmin)
                .param("parent_id", threeGrade.getId().toString())
                .param("title", "测试标题")
                .param("url", "http://test.com")
                .param("identifier", "测试_3")
                .param("auth_regular", "测试"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
    }


    /**
     * 菜单修改测试
     *
     * @throws Exception
     */
    @Test
    public void editTest() throws Exception {
        String[] names = new String[]{"parent_id", "title", "url", "identifier", "auth_regular", "message"};
        String[] values1 = new String[]{"", "测试标题", "http://test.com", "测试", "测试", "父菜单id不能为空"};
        String[] values2 = new String[]{"0", "", "http://test.com", "测试", "测试", "菜单标题不能为空"};
        String[] values4 = new String[]{"0", "测试标题", "http://test.com", "", "测试", "菜单唯一标识不能为空"};
        String[] values5 = new String[]{"0", "测试标题", "http://test.com", "测试", "", "权限表达式不能为空"};
        list = toMultiValueMaps(names, values1, values2, values4, values5);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(put("/admin/systems/menus/1")
                    .header("Authorization", superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //父菜单参数错误校验
        String[] values6 = new String[]{"-1", "测试标题", "http://test.com", "测试", "测试", "父菜单必须为数字且不能为负数"};
        list = toMultiValueMaps(names, values6);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(put("/admin/systems/menus/1")
                    .header("Authorization", superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }

        //当前菜单不存在校验
        ErrorMessage error = new ErrorMessage("003", "当前菜单不存在");
        mockMvc.perform(put("/admin/systems/menus/1")
                .header("Authorization", superAdmin)
                .param("parent_id", "0")
                .param("title", "测试标题")
                .param("url", "http://test.com")
                .param("identifier", "测试")
                .param("auth_regular", "测试"))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));

        //父菜单不存在校验
        this.addTest();
        error = new ErrorMessage("003", "父级菜单不存在");
        mockMvc.perform(put("/admin/systems/menus/" + id)
                .header("Authorization", superAdmin)
                .param("parent_id", "43")
                .param("title", "测试标题")
                .param("url", "http://test.com")
                .param("identifier", "测试")
                .param("auth_regular", "测试"))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));

        //正确校验
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("parent_id", "0");
        map.add("title", "测试标题");
        map.add("url", "http://test.com");
        map.add("identifier", "测试");
        map.add("auth_regular", "测试");
        String json = mockMvc.perform(put("/admin/systems/menus/" + id)
                .header("Authorization", superAdmin)
                .params(map))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        Menu menu = JsonUtil.jsonToObject(json, Menu.class);
        Map dbMap = systemDaoSupport.queryForMap("select parent_id,title,url,identifier,auth_regular from es_menu where id = ?", menu.getId());
        Assert.assertEquals(formatMap(map), formatMap(dbMap));
        //菜单唯一标识重复校验
        map = new LinkedMultiValueMap<>();
        map.add("parent_id", "0");
        map.add("title", "测试标题");
        map.add("url", "http://test.com");
        map.add("identifier", "测试一下");
        map.add("auth_regular", "测试");
        json = mockMvc.perform(post("/admin/systems/menus")
                .header("Authorization", superAdmin)
                .params(map))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        error = new ErrorMessage("913", "菜单唯一标识重复");
        mockMvc.perform(put("/admin/systems/menus/" + id)
                .header("Authorization", superAdmin)
                .param("parent_id", "0")
                .param("title", "测试标题")
                .param("url", "http://test.com")
                .param("identifier", "测试一下")
                .param("auth_regular", "测试"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //菜单级别限制测试
        error = new ErrorMessage("914", "菜单级别最多为3级");
        mockMvc.perform(put("/admin/systems/menus/" + id)
                .header("Authorization", superAdmin)
                .param("parent_id", threeGradeId.toString())
                .param("title", "测试标题")
                .param("url", "http://test.com")
                .param("identifier", "测试两下")
                .param("auth_regular", "测试"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));


    }

    /**
     * 删除菜单测试
     *
     * @throws Exception
     */
    @Test
    public void deleteTest() throws Exception {
        //当期菜单不存在校验
        ErrorMessage error = new ErrorMessage("003", "当前菜单不存在");
        mockMvc.perform(delete("/admin/systems/menus/1")
                .header("Authorization", superAdmin))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));
        //正确校验
        this.addTest();
        mockMvc.perform(delete("/admin/systems/menus/" + id)
                .header("Authorization", superAdmin))
                .andExpect(status().is(200));
        Menu menu = this.menuManager.getModel(id);
        Assert.assertNull(menu);
    }


}
