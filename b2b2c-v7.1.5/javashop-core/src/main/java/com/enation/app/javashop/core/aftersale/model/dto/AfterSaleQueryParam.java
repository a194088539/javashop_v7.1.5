package com.enation.app.javashop.core.aftersale.model.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 售后服务查询参数VO
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-17
 */
public class AfterSaleQueryParam {

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

    @ApiModelProperty(value = "售后类型 RETURN_GOODS：退货，CHANGE_GOODS：换货，SUPPLY_AGAIN_GOODS：补发货品，ORDER_CANCEL：取消订单", name = "service_type", allowableValues = "RETURN_GOODS,CHANGE_GOODS,SUPPLY_AGAIN_GOODS,ORDER_CANCEL")
    private String serviceType;

    @ApiModelProperty(value = "售后单状态 APPLY：申请，PASS：审核通过，REFUSE：审核拒绝，WAIT_FOR_MANUAL：待人工处理，STOCK_IN：入库，REFUNDING：退款中，REFUNDFAIL：退款失败，COMPLETE：完成", name = "service_status", allowableValues = "APPLY,PASS,REFUSE,WAIT_FOR_MANUAL,STOCK_IN,REFUNDING,REFUNDFAIL,COMPLETE")
    private String serviceStatus;

    @ApiModelProperty(value = "申请时间-起始时间", name = "start_time")
    private Long startTime;

    @ApiModelProperty(value = "申请时间-结束时间", name = "end_time")
    private Long endTime;

    @ApiModelProperty(value = "售后服务单创建渠道 NORMAL：正常渠道创建，PINTUAN：拼团失败自动创建", name = "create_channel", allowableValues = "NORMAL,PINTUAN", hidden = true)
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

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
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
        return "AfterSaleQueryParam{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", memberId=" + memberId +
                ", sellerId=" + sellerId +
                ", keyword='" + keyword + '\'' +
                ", serviceSn='" + serviceSn + '\'' +
                ", orderSn='" + orderSn + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", serviceStatus='" + serviceStatus + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", createChannel='" + createChannel + '\'' +
                '}';
    }
}
