package com.enation.app.javashop.core.orderbill.model.vo;

import com.enation.app.javashop.core.orderbill.model.dos.Bill;
import com.enation.app.javashop.core.orderbill.model.dos.BillItem;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author fk
 * @version v1.0
 * @Description: 结算单导出对象
 * @date 2018/7/26 15:35
 * @since v7.0.0
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BillExcel {

    @ApiModelProperty(name = "bill", value = "结算单信息")
    private Bill bill;

    @ApiModelProperty(name = "order_list", value = "订单列表信息")
    private List<BillItem> orderList;

    @ApiModelProperty(name = "refund_list", value = "退单列表信息")
    private List<BillItem> refundList;


    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public List<BillItem> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<BillItem> orderList) {
        this.orderList = orderList;
    }

    public List<BillItem> getRefundList() {
        return refundList;
    }

    public void setRefundList(List<BillItem> refundList) {
        this.refundList = refundList;
    }
}
