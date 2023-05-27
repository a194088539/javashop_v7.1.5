package com.enation.app.javashop.core.shop.model.dto;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 商家发票设置DTO
 * @author duanmingyu
 * @version 7.1.4
 * @since 7.1.0
 * 2019-06-20
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShopReceiptDTO {

    /**是否允许开具增值税普通发票 0：否，1：是*/
    @Column(name = "ordin_receipt_status")
    @ApiModelProperty(name="ordin_receipt_status",value="是否允许开具增值税普通发票 0：否，1：是",required=false, allowableValues = "0,1")
    private Integer ordinReceiptStatus;

    /**是否允许开具电子普通发票 0：否，1：是*/
    @Column(name = "elec_receipt_status")
    @ApiModelProperty(name="elec_receipt_status",value="是否允许开具电子普通发票 0：否，1：是",required=false, allowableValues = "0,1")
    private Integer elecReceiptStatus;

    /**是否允许开具增值税专用发票 0：否，1：是*/
    @Column(name = "tax_receipt_status")
    @ApiModelProperty(name="tax_receipt_status",value="是否允许开具增值税专用发票 0：否，1：是",required=false, allowableValues = "0,1")
    private Integer taxReceiptStatus;

    public Integer getOrdinReceiptStatus() {
        return ordinReceiptStatus;
    }

    public void setOrdinReceiptStatus(Integer ordinReceiptStatus) {
        this.ordinReceiptStatus = ordinReceiptStatus;
    }

    public Integer getElecReceiptStatus() {
        return elecReceiptStatus;
    }

    public void setElecReceiptStatus(Integer elecReceiptStatus) {
        this.elecReceiptStatus = elecReceiptStatus;
    }

    public Integer getTaxReceiptStatus() {
        return taxReceiptStatus;
    }

    public void setTaxReceiptStatus(Integer taxReceiptStatus) {
        this.taxReceiptStatus = taxReceiptStatus;
    }

    @Override
    public String toString() {
        return "ShopReceiptDTO{" +
                "ordinReceiptStatus=" + ordinReceiptStatus +
                ", elecReceiptStatus=" + elecReceiptStatus +
                ", taxReceiptStatus=" + taxReceiptStatus +
                '}';
    }
}
