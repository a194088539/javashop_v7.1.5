package com.enation.app.javashop.core.goods.service;

import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.model.dto.GoodsAuditParam;
import com.enation.app.javashop.core.goods.model.dto.GoodsDTO;
import com.enation.app.javashop.core.goods.model.enums.Permission;
import com.enation.app.javashop.core.goods.model.vo.BackendGoodsVO;

import java.util.List;

/**
 * 商品业务层
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-21 11:23:10
 */
public interface GoodsManager {


    /**
     * 修改商品
     *
     * @param goodsVo 商品
     * @param id      商品主键
     * @return Goods 商品
     */
    GoodsDO edit(GoodsDTO goodsVo, Integer id);


    /**
     * 添加商品
     *
     * @param goodsVo
     * @return
     */
    GoodsDO add(GoodsDTO goodsVo);

    /**
     * 商品下架
     *
     * @param goodsIds
     * @param reason
     * @param permission
     */
    void under(Integer[] goodsIds, String reason, Permission permission);

    /**
     * 商品放入回收站
     *
     * @param goodsIds
     */
    void inRecycle(Integer[] goodsIds);

    /**
     * 商品删除
     *
     * @param goodsIds
     */
    void delete(Integer[] goodsIds);

    /**
     * 回收站还原商品
     *
     * @param goodsIds
     */
    void revert(Integer[] goodsIds);

    /**
     * 上架商品
     *
     * @param goodsId
     */
    void up(Integer goodsId);

    /**
     * 批量审核商品
     * 管理员使用
     * @param param
     */
    void batchAuditGoods(GoodsAuditParam param);

    /**
     * 增加商品的浏览次数
     *
     * @param goodsId
     * @return
     */
    Integer visitedGoodsNum(Integer goodsId);


    /**
     * 获取新赠商品
     *
     * @param length
     * @return
     */
    List<BackendGoodsVO> newGoods(Integer length);

    /**
     * 下架某店铺的全部商品
     *
     * @param sellerId
     */
    void underShopGoods(Integer sellerId);

    /**
     * 更新商品好平率
     */
    void updateGoodsGrade();

    /**
     * 获取商品是否使用检测的模版
     *
     * @param templateId
     * @return 商品
     */
    GoodsDO checkShipTemplate(Integer templateId);

    /**
     * 修改某店铺商品店铺名称
     *
     * @param sellerId   商家id
     * @param sellerName 商家名称
     */
    void changeSellerName(Integer sellerId, String sellerName);

    /**
     * 更改商品类型
     *
     * @param sellerId
     * @param type
     */
    void updateGoodsType(Integer sellerId, String type);

    /**
     * 修改商品优先级别
     * @param goodsId
     * @param priority
     */
    void updatePriority(Integer goodsId,Integer priority);
}