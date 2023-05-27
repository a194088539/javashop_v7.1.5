package com.enation.app.javashop.manager.api.goods;

import com.enation.app.javashop.core.goods.model.dos.BrandDO;
import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.model.dto.GoodsDTO;
import com.enation.app.javashop.core.goods.service.GoodsManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v2.0
 * @Description: 品牌单元测试
 * @date 2018/3/3011:04
 * @since v7.0.0
 */
public class BrandControllerTest extends BaseTest{

    List<MultiValueMap<String, String>> list = null;


    @Autowired
    public GoodsManager goodsManager;

    @Qualifier("goodsDaoSupport")
    @Autowired
    private DaoSupport daoSupport;

    @Before
    public void insertTestData(){
        String[] names = new String[]{"name","logo","message"};
        String[] values1 = new String[]{"","http:www.baidu.com","品牌名称不能为空"};
        String[] values2 = new String[]{"三只松鼠","","品牌图标不能为空"};
        list = toMultiValueMaps(names,values2,values1);

    }

    @Test
    public void testAdd() throws Exception{

        BrandDO brand = this.add();

        // 必填验证
        for (MultiValueMap<String,String> params  : list){
            String message =  params.get("message").get(0);
            ErrorMessage error  = new ErrorMessage("004",message);
            mockMvc.perform(post("/admin/goods/brands")
                    .header("Authorization",superAdmin)
                    .params( params ))
                    .andExpect(status().is(400))
                    .andExpect(  objectEquals( error ));
        }

        // 修改必填验证
        for(MultiValueMap<String, String> params : list){
            String message =  params.get("message").get(0);
            ErrorMessage error  = new ErrorMessage("004",message);
            mockMvc.perform(put("/admin/goods/brands/"+brand.getBrandId())
                    .header("Authorization",superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(  objectEquals( error ));
        }
        //修改一个不存在的品牌
        ErrorMessage error  = new ErrorMessage("302","品牌不存在");
        mockMvc.perform(put("/admin/goods/brands/0")
                .header("Authorization",superAdmin)
                .param("logo" ,"http://www.baidu.com")
                .param("name" ,"三只松鼠"))
                .andExpect(status().is(500))
                .andExpect( objectEquals( error ) );

        //正确修改品牌
        mockMvc.perform(put("/admin/goods/brands/"+brand.getBrandId())
                .header("Authorization",superAdmin)
                .param("logo" ,"http://www.baidu.com1")
                .param("name" ,"三只松鼠"))
                .andExpect(status().is(200));

        //需要先添加商品
        String goodsJson = "{\n" +
                "  \"brand_id\": "+brand.getBrandId()+",\n" +
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
        this.daoSupport.insert(goodsDO);
        //删除品牌
        error  = new ErrorMessage("302","已有商品关联，不能删除");
        mockMvc.perform(delete("/admin/goods/brands/"+brand.getBrandId())
                .header("Authorization",superAdmin))
                .andExpect(status().is(500))
                .andExpect( objectEquals( error ));
        //正确删除
        BrandDO brand1 = this.add();
        mockMvc.perform(delete("/admin/goods/brands/"+brand1.getBrandId())
                .header("Authorization",superAdmin))
                .andExpect(status().is(200));

    }


    /**
     * 添加一个品牌
     * @return
     * @throws Exception
     */
    private BrandDO add() throws Exception{
        //添加成功验证
        String brandJson = mockMvc.perform(post("/admin/goods/brands")
                .header("Authorization",superAdmin)
                .param("logo" ,"http://www.baidu.com")
                .param("name" ,"三只松鼠"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("name").value("三只松鼠"))
                .andExpect(jsonPath("brand_id").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        return JsonUtil.jsonToObject(brandJson, BrandDO.class);
    }
}
