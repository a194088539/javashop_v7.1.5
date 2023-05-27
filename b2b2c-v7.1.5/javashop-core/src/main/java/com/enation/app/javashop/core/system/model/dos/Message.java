package com.enation.app.javashop.core.system.model.dos;

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
 * 站内消息实体
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-07-04 21:50:52
 */
@Table(name = "es_message")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Message implements Serializable {

    private static final long serialVersionUID = 8197127057448115L;

    /**
     * 站内消息主键
     */
    @Id(name = "id")
    @ApiModelProperty(hidden = true)
    private Integer id;
    /**
     * 标题
     */
    @Column(name = "title")
    @ApiModelProperty(name = "title", value = "标题", required = false)
    private String title;
    /**
     * 消息内容
     */
    @Column(name = "content")
    @ApiModelProperty(name = "content", value = "消息内容", required = false)
    private String content;
    /**
     * 会员id
     */
    @Column(name = "member_ids")
    @ApiModelProperty(name = "member_ids", value = "会员id", required = false)
    private String memberIds;
    /**
     * 管理员id
     */
    @Column(name = "admin_id")
    @ApiModelProperty(name = "admin_id", value = "管理员id", required = false)
    private Integer adminId;
    /**
     * 管理员名称
     */
    @Column(name = "admin_name")
    @ApiModelProperty(name = "admin_name", value = "管理员名称", required = false)
    private String adminName;
    /**
     * 发送时间
     */
    @Column(name = "send_time")
    @ApiModelProperty(name = "send_time", value = "发送时间", required = false)
    private Long sendTime;
    /**
     * 发送类型
     */
    @Column(name = "send_type")
    @ApiModelProperty(name = "send_type", value = "发送类型,0全站，1指定会员", required = false)
    private Integer sendType;
    /**
     * 是否删除 0：否，1：是
     */
    @Column(name = "disabled")
    @ApiModelProperty(name = "disabled", value = "是否删除 0：否，1：是", required = false)
    private Integer disabled;

    @PrimaryKeyField
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(String memberIds) {
        this.memberIds = memberIds;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        return Objects.equals(id, message.id) &&
                Objects.equals(title, message.title) &&
                Objects.equals(content, message.content) &&
                Objects.equals(memberIds, message.memberIds) &&
                Objects.equals(adminId, message.adminId) &&
                Objects.equals(adminName, message.adminName) &&
                Objects.equals(sendTime, message.sendTime) &&
                Objects.equals(sendType, message.sendType) &&
                Objects.equals(disabled, message.disabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, memberIds, adminId, adminName, sendTime, sendType, disabled);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", memberIds='" + memberIds + '\'' +
                ", adminId=" + adminId +
                ", adminName='" + adminName + '\'' +
                ", sendTime=" + sendTime +
                ", sendType=" + sendType +
                ", disabled=" + disabled +
                '}';
    }
}