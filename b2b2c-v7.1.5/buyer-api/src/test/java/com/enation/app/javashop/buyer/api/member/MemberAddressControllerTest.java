package com.enation.app.javashop.buyer.api.member;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.base.SceneType;
import com.enation.app.javashop.core.base.plugin.express.util.MD5;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.dos.MemberAddress;
import com.enation.app.javashop.core.member.model.vo.MemberVO;
import com.enation.app.javashop.core.member.service.MemberAddressManager;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 会员地址测试类
 *
 * @author zh
 * @version v7.0
 * @date 18/4/23 下午2:54
 * @since v7.0
 */
@Transactional(value = "memberTransactionManager", rollbackFor = Exception.class)
public class MemberAddressControllerTest extends BaseTest {

    List<MultiValueMap<String, String>> list = null;


    String token = null;
    /**
     * 添加成功的会员地址
     */
    Integer addressId = 0;
    @Autowired
    private MemberManager memberManager;

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;

    @Autowired
    private Cache cache;

    @Autowired
    private MemberAddressManager memberAddressManager;


    @Before
    public void dataPreparation() throws Exception {
        this.memberDaoSupport.execute("delete from es_member where mobile=?", "18234124444");
        this.register("haobeckhaobeckhao", StringUtil.md5("123123"),"18234124444");

        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        String json = mockMvc.perform(get("/passport/login")
                .param("username", "haobeckhaobeckhao")
                .param("password", StringUtil.md5("123123"))
                .param("captcha", "1111")
                .param("uuid", uuid))
                .andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
        Map map = JsonUtil.toMap(json);
        token = map.get("access_token").toString();
    }

