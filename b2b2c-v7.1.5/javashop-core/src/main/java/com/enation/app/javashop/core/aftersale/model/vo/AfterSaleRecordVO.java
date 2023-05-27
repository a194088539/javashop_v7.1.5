package com.enation.app.javashop.core.aftersale.model.vo;

import com.enation.app.javashop.core.aftersale.model.dos.AfterSaleServiceDO;
import com.enation.app.javashop.core.aftersale.model.dto.ServiceOperateAllowable;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceStatusEnum;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceTypeEnum;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 申请售后服务记录VO
 * 用于各端售后服务列表查询
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-17
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AfterSaleRecordVO implements Serializable {

    private static final long serialVersionUID = 2406372635349255098L;

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
     * 申请售后服务会员ID
     */
    @ApiModelProperty(name = "member_id", value = "申请售后服务会员ID")
    private Integer memberId;
    /**
     * 申请售后服务会员名称
     */
    @ApiModelProperty(name = "member_name", value = "申请售后服务会员名称")
    private String memberName;
    /**
     * 商家ID
     */
    @ApiModelProperty(name = "seller_id", value = "商家ID")
    private Integer sellerId;
    /**
     * 商家名称
     */
    @ApiModelProperty(name = "seller_name", value = "商家名称")
    private String sellerName;
    /**
     * 申请时间
     */
    @ApiModelProperty(name="create_time",value="申请时间")
    private Long createTime;
    /**
     * 售后类型 RETURN_GOODS：退货，CHANGE_GOODS：换货，SUPPLY_AGAIN_GOODS：补发货品，ORDER_CANCEL：取消订单（订单确认付款且未收货之前）
     */
    @ApiModelProperty(name = "service_type", value = "售后类型 RETURN_GOODS：退货，CHANGE_GOODS：换货，SUPPLY_AGAIN_GOODS：补发货品，ORDER_CANCEL：取消订单（订单确认付款且未收货之前）")
    private String serviceType;
    /**
     * 售后单状态 APPLY：申请，PASS：审核通过，REFUSE：审核拒绝，WAIT_FOR_MANUAL：待人工处理，STOCK_IN：入库，REFUNDING：退款中，REFUNDFAIL：退款失败，COMPLETE：完成
     */
    @ApiModelProperty(name = "service_status", value = "售后单状态 APPLY：申请，PASS：审核通过，REFUSE：审核拒绝，WAIT_FOR_MANUAL：待人工处理，STOCK_IN：入库，REFUNDING：退款中，REFUNDFAIL：退款失败，COMPLETE：完成")
    private String serviceStatus;
    /**
     * 售后类型 退货，换货，补发货品，取消订单
     */
    @ApiModelProperty(name = "service_type_text", value = "售后类型 退货，换货，补发货品，取消订单")
    private String serviceTypeText;
    /**
     * 售后单状态
     */
    @ApiModelProperty(name = "service_status_text", value = "售后单状态")
    private String serviceStatusText;
    /**
     * 售后商品信息集合
     */
    @ApiModelProperty(name="goodsList", value="售后商品信息集合")
    private List<AfterSaleGoodsVO> goodsList;
    /**
     * 售后服务允许操作信息
     */
    @ApiModelProperty(name="allowable", value="售后服务允许操作信息")
    private ServiceOperateAllowable allowable;

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

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
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

    public String getServiceTypeText() {
        return serviceTypeText;
    }

    public void setServiceTypeText(String serviceTypeText) {
        this.serviceTypeText = serviceTypeText;
    }

    public String getServiceStatusText() {
        return serviceStatusText;
    }

    public void setServiceStatusText(String serviceStatusText) {
        this.serviceStatusText = serviceStatusText;
    }

    public List<AfterSaleGoodsVO> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<AfterSaleGoodsVO> goodsList) {
        this.goodsList = goodsList;
    }

    public ServiceOperateAllowable getAllowable() {
        return allowable;
    }

    public void setAllowable(ServiceOperateAllowable allowable) {
        this.allowable = allowable;
    }

    public AfterSaleRecordVO(AfterSaleServiceDO serviceDO) {
        this.serviceSn = serviceDO.getSn();
        this.orderSn = serviceDO.getOrderSn();
        this.memberId = serviceDO.getMemberId();
        this.memberName = serviceDO.getMemberName();
        this.sellerId = serviceDO.getSellerId();
        this.sellerName = serviceDO.getSellerName();
        this.createTime = serviceDO.getCreateTime();
        this.serviceType = serviceDO.getServiceType();
        this.serviceStatus = serviceDO.getServiceStatus();
        this.serviceTypeText = ServiceTypeEnum.valueOf(serviceDO.getServiceType()).description();
        this.serviceStatusText = ServiceStatusEnum.valueOf(serviceDO.getServiceStatus()).description();
        this.goodsList = JsonUtil.jsonToList(serviceDO.getGoodsJson(), AfterSaleGoodsVO.class);
    }

    @Override
    public String toString() {
        return "AfterSaleRecordVO{" +
                "serviceSn='" + serviceSn + '\'' +
                ", orderSn='" + orderSn + '\'' +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                ", createTime=" + createTime +
                ", serviceType='" + serviceType + '\'' +
                ", serviceStatus='" + serviceStatus + '\'' +
                ", serviceTypeText='" + serviceTypeText + '\'' +
                ", serviceStatusText='" + serviceStatusText + '\'' +
                ", goodsList=" + goodsList +
                ", allowable=" + allowable +
                '}';
    }
}
