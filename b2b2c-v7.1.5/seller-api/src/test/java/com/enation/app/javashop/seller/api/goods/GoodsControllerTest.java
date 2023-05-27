package com.enation.app.javashop.seller.api.goods;

import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.client.system.SettingClient;
import com.enation.app.javashop.core.goods.model.dos.*;
import com.enation.app.javashop.core.goods.model.dto.GoodsSettingVO;
import com.enation.app.javashop.core.goods.service.CategoryManager;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import net.sf.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v2.0
 * @Description: 商品单元测试
 * @date 2018/4/89:45
 * @since v7.0.0
 */
public class GoodsControllerTest extends BaseTest {

    List<MultiValueMap<String, String>> list = null;

    List<GoodsGalleryDO> listGallery = null;

    /**
     * 规格1
     */
    SpecValuesDO specValue1 = null;

    /**
     * 规格2
     */
    SpecValuesDO specValue2 = null;

    @Autowired
    private CategoryManager categoryManager;
    @MockBean
    private SettingClient settingClient;

    @Before
    public void insertTestData() throws Exception {

        //添加商品之前需要添加设置信息
        GoodsSettingVO settingGoods = new GoodsSettingVO();
        settingGoods.setMarcketAuth(1);
        settingGoods.setUpdateAuth(1);
        settingGoods.setBigHeight(100);
        settingGoods.setBigWidth(100);
        settingGoods.setSmallHeight(100);
        settingGoods.setSmallWidth(100);
        settingGoods.setThumbnailHeight(100);
        settingGoods.setThumbnailWidth(100);

        Mockito.when(settingClient.get(SettingGroup.GOODS)).thenReturn(JsonUtil.objectToJson(settingGoods));

        listGallery = new ArrayList<>();
        GoodsGalleryDO gallery = new GoodsGalleryDO();
        gallery.setImgId(-1);
        gallery.setOriginal("a.jpg");
        listGallery.add(gallery);

        String[] names = new String[]{"category_id", "shop_cat_id", "brand_id", "goods_name", "sn", "price", "cost", "mktprice", "weight", "goods_transfee_charge", "market_enable", "template_id", "message"};
        String[] values1 = new String[]{"", "1", "1", "goods_name", "sn", "2.0", "2.0", "2.0", "2", "1", "1", "1", "商城分类不能为空"};
        String[] values2 = new String[]{"1", "", "1", "goods_name", "sn", "2.0", "2.0", "2.0", "2", "1", "1", "1", "店铺分类不能为空"};
        String[] values3 = new String[]{"1", "1", "1", "", "sn", "2.0", "2.0", "2.0", "2", "1", "1", "1", "商品名称不能为空"};
        String[] values4 = new String[]{"1", "1", "1", "商品名称", "sn", "", "2.0", "2.0", "2", "1", "1", "1", "商品价格不能为空"};
        String[] values5 = new String[]{"1", "1", "1", "商品名称", "sn", "2.0", "", "2.0", "2", "1", "1", "1", "成本价格不能为空"};
        String[] values6 = new String[]{"1", "1", "1", "商品名称", "sn", "2.0", "3.0", "", "2", "1", "1", "1", "市场价格不能为空"};
        String[] values7 = new String[]{"1", "1", "1", "商品名称", "sn", "2.0", "3.0", "3.0", "", "1", "1", "1", "商品重量不能为空"};
        String[] values8 = new String[]{"1", "1", "1", "商品名称", "sn", "2.0", "3.0", "3.0", "500", "", "1", "1", "承担运费不能为空"};
        String[] values9 = new String[]{"1", "1", "1", "商品名称", "sn", "2.0", "3.0", "3.0", "500", "1", "1", "1", "商品相册图片不能为空"};
        String[] values10 = new String[]{"1", "1", "1", "商品名称", "sn", "2.0", "3.0", "3.0", "500", "1", "1", "", "运费模板不能为空，没有运费模板时，传值0"};
        //值不正确
        String[] values11 = new String[]{"-1", "1", "1", "商品名称", "sn", "2.0", "3.0", "3.0", "500", "1", "1", "1", "商城分类值不正确"};
        String[] values12 = new String[]{"1", "-1", "1", "商品名称", "sn", "2.0", "3.0", "3.0", "500", "1", "1", "1", "店铺分类值不正确"};
        String[] values13 = new String[]{"1", "1", "-1", "商品名称", "sn", "2.0", "3.0", "3.0", "500", "1", "1", "1", "品牌值不正确"};
        String[] values17 = new String[]{"1", "1", "1", "商品名称", "sn", "-2.0", "3.0", "3.0", "500", "1", "1", "1", "商品价格不能为负数"};
        String[] values14 = new String[]{"1", "1", "1", "商品名称", "sn", "2.0", "-3.0", "3.0", "500", "1", "1", "1", "成本价格不能为负数"};
        String[] values15 = new String[]{"1", "1", "1", "商品名称", "sn", "2.0", "3.0", "-3.0", "500", "1", "1", "1", "市场价格不能为负数"};
        String[] values16 = new String[]{"1", "1", "1", "商品名称", "sn", "2.0", "3.0", "3.0", "-500", "1", "1", "1", "重量不能为负数"};
        String[] values18 = new String[]{"1", "1", "1", "商品名称", "sn", "2.0", "3.0", "3.0", "500", "-1", "1", "1", "承担运费值不正确"};
        String[] values19 = new String[]{"1", "1", "1", "商品名称", "sn", "2.0", "3.0", "3.0", "500", "1", "-1", "1", "是否上架值不正确"};
        String[] values20 = new String[]{"1", "1", "1", "商品名称", "sn", "2.0", "3.0", "3.0", "500", "1", "1", "-1", "运费模板值不正确"};
        list = toMultiValueMaps(names, values1, values2, values3, values4, values5, values6, values7, values8,
                values9, values10, values11, values12, values13, values14, values15, values16, values17, values18, values19, values20);

        //自定义规格的使用
        CategoryDO category = new CategoryDO();
        category.setName("测试分类");
        category.setParentId(0);
        CategoryDO categoryDO = categoryManager.add(category);
        // 添加一个规格
        String specJson = mockMvc.perform(post("/seller/goods/categories/" + categoryDO.getCategoryId() + "/specs")
                .param("spec_name", "测试规格")
                .header("Authorization", seller1))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        SpecificationDO spec = JsonUtil.jsonToObject(specJson, SpecificationDO.class);
        //正确添加3个规格值
        String value1 = mockMvc.perform(post("/seller/goods/specs/" + spec.getSpecId() + "/values")
                .param("value_name", "测试值1")
                .header("Authorization", seller1))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        specValue1 = JsonUtil.jsonToObject(value1, SpecValuesDO.class);
        String value2 = mockMvc.perform(post("/seller/goods/specs/" + spec.getSpecId() + "/values")
                .param("value_name", "测试值2")
                .header("Authorization", seller1))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        specValue2 = JsonUtil.jsonToObject(value1, SpecValuesDO.class);
        mockMvc.perform(post("/seller/goods/specs/" + spec.getSpecId() + "/values")
                .param("value_name", "测试值3")
                .header("Authorization", seller1))
                .andExpect(status().is(200));

    }

