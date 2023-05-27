package com.enation.app.javashop.core.member.service;

import com.enation.app.javashop.core.member.model.dos.AskReplyDO;
import com.enation.app.javashop.core.member.model.dto.ReplyQueryParam;
import com.enation.app.javashop.core.member.model.vo.BatchAuditVO;
import com.enation.app.javashop.framework.database.Page;

/**
 * 会员回复商品咨询业务接口
 *
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-09-16
 */
public interface AskReplyManager {

    /**
     * 查询会员问题咨询回复列表
     * 针对获取某条会员咨询的回复
     * @param param 查询条件
     * @return Page
     */
    Page list(ReplyQueryParam param);

    /**
     * 获取会员问题咨询回复列表
     * 针对获取会员所有的回复
     * @param param 查询条件
     * @return Page
     */
    Page listMemberReply(ReplyQueryParam param);

    /**
     * 新增会员商品咨询回复
     * @param askReplyDO
     * @return
     */
    void add(AskReplyDO askReplyDO);

    /**
     * 修改会员商品咨询回复
     * @param askId 会员商品咨询ID
     * @param replyContent 回复内容
     * @param anonymous 是否匿名 YES:是，NO:否
     * @return
     */
    AskReplyDO updateReply(Integer askId, String replyContent, String anonymous);

    /**
     * 删除会员商品咨询回复
     * @param id 主键ID
     */
    void delete(Integer id);

    /**
     * 根据咨询id删除回复
     * @param askId 咨询id
     */
    void deleteByAskId(Integer askId);

    /**
     * 批量审核会员商品咨询回复
     * @param batchAuditVO
     */
    void batchAudit(BatchAuditVO batchAuditVO);

    /**
     * 根据回复id获取会员商品咨询回复
     * @param id 主键ID
     * @return
     */
    AskReplyDO getModel(Integer id);

    /**
     * 根据咨询id和会员id获取回复信息
     * @param askId 咨询id
     * @param memberId 会员id
     * @return
     */
    AskReplyDO getModel(Integer askId, Integer memberId);

    /**
     * 获取会员商品咨询最新一条回复
     * @param askId 会员商品咨询id
     * @return
     */
    AskReplyDO getNewestModel(Integer askId);
}
