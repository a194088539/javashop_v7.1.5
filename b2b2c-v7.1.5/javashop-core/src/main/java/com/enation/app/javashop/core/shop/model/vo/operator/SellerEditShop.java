package com.enation.app.javashop.core.shop.model.vo.operator;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 卖家操作店铺
 * @author zhangjiping
 * @version v1.0
 * @since v7.0
 * 2018年3月27日 上午10:29:48
 */
public class SellerEditShop {
	
	@ApiModelProperty(value = "卖家id" )
	private Integer sellerId;
	
	@ApiModelProperty(value = "操作")
	private String operator;
	
	
	public SellerEditShop() {
		
	}

	public SellerEditShop(Integer sellerId, String operator) {
		super();
		this.sellerId = sellerId;
		this.operator = operator;
	}

	public Integer getSellerId() {
		return sellerId;
	}

	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	
}
