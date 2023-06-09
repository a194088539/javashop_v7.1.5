package com.enation.app.javashop.core.statistics.service;

import com.enation.app.javashop.core.statistics.model.vo.SimpleChart;
import com.enation.app.javashop.framework.database.Page;

/**
* 商家中心，商品收藏统计
*
* @author mengyuanming
* @version 2.0
* @since 7.0 
* 2018年4月20日下午4:23:58
*/
public interface CollectFrontStatisticsManager {
	
	/**
	 * 商品收藏图表数据
	 * 
	 * @param sellerId，商家id
	 * @return SimpleChart，简单图表数据
	 */
	SimpleChart getChart(Integer sellerId);
	
	/**
	 * 商品收藏列表数据
	 * @param pageNo，页码
	 * @param pageSize，页面数据量
	 * @param sellerId，商家id
	 * @return Page，分页数据
	 */
	Page getPage(Integer pageNo, Integer pageSize, Integer sellerId);

}
