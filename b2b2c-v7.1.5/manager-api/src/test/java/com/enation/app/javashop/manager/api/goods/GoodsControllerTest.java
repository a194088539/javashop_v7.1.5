package com.enation.app.javashop.manager.api.goods;

import com.enation.app.javashop.core.client.member.ShopClient;
import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.model.dto.GoodsDTO;
import com.enation.app.javashop.core.shop.model.vo.ShopVO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v2.0
 * @Description: 商品单元测试
 * @date 2018/4/1110:49
 * @since v7.0.0
 */
public class GoodsControllerTest extends BaseTest{

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport daoSupport;

    List<MultiValueMap<String, String>> list = null;

    @MockBean
    private ShopClient shopClient;

    @Before
    public void insertTestData(){

        String[] names = new String[]{"pass","message","assert"};
        String[] values1 = new String[]{"","","审核状态不能为空"};
        String[] values2 = new String[]{"3","","审核状态值不正确"};
        list = toMultiValueMaps(names,values2,values1);
    }

    @Test
    public void testAuth() throws Exception{

        //添加待审核一个商品
        GoodsDO goods = this.addGoods();
        // 必填验证
        for (MultiValueMap<String,String> params  : list){
            String message =  params.get("assert").get(0);
            ErrorMessage error  = new ErrorMessage("004",message);
            mockMvc.perform(put("/admin/goods/"+goods.getGoodsId()+"/auth")
                    .header("Authorization",superAdmin)
                    .params( params ))
                    .andExpect(status().is(400))
                    .andExpect(  objectEquals( error ));
        }

        //拒绝时原因必填
        ErrorMessage error  = new ErrorMessage("301","拒绝原因不能为空");
        mockMvc.perform(put("/admin/goods/"+goods.getGoodsId()+"/auth")
                .header("Authorization",superAdmin)
                .param("pass","0"))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));

        //不存在的商品
        error  = new ErrorMessage("301","无权操作");
        mockMvc.perform(put("/admin/goods/-1/auth")
                .header("Authorization",superAdmin)
                .param("pass","1"))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));

        //商品正确审核
        mockMvc.perform(put("/admin/goods/"+goods.getGoodsId()+"/auth")
                .header("Authorization",superAdmin)
                .param("pass","1"))
                .andExpect(status().is(200));
        //重复审核
        error  = new ErrorMessage("301","商品已审核，请勿重复审核");
        mockMvc.perform(put("/admin/goods/"+goods.getGoodsId()+"/auth")
                .header("Authorization",superAdmin)
                .param("pass","1"))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));
    }

    /**
     * 下架单元测试
     * @throws Exception
     */
    @Test
    public void testUnder() throws Exception{

        //添加待审核一个商品
        GoodsDO goods = this.addGoods();
        //商品正确审核
        mockMvc.perform(put("/admin/goods/"+goods.getGoodsId()+"/auth")
                .header("Authorization",superAdmin)
                .param("pass","1"))
                .andExpect(status().is(200));
        //商品下架
        ErrorMessage error  = new ErrorMessage("004","下架原因不能为空");
        mockMvc.perform(put("/admin/goods/"+goods.getGoodsId()+"/under")
                .header("Authorization",superAdmin)
                .param("reason",""))
                .andExpect(status().is(400))
                .andExpect(  objectEquals( error ));
        //正确下架
        mockMvc.perform(put("/admin/goods/"+goods.getGoodsId()+"/under")
                .header("Authorization",superAdmin)
                .param("reason","非法买卖"))
                .andExpect(status().is(200));
        //不是可以下架的状态
        error  = new ErrorMessage("301","存在不能下架的商品，不能操作");
        mockMvc.perform(put("/admin/goods/"+goods.getGoodsId()+"/under")
                .header("Authorization",superAdmin)
                .param("reason","非法买卖"))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));
    }
    /**
     * 上架单元测试
     * @throws Exception
     */
    @Test
    public void testMarket() throws Exception{

        ShopVO shop = new ShopVO();
        shop.setShopId(1);
        shop.setShopDisable("OPEN");
        Mockito.when(shopClient.getShop(1)).thenReturn(shop);

        //添加待审核一个商品
        GoodsDO goods = this.addGoods();
        //商品正确审核
        mockMvc.perform(put("/admin/goods/"+goods.getGoodsId()+"/auth")
                .header("Authorization",superAdmin)
                .param("pass","1"))
                .andExpect(status().is(200));
        //不能上架的状态
        ErrorMessage error  = new ErrorMessage("301","商品不能上架操作");
        mockMvc.perform(put("/admin/goods/"+goods.getGoodsId()+"/up")
                .header("Authorization",superAdmin))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));
        //正确下架
        mockMvc.perform(put("/admin/goods/"+goods.getGoodsId()+"/under")
                .header("Authorization",superAdmin)
                .param("reason","非法买卖"))
                .andExpect(status().is(200));
        //正确上架
        mockMvc.perform(put("/admin/goods/"+goods.getGoodsId()+"/up")
                .header("Authorization",superAdmin))
                .andExpect(status().is(200));
    }

    /**
     * 正确查询商品列表
     * @throws Exception
     */
    @Test
    public void testQuery() throws Exception{

        //状态200
        mockMvc.perform(get("/admin/goods")
                .header("Authorization",superAdmin))
                .andExpect(status().is(200));
    }

    /**
     * 添加一个商品
     * @return
     */
    private GoodsDO addGoods(){

        //需要先添加商品
        String goodsJson = "{\n" +
                "  \"brand_id\": 1,\n" +
                "  \"category_id\": 0,\n" +
                "  \"cost\": 0,\n" +
                "  \"exchange\": {\n" +
                "    \"category_id\": 0,\n" +
                "    \"enable_exchange\": 0,\n" +
                "    \"exchange_money\": 0,\n" +
                "    \"exchange_point\": 0\n" +
                "  },\n" +
                "  \"goods_gallery_list\": [\n" +
                "    {\n" +
                "      \"img_id\": 0,\n" +
                "      \"original\": \"string\",\n" +
                "      \"sort\": 0\n" +
                "    }\n" +
                "  ],\n" +
                "  \"goods_name\": \"string\",\n" +
                "  \"goods_params_list\": [\n" +
                "    {\n" +
                "      \"param_id\": 0,\n" +
                "      \"param_name\": \"string\",\n" +
                "      \"param_value\": \"string\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"goods_transfee_charge\": 0,\n" +
                "  \"has_changed\": 0,\n" +
                "  \"intro\": \"string\",\n" +
                "  \"market_enable\": 1,\n" +
                "  \"meta_description\": \"string\",\n" +
                "  \"meta_keywords\": \"string\",\n" +
                "  \"mktprice\": 0,\n" +
                "  \"page_title\": \"string\",\n" +
                "  \"price\": 0,\n" +
                "  \"quantity\": 0,\n" +
                "  \"shop_cat_id\": 0,\n" +
                "  \"sku_list\": [\n" +
                "    {\n" +
                "      \"cost\": 0,\n" +
                "      \"price\": 0,\n" +
                "      \"quantity\": 0,\n" +
                "      \"sn\": \"string\",\n" +
                "      \"spec_list\": [\n" +
                "        {\n" +
                "          \"spec_id\": 0,\n" +
                "          \"spec_image\": \"string\",\n" +
                "          \"spec_type\": 0,\n" +
                "          \"spec_value\": \"string\",\n" +
                "          \"spec_value_id\": 0\n" +
                "        }\n" +
                "      ],\n" +
                "      \"weight\": 0\n" +
                "    }\n" +
                "  ],\n" +
                "  \"sn\": \"string\",\n" +
                "  \"template_id\": 0,\n" +
                "  \"weight\": 0\n" +
                "}";
        GoodsDTO goods = JsonUtil.jsonToObject(goodsJson, GoodsDTO.class);
        GoodsDO goodsDO = new GoodsDO();
        BeanUtils.copyProperties(goods,goodsDO);
        goodsDO.setDisabled(1);
        goodsDO.setViewCount(0);
        goodsDO.setSellerId(1);
        //待审核
        goodsDO.setIsAuth(0);
        this.daoSupport.insert(goodsDO);
        goodsDO.setGoodsId(this.daoSupport.getLastId(""));
        return  goodsDO;
    }


}
