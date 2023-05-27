package com.enation.app.javashop.buyer.api.member;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.base.SceneType;
import com.enation.app.javashop.core.client.system.SmsClient;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.vo.MemberVO;
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

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 会员安全api测试
 *
 * @author zh
 * @version v7.0
 * @date 18/4/25 下午2:32
 * @since v7.0
 */
@Transactional(value = "memberTransactionManager", rollbackFor = Exception.class)
public class MemberSecurityControllerTest extends BaseTest {
    @Autowired
    private MemberManager memberManager;
    @Autowired
    private SmsClient smsClient;
    @Autowired
    private Cache cache;
    List<MultiValueMap<String, String>> list = null;

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;

    String token = "";

    String bindToken = "";

    @Before
    public void dataPreparation() throws Exception {
        memberDaoSupport.execute("delete from es_member where mobile = ? or uname = ?", "13233653048", "haobeck");
        //注册一个会员
        this.register("haobeck", StringUtil.md5("111111"),"13233653048");
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        String json = mockMvc.perform(get("/passport/login")
                .param("username", "haobeck")
                .param("password", StringUtil.md5("111111"))
                .param("captcha", "1111")
                .param("uuid", uuid))
                .andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
        Map tokenMap = JsonUtil.toMap(json);
        token = tokenMap.get("access_token").toString();
        memberDaoSupport.execute("insert into es_member (uname,password,disabled,have_shop) values (?,?,?,?)","chaobeck",StringUtil.md5(StringUtil.md5("111111")+"chaobeck"),"0","0");

        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        json = mockMvc.perform(get("/passport/login")
                .param("username", "chaobeck")
                .param("password", StringUtil.md5("111111"))
                .param("captcha", "1111")
                .param("uuid", uuid))
                .andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
        tokenMap = JsonUtil.toMap(json);
        bindToken = tokenMap.get("access_token").toString();
    }

