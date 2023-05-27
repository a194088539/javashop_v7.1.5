package com.enation.app.javashop.core.shop.model.dos;

import java.io.Serializable;

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

/**
 * 店铺幻灯片实体
 *
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-28 18:50:58
 */
@Table(name = "es_shop_silde")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShopSildeDO implements Serializable {

    private static final long serialVersionUID = 477259544785686L;

    /**
     * 幻灯片Id
     */
    @Id(name = "silde_id")
    @ApiModelProperty(name = "silde_id", value = "幻灯片id", required = false)
    private Integer sildeId;
    /**
     * 店铺Id
     */
    @Column(name = "shop_id")
    @ApiModelProperty(name = "shop_id", value = "店铺Id", required = false, hidden = false)
    private Integer shopId;
    /**
     * 幻灯片URL
     */
    @Column(name = "silde_url")
    @NotEmpty(message = "幻灯片URL不能为空")
    @Length(max = 100, message = "幻灯片URL超出限制")
    @ApiModelProperty(name = "silde_url", value = "幻灯片URL", required = true)
    private String sildeUrl;
    /**
     * 图片
     */
    @Column(name = "img")
    @NotEmpty(message = "图片不能为空")
    @ApiModelProperty(name = "img", value = "图片", required = true)
    private String img;

    @PrimaryKeyField
    public Integer getSildeId() {
        return sildeId;
    }

    public void setSildeId(Integer sildeId) {
        this.sildeId = sildeId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getSildeUrl() {
        return sildeUrl;
    }

    public void setSildeUrl(String sildeUrl) {
        this.sildeUrl = sildeUrl;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "ShopSildeDO [sildeId=" + sildeId + ", shopId=" + shopId + ", sildeUrl=" + sildeUrl + ", img=" + img
                + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShopSildeDO that = (ShopSildeDO) o;

        if (!sildeId.equals(that.sildeId)) {
            return false;
        }
        if (!shopId.equals(that.shopId)) {
            return false;
        }
        return sildeUrl.equals(that.sildeUrl);
    }

    @Override
    public int hashCode() {
        int result = sildeId.hashCode();
        result = 31 * result + shopId.hashCode();
        result = 31 * result + sildeUrl.hashCode();
        return result;
    }
}