package com.enation.app.javashop.core.client.goods;

import com.enation.app.javashop.core.goods.model.dos.CategoryDO;
import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.model.dto.GoodsQueryParam;
import com.enation.app.javashop.core.goods.model.vo.*;
import com.enation.app.javashop.core.trade.order.model.vo.OrderSkuVO;
import com.enation.app.javashop.framework.database.Page;

import java.util.List;
import java.util.Map;

/**
 * @author fk
 * @version v1.0
 * @Description: 商品对外的接口
 * @date 2018/7/26 10:33
 * @since v7.0.0
 */
public interface GoodsClient {

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
     * 缓存中查询商品的信息
     *
     * @param goodsId
     * @return
     */
    CacheGoods getFromCache(Integer goodsId);

    /**
     * 查询sku信息
     *
     * @param skuId
     * @return
     */
    GoodsSkuVO getSku(Integer skuId);

    /**
     * 查询多个商品的基本信息
     *
     * @param goodsIds
     * @return
     */
    List<GoodsSelectLine> query(Integer[] goodsIds);

    /**
     * 查询多个商品的基本信息
     *
     * @param goodsIds
     * @return
     */
    List<GoodsDO> queryDo(Integer[] goodsIds);

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
     * @param goodsIds 商品id集合
     * @return
     */
    List<Map<String, Object>> getGoodsAndParams(Integer[] goodsIds);

    /**
     * 查询商品信息
     *
     * @param goodsIds 商品id集合
     * @return
     */
    List<Map<String, Object>> getGoods(Integer[] goodsIds);

    /**
     * 根基商家id查询商家商品的信息和参数信息
     *
     * @param sellerId 商户id
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
     * 缓存中查询sku信息
     *
     * @param skuId
     * @return
     */
    GoodsSkuVO getSkuFromCache(Integer skuId);

    /**
     * 更新商品的评论数量
     *
     * @param goodsId
     * @param num
     */
    void updateCommentCount(Integer goodsId, Integer num);


    /**
     * 更新商品的购买数量
     *
     * @param list
     */
    void updateBuyCount(List<OrderSkuVO> list);


    /**
     * 查询商品总数
     *
     * @return 商品总数
     */
    Integer queryGoodsCount();

    /**
     * 根据条件查询商品总数
     *
     * @param status   商品状态
     * @param sellerId 商家id
     * @return 商品总数
     */
    Integer queryGoodsCountByParam(Integer status, Integer sellerId);

    /**
     * 查询某范围的商品信息
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<Map> queryGoodsByRange(Integer pageNo, Integer pageSize);

    /**
     * 添加商品快照时使用的接口
     *
     * @param goodsId
     * @return
     */
    GoodsSnapshotVO queryGoodsSnapShotInfo(Integer goodsId);

    /**
     * 查询商品列表
     *
     * @param goodsQueryParam
     * @return
     */
    Page list(GoodsQueryParam goodsQueryParam);

    /**
     * 获取商品分类
     *
     * @param id 商品分类主键
     * @return Category 商品分类
     */
    CategoryDO getCategory(Integer id);

    /**
     * 校验商品模版是否使用
     *
     * @param templateId
     * @return 商品
     */
    GoodsDO checkShipTemplate(Integer templateId);

    /**
     * 查询某店铺正在售卖中的商品数量
     *
     * @param sellerId
     * @return
     */
    Integer getSellerGoodsCount(Integer sellerId);


    /**
     * 修改某店铺商品店铺名称
     *
     * @param sellerId
     * @param sellerName
     */
    void changeSellerName(Integer sellerId, String sellerName);

    /**
     * 更新商品类型
     *
     * @param type 商品状态
     */
    void updateGoodsType(Integer sellerId, String type);


    /**
     * 查询某店铺的积分商品
     *
     * @param shopId
     * @return
     */
    List<GoodsDO> listPointGoods(Integer shopId);

    /**
     * 根据商品id查询所有sku
     *
     * @param goodsId 商品id
     * @return 所有sku
     */
    List<GoodsSkuVO> listByGoodsId(Integer goodsId);

    /**
     * 根据商家id查询出所有的商品信息
     *
     * @param sellerId 商家id
     * @return 所有的商品信息
     */
    List<GoodsDO> getSellerGoods(Integer sellerId);
}
