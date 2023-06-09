package com.enation.app.javashop.core.member.service;

import com.enation.app.javashop.core.goods.model.enums.Permission;
import com.enation.app.javashop.core.member.model.dos.MemberComment;
import com.enation.app.javashop.core.member.model.dto.AdditionalCommentDTO;
import com.enation.app.javashop.core.member.model.dto.CommentQueryParam;
import com.enation.app.javashop.core.member.model.dto.CommentScoreDTO;
import com.enation.app.javashop.core.member.model.vo.BatchAuditVO;
import com.enation.app.javashop.core.member.model.vo.CommentVO;
import com.enation.app.javashop.core.member.model.vo.GoodsGrade;
import com.enation.app.javashop.core.member.model.vo.MemberCommentCount;
import com.enation.app.javashop.core.trade.sdk.model.OrderDetailDTO;
import com.enation.app.javashop.framework.database.Page;

import java.util.List;

/**
 * 评论业务层
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-05-03 10:19:14
 */
public interface MemberCommentManager {

    /**
     * 查询评论列表
     *
     * @param param 条件
     * @return Page
     */
    Page list(CommentQueryParam param);

    /**
     * 添加评论
     *
     * @param comment
     * @param permission
     * @return
     */
    MemberComment add(CommentScoreDTO comment, Permission permission);

    /**
     * 修改评论
     *
     * @param memberComment 评论
     * @param id            评论主键
     * @return MemberComment 评论
     */
    MemberComment edit(MemberComment memberComment, Integer id);

    /**
     * 删除评论
     *
     * @param id 评论主键
     */
    void delete(Integer id);

    /**
     * 获取评论
     *
     * @param id 评论主键
     * @return MemberComment  评论
     */
    MemberComment getModel(Integer id);

    /**
     * 查询商品的好评比例
     *
     * @return
     */
    List<GoodsGrade> queryGoodsGrade();

    /**
     * 根据商品id获取评论数
     *
     * @param goodsId 商品id
     * @return 评论数
     */
    Integer getGoodsCommentCount(Integer goodsId);

    /**
     * 自动好评
     */
    void autoGoodComments(List<OrderDetailDTO> detailDTOList);

    /**
     * 查询某个商品的相关评论数量
     *
     * @param goodsId
     * @return
     */
    MemberCommentCount count(Integer goodsId);

    /**
     * 根据会员id修改头像信息
     *
     * @param memberId 会员id
     * @param face     头像
     */
    void editComment(Integer memberId, String face);

    /**
     * 会员追加评论
     * @param comments
     * @param permission
     * @return
     */
    List<AdditionalCommentDTO> additionalComments(List<AdditionalCommentDTO> comments, Permission permission);

    /**
     * 根据会员评论id获取追评信息
     * @param commentId
     * @return
     */
    MemberComment getAdditionalById(Integer commentId);

    /**
     * 批量审核会员商品评论
     * @param batchAuditVO
     */
    void batchAudit(BatchAuditVO batchAuditVO);

    /**
     * 获取评论详情
     * @param commentId
     * @return
     */
    CommentVO get(Integer commentId);

    /**
     * 根据商品编号和订单编号查询评论详情
     * @param orderSn
     * @param skuId
     * @return
     */
    List<CommentVO> get(String orderSn,Integer skuId);

}
