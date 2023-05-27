package com.enation.app.javashop.consumer.shop.goodssearch;

import com.enation.app.javashop.consumer.core.event.SearchKeywordEvent;
import com.enation.app.javashop.core.client.goods.SearchKeywordClient;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author liuyulei
 * @version 1.0
 * @Description:  搜索关键词记录历史
 * @date 2019/5/27 12:05
 * @since v7.0
 */
@Service
public class SearchKeywordUpdateConsumer  implements SearchKeywordEvent {

    @Autowired
    private SearchKeywordClient searchKeywordClient;

    @Override
    public void updateOrAdd(String keyword) {

        if(!StringUtil.isEmpty(keyword)){
            boolean isExit = this.searchKeywordClient.isExist(keyword);
            if(!isExit){
                this.searchKeywordClient.add(keyword);
            }else{
                this.searchKeywordClient.update(keyword);
            }
        }

    }
}
