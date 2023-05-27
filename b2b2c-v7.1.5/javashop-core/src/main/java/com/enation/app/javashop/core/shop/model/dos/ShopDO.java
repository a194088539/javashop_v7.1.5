package com.enation.app.javashop.core.shop.model.dos;

import java.io.Serializable;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 店铺实体
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-20 10:33:40
 */
@Table(name="es_shop")
@ApiModel
public class  ShopDO implements Serializable {
			
    private static final long serialVersionUID = 8432303547724758L;
    
    /**店铺Id*/
    @Id(name = "shop_id")
    @ApiModelProperty(hidden=true)
    private Integer shopId;
    /**会员Id*/
    @Column(name = "member_id")
    @ApiModelProperty(name="member_id",value="会员Id",required=false)
    private Integer memberId;
    /**会员名称*/
    @Column(name = "member_name")
    @ApiModelProperty(name="member_name",value="会员名称",required=false)
    private String memberName;
    /**店铺名称*/
    @Column(name = "shop_name")
    @ApiModelProperty(name="shop_name",value="店铺名称",required=false)
    private String shopName;
    /**店铺状态*/
    @Column(name = "shop_disable")
    @ApiModelProperty(name="shop_disable",value="店铺状态",required=false)
    private String shopDisable;
    /**店铺创建时间*/
    @Column(name = "shop_createtime")
    @ApiModelProperty(name="shop_createtime",value="店铺创建时间",required=false)
    private Long shopCreatetime;
    /**店铺关闭时间*/
    @Column(name = "shop_endtime")
    @ApiModelProperty(name="shop_endtime",value="店铺关闭时间",required=false)
    private Long shopEndtime;

    @PrimaryKeyField
    public Integer getShopId() {
        return shopId;
    }
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getMemberId() {
        return memberId;
    }
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getShopName() {
        return shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopDisable() {
        return shopDisable;
    }
    public void setShopDisable(String shopDisable) {
        this.shopDisable = shopDisable;
    }

    public Long getShopCreatetime() {
        return shopCreatetime;
    }
    public void setShopCreatetime(Long shopCreatetime) {
        this.shopCreatetime = shopCreatetime;
    }

    public Long getShopEndtime() {
        return shopEndtime;
    }
    public void setShopEndtime(Long shopEndtime) {
        this.shopEndtime = shopEndtime;
    }
	@Override
	public String toString() {
		return "ShopDO [shopId=" + shopId + ", memberId=" + memberId + ", memberName=" + memberName + ", shopName="
				+ shopName + ", shopDisable=" + shopDisable + ", shopCreatetime=" + shopCreatetime + ", shopEndtime="
				+ shopEndtime + "]";
	}

    
	
}