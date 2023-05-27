package com.enation.app.javashop.manager.api.pagedata;

import com.enation.app.javashop.core.pagedata.model.Article;
import com.enation.app.javashop.core.pagedata.model.enums.ArticleShowPosition;
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
 * @Description: 文章单元测试
 * @date 2018/6/15 16:40
 * @since v7.0.0
 */
@Transactional(value = "systemTransactionManager", rollbackFor = Exception.class)
public class ArticleControllerTest  extends BaseTest {

    List<MultiValueMap<String, String>> list = null;

    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport daoSupport;

    @Before
    public void insertTestData() {

        String[] names = new String[]{"article_name", "category_id", "content", "message"};
        String[] values1 = new String[]{"", "1", "首页","文章名称不能为空"};
        String[] values2 = new String[]{"PC", "", "首页", "文章分类不能为空"};
        String[] values5 = new String[]{"PC", "1", "", "文章内容不能为空"};
        list = toMultiValueMaps(names, values2, values1, values5);

    }

    /**
     * 添加文章
     *
     * @throws Exception
     */
    @Test
    public void testAdd() throws Exception {

        // 必填验证
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/admin/pages/articles")
                    .header("Authorization",superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }

    }

    /**
     * 修改文章
     *
     * @throws Exception
     */
    @Test
    public void testEdit() throws Exception {

        Article article = this.add();

        // 必填验证
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(put("/admin/pages/articles/"+article.getArticleId())
                    .header("Authorization",superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        // 正确修改
        mockMvc.perform(put("/admin/pages/articles/"+article.getArticleId())
                .header("Authorization",superAdmin)
                .param("article_name", "文章名称1")
                .param("category_id", "1")
                .param("content", "文章内容"));
        // 查询
        mockMvc.perform(get("/admin/pages/articles/"+article.getArticleId())
                .header("Authorization",superAdmin))
                .andExpect(jsonPath("article_name").value("文章名称1"));
    }

    /**
     * 删除文章
     *
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {

        // 准备一个特殊的文章
        Article article = new Article();
        article.setArticleName("文章名称");
        article.setShowPosition(ArticleShowPosition.CONTACT_INFORMATION.name());
        this.daoSupport.insert(article);
        article.setArticleId(this.daoSupport.getLastId(""));
        // 特殊的文章不能删除或者不存在的文章
        ErrorMessage error = new ErrorMessage("952", "该文章不可删除，只可修改");
        mockMvc.perform(delete("/admin/pages/articles/"+article.getArticleId())
                .header("Authorization",superAdmin))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));
        //正确删除
        article = this.add();
        mockMvc.perform(delete("/admin/pages/articles/"+article.getArticleId())
                .header("Authorization",superAdmin))
                .andExpect(status().is(200));

    }

    /**
     * 添加一个文章
     * @return
     * @throws Exception
     */
    private Article add() throws Exception{

        String res = mockMvc.perform(post("/admin/pages/articles")
                .header("Authorization",superAdmin)
                .param("article_name", "文章名称")
                .param("category_id", "1")
                .param("content", "文章内容"))
                .andReturn().getResponse().getContentAsString();

        return JsonUtil.jsonToObject(res, Article.class);

    }


}
