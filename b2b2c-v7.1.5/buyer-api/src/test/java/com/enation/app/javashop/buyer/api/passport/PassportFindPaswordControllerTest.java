package com.enation.app.javashop.buyer.api.passport;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.base.SceneType;
import com.enation.app.javashop.core.client.system.SmsClient;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.core.passport.service.PassportManager;
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
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 找回密码测试
 *
 * @author zh
 * @version v7.0
 * @date 18/5/17 上午10:35
 * @since v7.0
 */
@Transactional(value = "memberTransactionManager", rollbackFor = Exception.class)
public class PassportFindPaswordControllerTest extends BaseTest {

    List<MultiValueMap<String, String>> list = null;

    @Autowired
    private MemberManager memberManager;
    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;
    @Autowired
    private SmsClient smsClient;

    @Autowired
    private Cache cache;
    public String uuid = "";


    @Before
    public void dataPreparation() throws Exception{
        memberDaoSupport.execute("delete from es_member where uname = 'coco'");
        this.memberDaoSupport.execute("insert into es_member (uname,password,disabled,have_shop,email,mobile) values(?,?,?,?,?,?)","coco",StringUtil.md5(StringUtil.md5("123123")+"coco"),"0","0","310487699@qq.com","18234124444");
    }

    /**
     * 找回密码第一步 获取用户信息
     *
     * @throws Exception
     */
    @Test
    public void getMemberInfoTest() throws Exception {
        //参数为空校验
        String[] names = new String[]{"uuid", "captcha", "account", "message"};
        String[] values1 = new String[]{"", "1111", "coco", "uuid不能为空"};
        String[] values2 = new String[]{"aaa", "", "coco", "图片验证码不能为空"};
        String[] values3 = new String[]{"aaa", "1111", "", "账户名称不能为空"};

        list = toMultiValueMaps(names, values1, values2, values3);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(get("/passport/find-pwd")
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        cache.put(CachePrefix.CAPTCHA.getPrefix() + "aaa" + "_" + SceneType.FIND_PASSWORD.name(), "1111", 120);
        //验证码错误校验
        ErrorMessage error = new ErrorMessage("107", "图片验证码不正确");
        mockMvc.perform(get("/passport/find-pwd")
                .param("uuid", "aaa")
                .param("captcha", "111111")
                .param("account", "haobeck"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //会员不存在校验
        error = new ErrorMessage("003", "此会员不存在");
        mockMvc.perform(get("/passport/find-pwd")
                .param("uuid", "aaa")
                .param("captcha", "1111")
                .param("account", "ckck"))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));
        cache.put(CachePrefix.CAPTCHA.getPrefix() + "aaa" + "_" + SceneType.FIND_PASSWORD.name(), "1111", 120);
        //正确校验 手机号码查找会员;
        String json = mockMvc.perform(get("/passport/find-pwd")
                .param("uuid", "aaa")
                .param("captcha", "1111")
                .param("account", "18234124444"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        Map map = new HashMap(16);
        map.put("uname", "c***o");
        map.put("mobile", "182****4444");
        Map rightMap = JsonUtil.jsonToObject(json, Map.class);
        rightMap.remove("uuid");
        Assert.assertEquals(map, rightMap);
        cache.put(CachePrefix.CAPTCHA.getPrefix() + "aaa" + "_" + SceneType.FIND_PASSWORD.name(), "1111", 120);
        //正确校验 用户名查找会员;
        json = mockMvc.perform(get("/passport/find-pwd")
                .param("uuid", "aaa")
                .param("captcha", "1111")
                .param("account", "coco"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        rightMap = JsonUtil.jsonToObject(json, Map.class);
        rightMap.remove("uuid");
        Assert.assertEquals(map, rightMap);
        //正确校验 邮箱查找用户
        cache.put(CachePrefix.CAPTCHA.getPrefix() + "aaa" + "_" + SceneType.FIND_PASSWORD.name(), "1111", 120);
        //正确校验 用户名查找会员;
        json = mockMvc.perform(get("/passport/find-pwd")
                .param("uuid", "aaa")
                .param("captcha", "1111")
                .param("account", "310487699@qq.com"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        rightMap = JsonUtil.jsonToObject(json, Map.class);
        uuid = rightMap.get("uuid").toString();
        rightMap.remove("uuid");
        Assert.assertEquals(map, rightMap);
    }

    //发送找回密码验证码测试
    @Test
    public void sendSmsCodeTest() throws Exception {
        //参数为空校验
        String[] names = new String[]{"uuid", "captcha", "message"};
        String[] values1 = new String[]{"", "1111", "uuid不能为空"};
        String[] values2 = new String[]{"aaa", "", "图片验证码不能为空"};

        list = toMultiValueMaps(names, values1, values2);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/passport/find-pwd/add")
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //验证码错误校验
        cache.put(CachePrefix.CAPTCHA.getPrefix() + "aaa" + "_" + SceneType.FIND_PASSWORD.name(), "1111", 120);
        ErrorMessage error = new ErrorMessage("107", "图片验证码不正确");
        mockMvc.perform(post("/passport/find-pwd/add")
                .param("uuid", "aaa")
                .param("captcha", "111111"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //uuid错误校验
        cache.put(CachePrefix.CAPTCHA.getPrefix() + "aaa" + "_" + SceneType.FIND_PASSWORD.name(), "1111", 120);
        error = new ErrorMessage("119", "请先对当前用户进行身份校验");
        mockMvc.perform(post("/passport/find-pwd/add")
                .param("uuid", "aaa")
                .param("captcha", "1111"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //正确校验
        this.getMemberInfoTest();
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_" + SceneType.FIND_PASSWORD.name(), "1111", 120);
        mockMvc.perform(post("/passport/find-pwd/add")
                .param("uuid", uuid)
                .param("captcha", "1111"))
                .andExpect(status().is(200));

    }

    //修改密码测试
    @Test
    public void updatePasswordTest() throws Exception {
        //参数为空校验
        String[] names = new String[]{"uuid", "password", "message"};
        String[] values1 = new String[]{"", "123123", "uuid不能为空"};

        list = toMultiValueMaps(names, values1);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(put("/passport/find-pwd/update-password")
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //uuid错误校验
        cache.put(CachePrefix.CAPTCHA.getPrefix() + "aaa" + "_" + SceneType.FIND_PASSWORD.name(), "1111", 120);
        ErrorMessage error = new ErrorMessage("119", "请先对当前用户进行身份校验");
        mockMvc.perform(put("/passport/find-pwd/update-password")
                .param("uuid", "aaa1")
                .param("password", "123123"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //对会员未进行手机校验进行修改密码
        cache.put(CachePrefix.CAPTCHA.getPrefix() + "aaa" + "_" + SceneType.FIND_PASSWORD.name(), "1111", 120);
        this.getMemberInfoTest();
        error = new ErrorMessage("115", "请先对当前用户进行身份校验");
        mockMvc.perform(put("/passport/find-pwd/update-password")
                .param("uuid", uuid)
                .param("password", StringUtil.md5("123123")))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //对会员是否存在进行校验
        cache.put(CachePrefix.CAPTCHA.getPrefix() + "aaa" + "_" + SceneType.FIND_PASSWORD.name(), "1111", 120);
        this.sendSmsCodeTest();
        smsClient.valid(SceneType.VALIDATE_MOBILE.name(), "18234124444", "1111");
        memberDaoSupport.execute("delete from es_member where mobile = 18234124444");
        error = new ErrorMessage("003", "当前会员不存在");
        mockMvc.perform(put("/passport/find-pwd/update-password")
                .param("uuid", uuid)
                .param("password", StringUtil.md5("123123)")))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));
        //正确校验

        this.memberDaoSupport.execute("insert into es_member (uname,password,disabled,have_shop,email,mobile) values(?,?,?,?,?,?)","coco",StringUtil.md5(StringUtil.md5("123123")+"coco"),"0","0","310487699@qq.com","18234124444");

        cache.put(CachePrefix.CAPTCHA.getPrefix() + "aaa" + "_" + SceneType.FIND_PASSWORD.name(), "1111", 120);
        this.sendSmsCodeTest();
        smsClient.valid(SceneType.VALIDATE_MOBILE.name(), "18234124444", "1111");
        mockMvc.perform(put("/passport/find-pwd/update-password")
                .param("uuid", uuid)
                .param("password", "111111"))
                .andExpect(status().is(200));
        Member memberVO = memberManager.validation("coco", "111111");
        Assert.assertNotNull(memberVO);

    }


}
