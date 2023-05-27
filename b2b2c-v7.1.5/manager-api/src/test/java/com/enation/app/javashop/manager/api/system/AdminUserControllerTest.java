package com.enation.app.javashop.manager.api.system;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.system.model.dos.RoleDO;
import com.enation.app.javashop.core.system.model.vo.AdminLoginVO;
import com.enation.app.javashop.core.system.model.vo.AdminUserVO;
import com.enation.app.javashop.core.system.service.AdminUserManager;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.security.model.JWTConstant;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import com.enation.app.javashop.framework.util.TokenKeyGenerate;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
public class AdminUserControllerTest extends BaseTest {

    List<MultiValueMap<String, String>> list = null;
    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport systemDaoSupport;

    @Autowired
    private Cache cache;

    @Autowired
    private AdminUserManager adminUserManager;

    Integer roleId = 0;

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
        adminUserVO.setUsername("张三");
        adminUserVO.setPassword(StringUtil.md5("123456"));
        adminUserVO.setRoleId(roleId);
        adminUserVO.setRealName("测试");
        adminUserVO.setDepartment("产品部");
        adminUserVO.setFace("http://a.jpg");
        adminUserVO.setFounder(1);
        adminUserManager.add(adminUserVO);
    }


    /**
     * 管理员登录测试
     *
     * @throws Exception
     */
    @Test
    public void login() throws Exception {
        String uuid = "aaa";
        String[] names = new String[]{"username", "password", "captcha", "uuid", "message"};
        String[] values1 = new String[]{"", StringUtil.md5("123456"), "1111", uuid, "用户名不能为空"};
        String[] values2 = new String[]{"haobeck", "", "1111", uuid, "密码不能为空"};
        String[] values3 = new String[]{"haobeck", StringUtil.md5("123456"), "", uuid, "图片验证码不能为空"};
        String[] values4 = new String[]{"haobeck", StringUtil.md5("123456"), "1111", "", "uuid不能为空"};
        //参数为空校验
        list = toMultiValueMaps(names, values1, values2, values3, values4);
        for (MultiValueMap<String, String> params : list) {

            String message = params.get("message").get(0);

            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(get("/admin/systems/admin-users/login")
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }

        //图片校验码错误校验
        String[] values5 = new String[]{"haobeck", StringUtil.md5("123456"), "2222", uuid, "图片验证码错误"};
        list = toMultiValueMaps(names, values5);
        for (MultiValueMap<String, String> params : list) {
            cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("107", message);
            mockMvc.perform(get("/admin/systems/admin-users/login")
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }
        //账号密码错误校验
        String[] values6 = new String[]{"beckhao", StringUtil.md5("111111"), "1111", uuid, "管理员账号密码错误"};
        String[] values7 = new String[]{"beckhao", StringUtil.md5("123456"), "1111", uuid, "管理员账号密码错误"};
        list = toMultiValueMaps(names, values6, values7);
        for (MultiValueMap<String, String> params : list) {
            cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("918", message);
            mockMvc.perform(get("/admin/systems/admin-users/login")
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }

        //正确校验
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        mockMvc.perform(get("/admin/systems/admin-users/login")
                .param("username", "张三")
                .param("password", StringUtil.md5("123456"))
                .param("uuid", "aaa")
                .param("captcha", "1111"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();


    }


    /**
     * 重新获取token校验
     *
     * @throws Exception
     */
    @Test
    public void refreshToken() throws Exception {
        //参数为空校验
        ErrorMessage error = new ErrorMessage("004", "刷新token不能为空");
        mockMvc.perform(post("/admin/systems/admin-users/token")
                .param("refresh_token", ""))
                .andExpect(status().is(400))
                .andExpect(objectEquals(error))
                .andReturn().getResponse().getContentAsString();
        //refreshToken过期校验
        error = new ErrorMessage("109", "当前token已经失效");
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        String json = mockMvc.perform(get("/admin/systems/admin-users/login")
                .param("username", "张三")
                .param("password", StringUtil.md5("123456"))
                .param("uuid", uuid)
                .param("captcha", "1111").header("uuid", uuid))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        Map map = JsonUtil.toMap(json);
        //毫秒
        Thread.currentThread().sleep(41000);
        mockMvc.perform(post("/admin/systems/admin-users/token")
                .param("refresh_token", StringUtil.toString(map.get("refresh_token"))))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error))
                .andReturn().getResponse().getContentAsString();
        //执行退出
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        json = mockMvc.perform(get("/admin/systems/admin-users/login")
                .param("username", "张三")
                .param("password", StringUtil.md5("123456"))
                .param("uuid", uuid)
                .param("captcha", "1111").header("uuid", uuid))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        map = JsonUtil.toMap(json);
        cache.remove(TokenKeyGenerate.generateAdminRefreshToken(uuid,Integer.parseInt(map.get("uid").toString())));
        error = new ErrorMessage("110", "当前管理员已经退出");
        mockMvc.perform(post("/admin/systems/admin-users/token")
                .param("refresh_token", StringUtil.toString(map.get("refresh_token"))))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error))
                .andReturn().getResponse().getContentAsString();
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        json = mockMvc.perform(get("/admin/systems/admin-users/login")
                .param("username", "张三")
                .param("password", StringUtil.md5("123456"))
                .param("uuid", uuid)
                .param("captcha", "1111").header("uuid", uuid))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        map = JsonUtil.toMap(json);
        String tokenMap = mockMvc.perform(post("/admin/systems/admin-users/token")
                .param("refresh_token", StringUtil.toString(map.get("refresh_token"))).header("uuid", uuid))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        Map token = JsonUtil.jsonToObject(tokenMap, Map.class);
        String accessToken = StringUtil.toString(token.get("accessToken"));
        Claims claims
                = Jwts.parser()
                .setSigningKey(JWTConstant.SECRET)
                .parseClaimsJws(accessToken).getBody();
        Integer uid = claims.get("uid", Integer.class);
        boolean b = (uid.toString().equals(map.get("uid").toString()));
        Assert.assertTrue(b);
    }
}
