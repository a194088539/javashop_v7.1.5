package com.enation.app.javashop.consumer.shop.member;

import com.enation.app.javashop.consumer.core.event.MemberHistoryEvent;
import com.enation.app.javashop.core.client.member.MemberHistoryClient;
import com.enation.app.javashop.core.member.model.dto.HistoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 会员足迹
 *
 * @author zh
 * @version v7.0
 * @date 18/12/26 下午4:39
 * @since v7.0
 */
@Component
public class MemberHistoryConsumer implements MemberHistoryEvent {

    @Autowired
    private MemberHistoryClient memberHistoryClient;

    @Override
    public void addMemberHistory(HistoryDTO historyDTO) {
        memberHistoryClient.addMemberHistory(historyDTO);
    }
}
