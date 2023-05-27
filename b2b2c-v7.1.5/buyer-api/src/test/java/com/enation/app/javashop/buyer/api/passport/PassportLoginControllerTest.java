package com.enation.app.javashop.buyer.api.passport;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.StringUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 会员登录测试
 *
 * @author zh
 * @version v7.0
 * @since v7.0
 * 2018年4月11日 上午8:12:12
 */
public class PassportLoginControllerTest extends BaseTest {


    List<MultiValueMap<String, String>> list = null;
    private String uuid = "52c260";
    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;
    @Autowired
    private MemberManager memberManager;
    @Autowired
    private Cache cache;

    @Before
    public void smsSendReady() throws Exception {
        //手动存入验证码
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        cache.put(CachePrefix.SMS_CODE.getPrefix() + "LOGIN" + "_" + "18234124444", "1111", 1000);
        //插入一条数据
        this.register("haobeck", StringUtil.md5("123123"),"18234124444");

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
            mockMvc.perform(post("/passport/login/smscode/18234124444")
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //手机号码格式校验
        ErrorMessage error = new ErrorMessage("107", "手机号码格式不正确！");
        mockMvc.perform(post("/passport/login/smscode/1823412")
                .param("uuid", uuid)
                .param("captcha", "1111"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //参数错误校验
        String[] values3 = new String[]{uuid, "2222", "图片验证码不正确！"};
        String[] values4 = new String[]{uuid, "1111", "该手机号未注册！"};
        list = toMultiValueMaps(names, values3, values4);
        for (MultiValueMap<String, String> params : list) {
            cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
            String message = params.get("message").get(0);
            error = new ErrorMessage("107", message);
            mockMvc.perform(post("/passport/login/smscode/18234124442")
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }
        //正确校验
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        mockMvc.perform(post("/passport/login/smscode/18234124444")
                .param("uuid", uuid)
                .param("captcha", "1111"))
                .andExpect(status().is(200));
    }

    @Test
    public void loginTest() throws Exception {
        String[] names = new String[]{"username", "password", "captcha", "uuid", "message"};
        String[] values1 = new String[]{"", "123123", "1111", uuid, "用户名不能为空"};
        String[] values2 = new String[]{"haobeck", "", "1111", uuid, "密码不能为空"};
        String[] values3 = new String[]{"haobeck", "123123", "", uuid, "图片验证码不能为空"};
        String[] values4 = new String[]{"haobeck", "123123", "1111", "", "uuid不能为空"};
        //参数为空校验
        list = toMultiValueMaps(names, values1, values2, values3, values4);
        for (MultiValueMap<String, String> params : list) {

            String message = params.get("message").get(0);

            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(get("/passport/login")
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //参数错误校验
        String[] values5 = new String[]{"haobeck", "123123", "2222", uuid, "图片验证码错误！"};
        String[] values6 = new String[]{"beckhao", "123123", "1111", uuid, "账号密码错误！"};
        String[] values7 = new String[]{"beckhao", "111111", "1111", uuid, "账号密码错误！"};
        list = toMultiValueMaps(names, values5, values6, values7);
        for (MultiValueMap<String, String> params : list) {
            cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("107", message);
            mockMvc.perform(get("/passport/login")
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }
        //手机号码和密码正确登录
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        mockMvc.perform(get("/passport/login")
                .param("username", "18234124444")
                .param("password", StringUtil.md5("123123"))
                .param("captcha", "1111")
                .param("uuid", uuid))
                .andExpect(status().is(200));
        //账户和密码正确登录
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        mockMvc.perform(get("/passport/login")
                .param("username", "haobeck")
                .param("password", StringUtil.md5("123123"))
                .param("captcha", "1111")
                .param("uuid", uuid))
                .andExpect(status().is(200));
    }

    @Test
    public void mobileLoginTest() throws Exception {
        String[] names = new String[]{"sms_code", "message"};
        String[] values1 = new String[]{"", "短信验证码不能为空"};
        list = toMultiValueMaps(names, values1);
        for (MultiValueMap<String, String> params : list) {

            String message = params.get("message").get(0);

            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(get("/passport/login/18234124444")
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //验证码错误校验
        String[] values2 = new String[]{"2222", "短信验证码错误！"};
        list = toMultiValueMaps(names, values2);
        for (MultiValueMap<String, String> params : list) {

            String message = params.get("message").get(0);

            ErrorMessage error = new ErrorMessage("107", message);
            mockMvc.perform(get("/passport/login/18234124444")
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }
        //手机号码格式校验
        String[] values3 = new String[]{"1111", "短信验证码错误！"};
        list = toMultiValueMaps(names, values3);
        for (MultiValueMap<String, String> params : list) {

            String message = params.get("message").get(0);

            ErrorMessage error = new ErrorMessage("107", message);
            mockMvc.perform(get("/passport/login/1823412444")
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }

        //正确校验
        cache.put(CachePrefix.SMS_CODE.getPrefix() + "LOGIN" + "_" + "18234124444", "1111", 1000);
        mockMvc.perform(get("/passport/login/18234124444")
                .param("sms_code", "1111"))
                .andExpect(status().is(200));
    }


    @After
    public void loginAfter() throws Exception {
        cache.remove(CachePrefix.SMS_CODE.getPrefix() + "LOGIN" + "_" + "18234124444");
        cache.remove(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN");
        memberDaoSupport.execute("delete from es_member where mobile = 18234124444");
    }

}
