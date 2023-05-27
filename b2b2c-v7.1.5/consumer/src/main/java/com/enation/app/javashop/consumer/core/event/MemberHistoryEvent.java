package com.enation.app.javashop.consumer.core.event;

import com.enation.app.javashop.core.member.model.dto.HistoryDTO;

/**
 * 会员足迹事件
 *
 * @author zh
 * @version v2.0
 * @since v7.0.0
 * 2019年07月05日 上午10:24:31
 */
public interface MemberHistoryEvent {

    /**
     * 添加会员历史足迹
     *
     * @param historyDTO 会员历史足迹dto
     */
    void addMemberHistory(HistoryDTO historyDTO);
}
