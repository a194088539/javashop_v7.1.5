package com.enation.app.javashop.core.promotion.seckill.service;

import com.enation.app.javashop.core.promotion.seckill.model.dos.SeckillApplyDO;
import com.enation.app.javashop.core.promotion.seckill.model.dto.SeckillQueryParam;
import com.enation.app.javashop.core.promotion.seckill.model.vo.SeckillGoodsVO;
import com.enation.app.javashop.core.promotion.tool.model.dto.PromotionDTO;
import com.enation.app.javashop.framework.database.Page;

import java.util.List;
import java.util.Map;

/**
 * 限时抢购申请业务层
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-04-02 17:30:09
 */
public interface SeckillGoodsManager {

	/**
	 * 查询限时抢购申请列表
	 * @param queryParam 查询参数
	 * @return Page
	 */
	Page list(SeckillQueryParam queryParam);


	/**
	 * 删除限时抢购申请
	 * @param id 限时抢购申请主键
	 */
	void delete(Integer id);

	/**
	 * 获取限时抢购申请
	 * @param id 限时抢购申请主键
	 * @return SeckillApply  限时抢购申请
	 */
	SeckillApplyDO getModel(Integer id);


	/**
	 * 添加限时抢购申请
	 * @param list
	 */
	void addApply(List<SeckillApplyDO> list);

	/**
	 * 增加已销售库存数量
	 * @param promotionDTOList
	 */
	boolean addSoldNum(List<PromotionDTO> promotionDTOList);

	/**
	 * 读取当天限时抢购活动的商品
	 * @return
	 */
	Map<Integer, List<SeckillGoodsVO>> getSeckillGoodsList();


	/**
	 * 将商品压入缓存
	 * @param startTime
	 * @param rangeTime
	 * @param goodsVO
	 */
	void addRedis(Long startTime,Integer rangeTime,SeckillGoodsVO goodsVO);


	/**
	 * 根据时刻读取限时抢购商品列表
	 * @param rangeTime
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List getSeckillGoodsList(Integer rangeTime, Integer pageNo, Integer pageSize);

	/**
	 * 回滚库存
	 * @param promotionDTOList
	 */
	void rollbackStock(List<PromotionDTO> promotionDTOList);

	/**
	 * 删除限时抢购商品
	 */
	void deleteSeckillGoods(Integer goodsId);

	/**
	 * 根据限时抢购ID删除商品
	 * @param seckillId
	 */
	void deleteBySeckillId(Integer seckillId);
}
