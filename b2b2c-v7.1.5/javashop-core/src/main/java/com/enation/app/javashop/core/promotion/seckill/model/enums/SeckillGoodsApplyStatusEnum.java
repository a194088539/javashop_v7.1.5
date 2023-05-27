package com.enation.app.javashop.core.promotion.seckill.model.enums;

/**
 * 秒杀活动商品的申请状态
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年12月16日 下午12:49:17
 */
public enum SeckillGoodsApplyStatusEnum {

	/** 申请中 */
	APPLY("申请中"),

	/** 已通过 */
	PASS("已通过"),

	/** 已驳回 */
	FAIL("已驳回");

	private String description;

	SeckillGoodsApplyStatusEnum(String description){
		  this.description = description;

	}

	public String description(){
		return this.description;
	}

	public String value(){
		return this.name();
	}
}
