package com.enation.app.javashop.core.shop.model.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;


/**
 * 菜单管理店铺实体
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-08-02 15:38:40
 */
public class ShopMenus {

    /**
     * 菜单标题
     */
    @ApiModelProperty(value = "菜单标题")
    private String title;
    /**
     * 菜单唯一标识
     */
    @ApiModelProperty(value = "菜单唯一标识")
    private String identifier;
    /**
     * 此菜单是否选中
     */
    @ApiModelProperty(value = "此菜单是否选中")
    private boolean checked;
    /**
     * 菜单的权限表达式
     */
    @ApiModelProperty(value = "菜单的权限表达式")
    private String authRegular;
    /**
     * 子菜单
     */
    @ApiModelProperty(value = "子菜单")
    private List<ShopMenus> children;

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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getAuthRegular() {
        return authRegular;
    }

    public void setAuthRegular(String authRegular) {
        this.authRegular = authRegular;
    }

    public List<ShopMenus> getChildren() {
        return children;
    }

    public void setChildren(List<ShopMenus> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "ShopMenus{" +
                "title='" + title + '\'' +
                ", identifier='" + identifier + '\'' +
                ", checked=" + checked +
                ", authRegular='" + authRegular + '\'' +
                ", children=" + children +
                '}';
    }
}