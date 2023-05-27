package com.enation.app.javashop.core.aftersale.model.dos;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 售后基础信息实体类
 * 此类为售后服务单和退款单的共用数据类
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-15
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AfterSaleBase implements Serializable {

    private static final long serialVersionUID = -272499573960327341L;

    /**
     * 主键ID
     */
    @Id(name = "id")
    @ApiModelProperty(hidden=true)
    private Integer id;
    /**
     * 售后服务单号
     */
    @Column(name = "sn")
    @ApiModelProperty(name = "sn", value = "售后服务单号")
    private String sn;
    /**
     * 订单编号
     */
    @Column(name = "order_sn")
    @ApiModelProperty(name = "order_sn", value = "订单编号")
    private String orderSn;
    /**
     * 会员ID
     */
    @Column(name = "member_id")
    @ApiModelProperty(name = "member_id", value = "会员ID")
    private Integer memberId;
    /**
     * 会员名称
     */
    @Column(name = "member_name")
    @ApiModelProperty(name = "member_name", value = "会员名称")
    private String memberName;
    /**
     * 商家ID
     */
    @Column(name = "seller_id")
    @ApiModelProperty(name="seller_id",value="商家ID")
    private Integer sellerId;
    /**
     * 商家名称
     */
    @Column(name = "seller_name")
    @ApiModelProperty(name="seller_name",value="商家名称")
    private String sellerName;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="create_time",value="创建时间")
    private Long createTime;
    /**
     * 手机号
     */
    @Column(name = "mobile")
    @ApiModelProperty(name="mobile",value="手机号")
    private String mobile;
    /**
     * 售后商品信息json
     */
    @Column(name = "goods_json")
    @ApiModelProperty(name="goods_json",value="售后商品信息json")
    private String goodsJson;
    /**
     * 删除状态
     */
    @Column(name = "disabled")
    @ApiModelProperty(name="disabled",value="删除状态 DELETED：已删除 NORMAL：正常")
    private String disabled;
    /**
     * 创建渠道
     */
    @Column(name = "create_channel")
    @ApiModelProperty(name="create_channel",value="创建渠道 NORMAL：正常渠道创建，PINTUAN：拼团失败自动创建")
    private String createChannel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGoodsJson() {
        return goodsJson;
    }

    public void setGoodsJson(String goodsJson) {
        this.goodsJson = goodsJson;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getCreateChannel() {
        return createChannel;
    }

    public void setCreateChannel(String createChannel) {
        this.createChannel = createChannel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AfterSaleBase that = (AfterSaleBase) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(sn, that.sn) &&
                Objects.equals(orderSn, that.orderSn) &&
                Objects.equals(memberId, that.memberId) &&
                Objects.equals(memberName, that.memberName) &&
                Objects.equals(sellerId, that.sellerId) &&
                Objects.equals(sellerName, that.sellerName) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(mobile, that.mobile) &&
                Objects.equals(goodsJson, that.goodsJson) &&
                Objects.equals(disabled, that.disabled) &&
                Objects.equals(createChannel, that.createChannel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sn, orderSn, memberId, memberName, sellerId, sellerName, createTime, mobile, goodsJson, disabled, createChannel);
    }

    @Override
    public String toString() {
        return "AfterSaleBase{" +
                "id=" + id +
                ", sn='" + sn + '\'' +
                ", orderSn='" + orderSn + '\'' +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                ", createTime=" + createTime +
                ", mobile='" + mobile + '\'' +
                ", goodsJson='" + goodsJson + '\'' +
                ", disabled='" + disabled + '\'' +
                ", createChannel='" + createChannel + '\'' +
                '}';
    }
}
