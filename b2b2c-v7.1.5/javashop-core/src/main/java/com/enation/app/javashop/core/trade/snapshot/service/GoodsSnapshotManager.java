package com.enation.app.javashop.core.trade.snapshot.service;

import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.snapshot.model.SnapshotVO;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.trade.snapshot.model.GoodsSnapshot;

/**
 * 交易快照业务层
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-08-01 14:55:26
 */
public interface GoodsSnapshotManager	{

	/**
	 * 查询交易快照列表
	 * @param page 页码
	 * @param pageSize 每页数量
	 * @return Page 
	 */
	Page list(int page, int pageSize);
	/**
	 * 添加交易快照
	 * @param goodsSnapshot 交易快照
	 * @return GoodsSnapshot 交易快照
	 */
	GoodsSnapshot add(GoodsSnapshot goodsSnapshot);

	/**
	* 修改交易快照
	* @param goodsSnapshot 交易快照
	* @param id 交易快照主键
	* @return GoodsSnapshot 交易快照
	*/
	GoodsSnapshot edit(GoodsSnapshot goodsSnapshot, Integer id);
	
	/**
	 * 删除交易快照
	 * @param id 交易快照主键
	 */
	void delete(Integer id);
	
	/**
	 * 获取交易快照
	 * @param id 交易快照主键
	 * @return GoodsSnapshot  交易快照
	 */
	GoodsSnapshot getModel(Integer id);

	/**
	 * 添加交易快照
	 * @param orderDO
	 */
	void add(OrderDO orderDO);

	/**
	 * 查询快照VO
	 * @param id
	 * @param owner
     * @return
	 */
    SnapshotVO get(Integer id, String owner);
}