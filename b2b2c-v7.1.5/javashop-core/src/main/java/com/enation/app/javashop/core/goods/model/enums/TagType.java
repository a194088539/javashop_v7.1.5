package com.enation.app.javashop.core.goods.model.enums;

/**
 * 标签类型
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月28日 下午3:00:24
 */
public enum TagType {

	/**
	 * 热卖排行
	 */
	HOT("hot","热卖排行"),
	/**
	 * 新品推荐
	 */
	NEW("new","新品推荐"),
	/**
	 * 推荐商品
	 */
	RECOMMEND("recommend","推荐商品");
	
	private String mark;
	private String tagName;
	
	TagType(String mark,String tagName) {

		this.mark = mark;
		this.tagName = tagName;
	}
	
	public String mark(){
		return this.mark;
	}
	
	public String tagName(){
		return this.tagName;
	}
}
