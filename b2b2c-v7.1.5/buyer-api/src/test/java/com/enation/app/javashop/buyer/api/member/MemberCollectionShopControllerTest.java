package com.enation.app.javashop.buyer.api.member;

import com.enation.app.javashop.core.client.member.ShopClient;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.dos.MemberCollectionShop;
import com.enation.app.javashop.core.member.model.vo.MemberVO;
import com.enation.app.javashop.core.member.service.MemberCollectionShopManager;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.core.shop.model.vo.ShopVO;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 会员收藏店铺测试用例
 *
 * @author zh
 * @version v7.0
 * @date 18/5/9 下午8:12
 * @since v7.0
 */
@Transactional(value = "memberTransactionManager", rollbackFor = Exception.class)
public class MemberCollectionShopControllerTest extends BaseTest {

    @Autowired
    private MemberManager memberManager;
    @MockBean
    private ShopClient shopClient;
    @Autowired
    private MemberCollectionShopManager memberCollectionShopManager;

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;

    String token = "";
    /**
     * 收藏店铺id
     */
    Integer collectionShopId = 0;

    @Before
    public void prepareData() throws Exception {

        this.memberDaoSupport.execute("delete from es_member");

        Member member = new Member();
        member.setUname("beckhao");
        member.setMobile("18234124444");
        Map memberMap = this.register("beckhao",StringUtil.md5("123123"),"18234124444");
         token = this.loginForMap("beckhao", StringUtil.md5("123123")).get("access_token").toString();
        member.setHaveShop(1);
        member.setShopId(200);
        member.setMemberId(StringUtil.toInt(memberMap.get("uid").toString()));
        memberManager.edit(member, member.getMemberId());
    }

    /**
     * 添加会员收藏店铺测试
     *
     * @throws Exception
     */
    @Test
    public void addCollectionShopTest() throws Exception {
        //参数为空校验
        ErrorMessage error = new ErrorMessage("004", "店铺id不能为空");
        mockMvc.perform(post("/members/collection/shop")
                .header("Authorization", token)
                .param("shop_id", ""))
                .andExpect(status().is(400))
                .andExpect(objectEquals(error));

        Member member = new Member();
        member.setUname("haobeck");
        member.setMobile("18234124443");
        Map memberMap = this.register("haobeck", StringUtil.md5("123123"),"18234124443");
        member.setHaveShop(1);
        member.setShopId(100);
        member.setMemberId(StringUtil.toInt(memberMap.get("uid").toString()));
        memberManager.edit(member, member.getMemberId());
        ShopVO shopVO = new ShopVO();
        shopVO.setShopName("测试店铺");
        shopVO.setShopId(100);
        when(shopClient.getShop(100)).thenReturn(shopVO);
        //校验店铺无法收藏自己店铺
        error = new ErrorMessage("102", "无法将自己的店铺添加为收藏");
        mockMvc.perform(post("/members/collection/shop")
                .header("Authorization", this.loginForMap("haobeck", StringUtil.md5("123123")).get("access_token"))
                .param("shop_id", "100"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //要收藏的店铺不存在校验
        error = new ErrorMessage("003", "当前店铺不存在");
        mockMvc.perform(post("/members/collection/shop")
                .header("Authorization", token)
                .param("shop_id", "322"))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));
        //校验要收藏的会员是否存在
        shopVO.setShopName("测试店铺");
        shopVO.setShopId(100);
        when(shopClient.getShop(101)).thenReturn(shopVO);
        error = new ErrorMessage("003", "当前会员不存在");
        mockMvc.perform(post("/members/collection/shop")
                .header("Authorization", seller2)
                .param("shop_id", "100"))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));
        //正确校验
        String json = mockMvc.perform(post("/members/collection/shop")
                .header("Authorization", token)
                .param("shop_id", "100"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        //重复添加同一个店铺为收藏校验
        error = new ErrorMessage("103", "此店铺已经添加为收藏");
        mockMvc.perform(post("/members/collection/shop")
                .header("Authorization", token)
                .param("shop_id", "100"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        MemberCollectionShop memberCollectionShop = JsonUtil.jsonToObject(json, MemberCollectionShop.class);
        collectionShopId = memberCollectionShop.getShopId();

    }

    /**
     * 删除店铺收藏测试
     *
     * @throws Exception
     */
    @Test
    public void deleteTest() throws Exception {
        //删除不存在的收藏
        ErrorMessage error = new ErrorMessage("002", "无权限操作此收藏");
        mockMvc.perform(delete("/members/collection/shop/1111111")
                .header("Authorization", token))
                .andExpect(status().is(401))
                .andExpect(objectEquals(error));
        //删除其他会员的收藏测试
        this.addCollectionShopTest();
        error = new ErrorMessage("002", "无权限操作此收藏");
        mockMvc.perform(delete("/members/collection/shop/" + collectionShopId)
                .header("Authorization", buyer2))
                .andExpect(status().is(401))
                .andExpect(objectEquals(error));
        //正确测试
        mockMvc.perform(delete("/members/collection/shop/" + collectionShopId)
                .header("Authorization", token))
                .andExpect(status().is(200));
        MemberCollectionShop memberCollectionShop = memberCollectionShopManager.getModel(collectionShopId);
        Assert.assertNull(memberCollectionShop);
    }

}
