package com.enation.app.javashop.core.goods.service;

import com.enation.app.javashop.core.goods.model.dos.BrandDO;
import com.enation.app.javashop.core.goods.model.vo.SelectVO;
import com.enation.app.javashop.framework.database.Page;

import java.util.List;

/**
 * 品牌业务层
 * 
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-16 16:32:45
 */
public interface BrandManager {

	/**
	 * 查询品牌列表
	 * 
	 * @param page
	 *            页码
	 * @param pageSize
	 *            每页数量
	 * @param name
     * @return Page
	 */
	Page list(int page, int pageSize, String name);

	/**
	 * 添加品牌
	 * 
	 * @param brand
	 *            品牌
	 * @return Brand 品牌
	 */
	BrandDO add(BrandDO brand);

	/**
	 * 修改品牌
	 * 
	 * @param brand
	 *            品牌
	 * @param id
	 *            品牌主键
	 * @return Brand 品牌
	 */
	BrandDO edit(BrandDO brand, Integer id);

	/**
	 * 删除品牌
	 * 
	 * @param ids
	 *            品牌主键
	 */
	void delete(Integer[] ids);

	/**
	 * 获取品牌
	 * 
	 * @param id
	 *            品牌主键
	 * @return Brand 品牌
	 */
	BrandDO getModel(Integer id);

	/**
	 * 查询某分类下的品牌
	 * 
	 * @param categoryId
	 * @return
	 */
	List<BrandDO> getBrandsByCategory(Integer categoryId);

	/**
	 * 查询分类品牌，所有品牌，分类绑定的品牌为已选中状态
	 * 
	 * @param categoryId
	 * @return
	 */
	List<SelectVO> getCatBrand(Integer categoryId);


	/**查询全部的品牌
	 * @return
	 */
    List<BrandDO> getAllBrands();
}