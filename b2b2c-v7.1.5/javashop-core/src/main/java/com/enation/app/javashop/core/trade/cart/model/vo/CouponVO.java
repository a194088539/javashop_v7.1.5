package com.enation.app.javashop.core.trade.cart.model.vo;

import com.enation.app.javashop.core.member.model.dos.MemberCoupon;
import com.enation.app.javashop.framework.database.annotation.Column;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * 优惠券
 *
 * @author kingapex
 * @version v2.0
 * @since v7.0.0
 * 2017年3月22日下午1:03:52
 */
@ApiModel(description = "优惠券")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CouponVO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5236361119763282449L;

    /**
     * 记录下单时使用的会员优惠券ID
     */
    @ApiModelProperty(value = "会员优惠券id")
    private Integer memberCouponId;

    /**
     * 记录下单时赠送的优惠券ID
     */
    @ApiModelProperty(value = "优惠券id")
    private Integer couponId;

    @ApiModelProperty(value = "卖家id")
    private Integer sellerId;

    @ApiModelProperty(value = "卖家名称")
    private String sellerName;

    @ApiModelProperty(value = "金额")
    private Double amount;

    @ApiModelProperty(value = "有效期")
    private Long endTime;

    @ApiModelProperty(value = "使用条件")
    private String useTerm;


    @ApiModelProperty(value = "优惠券门槛金额")
    private Double couponThresholdPrice;

    @ApiModelProperty(value = "是否可用，1为可用，0为不可用")
    private int enable;

    @ApiModelProperty(value = "是否被选中，1为选中，0为不选中")
    private int selected;

    @ApiModelProperty(name = "error_msg", value = "错误信息，结算页使用")
    private String errorMsg;

    @ApiModelProperty(name = "use_scope", value = "优惠券使用范围")
    private String useScope;

    @ApiModelProperty(name = "enable_sku_list", value = "允许使用的ids")
    private List<Integer> enableSkuList;


    public CouponVO() {
    }

    public CouponVO(MemberCoupon memberCoupon) {

        this.setCouponId(memberCoupon.getCouponId());
        this.setAmount(memberCoupon.getCouponPrice());
        this.setUseTerm("满" + memberCoupon.getCouponThresholdPrice() + "可用");
        this.setSellerId(memberCoupon.getSellerId());
        this.setMemberCouponId(memberCoupon.getMcId());
        this.setEndTime(memberCoupon.getEndTime());
        this.setCouponThresholdPrice(memberCoupon.getCouponThresholdPrice());
        this.setUseScope(memberCoupon.getUseScope());
        this.setSellerName(memberCoupon.getSellerName());

    }

    public Integer getMemberCouponId() {
        return memberCouponId;
    }

    public void setMemberCouponId(Integer memberCouponId) {
        this.memberCouponId = memberCouponId;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getUseTerm() {
        return useTerm;
    }

    public void setUseTerm(String useTerm) {
        this.useTerm = useTerm;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }


    public Double getCouponThresholdPrice() {
        return couponThresholdPrice;
    }

    public void setCouponThresholdPrice(Double couponThresholdPrice) {
        this.couponThresholdPrice = couponThresholdPrice;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getUseScope() {
        return useScope;
    }

    public void setUseScope(String useScope) {
        this.useScope = useScope;
    }

    public List<Integer> getEnableSkuList() {
        return enableSkuList;
    }

    public void setEnableSkuList(List<Integer> enableSkuList) {
        this.enableSkuList = enableSkuList;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    @Override
    public String toString() {
        return "CouponVO{" +
                "memberCouponId=" + memberCouponId +
                ", couponId=" + couponId +
                ", sellerId=" + sellerId +
                ", amount=" + amount +
                ", endTime=" + endTime +
                ", useTerm='" + useTerm + '\'' +
                ", couponThresholdPrice=" + couponThresholdPrice +
                ", enable=" + enable +
                ", selected=" + selected +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CouponVO couponVO = (CouponVO) o;

        return new EqualsBuilder()
                .append(enable, couponVO.enable)
                .append(selected, couponVO.selected)
                .append(memberCouponId, couponVO.memberCouponId)
                .append(couponId, couponVO.couponId)
                .append(sellerId, couponVO.sellerId)
                .append(amount, couponVO.amount)
                .append(endTime, couponVO.endTime)
                .append(useTerm, couponVO.useTerm)
                .append(couponThresholdPrice, couponVO.couponThresholdPrice)
                .append(errorMsg, couponVO.errorMsg)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(memberCouponId)
                .append(couponId)
                .append(sellerId)
                .append(amount)
                .append(endTime)
                .append(useTerm)
                .append(couponThresholdPrice)
                .append(enable)
                .append(selected)
                .append(errorMsg)
                .toHashCode();
    }

}
