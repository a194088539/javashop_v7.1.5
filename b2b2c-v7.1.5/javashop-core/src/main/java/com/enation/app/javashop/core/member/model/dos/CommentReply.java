package com.enation.app.javashop.core.member.model.dos;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;


/**
 * 评论回复实体
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-05-03 16:34:50
 */
@Table(name = "es_comment_reply")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CommentReply implements Serializable {

    private static final long serialVersionUID = 8995158403058181L;

    /**
     * 主键
     */
    @Id(name = "reply_id")
    @ApiModelProperty(hidden = true)
    private Integer replyId;
    /**
     * 评论id
     */
    @Column(name = "comment_id")
    @ApiModelProperty(name = "comment_id", value = "评论id", required = false)
    private Integer commentId;

    /**
     * 回复内容
     */
    @Column(name = "content")
    @ApiModelProperty(name = "content", value = "回复内容", required = false)
    private String content;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name = "create_time", value = "创建时间", required = false)
    private Long createTime;
    /**
     * 商家或者买家
     */
    @Column(name = "role")
    @ApiModelProperty(name = "role", value = "商家或者买家", required = false)
    private String role;
    /**
     * 父子路径0|10|
     */
    @Column(name = "path")
    @ApiModelProperty(name = "path", value = "父子路径0|10|", required = false)
    private String path;

    @Column(name = "reply_type")
    @ApiModelProperty(name = "reply_type", value = "回复类型 :初评(INITIAL),追评(ADDITIONAL)", required = false)
    private String replyType;


    @PrimaryKeyField
    public Integer getReplyId() {
        return replyId;
    }

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getReplyType() {
        return replyType;
    }

    public void setReplyType(String replyType) {
        this.replyType = replyType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommentReply that = (CommentReply) o;

        return new EqualsBuilder()
                .append(replyId, that.replyId)
                .append(commentId, that.commentId)
                .append(content, that.content)
                .append(createTime, that.createTime)
                .append(role, that.role)
                .append(path, that.path)
                .append(replyType, that.replyType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(replyId)
                .append(commentId)
                .append(content)
                .append(createTime)
                .append(role)
                .append(path)
                .append(replyType)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "CommentReply{" +
                "replyId=" + replyId +
                ", commentId=" + commentId +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", role='" + role + '\'' +
                ", path='" + path + '\'' +
                '}';
    }


}