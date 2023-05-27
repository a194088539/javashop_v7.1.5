package com.enation.app.javashop.buyer.api.member;

import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.member.ShopClient;
import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.goods.service.GoodsQueryManager;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.dos.MemberCollectionGoods;
import com.enation.app.javashop.core.member.model.vo.MemberVO;
import com.enation.app.javashop.core.member.service.MemberCollectionGoodsManager;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.core.shop.model.vo.ShopVO;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.when;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 会员商品收藏测试
 *
 * @author zh
 * @version 7.0
 * @since 7.0
 */
@Transactional(value = "memberTransactionManager", rollbackFor = Exception.class)
public class MemberCollectionGoodsControllerTest extends BaseTest {

    @Autowired
    private MemberManager memberManager;
    @MockBean
    private GoodsClient goodsClient;

    @MockBean
    private ShopClient shopClient;
    @Autowired
    private MemberCollectionGoodsManager memberCollectionGoodsManager;

    String token = "";

    Integer collectionGoodsId = 0;

    @Before
    public void prepareData() throws Exception {
        this.register("haobeckhaobeckhao", StringUtil.md5("123123"),"18234124444");
        token = this.loginForMap("haobeckhaobeckhao", StringUtil.md5("123123")).get("access_token").toString();
    }

    @Test
    public void addCollectionGoodsTest() throws Exception {
        //参数为空校验
        ErrorMessage error = new ErrorMessage("004", "商品id不能为空");
        mockMvc.perform(post("/members/collection/goods")
                .header("Authorization", token)
                .header("uuid", uuid)
                .param("goods_id", ""))
                .andExpect(status().is(400))
                .andExpect(objectEquals(error));

        Member member = new Member();
        member.setUname("haobeck111");
        member.setMobile("18234124443");
        Map memberMap=this.register("haobeck111",StringUtil.md5("123123"),"18234124443");
        member.setHaveShop(1);
        member.setShopId(4);
        member.setMemberId(StringUtil.toInt(memberMap.get("uid").toString()));
        memberManager.edit(member, member.getMemberId());
        CacheGoods goods = new CacheGoods();
        goods.setGoodsName("测试商品");
        goods.setSellerId(4);
        when(goodsClient.getFromCache(2)).thenReturn(goods);

        ShopVO shopVO = new ShopVO();
        shopVO.setShopName("测试店铺");
        shopVO.setShopId(4);
        when(shopClient.getShop(4)).thenReturn(shopVO);
        Map tokenMap = this.loginForMap("haobeck111", StringUtil.md5("123123"));
        //校验店铺无法收藏自己店铺的商品
        error = new ErrorMessage("104", "无法收藏自己店铺的商品");
        mockMvc.perform(post("/members/collection/goods")
                .header("Authorization", tokenMap.get("access_token"))
                .header("uuid", uuid)
                .param("goods_id", "2"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //商品不存在校验
        goods = new CacheGoods();
        goods.setGoodsName("测试商品");
        goods.setSellerId(3);
        when(goodsClient.getFromCache(1)).thenReturn(goods);
        error = new ErrorMessage("003", "此商品不存在");
        mockMvc.perform(post("/members/collection/goods")
                .header("Authorization", token)
                .header("uuid", uuid)
                .param("goods_id", "3"))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));
        //校验要收藏的会员是否存在
        error = new ErrorMessage("003", "当前会员不存在");
        mockMvc.perform(post("/members/collection/goods")
                .header("Authorization", seller2)
                .header("uuid", uuid)
                .param("goods_id", "1"))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));
        //正确校验
        String json = mockMvc.perform(post("/members/collection/goods")
                .header("Authorization", token)
                .header("uuid", uuid)
                .param("goods_id", "1"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        //重复添加同一个商品校验
        error = new ErrorMessage("105", "当前商品已经添加为收藏");
        mockMvc.perform(post("/members/collection/goods")
                .header("Authorization", token)
                .header("uuid", uuid)
                .param("goods_id", "1"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        MemberCollectionGoods memberCollectionGoods = JsonUtil.jsonToObject(json, MemberCollectionGoods.class);
        collectionGoodsId = memberCollectionGoods.getGoodsId();
    }

    /**
     * 删除商品收藏测试
     *
     * @throws Exception
     */
    @Test
    public void deleteTest() throws Exception {
        //删除不存在的收藏
        ErrorMessage error = new ErrorMessage("002", "无权限操作此收藏");
        mockMvc.perform(delete("/members/collection/goods/111111")
                .header("Authorization", token).header("uuid", uuid))
                .andExpect(status().is(401))
                .andExpect(objectEquals(error));
        //删除其他会员的收藏测试
        this.addCollectionGoodsTest();
        error = new ErrorMessage("002", "无权限操作此收藏");
        mockMvc.perform(delete("/members/collection/goods/" + collectionGoodsId)
                .header("Authorization", buyer2).header("uuid", uuid))
                .andExpect(status().is(401))
                .andExpect(objectEquals(error));
        //正确测试
        mockMvc.perform(delete("/members/collection/goods/" + collectionGoodsId)
                .header("Authorization", token).header("uuid", uuid))
                .andExpect(status().is(200));
        MemberCollectionGoods memberCollectionGoods = memberCollectionGoodsManager.getModel(collectionGoodsId);
        Assert.assertNull(memberCollectionGoods);
    }


}
