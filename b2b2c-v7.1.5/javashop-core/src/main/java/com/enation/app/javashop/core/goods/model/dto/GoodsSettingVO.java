package com.enation.app.javashop.core.goods.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author fk
 * @version v2.0
 * @Description: 商品相关设置vo
 * @date 2018/4/911:10
 * @since v7.0.0
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GoodsSettingVO implements Serializable{


    /**
     * 上架是否需要审核  1需要 0 不需要
     */
    @ApiModelProperty(name = "marcket_auth", value = "上架是否需要审核  1需要 0 不需要", required = true)
    @NotNull(message = "上架是否需要审核不能为空")
    @Min(value = 0,message = "上架是否需要审核值不正确")
    @Max(value = 1,message = "上架是否需要审核值不正确")
    private Integer marcketAuth;

    /**
     * 修改商品是否需要审核  1需要 0 不需要
     */
    @ApiModelProperty(name = "update_auth", value = "修改商品是否需要审核", required = true)
    @NotNull(message = "修改商品是否需要不能为空")
    @Min(value = 0,message = "修改商品是否需要审核值不正确")
    @Max(value = 1,message = "修改商品是否需要审核值不正确")
    private Integer updateAuth;

    /**
     * 商品评论是否需要审核  1需要 0 不需要
     */
    @ApiModelProperty(name = "comment_auth", value = "商品评论是否需要审核  1需要 0 不需要", required = true)
    @NotNull(message = "商品评论需要审核值不能为空")
    @Min(value = 0,message = "商品评论需要审核值不正确")
    @Max(value = 1,message = "商品评论需要审核值不正确")
    private Integer commentAuth;

    /**
     * 商品咨询是否需要审核  1需要 0 不需要
     */
    @ApiModelProperty(name = "ask_auth", value = "商品咨询是否需要审核", required = true)
    @NotNull(message = "商品咨询是否需要审核值不能为空")
    @Min(value = 0,message = "商品咨询是否需要审核值不正确")
    @Max(value = 1,message = "商品咨询是否需要审核值不正确")
    private Integer askAuth;

    @ApiModelProperty(name = "thumbnail_width", value = "缩略图宽度", required = true)
    @NotNull(message = "缩略图宽度不能为空")
    private Integer thumbnailWidth;
    @ApiModelProperty(name = "thumbnail_height", value = "缩略图高度", required = true)
    @NotNull(message = "缩略图高度不能为空")
    private Integer thumbnailHeight;
    @ApiModelProperty(name = "small_width", value = "小图宽度", required = true)
    @NotNull(message = "小图宽度不能为空")
    private Integer smallWidth;
    @ApiModelProperty(name = "small_height", value = "小图高度", required = true)
    @NotNull(message = "小图高度不能为空")
    private Integer smallHeight;
    @ApiModelProperty(name = "big_width", value = "大图宽度", required = true)
    @NotNull(message = "大图宽度不能为空")
    private Integer bigWidth;
    @ApiModelProperty(name = "big_height", value = "大图高度", required = true)
    @NotNull(message = "大图高度不能为空")
    private Integer bigHeight;

    public Integer getMarcketAuth() {
        return marcketAuth;
    }

    public void setMarcketAuth(Integer marcketAuth) {
        this.marcketAuth = marcketAuth;
    }

    public Integer getUpdateAuth() {
        return updateAuth;
    }

    public void setUpdateAuth(Integer updateAuth) {
        this.updateAuth = updateAuth;
    }

    public Integer getCommentAuth() {
        return commentAuth;
    }

    public void setCommentAuth(Integer commentAuth) {
        this.commentAuth = commentAuth;
    }

    public Integer getAskAuth() {
        return askAuth;
    }

    public void setAskAuth(Integer askAuth) {
        this.askAuth = askAuth;
    }

    public Integer getThumbnailWidth() {
        return thumbnailWidth;
    }

    public void setThumbnailWidth(Integer thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }

    public Integer getThumbnailHeight() {
        return thumbnailHeight;
    }

    public void setThumbnailHeight(Integer thumbnailHeight) {
        this.thumbnailHeight = thumbnailHeight;
    }

    public Integer getSmallWidth() {
        return smallWidth;
    }

    public void setSmallWidth(Integer smallWidth) {
        this.smallWidth = smallWidth;
    }

    public Integer getSmallHeight() {
        return smallHeight;
    }

    public void setSmallHeight(Integer smallHeight) {
        this.smallHeight = smallHeight;
    }

    public Integer getBigWidth() {
        return bigWidth;
    }

    public void setBigWidth(Integer bigWidth) {
        this.bigWidth = bigWidth;
    }

    public Integer getBigHeight() {
        return bigHeight;
    }

    public void setBigHeight(Integer bigHeight) {
        this.bigHeight = bigHeight;
    }

    @Override
    public String toString() {
        return "GoodsSettingVO{" +
                "marcketAuth=" + marcketAuth +
                ", updateAuth=" + updateAuth +
                ", commentAuth=" + commentAuth +
                ", askAuth=" + askAuth +
                ", thumbnailWidth=" + thumbnailWidth +
                ", thumbnailHeight=" + thumbnailHeight +
                ", smallWidth=" + smallWidth +
                ", smallHeight=" + smallHeight +
                ", bigWidth=" + bigWidth +
                ", bigHeight=" + bigHeight +
                '}';
    }
}
