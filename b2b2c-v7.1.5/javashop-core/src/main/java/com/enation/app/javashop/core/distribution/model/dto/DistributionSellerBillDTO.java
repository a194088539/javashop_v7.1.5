package com.enation.app.javashop.core.distribution.model.dto;

import java.io.Serializable;

/**
 * DistributionSellerBillDTO
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-09-07 上午7:58
 */
public class DistributionSellerBillDTO implements Serializable{

    /**
     * 商家id
     */
    private Integer sellerId;
    /**
     * 支出总计
     */
    private Double countExpenditure;
    /**
     * 支出退换
     */
    private Double returnExpenditure;

    @Override
    public String toString() {
        return "DistributionSellerBillDTO{" +
                "sellerId=" + sellerId +
                ", countExpenditure=" + countExpenditure +
                ", returnExpenditure=" + returnExpenditure +
                '}';
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public Double getCountExpenditure() {
        return countExpenditure;
    }

    public void setCountExpenditure(Double countExpenditure) {
        this.countExpenditure = countExpenditure;
    }

    public Double getReturnExpenditure() {
        return returnExpenditure;
    }

    public void setReturnExpenditure(Double returnExpenditure) {
        this.returnExpenditure = returnExpenditure;
    }
}
