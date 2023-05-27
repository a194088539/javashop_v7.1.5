package com.enation.app.javashop.core.distribution.model.vo;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;

/**
 * SellerPushVO
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-06-13 下午4:07
 */
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SellerPushVO {


    @Column(name = "push_money")
    @ApiModelProperty(value="返现金额",name = "push_money",required=true)
    private Double pushMoney;

    @Column(name = "seller_name")
    @ApiModelProperty(value="店铺名称",name = "seller_name",required=true)
    private String sellerName;

    @Column(name = "seller_id")
    @ApiModelProperty(value="店铺ID",name = "seller_id",required=true)
    private String sellerId;

    public Double getPushMoney() {
        return pushMoney;
    }

    public void setPushMoney(Double pushMoney) {
        this.pushMoney = pushMoney;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    @Override
    public String toString() {
        return "SellerPushVO{" +
                "pushMoney=" + pushMoney +
                ", sellerName='" + sellerName + '\'' +
                ", sellerId='" + sellerId + '\'' +
                '}';
    }
}
