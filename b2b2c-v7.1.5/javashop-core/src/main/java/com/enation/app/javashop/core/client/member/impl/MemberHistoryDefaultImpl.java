package com.enation.app.javashop.core.client.member.impl;

import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.member.MemberClient;
import com.enation.app.javashop.core.client.member.MemberHistoryClient;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.member.model.dos.HistoryDO;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.dto.HistoryDTO;
import com.enation.app.javashop.core.member.service.HistoryManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 会员历史
 *
 * @author zh
 * @version v7.0
 * @date 18/7/27 下午2:57
 * @since v7.0
 */

@Service
@ConditionalOnProperty(value = "javashop.product", havingValue = "stand")
public class MemberHistoryDefaultImpl implements MemberHistoryClient {

    @Autowired
    private HistoryManager historyManager;


    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addMemberHistory(HistoryDTO historyDTO) {
        historyManager.addMemberHistory(historyDTO);
    }


}
