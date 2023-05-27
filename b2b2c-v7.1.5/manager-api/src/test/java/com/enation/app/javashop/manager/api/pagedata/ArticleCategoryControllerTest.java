package com.enation.app.javashop.manager.api.pagedata;

import com.enation.app.javashop.core.pagedata.model.ArticleCategory;
import com.enation.app.javashop.core.pagedata.model.enums.ArticleCategoryType;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v2.0
 * @Description: 文章分类的单元测试
 * @date 2018/6/15 15:44
 * @since v7.0.0
 */
@Transactional(value = "systemTransactionManager", rollbackFor = Exception.class)
public class ArticleCategoryControllerTest extends BaseTest {

    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport daoSupport;

    /**
     * 添加文章分类
     *
     * @throws Exception
     */
    @Test
    public void testAdd() throws Exception {

        ErrorMessage error = new ErrorMessage("004", "分类名称不能为空");
        //分类名称不能为空
        mockMvc.perform(post("/admin/pages/article-categories")
                .header("Authorization",superAdmin)
                .param("name", ""))
                .andExpect(status().is(400))
                .andExpect(objectEquals(error));
        //不存在的父分类
        error = new ErrorMessage("951", "父分类不存在");
        mockMvc.perform(post("/admin/pages/article-categories")
                .header("Authorization",superAdmin)
                .param("name", "会员特权")
                .param("parent_id", "-1"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //正确添加一个分类
        String res = mockMvc.perform(post("/admin/pages/article-categories")
                .header("Authorization",superAdmin)
                .param("name", "父分类")
                .param("parent_id", "0"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        ArticleCategory cat = JsonUtil.jsonToObject(res,ArticleCategory.class);
        //正确添加一个子分类
        String res1 = mockMvc.perform(post("/admin/pages/article-categories")
                .header("Authorization",superAdmin)
                .param("name", "子分类")
                .param("parent_id", ""+cat.getId()))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        ArticleCategory cat1 = JsonUtil.jsonToObject(res1,ArticleCategory.class);
        //最多两级分类
        error = new ErrorMessage("951", "最多为二级分类,添加失败");
        mockMvc.perform(post("/admin/pages/article-categories")
                .header("Authorization",superAdmin)
                .param("name", "子分类")
                .param("parent_id", ""+cat1.getId()))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
    }

    /**
     * 修改文章分类
     *
     * @throws Exception
     */
    @Test
    public void testEdit() throws Exception {

        //正确添加一个分类
        String res = mockMvc.perform(post("/admin/pages/article-categories")
                .header("Authorization",superAdmin)
                .param("name", "父分类")
                .param("parent_id", "0"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        ArticleCategory cat = JsonUtil.jsonToObject(res,ArticleCategory.class);

        ErrorMessage error = new ErrorMessage("004", "分类名称不能为空");
        //分类名称不能为空
        mockMvc.perform(put("/admin/pages/article-categories/"+cat.getId())
                .header("Authorization",superAdmin)
                .param("name", ""))
                .andExpect(status().is(400))
                .andExpect(objectEquals(error));
        //不存在的父分类
        error = new ErrorMessage("951", "父分类不存在");
        mockMvc.perform(put("/admin/pages/article-categories/"+cat.getId())
                .header("Authorization",superAdmin)
                .param("name", "会员特权")
                .param("parent_id", "-1"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //特殊的文章分类不可修改
        ArticleCategory category = new ArticleCategory();
        category.setName("");
        category.setType(ArticleCategoryType.HELP.name());
        category.setAllowDelete(0);
        category.setParentId(0);
        this.daoSupport.insert(category);
        int catId = this.daoSupport.getLastId("");
        error = new ErrorMessage("950", "特殊的文章分类，不可修改");
        mockMvc.perform(put("/admin/pages/article-categories/"+catId)
                .header("Authorization",superAdmin)
                .param("name", "会员特权")
                .param("parent_id", "0"))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //正确修改
        mockMvc.perform(put("/admin/pages/article-categories/"+cat.getId())
                .header("Authorization",superAdmin)
                .param("name", "会员特权1")
                .param("parent_id", ""+cat.getParentId()))
                .andExpect(status().is(200));
        //查询一个文章分类
        mockMvc.perform(get("/admin/pages/article-categories/"+cat.getId())
                .header("Authorization",superAdmin))
                .andExpect(status().is(200))
                .andExpect(jsonPath("name").value("会员特权1"));
    }


    /**
     * 删除文章分类
     *
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {

        //特殊的文章分类不可删除
        ArticleCategory category = new ArticleCategory();
        category.setName("");
        category.setType(ArticleCategoryType.HELP.name());
        category.setAllowDelete(0);
        category.setParentId(0);
        this.daoSupport.insert(category);
        int catId = this.daoSupport.getLastId("");
        ErrorMessage error = new ErrorMessage("950", "特殊的文章分类，不可删除");
        mockMvc.perform(delete("/admin/pages/article-categories/"+catId)
                .header("Authorization",superAdmin))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

        //正确删除
        String res = mockMvc.perform(post("/admin/pages/article-categories")
                .header("Authorization",superAdmin)
                .param("name", "父分类")
                .param("parent_id", "0"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        ArticleCategory cat = JsonUtil.jsonToObject(res,ArticleCategory.class);
        mockMvc.perform(delete("/admin/pages/article-categories/"+cat.getId())
                .header("Authorization",superAdmin))
                .andExpect(status().is(200));

    }

}
