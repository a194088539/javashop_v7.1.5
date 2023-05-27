package com.enation.app.javashop.core.member.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 会员商品咨询回复搜索参数实体
 *
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-09-17
 */
@ApiIgnore
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReplyQueryParam {

    @ApiModelProperty(value = "页码", name = "page_no", required = true)
    private Integer pageNo;

    @ApiModelProperty(value = "分页数", name = "page_size", required = true)
    private Integer pageSize;

    @ApiModelProperty(value = "会员商品咨询ID", name = "ask_id", required = true)
    private Integer askId;

    @ApiModelProperty(value = "模糊查询的关键字", name = "keyword")
    private String keyword;

    @ApiModelProperty(value = "会员名称", name = "member_name")
    private String memberName;

    @ApiModelProperty(value = "咨询内容", name = "content")
    private String content;

    @ApiModelProperty(value = "审核状态 WAIT_AUDIT:待审核,PASS_AUDIT:审核通过,REFUSE_AUDIT:审核未通过", name = "auth_status", allowableValues = "WAIT_AUDIT,PASS_AUDIT,REFUSE_AUDIT")
    private String authStatus;

    @ApiModelProperty(value = "咨询时间-起始时间", name = "start_time")
    private Long startTime;

    @ApiModelProperty(value = "咨询时间-结束时间", name = "end_time")
    private Long endTime;

    @ApiModelProperty(value = "是否匿名 YES:是，NO:否", name = "anonymous", allowableValues = "YES,NO")
    private String anonymous;

    @ApiModelProperty(value = "会员商品咨询回复id", name = "reply_id")
    private Integer replyId;

    @ApiModelProperty(value = "会员id", name = "member_id")
    private Integer memberId;

    @ApiModelProperty(value = "是否已回复 YES:是，NO:否", name = "reply_status", allowableValues = "YES,NO")
    private String replyStatus;

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

    public Integer getAskId() {
        return askId;
    }

    public void setAskId(Integer askId) {
        this.askId = askId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
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

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }

    public Integer getReplyId() {
        return replyId;
    }

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getReplyStatus() {
        return replyStatus;
    }

    public void setReplyStatus(String replyStatus) {
        this.replyStatus = replyStatus;
    }

    @Override
    public String toString() {
        return "ReplyQueryParam{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", askId=" + askId +
                ", keyword='" + keyword + '\'' +
                ", memberName='" + memberName + '\'' +
                ", content='" + content + '\'' +
                ", authStatus='" + authStatus + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", anonymous='" + anonymous + '\'' +
                ", replyId=" + replyId +
                ", memberId=" + memberId +
                ", replyStatus='" + replyStatus + '\'' +
                '}';
    }
}