    @Test
    public void testAdd() throws Exception {

        // 必填验证和不正确的值
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            //{"name":["三只松鼠"]}
            Map map = new HashMap<>();
            for (String key : params.keySet()) {
                map.put(key, params.get(key).get(0));
                if (!"商品相册图片不能为空".equals(message)) {
                    map.put("goods_gallery_list", listGallery);
                }
            }
            mockMvc.perform(post("/seller/goods")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.objectToJson(map))
                    .header("Authorization", seller1))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        // //添加两个规格商品
        String[] names = new String[]{"category_id", "shop_cat_id", "brand_id", "goods_name", "sn", "price", "cost", "mktprice", "weight", "goods_transfee_charge", "market_enable", "template_id"};
        String[] values = new String[]{"1", "1", "1", "商品名称", "sn", "2.0", "3.0", "3.0", "500", "1", "1", "1"};
        Map map = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            map.put(names[i], values[i]);
        }
        map.put("goods_gallery_list", listGallery);

        String skuJson = "[\n" +
                "    {\n" +
                "      \"cost\": 20,\n" +
                "      \"price\": 30,\n" +
                "      \"quantity\": 10,\n" +
                "      \"sn\": \"\",\n" +
                "      \"spec_list\": [\n" +
                "        {\n" +
                "          \"spec_id\": " + specValue1.getSpecId() + ",\n" +
                "          \"spec_image\": \"http://a.jpg\",\n" +
                "          \"spec_type\": 1,\n" +
                "          \"spec_value\": \"" + specValue1.getSpecValue() + "\",\n" +
                "          \"spec_value_id\": " + specValue1.getSpecValueId() + "\n" +
                "        }\n" +
                "      ],\n" +
                "      \"weight\": 100\n" +
                "    },\n" +
                "    {\n" +
                "      \"cost\": 20,\n" +
                "      \"price\": 35,\n" +
                "      \"quantity\": 20,\n" +
                "      \"sn\": \"\",\n" +
                "      \"spec_list\": [\n" +
                "        {\n" +
                "          \"spec_id\": " + specValue2.getSpecId() + ",\n" +
                "          \"spec_image\": \"http://b.jpg\",\n" +
                "          \"spec_type\": 1,\n" +
                "          \"spec_value\": \"" + specValue2.getSpecValue() + "\",\n" +
                "          \"spec_value_id\": " + specValue2.getSpecValueId() + "\n" +
                "        }\n" +
                "      ],\n" +
                "      \"weight\": 100\n" +
                "    }\n" +
                "  ]";
        map.put("sku_list", JSONArray.fromObject(skuJson));
        String json = mockMvc.perform(post("/seller/goods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(map))
                .header("Authorization", seller1).header("uuid", uuid))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        GoodsDO goods = JsonUtil.jsonToObject(json, GoodsDO.class);
        //查询规格商品是否添加成功
        mockMvc.perform(get("/seller/goods/" + goods.getGoodsId() + "/skus")
                .header("Authorization", seller1))
                .andExpect(status().is(200));

    }

    @Test
    public void testEdit() throws Exception {

        // 添加一个商品
        GoodsDO goods = this.addGoods();

        // 必填验证和不正确的值
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            //{"name":["三只松鼠"]}
            Map map = new HashMap<>();
            for (String key : params.keySet()) {
                map.put(key, params.get(key).get(0));
                if (!"商品相册图片不能为空".equals(message)) {
                    map.put("goods_gallery_list", listGallery);
                }
            }
            mockMvc.perform(put("/seller/goods/" + goods.getGoodsId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.objectToJson(map))
                    .header("Authorization", seller1))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //更换登录用户修改该商品
        String[] names = new String[]{"category_id", "shop_cat_id", "brand_id", "goods_name", "sn", "price", "cost", "mktprice", "weight", "goods_transfee_charge", "market_enable", "template_id"};
        String[] values = new String[]{"1", "1", "1", "商品名称", "sn", "2.0", "3.0", "3.0", "500", "1", "1", "1"};
        Map map = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            map.put(names[i], values[i]);
        }
        map.put("goods_gallery_list", listGallery);
        ErrorMessage error = new ErrorMessage("301", "没有操作权限");
        mockMvc.perform(put("/seller/goods/" + goods.getGoodsId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(map))
                .header("Authorization", seller2))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //正确修改
        map.put("has_changed", "0");
        mockMvc.perform(put("/seller/goods/" + goods.getGoodsId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(map))
                .header("Authorization", seller1))
                .andExpect(status().is(200));

    }

    /**
     * 编辑查询商品单元测试
     *
     * @throws Exception
     */
    @Test
    public void testGet() throws Exception {

        // 添加一个商品
        GoodsDO goods = this.addGoods();
        ErrorMessage error = new ErrorMessage("301", "没有操作权限");
        mockMvc.perform(get("/seller/goods/" + goods.getGoodsId())
                .header("Authorization", seller2))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

    }

    /**
     * 下架商品单元测试
     *
     * @throws Exception
     */
    @Test
    public void testUnder() throws Exception {
        //添加一个商品
        GoodsDO goods = this.addGoods();
        //不是自己的商品
        ErrorMessage error = new ErrorMessage("301", "存在不属于您的商品，不能操作");
        mockMvc.perform(put("/seller/goods/" + goods.getGoodsId() + "/under")
                .header("Authorization", seller2))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //成功下架
        mockMvc.perform(put("/seller/goods/" + goods.getGoodsId() + "/under")
                .header("Authorization", seller1))
                .andExpect(status().is(200));
        //下架后不能下架
        error = new ErrorMessage("301", "存在不能下架的商品，不能操作");
        mockMvc.perform(put("/seller/goods/" + goods.getGoodsId() + "/under")
                .header("Authorization", seller1))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
    }

    /**
     * 放入回收站单元测试
     *
     * @throws Exception
     */
    @Test
    public void testRecycle() throws Exception {

        //添加一个商品
        GoodsDO goods = this.addGoods();
        //不是自己的商品
        ErrorMessage error = new ErrorMessage("301", "存在不属于您的商品，不能操作");
        mockMvc.perform(put("/seller/goods/" + goods.getGoodsId() + "/recycle")
                .header("Authorization", seller2))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //上架状态 不是能放入回收站的状态
        error = new ErrorMessage("301", "存在不能放入回收站的商品，不能操作");
        mockMvc.perform(put("/seller/goods/" + goods.getGoodsId() + "/recycle")
                .header("Authorization", seller1))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //成功下架
        mockMvc.perform(put("/seller/goods/" + goods.getGoodsId() + "/under")
                .header("Authorization", seller1))
                .andExpect(status().is(200));
        //成功放入回收站
        mockMvc.perform(put("/seller/goods/" + goods.getGoodsId() + "/recycle")
                .header("Authorization", seller1))
                .andExpect(status().is(200));

    }

    /**
     * 回收站还原单元测试
     *
     * @throws Exception
     */
    @Test
    public void testRevert() throws Exception {

        //添加一个商品
        GoodsDO goods = this.addGoods();
        //不是自己的商品
        ErrorMessage error = new ErrorMessage("301", "存在不属于您的商品，不能操作");
        mockMvc.perform(put("/seller/goods/" + goods.getGoodsId() + "/revert")
                .header("uuid", uuid)
                .header("Authorization", seller2))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //上架状态 不是能放入还原的状态
        error = new ErrorMessage("301", "存在不能还原的商品，不能操作");
        mockMvc.perform(put("/seller/goods/" + goods.getGoodsId() + "/revert")
                .header("Authorization", seller1)
                .header("uuid", uuid))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //成功下架
        mockMvc.perform(put("/seller/goods/" + goods.getGoodsId() + "/under")
                .header("Authorization", seller1)
                .header("uuid", uuid))
                .andExpect(status().is(200));
        //成功放入回收站
        mockMvc.perform(put("/seller/goods/" + goods.getGoodsId() + "/recycle")
                .header("Authorization", seller1).header("uuid", uuid))
                .andExpect(status().is(200));
        //成功还原
        mockMvc.perform(put("/seller/goods/" + goods.getGoodsId() + "/revert")
                .header("Authorization", seller1).header("uuid", uuid))
                .andExpect(status().is(200));
        //验证还原成功
        mockMvc.perform(put("/seller/goods/" + goods.getGoodsId() + "/recycle")
                .header("Authorization", seller1).header("uuid", uuid))
                .andExpect(status().is(200));
    }

    /**
     * 彻底删除单元测试
     *
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {

        //添加一个商品
        GoodsDO goods = this.addGoods();
        //不是自己的商品
        ErrorMessage error = new ErrorMessage("301", "存在不属于您的商品，不能操作");
        mockMvc.perform(delete("/seller/goods/" + goods.getGoodsId())
                .header("Authorization", seller2).header("uuid", uuid))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //上架状态 不是能放入删除的状态
        error = new ErrorMessage("301", "存在不能删除的商品，不能操作");
        mockMvc.perform(delete("/seller/goods/" + goods.getGoodsId())
                .header("Authorization", seller1).header("uuid", uuid))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //成功下架
        mockMvc.perform(put("/seller/goods/" + goods.getGoodsId() + "/under")
                .header("Authorization", seller1).header("uuid", uuid))
                .andExpect(status().is(200));
        //成功放入回收站
        mockMvc.perform(put("/seller/goods/" + goods.getGoodsId() + "/recycle")
                .header("Authorization", seller1).header("uuid", uuid))
                .andExpect(status().is(200));
        //成功删除
        mockMvc.perform(delete("/seller/goods/" + goods.getGoodsId())
                .header("Authorization", seller1).header("uuid", uuid))
                .andExpect(status().is(200));
        //验证删除成功
        error = new ErrorMessage("301", "没有操作权限");
        mockMvc.perform(get("/seller/goods/" + goods.getGoodsId())
                .header("Authorization", seller1).header("uuid", uuid))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
    }

    /**
     * 添加一个商品
     *
     * @return
     */
    private GoodsDO addGoods() throws Exception {

        String[] names = new String[]{"category_id", "shop_cat_id", "brand_id", "goods_name", "sn", "price", "cost", "mktprice", "weight", "goods_transfee_charge", "market_enable", "template_id"};
        String[] values = new String[]{"1", "1", "1", "商品名称", "sn", "2.0", "3.0", "3.0", "500", "1", "1", "1"};
        Map map = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            map.put(names[i], values[i]);
        }
        map.put("goods_gallery_list", listGallery);

        String json = mockMvc.perform(post("/seller/goods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(map))
                .header("Authorization", seller1).header("uuid", uuid))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        return JsonUtil.jsonToObject(json, GoodsDO.class);

    }


}
