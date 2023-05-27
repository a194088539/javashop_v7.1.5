package com.enation.app.javashop.core.client.member;

import java.util.List;

/**
 * 店铺物流client
 *
 * @author fk
 * @version v7.0
 * @date 19/7/27 下午3:51
 * @since v7.0
 */
public interface ShopLogisticsCompanyClient {

    /**
     * 查询绑定该物流公司的列表
     * @param logisticsId
     * @return
     */
    List queryListByLogisticsId(Integer logisticsId);

    /**
     * 删除绑定某物流公司的所有店铺
     * @param logisticsId
     */
    void deleteByLogisticsId(Integer logisticsId);

}
