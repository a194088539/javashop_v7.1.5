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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v2.0
 * @Description: 规格测试
 * @date 2018/4/414:24
 * @since v7.0.0
 */
public class SpecificationControllerTest extends BaseTest{


    List<MultiValueMap<String, String>> list = null;

    @Before
    public void insertTestData() throws Exception {

        String[] names = new String[]{"spec_name", "spec_memo", "message"};
        String[] values = new String[]{"", "1","规格项名称不能为空"};
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
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/admin/goods/specs")
                    .header("Authorization",superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
    }

    @Test
    public void testEdit() throws Exception {

        //先添加一个规格
        SpecificationDO spec =  add();

        // 必填验证
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(put("/admin/goods/specs/"+spec.getSpecId())
                    .header("Authorization",superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }

        //不存在的规格
        ErrorMessage error = new ErrorMessage("305", "规格不存在");
        mockMvc.perform(put("/admin/goods/specs/-1")
                .header("Authorization",superAdmin)
                .param("spec_name", "颜色"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //正确修改
        mockMvc.perform(put("/admin/goods/specs/"+spec.getSpecId())
                .header("Authorization",superAdmin)
                .param("spec_name", "修改后的颜色"))
                .andExpect(status().is(200));

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
