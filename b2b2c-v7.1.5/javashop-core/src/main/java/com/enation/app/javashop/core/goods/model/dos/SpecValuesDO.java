package com.enation.app.javashop.core.goods.model.dos;

import java.io.Serializable;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 规格值实体
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-03-20 10:23:52
 */
@Table(name = "es_spec_values")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SpecValuesDO implements Serializable {

    private static final long serialVersionUID = 5356147177823233L;

    /**
     * 主键
     */
    @Id(name = "spec_value_id")
    @ApiModelProperty(name = "spec_value_id", value = "规格值id", required = false)
    @JsonProperty(value = "spec_value_id")
    private Integer specValueId;
    /**
     * 规格项id
     */
    @Column(name = "spec_id")
    @ApiModelProperty(value = "规格项id", required = false)
    private Integer specId;
    /**
     * 规格值名字
     */
    @Column(name = "spec_value")
    @ApiModelProperty(value = "规格值名字", required = false)
    private String specValue;
    /**
     * 所属卖家
     */
    @Column(name = "seller_id")
    @ApiModelProperty(value = "所属卖家", hidden = true)
    private Integer sellerId;

    /**
     * 规格名称
     */
    @Column(name = "spec_name")
    @ApiModelProperty(value = "规格名称")
    private String specName;

    public SpecValuesDO() {

    }


    public SpecValuesDO(Integer specId, String specValue, Integer sellerId) {
        super();
        this.specId = specId;
        this.specValue = specValue;
        this.sellerId = sellerId;
    }


    @PrimaryKeyField
    public Integer getSpecValueId() {
        return specValueId;
    }

    public void setSpecValueId(Integer specValueId) {
        this.specValueId = specValueId;
    }

    public Integer getSpecId() {
        return specId;
    }

    public void setSpecId(Integer specId) {
        this.specId = specId;
    }

    public String getSpecValue() {
        return specValue;
    }

    public void setSpecValue(String specValue) {
        this.specValue = specValue;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }


    @Override
    public String toString() {
        return "SpecValuesDO [specValueId=" + specValueId + ", specId=" + specId + ", specValue=" + specValue
                + ", sellerId=" + sellerId + "]";
    }


    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }
}