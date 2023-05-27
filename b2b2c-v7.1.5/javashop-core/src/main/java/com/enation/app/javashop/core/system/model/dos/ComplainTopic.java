package com.enation.app.javashop.core.system.model;

import java.io.Serializable;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;


/**
 * 投诉主题实体
 *
 * @author fk
 * @version v2.0
 * @since v2.0
 * 2019-11-26 16:06:44
 */
@Table(name = "es_complain_topic")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ComplainTopic implements Serializable {

    private static final long serialVersionUID = 361813636114391L;

    /**
     * 主键
     */
    @Id(name = "topic_id")
    @ApiModelProperty(hidden = true)
    private Integer topicId;
    /**
     * 投诉主题
     */
    @Column(name = "topic_name")
    @ApiModelProperty(name = "topic_name", value = "投诉主题", required = true)
    @Length(min = 2,max = 30,message = "投诉主题2-30个字符")
    private String topicName;
    /**
     * 添加时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name = "create_time", value = "添加时间", hidden = true)
    private Long createTime;
    /**
     * 主题说明
     */
    @Column(name = "topic_remark")
    @ApiModelProperty(name = "topic_remark", value = "主题说明", required = true)
    @Length(min = 1,max = 100,message = "主题描述1-100个字符")
    private String topicRemark;

    @PrimaryKeyField
    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getTopicRemark() {
        return topicRemark;
    }

    public void setTopicRemark(String topicRemark) {
        this.topicRemark = topicRemark;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplainTopic that = (ComplainTopic) o;
        if (topicId != null ? !topicId.equals(that.topicId) : that.topicId != null) {
            return false;
        }
        if (topicName != null ? !topicName.equals(that.topicName) : that.topicName != null) {
            return false;
        }
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) {
            return false;
        }
        if (topicRemark != null ? !topicRemark.equals(that.topicRemark) : that.topicRemark != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (topicId != null ? topicId.hashCode() : 0);
        result = 31 * result + (topicName != null ? topicName.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (topicRemark != null ? topicRemark.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ComplainTopic{" +
                "topicId=" + topicId +
                ", topicName='" + topicName + '\'' +
                ", createTime=" + createTime +
                ", topicRemark='" + topicRemark + '\'' +
                '}';
    }


}