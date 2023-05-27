package com.enation.app.javashop.core.client.goods;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
* @author liuyulei
 * @version 1.0
 * @Description: 关键词所搜历史对外接口
 * @date 2019/5/28 11:07
 * @since v7.0
 */
public interface SearchKeywordClient {

    /**
     * 添加搜索关键字
     * @param keyword
     */
    void add(String keyword);

    /**
     * 更新关键字数据
     * @param keyword
     */
    void update(String keyword);

    /**
     * 判断关键字是否存在
     * @param keyword
     * @return
     */
    boolean isExist(String keyword);
}
