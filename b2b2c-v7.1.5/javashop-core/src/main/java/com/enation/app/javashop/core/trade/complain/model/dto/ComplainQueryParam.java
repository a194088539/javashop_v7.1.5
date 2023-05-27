package com.enation.app.javashop.core.trade.complain.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 交易投诉查询参数
 *
 * @author fk
 * @version v2.0
 * @since v2.0
 * 2019-11-27 16:48:27
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ComplainQueryParam {

    @ApiModelProperty(name = "page_no",value = "第几页")
    private Integer pageNo;

    @ApiModelProperty(name = "page_size",value = "每页条数")
    private Integer pageSize;

    @ApiModelProperty(name = "member_id",value = "会员id")
    private Integer memberId;

    @ApiModelProperty(name = "seller_id",value = "商家id")
    private Integer sellerId;

    @ApiModelProperty(name = "order_sn",value = "订单编号")
    private String orderSn;

    @ApiModelProperty(name = "keywords",value = "关键词")
    private String keywords;

    @ApiModelProperty(name = "tag",value = "页面标签",allowableValues = "ALL,COMPLAINING,CANCELLED,COMPLETE",
            example = "ALL:所有,COMPLAINING:进行中,CANCELLED:已取消,COMPLETE:已完成")
    private String tag;


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

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
