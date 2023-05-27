package com.enation.app.javashop.core.aftersale.model.vo;

import com.enation.app.javashop.core.aftersale.model.dos.*;
import com.enation.app.javashop.core.aftersale.model.dto.ServiceOperateAllowable;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceStatusEnum;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceTypeEnum;
import com.enation.app.javashop.core.system.model.dos.LogisticsCompanyDO;
import com.enation.app.javashop.framework.util.BeanUtil;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 售后申请VO
 * 存放的是一个售后服务所有相关的信息，主要用于查看售后服务详情
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-15
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ApplyAfterSaleVO extends AfterSaleServiceDO implements Serializable {

    private static final long serialVersionUID = -96586965589627807L;

    @ApiModelProperty(name = "service_type_text", value = "售后类型 退货，换货，补发货品，取消订单")
    private String serviceTypeText;

    @ApiModelProperty(name = "service_status_text", value = "售后单状态 申请，审核通过，审核拒绝，待人工处理，入库，退款中，退款失败，完成")
    private String serviceStatusText;

    @ApiModelProperty(name = "sku_id", value = "申请售后商品skuID")
    private Integer skuId;

    @ApiModelProperty(name = "return_num", value = "申请售后商品数量")
    private Integer returnNum;

    @ApiModelProperty(name = "refund_info", value = "售后退款/退货信息")
    private AfterSaleRefundDO refundInfo;

    @ApiModelProperty(name = "change_info", value = "售后换货/补发商品信息")
    private AfterSaleChangeDO changeInfo;

    @ApiModelProperty(name = "express_info", value = "售后物流信息")
    private AfterSaleExpressDO expressInfo;

    @ApiModelProperty(name = "goods_list", value = "申请售后商品信息集合")
    private List<AfterSaleGoodsDO> goodsList;

    @ApiModelProperty(name = "images",value = "售后图片集合")
    private List<AfterSaleGalleryDO> images;

    @ApiModelProperty(name = "logs",value = "售后日志信息集合")
    private List<AfterSaleLogDO> logs;

    @ApiModelProperty(name = "logi_list",value = "物流公司集合")
    private List<LogisticsCompanyDO> logiList;

    @ApiModelProperty(name = "order_ship_status",value = "订单发货状态")
    private String orderShipStatus;

    @ApiModelProperty(name = "allowable",value = "售后服务单运行操作情况")
    private ServiceOperateAllowable allowable;

    @ApiModelProperty(name = "order_payment_type",value = "订单付款类型 ONLINE：在线支付，COD：货到付款")
    private String orderPaymentType;

    public String getServiceTypeText() {
        if (this.getServiceType() != null) {
            ServiceTypeEnum serviceTypeEnum = ServiceTypeEnum.valueOf(this.getServiceType());
            serviceTypeText = serviceTypeEnum.description();
        }
        return serviceTypeText;
    }

    public void setServiceTypeText(String serviceTypeText) {
        this.serviceTypeText = serviceTypeText;
    }

    public String getServiceStatusText() {
        if (this.getServiceStatus() != null) {
            ServiceStatusEnum serviceStatusEnum = ServiceStatusEnum.valueOf(this.getServiceStatus());
            serviceStatusText = serviceStatusEnum.description();
        }
        return serviceStatusText;
    }

    public void setServiceStatusText(String serviceStatusText) {
        this.serviceStatusText = serviceStatusText;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getReturnNum() {
        return returnNum;
    }

    public void setReturnNum(Integer returnNum) {
        this.returnNum = returnNum;
    }

    public AfterSaleRefundDO getRefundInfo() {
        return refundInfo;
    }

    public void setRefundInfo(AfterSaleRefundDO refundInfo) {
        this.refundInfo = refundInfo;
    }

    public AfterSaleChangeDO getChangeInfo() {
        return changeInfo;
    }

    public void setChangeInfo(AfterSaleChangeDO changeInfo) {
        this.changeInfo = changeInfo;
    }

    public AfterSaleExpressDO getExpressInfo() {
        return expressInfo;
    }

    public void setExpressInfo(AfterSaleExpressDO expressInfo) {
        this.expressInfo = expressInfo;
    }

    public List<AfterSaleGoodsDO> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<AfterSaleGoodsDO> goodsList) {
        this.goodsList = goodsList;
    }

    public List<AfterSaleGalleryDO> getImages() {
        return images;
    }

    public void setImages(List<AfterSaleGalleryDO> images) {
        this.images = images;
    }

    public List<AfterSaleLogDO> getLogs() {
        return logs;
    }

    public void setLogs(List<AfterSaleLogDO> logs) {
        this.logs = logs;
    }

    public List<LogisticsCompanyDO> getLogiList() {
        return logiList;
    }

    public void setLogiList(List<LogisticsCompanyDO> logiList) {
        this.logiList = logiList;
    }

    public String getOrderShipStatus() {
        return orderShipStatus;
    }

    public void setOrderShipStatus(String orderShipStatus) {
        this.orderShipStatus = orderShipStatus;
    }

    public ServiceOperateAllowable getAllowable() {
        return allowable;
    }

    public void setAllowable(ServiceOperateAllowable allowable) {
        this.allowable = allowable;
    }

    public String getOrderPaymentType() {
        return orderPaymentType;
    }

    public void setOrderPaymentType(String orderPaymentType) {
        this.orderPaymentType = orderPaymentType;
    }

    public ApplyAfterSaleVO() {
    }

    public ApplyAfterSaleVO(ReturnGoodsVO returnGoodsVO) {
        this.setOrderSn(returnGoodsVO.getOrderSn());
        this.setReason(returnGoodsVO.getReason());
        this.setProblemDesc(returnGoodsVO.getProblemDesc());
        this.setApplyVouchers(returnGoodsVO.getApplyVouchers());
        this.setSkuId(returnGoodsVO.getSkuId());
        this.setReturnNum(returnGoodsVO.getReturnNum());

        AfterSaleChangeDO afterSaleChangeDO = new AfterSaleChangeDO();
        BeanUtil.copyProperties(returnGoodsVO.getRegion(), afterSaleChangeDO);
        afterSaleChangeDO.setShipName(returnGoodsVO.getShipName());
        afterSaleChangeDO.setShipMobile(returnGoodsVO.getShipMobile());
        afterSaleChangeDO.setShipAddr(returnGoodsVO.getShipAddr());
        this.setChangeInfo(afterSaleChangeDO);


        List<String> images = returnGoodsVO.getImages();
        if (images != null && images.size() != 0) {
            List<AfterSaleGalleryDO> galleryDOS = new ArrayList<>();
            for (String image : images) {
                AfterSaleGalleryDO galleryDO = new AfterSaleGalleryDO();
                galleryDO.setImg(image);
                galleryDOS.add(galleryDO);
            }
            this.setImages(galleryDOS);
        }

        AfterSaleRefundDO refundDO = new AfterSaleRefundDO();
        refundDO.setAccountType(returnGoodsVO.getAccountType());
        refundDO.setReturnAccount(returnGoodsVO.getReturnAccount());
        refundDO.setBankAccountName(returnGoodsVO.getBankAccountName());
        refundDO.setBankAccountNumber(returnGoodsVO.getBankAccountNumber());
        refundDO.setBankDepositName(returnGoodsVO.getBankDepositName());
        refundDO.setBankName(returnGoodsVO.getBankName());
        this.setRefundInfo(refundDO);


    }

    public ApplyAfterSaleVO(AfterSaleApplyVO applyVO) {
        this.setOrderSn(applyVO.getOrderSn());
        this.setReason(applyVO.getReason());
        this.setProblemDesc(applyVO.getProblemDesc());
        this.setApplyVouchers(applyVO.getApplyVouchers());
        this.setSkuId(applyVO.getSkuId());
        this.setReturnNum(applyVO.getReturnNum());

        AfterSaleChangeDO afterSaleChangeDO = new AfterSaleChangeDO();
        BeanUtil.copyProperties(applyVO.getRegion(), afterSaleChangeDO);
        afterSaleChangeDO.setShipName(applyVO.getShipName());
        afterSaleChangeDO.setShipMobile(applyVO.getShipMobile());
        afterSaleChangeDO.setShipAddr(applyVO.getShipAddr());
        this.setChangeInfo(afterSaleChangeDO);

        List<String> images = applyVO.getImages();
        if (images != null && images.size() != 0) {
            List<AfterSaleGalleryDO> galleryDOS = new ArrayList<>();
            for (String image : images) {
                AfterSaleGalleryDO galleryDO = new AfterSaleGalleryDO();
                galleryDO.setImg(image);
                galleryDOS.add(galleryDO);
            }
            this.setImages(galleryDOS);
        }

    }

    @Override
    public String toString() {
        return "ApplyAfterSaleVO{" +
                "serviceTypeText='" + serviceTypeText + '\'' +
                ", serviceStatusText='" + serviceStatusText + '\'' +
                ", skuId=" + skuId +
                ", returnNum=" + returnNum +
                ", refundInfo=" + refundInfo +
                ", changeInfo=" + changeInfo +
                ", expressInfo=" + expressInfo +
                ", goodsList=" + goodsList +
                ", images=" + images +
                ", logs=" + logs +
                ", logiList=" + logiList +
                ", orderShipStatus='" + orderShipStatus + '\'' +
                ", allowable=" + allowable +
                ", orderPaymentType='" + orderPaymentType + '\'' +
                '}';
    }
}
