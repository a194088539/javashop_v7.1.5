package com.enation.app.javashop.core.goodssearch.model;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;


/**
 * 自定义分词表实体
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-06-21 16:51:38
 */
@Table(name = "es_custom_words")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CustomWords implements Serializable {

    private static final long serialVersionUID = 3314721960882914L;

    /**
     * 主键
     */
    @Id(name = "id")
    @ApiModelProperty(hidden = true)
    private Integer id;
    /**
     * 名称
     */
    @Column(name = "name")
    @ApiModelProperty(name = "name", value = "名称", required = true)
    @NotEmpty(message = "分词名称必填")
    @Length(max=20,message="分词名称长度不能大于20")
    private String name;
    /**
     * 添加时间
     */
    @Column(name = "add_time")
    @ApiModelProperty(name = "add_time", value = "添加时间", hidden = true)
    private Long addTime;
    /**
     * 显示 1  隐藏 0
     */
    @Column(name = "disabled")
    @ApiModelProperty(name = "disabled", value = "显示 1  隐藏 0", hidden = true)
    private Integer disabled;
    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    @ApiModelProperty(name = "modify_time", value = "修改时间", hidden = true)
    private Long modifyTime;

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

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomWords that = (CustomWords) o;
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) {
            return false;
        }
        if (disabled != null ? !disabled.equals(that.disabled) : that.disabled != null) {
            return false;
        }
        return modifyTime != null ? modifyTime.equals(that.modifyTime) : that.modifyTime == null;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        result = 31 * result + (disabled != null ? disabled.hashCode() : 0);
        result = 31 * result + (modifyTime != null ? modifyTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CustomWords{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", addTime=" + addTime +
                ", disabled=" + disabled +
                ", modifyTime=" + modifyTime +
                '}';
    }


}