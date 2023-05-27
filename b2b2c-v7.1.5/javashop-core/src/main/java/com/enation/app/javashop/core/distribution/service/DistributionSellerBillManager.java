package com.enation.app.javashop.core.distribution.service;


import com.enation.app.javashop.core.distribution.model.dos.DistributionOrderDO;
import com.enation.app.javashop.core.distribution.model.dto.DistributionSellerBillDTO;

import java.util.List;

/**
 *
 * 商家返现计算
 * @author liushuai
 * @version v1.0
 * @since v7.0
 * 2018/9/4 下午11:41
 * @Description:
 *
 */
public interface DistributionSellerBillManager {


    /**
     * 新增记录
     * @param distributionOrderDO
     */
    void add(DistributionOrderDO distributionOrderDO);
    /**
     * 新增退款记录
     * @param distributionOrderDO
     */
    void addRefund(DistributionOrderDO distributionOrderDO);

    /**
     * 商家返现统计
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return
     */
    List<DistributionSellerBillDTO> countSeller(Integer startTime, Integer endTime);

}
