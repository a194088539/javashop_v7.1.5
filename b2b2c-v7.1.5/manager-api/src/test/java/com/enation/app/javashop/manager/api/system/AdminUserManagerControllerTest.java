package com.enation.app.javashop.manager.api.system;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.member.model.vo.MemberVO;
import com.enation.app.javashop.core.system.model.dos.AdminUser;
import com.enation.app.javashop.core.system.model.dos.RoleDO;
import com.enation.app.javashop.core.system.model.dto.AdminUserDTO;
import com.enation.app.javashop.core.system.model.vo.AdminLoginVO;
import com.enation.app.javashop.core.system.model.vo.AdminUserVO;
import com.enation.app.javashop.core.system.service.AdminUserManager;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 管理员管理测试
 *
 * @author zh
 * @version v7.0
 * @date 18/6/22 下午3:11
 * @since v7.0
 */
@Transactional(value = "systemTransactionManager", rollbackFor = Exception.class)
public class AdminUserManagerControllerTest extends BaseTest {

    List<MultiValueMap<String, String>> list = null;
    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport systemDaoSupport;

    @Autowired
    private Cache cache;

    @Autowired
    private AdminUserManager adminUserManager;

    Integer id = 0;

    Integer roleId = 0;

    Map tokenMap = new HashMap();

    AdminUser adminUser = null;

