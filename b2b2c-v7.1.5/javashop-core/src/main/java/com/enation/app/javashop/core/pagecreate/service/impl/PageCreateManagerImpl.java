package com.enation.app.javashop.core.pagecreate.service.impl;

import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.pagecreate.service.PageCreateManager;
import com.enation.app.javashop.core.system.progress.model.ProgressEnum;
import com.enation.app.javashop.core.system.progress.model.TaskProgress;
import com.enation.app.javashop.core.system.progress.model.TaskProgressConstant;
import com.enation.app.javashop.core.system.progress.service.ProgressManager;
import org.apache.commons.logging.Log;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 静态页生成实现
 *
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年9月1日 上午11:51:09
 */
@Component
public class PageCreateManagerImpl implements PageCreateManager {

    protected final Log logger = org.apache.commons.logging.LogFactory.getLog(this.getClass());

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private ProgressManager progressManager;

    @Override
    public boolean startCreate(String[] choosePages) {

        TaskProgress taskProgress =  progressManager.getProgress(TaskProgressConstant.PAGE_CREATE);
        if ( taskProgress!= null) {
            //如果任务已经完成，返回可以执行
            return taskProgress.getTaskStatus().equals(ProgressEnum.SUCCESS.getStatus());
        }
        this.sendPageCreateMessage(choosePages);
        return true;
    }


    /**
     * 发送页面生成消息
     *
     * @param choosePages 要发送的对象 要生成的页面
     */
    public void sendPageCreateMessage(String[] choosePages) {
        try {
            this.amqpTemplate.convertAndSend(AmqpExchange.PAGE_CREATE, AmqpExchange.PAGE_CREATE+"_ROUTING", choosePages);
        } catch (Exception e) {
            logger.error(e);
        }
    }


}
