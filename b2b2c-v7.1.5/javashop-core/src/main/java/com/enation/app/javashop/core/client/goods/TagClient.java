package com.enation.app.javashop.core.client.goods;

/**
 * 商品标签对外接口
 *
 * @author zh
 * @version v7.0
 * @date 19/3/21 下午3:27
 * @since v7.0
 */
public interface TagClient {
    /**
     * 增加店铺标签
     *
     * @param sellerId 商家id
     */
    void addShopTags(Integer sellerId);
}
