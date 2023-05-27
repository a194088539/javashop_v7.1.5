package com.enation.app.javashop.core.member.model.vo;

import com.enation.app.javashop.core.member.model.dos.MemberCoupon;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * 结算页——我的优惠券VO
 *
 * @author Snow create in 2018/7/21
 * @version v2.0
 * @since v7.0.0
 */
public class MemberCouponVO implements Serializable {


    private static final long serialVersionUID = -8186653621359439607L;

    @ApiModelProperty(value = "店铺ID")
    private Integer sellerId;

    @ApiModelProperty(value = "店铺名称")
    private Integer sellerName;

    @ApiModelProperty(value = "优惠券集合")
    private List<MemberCoupon> memberCouponList;


    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public List<MemberCoupon> getMemberCouponList() {
        return memberCouponList;
    }

    public void setMemberCouponList(List<MemberCoupon> memberCouponList) {
        this.memberCouponList = memberCouponList;
    }

    public Integer getSellerName() {
        return sellerName;
    }

    public void setSellerName(Integer sellerName) {
        this.sellerName = sellerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (o == null || getClass() != o.getClass()){
            return false;
        }

        MemberCouponVO couponVO = (MemberCouponVO) o;

        return new EqualsBuilder()
                .append(sellerId, couponVO.sellerId)
                .append(sellerName, couponVO.sellerName)
                .append(memberCouponList, couponVO.memberCouponList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(sellerId)
                .append(sellerName)
                .append(memberCouponList)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "MemberCouponVO{" +
                "sellerId=" + sellerId +
                ", sellerName=" + sellerName +
                ", memberCouponList=" + memberCouponList +
                '}';
    }
}
