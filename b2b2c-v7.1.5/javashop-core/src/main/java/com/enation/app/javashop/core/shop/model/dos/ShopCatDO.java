package com.enation.app.javashop.core.shop.model.dos;

import java.io.Serializable;
import java.util.List;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * 店铺分组实体
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-04-24 11:18:37
 */
@Table(name="es_shop_cat")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShopCatDO implements Serializable {
			
    private static final long serialVersionUID = 9888143360348481L;
    
    /**店铺分组id*/
    @Id(name = "shop_cat_id")
    @ApiModelProperty(name = "shop_cat_id",value = "店铺分组id")
    private Integer shopCatId;
    /**店铺分组父ID*/
    @Column(name = "shop_cat_pid")
    @ApiModelProperty(name="shop_cat_pid",value="店铺分组父ID",required=false)
    private Integer shopCatPid;
    /**店铺id*/
    @Column(name = "shop_id")
    @ApiModelProperty(name="shop_id",value="店铺id",required=false)
    private Integer shopId;
    /**店铺分组名称*/
    @Column(name = "shop_cat_name")
    @ApiModelProperty(name="shop_cat_name",value="店铺分组名称",required=true)
    @NotEmpty(message = "店铺分组名称必填")
    private String shopCatName;
    /**店铺分组显示状态:1显示 0不显示*/
    @Column(name = "disable")
    @ApiModelProperty(name="disable",value="店铺分组显示状态:1显示 0不显示",required=true)
    @NotNull(message = "店铺分组显示状态必填")
    private Integer disable;
    /**排序*/
    @Column(name = "sort")
    @ApiModelProperty(name="sort",value="排序",required=true)
    @NotNull(message = "排序必填")
    private Integer sort;
    /**分组路径*/
    @Column(name = "cat_path")
    @ApiModelProperty(name="cat_path",value="分组路径",required=false)
    private String catPath;

    /**子分组*/
    @ApiModelProperty(name="children",value="分组路径",required=false)
    private List<ShopCatDO> children;

    @PrimaryKeyField
    public Integer getShopCatId() {
        return shopCatId;
    }
    public void setShopCatId(Integer shopCatId) {
        this.shopCatId = shopCatId;
    }

    public Integer getShopCatPid() {
        return shopCatPid;
    }
    public void setShopCatPid(Integer shopCatPid) {
        this.shopCatPid = shopCatPid;
    }

    public Integer getShopId() {
        return shopId;
    }
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getShopCatName() {
        return shopCatName;
    }
    public void setShopCatName(String shopCatName) {
        this.shopCatName = shopCatName;
    }

    public Integer getDisable() {
        return disable;
    }
    public void setDisable(Integer disable) {
        this.disable = disable;
    }

    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getCatPath() {
        return catPath;
    }
    public void setCatPath(String catPath) {
        this.catPath = catPath;
    }

    public List<ShopCatDO> getChildren() {
        return children;
    }

    public void setChildren(List<ShopCatDO> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShopCatDO shopCatDO = (ShopCatDO) o;

        if (!shopCatId.equals(shopCatDO.shopCatId)) {
            return false;
        }
        if (!shopCatPid.equals(shopCatDO.shopCatPid)) {
            return false;
        }
        return shopId.equals(shopCatDO.shopId);
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (shopCatId != null ? shopCatId.hashCode() : 0);
        result = 31 * result + (shopCatPid != null ? shopCatPid.hashCode() : 0);
        result = 31 * result + (shopId != null ? shopId.hashCode() : 0);
        result = 31 * result + (shopCatName != null ? shopCatName.hashCode() : 0);
        result = 31 * result + (disable != null ? disable.hashCode() : 0);
        result = 31 * result + (sort != null ? sort.hashCode() : 0);
        result = 31 * result + (catPath != null ? catPath.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ShopCatDO{" +
                "shopCatId=" + shopCatId +
                ", shopCatPid=" + shopCatPid +
                ", shopId=" + shopId +
                ", shopCatName='" + shopCatName + '\'' +
                ", disable=" + disable +
                ", sort=" + sort +
                ", catPath='" + catPath + '\'' +
                '}';
    }

	
}