    /**
     * 发送手机验证验证码
     *
     * @throws Exception
     */
    @Test
    public void sendValSmsCodeTest() throws Exception {
        //参数为空校验
        String[] names = new String[]{"uuid", "captcha", "message"};
        String[] values1 = new String[]{"", "1111", "uuid不能为空"};
        String[] values2 = new String[]{"52c260", "", "图片验证码不能为空"};
        list = toMultiValueMaps(names, values1, values2);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/members/security/add")
                    .header("Authorization", token)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //验证码错误校验
        cache.put(CachePrefix.CAPTCHA.getPrefix() + "52c260_VALIDATE_MOBILE", "1111", 120);
        ErrorMessage error = new ErrorMessage("107", "图片验证码不正确");
        mockMvc.perform(post("/members/security/add")
                .header("Authorization", token)
                .param("uuid", "52c260")
                .param("captcha", "1234"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //未绑定手机号码发送短信测试
        error = new ErrorMessage("114", "当前会员未绑定手机号");
        mockMvc.perform(post("/members/security/add")
                .header("Authorization", buyer2)
                .param("uuid", "52c260")
                .param("captcha", "1111"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        cache.put(CachePrefix.CAPTCHA.getPrefix() + "52c260_VALIDATE_MOBILE", "1111", 120);
        //正确校验
        mockMvc.perform(post("/members/security/add")
                .header("Authorization", token)
                .param("uuid", "52c260")
                .param("captcha", "1111"))
                .andExpect(status().is(200));

    }


    /**
     * 发送手机绑定验证码
     *
     * @throws Exception
     */
    @Test
    public void sendBindSmsCodeTest() throws Exception {
        //参数为空校验
        String[] names = new String[]{"uuid", "captcha", "message"};
        String[] values1 = new String[]{"", "1111", "uuid不能为空"};
        String[] values2 = new String[]{"52c260", "", "图片验证码不能为空"};
        list = toMultiValueMaps(names, values1, values2);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/members/security/bind/add/13233653044")
                    .header("Authorization", token)
                    .header("uuid", uuid)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //验证码错误校验
        cache.put(CachePrefix.CAPTCHA.getPrefix() + "52c260_BIND_MOBILE", "1111", 120);
        ErrorMessage error = new ErrorMessage("107", "图片验证码不正确");
        mockMvc.perform(post("/members/security/bind/add/13233653044")
                .header("Authorization", token)
                .header("uuid", uuid)
                .param("uuid", "52c260")
                .param("captcha", "1234"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //已经绑定手机号码校验
        cache.put(CachePrefix.CAPTCHA.getPrefix() + "52c260_BIND_MOBILE", "1111", 120);
        error = new ErrorMessage("111", "此手机号码已经绑定其他用户");
        mockMvc.perform(post("/members/security/bind/add/13233653048")
                .header("Authorization", token)
                .header("uuid", uuid)
                .param("uuid", "52c260")
                .param("captcha", "1111"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //手机格式校验
        cache.put(CachePrefix.CAPTCHA.getPrefix() + "52c260_BIND_MOBILE", "1111", 120);
        error = new ErrorMessage("107", "手机号码格式不正确");
        mockMvc.perform(post("/members/security/bind/add/13233653")
                .header("Authorization", token)
                .header("uuid", uuid)
                .param("uuid", "52c260")
                .param("captcha", "1111"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        cache.put(CachePrefix.CAPTCHA.getPrefix() + "52c260_BIND_MOBILE", "1111", 120);
        //正确校验
        mockMvc.perform(post("/members/security/bind/add/13233653044")
                .header("Authorization", token)
                .header("uuid", uuid)
                .param("uuid", "52c260")
                .param("captcha", "1111"))
                .andExpect(status().is(200));
    }

    /**
     * 手机换绑api测试
     *
     * @throws Exception
     */
    @Test
    public void exchangeBindMobileTest() throws Exception {
        //参数为空校验
        String[] names = new String[]{"sms_code", "message"};
        String[] values1 = new String[]{"", "短信验证码不能为空"};
        list = toMultiValueMaps(names, values1);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(put("/members/security/exchange-bind/13233653044")
                    .header("Authorization", token)
                    .header("uuid", uuid)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //短信验证码错误校验
        //发送验证码
        this.sendBindSmsCodeTest();
        //校验码错误校验
        ErrorMessage error = new ErrorMessage("107", "短信验证码错误");
        mockMvc.perform(put("/members/security/exchange-bind/13233653044")
                .header("Authorization", token)
                .header("uuid", uuid)
                .param("sms_code", "2222"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //未进行手机校验进行手机绑定
        error = new ErrorMessage("115", "对已绑定手机校验失效");
        mockMvc.perform(put("/members/security/exchange-bind/13233653044")
                .header("Authorization", token)
                .header("uuid", uuid)
                .param("sms_code", "1111"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //手机号码格式校验
        error = new ErrorMessage("107", "短信验证码错误");
        mockMvc.perform(put("/members/security/exchange-bind/132336530")
                .header("Authorization", token)
                .header("uuid", uuid)
                .param("sms_code", "1111"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //正确绑定校验
        this.sendValSmsCodeTest();
        smsClient.valid(SceneType.VALIDATE_MOBILE.name(), "13233653048", "1111");
        this.sendBindSmsCodeTest();
        mockMvc.perform(put("/members/security/exchange-bind/13233653044")
                .header("Authorization", token)
                .header("uuid", uuid)
                .param("sms_code", "1111"))
                .andExpect(status().is(200));
        Member member = memberManager.getMemberByMobile("13233653044");
        Assert.assertNotNull(member);

    }

    /**
     * 修改密码测试用例
     */
    @Test
    public void updatePasswordTest() throws Exception {
        //参数为空校验
        String[] names = new String[]{"uuid", "captcha", "password", "message"};
        String[] values1 = new String[]{"", "1111", "111111", "uuid不能为空"};
        String[] values2 = new String[]{"52c260", "", "111111", "图片验证码不能为空"};
        String[] values3 = new String[]{"52c260", "1111", "", "密码长度为4-20个字符"};
        list = toMultiValueMaps(names, values1, values2, values3);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(put("/members/security/password")
                    .header("Authorization", token)
                    .header("uuid", uuid)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }

        //未经过手机验证修改密码
        cache.put(CachePrefix.CAPTCHA.getPrefix() + "52c260_" + SceneType.MODIFY_PASSWORD.name(), "1111", 120);
        ErrorMessage error = new ErrorMessage("115", "请先对当前用户进行身份校验");
        mockMvc.perform(put("/members/security/password")
                .header("Authorization", token)
                .header("uuid", uuid)
                .param("uuid", "52c260")
                .param("captcha", "1111")
                .param("password", "111111"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //图片验证码错误校验
        cache.put(CachePrefix.CAPTCHA.getPrefix() + "52c260_" + SceneType.MODIFY_PASSWORD.name(), "1111", 120);
        error = new ErrorMessage("107", "图片验证码不正确");
        mockMvc.perform(put("/members/security/password")
                .header("Authorization", token)
                .header("uuid", uuid)
                .param("uuid", "52c260")
                .param("captcha", "2222")
                .param("password", "111111"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //密码长度未在4-20位之间
        this.sendValSmsCodeTest();
        smsClient.valid(SceneType.VALIDATE_MOBILE.name(), "13233653048", "1111");
        cache.put(CachePrefix.CAPTCHA.getPrefix() + "52c260_" + SceneType.MODIFY_PASSWORD.name(), "1111", 120);
        error = new ErrorMessage("004", "密码长度为4-20个字符");
        mockMvc.perform(put("/members/security/password")
                .header("Authorization", token)
                .header("uuid", uuid)
                .param("uuid", "52c260")
                .param("captcha", "1111")
                .param("password", "123"))
                .andExpect(status().is(400))
                .andExpect(objectEquals(error));
        //正确校验
        this.sendValSmsCodeTest();
        smsClient.valid(SceneType.FIND_PASSWORD.name(), "13233653048", "1111");
        cache.put(CachePrefix.CAPTCHA.getPrefix() + "52c260_" + SceneType.VALIDATE_MOBILE.name(), "1111", 120);
        mockMvc.perform(put("/members/security/password")
                .header("Authorization", token)
                .header("uuid", uuid)
                .param("uuid", "52c260")
                .param("captcha", "1111")
                .param("password", "222222"))
                .andExpect(status().is(200));
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        String json = mockMvc.perform(get("/passport/login")
                .param("username", "haobeck")
                .param("password", "222222")
                .param("captcha", "1111")
                .param("uuid", uuid))
                .andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
        Map tokenMap = JsonUtil.toMap(json);
        Assert.assertNotNull(tokenMap);
    }

    /**
     * 手机号码绑定api测试
     *
     * @throws Exception
     */
    @Test
    public void bindMobileTest() throws Exception {
        //参数为空校验
        String[] names = new String[]{"sms_code", "message"};
        String[] values1 = new String[]{"", "短信验证码不能为空"};
        list = toMultiValueMaps(names, values1);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(put("/members/security/bind/13233653044")
                    .header("Authorization", token)
                    .header("uuid", uuid)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //短信验证码错误校验
        //发送验证码
        this.sendBindSmsCodeTest();
        //校验码错误校验
        ErrorMessage error = new ErrorMessage("107", "短信验证码错误");
        mockMvc.perform(put("/members/security/bind/13233653044")
                .header("Authorization", token)
                .header("uuid", uuid)
                .param("sms_code", "2222"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //手机号码格式校验
        error = new ErrorMessage("107", "短信验证码错误");
        mockMvc.perform(put("/members/security/bind/132336530")
                .header("Authorization", token)
                .header("uuid", uuid)
                .param("sms_code", "1111"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //正确绑定校验
        this.sendBindSmsCodeTest();
        mockMvc.perform(put("/members/security/bind/13233653044")
                .header("Authorization", bindToken)
                .header("uuid", uuid)
                .param("sms_code", "1111"))
                .andExpect(status().is(200));
        Member member = memberManager.getMemberByMobile("13233653044");
        Assert.assertNotNull(member);
        //校验此操作是否是换绑
        cache.put(CachePrefix.CAPTCHA.getPrefix() + "52c260_BIND_MOBILE", "1111", 120);
        //正确校验
        mockMvc.perform(post("/members/security/bind/add/13233653000")
                .header("Authorization", token)
                .header("uuid", uuid)
                .param("uuid", "52c260")
                .param("captcha", "1111"))
                .andExpect(status().is(200));

        error = new ErrorMessage("111", "当前会员已经绑定手机号");
        mockMvc.perform(put("/members/security/bind/13233653000")
                .header("Authorization", bindToken)
                .header("uuid", uuid)
                .param("sms_code", "1111"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));


    }

    public void clearData() throws Exception {
        mockMvc.perform(post("/member/logout")
                .header("Authorization", token)
                .header("uuid", uuid))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
    }


}
