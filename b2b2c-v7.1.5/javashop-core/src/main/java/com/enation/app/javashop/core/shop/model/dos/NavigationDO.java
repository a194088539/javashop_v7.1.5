package com.enation.app.javashop.core.shop.model.dos;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 店铺导航管理实体
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-28 20:44:54
 */
@Table(name="es_navigation")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NavigationDO implements Serializable {
			
    private static final long serialVersionUID = 925654766140248L;
    
    /**导航id*/
    @Id(name = "id")
    @ApiModelProperty(name = "id",value = "导航id")
    private Integer id;
    /**名称*/
    @Column(name = "name")
    @ApiModelProperty(name="name",value="名称",required=true)
    @NotEmpty(message = "导航栏名称必填")
    private String name;
    /**是否显示*/
    @Column(name = "disable")
    @ApiModelProperty(name="disable",value="是否显示 1 是 0 否",required=true)
    @NotNull(message = "是否显示必填")
    private Integer disable;
    /**排序*/
    @Column(name = "sort")
    @ApiModelProperty(name="sort",value="排序",required=true)
    @NotNull(message = "排序必填")
    private Integer sort;
    /**URL*/
    @Column(name = "nav_url")
    @ApiModelProperty(name="nav_url",value="URL",required=true)
    @NotEmpty(message = "URL必填")
    private String navUrl;
    /**新窗口打开*/
    @Column(name = "target")
    @ApiModelProperty(name="target",value="新窗口打开 1 是 0 否",required=true)
    @NotNull(message = "是否新窗口打开必填")
    private Integer target;
    /**店铺id*/
    @Column(name = "shop_id")
    @ApiModelProperty(name="shop_id",value="店铺id",required=false)
    private Integer shopId;

    @PrimaryKeyField
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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

    public String getNavUrl() {
        return navUrl;
    }
    public void setNavUrl(String navUrl) {
        this.navUrl = navUrl;
    }

    public Integer getTarget() {
        return target;
    }
    public void setTarget(Integer target) {
        this.target = target;
    }

    public Integer getShopId() {
        return shopId;
    }
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }
	@Override
	public String toString() {
		return "NavigationDO [id=" + id + ", name=" + name + ", disable=" + disable + ", sort=" + sort + ", navUrl="
				+ navUrl + ", target=" + target + ", shopId=" + shopId + "]";
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
            return true;
        }
		if (obj == null) {
            return false;
        }
		if (getClass() != obj.getClass()) {
            return false;
        }
		NavigationDO other = (NavigationDO) obj;
		if (id == null) {
			if (other.id != null) {
                return false;
            }
		} else if (!id.equals(other.id)) {
            return false;
        }
		if (name == null) {
			if (other.name != null) {
                return false;
            }
		} else if (!name.equals(other.name)) {
            return false;
        }
		if (shopId == null) {
			if (other.shopId != null) {
                return false;
            }
		} else if (!shopId.equals(other.shopId)) {
            return false;
        }
		return true;
	}

    
	
}