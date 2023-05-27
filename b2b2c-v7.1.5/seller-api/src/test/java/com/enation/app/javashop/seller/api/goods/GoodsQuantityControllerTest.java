package com.enation.app.javashop.seller.api.goods;

import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.model.vo.GoodsSkuVO;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import net.sf.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v2.0
 * @Description: 商品库存维护单元测试
 * @date 2018/4/109:36
 * @since v7.0.0
 */
public class GoodsQuantityControllerTest extends BaseTest {

    List<MultiValueMap<String, String>> list = null;

    GoodsDO goods = null;
    List<GoodsSkuVO> skuList = null;

    @Before
    public void insertTestData() throws Exception {

        // 添加一个带有规格的商品
        goods = this.addGoods();
        String skuJson = mockMvc.perform(get("/seller/goods/" + goods.getGoodsId() + "/skus")
                .header("Authorization", seller1))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        skuList = JsonUtil.jsonToList(skuJson, GoodsSkuVO.class);

        String[] names = new String[]{"context", "message"};
        String[] values1 = new String[]{"[{\"quantity_count\":0,\"sku_id\":" + skuList.get(0).getSkuId() + "}]", "没有操作权限"};
        String[] values2 = new String[]{"[{\"quantity_count\":-1,\"sku_id\":" + skuList.get(0).getSkuId() + "}]", "sku总库存不能为空或负数"};
        String[] values3 = new String[]{"[{\"sku_id\":" + skuList.get(0).getSkuId() + "}]", "sku总库存不能为空或负数"};
        String[] values4 = new String[]{"[{\"quantity_count\":2,\"sku_id\":-1}]", "商品sku不存在"};

        list = toMultiValueMaps(names, values1, values2, values3,values4);
    }

    @Test
    public void testUpdateQuantity() throws Exception {

        //修改不是我的商品
        int i = 0;
        for (MultiValueMap<String, String> params : list) {

            String content = params.get("context").get(0);
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("307", message);
            String authorization = i == 0 ? seller2 : seller1;
            mockMvc.perform(put("/seller/goods/" + goods.getGoodsId() + "/quantity")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .header("Authorization", authorization))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
            i++;
        }
        //正确修改
        mockMvc.perform(put("/seller/goods/" + goods.getGoodsId() + "/quantity")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{\"quantity_count\":10,\"sku_id\":" + skuList.get(0).getSkuId() + "}]")
                .header("Authorization", seller1))
                .andExpect(status().is(200));


    }

    /**
     * 添加一个带规格的商品
     *
     * @return
     * @throws Exception
     */
    private GoodsDO addGoods() throws Exception {

        String[] names = new String[]{"category_id", "category_name", "shop_cat_id", "brand_id", "goods_name", "sn", "price", "cost", "mktprice", "weight", "goods_transfee_charge", "market_enable", "template_id"};
        String[] values = new String[]{"1", "1", "1","1", "商品名称", "sn", "2.0", "3.0", "3.0", "500", "1", "1", "1"};
        Map map = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            map.put(names[i], values[i]);
        }
        map.put("goods_gallery_list", JSONArray.fromObject("[{\"original\":\"a.jpg\"}]"));

        String skuJson = "[\n" +
                "    {\n" +
                "      \"cost\": 20,\n" +
                "      \"price\": 10,\n" +
                "      \"quantity\": 10,\n" +
                "      \"sn\": \"\",\n" +
                "      \"spec_list\": [\n" +
                "        {\n" +
                "          \"spec_id\": 0,\n" +
                "          \"spec_image\": \"\",\n" +
                "          \"spec_type\": 0,\n" +
                "          \"spec_value\": \"string\",\n" +
                "          \"spec_value_id\": 0\n" +
                "        }\n" +
                "      ],\n" +
                "      \"weight\": 0\n" +
                "    }\n" +
                "  ]";
        map.put("sku_list", JSONArray.fromObject(skuJson));
        String json = mockMvc.perform(post("/seller/goods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(map))
                .header("Authorization", seller1))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        GoodsDO goods = JsonUtil.jsonToObject(json, GoodsDO.class);
        return goods;
    }


}
