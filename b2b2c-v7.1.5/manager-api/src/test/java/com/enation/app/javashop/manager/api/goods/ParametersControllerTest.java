package com.enation.app.javashop.manager.api.goods;

import com.enation.app.javashop.core.goods.model.dos.CategoryDO;
import com.enation.app.javashop.core.goods.model.dos.ParameterGroupDO;
import com.enation.app.javashop.core.goods.model.dos.ParametersDO;
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
 * @Description: 参数单元测试
 * @date 2018/4/315:19
 * @since v7.0.0
 */
public class ParametersControllerTest extends BaseTest {

    List<MultiValueMap<String, String>> list = null;

    List<MultiValueMap<String, String>> listNew = null;

    ParameterGroupDO group = null;

    @Before
    public void insertTestData() throws Exception {

        //先添加一个参数组
        group = addGroup();

        String[] names = new String[]{"param_name", "param_type", "options", "is_index", "required", "group_id", "message"};
        String[] values2 = new String[]{"", "1", "", "0", "0", "" + group.getGroupId(), "参数名称必填"};
        String[] values1 = new String[]{"参数名称1", "", "", "0", "0", "" + group.getGroupId(), "参数类型必选"};
        String[] values3 = new String[]{"参数名称2", "3", "", "0", "0", "" + group.getGroupId(), "参数类型传值不正确"};
        String[] values4 = new String[]{"参数名称3", "1", "", "", "0", "" + group.getGroupId(), "是否可索引必选"};
        String[] values5 = new String[]{"参数名称4", "1", "", "3", "0", "" + group.getGroupId(), "是否可索引传值不正确"};
        String[] values6 = new String[]{"参数名称5", "1", "", "0", "", "" + group.getGroupId(), "是否必填必选"};
        String[] values7 = new String[]{"参数名称6", "1", "", "0", "3", "" + group.getGroupId(), "是否必填传值不正确"};
        String[] values8 = new String[]{"参数名称7", "1", "", "0", "1", "", "所属参数组不能为空"};
        String[] values9 = new String[]{"参数名称8", "1", "", "0", "1", "-1", "所属参数组不存在"};
        String[] values10 = new String[]{"参数名称9", "2", "", "0", "1", "" + group.getGroupId(), "选择项类型必须填写选择内容"};
        list = toMultiValueMaps(names, values1, values2, values3, values4, values5, values6, values7, values8);
        listNew = toMultiValueMaps(names, values9, values10);
    }

    /**
     * 添加单元测试
     *
     * @throws Exception
     */
    @Test
    public void testAdd() throws Exception {

        // 必填验证
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/admin/goods/parameters")
                    .header("Authorization",superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //不存在的参数组
        for (MultiValueMap<String, String> params : listNew) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("303", message);
            mockMvc.perform(post("/admin/goods/parameters")
                    .header("Authorization",superAdmin)
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }

    }

    /**
     * 修改单元测试
     *
     * @throws Exception
     */
    @Test
    public void testEdit() throws Exception {

        //添加一个参数
        ParametersDO parameter = add();

        // 必填验证
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(put("/admin/goods/parameters/"+parameter.getParamId())
                    .header("Authorization",superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //选择项的参数类型必填选择内容
        ErrorMessage error = new ErrorMessage("303", "选择项类型必须填写选择内容");
        mockMvc.perform(put("/admin/goods/parameters/"+parameter.getParamId())
                .header("Authorization",superAdmin)
                .param("param_name", "新的参数")
                .param("param_type", "2")
                .param("is_index", "0")
                .param("required", "0")
                .param("group_id", "" + group.getGroupId()))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //不存在的参数
        error = new ErrorMessage("303", "参数不存在");
        mockMvc.perform(put("/admin/goods/parameters/-1")
                .header("Authorization",superAdmin)
                .param("param_name", "新的参数")
                .param("param_type", "1")
                .param("is_index", "0")
                .param("required", "0")
                .param("group_id", "" + group.getGroupId()))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //正确修改
        mockMvc.perform(put("/admin/goods/parameters/"+parameter.getParamId())
                .header("Authorization",superAdmin)
                .param("param_name", "修改后的参数")
                .param("param_type", "1")
                .param("is_index", "0")
                .param("required", "0")
                .param("group_id", "" + group.getGroupId()))
                .andExpect(status().is(200));
    }

    /**
     * 排序测试
     * @throws Exception
     */
    @Test
    public void testSort() throws Exception {

        //添加一个参数
        ParametersDO parameter = add();
        ParametersDO parameter1 = add();

        // 错误的关键字
        ErrorMessage error = new ErrorMessage("004", "不正确的状态 , 应该是 'up', 'down'其中之一");
        mockMvc.perform(put("/admin/goods/parameters/"+parameter.getParamId()+"/sort")
                .header("Authorization",superAdmin)
                .param("sort_type","acb"))
                .andExpect(status().is(400))
                .andExpect(objectEquals(error));

        //不存在的参数
        error = new ErrorMessage("303", "要移动的参数不存在");
        mockMvc.perform(put("/admin/goods/parameters/-1/sort")
                .header("Authorization",superAdmin)
                .param("sort_type","up"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //正确上移下移
        mockMvc.perform(put("/admin/goods/parameters/"+parameter1.getParamId()+"/sort")
                .header("Authorization",superAdmin)
                .param("sort_type","up"))
                .andExpect(status().is(200));

    }

    /**
     * 添加一个参数
     *
     * @return
     */
    private ParametersDO add() throws Exception {
        group = addGroup();
        String parameterJson = mockMvc.perform(post("/admin/goods/parameters")
                .header("Authorization",superAdmin)
                .param("param_name", "新的参数")
                .param("param_type", "1")
                .param("is_index", "0")
                .param("required", "0")
                .param("group_id", "" + group.getGroupId()))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        ParametersDO parameter = JsonUtil.jsonToObject(parameterJson, ParametersDO.class);
        return parameter;
    }

    /**
     * 正确添加一个分类后添加参数组
     *
     * @return
     * @throws Exception
     */
    private ParameterGroupDO addGroup() throws Exception {

        String categoryJson = mockMvc.perform(post("/admin/goods/categories")
                .header("Authorization",superAdmin)
                .param("name", "顶级分类")
                .param("parent_id", "0"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        CategoryDO category = JsonUtil.jsonToObject(categoryJson, CategoryDO.class);

        //添加参数组
        String json = mockMvc.perform(post("/admin/goods/parameter-groups")
                .header("Authorization",superAdmin)
                .param("group_name", "测试参数组")
                .param("category_id", category.getCategoryId() + ""))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        return JsonUtil.jsonToObject(json, ParameterGroupDO.class);
    }


}
