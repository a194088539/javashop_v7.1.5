package com.enation.app.javashop.manager.api.goodssearch;

import com.enation.app.javashop.core.base.service.SettingManager;
import com.enation.app.javashop.core.goodssearch.model.CustomWords;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomWordsManagerControllerTest extends BaseTest {



    @Autowired
    private DaoSupport daoSupport;

    @Autowired
    private SettingManager settingManager;
    /**
     * 测试添加和修改
     * @throws Exception
     */
    @Test
    public void testAddAndEdit() throws Exception{

        String sql = "delete from es_custom_words";
        this.daoSupport.execute(sql);

        //添加-必填验证
        ErrorMessage error  = new ErrorMessage("004","分词名称必填");
        ErrorMessage error1  = new ErrorMessage("310","【自定义个分词1】已存在");
        mockMvc.perform(post("/admin/goodssearch/custom-words")
                .header("Authorization",superAdmin))
                .andExpect(status().is(400))
                .andExpect(  objectEquals( error ));
        //正确添加
        String res = mockMvc.perform(post("/admin/goodssearch/custom-words").param("name","自定义个分词")
                .header("Authorization",superAdmin))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();


        CustomWords customWords = JsonUtil.jsonToObject(res,CustomWords.class);
        //修改-必填
        mockMvc.perform(put("/admin/goodssearch/custom-words/"+customWords.getId())
                .header("Authorization",superAdmin))
                .andExpect(status().is(400))
                .andExpect(  objectEquals( error ));
        //正确修改
        mockMvc.perform(put("/admin/goodssearch/custom-words/"+customWords.getId())
                .header("Authorization",superAdmin)
                .param("name","自定义个分词1"))
                .andExpect(status().is(200));

        //添加已存在关键词
        mockMvc.perform(post("/admin/goodssearch/custom-words").param("name","自定义个分词1")
                .header("Authorization",superAdmin))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error1));
        //修改成已存在关键词
        mockMvc.perform(put("/admin/goodssearch/custom-words/"+customWords.getId()).param("name","自定义个分词1")
                .header("Authorization",superAdmin))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error1));
        //查询一个
        mockMvc.perform(get("/admin/goodssearch/custom-words/"+customWords.getId())
                .header("Authorization",superAdmin))
                .andExpect(status().is(200))
                .andExpect(jsonPath("name").value("自定义个分词1"));

        //设置秘钥
        res = mockMvc.perform(put("/admin/goodssearch/custom-words/secret-key")
                .header("Authorization",superAdmin)
                .param("password","123456"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        //获取秘钥
        String res1 = mockMvc.perform(get("/admin/goodssearch/custom-words/secret-key")
                .header("Authorization",superAdmin))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        Assert.assertEquals(res,res1);

    }

}