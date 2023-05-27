package com.enation.app.javashop.core.goods.service;

import com.enation.app.javashop.framework.database.Page;

import java.util.List;

import com.enation.app.javashop.core.goods.model.dos.SpecificationDO;
import com.enation.app.javashop.core.goods.model.vo.SelectVO;
import com.enation.app.javashop.core.goods.model.vo.SpecificationVO;

/**
 * 规格项业务层
 * 
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-20 09:31:27
 */
public interface SpecificationManager {

	/**
	 * 查询规格项列表
	 * 
	 * @param page
	 *            页码
	 * @param pageSize
	 *            每页数量
	 * @param keyword
	 * 			  关键字
	 * @return Page
	 */
	Page list(int page, int pageSize, String keyword);

	/**
	 * 添加规格项
	 * 
	 * @param specification
	 *            规格项
	 * @return Specification 规格项
	 */
	SpecificationDO add(SpecificationDO specification);

	/**
	 * 修改规格项
	 * 
	 * @param specification
	 *            规格项
	 * @param id
	 *            规格项主键
	 * @return Specification 规格项
	 */
	SpecificationDO edit(SpecificationDO specification, Integer id);

	/**
	 * 删除规格项
	 * 
	 * @param ids
	 *            规格项主键
	 */
	void delete(Integer[] ids);

	/**
	 * 获取规格项
	 * 
	 * @param id
	 *            规格项主键
	 * @return Specification 规格项
	 */
	SpecificationDO getModel(Integer id);

	/**
	 * 查询分类绑定的规格，系统规格
	 * 
	 * @param categoryId
	 * @return
	 */
	List<SelectVO> getCatSpecification(Integer categoryId);

	/**
	 * 商家自定义规格
	 * 
	 * @param categoryId
	 * @param specName
	 * @return
	 */
	SpecificationDO addSellerSpec(Integer categoryId, String specName);

	/**
	 * 商家查询某分类的规格
	 * 
	 * @param categoryId
	 * @return
	 */
	List<SpecificationVO> querySellerSpec(Integer categoryId);

}