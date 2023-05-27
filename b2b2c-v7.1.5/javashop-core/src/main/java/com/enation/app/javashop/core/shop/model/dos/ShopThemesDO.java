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
 * 店铺模版实体
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-27 14:17:32
 */
@Table(name="es_shop_themes")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShopThemesDO implements Serializable {
			
    private static final long serialVersionUID = 2259403879829841L;
    
    /**模版id*/
    @Id(name = "id")
    @ApiModelProperty(hidden=true)
    private Integer id;
    /**模版名称*/
    @Column(name = "name")
    @ApiModelProperty(name="name",value="模版名称",required=true)
    @NotEmpty(message="模版名称必填")
    private String name;
    /**模版目录*/
    @Column(name = "image_path")
    @ApiModelProperty(name="image_path",value="模版图片路径",required=true)
    @NotEmpty(message="模版图片路径不能为空")
    private String imagePath;
    /**模版标识*/
    @Column(name = "mark")
    @ApiModelProperty(name = "mark",value = "模版标识",required=true)
    @NotEmpty(message="模版标识不能为空")
    private String mark;

    /**是否为默认*/
    @Column(name = "is_default")
    @ApiModelProperty(name="is_default",value="是否为默认 1为默认 反之为0",required=true)
    @NotNull(message="是否为默认必填")
    private Integer isDefault;
    /**模版类型*/
    @Column(name = "type")
    @ApiModelProperty(name="type",value="模版类型",required=true,allowableValues = "PC,WAP")
    @NotEmpty(message="模版类型必填")
    private String type;

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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Integer getIsDefault() {
        return isDefault;
    }
    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ShopThemesDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", mark='" + mark + '\'' +
                ", isDefault=" + isDefault +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
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
		ShopThemesDO other = (ShopThemesDO) obj;
		if (id == null) {
			if (other.id != null) {
                return false;
            }
		} else if (!id.equals(other.id)) {
            return false;
        }
		if (type == null) {
			if (other.type != null) {
                return false;
            }
		} else if (!type.equals(other.type)) {
            return false;
        }
		return true;
	}

    
	
}