package com.enation.app.javashop.seller.api.goods;

import com.enation.app.javashop.core.goods.model.dos.CategoryDO;
import com.enation.app.javashop.core.goods.model.dos.SpecificationDO;
import com.enation.app.javashop.core.goods.service.CategoryManager;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v2.0
 * @Description: 自定义规格单元测试
 * @date 2018/4/10 11:34
 * @since v7.0.0
 */
public class SpecificationControllerTest extends BaseTest {


    CategoryDO categoryDO = null;

    @Autowired
    private CategoryManager categoryManager;

    @Before
    public void insertTestData() throws Exception {

        CategoryDO category = new CategoryDO();
        category.setName("测试分类");
        category.setParentId(0);
        categoryDO = categoryManager.add(category);
    }

    /**
     * 添加规格单元测试
     *
     * @throws Exception
     */
    @Test
    public void testAddSpec() throws Exception {

        // 规格名称为空
        ErrorMessage error = new ErrorMessage("004", "规格名称不能为空");
        mockMvc.perform(post("/seller/goods/categories/" + categoryDO.getCategoryId() + "/specs")
                .param("spec_name", "")
                .header("Authorization", seller1))
                .andExpect(status().is(400))
                .andExpect(objectEquals(error));
        // 不存在的分类
        error = new ErrorMessage("305", "分类不存在");
        mockMvc.perform(post("/seller/goods/categories/-1/specs")
                .param("spec_name", "测试规格")
                .header("Authorization", seller1))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

    }

    /**
     * 添加规格值单元测试
     *
     * @throws Exception
     */
    @Test
    public void testAddSpecValue() throws Exception {

        // 添加一个规格
        String specJson = mockMvc.perform(post("/seller/goods/categories/" + categoryDO.getCategoryId() + "/specs")
                .param("spec_name", "测试规格")
                .header("Authorization", seller1))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        SpecificationDO spec = JsonUtil.jsonToObject(specJson, SpecificationDO.class);
        // 规格值为空
        ErrorMessage error = new ErrorMessage("004", "规格值名称不能为空");
        mockMvc.perform(post("/seller/goods/specs/" + spec.getSpecId() + "/values")
                .param("value_name", "")
                .header("Authorization", seller1))
                .andExpect(status().is(400))
                .andExpect(objectEquals(error));
        // 不属于我的或者平台的规格
        error = new ErrorMessage("306", "无权操作");
        mockMvc.perform(post("/seller/goods/specs/" + spec.getSpecId() + "/values")
                .param("value_name", "测试值1")
                .header("Authorization", seller2))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //正确添加3个规格值
        mockMvc.perform(post("/seller/goods/specs/" + spec.getSpecId() + "/values")
                .param("value_name", "测试值1")
                .header("Authorization", seller1))
                .andExpect(status().is(200));
        mockMvc.perform(post("/seller/goods/specs/" + spec.getSpecId() + "/values")
                .param("value_name", "测试值2")
                .header("Authorization", seller1))
                .andExpect(status().is(200));
        mockMvc.perform(post("/seller/goods/specs/" + spec.getSpecId() + "/values")
                .param("value_name", "测试值3")
                .header("Authorization", seller1))
                .andExpect(status().is(200));
        //查询分类关联的规格,和规格值
        mockMvc.perform(get("/seller/goods/categories/" + categoryDO.getCategoryId() + "/specs")
                .header("Authorization", seller1))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[0].spec_name").value("测试规格"))
                .andExpect(jsonPath("$.[0].value_list[0].spec_value").value("测试值1"))
                .andExpect(jsonPath("$.[0].value_list[1].spec_value").value("测试值2"))
                .andExpect(jsonPath("$.[0].value_list[2].spec_value").value("测试值3"));

        //更换商家查询不到
        mockMvc.perform(get("/seller/goods/categories/" + categoryDO.getCategoryId() + "/specs")
                .header("Authorization", seller2))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$").isEmpty());

    }


}
