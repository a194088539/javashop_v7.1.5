package com.enation.app.javashop.core.trade.order.service;

import com.enation.app.javashop.core.trade.order.model.dos.OrderOutStatus;
import com.enation.app.javashop.core.trade.order.model.enums.OrderOutStatusEnum;
import com.enation.app.javashop.core.trade.order.model.enums.OrderOutTypeEnum;
import com.enation.app.javashop.framework.database.Page;

/**
 * 订单出库状态业务层
 * @author xlp
 * @version v2.0
 * @since v7.0.0
 * 2018-07-10 14:06:38
 */
public interface OrderOutStatusManager	{

	/**
	 * 查询订单出库状态列表
	 * @param page 页码
	 * @param pageSize 每页数量
	 * @return Page
	 */
	Page list(int page, int pageSize);

	/**
	 * 添加订单出库状态
	 * @param orderOutStatus 订单出库状态
	 * @return OrderOutStatus 订单出库状态
	 */
	OrderOutStatus add(OrderOutStatus orderOutStatus);

	/**
	* 修改订单出库状态
	* @param orderSn 订单编号
	* @param typeEnum 出库类型
	* @param  statusEnum  出库状态
	* @return OrderOutStatus 订单出库状态
	*/
	void edit(String orderSn, OrderOutTypeEnum typeEnum, OrderOutStatusEnum statusEnum);

	/**
	 * 删除订单出库状态
	 * @param id 订单出库状态主键
	 */
	void delete(Integer id);

	/**
	 * 获取订单出库状态
	 * @param orderSn	订单编号
	 * @param typeEnum	出库类型
	 * @return OrderOutStatus  订单出库状态
	 */
	OrderOutStatus getModel(String orderSn, OrderOutTypeEnum typeEnum);

}
