package com.enation.app.javashop.core.shop.model.dos;

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
 * 店铺站内消息实体
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-07-10 10:21:45
 */
@Table(name="es_shop_notice_log")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShopNoticeLogDO implements Serializable {
			
    private static final long serialVersionUID = 7865920545802873L;
    
    /**id*/
    @Id(name = "id")
    @ApiModelProperty(hidden=true)
    private Integer id;
    /**店铺ID*/
    @Column(name = "shop_id")
    @ApiModelProperty(name="shop_id",value="店铺ID",required=false)
    private Integer shopId;
    /**站内信内容*/
    @Column(name = "notice_content")
    @ApiModelProperty(name="notice_content",value="站内信内容",required=false)
    private String noticeContent;
    /**发送时间*/
    @Column(name = "send_time")
    @ApiModelProperty(name="send_time",value="发送时间",required=false)
    private Long sendTime;
    /**是否删除 ：1 删除   0  未删除*/
    @Column(name = "is_delete")
    @ApiModelProperty(name="is_delete",value="是否删除 ：1 删除   0  未删除",required=false)
    private Integer isDelete;
    /**是否已读 ：1已读   0 未读*/
    @Column(name = "is_read")
    @ApiModelProperty(name="is_read",value="是否已读 ：1已读   0 未读",required=false)
    private Integer isRead;
    /**消息类型*/
    @Column(name = "type")
    @ApiModelProperty(name="type",value="消息类型",required=false)
    private String type;

    @PrimaryKeyField
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShopId() {
        return shopId;
    }
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getNoticeContent() {
        return noticeContent;
    }
    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public Long getSendTime() {
        return sendTime;
    }
    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getIsDelete() {
        return isDelete;
    }
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getIsRead() {
        return isRead;
    }
    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }


	@Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ShopNoticeLogDO that = (ShopNoticeLogDO) o;
        if (id != null ? !id.equals(that.id) : that.id != null) {return false;}
        if (shopId != null ? !shopId.equals(that.shopId) : that.shopId != null) {return false;}
        if (noticeContent != null ? !noticeContent.equals(that.noticeContent) : that.noticeContent != null) {return false;}
        if (sendTime != null ? !sendTime.equals(that.sendTime) : that.sendTime != null) {return false;}
        if (isDelete != null ? !isDelete.equals(that.isDelete) : that.isDelete != null) {return false;}
        if (isRead != null ? !isRead.equals(that.isRead) : that.isRead != null) {return false;}
        return type != null ? type.equals(that.type) : that.type == null;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (shopId != null ? shopId.hashCode() : 0);
        result = 31 * result + (noticeContent != null ? noticeContent.hashCode() : 0);
        result = 31 * result + (sendTime != null ? sendTime.hashCode() : 0);
        result = 31 * result + (isDelete != null ? isDelete.hashCode() : 0);
        result = 31 * result + (isRead != null ? isRead.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ShopNoticeLogDO{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", noticeContent='" + noticeContent + '\'' +
                ", sendTime=" + sendTime +
                ", isDelete=" + isDelete +
                ", isRead=" + isRead +
                ", type='" + type + '\'' +
                '}';
    }

	
}