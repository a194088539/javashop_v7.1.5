package com.enation.app.javashop.manager.api.pagedata;

import com.enation.app.javashop.core.pagedata.model.FocusPicture;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v1.0
 * @Description: 焦点图单元测试
 * @date 2018/6/15 11:43
 * @since v7.0.0
 */
@Transactional(value = "systemTransactionManager", rollbackFor = Exception.class)
public class FocusPictureControllerTest extends BaseTest {

    List<MultiValueMap<String, String>> list = null;

    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport daoSupport;

    @Before
    public void insertTestData() {
        String  sql = "delete from es_focus_picture";
        this.daoSupport.execute(sql);

        String[] names = new String[]{"pic_url", "operation_type", "operation_param", "client_type", "message"};
        String[] values1 = new String[]{"", "URL", "http://www.baidu.com", "PC", "图片地址不能为空"};
        String[] values2 = new String[]{"http://a.jpg", "", "http://www.baidu.com", "PC", "不正确的操作类型"};
        String[] values5 = new String[]{"http://a.jpg", "1", "http://www.baidu.com", "PC", "不正确的操作类型"};
        String[] values4 = new String[]{"http://a.jpg", "URL", "http://www.baidu.com", "", "不正确的客户端类型"};
        String[] values6 = new String[]{"http://a.jpg", "URL", "http://www.baidu.com", "PC1", "不正确的客户端类型"};
        list = toMultiValueMaps(names, values2, values1,  values4, values5, values6);

    }

    /**
     * 添加焦点图
     *
     * @throws Exception
     */
    @Test
    public void testAdd() throws Exception {

        // 必填验证
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/admin/focus-pictures")
                    .header("Authorization",superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }

    }

    /**
     * 修改焦点图
     *
     * @throws Exception
     */
    @Test
    public void testEdit() throws Exception {

        FocusPicture picture = this.add();

        // 必填验证
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(put("/admin/focus-pictures/" + picture.getId())
                    .header("Authorization",superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }

        //正确修改
        mockMvc.perform(put("/admin/focus-pictures/" + picture.getId())
                .header("Authorization",superAdmin)
                .param("pic_url", "http://a.jpg")
                .param("operation_type", "URL")
                .param("operation_param", "http://www.baidu.com1")
                .param("client_type", "PC"))
                .andExpect(status().is(200));
        //查询一个
        mockMvc.perform(get("/admin/focus-pictures/" + picture.getId())
                .header("Authorization",superAdmin))
                .andExpect(status().is(200))
                .andExpect(jsonPath("operation_param").value("http://www.baidu.com1"));


    }

    /**
     * 删除焦点图
     *
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {
        FocusPicture picture = this.add();
        mockMvc.perform(delete("/admin/focus-pictures/" + picture.getId())
                .header("Authorization",superAdmin))
                .andExpect(status().is(200));
    }


    /**
     * 添加一个焦点图
     *
     * @return
     * @throws Exception
     */
    private FocusPicture add() throws Exception {
        String res = mockMvc.perform(post("/admin/focus-pictures")
                .header("Authorization",superAdmin)
                .param("pic_url", "http://a.jpg")
                .param("operation_type", "URL")
                .param("operation_param", "http://www.baidu.com")
                .param("client_type", "PC"))
                .andReturn().getResponse().getContentAsString();

        return JsonUtil.jsonToObject(res, FocusPicture.class);
    }

}
