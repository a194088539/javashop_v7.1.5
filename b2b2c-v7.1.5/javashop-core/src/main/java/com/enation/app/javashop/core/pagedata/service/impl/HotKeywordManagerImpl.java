package com.enation.app.javashop.core.pagedata.service.impl;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.pagedata.model.HotKeyword;
import com.enation.app.javashop.core.pagedata.service.HotKeywordManager;
import com.enation.app.javashop.core.system.SystemErrorCode;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 热门关键字业务类
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-06-04 10:43:23
 */
@Service
public class HotKeywordManagerImpl implements HotKeywordManager {

    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport daoSupport;


    @Autowired
    private Cache cache;

    @Override
    public Page list(int page, int pageSize) {

        String sql = "select * from es_hot_keyword  ";
        Page webPage = this.daoSupport.queryForPage(sql, page, pageSize, HotKeyword.class);

        return webPage;
    }

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public HotKeyword add(HotKeyword hotKeyword) {
        this.daoSupport.insert(hotKeyword);
        hotKeyword.setId(this.daoSupport.getLastId(""));
        return hotKeyword;
    }

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public HotKeyword edit(HotKeyword hotKeyword, Integer id) {

        HotKeyword keyword = this.getModel(id);
        if (keyword == null) {
            throw new ServiceException(SystemErrorCode.E954.code(), "该记录不存在，请正确操作");
        }

        this.daoSupport.update(hotKeyword, id);

        return hotKeyword;
    }

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {
        this.daoSupport.delete(HotKeyword.class, id);
    }

    @Override
    public HotKeyword getModel(Integer id) {
        return this.daoSupport.queryForObject(HotKeyword.class, id);
    }

    @Override
    public List<HotKeyword> listByNum(Integer num) {
        List<HotKeyword> result = (List<HotKeyword>) cache.get(CachePrefix.HOT_KEYWORD.getPrefix() + num);

        if (result == null || result.isEmpty()) {
            if (num == null) {
                num = 5;
            }
            String sql = " select * from es_hot_keyword order by sort asc  limit 0,? ";
            result = this.daoSupport.queryForList(sql, HotKeyword.class, num);
            cache.put(CachePrefix.HOT_KEYWORD.getPrefix() + num, result);
        }

        return result;
    }
}
