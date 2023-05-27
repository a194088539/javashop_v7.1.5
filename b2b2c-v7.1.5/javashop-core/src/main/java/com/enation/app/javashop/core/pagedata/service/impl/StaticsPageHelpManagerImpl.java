package com.enation.app.javashop.core.pagedata.service.impl;

import com.enation.app.javashop.core.pagedata.model.enums.ArticleShowPosition;
import com.enation.app.javashop.core.pagedata.service.StaticsPageHelpManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 静态页面实现
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-07-17 下午3:27
 */
@Service
public class StaticsPageHelpManagerImpl implements StaticsPageHelpManager {

    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport daoSupport;

    /**
     * 获取帮助页面总数
     * 不包含固定位置的文章
     *
     * @return
     */
    @Override
    public Integer count() {
        return this.daoSupport.queryForInt("select count(0) from es_article where show_position = ? ", ArticleShowPosition.OTHER.name());
    }

    /**
     * 分页获取帮助
     *
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List helpList(Integer page, Integer pageSize) {
        return this.daoSupport.queryForListPage("select article_id as id from es_article where show_position = ? ",page,pageSize,ArticleShowPosition.OTHER.name());
    }
}
