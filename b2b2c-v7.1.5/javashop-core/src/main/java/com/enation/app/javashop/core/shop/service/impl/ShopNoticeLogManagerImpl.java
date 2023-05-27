package com.enation.app.javashop.core.shop.service.impl;

import com.enation.app.javashop.core.shop.model.dos.ShopNoticeLogDO;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.util.SqlUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.shop.service.ShopNoticeLogManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 店铺站内消息业务类
 *
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-07-10 10:21:45
 */
@Service
public class ShopNoticeLogManagerImpl implements ShopNoticeLogManager {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport daoSupport;

    @Override
    public Page list(int page, int pageSize, String type, Integer isRead) {
        List<Object> term = new ArrayList<>();
        term.add(UserContext.getSeller().getSellerId());
        term.add(isRead);
        StringBuffer stringBuffer = new StringBuffer("select * from es_shop_notice_log where shop_id = ? and is_delete = 0 and is_read =? ");
        if (!StringUtil.isEmpty(type)) {
            stringBuffer.append(" and type = ?");
            term.add(type);
        }
        stringBuffer.append(" ORDER BY send_time DESC ");
        Page webPage = this.daoSupport.queryForPage(stringBuffer.toString(), page, pageSize, ShopNoticeLogDO.class, term.toArray());

        return webPage;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ShopNoticeLogDO add(ShopNoticeLogDO shopNoticeLog) {
        this.daoSupport.insert(shopNoticeLog);

        return shopNoticeLog;
    }


    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer[] ids) {
        List<Object> term = new ArrayList<>();
        String str = SqlUtil.getInSql(ids, term);
        term.add(UserContext.getSeller().getSellerId());
        String sql = "update es_shop_notice_log set is_delete = 1 where id IN (" + str + ") and shop_id = ?";
        daoSupport.execute(sql, term.toArray());
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void read(Integer[] ids) {
        List<Object> term = new ArrayList<>();
        String str = SqlUtil.getInSql(ids, term);
        term.add(UserContext.getSeller().getSellerId());
        String sql = "update es_shop_notice_log set is_read = 1 where id IN (" + str + ") and shop_id = ?";
        daoSupport.execute(sql, term.toArray());
    }
}
