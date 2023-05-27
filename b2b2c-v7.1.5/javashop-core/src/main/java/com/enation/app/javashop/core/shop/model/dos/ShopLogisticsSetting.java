package com.enation.app.javashop.core.shop.model.dos;

import com.enation.app.javashop.core.system.model.dto.KDNParams;
import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 店铺电子面单设置
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2019-05-23 上午11:10
 */
@Table(name="es_shop_logistics_setting")
@ApiModel
public class ShopLogisticsSetting {


    @Id(name = "id")
    private Integer id;

    @Column(name = "shop_id")
    @ApiModelProperty(name = "shop_id", value = "店铺id", hidden = true)
    private Integer shopId;

    @Column(name = "logistics_id")
    @ApiModelProperty(name = "logistics_id", value = "物流id")
    private Integer logisticsId;

    @Column(name = "config")
    @ApiModelProperty(name = "config", value = "配置项", hidden = true)
    private String config;


    public void setParams(KDNParams kdnParams) {
        if (kdnParams != null) {
            this.setConfig(JsonUtil.objectToJson(kdnParams));
        }
    }

    public KDNParams getParams() {
        if (!StringUtil.isEmpty(config)) {
            return JsonUtil.jsonToObject(config, KDNParams.class);
        }
        return null;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(Integer logisticsId) {
        this.logisticsId = logisticsId;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
