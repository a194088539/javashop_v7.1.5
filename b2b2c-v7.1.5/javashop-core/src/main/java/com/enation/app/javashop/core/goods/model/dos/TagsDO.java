package com.enation.app.javashop.core.goods.model.dos;

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
 * 商品标签实体
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-03-28 14:49:36
 */
@Table(name="es_tags")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TagsDO implements Serializable {
			
    private static final long serialVersionUID = 1899720595535600L;
    
    /**主键*/
    @Id(name = "tag_id")
    @ApiModelProperty(hidden=true)
    private Integer tagId;
    /**标签名字*/
    @Column(name = "tag_name")
    @ApiModelProperty(name="tag_name",value="标签名字",required=false)
    private String tagName;
    /**所属卖家*/
    @Column(name = "seller_id")
    @ApiModelProperty(name="seller_id",value="所属卖家",required=false)
    private Integer sellerId;
    /**关键字*/
    @Column(name = "mark")
    @ApiModelProperty(name="mark",value="关键字",required=false)
    private String mark;

    
    public TagsDO() {}
    
    public TagsDO(String tagName, Integer sellerId, String mark) {
		super();
		this.tagName = tagName;
		this.sellerId = sellerId;
		this.mark = mark;
	}



	@PrimaryKeyField
    public Integer getTagId() {
        return tagId;
    }
    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Integer getSellerId() {
        return sellerId;
    }
    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getMark() {
        return mark;
    }
    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    public String toString() {
        return "TagsDO{" +
                "tagId=" + tagId +
                ", tagName='" + tagName + '\'' +
                ", sellerId=" + sellerId +
                ", mark='" + mark + '\'' +
                '}';
    }
}