    @Before
    public void dataPreparation() throws Exception {
        systemDaoSupport.execute("delete from es_admin_user");
        //添加角色
        RoleDO roleDO = new RoleDO();
        roleDO.setRoleName("测试权限");
        roleDO.setAuthIds("1");
        systemDaoSupport.insert(roleDO);
        roleId = systemDaoSupport.getLastId("es_role");

        AdminUserVO adminUserVO = new AdminUserVO();
        adminUserVO.setUsername("李四");
        adminUserVO.setPassword(StringUtil.md5("123456"));
        adminUserVO.setRoleId(roleId);
        adminUserVO.setRealName("测试");
        adminUserVO.setDepartment("产品部");
        adminUserVO.setFace("http://a.jpg");
        adminUserVO.setFounder(1);
        adminUser = adminUserManager.add(adminUserVO);


        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        String json = mockMvc.perform(get("/admin/systems/admin-users/login")
                .param("username", "李四")
                .param("password", StringUtil.md5("123456"))
                .param("uuid", uuid)
                .param("captcha", "1111"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        tokenMap = JsonUtil.toMap(json);


    }

    /**
     * 添加管理员测试
     *
     * @throws Exception
     */
    @Test
    public void addTest() throws Exception {
        //基本参数校验
        String[] names = new String[]{"username", "password", "department", "role_id", "remark", "real_name", "face", "founder", "message"};
        String[] values1 = new String[]{"", StringUtil.md5("123456"), "产品部", "1", "备注", "李四", "http://a.jpg", "1", "名称不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values2 = new String[]{"1234567", StringUtil.md5("123456"), "产品部", "1", "备注", "李四", "http://a.jpg", "1", "名称不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values3 = new String[]{"123￥%", StringUtil.md5("123456"), "产品部", "1", "备注", "李四", "http://a.jpg", "1", "名称不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values4 = new String[]{"张", StringUtil.md5("123456"), "产品部", "1", "备注", "李四", "http://a.jpg", "1", "名称不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values5 = new String[]{"张张张张张张张张张张张张张张张张张张张张张张张张", StringUtil.md5("123456"), "产品部", "1", "备注", "李四", "http://a.jpg", "1", "名称不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values6 = new String[]{"张三", StringUtil.md5("123456"), "产品部", "-1", "备注", "李四", "http://a.jpg", "1", "权限id必须为数字"};
        String[] values7 = new String[]{"张三", StringUtil.md5("123456"), "产品部", "1", "备注aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa备注", "李四", "http://a.jpg", "1", "备注最大为90个字符"};
        String[] values8 = new String[]{"张三", StringUtil.md5("123456"), "产品部", "1", "备注", "李", "http://a.jpg", "1", "真实姓名不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values9 = new String[]{"张三", StringUtil.md5("123456"), "产品部", "1", "备注", "", "http://a.jpg", "1", "真实姓名不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values10 = new String[]{"张三", StringUtil.md5("123456"), "产品部", "1", "备注", "李$%", "http://a.jpg", "1", "真实姓名不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values11 = new String[]{"张三", StringUtil.md5("123456"), "产品部", "1", "备注", "李李李李李李李李李李李李李李李李李李李李李李", "http://a.jpg", "1", "真实姓名不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values12 = new String[]{"张三", StringUtil.md5("123456"), "产品部", "1", "备注", "123123", "http://a.jpg", "1", "真实姓名不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values13 = new String[]{"张三", StringUtil.md5("123456"), "产品部", "1", "备注", "张三", "http://a.jpg", "-1", "必须为数字且,1为超级管理员,0为其他"};
        String[] values14 = new String[]{"张三", StringUtil.md5("123456"), "产品部", "1", "备注", "张三", "http://a.jpg", "2", "必须为数字且,1为超级管理员,0为其他"};
        String[] values15 = new String[]{"张三", StringUtil.md5("123456"), "产品部", "1", "备注", "张三", "http://a.jpg", "", "是否为超级管理员不能为空"};

        list = toMultiValueMaps(names, values1, values2, values3, values4, values5, values6, values7, values8, values9, values10, values11, values12, values13, values14, values15);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/admin/systems/manager/admin-users")
                    .header("Authorization", tokenMap.get("access_token"))
                    .header("uuid", uuid)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //管理员密码为空校验
        String[] values16 = new String[]{"张三", "", "产品部", "1", "备注", "张三", "http://a.jpg", "1", "密码格式不正确"};
        list = toMultiValueMaps(names, values16);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("917", message);
            mockMvc.perform(post("/admin/systems/manager/admin-users")
                    .header("Authorization", tokenMap.get("access_token"))
                    .header("uuid", uuid)
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }
        //正确校验
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", "张三");
        map.add("password", StringUtil.md5("123456"));
        map.add("department", "产品部");
        map.add("role_id", roleId.toString());
        map.add("real_name", "测试");
        map.add("remark", "测试");
        map.add("face", "http://a.jpg");
        map.add("founder", "1");
        String json = mockMvc.perform(post("/admin/systems/manager/admin-users")
                .header("Authorization", tokenMap.get("access_token"))
                .header("uuid", uuid)
                .params(map))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        AdminUser adminUser = JsonUtil.jsonToObject(json, AdminUser.class);
        id = adminUser.getId();
        Map dbMap = systemDaoSupport.queryForMap("select username,department,role_id,real_name,face,remark,founder from es_admin_user where id = ?", id);
        map.remove("password");
        Assert.assertEquals(formatMap(map), formatMap(dbMap));

        //管理员名称重复校验
        map = new LinkedMultiValueMap<>();
        map.add("username", "张三");
        map.add("password", StringUtil.md5("123456"));
        map.add("department", "产品部");
        map.add("role_id", roleId.toString());
        map.add("identifier", "1");
        map.add("real_name", "测试");
        map.add("remark", "测试");
        map.add("face", "http://a.jpg");
        map.add("founder", "1");
        ErrorMessage error = new ErrorMessage("915", "管理员名称重复");
        mockMvc.perform(post("/admin/systems/manager/admin-users")
                .header("Authorization", tokenMap.get("access_token"))
                .header("uuid", uuid)
                .params(map))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //角色id不存在校验
        map = new LinkedMultiValueMap<>();
        map.add("username", "加加洛克");
        map.add("password", StringUtil.md5("123456"));
        map.add("department", "产品部");
        map.add("role_id", "11111");
        map.add("identifier", "1");
        map.add("real_name", "测试");
        map.add("remark", "测试");
        map.add("face", "http://a.jpg");
        map.add("founder", "0");
        error = new ErrorMessage("003", "所属权限不存在");
        mockMvc.perform(post("/admin/systems/manager/admin-users")
                .header("Authorization", tokenMap.get("access_token"))
                .params(map))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));
    }


    /**
     * 管理员修改测试
     *
     * @throws Exception
     */
    @Test
    public void editTest() throws Exception {
        //基本参数校验
        String[] names = new String[]{"username", "password", "department", "role_id", "remark", "real_name", "face", "founder", "message"};
        String[] values1 = new String[]{"", StringUtil.md5("123456"), "产品部", "1", "备注", "李四", "http://a.jpg", "1", "名称不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values2 = new String[]{"1234567", StringUtil.md5("123456"), "产品部", "1", "备注", "李四", "http://a.jpg", "1", "名称不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values3 = new String[]{"123￥%", StringUtil.md5("123456"), "产品部", "1", "备注", "李四", "http://a.jpg", "1", "名称不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values4 = new String[]{"张", StringUtil.md5("123456"), "产品部", "1", "备注", "李四", "http://a.jpg", "1", "名称不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values5 = new String[]{"张张张张张张张张张张张张张张张张张张张张张张张张", StringUtil.md5("123456"), "产品部", "1", "备注", "李四", "http://a.jpg", "1", "名称不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values6 = new String[]{"张三", StringUtil.md5("123456"), "产品部", "-1", "备注", "李四", "http://a.jpg", "1", "权限id必须为数字"};
        String[] values7 = new String[]{"张三", StringUtil.md5("123456"), "产品部", "1", "备注aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa备注", "李四", "http://a.jpg", "1", "备注最大为90个字符"};
        String[] values8 = new String[]{"张三", StringUtil.md5("123456"), "产品部", "1", "备注", "李", "http://a.jpg", "1", "真实姓名不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values9 = new String[]{"张三", StringUtil.md5("123456"), "产品部", "1", "备注", "", "http://a.jpg", "1", "真实姓名不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values10 = new String[]{"张三", StringUtil.md5("123456"), "产品部", "1", "备注", "李$%", "http://a.jpg", "1", "真实姓名不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values11 = new String[]{"张三", StringUtil.md5("123456"), "产品部", "1", "备注", "李李李李李李李李李李李李李李李李李李李李李李", "http://a.jpg", "1", "真实姓名不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values12 = new String[]{"张三", StringUtil.md5("123456"), "产品部", "1", "备注", "123123", "http://a.jpg", "1", "真实姓名不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values13 = new String[]{"张三", StringUtil.md5("123456"), "产品部", "1", "备注", "张三", "http://a.jpg", "-1", "必须为数字且,1为超级管理员,0为其他"};
        String[] values14 = new String[]{"张三", StringUtil.md5("123456"), "产品部", "1", "备注", "张三", "http://a.jpg", "2", "必须为数字且,1为超级管理员,0为其他"};
        String[] values15 = new String[]{"张三", StringUtil.md5("123456"), "产品部", "1", "备注", "张三", "http://a.jpg", "", "是否为超级管理员不能为空"};

        list = toMultiValueMaps(names, values1, values2, values3, values4, values5, values6, values7, values8, values9, values10, values11, values12, values13, values14, values15);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(put("/admin/systems/manager/admin-users/1")
                    .header("Authorization", tokenMap.get("access_token"))
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //校验要修改的管理员是否存在
        String[] values16 = new String[]{"张三", StringUtil.md5("123456"), "产品部", "1", "备注", "张三", "http://a.jpg", "1", "当前管理员不存在"};
        list = toMultiValueMaps(names, values16);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("003", message);
            mockMvc.perform(put("/admin/systems/manager/admin-users/1")
                    .header("Authorization", tokenMap.get("access_token"))
                    .params(params))
                    .andExpect(status().is(404))
                    .andExpect(objectEquals(error));
        }
        this.addTest();
        adminUserManager.delete(id);
        //最后一个超级管理员从修改成为普通管理员校验(必须保留一个超级管理员)
        String[] values18 = new String[]{"李三枪", StringUtil.md5("123456"), "产品部", roleId.toString(), "备注", "张三", "http://a.jpg", "0", "必须保留一个超级管理员"};
        list = toMultiValueMaps(names, values18);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("916", message);
            mockMvc.perform(put("/admin/systems/manager/admin-users/" + adminUser.getId())
                    .header("Authorization", tokenMap.get("access_token"))
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        //所属权限校验
        //正确校验
        map = new LinkedMultiValueMap<>();
        map.add("username", "张三风");
        map.add("password", StringUtil.md5("123456"));
        map.add("department", "产品部");
        map.add("role_id", roleId.toString());
        map.add("real_name", "测试");
        map.add("remark", "测试");
        map.add("face", "http://a.jpg");
        map.add("founder", "1");
        String json = mockMvc.perform(post("/admin/systems/manager/admin-users")
                .header("Authorization", tokenMap.get("access_token"))
                .params(map))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        AdminUser adminUser = JsonUtil.jsonToObject(json, AdminUser.class);
        String[] values19 = new String[]{"李四", StringUtil.md5("123456"), "产品部", "12222", "备注", "张三", "http://a.jpg", "0", "所属权限不存在"};
        list = toMultiValueMaps(names, values19);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("003", message);
            mockMvc.perform(put("/admin/systems/manager/admin-users/" + adminUser.getId())
                    .header("Authorization", tokenMap.get("access_token"))
                    .params(params))
                    .andExpect(status().is(404))
                    .andExpect(objectEquals(error));
        }
        //正确校验
        map = new LinkedMultiValueMap<>();
        map.add("username", "李四");
        map.add("password", StringUtil.md5("123456"));
        map.add("department", "产品部");
        map.add("role_id", "0");
        map.add("real_name", "测试");
        map.add("remark", "测试");
        map.add("face", "http://a.jpg");
        map.add("founder", "1");
        mockMvc.perform(put("/admin/systems/manager/admin-users/" + tokenMap.get("uid"))
                .header("Authorization", tokenMap.get("access_token"))
                .params(map))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        Map dbMap = systemDaoSupport.queryForMap("select username,password,department,role_id,real_name,face,remark,founder from es_admin_user where id = ?", tokenMap.get("uid").toString());
        map.remove("password");
        map.add("password", StringUtil.md5(StringUtil.md5("123456") + "李四".toLowerCase()));
        Assert.assertEquals(formatMap(map), formatMap(dbMap));
    }

    /**
     * 管理员删除测试
     *
     * @throws Exception
     */
    @Test
    public void deleteTest() throws Exception {
        //管理员不存在校验
        ErrorMessage error = new ErrorMessage("003", "当前管理员不存在");
        mockMvc.perform(delete("/admin/systems/manager/admin-users/1")
                .header("Authorization", tokenMap.get("access_token")))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));

        //删除最后一个超级管理员测试
        this.addTest();
        adminUserManager.delete(StringUtil.toInt(tokenMap.get("uid").toString()));
        error = new ErrorMessage("916", "必须保留一个超级管理员");
        mockMvc.perform(delete("/admin/systems/manager/admin-users/" + id)
                .header("Authorization", tokenMap.get("access_token")))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //正确测试
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", "李三");
        map.add("password", StringUtil.md5("123456"));
        map.add("department", "产品部");
        map.add("role_id", "1");
        map.add("real_name", "测试");
        map.add("remark", "测试");
        map.add("face", "http://a.jpg");
        map.add("founder", "1");
        String json = mockMvc.perform(post("/admin/systems/manager/admin-users")
                .header("Authorization", tokenMap.get("access_token"))
                .params(map))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        AdminUser adminUser = JsonUtil.jsonToObject(json, AdminUser.class);
        mockMvc.perform(delete("/admin/systems/manager/admin-users/" + adminUser.getId())
                .header("Authorization", tokenMap.get("access_token")))
                .andExpect(status().is(200));
        AdminUser user = adminUserManager.getModel(adminUser.getId());
        Assert.assertNull(user);

    }

}
