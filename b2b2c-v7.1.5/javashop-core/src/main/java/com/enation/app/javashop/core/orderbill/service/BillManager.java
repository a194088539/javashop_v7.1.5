package com.enation.app.javashop.core.orderbill.service;

import com.enation.app.javashop.core.goods.model.enums.Permission;
import com.enation.app.javashop.core.orderbill.model.vo.BillDetail;
import com.enation.app.javashop.core.orderbill.model.vo.BillExcel;
import com.enation.app.javashop.core.orderbill.model.vo.BillQueryParam;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.orderbill.model.dos.Bill;

/**
 * 结算单业务层
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-04-26 16:21:26
 */
public interface BillManager	{

	/**
	 * 查询结算单列表
	 * @param page 页码
	 * @param pageSize 每页数量
	 * @return Page
	 */
	Page list(int page, int pageSize);
	/**
	 * 添加结算单
	 * @param bill 结算单
	 * @return Bill 结算单
	 */
	Bill add(Bill bill);
	
	/**
	 * 获取结算单
	 * @param id 结算单主键
	 * @return Bill  结算单
	 */
	Bill getModel(Integer id);

	/**
	 * 生成结算单
	 * @param startTime
	 * @param endTime
	 */
	void createBills(Long startTime,Long endTime);

	/**
	 * 查询账单列表
	 * @param param
	 * @return
	 */
    Page queryBills(BillQueryParam param);

	/**
	 * 修改账单的状态
	 * @param billId
	 * @param permission
	 * @return
	 */
	Bill editStatus(Integer billId, Permission permission);

	/**
	 * 获取结算单详细
	 * @param billId
	 * @param permission
	 * @return
	 */
	BillDetail getBillDetail(Integer billId,Permission permission);

	/**
	 * 查看每个周期的结果统计
	 * @param pageNo
	 * @param pageSize
	 * @param sn
	 * @return
	 */
	Page getAllBill(Integer pageNo, Integer pageSize, String sn);

	/**
	 * 结算单导出
	 * @param billId
	 * @return
	 */
	BillExcel exportBill(Integer billId);
}