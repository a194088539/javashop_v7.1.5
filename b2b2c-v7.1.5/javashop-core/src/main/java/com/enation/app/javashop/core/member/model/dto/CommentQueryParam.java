package com.enation.app.javashop.core.member.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 评论查询条件
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-05-03 10:38:00
 */
@ApiModel
public class CommentQueryParam {

    @ApiModelProperty(value = "页码", name = "page_no", required = true)
    private Integer pageNo;

    @ApiModelProperty(value = "分页数", name = "page_size", required = true)
    private Integer pageSize;

    @ApiModelProperty(value = "商品id", name = "goods_id")
    private Integer goodsId;

    @ApiModelProperty(value = "会员id", name = "member_id")
    private Integer memberId;

    @ApiModelProperty(value = "卖家id", name = "seller_id")
    private Integer sellerId;

    @ApiModelProperty(value = "模糊查询的关键字", name = "keyword")
    private String keyword;

    @ApiModelProperty(value = "评论内容", name = "content")
    private String content;

    @ApiModelProperty(value = "商品名称", name = "goods_name")
    private String goodsName;

    @ApiModelProperty(value = "会员名称", name = "member_name")
    private String memberName;

    @ApiModelProperty(value = "好中差评 good：好评，neutral：中评，bad：差评", name = "grade", allowableValues = "goods,neutral,bad")
    private String grade;

    @ApiModelProperty(value = "回复状态 1：已回复，0：未回复", name = "reply_status", allowableValues = "0,1")
    private Integer replyStatus;

    @ApiModelProperty(value = "审核状态 WAIT_AUDIT：待审核，PASS_AUDIT：审核通过，REFUSE_AUDIT：审核未通过", name = "audit_status", allowableValues = "WAIT_AUDIT,PASS_AUDIT,REFUSE_AUDIT")
    private String auditStatus;

    @ApiModelProperty(value = "评论类型 INITIAL初评，ADDITIONAL：追评", name = "comments_type", allowableValues = "INITIAL,ADDITIONAL")
    private String commentsType;

    @ApiModelProperty(value = "评论状态，待追评评论：WAIT_CHASE，已经完成评论：FINISHED", name = "comment_status", allowableValues = "WAIT_CHASE,FINISHED")
    private String commentStatus;

    @ApiModelProperty(value = "是否有图 1：有图，0：无图", name = "have_image", allowableValues = "0,1")
    private Integer haveImage;

    @ApiModelProperty(value = "是否含有追评 1：是，0：否", name = "have_additional", allowableValues = "0,1")
    private Integer haveAdditional;

    @ApiModelProperty(value = "评论日期--开始时间", name = "start_time")
    private Long startTime;

    @ApiModelProperty(value = "评论日期--结束时间", name = "end_time")
    private Long endTime;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Integer getReplyStatus() {
        return replyStatus;
    }

    public void setReplyStatus(Integer replyStatus) {
        this.replyStatus = replyStatus;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getCommentsType() {
        return commentsType;
    }

    public void setCommentsType(String commentsType) {
        this.commentsType = commentsType;
    }

    public String getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(String commentStatus) {
        this.commentStatus = commentStatus;
    }

    public Integer getHaveImage() {
        return haveImage;
    }

    public void setHaveImage(Integer haveImage) {
        this.haveImage = haveImage;
    }

    public Integer getHaveAdditional() {
        return haveAdditional;
    }

    public void setHaveAdditional(Integer haveAdditional) {
        this.haveAdditional = haveAdditional;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "CommentQueryParam{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", goodsId=" + goodsId +
                ", memberId=" + memberId +
                ", sellerId=" + sellerId +
                ", keyword='" + keyword + '\'' +
                ", content='" + content + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", memberName='" + memberName + '\'' +
                ", grade='" + grade + '\'' +
                ", replyStatus=" + replyStatus +
                ", auditStatus='" + auditStatus + '\'' +
                ", commentsType='" + commentsType + '\'' +
                ", commentStatus='" + commentStatus + '\'' +
                ", haveImage=" + haveImage +
                ", haveAdditional=" + haveAdditional +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
