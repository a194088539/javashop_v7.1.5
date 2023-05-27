package com.enation.app.javashop.buyer.api.passport;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.base.SceneType;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.StringUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 会员注册测试
 *
 * @author zh
 * @version v7.0
 * @since v7.0
 * 2018年4月8日 下午8:12:12
 */
public class PassportRegisterControllerTest extends BaseTest {

    @Autowired
    private MemberManager memberManager;
    @Autowired
    private Cache cache;
    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;


    private String uuid = "52c260";

    List<MultiValueMap<String, String>> list = null;

    @Before
    public void smsSendReady() throws Exception {
        //手动存入验证码
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_REGISTER", "1111", 10000);
        this.register("haobeck", StringUtil.md5("123123"),"18234124444");
    }

    /**
     * 会员发送注册短信测试
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
            mockMvc.perform(post("/passport/register/smscode/18234124444")
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //手机号码格式校验
        ErrorMessage error = new ErrorMessage("107", "手机号码格式不正确！");
        mockMvc.perform(post("/passport/register/smscode/1823412")
                .param("uuid", uuid)
                .param("captcha", "1111"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //参数错误校验
        String[] values3 = new String[]{uuid, "2222", "图片验证码不正确！"};
        String[] values4 = new String[]{uuid, "1111", "该手机号已经被占用！"};
        list = toMultiValueMaps(names, values3, values4);
        for (MultiValueMap<String, String> params : list) {
            cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_REGISTER", "1111", 10000);
            String message = params.get("message").get(0);
            error = new ErrorMessage("107", message);
            mockMvc.perform(post("/passport/register/smscode/18234124444")
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_REGISTER", "1111", 10000);
        //正确校验
        mockMvc.perform(post("/passport/register/smscode/13233653048")
                .param("uuid", uuid)
                .param("captcha", "1111"))
                .andExpect(status().is(200));

    }

    /**
     * pc注册测试
     *
     * @throws Exception
     */
    @Test
    public void registerForPCTest() throws Exception {
        cache.remove(CachePrefix.SMS_CODE.getPrefix() + SceneType.REGISTER.name() + "_13233653048");
        //测试未发送验证码 验证验证码
        String[] names = new String[]{"username", "mobile", "password", "sms_code", "message"};
        String[] values0 = new String[]{"测试用户名", "13233653048", "c20ad4d76fe97759aa27a0c99bff6710", "1111", "短信验证码错误"};
        list = toMultiValueMaps(names, values0);
        for (MultiValueMap<String, String> params : list) {

            String message = params.get("message").get(0);

            ErrorMessage error = new ErrorMessage("107", message);
            mockMvc.perform(post("/passport/register/pc")
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }

        //发送短信验证码
        mockMvc.perform(post("/passport/register/smscode/13233653048")
                .param("uuid", uuid)
                .param("scene", "REGISTER")
                .param("captcha", "1111"))
                .andExpect(status().is(200));


        //参数为空校验
        String[] values1 = new String[]{"", "13233653048", "c20ad4d76fe97759aa27a0c99bff6710", "1111", "用户名不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values2 = new String[]{"测", "13233653048", "c20ad4d76fe97759aa27a0c99bff6710", "1111", "用户名不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values3 = new String[]{"测试测试测试测试1234567890123", "13233653048", "c20ad4d76fe97759aa27a0c99bff6710", "1111", "用户名不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values4 = new String[]{"测试用户名", "", "c20ad4d76fe97759aa27a0c99bff6710", "1111", "手机号不能为空"};
        String[] values5 = new String[]{"测试用户名", "13233653048", "", "1111", "密码格式不正确"};
        String[] values6 = new String[]{"测试用户名", "13233653048", "123", "1111", "密码格式不正确"};
        String[] values8 = new String[]{"测试用户名", "13233653048", "c20ad4d76fe97759aa27a0c99bff6710", "", "短信验证码不能为空"};
        String[] values9 = new String[]{"测试用@户名", "13233653048", "c20ad4d76fe97759aa27a0c99bff6710", "1111", "用户名不能为纯数字和特殊字符，并且长度为2-20个字符"};
        String[] values10 = new String[]{"13233653048", "13233653048", "c20ad4d76fe97759aa27a0c99bff6710", "1111", "用户名不能为纯数字和特殊字符，并且长度为2-20个字符"};

        list = toMultiValueMaps(names, values1, values2, values3, values4, values5, values6, values8, values9, values10);
        for (MultiValueMap<String, String> params : list) {

            String message = params.get("message").get(0);

            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/passport/register/pc")
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //参数错误值校验
        String[] values11 = new String[]{"测试用户名", "132336530", "C20AD4D76FE97759AA27A0C99BFF6710", "1111", "短信验证码错误"};
        String[] values12 = new String[]{"测试用户名", "13233653048", "C20AD4D76FE97759AA27A0C99BFF6710", "2222", "短信验证码错误"};
        String[] values13 = new String[]{"haobeck", "13233653048", "C20AD4D76FE97759AA27A0C99BFF6710", "1111", "当前会员已经注册"};

        list = toMultiValueMaps(names, values11, values12, values13);
        for (MultiValueMap<String, String> params : list) {
            cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_REGISTER", "1111", 10000);
            //发送短信验证码
            mockMvc.perform(post("/passport/register/smscode/13233653048")
                    .param("uuid", uuid)
                    .param("scene", "REGISTER")
                    .param("captcha", "1111"))
                    .andExpect(status().is(200));
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("107", message);
            mockMvc.perform(post("/passport/register/pc")
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_REGISTER", "1111", 10000);
        //发送短信验证码
        mockMvc.perform(post("/passport/register/smscode/13233653048")
                .param("uuid", uuid)
                .param("scene", "REGISTER")
                .param("captcha", "1111"))
                .andExpect(status().is(200));


        //正确校验
        mockMvc.perform(post("/passport/register/pc")
                .param("username", "测试用户名")
                .param("mobile", "13233653048")
                .param("password", "C20AD4D76FE97759AA27A0C99BFF6710")
                .param("sms_code", "1111"))
                .andExpect(status().is(200));
        Member member = memberManager.getMemberByMobile("13233653048");
        Assert.assertNotNull(member);

    }

    /**
     * wap注册测试
     *
     * @throws Exception
     */
    @Test
    public void registerForWapTest() throws Exception {
        String[] names = new String[]{"mobile", "password", "message"};
        String[] values0 = new String[]{"", "C20AD4D76FE97759AA27A0C99BFF6710", "手机号码格式不正确"};
        String[] values1 = new String[]{"13233653048", "", "密码格式不正确"};
        String[] values2 = new String[]{"1323365304", "C20AD4D76FE97759AA27A0C99BFF6710", "手机号码格式不正确"};
        list = toMultiValueMaps(names, values0, values1, values2);
        for (MultiValueMap<String, String> params : list) {

            String message = params.get("message").get(0);

            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/passport/register/wap")
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        String[] values3 = new String[]{"18234124444", "C20AD4D76FE97759AA27A0C99BFF6710", "该手机号已经被占用"};
        list = toMultiValueMaps(names, values3);
        for (MultiValueMap<String, String> params : list) {

            String message = params.get("message").get(0);

            ErrorMessage error = new ErrorMessage("107", message);
            mockMvc.perform(post("/passport/register/wap")
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }

        //正确校验
        mockMvc.perform(post("/passport/register/wap")
                .param("mobile", "13233653048")
                .param("password", "C20AD4D76FE97759AA27A0C99BFF6710"))
                .andExpect(status().is(200));
        Member member = memberManager.getMemberByMobile("13233653048");
        Assert.assertNotNull(member);
    }

    @After
    public void smsSendAfter() {
        // 清除缓存
        cache.remove(CachePrefix.CAPTCHA.getPrefix() + uuid + "_REGISTER");
        cache.remove(CachePrefix.SMS_CODE.getPrefix() + SceneType.REGISTER.name() + "_13233653048");
        //删除数据库
        memberDaoSupport.execute("delete from es_member where mobile = 18234124444");
        memberDaoSupport.execute("delete from es_member where mobile = 13233653048");
    }


}
