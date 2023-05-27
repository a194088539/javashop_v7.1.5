package com.enation.app.javashop.core.client.goods;

import com.enation.app.javashop.core.goodssearch.enums.GoodsWordsType;
import com.enation.app.javashop.framework.database.Page;

/**
 * @author fk
 * @version v2.0
 * @Description: 商品分词client
 * @date 2018/8/21 11:04
 * @since v7.0.0
 */
public interface GoodsWordsClient {

    /**
     * 删除某个分词
     * @param words
     */
    void delete(String words);

    /**
     * 添加一组分词，存在累加数量，不存在新增
     * @param words
     */
    void addWords(String words);

    /**
     * 删除
     * @param goodsWordsType
     * @param id
     */
    void delete(GoodsWordsType goodsWordsType, Integer id);

    /**
     * 变更商品数量
     * @param words
     */
    void updateGoodsNum(String words);

    /**
     * 变更所有平台提示词商品数量
     */
    void batchUpdateGoodsNum();

}
