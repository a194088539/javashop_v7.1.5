package com.enation.app.javashop.core.goodssearch.service.impl;

import com.enation.app.javashop.core.goods.GoodsErrorCode;
import com.enation.app.javashop.core.goodssearch.model.SearchKeywordDO;
import com.enation.app.javashop.core.goodssearch.service.SearchKeywordManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
* @author liuyulei
 * @version 1.0
 * @Description:  搜索关键词业务实现类
 * @date 2019/5/27 11:14
 * @since v7.0
 */
@Service
public class SearchKeywordManagerImpl implements SearchKeywordManager {

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport goodsDaoSupport;

    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void add(String keyword) {
        if (StringUtil.isEmpty(keyword)) {
            throw new ServiceException(GoodsErrorCode.E310.code(), "关键字不能为空！");
        }
        SearchKeywordDO searchKeywordDO = new SearchKeywordDO(keyword);
        this.goodsDaoSupport.insert(searchKeywordDO);

    }

    @Override
    public Page list(Integer pageNo, Integer pageSize, String keyword) {
        StringBuffer sql = new StringBuffer("select * from es_keyword_search_history where count > 0 ");

        List term = new ArrayList();

        if (!StringUtil.isEmpty(keyword)) {
            sql.append(" and keyword like ? ");
            term.add("%"+keyword+"%");
        }
        sql.append(" order by count desc ");
        return this.goodsDaoSupport.queryForPage(sql.toString(), pageNo, pageSize, term.toArray());

    }

    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void update(String keyword) {
        if (StringUtil.isEmpty(keyword)) {
            return;
        }
        String sql = "update es_keyword_search_history set count = count+1,modify_time = ? where keyword = ? ";
        this.goodsDaoSupport.execute(sql, DateUtil.getDateline(), keyword);
    }

    @Override
    public boolean isExist(String keyword) {
        int count = this.goodsDaoSupport.queryForInt("select count(1) from es_keyword_search_history where keyword = ? ",keyword);
        if(count > 0){
            return true;
        }
        return false;
    }
}
