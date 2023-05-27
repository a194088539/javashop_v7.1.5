package com.enation.app.javashop.core.client.goods.impl;

import com.enation.app.javashop.core.client.goods.SearchKeywordClient;
import com.enation.app.javashop.core.goodssearch.service.SearchKeywordManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
* @author liuyulei
 * @version 1.0
 * @Description:  关键词所搜历史对外接口实现
 * @date 2019/5/28 11:09
 * @since v7.0
 */

@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class SearchKeywordClientImpl implements SearchKeywordClient {

    @Autowired
    private SearchKeywordManager searchKeywordManager;

    @Override
    public void add(String keyword) {
        this.searchKeywordManager.add(keyword);
    }

    @Override
    public void update(String keyword) {
        this.searchKeywordManager.update(keyword);
    }

    @Override
    public boolean isExist(String keyword) {
        return this.searchKeywordManager.isExist(keyword);
    }
}
