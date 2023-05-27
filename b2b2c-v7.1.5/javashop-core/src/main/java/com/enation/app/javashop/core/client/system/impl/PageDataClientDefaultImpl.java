package com.enation.app.javashop.core.client.system.impl;

import com.enation.app.javashop.core.client.system.PageDataClient;
import com.enation.app.javashop.core.pagedata.model.PageData;
import com.enation.app.javashop.core.pagedata.service.PageDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 楼层渲染实现
 *
 * @author zh
 * @version v1.0
 * @since v7.0
 * 2018-08-14 下午2:37
 */
@Service
@ConditionalOnProperty(value = "javashop.product", havingValue = "stand")
public class PageDataClientDefaultImpl implements PageDataClient {

    @Autowired
    private PageDataManager pageDataManager;

    @Override
    public PageData getByType(String clientType, String pageType) {
        return pageDataManager.getByType(clientType, pageType);
    }

    @Override
    public PageData edit(PageData page, Integer id) {
        return pageDataManager.edit(page,id);
    }
}
