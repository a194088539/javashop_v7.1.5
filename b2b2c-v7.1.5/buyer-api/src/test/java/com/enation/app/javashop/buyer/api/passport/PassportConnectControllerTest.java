package com.enation.app.javashop.buyer.api.passport;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.enums.ConnectTypeEnum;
import com.enation.app.javashop.core.member.model.vo.Auth2Token;
import com.enation.app.javashop.core.member.service.MemberManager;
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
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author zjp
 * @version v7.0
 * @Description
 * @ClassName PassportConnectControllerTest
 * @since v7.0 上午10:50 2018/6/19
 */
@Transactional(value = "memberTransactionManager", rollbackFor = Exception.class)
public class PassportConnectControllerTest extends BaseTest {
    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;
    @Autowired
    private Cache cache;

    private String uuid = "52c260";

    List<MultiValueMap<String, String>> list = null;

    Member member = new Member();

    @Before
    public void insertTestData() throws Exception {
        this.memberDaoSupport.execute("delete from es_member");
        this.memberDaoSupport.execute("delete from es_connect");

        //手动存入验证码
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        cache.put(CachePrefix.SMS_CODE.getPrefix() + "LOGIN" + "_" + "18234124444", "1111", 1000);
        cache.put(CachePrefix.SMS_CODE.getPrefix() + "LOGIN" + "_" + "18234123333", "1111", 1000);
        cache.put(CachePrefix.SMS_CODE.getPrefix() + "LOGIN" + "_" + "18234125555", "1111", 1000);
        //插入一条数据
        member.setUname("test");
        member.setPassword("111111");
        member.setBirthday(1523259789l);
        member.setMobile("18234124444");
        member.setHaveShop(0);
        Map memberMap = this.register("test", StringUtil.md5("111111"), "18234124444");

        Map map = new HashMap(2);
        map.put("member_id", memberMap.get("uid"));
        map.put("union_type", ConnectTypeEnum.QQ.name());

        member.setUname("test1");
        member.setPassword("111111");
        member.setMobile("18234123333");
        memberMap = this.register("test1", StringUtil.md5("111111"), "18234123333");

        map.put("union_id", "123456789");
        map.put("member_id", memberMap.get("uid"));

        Auth2Token auth2Token = new Auth2Token();
        auth2Token.setUnionid("987654321");
        auth2Token.setType(ConnectTypeEnum.QQ.value());
        cache.put(CachePrefix.CONNECT_LOGIN.getPrefix() + uuid, auth2Token, 300);

        this.memberDaoSupport.insert("es_connect", map);

    }

