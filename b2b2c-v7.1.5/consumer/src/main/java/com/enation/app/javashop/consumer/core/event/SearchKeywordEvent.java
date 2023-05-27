package com.enation.app.javashop.consumer.core.event;

/**
* @author liuyulei
 * @version 1.0
 * @Description: 搜索关键字历史变更事件
 * @date 2019/5/27 12:01
 * @since v7.0
 */
public interface SearchKeywordEvent {

    void updateOrAdd(String keyword);
}
