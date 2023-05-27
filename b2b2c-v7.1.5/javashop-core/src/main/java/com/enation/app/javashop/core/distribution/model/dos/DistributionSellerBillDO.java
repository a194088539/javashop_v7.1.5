package com.enation.app.javashop.core.distribution.model.dos;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分销商家结算单
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-09-04 上午10:03
 */
@Table(name="es_seller_bill")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DistributionSellerBillDO {


    @Column(name = "id")
    @ApiModelProperty(value = "结算单id", name = "id")
    private Integer id;


    /** 周期开始时间*/
    @Column(name = "create_time")
    @ApiModelProperty(value="开始时间")
    private Long createTime;

    @Column(name = "seller_id")
    @ApiModelProperty(value = "商家id", name = "seller_id")
    private Integer sellerId;

    @ApiModelProperty(value = "order_sn", name = "order_sn")
    @Column(name = "order_sn")
    private String orderSn;

    @Column(name = "expenditure")
    @ApiModelProperty(value = "商品反现支出", name = "expenditure")
    private Double expenditure;

    @Column(name = "return_expenditure")
    @ApiModelProperty(value = "商品反现退换", name = "return_expenditure")
    private Double returnExpenditure;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public Double getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(Double expenditure) {
        this.expenditure = expenditure;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Double getReturnExpenditure() {
        return returnExpenditure;
    }

    public void setReturnExpenditure(Double returnExpenditure) {
        this.returnExpenditure = returnExpenditure;
    }

    @Override
    public String toString() {
        return "DistributionSellerBillDO{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", sellerId=" + sellerId +
                ", orderSn='" + orderSn + '\'' +
                ", expenditure=" + expenditure +
                ", returnExpenditure=" + returnExpenditure +
                '}';
    }
}
