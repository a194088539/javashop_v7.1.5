package com.enation.app.javashop.consumer.core.receiver;

import com.enation.app.javashop.consumer.job.execute.EveryYearExecute;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 每日执行调用
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-07-25 上午8:26
 */
public class EveryYearExecuteReceiver {
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired(required = false)
    private List<EveryYearExecute> everyYearExecutes;

    public void everyYear() {

        if (everyYearExecutes != null) {
            for (EveryYearExecute everyYearExecute : everyYearExecutes) {
                try {
                    everyYearExecute.everyYear();
                } catch (Exception e) {
                    logger.error("每年任务异常：", e);
                }

            }
        }


    }


}
