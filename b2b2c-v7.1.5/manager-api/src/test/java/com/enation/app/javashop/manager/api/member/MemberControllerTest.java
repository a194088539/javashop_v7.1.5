package com.enation.app.javashop.manager.api.member;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
 * 平台会员测试用例
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018年4月9日 下午3:01:22
 */
@Transactional(value = "memberTransactionManager", rollbackFor = Exception.class)
public class MemberControllerTest extends BaseTest {

    @Autowired
    private MemberManager memberManager;
    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    List<MultiValueMap<String, String>> list = null;

    Member member = null;

    @Before
    public void dataPreparation() throws Exception {
        this.memberDaoSupport.execute("insert into es_member (uname,password,disabled,have_shop,email,mobile) values(?,?,?,?,?,?)", "haobeck", StringUtil.md5(StringUtil.md5("123456") + "haobeck"), "0", "0", "310487699@qq.com", "13233653048");
        member = memberManager.getModel(memberDaoSupport.getLastId("es_member"));
        this.memberDaoSupport.execute("insert into es_member (uname,password,disabled,have_shop,email,mobile) values(?,?,?,?,?,?)", "beckhao", StringUtil.md5(StringUtil.md5("123456") + "beckhao"), "0", "0", "310487688@qq.com", "13233653044");
    }

