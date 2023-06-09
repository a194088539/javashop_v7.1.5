package com.enation.app.javashop.core.trade.cart.model.dos;

import com.enation.app.javashop.core.promotion.fulldiscount.model.dos.FullDiscountGiftDO;
import com.enation.app.javashop.core.trade.cart.model.vo.CartSkuVO;
import com.enation.app.javashop.core.trade.cart.model.vo.CouponVO;
import com.enation.app.javashop.core.trade.cart.model.vo.PriceDetailVO;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车模型
 * @author Snow
 * @version 1.0
 * @since v7.0.0
 * 2018年03月20日14:21:39
 */

@ApiModel( description = "购物车")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CartDO implements Serializable {


	private static final long serialVersionUID = 1466001652922300536L;

	@ApiModelProperty(value = "卖家id" )
	private Integer sellerId;

	@ApiModelProperty(value = "选中的配送方式id" )
	private Integer shippingTypeId;


	@ApiModelProperty(value = "选中的配送方式名称" )
	private String shippingTypeName;


	@ApiModelProperty(value = "卖家店名" )
	private String sellerName;


	@ApiModelProperty(value = "购物车重量" )
	private Double weight;

	@ApiModelProperty(value = "购物车价格")
	private PriceDetailVO price;

	@ApiModelProperty(value = "购物车中的产品列表" )
	private List<CartSkuVO> skuList;

	@ApiModelProperty(value = "赠品列表" )
	private List<FullDiscountGiftDO> giftList;

	@ApiModelProperty(value = "赠送优惠券列表")
	private List<CouponVO> giftCouponList;

	@ApiModelProperty(value = "赠送积分")
	private Integer giftPoint;

	@ApiModelProperty(value = "是否失效：0:正常 1:已失效")
	private Integer invalid;

	public CartDO(){}

	/**
	 * 在构造器中初始化属主、产品列表、促销列表及优惠券列表
	 */
	public CartDO(int sellerId, String sellerName){

		this.sellerId = sellerId;
		this.sellerName = sellerName;
		price = new PriceDetailVO();
		skuList = new ArrayList<CartSkuVO>();
		giftCouponList = new ArrayList<CouponVO>();
		giftList = new ArrayList<FullDiscountGiftDO>();
		giftPoint=0;
	}


	/**
	 * 清空优惠信息功能，不清空优惠券
	 */
	public void clearPromotion(){
		if(price!=null){
			price.clear();
		}
		giftCouponList = new ArrayList<CouponVO>();
		giftList = new ArrayList<FullDiscountGiftDO>();
		giftPoint=0;
	}

	@Override
	public String toString() {
		return "CartDO{" +
				"sellerId=" + sellerId +
				", shippingTypeId=" + shippingTypeId +
				", shippingTypeName='" + shippingTypeName + '\'' +
				", sellerName='" + sellerName + '\'' +
				", weight=" + weight +
				", price=" + price +
				", skuList=" + skuList +
				", giftList=" + giftList +
				", giftCouponList=" + giftCouponList +
				", giftPoint=" + giftPoint +
				", invalid=" + invalid +
				'}';
	}

	public Integer getSellerId() {
		return sellerId;
	}

	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}

	public Integer getShippingTypeId() {
		return shippingTypeId;
	}

	public void setShippingTypeId(Integer shippingTypeId) {
		this.shippingTypeId = shippingTypeId;
	}

	public String getShippingTypeName() {
		return shippingTypeName;
	}

	public void setShippingTypeName(String shippingTypeName) {
		this.shippingTypeName = shippingTypeName;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public PriceDetailVO getPrice() {
		return price;
	}

	public void setPrice(PriceDetailVO price) {
		this.price = price;
	}

	public List<CartSkuVO> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<CartSkuVO> skuList) {
		this.skuList = skuList;
	}


	public List<FullDiscountGiftDO> getGiftList() {
		return giftList;
	}

	public void setGiftList(List<FullDiscountGiftDO> giftList) {
		this.giftList = giftList;
	}

	public List<CouponVO> getGiftCouponList() {
		return giftCouponList;
	}

	public void setGiftCouponList(List<CouponVO> giftCouponList) {
		this.giftCouponList = giftCouponList;
	}

	public Integer getGiftPoint() {
		return giftPoint;
	}

	public void setGiftPoint(Integer giftPoint) {
		this.giftPoint = giftPoint;
	}


	public Integer getInvalid() {
		return invalid;
	}


	public void setInvalid(Integer invalid) {
		this.invalid = invalid;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o){
			return true;
		}

		if (o == null || getClass() != o.getClass()){
			return false;
		}

		CartDO cartDO = (CartDO) o;

		return new EqualsBuilder()
				.append(sellerId, cartDO.sellerId)
				.append(shippingTypeId, cartDO.shippingTypeId)
				.append(shippingTypeName, cartDO.shippingTypeName)
				.append(sellerName, cartDO.sellerName)
				.append(weight, cartDO.weight)
				.append(price, cartDO.price)
				.append(skuList, cartDO.skuList)
				.append(giftList, cartDO.giftList)
				.append(giftCouponList, cartDO.giftCouponList)
				.append(giftPoint, cartDO.giftPoint)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(sellerId)
				.append(shippingTypeId)
				.append(shippingTypeName)
				.append(sellerName)
				.append(weight)
				.append(price)
				.append(skuList)
				.append(giftList)
				.append(giftCouponList)
				.append(giftPoint)
				.toHashCode();
	}
}
