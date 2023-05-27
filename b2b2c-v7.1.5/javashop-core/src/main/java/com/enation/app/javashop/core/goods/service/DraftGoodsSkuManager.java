package com.enation.app.javashop.core.goods.service;

import java.util.List;

import com.enation.app.javashop.core.goods.model.dto.GoodsDTO;
import com.enation.app.javashop.core.goods.model.vo.GoodsSkuVO;

/**
 * 草稿商品sku业务层
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-03-26 11:38:06
 */
public interface DraftGoodsSkuManager	{

	/**
	 * 添加sku规格列表
	 * @param goodsVO
	 * @param draftGoodsId
	 */
	void add(GoodsDTO goodsVO, Integer draftGoodsId);

	/**
	 * 查询草稿箱的sku列表
	 * @param draftGoodsId
	 * @return
	 */
	List<GoodsSkuVO> getSkuList(Integer draftGoodsId);

}