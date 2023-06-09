package com.enation.app.javashop.core.member.service.impl;

import com.enation.app.javashop.framework.database.DaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.member.model.dos.MemberPointHistory;
import com.enation.app.javashop.core.member.service.MemberPointHistoryManager;

/**
 * 会员积分表业务类
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-04-03 15:44:12
 */
@Service
public class MemberPointHistoryManagerImpl implements MemberPointHistoryManager {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;

    @Override
    public Page list(int page, int pageSize, Integer memberId) {
        String sql = "select * from es_member_point_history  where member_id = ? and (grade_point>0 || consum_point>0) order by time desc";
        Page webPage = this.memberDaoSupport.queryForPage(sql, page, pageSize, MemberPointHistory.class, memberId);

        return webPage;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public MemberPointHistory add(MemberPointHistory memberPointHistory) {
        this.memberDaoSupport.insert(memberPointHistory);
        return memberPointHistory;
    }



}
