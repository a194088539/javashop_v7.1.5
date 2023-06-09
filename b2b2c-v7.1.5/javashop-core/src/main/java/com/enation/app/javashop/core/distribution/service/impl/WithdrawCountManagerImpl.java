package com.enation.app.javashop.core.distribution.service.impl;

import com.enation.app.javashop.core.distribution.service.WithdrawCountManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.util.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * WithdrawCountManagerImpl
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-08-15 上午8:43
 */
@Service
public class WithdrawCountManagerImpl implements WithdrawCountManager {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    @Qualifier("distributionDaoSupport")
    private DaoSupport daoSupport;

    /**
     * 整理解冻金额
     */
    @Override
    public void withdrawCount() {
        try {
            Long currentData = DateUtil.getDateline();
            String sql = "UPDATE es_distribution distribution SET can_rebate = can_rebate + \n" +
                    "IFNULL(( SELECT ( IFNULL( ( SELECT sum(disorder.grade1_rebate) FROM es_distribution_order disorder WHERE member_id_lv1 = distribution.member_id AND disorder.is_withdraw = 0 and disorder.settle_cycle <? ), 0 )+ IFNULL( ( SELECT sum(disorder.grade2_rebate) FROM es_distribution_order disorder  WHERE member_id_lv2 = distribution.member_id AND disorder.is_withdraw = 0 and disorder.settle_cycle <? ), 0 ) ) ), 0 )\n" +
                    ",commission_frozen = commission_frozen-IFNULL(( SELECT ( IFNULL( ( SELECT sum(disorder.grade1_rebate) FROM es_distribution_order disorder WHERE member_id_lv1 = distribution.member_id AND disorder.is_withdraw = 0 and disorder.settle_cycle <? ), 0 )+ IFNULL( ( SELECT sum(disorder.grade2_rebate) FROM es_distribution_order disorder  WHERE member_id_lv2 = distribution.member_id AND disorder.is_withdraw = 0 and disorder.settle_cycle <? ), 0 ) ) ), 0 )\n";

            this.daoSupport.execute(sql, currentData, currentData, currentData, currentData);
            this.daoSupport.execute("update es_distribution_order set is_withdraw = 1 where settle_cycle <?", currentData);
        } catch (Exception e) {
            logger.error("每日将解锁金额自动添加到可提现金额异常：", e);
        }
    }
}
