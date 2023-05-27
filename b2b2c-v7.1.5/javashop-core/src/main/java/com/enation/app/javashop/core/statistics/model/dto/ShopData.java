package com.enation.app.javashop.core.statistics.model.dto;

import com.enation.app.javashop.core.shop.model.vo.ShopVO;
import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 店铺统计数据
 *
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018-03-29 下午4:41
 */
@Table(name = "es_sss_shop_data")
public class ShopData implements Serializable {


    @ApiModelProperty(value = "主键id")
    @Id
    private Integer id;

    @ApiModelProperty(value = "商家id")
    @Column(name = "seller_id")
    private Integer sellerId;

    @ApiModelProperty(value = "商家名称")
    @Column(name = "seller_name")
    private String sellerName;

    @ApiModelProperty(value = "收藏数量")
    @Column(name = "favorite_num")
    private Integer favoriteNum;

    @ApiModelProperty(value = "OPEN/CLOSED/APPLY/REFUSED/APPLYING")
    @Column(name = "shop_disable")
    private String shopDisable;

    public ShopData() {
    }

    public ShopData(ShopVO shopVO) {
        this.setSellerId(shopVO.getShopId());
        this.setSellerName(shopVO.getShopName());
        this.setShopDisable(shopVO.getShopDisable());
        this.setFavoriteNum(0);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Integer getFavoriteNum() {
        return favoriteNum;
    }

    public void setFavoriteNum(Integer favoriteNum) {
        this.favoriteNum = favoriteNum;
    }

    public String getShopDisable() {
        return shopDisable;
    }

    public void setShopDisable(String shopDisable) {
        this.shopDisable = shopDisable;
    }

    @Override
    public String toString() {
        return "ShopData{" +
                "sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                ", favoriteNum=" + favoriteNum +
                ", shopDisable='" + shopDisable + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShopData shopData = (ShopData) o;

        if (sellerId != null ? !sellerId.equals(shopData.sellerId) : shopData.sellerId != null) {
            return false;
        }
        if (sellerName != null ? !sellerName.equals(shopData.sellerName) : shopData.sellerName != null) {
            return false;
        }
        if (favoriteNum != null ? !favoriteNum.equals(shopData.favoriteNum) : shopData.favoriteNum != null) {
            return false;
        }
        return shopDisable != null ? shopDisable.equals(shopData.shopDisable) : shopData.shopDisable == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sellerId != null ? sellerId.hashCode() : 0);
        result = 31 * result + (sellerName != null ? sellerName.hashCode() : 0);
        result = 31 * result + (favoriteNum != null ? favoriteNum.hashCode() : 0);
        result = 31 * result + (shopDisable != null ? shopDisable.hashCode() : 0);
        return result;
    }
}