    @Test
    public void pcBindTest() throws Exception {
        String[] names = new String[]{"username", "password", "captcha", "uuid", "message"};
        String[] values1 = new String[]{"", "111111", "1111", uuid, "用户名不能为空"};
        String[] values2 = new String[]{"test", "", "1111", uuid, "密码不能为空"};
        String[] values3 = new String[]{"test", "111111", "", uuid, "图片验证码不能为空"};
        //参数为空校验
        list = toMultiValueMaps(names, values1, values2, values3);
        for (MultiValueMap<String, String> params : list) {

            String message = params.get("message").get(0);

            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(put("/passport/login-binder/pc/" + uuid)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //参数错误校验
        String[] values5 = new String[]{"test", "111111", "2222", uuid, "图片验证码错误！"};
        String[] values6 = new String[]{"test", "123123", "1111", uuid, "账号密码错误！"};
        list = toMultiValueMaps(names, values5, values6);
        for (MultiValueMap<String, String> params : list) {
            cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("107", message);
            mockMvc.perform(put("/passport/login-binder/pc/" + uuid)
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }

        String[] values7 = new String[]{"test", StringUtil.md5("111111"), "1111", uuid, ""};
        list = toMultiValueMaps(names, values7);
        //会员未绑定授权正确性校验
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        String contentAsString = mockMvc.perform(put("/passport/login-binder/pc/" + uuid)
                .params(list.get(0)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        LinkedHashMap<String, Object> map = JsonUtil.toMap(contentAsString);
        Assert.assertEquals("bind_success", map.get("result"));

        //会员已经授权 正确性校验
        String[] values8 = new String[]{"test1", StringUtil.md5("111111"), "1111", uuid, ""};
        list = toMultiValueMaps(names, values8);
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        contentAsString = mockMvc.perform(put("/passport/login-binder/pc/" + uuid)
                .params(list.get(0)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        map = JsonUtil.toMap(contentAsString);
        Assert.assertEquals("existed", map.get("result"));
    }

    /**
     * 会员发送登录短信测试
     */
    @Test
    public void smscodeTest() throws Exception {
        //为空校验
        String[] names = new String[]{"uuid", "captcha", "message"};
        String[] values1 = new String[]{"", "1111", "uuid不能为空"};
        String[] values2 = new String[]{uuid, "", "图片验证码不能为空"};
        list = toMultiValueMaps(names, values1, values2);

        for (MultiValueMap<String, String> params : list) {

            String message = params.get("message").get(0);

            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/passport/mobile-binder/wap/smscode/18234124442")
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //手机号码格式校验
        ErrorMessage error = new ErrorMessage("107", "手机号码格式不正确！");
        mockMvc.perform(post("/passport/mobile-binder/wap/smscode/1823412")
                .param("uuid", uuid)
                .param("captcha", "1111"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //正确校验
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        mockMvc.perform(post("/passport/mobile-binder/wap/smscode/18234124444")
                .param("uuid", uuid)
                .param("captcha", "1111"))
                .andExpect(status().is(200));
    }

    /**
     * wap手机绑定
     *
     * @throws Exception
     */
    @Test
    public void mobileBindTest() throws Exception {
        String[] names = new String[]{"sms_code", "mobile", "message"};
        String[] values1 = new String[]{"", "18234124444", "短信验证码不能为空"};
        String[] values4 = new String[]{"1111", "", "手机号不能为空"};
        list = toMultiValueMaps(names, values1, values4);
        for (MultiValueMap<String, String> params : list) {

            String message = params.get("message").get(0);

            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/passport/mobile-binder/wap/" + uuid)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //验证码错误校验
        String[] values2 = new String[]{"2222", "18234124444", "短信验证码错误！"};
        list = toMultiValueMaps(names, values2);
        for (MultiValueMap<String, String> params : list) {

            String message = params.get("message").get(0);

            ErrorMessage error = new ErrorMessage("107", message);
            mockMvc.perform(post("/passport/mobile-binder/wap/" + uuid)
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }

        //会员存在且未绑定过正确校验
        String contentAsString = mockMvc.perform(post("/passport/mobile-binder/wap/" + uuid)
                .param("sms_code", "1111")
                .param("mobile", "18234124444"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        LinkedHashMap<String, Object> map = JsonUtil.toMap(contentAsString);
        Assert.assertEquals("bind_success", map.get("result"));

        //会员存在且已经绑定过正确校验
        contentAsString = mockMvc.perform(post("/passport/mobile-binder/wap/" + uuid)
                .param("sms_code", "1111")
                .param("mobile", "18234123333"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        map = JsonUtil.toMap(contentAsString);
        Assert.assertEquals("existed", map.get("result"));

        //当前手机不存在会员正确校验
        contentAsString = mockMvc.perform(post("/passport/mobile-binder/wap/" + uuid)
                .param("sms_code", "1111")
                .param("mobile", "18234125555"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        map = JsonUtil.toMap(contentAsString);
        Assert.assertEquals("bind_success", map.get("result"));
    }

    /**
     * wap登录绑定
     *
     * @throws Exception
     */
    @Test
    public void wapBindTest() throws Exception {
        String[] names = new String[]{"username", "password", "captcha", "uuid", "message"};
        String[] values1 = new String[]{"", "111111", "1111", uuid, "用户名不能为空"};
        String[] values2 = new String[]{"test", "", "1111", uuid, "密码不能为空"};
        String[] values3 = new String[]{"test", "111111", "", uuid, "图片验证码不能为空"};
        //参数为空校验
        list = toMultiValueMaps(names, values1, values2, values3);
        for (MultiValueMap<String, String> params : list) {

            String message = params.get("message").get(0);

            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/passport/login-binder/wap/" + uuid)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //参数错误校验
        String[] values5 = new String[]{"test", "111111", "2222", uuid, "图片验证码错误！"};
        String[] values6 = new String[]{"test1", "111112", "1111", uuid, "账号密码错误！"};
        String[] values7 = new String[]{"test", "111112", "1111", uuid, "账号密码错误！"};
        list = toMultiValueMaps(names, values5, values6, values7);
        for (MultiValueMap<String, String> params : list) {
            cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("107", message);
            mockMvc.perform(post("/passport/login-binder/wap/" + uuid)
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }

        //会员未绑定授权 真确性校验
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        String contentAsString = mockMvc.perform(post("/passport/login-binder/wap/" + uuid)
                .param("username", "test")
                .param("password", StringUtil.md5("111111"))
                .param("captcha", "1111")
                .param("uuid", uuid))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        LinkedHashMap<String, Object> map = JsonUtil.toMap(contentAsString);
        Assert.assertEquals("bind_success", map.get("result"));

        //会员已绑定授权 真确性校验
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        contentAsString = mockMvc.perform(post("/passport/login-binder/wap/" + uuid)
                .param("username", "test1")
                .param("password", StringUtil.md5("111111"))
                .param("captcha", "1111")
                .param("uuid", uuid))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        map = JsonUtil.toMap(contentAsString);
        Assert.assertEquals("existed", map.get("result"));
    }
}
