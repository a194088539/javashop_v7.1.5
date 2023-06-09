package com.enation.app.javashop.core.pagedata.service;

import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.pagedata.model.Article;

import java.util.List;

/**
 * 文章业务层
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-06-12 10:43:18
 */
public interface ArticleManager {

    /**
     * 查询文章列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @param name
     * @param categoryId
     * @return Page
     */
    Page list(int page, int pageSize, String name, Integer categoryId);

    /**
     * 添加文章
     *
     * @param article 文章
     * @return Article 文章
     */
    Article add(Article article);

    /**
     * 修改文章
     *
     * @param article 文章
     * @param id      文章主键
     * @return Article 文章
     */
    Article edit(Article article, Integer id);

    /**
     * 删除文章
     *
     * @param id 文章主键
     */
    void delete(Integer id);

    /**
     * 获取文章
     *
     * @param id 文章主键
     * @return Article  文章
     */
    Article getModel(Integer id);

    /**
     * 查询某位置的文章
     * @param position
     * @return
     */
    List<Article> listByPosition(String position);

    /**
     * 某分类类型下的文章
     * @param categoryType
     * @return
     */
    List<Article> listByCategoryType(String categoryType);
}