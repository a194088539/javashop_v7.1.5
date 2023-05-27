package com.enation.app.javashop.core.aftersale.model.vo;

import com.enation.app.javashop.core.aftersale.model.dos.*;
import com.enation.app.javashop.core.aftersale.model.enums.RefundStatusEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 售后退款单详情VO
 * 存放的是一个售后退款单所有相关的信息，主要用于查看售后退款单详情
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-28
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RefundDetailVO extends RefundDO {

    @ApiModelProperty(name = "refund_status_text", value = "退款单状态 新申请，退款中，退款失败，完成")
    private String refundStatusText;

    @ApiModelProperty(name = "goods_list", value = "申请售后商品信息集合")
    private List<AfterSaleGoodsDO> goodsList;

    @ApiModelProperty(name = "logs",value = "售后退款日志信息集合")
    private List<AfterSaleLogDO> logs;

    public String getRefundStatusText() {
        if (this.getRefundStatus() != null) {
            RefundStatusEnum refundStatusEnum = RefundStatusEnum.valueOf(this.getRefundStatus());
            refundStatusText = refundStatusEnum.description();
        }
        return refundStatusText;
    }

    public void setRefundStatusText(String refundStatusText) {
        this.refundStatusText = refundStatusText;
    }

    public List<AfterSaleGoodsDO> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<AfterSaleGoodsDO> goodsList) {
        this.goodsList = goodsList;
    }

    public List<AfterSaleLogDO> getLogs() {
        return logs;
    }

    public void setLogs(List<AfterSaleLogDO> logs) {
        this.logs = logs;
    }

    @Override
    public String toString() {
        return "RefundDetailVO{" +
                "goodsList=" + goodsList +
                ", logs=" + logs +
                '}';
    }
}
