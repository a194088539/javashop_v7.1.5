package com.enation.app.javashop.core.trade.order.model.vo;

import com.enation.app.javashop.core.trade.complain.model.enums.ComplainSkuStatusEnum;
import com.enation.app.javashop.core.trade.order.model.enums.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * 商品可进行的操作
 *
 * @author Snow create in 2018/5/15
 * @version v2.0
 * @since v7.0.0
 */
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GoodsOperateAllowable implements Serializable {

    @ApiModelProperty(value = "是否允许申请售后")
    private Boolean allowApplyService;

    @ApiModelProperty(value = "是否显示交易投诉相关")
    private Boolean allowOrderComplain;


    public Boolean getAllowApplyService() {
        return allowApplyService;
    }

    public void setAllowApplyService(Boolean allowApplyService) {
        this.allowApplyService = allowApplyService;
    }

    public Boolean getAllowOrderComplain() {
        return allowOrderComplain;
    }

    public void setAllowOrderComplain(Boolean allowOrderComplain) {
        this.allowOrderComplain = allowOrderComplain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GoodsOperateAllowable that = (GoodsOperateAllowable) o;

        return new EqualsBuilder()
                .append(allowApplyService, that.allowApplyService)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(allowApplyService)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "GoodsOperateAllowable{" +
                "allowApplyService=" + allowApplyService +
                '}';
    }

    /**
     * 空构造器
     */
    public GoodsOperateAllowable() {

    }

    /**
     * 根据各种状态构建对象
     *
     * @param shipStatus 订单发货状态
     * @param serviceStatus 订单商品售后状态
     * @param payStatus 订单付款状态
     */
    public GoodsOperateAllowable(ShipStatusEnum shipStatus, OrderServiceStatusEnum serviceStatus, PayStatusEnum payStatus, ComplainSkuStatusEnum complainSkuStatusEnum) {


        //订单中的商品是否允许被申请售后 = 订单付款状态为已付款状态 && 订单发货状态为已发货状态 && 订单商品售后状态为未申请售后（注：无论在线支付还是货到付款的订单，都符合下列标准）
        allowApplyService = PayStatusEnum.PAY_YES.value().equals(payStatus.value()) && ShipStatusEnum.SHIP_ROG.value().equals(shipStatus.value())
                && OrderServiceStatusEnum.NOT_APPLY.value().equals(serviceStatus.value());

        //是否允许显示交易投诉相关
        allowOrderComplain = PayStatusEnum.PAY_YES.value().equals(payStatus.value()) && !ComplainSkuStatusEnum.EXPIRED.equals(complainSkuStatusEnum);

    }


}