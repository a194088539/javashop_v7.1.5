package com.enation.app.javashop.core.aftersale.model.dto;

import com.enation.app.javashop.core.aftersale.model.dos.AfterSaleServiceDO;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceStatusEnum;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceTypeEnum;
import com.enation.app.javashop.framework.util.StringUtil;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 售后服务可进行的操作
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-11-13
 */
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ServiceOperateAllowable implements Serializable {

    @ApiModelProperty(value = "是否允许商家审核")
    private Boolean allowAudit;

    @ApiModelProperty(value = "是否允许用户退还商品（填充物流信息）")
    private Boolean allowShip;

    @ApiModelProperty(value = "是否允许商家入库")
    private Boolean allowPutInStore;

    @ApiModelProperty(value = "是否允许平台退款")
    private Boolean allowAdminRefund;

    @ApiModelProperty(value = "是否允许显示入库数量")
    private Boolean allowShowStorageNum;

    @ApiModelProperty(value = "是否允许显示退货地址")
    private Boolean allowShowReturnAddr;

    @ApiModelProperty(value = "是否允许显示用户填写的物流信息")
    private Boolean allowShowShipInfo;

    @ApiModelProperty(value = "是否允许商家退款")
    private Boolean allowSellerRefund;

    @ApiModelProperty(value = "是否允许商家手动创建新订单")
    private Boolean allowSellerCreateOrder;

    @ApiModelProperty(value = "是否允许商家关闭售后服务")
    private Boolean allowSellerClose;

    public Boolean getAllowAudit() {
        return allowAudit;
    }

    public void setAllowAudit(Boolean allowAudit) {
        this.allowAudit = allowAudit;
    }

    public Boolean getAllowShip() {
        return allowShip;
    }

    public void setAllowShip(Boolean allowShip) {
        this.allowShip = allowShip;
    }

    public Boolean getAllowPutInStore() {
        return allowPutInStore;
    }

    public void setAllowPutInStore(Boolean allowPutInStore) {
        this.allowPutInStore = allowPutInStore;
    }

    public Boolean getAllowAdminRefund() {
        return allowAdminRefund;
    }

    public void setAllowAdminRefund(Boolean allowAdminRefund) {
        this.allowAdminRefund = allowAdminRefund;
    }

    public Boolean getAllowShowStorageNum() {
        return allowShowStorageNum;
    }

    public void setAllowShowStorageNum(Boolean allowShowStorageNum) {
        this.allowShowStorageNum = allowShowStorageNum;
    }

    public Boolean getAllowShowReturnAddr() {
        return allowShowReturnAddr;
    }

    public void setAllowShowReturnAddr(Boolean allowShowReturnAddr) {
        this.allowShowReturnAddr = allowShowReturnAddr;
    }

    public Boolean getAllowShowShipInfo() {
        return allowShowShipInfo;
    }

    public void setAllowShowShipInfo(Boolean allowShowShipInfo) {
        this.allowShowShipInfo = allowShowShipInfo;
    }

    public Boolean getAllowSellerRefund() {
        return allowSellerRefund;
    }

    public void setAllowSellerRefund(Boolean allowSellerRefund) {
        this.allowSellerRefund = allowSellerRefund;
    }

    public Boolean getAllowSellerCreateOrder() {
        return allowSellerCreateOrder;
    }

    public void setAllowSellerCreateOrder(Boolean allowSellerCreateOrder) {
        this.allowSellerCreateOrder = allowSellerCreateOrder;
    }

    public Boolean getAllowSellerClose() {
        return allowSellerClose;
    }

    public void setAllowSellerClose(Boolean allowSellerClose) {
        this.allowSellerClose = allowSellerClose;
    }

    public ServiceOperateAllowable() {

    }

    /**
     * 构建各种状态
     * @param serviceDO
     */
    public ServiceOperateAllowable(AfterSaleServiceDO serviceDO) {

        //获取售后服务类型
        String serviceType = serviceDO.getServiceType();
        //获取售后服务状态
        String serviceStatus = serviceDO.getServiceStatus();
        //获取售后服务生成的新订单编号
        String newOrderSn = serviceDO.getNewOrderSn();

        //是否允许商家审核：售后服务状态为新申请即可被商家审核
        this.allowAudit = ServiceStatusEnum.APPLY.value().equals(serviceStatus);

        //是否允许用户退还商品（填充物流信息）：售后服务类型为退货并且售后服务状态为审核通过 或者 售后服务类型为换货并且售后服务状态为审核通过并且售后服务已成功生成了新订单
        this.allowShip = (ServiceTypeEnum.RETURN_GOODS.value().equals(serviceType) && ServiceStatusEnum.PASS.value().equals(serviceStatus))
                || (ServiceTypeEnum.CHANGE_GOODS.value().equals(serviceType) && ServiceStatusEnum.PASS.value().equals(serviceStatus) && StringUtil.notEmpty(newOrderSn));

        //是否允许商家入库：售后服务类型为取消订单并且审核通过 或者 售后服务类型为退货或者换货并且用户已经返还了商品
        this.allowPutInStore = (ServiceTypeEnum.ORDER_CANCEL.value().equals(serviceType) && ServiceStatusEnum.PASS.value().equals(serviceStatus))
                || ((ServiceTypeEnum.RETURN_GOODS.value().equals(serviceType) || ServiceTypeEnum.CHANGE_GOODS.value().equals(serviceType)) && ServiceStatusEnum.FULL_COURIER.value().equals(serviceStatus));

        //是否允许平台进行退款：售后服务类型为退货或者取消订单 并且 售后服务状态为待人工处理
        this.allowAdminRefund = (ServiceTypeEnum.RETURN_GOODS.value().equals(serviceType) || ServiceTypeEnum.ORDER_CANCEL.value().equals(serviceType))
                && ServiceStatusEnum.WAIT_FOR_MANUAL.value().equals(serviceStatus);

        //是否允许展示商品的入库数量：售后服务类型不为补发商品 并且 售后服务状态是已入库或者退款中或者待人工处理或者退款失败或者已完成
        this.allowShowStorageNum = !ServiceTypeEnum.SUPPLY_AGAIN_GOODS.value().equals(serviceType) && (ServiceStatusEnum.STOCK_IN.value().equals(serviceStatus)
                || ServiceStatusEnum.REFUNDING.value().equals(serviceStatus) || ServiceStatusEnum.WAIT_FOR_MANUAL.value().equals(serviceStatus)
                || ServiceStatusEnum.REFUNDFAIL.value().equals(serviceStatus) || ServiceStatusEnum.COMPLETED.value().equals(serviceStatus));

        //是否允许展示退货地址：售后服务类型为退货或换货 并且 售后服务状态不等于待审核和审核未通过
        this.allowShowReturnAddr = (ServiceTypeEnum.RETURN_GOODS.value().equals(serviceType) || ServiceTypeEnum.CHANGE_GOODS.value().equals(serviceType))
                && !ServiceStatusEnum.APPLY.value().equals(serviceStatus) && !ServiceStatusEnum.REFUSE.value().equals(serviceStatus);

        //是否允许展示用户填写的物流信息：售后服务类型为退货或换货 并且 售后服务状态不等于待审核、审核未通过、审核通过、已关闭和异常状态
        this.allowShowShipInfo = (ServiceTypeEnum.RETURN_GOODS.value().equals(serviceType) || ServiceTypeEnum.CHANGE_GOODS.value().equals(serviceType))
                && !ServiceStatusEnum.APPLY.value().equals(serviceStatus) && !ServiceStatusEnum.PASS.value().equals(serviceStatus)
                && !ServiceStatusEnum.REFUSE.value().equals(serviceStatus) && !ServiceStatusEnum.CLOSED.value().equals(serviceStatus) && !ServiceStatusEnum.ERROR_EXCEPTION.value().equals(serviceStatus);

        //是否允许商家退款：售后服务类型为退货或取消订单 并且 售后服务状态等于已入库
        this.allowSellerRefund = (ServiceTypeEnum.RETURN_GOODS.value().equals(serviceType) || ServiceTypeEnum.ORDER_CANCEL.value().equals(serviceType))
                && ServiceStatusEnum.STOCK_IN.value().equals(serviceStatus);

        //是否允许商家手动创建新订单：售后服务类型为换货或补发商品 并且 售后服务状态等于异常状态
        this.allowSellerCreateOrder = (ServiceTypeEnum.SUPPLY_AGAIN_GOODS.value().equals(serviceType) || ServiceTypeEnum.CHANGE_GOODS.value().equals(serviceType))
                && ServiceStatusEnum.ERROR_EXCEPTION.value().equals(serviceStatus);

        //是否允许商家关闭售后服务单：售后服务类型为换货或补发商品 并且 售后服务状态等于异常状态
        this.allowSellerClose = (ServiceTypeEnum.SUPPLY_AGAIN_GOODS.value().equals(serviceType) || ServiceTypeEnum.CHANGE_GOODS.value().equals(serviceType))
                && ServiceStatusEnum.ERROR_EXCEPTION.value().equals(serviceStatus);
    }
}
