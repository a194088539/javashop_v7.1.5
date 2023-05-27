package com.enation.app.javashop.core.client.distribution;

import com.enation.app.javashop.core.distribution.model.dto.DistributionSellerBillDTO;

import java.util.List;

/**
 * 分销结算单接口
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-09-06 下午4:38
 */
public interface DistributionSellerBillClient {
    /**
     * 获取某个周期的返现支出
     * @param startTime 开始时间
     * @param endTime 开始时间
     * @return 周期内返现DTO
     */
    List<DistributionSellerBillDTO> countSeller(Integer startTime, Integer endTime);



}
