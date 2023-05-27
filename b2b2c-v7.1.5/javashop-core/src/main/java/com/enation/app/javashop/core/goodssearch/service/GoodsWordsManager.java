package com.enation.app.javashop.core.goodssearch.service;


import com.enation.app.javashop.core.goodssearch.enums.GoodsWordsType;
import com.enation.app.javashop.framework.database.Page;

/**
 * @author fk
 * @version v2.0
 * @Description: 商品分词Manager
 * @date 2019/12/6 11:04
 * @since v7.0.0
 */
public interface GoodsWordsManager {

    /**
     * 添加一个分词
     * @param word
     */
    void addWord(String word);

    /**
     * 修改提示词
     * @param word
     * @param id
     */
    void updateWords(String word,Integer id);

    /**
     * 修改排序
     * @param id
     * @param sort
     */
    void updateSort(Integer id ,Integer sort);

    /**
     * 根据分词查询列表
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @return
     */
    Page listPage(Integer pageNo, Integer pageSize, String keyword);

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
