package com.enation.app.javashop.core.shop.model.vo.operator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 初始化店铺
 * @author zhangjiping
 * @version v7.0.0
 * @since v7.0.0
 * 2018年3月30日 上午11:10:35
 */
@ApiModel(description = "初始化店铺")
public class Init {
	
	@ApiModelProperty(value = "会员id" )
	private Integer memberId;
	

	@ApiModelProperty()
	private String operator;


	private String shopStatus;
	public Init() {
		
	}

	public Init(Integer memberId, String operator) {
		super();
		this.memberId = memberId;
		this.operator = operator;
	}


	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public String getOperator() {
		return operator;
	}


	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getShopStatus() {
		return shopStatus;
	}

	public void setShopStatus(String shopStatus) {
		this.shopStatus = shopStatus;
	}
}
