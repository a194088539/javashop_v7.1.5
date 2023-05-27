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
 * 评论图片实体
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-05-03 14:11:46
 */
@Table(name="es_comment_gallery")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CommentGallery implements Serializable {
			
    private static final long serialVersionUID = 8048552718399969L;
    
    /**主键*/
    @Id(name = "img_id")
    @ApiModelProperty(hidden=true)
    private Integer imgId;
    /**主键*/
    @Column(name = "comment_id")
    @ApiModelProperty(name="comment_id",value="主键",required=false)
    private Integer commentId;
    /**图片路径*/
    @Column(name = "original")
    @ApiModelProperty(name="original",value="图片路径",required=false)
    private String original;
    /**排序*/
    @Column(name = "sort")
    @ApiModelProperty(name="sort",value="排序",required=false)
    private Integer sort;

    @PrimaryKeyField
    public Integer getImgId() {
        return imgId;
    }
    public void setImgId(Integer imgId) {
        this.imgId = imgId;
    }

    public Integer getCommentId() {
        return commentId;
    }
    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getOriginal() {
        return original;
    }
    public void setOriginal(String original) {
        this.original = original;
    }

    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }


    @Override
    public String toString() {
        return "CommentGallery{" +
                "imgId=" + imgId +
                ", commentId=" + commentId +
                ", original='" + original + '\'' +
                ", sort=" + sort +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommentGallery that = (CommentGallery) o;

        return new EqualsBuilder()
                .append(imgId, that.imgId)
                .append(commentId, that.commentId)
                .append(original, that.original)
                .append(sort, that.sort)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(imgId)
                .append(commentId)
                .append(original)
                .append(sort)
                .toHashCode();
    }
}