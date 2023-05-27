package com.enation.app.javashop.manager.api.goods;

import com.enation.app.javashop.core.goods.model.dos.CategoryDO;
import com.enation.app.javashop.core.goods.model.dos.ParameterGroupDO;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v2.0
 * @Description: 参数组单元测试
 * @date 2018/4/310:38
 * @since v7.0.0
 */
public class ParameterGroupControllerTest extends BaseTest {


    List<MultiValueMap<String, String>> list = null;

    @Before
    public void insertTestData() {

        String[] names = new String[]{"group_name", "category_id", "message"};
        String[] values2 = new String[]{"", "0", "参数组名称不能为空"};
        String[] values1 = new String[]{"基本参数", "", "关联的分类不能为空"};
        list = toMultiValueMaps(names, values2, values1);
    }

    /**
     * 添加单元测试
     * @throws Exception
     */
    @Test
    public void testAdd() throws Exception {

        // 必填验证
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/admin/goods/parameter-groups")
                    .header("Authorization",superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }

        // 关联一个不存在的分类
        ErrorMessage error = new ErrorMessage("304", "关联分类不存在");
        mockMvc.perform(post("/admin/goods/parameter-groups")
                .header("Authorization",superAdmin)
                .param("group_name","基本参数")
                .param("category_id","-1"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

    }

    /**
     * 修改单元测试
     * @throws Exception
     */
    @Test
    public void testEdit() throws Exception {

        CategoryDO category = addCategory();
        ParameterGroupDO group = add("基本参数", category.getCategoryId());
        // 必填验证
        ErrorMessage error = new ErrorMessage("004", "参数组名称不能为空");
        mockMvc.perform(put("/admin/goods/parameter-groups/"+group.getGroupId())
                .header("Authorization",superAdmin)
                .param("group_name",""))
                .andExpect(status().is(400))
                .andExpect(objectEquals(error));

        // 修改一个不存在的参数组
        error = new ErrorMessage("304", "参数组不存在");
        mockMvc.perform(put("/admin/goods/parameter-groups/-1")
                .header("Authorization",superAdmin)
                .param("group_name","基本参数"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //正确修改
        mockMvc.perform(put("/admin/goods/parameter-groups/"+group.getGroupId())
                .header("Authorization",superAdmin)
                .param("group_name","修改后的参数组"))
                .andExpect(status().is(200));
    }

    /**
     * 上下移单元测试
     * @throws Exception
     */
    @Test
    public void testSort() throws Exception {

        CategoryDO category = addCategory();
        ParameterGroupDO group = add("基本参数", category.getCategoryId());
        ParameterGroupDO group1 = add("基本参数1", category.getCategoryId());
        // 错误的关键字
        ErrorMessage error = new ErrorMessage("004", "不正确的状态 , 应该是 'up', 'down'其中之一");
        mockMvc.perform(put("/admin/goods/parameter-groups/"+group.getGroupId()+"/sort")
                .header("Authorization",superAdmin)
                .param("sort_type","acb"))
                .andExpect(status().is(400))
                .andExpect(objectEquals(error));
        // 操作一个不存在的参数组
        error = new ErrorMessage("304", "参数组不存在");
        mockMvc.perform(put("/admin/goods/parameter-groups/-1/sort")
                .header("Authorization",superAdmin)
                .param("sort_type","up"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //正确上移或下移
        mockMvc.perform(put("/admin/goods/parameter-groups/"+group1.getGroupId()+"/sort")
                .header("Authorization",superAdmin)
                .param("sort_type","up"))
                .andExpect(status().is(200));

    }

    /**
     * 添加一个参数组
     * @param groupName
     * @param categoryId
     * @return
     * @throws Exception
     */
    private ParameterGroupDO add(String groupName,Integer categoryId)throws Exception{

       String json =  mockMvc.perform(post("/admin/goods/parameter-groups")
               .header("Authorization",superAdmin)
                .param("group_name",groupName)
                .param("category_id",categoryId+""))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        return JsonUtil.jsonToObject(json,ParameterGroupDO.class);
    }

    /**
     * 正确添加一个分类
     * @return
     * @throws Exception
     */
    private CategoryDO addCategory() throws Exception{

        String categoryJson =  mockMvc.perform(post("/admin/goods/categories")
                .header("Authorization",superAdmin)
                .param("name","顶级分类")
                .param("parent_id","0"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        return  JsonUtil.jsonToObject(categoryJson, CategoryDO.class);

    }


}
