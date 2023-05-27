package com.enation.app.javashop.core.client.member;

import com.enation.app.javashop.core.member.model.dto.HistoryDTO;

/**
 * 会员足迹客户端
 *
 * @author zh
 * @version v7.0
 * @date 19/07/05 上午11:48
 * @since v7.0
 */

public interface MemberHistoryClient {
    /**
     * 添加会员历史足迹
     *
     * @param historyDTO 历史足迹dto
     */
    void addMemberHistory(HistoryDTO historyDTO);

}
