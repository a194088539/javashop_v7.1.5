package com.enation.app.javashop.core.redissub;

import com.enation.app.javashop.core.base.message.SensitiveWordsMsg;
import com.enation.app.javashop.core.base.redismq.RedisChannel;
import com.enation.app.javashop.core.system.sensitiveutil.SensitiveFilter;
import com.enation.app.javashop.framework.redis.redismq.RedisMsgReceiver;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * redis敏感词消费者
 *
 * @author fk
 * @version 2.0
 * @since 7.1.5
 * 2019-09-07 18：00
 */
@Component
@ConditionalOnMissingBean(name = "systemServiceApplication")
public class SensitiveReceiver implements RedisMsgReceiver {


    @Override
    public String getChannelName() {
        return RedisChannel.SENSITIVE_WORDS;
    }

    @Override
    public void receiveMsg(String message) {
        //此message是一个json，所以需要转换一下
        SensitiveWordsMsg msg = JsonUtil.jsonToObject(message,SensitiveWordsMsg.class);

        //添加操作
        if(SensitiveWordsMsg.ADD.equals(msg.getOperation())){

            SensitiveFilter.put(msg.getWord());
            return;
        }
        //删除操作
        if(SensitiveWordsMsg.DELETE.equals(msg.getOperation())){

            SensitiveFilter.remove(msg.getWord());
            return;
        }
    }
}
