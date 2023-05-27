package com.enation.app.javashop.core.system.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 消息搜索参数实体
 *
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-12-06
 */
@ApiIgnore
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MessageQueryParam {

    @ApiModelProperty(value = "页码", name = "page_no")
    private Integer pageNo;

    @ApiModelProperty(value = "分页数", name = "page_size")
    private Integer pageSize;

    @ApiModelProperty(value = "模糊查询的关键字", name = "keyword")
    private String keyword;

    @ApiModelProperty(value = "消息标题", name = "title")
    private String title;

    @ApiModelProperty(value = "消息内容", name = "content")
    private String content;

    @ApiModelProperty(value = "发送类型", name = "send_type")
    private Integer sendType;

    @ApiModelProperty(value = "开始时间", name = "start_time")
    private Long startTime;

    @ApiModelProperty(value = "起止时间", name = "end_time")
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
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
        return "MessageQueryParam{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", keyword='" + keyword + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", sendType=" + sendType +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}