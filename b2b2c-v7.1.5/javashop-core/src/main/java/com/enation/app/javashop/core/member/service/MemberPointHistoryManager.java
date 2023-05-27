package com.enation.app.javashop.core.member.service;

import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.member.model.dos.MemberPointHistory;

/**
 * 会员积分表业务层
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-04-03 15:44:12
 */
public interface MemberPointHistoryManager {

    /**
     * 查询会员积分表列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @param memberId 会员id
     * @return Page
     */
    Page list(int page, int pageSize, Integer memberId);

    /**
     * 添加会员积分表
     *
     * @param memberPointHistory 会员积分表
     * @return MemberPointHistory 会员积分表
     */
    MemberPointHistory add(MemberPointHistory memberPointHistory);


}