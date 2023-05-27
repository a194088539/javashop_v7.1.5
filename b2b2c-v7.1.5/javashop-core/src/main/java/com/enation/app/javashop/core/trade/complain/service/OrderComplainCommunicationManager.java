package com.enation.app.javashop.core.trade.complain.service;

import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.trade.complain.model.dos.OrderComplainCommunication;

import java.util.List;

/**
 * 交易投诉对话表业务层
 *
 * @author fk
 * @version v2.0
 * @since v2.0
 * 2019-11-29 10:46:34
 */
public interface OrderComplainCommunicationManager {

    /**
     * 查询交易投诉对话表列表
     *
     * @param complainId   交易投诉id
     * @return Page
     */
    List<OrderComplainCommunication> list(int complainId);

    /**
     * 添加交易投诉对话表
     *
     * @param orderComplainCommunication 交易投诉对话表
     * @return OrderComplainCommunication 交易投诉对话表
     */
    OrderComplainCommunication add(OrderComplainCommunication orderComplainCommunication);

}