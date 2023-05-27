package com.enation.app.javashop.core.goods.model.dos;

import com.enation.app.javashop.core.goods.model.vo.GoodsQuantityVO;
import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;


/**
 * 库存日志表实体
 *
 * @author admin
 * @version vv2.0
 * @since v7.0.0
 * 2018-05-15 16:17:32
 */
@Table(name = "es_quantity_log")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class QuantityLog implements Serializable {

    private static final long serialVersionUID = 6427256120262393L;

    public QuantityLog() {

    }

    public QuantityLog(GoodsQuantityVO goodsQuantity) {
        this.skuId = goodsQuantity.getSkuId();
        this.quantity = goodsQuantity.getQuantity();

        this.goodsId = goodsQuantity.getGoodsId();
    }

    /**
     * 日志id
     */
    @Id(name = "log_id")
    @ApiModelProperty(hidden = true)
    private Integer logId;
    /**
     * 订单编号
     */
    @Column(name = "order_sn")
    @NotEmpty(message = "订单编号不能为空")
    @ApiModelProperty(name = "order_sn", value = "订单编号", required = true)
    private String orderSn;
    /**
     * 商品id
     */
    @Column(name = "goods_id")
    @NotEmpty(message = "商品id不能为空")
    @ApiModelProperty(name = "goods_id", value = "商品id", required = true)
    private Integer goodsId;
    /**
     * sku id
     */
    @Column(name = "sku_id")
    @NotEmpty(message = "sku id不能为空")
    @ApiModelProperty(name = "sku_id", value = "sku id", required = true)
    private Integer skuId;
    /**
     * 库存数
     */
    @Column(name = "quantity")
    @NotEmpty(message = "库存数不能为空")
    @ApiModelProperty(name = "quantity", value = "库存数", required = true)
    private Integer quantity;
    /**
     * 可用库存
     */
    @Column(name = "enable_quantity")
    @Min(message = "必须为数字", value = 0)
    @ApiModelProperty(name = "enable_quantity", value = "可用库存", required = false)
    private Integer enableQuantity;
    /**
     * 操作时间
     */
    @Column(name = "op_time")
    @ApiModelProperty(name = "op_time", value = "操作时间", required = false)
    private Long opTime;

    @Column(name = "log_type")
    @ApiModelProperty(name = "log_type", value = "日志类型", required = false)
    private String logType;

    @Column(name = "reason")
    @ApiModelProperty(name = "reason", value = "原因", required = false)
    private String reason;


    @PrimaryKeyField
    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getEnableQuantity() {
        return enableQuantity;
    }

    public void setEnableQuantity(Integer enableQuantity) {
        this.enableQuantity = enableQuantity;
    }

    public Long getOpTime() {
        return opTime;
    }

    public void setOpTime(Long opTime) {
        this.opTime = opTime;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuantityLog that = (QuantityLog) o;

        return new EqualsBuilder()
                .append(orderSn, that.orderSn)
                .append(goodsId, that.goodsId)
                .append(skuId, that.skuId)
                .append(quantity, that.quantity)
                .append(enableQuantity, that.enableQuantity)
                .append(logType, that.logType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(orderSn)
                .append(goodsId)
                .append(skuId)
                .append(quantity)
                .append(enableQuantity)
                .append(logType)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "QuantityLog{" +
                "logId=" + logId +
                ", orderSn='" + orderSn + '\'' +
                ", goodsId=" + goodsId +
                ", skuId=" + skuId +
                ", quantity=" + quantity +
                ", enableQuantity=" + enableQuantity +
                ", opTime=" + opTime +
                ", logType='" + logType + '\'' +
                '}';
    }
}
