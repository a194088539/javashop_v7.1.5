package com.enation.app.javashop.manager.api.system;


import com.enation.app.javashop.core.system.model.dos.RoleDO;
import com.enation.app.javashop.core.system.service.RoleManager;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import net.sf.json.JSONArray;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 角色管理测试
 *
 * @author zh
 * @version v7.0
 * @date 18/7/4 下午1:45
 * @since v7.0
 */
@Transactional(value = "systemTransactionManager", rollbackFor = Exception.class)
public class RoleControllerTest extends BaseTest {

    /**
     * 角色id
     */
    Integer id = 0;
    @Autowired
    private RoleManager roleManager;


    /**
     * 权限添加测试
     *
     * @throws Exception
     */
    @Test
    public void addTest() throws Exception {
        //正确校验
        String config = "[\n" +
                "    {\n" +
                "      \"auth_regular\": \"/goods.*\",\n" +
                "      \"checked\": true,\n" +
                "      \"children\": [],\n" +
                "      \"identifier\": \"goods\",\n" +
                "      \"title\": \"商品管理\"\n" +
                "    }\n" +
                "  ]";

        Map map = new HashMap<>(16);
        map.put("role_name", "角色名称");
        map.put("role_describe", "角色描述");
        map.put("menus", JSONArray.fromObject(config));
        String result = mockMvc.perform(post("/admin/systems/roles")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        Map roleVO = JsonUtil.jsonToObject(result, Map.class);
        id = StringUtil.toInt(roleVO.get("role_id"), false);
        Assert.assertNotNull(roleManager.getModel(StringUtil.toInt(roleVO.get("role_id"), false)));

    }

    /**
     * 修改角色
     *
     * @throws Exception
     */
    @Test
    public void editTest() throws Exception {
        this.addTest();
        //角色不存在校验
        String config = "[\n" +
                "    {\n" +
                "      \"auth_regular\": \"/goods/**\",\n" +
                "      \"checked\": true,\n" +
                "      \"children\": [],\n" +
                "      \"identifier\": \"goods\",\n" +
                "      \"title\": \"商品管理\"\n" +
                "    }\n" +
                "  ]";

        Map<String, Object> map = new HashMap<>(16);
        map.put("role_name", "角色名称");
        map.put("role_describe", "角色描述");
        map.put("menus", JSONArray.fromObject(config));
        ErrorMessage error = new ErrorMessage("003", "此角色不存在");
        mockMvc.perform(put("/admin/systems/roles/999999")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));
        //正确校验
        mockMvc.perform(put("/admin/systems/roles/" + id)
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        RoleDO roleDO = this.roleManager.getModel(id);
        Map dbMap = new HashMap<>();
        dbMap.put("role_name", roleDO.getRoleName());
        dbMap.put("role_describe", roleDO.getRoleDescribe());
        dbMap.put("menus", roleDO.getAuthIds());
        map.remove("menus");
        map.put("menus", "[{\"title\":\"商品管理\",\"identifier\":\"goods\",\"checked\":true,\"authRegular\":\"/goods/**\",\"children\":[]}]");
        Assert.assertEquals(formatMap(map), formatMap(dbMap));
    }

    /**
     * 删除测试
     *
     * @throws Exception
     */
    @Test
    public void delTest() throws Exception {
        this.addTest();
        //角色不存在校验
        ErrorMessage error = new ErrorMessage("003", "此角色不存在");
        mockMvc.perform(delete("/admin/systems/roles/999999")
                .header("Authorization", superAdmin))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));
        //正确校验
        mockMvc.perform(delete("/admin/systems/roles/" + id)
                .header("Authorization", superAdmin))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        Assert.assertNull(this.roleManager.getModel(id));
    }


}
