package com.enation.app.javashop.manager.api.goods;

import com.enation.app.javashop.core.goodssearch.model.CustomWords;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v1.0
 * @Description: 自定义分词单元测试
 * @date 2018/6/21 16:33
 * @since v7.0.0
 */
public class CustomWordsControllerTest extends BaseTest {

    @Autowired
    private DaoSupport daoSupport;

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
        mockMvc.perform(post("/admin/goods/custom-words")
                .header("Authorization",superAdmin))
                .andExpect(status().is(400))
                .andExpect(  objectEquals( error ));
        //正确添加
        String res = mockMvc.perform(post("/admin/goods/custom-words").param("name","自定义个分词")
                .header("Authorization",superAdmin))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        CustomWords customWords = JsonUtil.jsonToObject(res,CustomWords.class);
        //修改-必填
        mockMvc.perform(put("/admin/goods/custom-words/"+customWords.getId())
                .header("Authorization",superAdmin))
                .andExpect(status().is(400))
                .andExpect(  objectEquals( error ));
        //正确修改
        mockMvc.perform(put("/admin/goods/custom-words/"+customWords.getId())
                .header("Authorization",superAdmin)
                .param("name","自定义个分词1"))
                .andExpect(status().is(200));
        //查询一个
        mockMvc.perform(get("/admin/goods/custom-words/"+customWords.getId())
                .header("Authorization",superAdmin))
                .andExpect(status().is(200))
                .andExpect(jsonPath("name").value("自定义个分词1"));
        //热部署调用
        mockMvc.perform(post("/admin/goods/custom-words")
                .header("Authorization",superAdmin)
                .param("name","自定义个分词"))
                .andExpect(status().is(200));

        res = mockMvc.perform(get("/admin/goods/custom-words/deploy")
                .header("Authorization",superAdmin))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        Assert.assertThat(res,containsString(new String("自定义个分词1".getBytes(),"utf-8")));

    }
}
