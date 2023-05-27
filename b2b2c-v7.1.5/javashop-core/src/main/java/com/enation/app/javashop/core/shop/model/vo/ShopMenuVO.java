package com.enation.app.javashop.core.shop.model.vo;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * 菜单管理店铺实体
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-08-02 15:38:40
 */
public class ShopMenuVO {

    /**
     * 菜单父id
     */
    @NotNull(message = "父菜单id不能为空")
    @Min(message = "父菜单必须为数字且不能为负数", value = 0)
    @ApiModelProperty(name = "parent_id", value = "菜单父id", required = false)
    private Integer parentId;
    /**
     * 菜单标题
     */
    @NotEmpty(message = "菜单标题不能为空")
    @ApiModelProperty(name = "title", value = "菜单标题", required = false)
    private String title;
    /**
     * 菜单唯一标识
     */
    @NotEmpty(message = "菜单唯一标识不能为空")
    @ApiModelProperty(name = "identifier", value = "菜单唯一标识", required = false)
    private String identifier;
    /**
     * 权限表达式
     */
    @NotEmpty(message = "权限表达式不能为空")
    @ApiModelProperty(name = "auth_regular", value = "权限表达式", required = false)
    private String authRegular;

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getAuthRegular() {
        return authRegular;
    }

    public void setAuthRegular(String authRegular) {
        this.authRegular = authRegular;
    }

    @Override
    public String toString() {
        return "ShopMenuVO{" +
                "parentId=" + parentId +
                ", title='" + title + '\'' +
                ", identifier='" + identifier + '\'' +
                ", authRegular='" + authRegular + '\'' +
                '}';
    }
}