    /**
     * 添加会员地址测试用例
     *
     * @throws Exception
     */
    @Test
    public void addTest() throws Exception {
        //参数为空校验
        String[] names = new String[]{"name", "addr", "tel", "mobile", "def_addr", "ship_address_name", "region", "message"};
        String[] values1 = new String[]{"", "山西省太原市阳曲县政府", "0351-5594660", "13233653048", "1", "学校", "5008", "收货人姓名不能为空"};
        String[] values2 = new String[]{"小皮皮", "", "0351-5594660", "13233653048", "1", "学校", "5008", "详细地址不能为空"};
        String[] values3 = new String[]{"小皮皮", "山西省太原市阳曲县政府", "0351-5594660", "", "1", "学校", "5008", "手机号码格式不正确"};
        String[] values4 = new String[]{"小皮皮", "山西省太原市阳曲县政府", "0351-5594660", "13233653048", "2", "学校", "5008", "是否为默认地址参数错误"};
        String[] values5 = new String[]{"小皮皮", "山西省太原市阳曲县政府", "0351-5594660", "13233653048", "-1", "学校", "5008", "是否为默认地址参数错误"};
        String[] values6 = new String[]{"小皮皮", "山西省太原市阳曲县政府", "0351-5594660", "13233653048", "", "学校", "5008", "是否为默认地址不能为空"};
        String[] values7 = new String[]{"小皮皮", "山西省太原市阳曲县政府", "0351-5594660", "132336530", "1", "学校", "5008", "手机号码格式不正确"};
        list = toMultiValueMaps(names, values1, values2, values3, values4, values5, values6, values7);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/members/address")
                    .params(params)
                    .header("Authorization", token))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //会员是否存在校验
        ErrorMessage error = new ErrorMessage("003", "当前会员不存在");
        mockMvc.perform(post("/members/address")
                .param("name", "小皮皮")
                .param("addr", "山西省太原市阳曲县政府")
                .param("mobile", "13233653048")
                .param("def_addr", "1")
                .param("region", "5008")
                .header("Authorization", buyer1))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));
        memberDaoSupport.execute("delete from es_member_address");
        //对会员地址上限测试,最多为20个地址
        int array = 20;
        for (int i = 0; i < array; i++) {
            mockMvc.perform(post("/members/address")
                    .param("name", "小皮皮")
                    .param("addr", "山西省太原市阳曲县政府")
                    .param("mobile", "13233653048")
                    .param("def_addr", "1")
                    .param("region", "5008")
                    .header("Authorization", token))
                    .andExpect(status().is(200));
        }
        error = new ErrorMessage("100", "会员地址已达20个上限，无法添加");
        mockMvc.perform(post("/members/address")
                .param("name", "小皮皮")
                .param("addr", "山西省太原市阳曲县政府")
                .param("mobile", "13233653048")
                .param("def_addr", "1")
                .param("region", "5008")
                .header("Authorization", token))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        memberDaoSupport.execute("delete from es_member_address");
        //正确性校验
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("name", "小皮皮");
        map.add("addr", "山西省太原市阳曲县政府");
        map.add("mobile", "13233653048");
        map.add("def_addr", "1");
        map.add("region", "5008");
        String json = mockMvc.perform(post("/members/address")
                .params(map)
                .header("Authorization", token))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        MemberAddress address = JsonUtil.jsonToObject(json, MemberAddress.class);
        addressId = JsonUtil.jsonToObject(json, MemberAddress.class).getAddrId();
        MemberAddress memberAddress = memberAddressManager.getModel(addressId);
        Assert.assertEquals(address, memberAddress);
    }

    /**
     * 会员地址修改测试用例
     *
     * @throws Exception
     */
    @Test
    public void editTest() throws Exception {
        //参数为空校验
        String[] names = new String[]{"name", "addr", "tel", "mobile", "def_addr", "ship_address_name", "region", "message"};
        String[] values1 = new String[]{"", "山西省太原市阳曲县政府", "0351-5594660", "13233653048", "1", "学校", "5008", "收货人姓名不能为空"};
        String[] values2 = new String[]{"小皮皮", "", "0351-5594660", "13233653048", "1", "学校", "5008", "详细地址不能为空"};
        String[] values3 = new String[]{"小皮皮", "山西省太原市阳曲县政府", "0351-5594660", "", "1", "学校", "5008", "手机号码格式不正确"};
        String[] values4 = new String[]{"小皮皮", "山西省太原市阳曲县政府", "0351-5594660", "13233653048", "2", "学校", "5008", "是否为默认地址参数错误"};
        String[] values5 = new String[]{"小皮皮", "山西省太原市阳曲县政府", "0351-5594660", "13233653048", "-1", "学校", "5008", "是否为默认地址参数错误"};
        String[] values6 = new String[]{"小皮皮", "山西省太原市阳曲县政府", "0351-5594660", "13233653048", "", "学校", "5008", "是否为默认地址不能为空"};
        String[] values7 = new String[]{"小皮皮", "山西省太原市阳曲县政府", "0351-5594660", "132336530", "1", "学校", "5008", "手机号码格式不正确"};
        list = toMultiValueMaps(names, values1, values2, values3, values4, values5, values6, values7);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(put("/members/address/" + addressId)
                    .params(params)
                    .header("Authorization", token))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //会员是否存在校验
        ErrorMessage error = new ErrorMessage("003", "当前会员不存在");
        mockMvc.perform(put("/members/address/" + addressId)
                .param("name", "小皮皮")
                .param("addr", "山西省太原市阳曲县政府")
                .param("mobile", "13233653048")
                .param("def_addr", "1")
                .param("region", "5008")
                .header("Authorization", buyer1))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));
        //要修改的地址不存在
        error = new ErrorMessage("002", "无权限操作此地址");
        mockMvc.perform(put("/members/address/999999999")
                .param("name", "小皮皮")
                .param("addr", "山西省太原市阳曲县政府")
                .param("mobile", "13233653048")
                .param("def_addr", "1")
                .param("region", "5008")
                .header("Authorization", token))
                .andExpect(status().is(401))
                .andExpect(objectEquals(error));
        //默认地址修改为非默认地址校验
        this.addTest();
        error = new ErrorMessage("101", "无法更改当前默认地址为非默认地址");
        mockMvc.perform(put("/members/address/" + addressId)
                .param("name", "小皮皮")
                .param("addr", "山西省太原市阳曲县政府")
                .param("mobile", "13233653048")
                .param("def_addr", "0")
                .param("region", "5008")
                .header("Authorization", token))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //修改其他会员地址校验

        this.register("haobeck",StringUtil.md5("123123"),"18234124443");

        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        String json = mockMvc.perform(get("/passport/login")
                .param("username", "haobeck")
                .param("password", StringUtil.md5("123123"))
                .param("captcha", "1111")
                .param("uuid", uuid))
                .andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
        Map tokenMap = JsonUtil.toMap(json);


        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("name", "张三丰");
        map.add("addr", "山西省太原市阳曲县政府");
        map.add("mobile", "13233653048");
        map.add("def_addr", "1");
        map.add("region", "5008");
        json = mockMvc.perform(post("/members/address")
                .params(map)
                .header("Authorization", tokenMap.get("access_token")))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        MemberAddress address = JsonUtil.jsonToObject(json, MemberAddress.class);


        error = new ErrorMessage("002", "无权限操作此地址");
        mockMvc.perform(put("/members/address/" + address.getAddrId())
                .param("name", "小皮皮")
                .param("addr", "山西省太原市阳曲县政府")
                .param("mobile", "13233653048")
                .param("def_addr", "1")
                .param("region", "5008")
                .header("Authorization", token))
                .andExpect(status().is(401))
                .andExpect(objectEquals(error));
        //正确校验
        this.addTest();
        json = mockMvc.perform(put("/members/address/" + addressId)
                .param("name", "小龙龙")
                .param("addr", "山西省太原市阳曲县公园")
                .param("mobile", "13233653048")
                .param("def_addr", "1")
                .param("region", "5008")
                .header("Authorization", token))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        MemberAddress memberaddress = JsonUtil.jsonToObject(json, MemberAddress.class);
        address = memberAddressManager.getModel(addressId);
        Assert.assertEquals(memberaddress, address);
    }

    /**
     * 删除会员测试用例
     *
     * @throws Exception
     */
    @Test
    public void deleteTest() throws Exception {
        //删除不存在的会员地址
        ErrorMessage error = new ErrorMessage("002", "无权限操作此地址");
        mockMvc.perform(delete("/members/address/" + 999999)
                .param("name", "小龙龙")
                .param("addr", "山西省太原市阳曲县公园")
                .param("mobile", "13233653048")
                .param("def_addr", "1")
                .param("region", "5008")
                .header("Authorization", token))
                .andExpect(status().is(401))
                .andExpect(objectEquals(error));
        //添加新用户
        this.register("haobeck",StringUtil.md5("123123"),"18234124445");
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        String json = mockMvc.perform(get("/passport/login")
                .param("username", "haobeck")
                .param("password", StringUtil.md5("123123"))
                .param("captcha", "1111")
                .param("uuid", uuid))
                .andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
        Map tokenMap = JsonUtil.toMap(json);

        //测试删除其他人的会员地址
        error = new ErrorMessage("002", "无权限操作此地址");
        mockMvc.perform(delete("/members/address/" + addressId)
                .param("name", "小龙龙")
                .param("addr", "山西省太原市阳曲县公园")
                .param("mobile", "13233653048")
                .param("def_addr", "1")
                .param("region", "5008")
                .header("Authorization", tokenMap.get("access_token")))
                .andExpect(status().is(401))
                .andExpect(objectEquals(error));
        //正确测试
        this.addTest();
        mockMvc.perform(delete("/members/address/" + addressId)
                .param("name", "小龙龙")
                .param("addr", "山西省太原市阳曲县公园")
                .param("mobile", "13233653048")
                .param("def_addr", "1")
                .param("region", "5008")
                .header("Authorization", token))
                .andExpect(status().is(200));
        MemberAddress memberAddress = memberAddressManager.getModel(addressId);
        Assert.assertNull(memberAddress);

    }

    /**
     * 设置地址为默认
     *
     * @throws Exception
     */
    @Test
    public void editDefaultTest() throws Exception {
        //地址不存在校验
        ErrorMessage error = new ErrorMessage("002", "权限不足");
        mockMvc.perform(put("/members/address/10000000/default")
                .header("Authorization", token))
                .andExpect(status().is(401))
                .andExpect(objectEquals(error));
        //修改其他会员地址校验
        this.addTest();
        error = new ErrorMessage("002", "权限不足");
        mockMvc.perform(put("/members/address/" + addressId + "/default")
                .header("Authorization", buyer1))
                .andExpect(status().is(401))
                .andExpect(objectEquals(error));
        //正确性校验
        mockMvc.perform(put("/members/address/" + addressId + "/default")
                .header("Authorization", token))
                .andExpect(status().is(200));
        MemberAddress memberAddress = this.memberAddressManager.getModel(addressId);
        Assert.assertEquals(memberAddress.getDefAddr().toString(), "1");

    }


}
