package com.enation.app.javashop.core.aftersale.model.vo;

import com.enation.app.javashop.core.aftersale.model.dos.RefundDO;
import com.enation.app.javashop.core.aftersale.model.enums.RefundStatusEnum;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 售后退款单VO
 * 用于各端售后退款单列表查询
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-28
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RefundRecordVO implements Serializable {

    private static final long serialVersionUID = 6524326855011692559L;

    /**
     * 售后服务单号
     */
    @ApiModelProperty(name = "service_sn", value = "售后服务单号")
    private String serviceSn;
    /**
     * 订单编号
     */
    @ApiModelProperty(name = "order_sn", value = "订单编号")
    private String orderSn;
    /**
     * 申请退款金额
     */
    @ApiModelProperty(name="refund_price",value="申请退款金额")
    private Double refundPrice;
    /**
     * 商家同意退款金额
     */
    @ApiModelProperty(name="agree_price",value="同意退款金额")
    private Double agreePrice;
    /**
     * 实际退款金额
     */
    @ApiModelProperty(name="actual_price",value="实际退款金额")
    private Double actualPrice;
    /**
     * 申请时间
     */
    @ApiModelProperty(name="create_time",value="申请时间")
    private Long createTime;
    /**
     * 退款方式 ORIGINAL：原路退回，OFFLINE：线下支付
     */
    @ApiModelProperty(name="refund_way", value="退款方式 ORIGINAL：原路退回，OFFLINE：线下支付")
    private String refundWay;
    /**
     * 退款单状态 APPLY：新申请，REFUNDING：退款中，REFUNDFAIL：退款失败，COMPLETE：完成
     */
    @ApiModelProperty(name="refund_status",value="退款单状态 APPLY：新申请，REFUNDING：退款中，REFUNDFAIL：退款失败，COMPLETE：完成")
    private String refundStatus;
    /**
     * 退款单状态 新申请，退款中，退款失败，完成
     */
    @ApiModelProperty(name="refund_status_text",value="退款单状态 新申请，退款中，退款失败，完成")
    private String refundStatusText;
    /**
     * 售后商品信息集合
     */
    @ApiModelProperty(name="goodsList",value="售后商品信息集合")
    private List<AfterSaleGoodsVO> goodsList;

    public String getServiceSn() {
        return serviceSn;
    }

    public void setServiceSn(String serviceSn) {
        this.serviceSn = serviceSn;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Double getRefundPrice() {
        return refundPrice;
    }

    public void setRefundPrice(Double refundPrice) {
        this.refundPrice = refundPrice;
    }

    public Double getAgreePrice() {
        return agreePrice;
    }

    public void setAgreePrice(Double agreePrice) {
        this.agreePrice = agreePrice;
    }

    public Double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getRefundWay() {
        return refundWay;
    }

    public void setRefundWay(String refundWay) {
        this.refundWay = refundWay;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getRefundStatusText() {
        return refundStatusText;
    }

    public void setRefundStatusText(String refundStatusText) {
        this.refundStatusText = refundStatusText;
    }

    public List<AfterSaleGoodsVO> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<AfterSaleGoodsVO> goodsList) {
        this.goodsList = goodsList;
    }

    public RefundRecordVO(RefundDO refundDO) {
        this.serviceSn = refundDO.getSn();
        this.orderSn = refundDO.getOrderSn();
        this.refundPrice = refundDO.getRefundPrice();
        this.agreePrice = refundDO.getAgreePrice() == null ? 0.00 : refundDO.getAgreePrice();
        this.actualPrice = refundDO.getActualPrice() == null ? 0.00 : refundDO.getActualPrice();
        this.createTime = refundDO.getCreateTime();
        this.refundWay = refundDO.getRefundWay();
        this.refundStatus = refundDO.getRefundStatus();
        this.refundStatusText = RefundStatusEnum.valueOf(refundDO.getRefundStatus()).description();
        this.goodsList = JsonUtil.jsonToList(refundDO.getGoodsJson(), AfterSaleGoodsVO.class);
    }

    @Override
    public String toString() {
        return "RefundRecordVO{" +
                "serviceSn='" + serviceSn + '\'' +
                ", orderSn='" + orderSn + '\'' +
                ", refundPrice=" + refundPrice +
                ", agreePrice=" + agreePrice +
                ", actualPrice=" + actualPrice +
                ", createTime=" + createTime +
                ", refundWay='" + refundWay + '\'' +
                ", refundStatus='" + refundStatus + '\'' +
                ", refundStatusText='" + refundStatusText + '\'' +
                ", goodsList=" + goodsList +
                '}';
    }
}
