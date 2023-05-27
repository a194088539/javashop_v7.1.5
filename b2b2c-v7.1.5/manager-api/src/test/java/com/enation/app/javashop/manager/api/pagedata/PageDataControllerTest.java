package com.enation.app.javashop.manager.api.pagedata;

import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v1.0
 * @Description: 楼层数据单元测试
 * @date 2018/6/15 14:30
 * @since v7.0.0
 */
@Transactional(value = "systemTransactionManager", rollbackFor = Exception.class)
public class PageDataControllerTest extends BaseTest {

    List<MultiValueMap<String, String>> list = null;

    String pageData = "[     {         \"tpl_id\":23,         \"blockList\":[             {                 \"block_type\":\"IMAGE\",                 \"block_value\":\"http://javashop-statics.oss-cn-beijing.aliyuncs.com/demo/56256727.jpg\"             }         ]     },     {         \"tpl_id\":24,         \"blockList\":[             {                 \"block_type\":\"GOODS\",                 \"block_value\": {                     \"goods_id\":75,                     \"goods_name\":\"促销商品K\",                     \"sn\":\"0074\",                     \"goods_image\":\"http://javashop-statics.oss-cn-beijing.aliyuncs.com/demo/E1EE42BA506F4A2895B449F661D1644B.jpg_300x300\",                     \"enable_quantity\":100,                     \"quantity\":100,                     \"price\":100.0,                     \"create_time\":1517815032,                     \"market_enable\":1,                     \"brand_name\":\"蜜饯\",                     \"category_name\":\"蜜饯\"                 }             }         ]     },     {         \"tpl_id\":27,         \"blockList\":[             {                 \"block_type\":\"IMAGE\",                 \"block_value\":\"http://javashop-statics.oss-cn-beijing.aliyuncs.com/test/null/4D928A6DABE74EF5A78C2A2E2F5764F7.GIF\",                 \"block_opt\":{                     \"opt_type\":\"keyword\",                     \"opt_value\":\"关键字\",                     \"opt_detail\":\"这是个关键字类型\"                 }             },             {                 \"block_type\":\"IMAGE\",                 \"block_value\":\"\"             }         ]     } ]";


    @Before
    public void insertTestData() {

        String[] names = new String[]{"client_type", "page_type", "page_name", "page_data", "message"};
        String[] values1 = new String[]{"PC1", "INDEX", "首页", pageData, "不正确的客户端类型"};
        String[] values2 = new String[]{"PC", "INDEX1", "首页", pageData, "不正确的页面类型"};
        String[] values5 = new String[]{"PC", "INDEX", "", pageData, "名称不能为空"};
        String[] values3 = new String[]{"PC", "INDEX", "首页", "", "页面数据不能为空"};
        list = toMultiValueMaps(names, values2, values1, values3, values5);

    }

    /**
     * 添加或修改楼层数据
     *
     * @throws Exception
     */
    @Test
    public void testAdd() throws Exception {

        // 必填验证
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            String clientType = params.get("client_type").get(0);
            String pageType = params.get("page_type").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(put("/admin/pages/" + clientType + "/" + pageType)
                    .header("Authorization",superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }

        //正确添加
        mockMvc.perform(put("/admin/pages/PC/INDEX")
                .header("Authorization",superAdmin)
                .param("page_name", "首页")
                .param("page_data", pageData))
                .andExpect(status().is(200));

        //使用客户端类型和页面类型查询
        ErrorMessage error = new ErrorMessage("004", "不正确的客户端类型");
        mockMvc.perform(get("/admin/pages/PC1/INDEX")
                .header("Authorization",superAdmin))
                .andExpect(status().is(400))
                .andExpect(objectEquals(error));

        error = new ErrorMessage("004", "不正确的页面类型");
        mockMvc.perform(get("/admin/pages/PC/INDEX1")
                .header("Authorization",superAdmin))
                .andExpect(status().is(400))
                .andExpect(objectEquals(error));

        mockMvc.perform(get("/admin/pages/PC/INDEX")
                .header("Authorization",superAdmin))
                .andExpect(status().is(200))
                .andExpect(jsonPath("page_name").value("首页"));

    }


}
