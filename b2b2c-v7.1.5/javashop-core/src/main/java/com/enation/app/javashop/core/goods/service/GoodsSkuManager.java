package com.enation.app.javashop.core.goods.service;

import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.model.dos.GoodsSkuDO;
import com.enation.app.javashop.core.goods.model.dto.GoodsQueryParam;
import com.enation.app.javashop.core.goods.model.vo.GoodsSkuVO;
import com.enation.app.javashop.framework.database.Page;

import java.util.List;

/**
 * 商品sku业务层
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-21 11:48:40
 */
public interface GoodsSkuManager {
    /**
     * 查询SKU列表
     * @param param
     * @return
     */
    Page list(GoodsQueryParam param);

    /**
     * 添加商品sku
     *
     * @param skuList
     * @param goods
     */
    void add(List<GoodsSkuVO> skuList, GoodsDO goods);

    /**
     * 修改商品sku
     *
     * @param skuList
     * @param goods
     */
    void edit(List<GoodsSkuVO> skuList, GoodsDO goods);

    /**
     * 删除商品关联的sku
     *
     * @param goodsIds
     */
    void delete(Integer[] goodsIds);

    /**
     * 查询某商品的sku
     *
     * @param goodsId
     * @return
     */
    List<GoodsSkuVO> listByGoodsId(Integer goodsId);

    /**
     * 缓存中查询sku信息
     *
     * @param skuId
     * @return
     */
    GoodsSkuVO getSkuFromCache(Integer skuId);

    /**
     * 查询sku信息
     *
     * @param skuId
     * @return
     */
    GoodsSkuVO getSku(Integer skuId);

    /**
     * 查询某商家的可售卖的商品的sku集合
     *
     * @return
     */
    List<GoodsSkuDO> querySellerAllSku();

    /**
     * 判断商品是否都是某商家的商品
     *
     * @param skuIds
     * @return
     */
    void checkSellerGoodsCount(Integer[] skuIds);

    /**
     * 查询单个sku
     *
     * @param id
     * @return
     */
    GoodsSkuDO getModel(Integer id);


    /**
     * 根据商品sku主键id集合获取商品信息
     * @param skuIds
     * @return
     */
    List<GoodsSkuVO> query(Integer[] skuIds);
}