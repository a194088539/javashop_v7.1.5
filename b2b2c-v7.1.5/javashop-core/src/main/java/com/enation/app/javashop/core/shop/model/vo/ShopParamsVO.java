package com.enation.app.javashop.core.shop.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 店铺搜索条件VO
 * @author zhangjiping
 * @version v1.0
 * @since v7.0
 * 2018年3月21日 下午8:43:57
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShopParamsVO {
	/** 页码 */
	@ApiModelProperty(name="page_no",value = "页码")
	private Integer pageNo;
	/** 分页数 */
	@ApiModelProperty(name="page_size",value = "分页数")
	private Integer pageSize;
	 /**店铺名称*/
    @ApiModelProperty(name="shop_name",value="店铺名称",required=false)
    private String shopName;	
    /**会员名称*/
    @ApiModelProperty(name="member_name",value="会员名称",required=false)
    private String memberName;
    /**开始时间*/
    @ApiModelProperty(name="start_time",value="店铺开始时间",required=false)
    private String startTime;
    /**结束时间*/
    @ApiModelProperty(name="end_time",value="店铺关闭时间",required=false)
    private String endTime;
    /**关键字*/
    @ApiModelProperty(name="keyword",value="关键字",required=false)
    private String keyword;
    /**店铺状态*/
    @ApiModelProperty(name="shop_disable",value="店铺状态",required=false)
    private String shopDisable;
	/**排序方式*/
	@ApiModelProperty(name="order",value="排序方式",required=false)
    private String order ;

	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getShopDisable() {
		return shopDisable;
	}
	public void setShopDisable(String shopDisable) {
		this.shopDisable = shopDisable;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "ShopParamsVO{" +
				"pageNo=" + pageNo +
				", pageSize=" + pageSize +
				", shopName='" + shopName + '\'' +
				", memberName='" + memberName + '\'' +
				", startTime='" + startTime + '\'' +
				", endTime='" + endTime + '\'' +
				", keyword='" + keyword + '\'' +
				", shopDisable='" + shopDisable + '\'' +
				", order='" + order + '\'' +
				'}';
	}
}
