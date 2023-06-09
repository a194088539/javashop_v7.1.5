package com.enation.app.javashop.core.goods.service;

import java.util.List;

import com.enation.app.javashop.core.goods.model.dos.GoodsParamsDO;
import com.enation.app.javashop.core.goods.model.vo.GoodsParamsGroupVO;

/**
 * 草稿商品参数表业务层
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-03-26 11:31:20
 */
public interface DraftGoodsParamsManager	{

	/**
	 * 添加草稿商品的参数集合
	 * @param goodsParamsList
	 * @param draftGoodsId
	 */
	void addParams(List<GoodsParamsDO> goodsParamsList, Integer draftGoodsId);

	/**
	 * 查询分类关联的参数，同时返回已经添加的值
	 * @param categoryId
	 * @param draftGoodsId
	 * @return
	 */
	List<GoodsParamsGroupVO> getParamByCatAndDraft(Integer categoryId, Integer draftGoodsId);

}