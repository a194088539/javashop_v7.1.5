package com.enation.app.javashop.core.member.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 会员咨询搜索参数实体
 *
 * @author duanmingyu
 * @version v7.1.4
 * @since v7.0.0
 * 2019-12-06
 */
@ApiIgnore
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AskQueryParam {

    @ApiModelProperty(value = "页码", name = "page_no", required = true)
    private Integer pageNo;

    @ApiModelProperty(value = "分页数", name = "page_size", required = true)
    private Integer pageSize;

    @ApiModelProperty(value = "模糊查询的关键字", name = "keyword")
    private String keyword;

    @ApiModelProperty(value = "会员id", name = "member_id")
    private Integer memberId;

    @ApiModelProperty(value = "商家是否回复 YES：是，NO：否", name = "reply_status", allowableValues = "YES,NO")
    private String replyStatus;

    @ApiModelProperty(value = "卖家id", name = "seller_id")
    private Integer sellerId;

    @ApiModelProperty(value = "商品名称", name = "goods_name")
    private String goodsName;

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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
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

    @Override
    public String toString() {
        return "AskQueryParam{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", keyword='" + keyword + '\'' +
                ", memberId=" + memberId +
                ", replyStatus='" + replyStatus + '\'' +
                ", sellerId=" + sellerId +
                ", goodsName='" + goodsName + '\'' +
                ", memberName='" + memberName + '\'' +
                ", content='" + content + '\'' +
                ", authStatus='" + authStatus + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", anonymous='" + anonymous + '\'' +
                '}';
    }
}
