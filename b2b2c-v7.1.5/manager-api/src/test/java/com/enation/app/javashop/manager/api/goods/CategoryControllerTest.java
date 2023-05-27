package com.enation.app.javashop.manager.api.goods;

import com.enation.app.javashop.core.goods.model.dos.BrandDO;
import com.enation.app.javashop.core.goods.model.dos.CategoryDO;
import com.enation.app.javashop.core.goods.model.dos.SpecificationDO;
import com.enation.app.javashop.core.goods.service.BrandManager;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @Description: 分类测试
 * @date 2018/4/215:27
 * @since v7.0.0
 */
public class CategoryControllerTest extends BaseTest{

    List<MultiValueMap<String, String>> list = null;

    CategoryDO category = null;

    @Autowired
    private BrandManager brandManager;

    @Before
    public void insertTestData() throws Exception{

        //添加一个分类
        category = add("食品酒水", 0);

        String[] names = new String[]{"name","parent_id","category_order","image","message"};
        String[] values1 = new String[]{"","0","","","分类名称不能为空"};
        String[] values2 = new String[]{"服装鞋帽","","","","父分类不能为空"};
        list = toMultiValueMaps(names,values2,values1);
    }

    /**
     * 添加测试
     * @throws Exception
     */
    @Test
    public void testAdd() throws Exception{

        // 必填验证
        for (MultiValueMap<String,String> params  : list){
            String message =  params.get("message").get(0);
            ErrorMessage error  = new ErrorMessage("004",message);
            mockMvc.perform(post("/admin/goods/categories")
                    .header("Authorization",superAdmin)
                    .params( params ))
                    .andExpect(status().is(400))
                    .andExpect(  objectEquals( error ));
        }
        // 使用一个不存在的父分类
        ErrorMessage error  = new ErrorMessage("300","父分类不存在");
        mockMvc.perform(post("/admin/goods/categories")
                .header("Authorization",superAdmin)
                .param("name","服装鞋帽")
                .param("parent_id","-1"))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));
        // 最多三级分类
        CategoryDO twoCategory = add("二级",category.getCategoryId());
        CategoryDO threeCategory = add("三级",twoCategory.getCategoryId());
        error  = new ErrorMessage("300","最多为三级分类,添加失败");
        mockMvc.perform(post("/admin/goods/categories")
                .header("Authorization",superAdmin)
                .param("name","四级")
                .param("parent_id",""+threeCategory.getCategoryId()))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));


    }

    /**
     * 修改测试
     * @throws Exception
     */
    @Test
    public void testEdit() throws Exception{
        CategoryDO category = add("食品酒水", 0);
        CategoryDO category1 = add("食品酒水1", 0);
        // 必填验证
        for (MultiValueMap<String,String> params  : list){
            String message =  params.get("message").get(0);
            ErrorMessage error  = new ErrorMessage("004",message);
            mockMvc.perform(put("/admin/goods/categories/"+category.getCategoryId())
                    .header("Authorization",superAdmin)
                    .params( params ))
                    .andExpect(status().is(400))
                    .andExpect(  objectEquals( error ));
        }
        // 使用一个不存在的父分类
        ErrorMessage error  = new ErrorMessage("300","父分类不存在");
        mockMvc.perform(put("/admin/goods/categories/"+category.getCategoryId())
                .header("Authorization",superAdmin)
                .param("name","服装鞋帽")
                .param("parent_id","-1"))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));
        // 最多三级分类
        CategoryDO twoCategory = add("二级",category.getCategoryId());
        CategoryDO threeCategory = add("三级",twoCategory.getCategoryId());
        CategoryDO threeCategory1 = add("三级2",twoCategory.getCategoryId());
        error  = new ErrorMessage("300","最多为三级分类,添加失败");
        mockMvc.perform(put("/admin/goods/categories/"+threeCategory1.getCategoryId())
                .header("Authorization",superAdmin)
                .param("name","四级")
                .param("parent_id",""+threeCategory.getCategoryId()))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));
        //有子分类不能修改上级分类
        error  = new ErrorMessage("300","当前分类有子分类，不能更换上级分类");
        mockMvc.perform(put("/admin/goods/categories/"+twoCategory.getCategoryId())
                .header("Authorization",superAdmin)
                .param("name","四级")
                .param("parent_id",""+category1.getCategoryId()))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));
        //正确修改
        mockMvc.perform(put("/admin/goods/categories/"+twoCategory.getCategoryId())
                .header("Authorization",superAdmin)
                .param("name","修改分类名称")
                .param("parent_id",""+twoCategory.getParentId()))
                .andExpect(status().is(200));
    }

    /**
     * 测试删除
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception{
        // 有子分类不能删除
        CategoryDO oneCategory = add("二级", category.getCategoryId());
        ErrorMessage error  = new ErrorMessage("300","此类别下存在子类别不能删除");
        mockMvc.perform(delete("/admin/goods/categories/"+category.getCategoryId())
                .header("Authorization",superAdmin))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));

        //正确删除
        mockMvc.perform(delete("/admin/goods/categories/"+oneCategory.getCategoryId())
                .header("Authorization",superAdmin))
                .andExpect(status().is(200));
        mockMvc.perform(delete("/admin/goods/categories/"+category.getCategoryId())
                .header("Authorization",superAdmin))
                .andExpect(status().is(200));
    }

    /**
     * 测试分类绑定品牌
     * @throws Exception
     */
    @Test
    public void testBingBrand() throws Exception{

        //添加两个品牌
        BrandDO brand = new BrandDO();
        brand.setName("三只松鼠");
        brand.setLogo("http:www.jpg");
        BrandDO brand1 = brandManager.add(brand);
        BrandDO brand3 = new BrandDO();
        brand3.setName("三只松鼠");
        brand3.setLogo("http:www.jpg");
        BrandDO brand2 = brandManager.add(brand3);
        //不存在的分类
        ErrorMessage error  = new ErrorMessage("300","该分类不存在");
        mockMvc.perform(put("/admin/goods/categories/-1/brands")
                .header("Authorization",superAdmin)
                .param("choose_brands","1"))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));
        //品牌为空
        error  = new ErrorMessage("004","至少选中一个品牌");
        mockMvc.perform(put("/admin/goods/categories/"+category.getCategoryId()+"/brands")
                .header("Authorization",superAdmin)
                .param("choose_brands",""))
                .andExpect(status().is(400))
                .andExpect(  objectEquals( error ));
        //绑定了不存在的品牌
        error  = new ErrorMessage("300","品牌传参错误");
        mockMvc.perform(put("/admin/goods/categories/"+category.getCategoryId()+"/brands")
                .header("Authorization",superAdmin)
                .param("choose_brands",""+brand1.getBrandId())
                .param("choose_brands",""+brand2.getBrandId())
                .param("choose_brands","-1"))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));
        //正确绑定品牌
        mockMvc.perform(put("/admin/goods/categories/"+category.getCategoryId()+"/brands")
                .header("Authorization",superAdmin)
                .param("choose_brands",""+brand1.getBrandId())
                .param("choose_brands",""+brand2.getBrandId()))
                .andExpect(status().is(200))
                .andExpect( jsonPath("$.[0].brand_id").value(brand1.getBrandId()+""))
                .andExpect( jsonPath("$.[1].brand_id").value(brand2.getBrandId()+""));
    }

    /**
     * 测试分类绑定规格
     * @throws Exception
     */
    @Test
    public void testBingSpec() throws Exception{

        //添加两个规格
        SpecificationDO spec1 = addSpec();
        SpecificationDO spec2 = addSpec();
        //不存在的分类
        ErrorMessage error  = new ErrorMessage("300","该分类不存在");
        mockMvc.perform(put("/admin/goods/categories/-1/specs")
                .header("Authorization",superAdmin)
                .param("choose_specs","1"))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));
        //规格为空
        error  = new ErrorMessage("004","至少选中一个规格");
        mockMvc.perform(put("/admin/goods/categories/"+category.getCategoryId()+"/specs")
                .header("Authorization",superAdmin)
                .param("choose_specs",""))
                .andExpect(status().is(400))
                .andExpect(  objectEquals( error ));
        //绑定了不存在的规格
        error  = new ErrorMessage("300","规格绑定传参错误");
        mockMvc.perform(put("/admin/goods/categories/"+category.getCategoryId()+"/specs")
                .header("Authorization",superAdmin)
                .param("choose_specs",""+spec1.getSpecId())
                .param("choose_specs",""+spec2.getSpecId())
                .param("choose_specs","-1"))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));
        //正确绑定规格
        mockMvc.perform(put("/admin/goods/categories/"+category.getCategoryId()+"/specs")
                .header("Authorization",superAdmin)
                .param("choose_specs",""+spec1.getSpecId())
                .param("choose_specs",""+spec2.getSpecId()))
                .andExpect(status().is(200))
                .andExpect( jsonPath("$.[0].spec_id").value(spec1.getSpecId()+""))
                .andExpect( jsonPath("$.[1].spec_id").value(spec2.getSpecId()+""));
    }
    /**
     * 正确添加一个分类
     * @param name
     * @param parentId
     * @return
     * @throws Exception
     */
    private CategoryDO add(String name,Integer parentId) throws Exception{

       String categoryJson =  mockMvc.perform(post("/admin/goods/categories")
               .header("Authorization",superAdmin)
               .param("name",name)
               .param("parent_id",parentId+""))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        return  JsonUtil.jsonToObject(categoryJson, CategoryDO.class);

    }

    /**
     * 添加一个规格
     * @return
     * @throws Exception
     */
    private SpecificationDO addSpec() throws Exception {

        String json = mockMvc.perform(post("/admin/goods/specs")
                .header("Authorization",superAdmin)
                .param("spec_name", "颜色"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("seller_id").value(0))
                .andReturn().getResponse().getContentAsString();

        SpecificationDO parameter = JsonUtil.jsonToObject(json, SpecificationDO.class);

        return parameter;
    }

}
