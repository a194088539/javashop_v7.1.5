package com.enation.app.javashop.core.trade.order.service;

import com.enation.app.javashop.core.trade.order.model.dos.PayLog;
import com.enation.app.javashop.core.trade.order.model.dto.PayLogQueryParam;
import com.enation.app.javashop.framework.database.Page;

import java.util.List;

/**
 * 收款单业务层
 * @author xlp
 * @version v2.0
 * @since v7.0.0
 * 2018-07-18 10:39:51
 */
public interface PayLogManager	{

	/**
	 * 查询收款单列表
	 * @param queryParam 查询参数
	 * @return Page
	 */
	Page list(PayLogQueryParam queryParam);

	/**
	 * 添加收款单
	 * @param payLog 收款单
	 * @return PayLog 收款单
	 */
	PayLog add(PayLog payLog);

	/**
	* 修改收款单
	* @param payLog 收款单
	* @param id 收款单主键
	* @return PayLog 收款单
	*/
	PayLog edit(PayLog payLog, Integer id);

	/**
	 * 删除收款单
	 * @param id 收款单主键
	 */
	void delete(Integer id);

	/**
	 * 获取收款单
	 * @param id 收款单主键
	 * @return PayLog  收款单
	 */
	PayLog getModel(Integer id);

	/**
	 * 根据订单号
	 * @param orderSn
	 * @return
	 */
	PayLog getModel(String  orderSn);

	/**
	 * 返回不分页的数据
	 * @param queryParam
	 * @return
	 */
	List<PayLog> exportExcel(PayLogQueryParam queryParam);
}
