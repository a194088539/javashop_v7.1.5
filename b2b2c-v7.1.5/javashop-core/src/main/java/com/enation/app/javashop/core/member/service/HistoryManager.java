package com.enation.app.javashop.core.member.service;

import com.enation.app.javashop.core.member.model.dos.HistoryDO;
import com.enation.app.javashop.core.member.model.dto.HistoryDTO;
import com.enation.app.javashop.core.member.model.dto.HistoryDelDTO;
import com.enation.app.javashop.framework.database.Page;

/**
 * 会员足迹业务层
 *
 * @author zh
 * @version v7.1.4
 * @since vv7.1
 * 2019-06-18 15:18:56
 */
public interface HistoryManager {

    /**
     * 查询会员足迹列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return Page
     */
    Page list(int page, int pageSize);

    /**
     * 修改会员足迹
     *
     * @param history 会员足迹
     * @param id      会员足迹主键
     * @return HistoryDO 会员足迹
     */
    HistoryDO edit(HistoryDO history, Integer id);

    /**
     * 删除会员足迹
     *
     * @param historyDelDTO 会员足迹主键
     */
    void delete(HistoryDelDTO historyDelDTO);

    /**
     * 删除100条以后的足迹信息
     * @param memberId
     */
    void delete(Integer memberId);

    /**
     * 获取会员足迹
     *
     * @param id 会员足迹主键
     * @return History  会员足迹
     */
    HistoryDO getModel(Integer id);

    /**
     * 根据商品和会员id查询此商品足迹信息
     *
     * @param goodsId  商品id
     * @param memberId 会员id
     * @return 会员历史足迹
     */
    HistoryDO getHistoryByGoods(Integer goodsId, Integer memberId);

    /**
     * 添加会员历史足迹
     *
     * @param historyDTO 历史足迹dto
     */
    void addMemberHistory(HistoryDTO historyDTO);

}