package com.enation.app.javashop.seller.api.shop;

import com.enation.app.javashop.core.shop.model.dos.ShopCatDO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 店铺分组测试类
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-04-24 11:18:37
 */
@Transactional(value = "memberTransactionManager",rollbackFor=Exception.class)
public class ShopCatControllerTest extends BaseTest{

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport daoSupport;

    List<MultiValueMap<String, String>> list = null ;
    //定义卖家token sellerId：100 shopName:测试店铺
    String token = "";
    int shopCatId1 = 0;
    int shopCatId2 = 0;
    String[] names = new String[] {"shop_cat_pid","shop_cat_name","disable","sort","message"};
    String[] names1 = new String[] {"shop_cat_pid","shop_cat_name","disable","sort"};

    @Before
    public void prepareData(){

        this.daoSupport.execute("delete from es_shop_cat");

        Map map = new HashMap();
        map.put("shop_cat_pid",0);
        map.put("shop_id",100);
        map.put("shop_cat_name","测试分组");
        map.put("disable",1);
        map.put("sort", 1);
        this.daoSupport.insert("es_shop_cat",map);
        shopCatId1 = this.daoSupport.getLastId("es_shop_cat");

        map.put("shop_cat_pid",shopCatId1);
        map.put("shop_cat_name","测试分组2");
        this.daoSupport.insert("es_shop_cat",map);
        shopCatId2 = this.daoSupport.getLastId("es_shop_cat");

        token = this.createSellerToken(100, 100, "woshiceshi", "测试店铺");
    }

    /**
     * 查询分组列表测试
     * @throws Exception
     */
    @Test
    public void testList() throws Exception {

        //正确性校验
        mockMvc.perform(get("/seller/shops/cats")
                .header("Authorization", token))
                .andExpect(jsonPath("$.[0].shop_id").value(100))
                .andExpect(jsonPath("$.[0].shop_cat_id").value(shopCatId1))
                .andExpect(jsonPath("$.[0].shop_cat_pid").value(0));

    }

    /**
     * 添加分组测试
     * @throws Exception
     */
    @Test
    public void testAdd() throws Exception {


        String[] values = new String[] {"0","","1","1","店铺分组名称必填"};
        String[] values1 = new String[] {"0","测试分组1","","1","店铺分组显示状态必填"};
        String[] values2= new String[] {"0","测试分组1","1","","排序必填"};

        List<MultiValueMap<String, String>> list = toMultiValueMaps(names, values, values1, values2) ;

        String[] values3 = new String[] {"0","测试分组1","1","1"};
        String[] values4 = new String[] {"-1","测试分组1","1","1"};
        List<MultiValueMap<String, String>> cats = toMultiValueMaps(names1, values3,values4);
        ErrorMessage error = null;
        //参数性校验
        for (MultiValueMap<String, String> params : list) {
            String message =  params.get("message").get(0);
            error  = new ErrorMessage("004",message);
            mockMvc.perform(post("/seller/shops/cats")
                    .header("Authorization", token)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals( error ));
        }
        error = new ErrorMessage("E223","父分组不存在");
        //无效的父分类校验
        mockMvc.perform(post("/seller/shops/cats")
                .header("Authorization",token)
                .params(cats.get(1)))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));


        //正确性校验
        String json = mockMvc.perform(post("/seller/shops/cats")
                .header("Authorization", token)
                .params(cats.get(0)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        ShopCatDO shopCatDO = JsonUtil.jsonToObject(json, ShopCatDO.class);
        Map shopCat = this.daoSupport.queryForMap("select shop_cat_pid,shop_cat_name,disable,sort from es_shop_cat where shop_cat_id =  ? ",shopCatDO.getShopCatId());
        Map expected = this.formatMap(cats.get(0));
        Map actual  = this.formatMap(shopCat);
        Assert.assertEquals(expected, actual);
    }

    /**
     * 编辑店铺分组
     * @throws Exception
     */
    @Test
    public void testEdit() throws Exception {

        String[] values = new String[] {"0","","1","1","店铺分组名称必填"};
        String[] values1 = new String[] {"0","测试分组1","","1","店铺分组显示状态必填"};
        String[] values2= new String[] {"0","测试分组1","1","","排序必填"};

        List<MultiValueMap<String, String>> list = toMultiValueMaps(names, values, values1, values2) ;

        String[] values3 = new String[] {"0","测试分组1","1","1"};
        String[] values4 = new String[] {"-1","测试分组1","1","1"};
        String[] values5 = new String[] {"10","测试分组1","1","1"};
        List<MultiValueMap<String, String>> cats = toMultiValueMaps(names1, values3,values4,values5);
        ErrorMessage error = null;

        //参数性校验
        for (MultiValueMap<String, String> params : list) {
            String message =  params.get("message").get(0);
            error  = new ErrorMessage("004",message);
            mockMvc.perform(put("/seller/shops/cats/"+shopCatId1)
                    .header("Authorization", token)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals( error ));
        }

        //店铺分组无效性校验
        error  = new ErrorMessage("E218","店铺分组不存在");
        mockMvc.perform(put("/seller/shops/cats/-1")
                .header("Authorization", token)
                .params(cats.get(0)))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));

        //顶级分类修改上级分类校验
        error  = new ErrorMessage("E219","顶级分类不可修改上级分类");
        mockMvc.perform(put("/seller/shops/cats/"+shopCatId1)
                .header("Authorization", token)
                .params(cats.get(2)))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));

        //父分类有效性校验
        error  = new ErrorMessage("E223","父分组不存在");
        mockMvc.perform(put("/seller/shops/cats/"+shopCatId2)
                .header("Authorization", token)
                .params(cats.get(1)))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));

        //正确性校验
        String json = mockMvc.perform(put("/seller/shops/cats/"+shopCatId1)
                .header("Authorization", token)
                .params(cats.get(0)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        ShopCatDO shopCatDO = JsonUtil.jsonToObject(json, ShopCatDO.class);

        Map shopCat = this.daoSupport.queryForMap("select shop_cat_pid,shop_cat_name,disable,sort from es_shop_cat where shop_cat_id =  ? ", shopCatId1);
        Map expected = this.formatMap(cats.get(0));
        Map actual  = this.formatMap(shopCat);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDelete() throws Exception {

        //数据无效性校验
        ErrorMessage error  = new ErrorMessage("E218","店铺分组不存在");
        mockMvc.perform(delete("/seller/shops/cats/-1")
                .header("Authorization", token))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //分组存在子不能删除校验
        error  = new ErrorMessage("E220","当前分组存在子分组，不能删除");
        mockMvc.perform(delete("/seller/shops/cats/"+shopCatId1)
                .header("Authorization", token))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));

        //正确性校验
        mockMvc.perform(delete("/seller/shops/cats/"+shopCatId2)
                .header("Authorization", token))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        int count = this.daoSupport.queryForInt("select count(*) from es_shop_cat where shop_cat_id = ? ", shopCatId2);
        Assert.assertEquals(0, count);
    }
}
