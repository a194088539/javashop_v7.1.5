package com.enation.app.javashop.consumer.job.execute.impl;

import com.enation.app.javashop.consumer.job.execute.EveryDayExecute;
import com.enation.app.javashop.core.client.distribution.WithdrawCountClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 可提现金额计算
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/23 上午7:46
 */
@Service
public class WithdrawCountJob implements EveryDayExecute{


    protected final Log logger = LogFactory.getLog(this.getClass());


    @Autowired
    private WithdrawCountClient withdrawCountClient;


    /**
     * 每天执行结算
     */
    @Override
    public void everyDay() {
        try {
            withdrawCountClient.everyDay();
        } catch (Exception e) {
            logger.error("每日将解锁金额自动添加到可提现金额异常：",e);
        }
    }
}
