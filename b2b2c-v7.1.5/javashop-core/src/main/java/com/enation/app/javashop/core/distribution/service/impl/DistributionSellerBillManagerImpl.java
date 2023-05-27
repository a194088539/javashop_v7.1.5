package com.enation.app.javashop.core.distribution.service.impl;

import com.enation.app.javashop.core.distribution.model.dos.DistributionSellerBillDO;
import com.enation.app.javashop.core.distribution.model.dos.DistributionOrderDO;
import com.enation.app.javashop.core.distribution.model.dto.DistributionSellerBillDTO;
import com.enation.app.javashop.core.distribution.service.DistributionSellerBillManager;
import com.enation.app.javashop.core.statistics.util.DateUtil;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.util.CurrencyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 实现
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-09-05 下午4:12
 */
@Service
public class DistributionSellerBillManagerImpl implements DistributionSellerBillManager {


    @Autowired
    @Qualifier("distributionDaoSupport")
    private DaoSupport daoSupport;

    /**
     * 新增记录
     *
     * @param distributionOrderDO
     */
    @Override
    public void add(DistributionOrderDO distributionOrderDO) {
        DistributionSellerBillDO distributionSellerBillDO = new DistributionSellerBillDO();
        distributionSellerBillDO.setSellerId(distributionOrderDO.getSellerId());
        distributionSellerBillDO.setCreateTime(DateUtil.getDateline());
        if(distributionOrderDO.getGrade1Rebate()==null){
            distributionOrderDO.setGrade1Rebate(0D);
        }
        if(distributionOrderDO.getGrade2Rebate()==null){
            distributionOrderDO.setGrade2Rebate(0D);
        }
        distributionSellerBillDO.setExpenditure(CurrencyUtil.add(distributionOrderDO.getGrade1Rebate(), distributionOrderDO.getGrade2Rebate()));
        distributionSellerBillDO.setReturnExpenditure(0D);
        distributionSellerBillDO.setOrderSn(distributionOrderDO.getOrderSn());
        daoSupport.insert(distributionSellerBillDO);
    }

    /**
     * 新增退款记录
     *
     * @param distributionOrderDO
     */
    @Override
    public void addRefund(DistributionOrderDO distributionOrderDO) {
        DistributionSellerBillDO distributionSellerBillDO = new DistributionSellerBillDO();
        distributionSellerBillDO.setSellerId(distributionOrderDO.getSellerId());
        distributionSellerBillDO.setCreateTime(DateUtil.getDateline());
        if(distributionOrderDO.getGrade1SellbackPrice()==null){
            distributionOrderDO.setGrade1SellbackPrice(0D);
        }
        if(distributionOrderDO.getGrade2SellbackPrice()==null){
            distributionOrderDO.setGrade2SellbackPrice(0D);
        }
        distributionSellerBillDO.setReturnExpenditure(CurrencyUtil.add(distributionOrderDO.getGrade1SellbackPrice(), distributionOrderDO.getGrade2SellbackPrice()));
        distributionSellerBillDO.setExpenditure(0D);
        distributionSellerBillDO.setOrderSn(distributionOrderDO.getOrderSn());
        daoSupport.insert(distributionSellerBillDO);
    }

    @Override
    public List<DistributionSellerBillDTO> countSeller(Integer startTime, Integer endTime) {
        List<DistributionSellerBillDTO> dtos = this.daoSupport.queryForList("select sum(expenditure) count_expenditure,sum(return_expenditure) return_expenditure,seller_id" +
                " from es_seller_bill where create_time > ? and create_time< ? group by seller_id",DistributionSellerBillDTO.class,startTime,endTime);

        return dtos;
    }

}
