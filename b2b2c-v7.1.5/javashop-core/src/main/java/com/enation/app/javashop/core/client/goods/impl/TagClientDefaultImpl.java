package com.enation.app.javashop.core.client.goods.impl;

import com.enation.app.javashop.core.client.goods.TagClient;
import com.enation.app.javashop.core.goods.service.TagsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 商品标签对外接口实现
 *
 * @author zh
 * @version v7.0
 * @date 19/3/21 下午3:29
 * @since v7.0
 */
@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class TagClientDefaultImpl implements TagClient {

    @Autowired
    private TagsManager tagsManager;

    @Override
    public void addShopTags(Integer sellerId) {
        tagsManager.addShopTags(sellerId);
    }
}
