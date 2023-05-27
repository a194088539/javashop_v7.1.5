package com.enation.app.javashop.core.client.system;

import com.enation.app.javashop.core.pagedata.model.PageData;

/**
 * 静态页渲染客户端
 *
 * @author zh
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/8 下午4:10
 */
public interface PageDataClient {

    /**
     * 获取楼层数据
     *
     * @param clientType 客户端类型
     * @param pageType   楼层类型
     */
    PageData getByType(String clientType, String pageType);

    /**
     * 修改楼层商品
     *
     * @param page 数据
     * @param id   id
     * @return
     */
    PageData edit(PageData page, Integer id);


}
