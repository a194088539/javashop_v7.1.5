package com.enation.app.javashop.core.goodssearch.service;

import com.enation.app.javashop.framework.database.Page;

/**
* @author liuyulei
 * @version 1.0
 * @Description:  搜索关键字历史业务类
 * @date 2019/5/27 11:11
 * @since v7.0
 */
public interface SearchKeywordManager {

    /**
     * 添加搜索关键字
     * @param keyword
     */
    void add(String keyword);

    /**
     * 关键字历史列表
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @return
     */
    Page list(Integer pageNo,Integer pageSize,String keyword);


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
