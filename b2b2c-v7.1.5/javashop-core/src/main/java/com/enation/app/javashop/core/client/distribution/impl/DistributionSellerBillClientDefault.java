package com.enation.app.javashop.core.client.distribution.impl;

import com.enation.app.javashop.core.client.distribution.DistributionSellerBillClient;
import com.enation.app.javashop.core.distribution.model.dto.DistributionSellerBillDTO;
import com.enation.app.javashop.core.distribution.service.DistributionSellerBillManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 默认实现
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-09-07 上午8:11
 */

@Service
@ConditionalOnProperty(value = "javashop.product", havingValue = "stand")
public class DistributionSellerBillClientDefault implements DistributionSellerBillClient {

    @Autowired
    private DistributionSellerBillManager distributionSellerBillManager;
    /**
     * 获取某个周期的返现支出
     *
     * @param startTime 开始时间
     * @param endTime   开始时间
     * @return 周期内返现DTO
     */
    @Override
    public List<DistributionSellerBillDTO> countSeller(Integer startTime, Integer endTime) {
        return distributionSellerBillManager.countSeller(startTime,endTime);
    }
}
