package com.enation.app.javashop.core.shop.model.vo;

import com.enation.app.javashop.framework.database.annotation.Column;

import java.io.Serializable;

/**
 * @author fk
 * @version v2.0
 * @Description:
 * @date 2018/10/26 16:32
 * @since v7.0.0
 */
public class ShipTemplateChildBuyerVO extends ShipTemplateChildBaseVO implements Serializable {

    @Column(name = "area_id")
    private String areaId;


    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}
