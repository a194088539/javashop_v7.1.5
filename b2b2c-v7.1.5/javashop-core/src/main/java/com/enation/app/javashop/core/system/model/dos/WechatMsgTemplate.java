package com.enation.app.javashop.core.system.model.dos;

import java.io.Serializable;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 微信服务消息模板实体
 *
 * @author fk
 * @version v7.1.4
 * @since vv7.1.0
 * 2019-06-14 16:42:35
 */
@Table(name = "es_wechat_msg_template")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class WechatMsgTemplate implements Serializable {

    private static final long serialVersionUID = 5627454075445246L;

    /**
     * 主键
     */
    @Id(name = "id")
    @ApiModelProperty(hidden = true)
    private Integer id;
    /**
     * 模板名称
     */
    @Column(name = "msg_tmp_name")
    @ApiModelProperty(name = "msg_tmp_name", value = "模板名称", required = false)
    private String msgTmpName;
    /**
     * 消息编号
     */
    @Column(name = "msg_tmp_sn")
    @ApiModelProperty(name = "msg_tmp_sn", value = "消息编号", required = false)
    private String msgTmpSn;
    /**
     * 模板ID
     */
    @Column(name = "template_id")
    @ApiModelProperty(name = "template_id", value = "模板ID", required = false)
    private String templateId;
    /**
     * 消息开头文字
     */
    @Column(name = "msg_first")
    @ApiModelProperty(name = "msg_first", value = "消息开头文字", required = false)
    private String msgFirst;
    /**
     * 消息结尾备注文字
     */
    @Column(name = "msg_remark")
    @ApiModelProperty(name = "msg_remark", value = "消息结尾备注文字", required = false)
    private String msgRemark;
    /**
     * 是否开启
     */
    @Column(name = "is_open")
    @ApiModelProperty(name = "is_open", value = "是否开启", required = false)
    private Integer isOpen;
    /**
     * 模板类型，枚举
     */
    @Column(name = "tmp_type")
    @ApiModelProperty(name = "tmp_type", value = "模板类型，枚举", required = false)
    private String tmpType;

    @PrimaryKeyField
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMsgTmpName() {
        return msgTmpName;
    }

    public void setMsgTmpName(String msgTmpName) {
        this.msgTmpName = msgTmpName;
    }

    public String getMsgTmpSn() {
        return msgTmpSn;
    }

    public void setMsgTmpSn(String msgTmpSn) {
        this.msgTmpSn = msgTmpSn;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getMsgFirst() {
        return msgFirst;
    }

    public void setMsgFirst(String msgFirst) {
        this.msgFirst = msgFirst;
    }

    public String getMsgRemark() {
        return msgRemark;
    }

    public void setMsgRemark(String msgRemark) {
        this.msgRemark = msgRemark;
    }

    public Integer getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Integer isOpen) {
        this.isOpen = isOpen;
    }

    public String getTmpType() {
        return tmpType;
    }

    public void setTmpType(String tmpType) {
        this.tmpType = tmpType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WechatMsgTemplate that = (WechatMsgTemplate) o;
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (msgTmpName != null ? !msgTmpName.equals(that.msgTmpName) : that.msgTmpName != null) {
            return false;
        }
        if (msgTmpSn != null ? !msgTmpSn.equals(that.msgTmpSn) : that.msgTmpSn != null) {
            return false;
        }
        if (templateId != null ? !templateId.equals(that.templateId) : that.templateId != null) {
            return false;
        }
        if (msgFirst != null ? !msgFirst.equals(that.msgFirst) : that.msgFirst != null) {
            return false;
        }
        if (msgRemark != null ? !msgRemark.equals(that.msgRemark) : that.msgRemark != null) {
            return false;
        }
        if (isOpen != null ? !isOpen.equals(that.isOpen) : that.isOpen != null) {
            return false;
        }
        if (tmpType != null ? !tmpType.equals(that.tmpType) : that.tmpType != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (msgTmpName != null ? msgTmpName.hashCode() : 0);
        result = 31 * result + (msgTmpSn != null ? msgTmpSn.hashCode() : 0);
        result = 31 * result + (templateId != null ? templateId.hashCode() : 0);
        result = 31 * result + (msgFirst != null ? msgFirst.hashCode() : 0);
        result = 31 * result + (msgRemark != null ? msgRemark.hashCode() : 0);
        result = 31 * result + (isOpen != null ? isOpen.hashCode() : 0);
        result = 31 * result + (tmpType != null ? tmpType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WechatMsgTemplate{" +
                "id=" + id +
                ", msgTmpName='" + msgTmpName + '\'' +
                ", msgTmpSn='" + msgTmpSn + '\'' +
                ", templateId='" + templateId + '\'' +
                ", msgFirst='" + msgFirst + '\'' +
                ", msgRemark='" + msgRemark + '\'' +
                ", isOpen=" + isOpen +
                ", tmpType='" + tmpType + '\'' +
                '}';
    }


}