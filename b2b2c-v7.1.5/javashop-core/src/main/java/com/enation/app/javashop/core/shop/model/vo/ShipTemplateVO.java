package com.enation.app.javashop.core.shop.model.vo;

import com.enation.app.javashop.core.shop.model.dos.ShipTemplateDO;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

/**
 * @author fk
 * @version v2.0
 * @Description: 运费模板VO
 * @date 2018/8/22 15:16
 * @since v7.0.0
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShipTemplateVO extends ShipTemplateDO implements Serializable {

    @ApiModelProperty(name = "items", value = "指定配送区域", required = true)
    private List<ShipTemplateChildBuyerVO>  items;

    public List<ShipTemplateChildBuyerVO> getItems() {
        return items;
    }

    public void setItems(List<ShipTemplateChildBuyerVO> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ShipTemplateVO{" +
                "items=" + items +
                '}';
    }
}
