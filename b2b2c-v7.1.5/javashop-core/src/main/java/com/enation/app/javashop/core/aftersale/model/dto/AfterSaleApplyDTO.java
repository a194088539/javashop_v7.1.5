package com.enation.app.javashop.core.aftersale.model.dto;

import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderItemsDO;
import com.enation.app.javashop.framework.security.model.Buyer;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 申请售后所需要的相关数据DTO
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-12-03
 */
public class AfterSaleApplyDTO implements Serializable {

    private static final long serialVersionUID = 2912409102735600572L;

    @ApiModelProperty(value = "当前登录的会员信息")
    private Buyer buyer;

    @ApiModelProperty(value = "申请售后的订单信息")
    private OrderDO orderDO;

    @ApiModelProperty(value = "申请售后的订单商品信息")
    private OrderItemsDO itemsDO;

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public OrderDO getOrderDO() {
        return orderDO;
    }

    public void setOrderDO(OrderDO orderDO) {
        this.orderDO = orderDO;
    }

    public OrderItemsDO getItemsDO() {
        return itemsDO;
    }

    public void setItemsDO(OrderItemsDO itemsDO) {
        this.itemsDO = itemsDO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AfterSaleApplyDTO that = (AfterSaleApplyDTO) o;
        return Objects.equals(buyer, that.buyer) &&
                Objects.equals(orderDO, that.orderDO) &&
                Objects.equals(itemsDO, that.itemsDO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buyer, orderDO, itemsDO);
    }

    @Override
    public String toString() {
        return "AfterSaleApplyDTO{" +
                "buyer=" + buyer +
                ", orderDO=" + orderDO +
                ", itemsDO=" + itemsDO +
                '}';
    }
}
