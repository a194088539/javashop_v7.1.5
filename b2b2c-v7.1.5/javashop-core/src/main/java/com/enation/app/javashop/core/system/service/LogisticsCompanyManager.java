package com.enation.app.javashop.core.system.service;

import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.system.model.dos.LogisticsCompanyDO;

import java.util.List;

/**
 * 物流公司业务层
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-29 15:10:38
 */
public interface LogisticsCompanyManager {

	/**
	 * 查询物流公司列表
	 * @param page 页码
	 * @param pageSize 每页数量
	 * @param name 物流公司名称
	 * @return Page
	 */
	Page list(int page, int pageSize, String name);
	/**
	 * 添加物流公司
	 * @param logi 物流公司
	 * @return Logi 物流公司
	 */
	LogisticsCompanyDO add(LogisticsCompanyDO logi);

	/**
	* 修改物流公司
	* @param logi 物流公司
	* @param id 物流公司主键
	* @return Logi 物流公司
	*/
	LogisticsCompanyDO edit(LogisticsCompanyDO logi, Integer id);
	
	/**
	 * 删除物流公司
	 * @param id 物流公司主键
	 */
	void delete(Integer id);
	
	/**
	 * 获取物流公司
	 * @param id 物流公司主键
	 * @return Logi  物流公司
	 */
	LogisticsCompanyDO getModel(Integer id);
	
	/**
	 * 通过code获取物流公司
	 * @param code 物流公司code
	 * @return 物流公司
	 */
	LogisticsCompanyDO getLogiByCode(String code);
	/**
	 * 通过快递鸟物流code获取物流公司
	 * @param kdcode 快递鸟公司code
	 * @return 物流公司
	 */
	LogisticsCompanyDO getLogiBykdCode(String kdcode);
	
	/**
	 * 根据物流名称查询物流信息
	 * @param name 物流名称
	 * @return 物流公司
	 */
	LogisticsCompanyDO getLogiByName(String name);

	/**
	 * 查询物流公司列表(不分页)
	 * @return Page
	 */
	List<LogisticsCompanyDO> list();

	/**
	 * 开启或禁用物流公司
	 * @param id 物流公司主键ID
	 * @param disabled 状态 OPEN：开启，CLOSE：禁用
	 */
	void openCloseLogi(Integer id, String disabled);

	/**
	 * 查询平台添加的全部物流公司（正常使用未删除的）
	 * @return
	 */
	List<LogisticsCompanyDO> listAllNormal();

}