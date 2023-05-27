package com.enation.app.javashop.core.shop.model.vo;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 店铺模版VO(商家中心使用)
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-27 14:17:32
 */
@Table(name="es_shop_themes")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShopThemesVO implements Serializable {
			
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
    @NotEmpty(message="模版目录必填")
    private String imagePath;
    /**是否为默认*/
    @Column(name = "is_default")
    @ApiModelProperty(name="is_default",value="是否为默认 1为默认 反之为0",required=true)
    @NotEmpty(message="是否为默认必填")
    private Integer isDefault;
    /**模版类型*/
    @Column(name = "type")
    @ApiModelProperty(name="type",value="模版类型 PC WAP",required=true)
    @NotEmpty(message="模版类型必填")
    private String type;
    /**模版标识*/
    @Column(name = "mark")
    @ApiModelProperty(name = "mark",value = "模版标识",required=true)
    @NotEmpty(message="模版标识不能为空")
    private String mark;

    @ApiModelProperty(name="current_use",value="当前是否使用 1 是 0 否",required=true)
    private Integer currentUse;
    
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
	public Integer getCurrentUse() {
		return currentUse;
	}
	public void setCurrentUse(Integer currentUse) {
		this.currentUse = currentUse;
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

    @Override
    public String toString() {
        return "ShopThemesVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", isDefault=" + isDefault +
                ", type='" + type + '\'' +
                ", mark='" + mark + '\'' +
                ", currentUse=" + currentUse +
                '}';
    }
}