package com.enation.app.javashop.consumer.job.execute.impl;

import com.enation.app.javashop.consumer.job.execute.EveryDayExecute;
import com.enation.app.javashop.core.client.statistics.SyncopateTableClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 分表任务每日执行
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-07-16 下午4:17
 */
@Component
public class SyncopateTableJob implements EveryDayExecute {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private SyncopateTableClient syncopateTableClient;

    /**
     * 每年执行
     */
    @Override
    public void everyDay() {
        try {
            syncopateTableClient.everyDay();
        } catch (Exception e) {
            logger.error("分表业务执行异常：", e);
        }

    }
}
