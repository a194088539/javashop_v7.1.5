package com.enation.app.javashop.core.orderbill.service;

import com.enation.app.javashop.core.orderbill.model.vo.BillResult;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.orderbill.model.dos.BillItem;

import java.util.Map;

/**
 * 结算单项表业务层
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-04-26 15:39:57
 */
public interface BillItemManager	{

	/**
	 * 查询结算单项表列表
	 * @param page
	 * @param pageSize
	 * @param billId
	 * @param billType
	 * @return
	 */
	Page list(int page, int pageSize,Integer billId,String billType);
	/**
	 * 添加结算单项表
	 * @param billItem 结算单项表
	 * @return BillItem 结算单项表
	 */
	BillItem add(BillItem billItem);

	/**
	* 修改结算单项表
	* @param billItem 结算单项表
	* @param id 结算单项表主键
	* @return BillItem 结算单项表
	*/
	BillItem edit(BillItem billItem, Integer id);
	
	/**
	 * 删除结算单项表
	 * @param id 结算单项表主键
	 */
	void delete(Integer id);
	
	/**
	 * 获取结算单项表
	 * @param id 结算单项表主键
	 * @return BillItem  结算单项表
	 */
	BillItem getModel(Integer id);

	/**
	 * 更新结算项的状态
	 * @param sellerId
	 * @param billId
	 * @param startTime
	 * @param lastTime
	 */
    void updateBillItem(Integer sellerId, Integer billId,String startTime, String lastTime);

	/**
	 * 查询结算单项的统计结果
	 * @param lastTime
	 * @param startTime
	 * @return key是卖家id
	 */
	Map<Integer,BillResult> countBillResultMap(String startTime,String lastTime);
}