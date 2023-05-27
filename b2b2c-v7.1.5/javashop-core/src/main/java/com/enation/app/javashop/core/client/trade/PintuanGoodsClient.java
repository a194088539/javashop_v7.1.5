package com.enation.app.javashop.core.client.trade;

/**
 * @author zh
 * @version v1.0
 * @Description: 拼团索引
 * @date 2019/3/5 10:33
 * @since v7.0.0
 */
public interface PintuanGoodsClient {

    /**
     * 生成拼团索引
     *
     * @param promotionId 拼团活动ID
     * @return 生成结果
     */
    boolean createGoodsIndex(Integer promotionId);


    /**
     * 删除某个商品的所有sku索引
     * @param goodsId
     */
    void deleteIndexByGoodsId(Integer goodsId);

    /**
     * 同步一个商品的所有sku的索引
     * @param goodsId
     */
    void syncIndexByGoodsId(Integer goodsId);

    /**
     * 删除拼团商品
     * @param goodsId
     */
    void delete(Integer goodsId);




}
