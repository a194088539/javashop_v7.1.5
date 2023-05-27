package com.enation.app.javashop.manager.api.goods;

import com.enation.app.javashop.core.goods.model.dos.SpecificationDO;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v2.0
 * @Description: 规格值单元测试
 * @date 2018/4/414:59
 * @since v7.0.0
 */
public class SpecValuesControllerTest extends BaseTest{

    List<MultiValueMap<String, String>> list = null;

    SpecificationDO specs = null;

    @Before
    public void insertTestData() throws Exception {

        //添加一个规格
        specs = add();

        String[] names = new String[]{"spec_id",  "message"};
        String[] values = new String[]{""+specs.getSpecId(), "至少添加一个规格值"};
        list = toMultiValueMaps(names, values);
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
            String specId = params.get("spec_id").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/admin/goods/specs/"+specId+"/values")
                    .header("Authorization",superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //所属规格不存在
        ErrorMessage error = new ErrorMessage("306", "所属规格不存在");
        mockMvc.perform(post("/admin/goods/specs/-1/values")
                .header("Authorization",superAdmin)
                .param("value_list","红色"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //正确添加多个规格值
        mockMvc.perform(post("/admin/goods/specs/"+specs.getSpecId()+"/values")
                .header("Authorization",superAdmin)
                .param("value_list","红色")
                .param("value_list","白色"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[0].spec_value").value("红色"))
                .andExpect(jsonPath("$.[1].spec_value").value("白色"));
    }

    /**
     * 添加一个规格
     * @return
     * @throws Exception
     */
    private SpecificationDO add()throws Exception {

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
