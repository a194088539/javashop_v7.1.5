package com.enation.app.javashop.buyer.api.member;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.dto.MemberEditDTO;
import com.enation.app.javashop.core.member.model.vo.MemberVO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 会员api测试类
 *
 * @author zh
 * @version v7.0
 * @date 18/4/24 下午3:24
 * @since v7.0
 */
@Transactional(value = "memberTransactionManager", rollbackFor = Exception.class)
public class MemberControllerTest extends BaseTest {
    @Autowired
    private MemberManager memberManager;
    @Autowired
    private PassportManager passportManager;
    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;


    String token = "";

    Member member = null;

    List<MultiValueMap<String, String>> list = null;

    @Autowired
    private Cache cache;

    @Before
    public void dataPreparation() throws Exception {
        this.memberDaoSupport.execute("delete from es_member where email = '310487699@qq.com'");
        this.memberDaoSupport.execute("delete from es_member where mobile = '18234124444'");

        this.register("haobeckhaobeckhao", StringUtil.md5("123123"), "18234124444");
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        String json = mockMvc.perform(get("/passport/login")
                .param("username", "haobeckhaobeckhao")
                .param("password", StringUtil.md5("123123"))
                .param("captcha", "1111")
                .param("uuid", uuid))
                .andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
        Map tokenMap = JsonUtil.toMap(json);

        token = tokenMap.get("access_token").toString();


        this.register("haobeck", StringUtil.md5("123123"), "18234124333");
    }


    /**
     * 完善会员信息测试
     *
     * @throws Exception
     */
    @Test
    public void perfectInfo() throws Exception {
        //参数为空非法校验
        String[] names = new String[]{"nickname", "sex", "birthday", "region", "address", "email", "tel", "face", "message"};
        String[] values1 = new String[]{"", "1", "19940303", "5008", "山西省太原市阳曲县", "javashop@qq.com", "03515594666", "http://image.jpg", "会员昵称必须为2到20位之间"};
        String[] values2 = new String[]{"haobeck", "", "19940303", "5008", "山西省太原市阳曲县", "javashop@qq.com", "03515594666", "http://image.jpg", "会员性别不能为空"};
        String[] values3 = new String[]{"haobeck", "-1", "19940303", "5008", "山西省太原市阳曲县", "javashop@qq.com", "03515594666", "http://image.jpg", "必须为数字且1为男,0为女"};
        String[] values4 = new String[]{"haobeck", "3", "19940303", "5008", "山西省太原市阳曲县", "javashop@qq.com", "03515594666", "http://image.jpg", "必须为数字且1为男,0为女"};
        String[] values8 = new String[]{"haobeck", "1", "19940303", "5008", "山西省太原市阳曲县", "123", "03515594666", "http://image.jpg", "邮箱格式不正确"};
        list = toMultiValueMaps(names, values1, values2, values3, values4, values8);
        for (MultiValueMap<String, String> params : list) {

            String message = params.get("message").get(0);

            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(put("/members")
                    .params(params)
                    .header("Authorization", token)
                    .header("uuid", uuid))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }

        //会员是否存在校验
        ErrorMessage error = new ErrorMessage("003", "此会员不存在");
        mockMvc.perform(put("/members")
                .param("nickname", "haobeck")
                .param("sex", "1")
                .param("birthday", "19940303")
                .param("region", "5008")
                .param("address", "山西省太原市阳曲县")
                .param("email", "310487699@qq.com")
                .header("Authorization", seller1).header("uuid", uuid))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));
        //正确性校验
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        String json = mockMvc.perform(get("/passport/login")
                .param("username", "haobeck")
                .param("password", StringUtil.md5("123123"))
                .param("captcha", "1111")
                .param("uuid", uuid).header("uuid",uuid))
                .andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
        Map tokenMap = JsonUtil.toMap(json);


        MemberEditDTO memberEditDTO = new MemberEditDTO();
        memberEditDTO.setNickname("haobeck");
        memberEditDTO.setSex(1);
        memberEditDTO.setBirthday(19940303l);
        memberEditDTO.setEmail("11111111@qq.com");
        memberEditDTO.setAddress("山西省太原市阳曲县");
        memberEditDTO.setFace("");
        memberEditDTO.setTel("03515594660");
        MultiValueMap<String, String> map = objectToMap(memberEditDTO);
        map.remove("region");
        map.add("region", "5008");
        mockMvc.perform(put("/members")
                .params(map)
                .header("Authorization", tokenMap.get("access_token")).header("uuid", uuid))
                .andExpect(status().is(200));
        //从数据库查询出map
        String sql = "select nickname,sex,birthday,province_id,city_id,county_id,town_id,province,city,county,town,email,address,face,tel from es_member where member_id = ?";
        Map memberMap = this.memberDaoSupport.queryForMap(sql, tokenMap.get("uid").toString());
        //修改后的map
        Map editMap = new HashMap();
        editMap.put("province_id", 5);
        editMap.put("city_id", 142);
        editMap.put("county_id", 143);
        editMap.put("town_id", 5008);
        editMap.put("province", "河北");
        editMap.put("city", "石家庄市");
        editMap.put("county", "辛集市");
        editMap.put("town", "辛集镇");
        editMap.put("tel", "03515594660");
        editMap.put("nickname", "haobeck");
        editMap.put("sex", 1);
        editMap.put("birthday", 19940303l);
        editMap.put("email", "11111111@qq.com");
        editMap.put("address", "山西省太原市阳曲县");
        editMap.put("face", "");
        Assert.assertEquals(editMap, memberMap);

    }
}
