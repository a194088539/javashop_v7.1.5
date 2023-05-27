package com.enation.app.javashop.buyer.api.goods;

import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.model.dto.GoodsDTO;
import com.enation.app.javashop.core.member.model.vo.SalesVO;
import com.enation.app.javashop.core.trade.order.model.enums.PayStatusEnum;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v2.0
 * @Description: 商品测试用例
 * @date 2018/4/119:13
 * @since v7.0.0
 */
public class GoodsControllerTest extends BaseTest {

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport daoSupport;


    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport tradeDaoSupport;

    @Before
    public void insertTestData() {

    }

    @Test
    public void testGoods() throws Exception {

        //添加一个商品
        GoodsDO goods = this.addGoods();
        //查询商品返回200
        mockMvc.perform(get("/goods/" + goods.getGoodsId()))
                .andExpect(status().isOk());
        //查询商品sku返回200
        mockMvc.perform(get("/goods/" + goods.getGoodsId() + "/skus"))
                .andExpect(status().isOk());
        Map map = this.daoSupport.queryForMap("select view_count from es_goods where goods_id = ?", goods.getGoodsId());

        Assert.assertEquals(map.get("view_count"), 0);

    }



    /**
     * 添加一个商品
     *
     * @return
     */
    private GoodsDO addGoods() {

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
        BeanUtils.copyProperties(goods, goodsDO);
        goodsDO.setViewCount(0);
        this.daoSupport.insert(goodsDO);
        goodsDO.setGoodsId(this.daoSupport.getLastId(""));
        return goodsDO;
    }

}
