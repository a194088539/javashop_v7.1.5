package com.enation.app.javashop.buyer.api.passport;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.service.MemberManager;
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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 会员登录测试
 *
 * @author zh
 * @version v7.0
 * @since v7.0
 * 2018年4月11日 下午8:12:12
 */
public class PassportControllerTest extends BaseTest {

    List<MultiValueMap<String, String>> list = null;
    @Autowired
    private Cache cache;
    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;
    @Autowired
    private MemberManager memberManager;

    @Before
    public void sendSmsCode() throws Exception {
        this.memberDaoSupport.execute("delete from es_member where mobile = '18234124444'");
        cache.put(CachePrefix.CAPTCHA.getPrefix() + "52c260_REGISTER", "1111", 1000);
        mockMvc.perform(post("/passport/register/smscode/18234124444")
                .param("uuid", "52c260")
                .param("captcha", "1111"))
                .andExpect(status().is(200));
        this.register("haobeckhaobeckhao",StringUtil.md5("123123"),"18234124444");


    }

    @Test
    public void checkSmsCodeTest() throws Exception {
        //为空校验
        String[] names = new String[]{"scene", "sms_code", "message"};
        String[] values1 = new String[]{"", "1111", "业务场景不能为空"};
        String[] values2 = new String[]{"REGISTER", "", "验证码不能为空"};
        list = toMultiValueMaps(names, values1, values2);

        for (MultiValueMap<String, String> params : list) {

            String message = params.get("message").get(0);

            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(get("/passport/smscode/18234124444")
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //手机格式校验
        String[] values3 = new String[]{"REGISTER", "1111", "短信验证码不正确"};
        list = toMultiValueMaps(names, values3);
        for (MultiValueMap<String, String> params : list) {

            String message = params.get("message").get(0);

            ErrorMessage error = new ErrorMessage("107", message);
            mockMvc.perform(get("/passport/smscode/1823412")
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }
        cache.put(CachePrefix.SMS_CODE.getPrefix()  + "REGISTER_18234124444", "1111", 10000);
        //正确校验
        mockMvc.perform(get("/passport/smscode/18234124444")
                .param("scene", "REGISTER")
                .param("sms_code", "1111"))
                .andExpect(status().is(200));

    }


    @Test
    public void checkUserNameTest() throws Exception {
        //校验用户名不存在情况
        String json = mockMvc.perform(get("/passport/username/11111"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        Map map = JsonUtil.jsonToObject(json, Map.class);
        boolean b = (boolean) map.get("exist");
        Assert.assertFalse(b);

        //校验用户名存在的情况 而且返回的用户名是未注册的
        json = mockMvc.perform(get("/passport/username/haobeckhaobeckhao"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        map = JsonUtil.jsonToObject(json, Map.class);
        //校验返回值是否存在此会员是否正确
        b = (boolean) map.get("exist");
        Assert.assertTrue(b);
        //校验返回的用户名称是否是未注册的会员
        List list = (List) map.get("suggests");
        for (int i = 0; i < list.size(); i++) {
            Member member = memberManager.getMemberByName(list.get(i).toString());
            Assert.assertNull(member);
        }


    }

    @Test
    public void checkMobileTest() throws Exception {

        //校验手机号码不存在情况
        String json = mockMvc.perform(get("/passport/mobile/18234124442"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        Map map = JsonUtil.jsonToObject(json, Map.class);
        boolean b = (boolean) map.get("exist");
        Assert.assertFalse(b);

        //校验手机号码格式不正确
        ErrorMessage error = new ErrorMessage("107", "手机号码格式不正确");
        mockMvc.perform(get("/passport/mobile/abc"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //校验手机号码存在情况
        json = mockMvc.perform(get("/passport/mobile/18234124444"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        map = JsonUtil.jsonToObject(json, Map.class);
        b = (boolean) map.get("exist");
        Assert.assertTrue(b);

    }

    /**
     * 刷新token测试脚本
     */
    @Test
    public void refreshTokenTest() throws Exception {
        //参数为空校验
        ErrorMessage error = new ErrorMessage("004", "刷新token不能为空");
        mockMvc.perform(post("/passport/token")
                .param("refresh_token", ""))
                .andExpect(status().is(400))
                .andExpect(objectEquals(error))
                .andReturn().getResponse().getContentAsString();

        //refreshToken过期校验
        error = new ErrorMessage("109", "当前token已经失效");
        Map map = this.login("haobeckhaobeckhao", StringUtil.md5("123123"));
        //毫秒
        Thread.currentThread().sleep(41000);
        mockMvc.perform(post("/passport/token")
                .param("refresh_token", map.get("refresh_token").toString()))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error))
                .andReturn().getResponse().getContentAsString();

        //用户是否退出校验
        map = this.login("haobeckhaobeckhao", StringUtil.md5("123123"));
        error = new ErrorMessage("110", "当前会员已经退出");
        //执行退出

        cache.remove(TokenKeyGenerate.generateBuyerRefreshToken(uuid,Integer.parseInt(map.get("uid").toString())));
        mockMvc.perform(post("/passport/token")
                .param("refresh_token", map.get("refresh_token").toString())
                .header("uuid", "123456789"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error))
                .andReturn().getResponse().getContentAsString();

        //正确校验
        map = this.login("haobeckhaobeckhao", StringUtil.md5("123123"));
        String tokenMap = mockMvc.perform(post("/passport/token")
                .param("refresh_token", map.get("refresh_token").toString()).header("uuid", "123456789"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        Map token = JsonUtil.jsonToObject(tokenMap, Map.class);
        String accessToken = StringUtil.toString(token.get("accessToken"));
        Claims claims
                = Jwts.parser()
                .setSigningKey(JWTConstant.SECRET)
                .parseClaimsJws(accessToken).getBody();
        Integer uid = claims.get("uid", Integer.class);
        boolean b = (uid.equals(StringUtil.toInt(map.get("uid"), false)));
        Assert.assertTrue(b);


    }


    @After
    public void afterTest() {
        cache.remove(CachePrefix.CAPTCHA.getPrefix() + "52c260_REGISTER");
        this.memberDaoSupport.execute("delete from es_member where mobile = 18234124444");

    }
}
