package com.enation.app.javashop.manager.api.pagedata;

import com.enation.app.javashop.core.pagedata.model.HotKeyword;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v1.0
 * @Description: 热门关键字单元测试
 * @date 2018/6/14 11:15
 * @since v7.0.0
 */
@Transactional(value = "systemTransactionManager",rollbackFor=Exception.class)
public class HotKeywordControllerTest extends BaseTest {


    @Test
    public void testAdd() throws Exception{

        //关键字内容必填
        ErrorMessage error  = new ErrorMessage("004","关键字不能为空");
        mockMvc.perform(post("/admin/pages/hot-keywords")
                .header("Authorization",superAdmin))
                .andExpect(status().is(400))
                .andExpect(  objectEquals( error ));
        //正确添加
        String res = mockMvc.perform(post("/admin/pages/hot-keywords").param("hot_name","狗粮")
                .header("Authorization",superAdmin))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        HotKeyword keyword = JsonUtil.jsonToObject(res, HotKeyword.class);
        //查询这个关键字
        mockMvc.perform(get("/admin/pages/hot-keywords/"+keyword.getId())
                .header("Authorization",superAdmin))
                .andExpect(status().is(200))
                .andExpect( jsonPath("hot_name").value("狗粮"));
        //修改热门关键字，关键字为空
        mockMvc.perform(put("/admin/pages/hot-keywords/"+keyword.getId())
                .header("Authorization",superAdmin))
                .andExpect(status().is(400))
                .andExpect(  objectEquals( error ));
        //不存在的记录
        error  = new ErrorMessage("954","该记录不存在，请正确操作");
        mockMvc.perform(put("/admin/pages/hot-keywords/-1").param("hot_name","狗粮1")
                .header("Authorization",superAdmin))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));
        //正确修改
        mockMvc.perform(put("/admin/pages/hot-keywords/"+keyword.getId()).param("hot_name","狗粮1")
                .header("Authorization",superAdmin))
                .andExpect(status().is(200));
        //查询这个关键字
        mockMvc.perform(get("/admin/pages/hot-keywords/"+keyword.getId())
                .header("Authorization",superAdmin))
                .andExpect(status().is(200))
                .andExpect( jsonPath("hot_name").value("狗粮1"));
        //查询关键字列表
        mockMvc.perform(get("/admin/pages/hot-keywords")
                .header("Authorization",superAdmin))
                .andExpect(status().is(200));
    }





}
