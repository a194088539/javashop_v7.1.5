package com.enation.app.javashop.buyer.api.member;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.member.model.dos.ConnectDO;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.enums.ConnectTypeEnum;
import com.enation.app.javashop.core.member.model.vo.Auth2Token;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author zjp
 * @version v7.0
 * @Description
 * @ClassName MemberConnectControllerTest
 * @since v7.0 下午4:43 2018/6/26
 */
@Transactional(value = "memberTransactionManager", rollbackFor = Exception.class)
public class MemberConnectControllerTest extends BaseTest {
    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;


    @Autowired
    private Cache cache;

    private String uuid = "52c260";


    String token = "";
    String token1 = "";
    Integer getMemberId = 0;

    @Before
    public void insertTestData() throws Exception {
        this.memberDaoSupport.execute("delete from es_member");
        this.memberDaoSupport.execute("delete from es_connect");

        Map memberMap = this.register("test", StringUtil.md5("111111"), "18234124444");

        token = this.createBuyerToken(StringUtil.toInt(memberMap.get("uid").toString()), "ceshi");

        Map map = new HashMap(4);
        map.put("member_id", memberMap.get("uid"));
        map.put("union_type", ConnectTypeEnum.QQ.name());
        map.put("union_id", "123456789");
        map.put("unbound_time", DateUtil.getDateline());
        this.memberDaoSupport.insert("es_connect", map);
        memberMap = this.register("test1", StringUtil.md5("111111"), "18234123333");

        getMemberId = StringUtil.toInt(memberMap.get("uid").toString());
        token1 = this.createBuyerToken(StringUtil.toInt(memberMap.get("uid").toString()), "ceshi");

        map.put("member_id", StringUtil.toInt(memberMap.get("uid").toString()));
        map.put("union_id", "987654321");
        map.remove("unbound_time");
        this.memberDaoSupport.insert("es_connect", map);

        Auth2Token auth2Token = new Auth2Token();
        auth2Token.setUnionid("987654321");
        auth2Token.setType(ConnectTypeEnum.QQ.value());
        cache.put(CachePrefix.CONNECT_LOGIN.getPrefix() + uuid, auth2Token, 300);

    }

    /**
     * 解绑测试
     *
     * @throws Exception
     */
    @Test
    public void unbindTest() throws Exception {
        //无效性校验
        ErrorMessage error = new ErrorMessage("E134", "会员未绑定相关账号");
        mockMvc.perform(post("/account-binder/pc/" + ConnectTypeEnum.QQ.value())
                .header("Authorization", buyer1))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //30不可重复解绑校验
        error = new ErrorMessage("E135", "30天内不可重复解绑");
        mockMvc.perform(post("/account-binder/pc/" + ConnectTypeEnum.QQ.value())
                .header("Authorization", token))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //正确性校验
        mockMvc.perform(post("/account-binder/pc/" + ConnectTypeEnum.QQ.value())
                .header("Authorization", token1))
                .andExpect(status().is(200));
        String sql = "select * from es_connect where member_id = ? and union_type = ? ";
        ConnectDO connectDO = this.memberDaoSupport.queryForObject(sql, ConnectDO.class, getMemberId, ConnectTypeEnum.QQ.value());
        Assert.assertEquals("", connectDO.getUnionId());
    }

    //登录绑定openid测试
    @Test
    public void openidBindTest() throws Exception {
        //redis存储信息失效校验
        ErrorMessage error = new ErrorMessage("E133", "授权超时，请重新授权");
        mockMvc.perform(post("/account-binder/login/1234")
                .header("Authorization", token1))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //正确性校验
        String contentAsString = mockMvc.perform(post("/account-binder/login/" + uuid)
                .header("Authorization", token1))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        LinkedHashMap<String, Object> map = JsonUtil.toMap(contentAsString);
        Assert.assertEquals("bind_success", map.get("result"));
        String sql = "select * from es_connect where member_id = ? and union_type = ?";
        ConnectDO connectDO = this.memberDaoSupport.queryForObject(sql, ConnectDO.class, getMemberId, ConnectTypeEnum.QQ.name());
        Assert.assertEquals("987654321", connectDO.getUnionId());
    }
}
