package com.enation.app.javashop.core.shop.service;

import java.util.List;

import com.enation.app.javashop.core.shop.model.dos.ShipTemplateDO;
import com.enation.app.javashop.core.shop.model.vo.ShipTemplateSellerVO;
import com.enation.app.javashop.core.shop.model.vo.ShipTemplateVO;

/**
 * 运费模版业务层
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-28 21:44:49
 */
public interface ShipTemplateManager{


	/**
	 * 新增
	 * @param tamplate
	 * @return
	 */
	ShipTemplateDO save(ShipTemplateSellerVO tamplate);

	/**
	 * 修改
	 * @param template
	 * @return
	 */
	ShipTemplateDO edit(ShipTemplateSellerVO template);


	/**
	 * 获取商家运送方式
	 * @param sellerId
	 * @return
	 */
	List<ShipTemplateSellerVO> getStoreTemplate(Integer sellerId);


	/**
	 * 获取商家运送方式
	 * @param templateId
	 * @return
	 */
	ShipTemplateVO getFromCache(Integer templateId);

	/**
	 * 删除
	 * @param templateId
	 */
	void delete(Integer templateId);

	/**
	 * 数据库中查询一个运费模板VO
	 * @param templateId
	 * @return
	 */
	ShipTemplateSellerVO getFromDB(Integer templateId);

	/**
	 * 获取运费模板的脚本
	 * @param id
	 * @return
	 */
	List<String> getScripts(Integer id);

	/**
	 * 新增运费模板的时候，生成script缓存到redis
	 *
	 * @param shipTemplateVO 运费模板
	 */
	List<String> cacheShipTemplateScript(ShipTemplateVO shipTemplateVO);
}