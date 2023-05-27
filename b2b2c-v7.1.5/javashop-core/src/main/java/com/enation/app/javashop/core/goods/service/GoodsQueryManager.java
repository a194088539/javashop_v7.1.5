package com.enation.app.javashop.core.goods.service;

import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.model.dto.GoodsQueryParam;
import com.enation.app.javashop.core.goods.model.vo.BuyCountVO;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.goods.model.vo.GoodsSelectLine;
import com.enation.app.javashop.core.goods.model.vo.GoodsVO;
import com.enation.app.javashop.framework.database.Page;

import java.util.List;
import java.util.Map;

/**
 * 商品业务层
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-21 11:23:10
 */
public interface GoodsQueryManager {

    /**
     * 查询商品列表
     *
     * @param goodsQueryParam
     * @return
     */
    Page list(GoodsQueryParam goodsQueryParam);

    /**
     * 查询商品排序列表
     *
     * @param goodsQueryParam
     * @return
     */
    Page goodsPriorityList(GoodsQueryParam goodsQueryParam);

    /**
     * 获取商品
     *
     * @param id 商品主键
     * @return Goods 商品
     */
    GoodsDO getModel(Integer id);

    /**
     * 商家查询商品,编辑查看使用
     *
     * @param goodsId
     * @return
     */
    GoodsVO sellerQueryGoods(Integer goodsId);

    /**
     * 缓存中查询商品的信息
     *
     * @param goodsId
     * @return
     */
    CacheGoods getFromCache(Integer goodsId);

    /**
     * 查询预警货品的商品
     *
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @return
     */
    Page warningGoodsList(int pageNo, int pageSize, String keyword);

    /**
     * 查询多个商品的基本信息
     *
     * @param goodsIds
     * @param sellerId 某商家 null 查全部
     * @return
     */
    List<GoodsSelectLine> query(Integer[] goodsIds, Integer sellerId);

    /**
     * 查询多个商品的信息
     *
     * @param goodsIds
     * @return
     */
    List<GoodsDO> queryDo(Integer[] goodsIds);

    /**
     * 查询商品的好平率
     *
     * @param goodsId
     * @return
     */
    Double getGoodsGrade(Integer goodsId);

    /**
     * 判断商品是否都是某商家的商品
     *
     * @param goodsIds
     * @return
     */
    void checkSellerGoodsCount(Integer[] goodsIds);

    /**
     * 查询很多商品的信息和参数信息
     *
     * @param goodsIds
     * @return
     */
    List<Map<String, Object>> getGoodsAndParams(Integer[] goodsIds);

    /**
     * 查询一个商家的所有在售商品
     *
     * @param sellerId
     * @return
     */
    List<Map<String, Object>> getGoodsAndParams(Integer sellerId);

    /**
     * 按销量查询若干条数据
     *
     * @param sellerId
     * @return
     */
    List<GoodsDO> listGoods(Integer sellerId);

    /**
     * 查询购买量
     *
     * @param goodsIds
     * @return
     */
    List<BuyCountVO> queryBuyCount(Integer[] goodsIds);

    /**
     * 获取商品分类路径
     *
     * @param id
     * @return
     */
    String queryCategoryPath(Integer id);

    /**
     * 查询某店铺正在售卖中的商品数量
     *
     * @param sellerId
     * @return
     */
    Integer getSellerGoodsCount(Integer sellerId);

    /**
     * 查看某商品是否在配送范围
     * @param goodsId
     * @param areaId
     * @return
     */
    Integer checkArea(Integer goodsId, Integer areaId);

    /**
     * 根据条件查询商品数
     *
     * @param status   商品状态
     * @param sellerId 商家id
     * @param disabled 商品是否已删除
     * @return 商品数
     */
    Integer getGoodsCountByParam(Integer status, Integer sellerId, Integer disabled);

    /**
     * 根据商品id集合查询商品信息
     *
     * @param goodsIds 商品ids
     * @return  商品信息
     */
    List<Map<String, Object>> getGoods(Integer[] goodsIds);

    /**
     * 查询商家所有的商品
     * @param sellerId  商家id
     * @return  所有商品
     *
     */
    List<GoodsDO> getSellerGoods(Integer sellerId);
}