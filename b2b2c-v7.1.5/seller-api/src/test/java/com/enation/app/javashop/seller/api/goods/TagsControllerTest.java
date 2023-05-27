package com.enation.app.javashop.seller.api.goods;

import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.model.dos.TagsDO;
import com.enation.app.javashop.core.goods.model.dto.GoodsDTO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v2.0
 * @Description: 商品标签单元测试
 * @date 2018/4/1114:19
 * @since v7.0.0
 */
public class TagsControllerTest extends BaseTest{


    @Qualifier("goodsDaoSupport")
    @Autowired
    private DaoSupport daoSupport;

    TagsDO tag = null;

    @Before
    public void insertTestData() throws Exception {

        //添加一个标签
        tag = new TagsDO();
        tag.setMark("hot");
        //王峰的店铺111
        tag.setSellerId(3);
        tag.setTagName("热卖");
        daoSupport.insert(tag);
        tag.setTagId(this.daoSupport.getLastId(""));

    }

    /**
     * 查询标签列表单元测试
     * @throws Exception
     */
    @Test
    public void testQueryTags() throws Exception {

        //返回状态是200
        mockMvc.perform(get("/seller/goods/tags")
                .header("Authorization", seller1))
                .andExpect(status().is(200));

    }

    /**
     * 查询某标签下的商品单元测试
     * @throws Exception
     */
    @Test
    public void testQueryTagsGoods() throws Exception {

        //不是我店铺的标签
        ErrorMessage error = new ErrorMessage("309", "无权操作");
        mockMvc.perform(get("/seller/goods/tags/-1/goods")
                .header("Authorization", seller1))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //正确查询，状态返回200
        mockMvc.perform(get("/seller/goods/tags/"+tag.getTagId()+"/goods")
                .header("Authorization", seller1))
                .andExpect(status().is(200));

    }

    /**
     * 保存标签商品
     * @throws Exception
     */
    @Test
    public void testSaveTagsGoods() throws Exception {

        //添加一个商品
        GoodsDO goods = this.addGoods();
        //不是我店铺的标签
        ErrorMessage error = new ErrorMessage("309", "无权操作");
        mockMvc.perform(put("/seller/goods/tags/-1/goods/"+goods.getGoodsId())
                .header("Authorization", seller1))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //不是我店铺的商品
        mockMvc.perform(put("/seller/goods/tags/"+tag.getTagId()+"/goods/-1,"+goods.getGoodsId())
                .header("Authorization", seller1))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //正确绑定
        mockMvc.perform(put("/seller/goods/tags/"+tag.getTagId()+"/goods/"+goods.getGoodsId())
                .header("Authorization", seller1))
                .andExpect(status().is(200));

    }

    /**
     * 添加商品
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
                "  \"market_enable\": 0,\n" +
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
        goodsDO.setSellerId(3);
        this.daoSupport.insert(goodsDO);
        goodsDO.setGoodsId(this.daoSupport.getLastId(""));
        return goodsDO;
    }




}
