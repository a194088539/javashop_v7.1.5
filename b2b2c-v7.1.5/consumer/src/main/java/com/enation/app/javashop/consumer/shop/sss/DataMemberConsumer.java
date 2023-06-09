package com.enation.app.javashop.consumer.shop.sss;

import com.enation.app.javashop.consumer.core.event.MemberRegisterEvent;
import com.enation.app.javashop.core.base.message.MemberRegisterMsg;
import com.enation.app.javashop.core.client.statistics.MemberDataClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员数据收集消费
 *
 * @author chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/6/20 下午2:20
 */
@Service
public class DataMemberConsumer implements MemberRegisterEvent {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private MemberDataClient memberDataClient;

    /**
     * 会员注册
     *
     * @param memberRegisterMsg
     */
    @Override
    public void memberRegister(MemberRegisterMsg memberRegisterMsg) {

        try {
            this.memberDataClient.register(memberRegisterMsg.getMember());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("会员注册消息异常:",e);
        }
    }
}
