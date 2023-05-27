package com.enation.app.javashop.core.member.model.vo;

import com.enation.app.javashop.core.member.model.dos.CommentReply;
import com.enation.app.javashop.core.member.model.dos.MemberComment;
import com.enation.app.javashop.core.member.model.dos.MemberShopScore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author fk
 * @version v2.0
 * @Description: 评论vo
 * @date 2018/5/31 5:03
 * @since v7.0.0
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CommentVO extends MemberComment{

    @ApiModelProperty(name = "images", value = "评论图片", required = false)
    private List<String> images;

    @ApiModelProperty(name = "reply", value = "评论回复", required = false)
    private CommentReply reply;

    @ApiModelProperty(name = "additional_comment", value = "评论追评信息", required = false)
    private CommentVO additionalComment;

    @ApiModelProperty(name = "goods_shop_score", value = "店铺评分信息", required = false)
    private MemberShopScore memberShopScore;

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public CommentReply getReply() {
        return reply;
    }

    public void setReply(CommentReply reply) {
        this.reply = reply;
    }

    public CommentVO getAdditionalComment() {
        return additionalComment;
    }

    public void setAdditionalComment(CommentVO additionalComment) {
        this.additionalComment = additionalComment;
    }

    public MemberShopScore getMemberShopScore() {
        return memberShopScore;
    }

    public void setMemberShopScore(MemberShopScore memberShopScore) {
        this.memberShopScore = memberShopScore;
    }

    @Override
    public String toString() {
        return "CommentVO{" +
                "images=" + images +
                ", reply=" + reply +
                ", additionalComment=" + additionalComment +
                ", memberShopScore=" + memberShopScore +
                '}';
    }
}
