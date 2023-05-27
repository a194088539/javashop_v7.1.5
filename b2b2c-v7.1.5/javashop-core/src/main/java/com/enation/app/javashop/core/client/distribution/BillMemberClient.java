package com.enation.app.javashop.core.client.distribution;

import com.enation.app.javashop.core.distribution.model.dos.DistributionOrderDO;
import com.enation.app.javashop.core.distribution.model.dto.DistributionRefundDTO;

/**
 * DistributionClient
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-08-14 下午1:54
 */
public interface BillMemberClient {

    /**
     * 购买商品产生的结算
     *
     * @param order
     */
    void buyShop(DistributionOrderDO order);


    /**
     * 退货商品产生的结算
     *
     * @param order
     */
    void returnShop(DistributionOrderDO order, DistributionRefundDTO distributionRefundDTO);
}
