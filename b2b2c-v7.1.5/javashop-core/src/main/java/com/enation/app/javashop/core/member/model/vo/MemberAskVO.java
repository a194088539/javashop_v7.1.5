package com.enation.app.javashop.core.member.model.vo;

import com.enation.app.javashop.core.member.model.dos.AskReplyDO;
import com.enation.app.javashop.core.member.model.dos.MemberAsk;
import io.swagger.annotations.ApiModelProperty;

/**
 * 会员问题咨询对象vo
 *
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-09-16
 */
public class MemberAskVO extends MemberAsk {

    @ApiModelProperty(name = "goods_price", value = "商品价格")
    private Double goodsPrice;

    @ApiModelProperty(name = "praise_rate", value = "商品好评率")
    private Double praiseRate;

    @ApiModelProperty(name = "comment_num", value = "商品评论数量")
    private Integer commentNum;

    @ApiModelProperty(name = "first_reply", value = "会员商品咨询首条回复")
    private AskReplyDO firstReply;

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Double getPraiseRate() {
        return praiseRate;
    }

    public void setPraiseRate(Double praiseRate) {
        this.praiseRate = praiseRate;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public AskReplyDO getFirstReply() {
        return firstReply;
    }

    public void setFirstReply(AskReplyDO firstReply) {
        this.firstReply = firstReply;
    }

    @Override
    public String toString() {
        return "MemberAskVO{" +
                "goodsPrice=" + goodsPrice +
                ", praiseRate=" + praiseRate +
                ", commentNum=" + commentNum +
                ", firstReply=" + firstReply +
                '}';
    }
}
