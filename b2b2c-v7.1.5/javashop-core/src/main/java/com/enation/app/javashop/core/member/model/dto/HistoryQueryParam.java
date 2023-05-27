package com.enation.app.javashop.core.member.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 会员开票历史记录搜索参数实体
 *
 * @author duanmingyu
 * @version v7.1.4
 * @since v7.0.0
 * 2019-06-20
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HistoryQueryParam {

    /**
     * 搜索关键字
     */
    @ApiModelProperty(name = "keyword", value = "搜索关键字")
    private String keyword;

    /**
     * 订单编号
     */
    @ApiModelProperty(name = "order_sn", value = "订单编号")
    private String orderSn;

    /**
     * 商家名称
     */
    @ApiModelProperty(name = "seller_name", value = "商家名称")
    private String sellerName;

    /**
     * 开票状态 0：未开，1：已开
     */
    @ApiModelProperty(name = "status", value = "开票状态", example = "0：未开，1：已开", allowableValues = "0,1")
    private String status;

    /**
     * 发票类型 ELECTRO：电子普通发票，VATORDINARY：增值税普通发票，VATOSPECIAL：增值税专用发票
     */
    @ApiModelProperty(name = "receipt_type", value = "发票类型", example = "ELECTRO：电子普通发票，VATORDINARY：增值税普通发票，VATOSPECIAL：增值税专用发票", allowableValues = "ELECTRO,VATORDINARY,VATOSPECIAL")
    private String receiptType;

    /**
     * 申请开票开始时间
     */
    @ApiModelProperty(name = "start_time", value = "申请开票开始时间")
    private String startTime;
    /**
     * 申请开票结束时间
     */
    @ApiModelProperty(name = "end_time", value = "申请开票结束时间")
    private String endTime;

    /**
     * 商家ID
     */
    @ApiModelProperty(name = "seller_id", value = "商家ID")
    private Integer sellerId;

    /**
     * 会员ID
     */
    @ApiModelProperty(name = "member_id", value = "会员ID")
    private Integer memberId;

    /**
     * 会员用户名
     */
    @ApiModelProperty(name = "uname", value = "会员用户名")
    private String uname;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    @Override
    public String toString() {
        return "HistoryQueryParam{" +
                "keyword='" + keyword + '\'' +
                ", orderSn='" + orderSn + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", status='" + status + '\'' +
                ", receiptType='" + receiptType + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", sellerId=" + sellerId +
                ", memberId=" + memberId +
                ", uname='" + uname + '\'' +
                '}';
    }
}
