package com.enation.app.javashop.core.goods.service;

import com.enation.app.javashop.core.goods.model.dos.CategoryBrandDO;
import com.enation.app.javashop.core.goods.model.dos.CategoryDO;
import com.enation.app.javashop.core.goods.model.dos.CategorySpecDO;
import com.enation.app.javashop.core.goods.model.vo.CategoryVO;

import java.util.List;

/**
 * 商品分类业务层
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-15 17:22:06
 */
public interface CategoryManager {

	/**
	 * 添加商品分类
	 *
	 * @param category
	 *            商品分类
	 * @return Category 商品分类
	 */
	CategoryDO add(CategoryDO category);

	/**
	 * 修改商品分类
	 *
	 * @param category
	 *            商品分类
	 * @param id
	 *            商品分类主键
	 * @return Category 商品分类
	 */
	CategoryDO edit(CategoryDO category, Integer id);

	/**
	 * 删除商品分类
	 *
	 * @param id
	 *            商品分类主键
	 */
	void delete(Integer id);

	/**
	 * 获取商品分类
	 *
	 * @param id
	 *            商品分类主键
	 * @return Category 商品分类
	 */
	CategoryDO getModel(Integer id);

	/**
	 * 查询某分类下的子分类
	 *
	 * @param parentId
	 * @param format
	 * @return
	 */
	List list(Integer parentId, String format);

	/**
	 * 获取卖家经营类目对应的分类
	 *
	 * @param categoryId
	 * @return
	 */
	List<CategoryDO> getSellerCategory(Integer categoryId);

	/**
	 * 查询所有的分类，父子关系
	 *
	 * @param parentId
	 * @return
	 */
	List<CategoryVO> listAllChildren(Integer parentId);


	/**
	 * 查询店铺
	 *
	 * @return
	 */
	List<CategoryVO> listAllSellerChildren();

	/**
	 * 保存分类绑定的品牌
	 *
	 * @param categoryId
	 * @param chooseBrands
	 * @return
	 */
	List<CategoryBrandDO> saveBrand(Integer categoryId, Integer[] chooseBrands);

	/**
	 * 保存分类绑定的规格
	 *
	 * @param categoryId
	 * @param chooseSpecs
	 * @return
	 */
	List<CategorySpecDO> saveSpec(Integer categoryId, Integer[] chooseSpecs);

}