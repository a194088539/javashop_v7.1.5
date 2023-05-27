package com.enation.app.javashop.core.member.service.impl;

import com.enation.app.javashop.core.goods.model.enums.Permission;
import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.member.model.dos.CommentReply;
import com.enation.app.javashop.core.member.model.dos.MemberComment;
import com.enation.app.javashop.core.member.service.CommentReplyManager;
import com.enation.app.javashop.core.member.service.MemberCommentManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 评论回复业务类
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-05-03 16:34:50
 */
@Service
public class CommentReplyManagerImpl implements CommentReplyManager {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private MemberCommentManager memberCommentManager;

    @Override
    public Page list(int page, int pageSize) {

        String sql = "select * from es_comment_reply  ";
        Page webPage = this.daoSupport.queryForPage(sql, page, pageSize, CommentReply.class);

        return webPage;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {
        this.daoSupport.delete(CommentReply.class, id);
    }

    @Override
    public CommentReply getModel(Integer id) {
        return this.daoSupport.queryForObject(CommentReply.class, id);
    }

    @Override
    public CommentReply getReply(Integer commentId) {

        Integer count = this.daoSupport.queryForInt("select count(1) from es_comment_reply where comment_id = ?",commentId);

        if(count > 0 ){
            String sql = "select * from es_comment_reply where comment_id = ?  ";
            // 查询评论回复
            List<CommentReply> resList = this.daoSupport.queryForList(sql, CommentReply.class, commentId);

            //目前商城只支持单次回复，一次对话
            return resList.get(0);
        }



        return null;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CommentReply replyComment(Integer commentId, String reply, Permission permission) {

        Seller seller = UserContext.getSeller();
        MemberComment comment = this.memberCommentManager.getModel(commentId);
        //判断是否为商家，且评论属于当前登陆商家
        boolean flag = Permission.SELLER.equals(permission) && comment.getSellerId().equals(seller.getSellerId());
        if (comment == null || !flag) {
            throw new ServiceException(MemberErrorCode.E200.code(), "无权限回复");
        }
        //不能重复回复
        String sql = "select count(1) from es_comment_reply where comment_id = ? ";
        Integer count  = this.daoSupport.queryForInt(sql, commentId);
        if (count > 0) {
            throw new ServiceException(MemberErrorCode.E200.code(), "不能重复回复");
        }

        CommentReply commentReply = new CommentReply();
        commentReply.setCommentId(commentId);
        commentReply.setContent(reply);
        commentReply.setCreateTime(DateUtil.getDateline());
        commentReply.setRole(permission.name());
        //如果是初评，则回复为初评回复，如果是追评，则回复为追评回复
        commentReply.setReplyType(comment.getCommentsType());


        this.daoSupport.insert(commentReply);
        commentReply.setReplyId(this.daoSupport.getLastId("es_comment_reply"));
        //更改评论的状态为已回复
        sql = "update es_member_comment set reply_status = 1 where comment_id = ? ";
        this.daoSupport.execute(sql, commentId);
        return commentReply;
    }
}
