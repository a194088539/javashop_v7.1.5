package com.enation.app.javashop.core.client.distribution.impl;

import com.enation.app.javashop.core.client.distribution.BillMemberClient;
import com.enation.app.javashop.core.distribution.model.dos.DistributionOrderDO;
import com.enation.app.javashop.core.distribution.model.dto.DistributionRefundDTO;
import com.enation.app.javashop.core.distribution.service.BillMemberManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * BillMemberCliendDefaultImpl
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-08-14 下午2:00
 */
@Service
@ConditionalOnProperty(value = "javashop.product", havingValue = "stand")
public class BillMemberClientDefaultImpl implements BillMemberClient {

    @Autowired
    private BillMemberManager billMemberManager;

    /**
     * 购买商品产生的结算
     *
     * @param order
     */
    @Override
    public void buyShop(DistributionOrderDO order) {
        billMemberManager.buyShop(order);
    }

    /**
     * 退货商品产生的结算
     *
     * @param order
     */
    @Override
    public void returnShop(DistributionOrderDO order, DistributionRefundDTO distributionRefundDTO) {
        billMemberManager.returnShop(order,distributionRefundDTO);
    }
}
