package com.enation.app.javashop.consumer.core.event;

/**
* @author liuyulei
 * @version 1.0
 * @Description: 提示词变化
 * @date 2019/6/14 16:41
 * @since v7.0
 */
public interface GoodsWordsChangeEvent {


    /**
     * 提示词变化
     * @param words
     */
    void wordsChange(String words);
}
