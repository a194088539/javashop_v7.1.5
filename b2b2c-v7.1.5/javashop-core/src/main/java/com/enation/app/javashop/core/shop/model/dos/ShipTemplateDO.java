package com.enation.app.javashop.core.shop.model.dos;

import java.io.Serializable;

import com.enation.app.javashop.framework.database.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;


/**
 * 运费模版实体
 *
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-28 21:44:49
 */
@Table(name = "es_ship_template")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShipTemplateDO implements Serializable {

    private static final long serialVersionUID = 6348162955890093L;

    /**
     * 模版id
     */
    @Id(name = "id")
    @ApiModelProperty(hidden = true, value = "模版id")
    private Integer id;

    @Column(name = "seller_id")
    @ApiModelProperty(hidden = true)
    private Integer sellerId;

    @Column
    @ApiParam("名字")
    private String name;

    @Column
    @ApiParam("模版类型，1 重量算运费 2 计件算运费")
    private Integer type;

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PrimaryKeyField
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 1 重量算运费 2 计件算运费
     *
     * @return
     */
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ShipTemplateDO{" +
                "id=" + id +
                ", sellerId=" + sellerId +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}