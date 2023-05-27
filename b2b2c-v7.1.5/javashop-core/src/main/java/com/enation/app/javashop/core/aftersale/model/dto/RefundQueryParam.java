package com.enation.app.javashop.core.aftersale.model.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 售后退款单查询参数VO
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-25
 */
public class RefundQueryParam {

    @ApiModelProperty(value = "页码",name = "page_no", required = true)
    private Integer pageNo;

    @ApiModelProperty(value = "分页大小" ,name = "page_size", required = true)
    private Integer pageSize;

    @ApiModelProperty(value = "会员ID" ,name = "member_id", hidden = true)
    private Integer memberId;

    @ApiModelProperty(value = "商家ID" ,name = "seller_id", hidden = true)
    private Integer sellerId;

    @ApiModelProperty(value = "模糊查询的关键字" ,name = "keyword")
    private String keyword;

    @ApiModelProperty(value = "售后服务单号" ,name = "service_sn")
    private String serviceSn;

    @ApiModelProperty(value = "订单编号" ,name = "order_sn")
    private String orderSn;

    @ApiModelProperty(value = "商品名称" ,name = "goods_name")
    private String goodsName;

    @ApiModelProperty(value = "退款单状态 APPLY：申请，REFUNDING：退款中，REFUNDFAIL：退款失败，COMPLETE：完成", name = "refund_status", allowableValues = "APPLY,REFUNDING,REFUNDFAIL,COMPLETE")
    private String refundStatus;

    @ApiModelProperty(value = "退款方式 ORIGINAL：原路退回，OFFLINE：线下支付", name = "refund_way", allowableValues = "ORIGINAL,OFFLINE")
    private String refundWay;

    @ApiModelProperty(value = "申请时间-起始时间", name = "start_time")
    private Long startTime;

    @ApiModelProperty(value = "申请时间-结束时间", name = "end_time")
    private Long endTime;

    @ApiModelProperty(value = "退款单创建渠道 NORMAL：正常渠道创建，PINTUAN：拼团失败自动创建", name = "create_channel", allowableValues = "NORMAL,PINTUAN", hidden = true)
    private String createChannel;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public String getRefundWay() {
        return refundWay;
    }

    public void setRefundWay(String refundWay) {
        this.refundWay = refundWay;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getCreateChannel() {
        return createChannel;
    }

    public void setCreateChannel(String createChannel) {
        this.createChannel = createChannel;
    }

    @Override
    public String toString() {
        return "RefundQueryParam{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", memberId=" + memberId +
                ", sellerId=" + sellerId +
                ", keyword='" + keyword + '\'' +
                ", serviceSn='" + serviceSn + '\'' +
                ", orderSn='" + orderSn + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", refundStatus='" + refundStatus + '\'' +
                ", refundWay='" + refundWay + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", createChannel='" + createChannel + '\'' +
                '}';
    }
}
