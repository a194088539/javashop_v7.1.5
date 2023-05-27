package com.enation.app.javashop.core.member.model.dos;

import java.io.Serializable;
import java.util.Objects;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 会员问题咨询实体
 *
 * @author duanmingyu
 * @version v2.0
 * @since v7.1.5
 * 2019-09-16
 */
@Table(name = "es_member_ask")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberAsk implements Serializable {

    private static final long serialVersionUID = 1642694855238993L;

    /**
     * 主键
     */
    @Id(name = "ask_id")
    @ApiModelProperty(hidden = true)
    private Integer askId;
    /**
     * 商品id
     */
    @Column(name = "goods_id")
    @ApiModelProperty(name = "goods_id", value = "商品id", required = false)
    private Integer goodsId;
    /**
     * 会员id
     */
    @Column(name = "member_id")
    @ApiModelProperty(name = "member_id", value = "会员id", required = false)
    private Integer memberId;
    /**
     * 卖家id
     */
    @Column(name = "seller_id")
    @ApiModelProperty(name = "seller_id", value = "卖家id", required = false)
    private Integer sellerId;
    /**
     * 咨询内容
     */
    @Column(name = "content")
    @ApiModelProperty(name = "content", value = "咨询内容", required = false)
    private String content;
    /**
     * 咨询时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name = "create_time", value = "咨询时间", required = false)
    private Long createTime;
    /**
     * 商家回复内容
     */
    @Column(name = "reply")
    @ApiModelProperty(name = "reply", value = "商家回复内容", required = false)
    private String reply;
    /**
     * 商家回复时间
     */
    @Column(name = "reply_time")
    @ApiModelProperty(name = "reply_time", value = "商家回复时间", required = false)
    private Long replyTime;
    /**
     * 商家是否回复 YES：是，NO：否
     */
    @Column(name = "reply_status")
    @ApiModelProperty(name = "reply_status", value = "商家是否回复 YES：是，NO：否", required = false)
    private String replyStatus;
    /**
     * 删除状态 DELETED：已删除 NORMAL：正常
     */
    @Column(name = "status")
    @ApiModelProperty(name = "status", value = "删除状态 DELETED：已删除 NORMAL：正常", required = false)
    private String status;
    /**
     * 咨询人名称
     */
    @Column(name = "member_name")
    @ApiModelProperty(name = "member_name", value = "咨询人名称", required = false)
    private String memberName;
    /**
     * 会员头像
     */
    @Column(name = "member_face")
    @ApiModelProperty(name = "member_face", value = "会员头像", required = false)
    private String memberFace;
    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    @ApiModelProperty(name = "goods_name", value = "商品名称", required = false)
    private String goodsName;
    /**
     * 商品图片
     */
    @Column(name = "goods_img")
    @ApiModelProperty(name = "goods_img", value = "商品图片", required = false)
    private String goodsImg;
    /**
     * 审核状态 WAIT_AUDIT:待审核,PASS_AUDIT:审核通过,REFUSE_AUDIT:审核未通过
     */
    @Column(name = "auth_status")
    @ApiModelProperty(name = "auth_status", value = "审核状态 WAIT_AUDIT:待审核,PASS_AUDIT:审核通过,REFUSE_AUDIT:审核未通过", required = false)
    private String authStatus;
    /**
     * 是否匿名 YES:是，NO:否
     */
    @Column(name = "anonymous")
    @ApiModelProperty(name = "anonymous", value = "是否匿名 YES:是，NO:否", required = false)
    private String anonymous;
    /**
     * 会员问题咨询回复数量
     * 包含商家回复
     */
    @Column(name = "reply_num")
    @ApiModelProperty(name = "reply_num", value = "会员问题咨询回复数量", required = false)
    private Integer replyNum;

    @PrimaryKeyField
    public Integer getAskId() {
        return askId;
    }

    public void setAskId(Integer askId) {
        this.askId = askId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Long getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Long replyTime) {
        this.replyTime = replyTime;
    }

    public String getReplyStatus() {
        return replyStatus;
    }

    public void setReplyStatus(String replyStatus) {
        this.replyStatus = replyStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberFace() {
        return memberFace;
    }

    public void setMemberFace(String memberFace) {
        this.memberFace = memberFace;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }

    public Integer getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(Integer replyNum) {
        this.replyNum = replyNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberAsk memberAsk = (MemberAsk) o;
        return Objects.equals(askId, memberAsk.askId) &&
                Objects.equals(goodsId, memberAsk.goodsId) &&
                Objects.equals(memberId, memberAsk.memberId) &&
                Objects.equals(sellerId, memberAsk.sellerId) &&
                Objects.equals(content, memberAsk.content) &&
                Objects.equals(createTime, memberAsk.createTime) &&
                Objects.equals(reply, memberAsk.reply) &&
                Objects.equals(replyTime, memberAsk.replyTime) &&
                Objects.equals(replyStatus, memberAsk.replyStatus) &&
                Objects.equals(status, memberAsk.status) &&
                Objects.equals(memberName, memberAsk.memberName) &&
                Objects.equals(memberFace, memberAsk.memberFace) &&
                Objects.equals(goodsName, memberAsk.goodsName) &&
                Objects.equals(goodsImg, memberAsk.goodsImg) &&
                Objects.equals(authStatus, memberAsk.authStatus) &&
                Objects.equals(anonymous, memberAsk.anonymous) &&
                Objects.equals(replyNum, memberAsk.replyNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(askId, goodsId, memberId, sellerId, content, createTime, reply, replyTime, replyStatus, status, memberName, memberFace, goodsName, goodsImg, authStatus, anonymous, replyNum);
    }

    @Override
    public String toString() {
        return "MemberAsk{" +
                "askId=" + askId +
                ", goodsId=" + goodsId +
                ", memberId=" + memberId +
                ", sellerId=" + sellerId +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", reply='" + reply + '\'' +
                ", replyTime=" + replyTime +
                ", replyStatus='" + replyStatus + '\'' +
                ", status='" + status + '\'' +
                ", memberName='" + memberName + '\'' +
                ", memberFace='" + memberFace + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsImg='" + goodsImg + '\'' +
                ", authStatus='" + authStatus + '\'' +
                ", anonymous='" + anonymous + '\'' +
                ", replyNum=" + replyNum +
                '}';
    }
}