    /**
     * 添加会员测试
     */
    @Test
    public void addMemberTest() throws Exception {
        //参数为空非法校验
        String[] names = new String[]{"uname", "password", "mobile", "nickname", "region", "sex", "birthday", "address", "email", "tel", "face", "message"};
        String[] values1 = new String[]{"", StringUtil.md5("123123"), "13233653048", "haobeck", "5008", "1", "19940303", "阳曲县政府", "310487666@qq.com", "03515594666", "http://image.jpg", "用户名长度必须在2到20位之间"};
        String[] values2 = new String[]{"haobeck", "", "13233653048", "haobeck", "5008", "1", "19940303", "阳曲县政府", "310487666@qq.com", "03515594666", "http://image.jpg", "会员密码不能为空"};
        String[] values3 = new String[]{"haobeck", StringUtil.md5("123123"), "", "haobeck", "5008", "1", "19940303", "阳曲县政府", "310487666@qq.com", "03515594666", "http://image.jpg", "手机号码格式不正确"};
        String[] values4 = new String[]{"haobeck", StringUtil.md5("123123"), "132336530", "haobeck", "5008", "1", "19940303", "阳曲县政府", "310487666@qq.com", "03515594666", "http://image.jpg", "手机号码格式不正确"};
        String[] values5 = new String[]{"haobeck", StringUtil.md5("123123"), "13233653048", "", "5008", "1", "19940303", "阳曲县政府", "310487666@qq.com", "03515594666", "http://image.jpg", "会员昵称必须为2到20位之间"};
        String[] values6 = new String[]{"haobeck", StringUtil.md5("123123"), "13233653048", "haobeck", "5008", "", "19940303", "阳曲县政府", "310487666@qq.com", "03515594666", "http://image.jpg", "会员性别不能为空"};
        String[] values7 = new String[]{"haobeck", StringUtil.md5("123123"), "13233653048", "haobeck", "5008", "-1", "19940303", "阳曲县政府", "310487699@qq.com", "03515594666", "http://image.jpg", "必须为数字且1为男,0为女"};
        String[] values8 = new String[]{"haobeck", StringUtil.md5("123123"), "13233653048", "haobeck", "5008", "2", "19940303", "阳曲县政府", "310487666@qq.com", "03515594666", "http://image.jpg", "必须为数字且1为男,0为女"};
        String[] values11 = new String[]{"haobeck", StringUtil.md5("123123"), "13233653048", "haobeck", "5008", "1", "19940303", "阳曲县政府", "310487699", "03515594666", "http://image.jpg", "邮箱格式不正确"};
        String[] values13 = new String[]{"张", StringUtil.md5("123123"), "13233653044", "haobeck", "5008", "1", "19940303", "阳曲县政府", "310487666@qq.com", "03515594666", "http://image.jpg", "用户名长度必须在2到20位之间"};
        String[] values14 = new String[]{"张三李四王五张三李四王五张三李四王五张三李四王五", StringUtil.md5("123123"), "13233653048", "haobeck", "5008", "1", "19940303", "阳曲县政府", "310487699@qq.com", "03515594666", "http://image.jpg", "用户名长度必须在2到20位之间"};
        String[] values15 = new String[]{"haobeck", StringUtil.md5("123123"), "13233653044", "张", "5008", "1", "19940303", "阳曲县政府", "310487666@qq.com", "03515594666", "http://image.jpg", "会员昵称必须为2到20位之间"};
        String[] values16 = new String[]{"haobeck", StringUtil.md5("123123"), "13233653048", "张三李四王五张三李四王五张三李四王五张三李四王五", "5008", "1", "19940303", "阳曲县政府", "310487666@qq.com", "03515594666", "http://image.jpg", "会员昵称必须为2到20位之间"};
        list = toMultiValueMaps(names, values1, values2, values3, values4, values5, values6, values7, values8, values11, values13, values14, values15, values16);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/admin/members")
                    .header("Authorization", superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //手机号码被注册校验
        String[] values17 = new String[]{"haobeck", StringUtil.md5("123123"), "13233653044", "haobeck", "5008", "1", "19940303", "阳曲县政府", "310487446@qq.com", "03515594666", "http://image.jpg", "该手机号已经被占用"};
        list = toMultiValueMaps(names, values17);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("107", message);
            mockMvc.perform(post("/admin/members")
                    .header("Authorization", superAdmin)
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }
        //会员用户名被占用校验
        String[] values18 = new String[]{"haobeck", StringUtil.md5("123123"), "13233653033", "haobeck", "5008", "1", "19940303", "阳曲县政府", "310487666@qq.com", "03515594666", "http://image.jpg", "当前会员已经注册"};
        list = toMultiValueMaps(names, values18);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("107", message);
            mockMvc.perform(post("/admin/members")
                    .header("Authorization", superAdmin)
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }
        //邮箱被占用测试
        String[] values19 = new String[]{"zhangsan", StringUtil.md5("123123"), "13233653033", "zhangsan", "5008", "1", "19940303", "阳曲县政府", "310487688@qq.com", "03515594666", "http://image.jpg", "邮箱已经被占用"};
        list = toMultiValueMaps(names, values19);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("117", message);
            mockMvc.perform(post("/admin/members")
                    .header("Authorization", superAdmin)
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }
        //正确性校验

        String json = mockMvc.perform(post("/admin/members")
                .header("Authorization", superAdmin)
                .param("uname", "zhangsan")
                .param("password", StringUtil.md5("123123"))
                .param("mobile", "13233653022")
                .param("nickname", "zhangsan")
                .param("region", "5008")
                .param("sex", "1")
                .param("birthday", "19940303")
                .param("address", "阳曲县政府")
                .param("email", "310487555@qq.com"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        Map returnMap = JsonUtil.toMap(json);
        Member member = this.memberManager.getMemberByMobile("13233653022");
        Map map = JsonUtil.toMap(JsonUtil.objectToJson(member));
        Assert.assertEquals(returnMap, map);
    }


    //修改会员测试
    @Test
    public void editTest() throws Exception {
        //参数为空非法校验
        String[] names = new String[]{"password", "mobile", "nickname", "region", "sex", "birthday", "address", "email", "tel", "face", "message"};
        String[] values1 = new String[]{StringUtil.md5("123123"), "", "haobeck", "5008", "1", "19940303", "阳曲县政府", "310487666@qq.com", "03515594666", "http://image.jpg", "手机号码格式不正确"};
        String[] values2 = new String[]{StringUtil.md5("123123"), "132336530", "haobeck", "5008", "1", "19940303", "阳曲县政府", "310487666@qq.com", "03515594666", "http://image.jpg", "手机号码格式不正确"};
        String[] values3 = new String[]{StringUtil.md5("123123"), "13233653048", "", "5008", "1", "19940303", "阳曲县政府", "310487666@qq.com", "03515594666", "http://image.jpg", "会员昵称必须为2到20位之间"};
        String[] values4 = new String[]{StringUtil.md5("123123"), "13233653048", "haobeck", "5008", "", "19940303", "阳曲县政府", "310487666@qq.com", "03515594666", "http://image.jpg", "会员性别不能为空"};
        String[] values5 = new String[]{StringUtil.md5("123123"), "13233653048", "haobeck", "5008", "-1", "19940303", "阳曲县政府", "310487699@qq.com", "03515594666", "http://image.jpg", "必须为数字且1为男,0为女"};
        String[] values6 = new String[]{StringUtil.md5("123123"), "13233653048", "haobeck", "5008", "2", "19940303", "阳曲县政府", "310487666@qq.com", "03515594666", "http://image.jpg", "必须为数字且1为男,0为女"};
        String[] values9 = new String[]{StringUtil.md5("123123"), "13233653048", "haobeck", "5008", "1", "19940303", "阳曲县政府", "310487699", "03515594666", "http://image.jpg", "邮箱格式不正确"};
        String[] values11 = new String[]{StringUtil.md5("123123"), "13233653044", "张", "5008", "1", "19940303", "阳曲县政府", "310487666@qq.com", "03515594666", "http://image.jpg", "会员昵称必须为2到20位之间"};
        String[] values12 = new String[]{StringUtil.md5("123123"), "13233653048", "张三李四王五张三李四王五张三李四王五张三李四王五", "5008", "1", "19940303", "阳曲县政府", "310487666@qq.com", "03515594666", "http://image.jpg", "会员昵称必须为2到20位之间"};
        list = toMultiValueMaps(names, values1, values2, values3, values4, values5, values6, values9, values11, values12);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(put("/admin/members/" + member.getMemberId())
                    .header("Authorization", superAdmin)
                    .header("uuid", this.uuid)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //会员是否存在校验
        String[] values16 = new String[]{StringUtil.md5("123123"), "13233653048", "张三", "5008", "1", "19940303", "阳曲县政府", "310487666@qq.com", "03515594666", "http://image.jpg", "当前会员不存在"};
        list = toMultiValueMaps(names, values16);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("003", message);
            mockMvc.perform(put("/admin/members/" + 9999)
                    .header("Authorization", superAdmin)
                    .header("uuid", this.uuid)
                    .params(params))
                    .andExpect(status().is(404))
                    .andExpect(objectEquals(error));
        }
        //会员手机号码是否存在校验
        String[] values18 = new String[]{StringUtil.md5("123123"), "13233653044", "张三", "5008", "1", "19940303", "阳曲县政府", "310487666@qq.com", "03515594666", "http://image.jpg", "当前手机号已经被使用"};
        list = toMultiValueMaps(names, values18);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("118", message);
            mockMvc.perform(put("/admin/members/" + member.getMemberId())
                    .header("Authorization", superAdmin)
                    .header("uuid", this.uuid)
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }
        //会员手机号码是否存在校验
        String[] values19 = new String[]{StringUtil.md5("123123"), "13233653048", "张三", "5008", "1", "19940303", "阳曲县政府", "310487688@qq.com", "03515594666", "http://image.jpg", "邮箱已经被占用"};
        list = toMultiValueMaps(names, values19);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("117", message);
            mockMvc.perform(put("/admin/members/" + member.getMemberId())
                    .header("Authorization", superAdmin)
                    .header("uuid", this.uuid)
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }
        //会员修改正确校验
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("password", StringUtil.md5("123123"));
        map.add("mobile", "13233653048");
        map.add("region", "5008");
        map.add("sex", "1");
        map.add("birthday", "19940303");
        map.add("address", "阳曲县政府");
        map.add("email", "3104876666@qq.com");
        map.add("remark", "测试");
        mockMvc.perform(put("/admin/members/" + member.getMemberId())
                .header("Authorization", superAdmin)
                .header("uuid", this.uuid)
                .params(map))
                .andExpect(status().is(200));
        Map memberMap = formatMap(map);
        memberMap.remove("region");
        memberMap.put("province", "河北");
        memberMap.put("city", "石家庄市");
        memberMap.put("county", "辛集市");
        memberMap.put("town", "辛集镇");
        memberMap.put("province_id", "5");
        memberMap.put("city_id", "142");
        memberMap.put("county_id", "143");
        memberMap.put("town_id", "5008");
        memberMap.put("sex", "1");
        memberMap.put("password", StringUtil.md5(StringUtil.md5("123123")+"haobeck"));
        Map dbMemberMap = memberDaoSupport.queryForMap("select password,mobile,sex,birthday,address,email,province,city,county,town,province_id,city_id,county_id,town_id,remark from es_member where member_id = ?", member.getMemberId());
        Assert.assertEquals(this.formatMap(memberMap), this.formatMap(dbMemberMap));
    }

    /**
     * 删除会员测试
     *
     * @throws Exception
     */
    @Test
    public void deleteTest() throws Exception {
        //删除无效会员
        ErrorMessage error = new ErrorMessage("003", "当前会员不存在");
        mockMvc.perform(delete("/admin/members/999999")
                .header("Authorization", superAdmin))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));
        //正确性校验
        mockMvc.perform(delete("/admin/members/9999999")
                .header("Authorization", superAdmin))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));
        //正确性校验
        mockMvc.perform(delete("/admin/members/" + member.getMemberId())
                .header("Authorization", superAdmin))
                .andExpect(status().is(200));
        Member mb = this.memberManager.getModel(member.getMemberId());

        thrown.expect(ServiceException.class);
        thrown.expectMessage("当前账号已经禁用，请联系管理员");
        memberManager.login(member.getUname(), StringUtil.md5("123456"),1);
        Assert.assertEquals("-1", mb.getDisabled().toString());


    }

    /**
     * 会员恢复测试
     *
     * @throws Exception
     */

    @Test
    public void recoveryTest() throws Exception {
        ErrorMessage error = new ErrorMessage("003", "当前会员不存在");
        mockMvc.perform(post("/admin/members/999999")
                .header("Authorization", superAdmin))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));
        this.deleteTest();
        //正确性校验
        mockMvc.perform(post("/admin/members/" + member.getMemberId())
                .header("Authorization", superAdmin))
                .andExpect(status().is(200));
        Member mb = this.memberManager.getModel(member.getMemberId());
        Assert.assertNotNull(memberManager.login(member.getUname(), StringUtil.md5("123456"),1));
        Assert.assertEquals("0", mb.getDisabled().toString());

    }

}
