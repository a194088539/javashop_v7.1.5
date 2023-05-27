package com.enation.app.javashop.consumer.shop.goods;

import com.enation.app.javashop.consumer.core.event.GoodsCommentEvent;
import com.enation.app.javashop.core.base.message.GoodsCommentMsg;
import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.member.model.dos.MemberComment;
import com.enation.app.javashop.core.member.model.enums.CommentTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fk
 * @version v1.0
 * @Description: 更改商品的评论数量
 * @date 2018/6/25 10:23
 * @since v7.0.0
 */
@Service
public class GoodsCommentNumConsumer implements GoodsCommentEvent {

    @Autowired
    private GoodsClient goodsClient;

    @Override
    public void goodsComment(GoodsCommentMsg goodsCommentMsg) {

        if (goodsCommentMsg.getComment() == null || goodsCommentMsg.getComment().isEmpty()) {
            return;
        }

        //获取操作类型
        int operaType = goodsCommentMsg.getOperaType();
        //获取商品评论集合
        List<MemberComment> commentList = goodsCommentMsg.getComment();

        for (MemberComment comment : commentList) {
            //如果评论不为空并且是初评
            if (comment != null && CommentTypeEnum.INITIAL.name().equals(comment.getCommentsType())) {
                // 如果是新增评论操作则商品评论数量加1，如果是删除评论操作则商品评论数量减1
                if (GoodsCommentMsg.ADD == operaType) {
                    this.goodsClient.updateCommentCount(comment.getGoodsId(), 1);
                } else if (GoodsCommentMsg.DELETE == operaType) {
                    this.goodsClient.updateCommentCount(comment.getGoodsId(), -1);
                }
            }
        }
    }
}
