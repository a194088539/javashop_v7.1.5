package com.enation.app.javashop.core.shop.model.vo.operator;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 管理员对店铺进行操作
 * @author zhangjiping
 * @version v1.0
 * @since v7.0
 * 2018年3月27日 上午10:06:19
 */
public class AdminEditShop {
	@ApiModelProperty(value = "卖家id" )
	private Integer sellerId;
	

	@ApiModelProperty(value = "操作")
	private String operator;
	
	
	public AdminEditShop() {
		
	}

	public AdminEditShop(Integer sellerId, String operator) {
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
