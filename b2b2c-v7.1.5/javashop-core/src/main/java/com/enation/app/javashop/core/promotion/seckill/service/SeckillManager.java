package com.enation.app.javashop.core.promotion.seckill.service;

import com.enation.app.javashop.core.promotion.seckill.model.dto.SeckillAuditParam;
import com.enation.app.javashop.core.promotion.seckill.model.dto.SeckillQueryParam;
import com.enation.app.javashop.core.promotion.seckill.model.vo.SeckillGoodsVO;
import com.enation.app.javashop.core.promotion.seckill.model.vo.SeckillVO;
import com.enation.app.javashop.framework.database.Page;

/**
 * 限时抢购入库业务层
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-21 10:32:36
 */
public interface SeckillManager	{

	/**
	 * 查询限时抢购入库列表
	 * @param param 搜索参数
	 * @return Page
	 */
	Page list(SeckillQueryParam param);

	/**
	 * 添加限时抢购入库
	 * @param seckill 限时抢购入库
	 * @return Seckill 限时抢购入库
	 */
	SeckillVO add(SeckillVO seckill);

	/**
	* 修改限时抢购入库
	* @param seckill 限时抢购入库
	* @param id 限时抢购入库主键
	* @return Seckill 限时抢购入库
	*/
	SeckillVO edit(SeckillVO seckill, Integer id);

	/**
	 * 删除限时抢购入库
	 * @param id 限时抢购入库主键
	 */
	void delete(Integer id);

	/**
	 * 获取限时抢购入库
	 * @param id 限时抢购入库主键
	 * @return Seckill  限时抢购入库
	 */
	SeckillVO getModel(Integer id);

	/**
	 * 根据商品ID，读取限时秒杀的活动信息
	 * @param goodsId
	 * @return
	 */
	SeckillGoodsVO getSeckillGoods(Integer goodsId);

	/**
	 * 批量审核商品
	 * @param param
	 */
	void batchAuditGoods(SeckillAuditParam param);

	/**
	 * 商家报名限时抢购活动
	 * @param sellerId
	 * @param seckillId
	 */
	void sellerApply(Integer sellerId,Integer seckillId);

	/**
	 * 关闭某限时抢购
	 * @param id
	 */
    void close(Integer id);
}
