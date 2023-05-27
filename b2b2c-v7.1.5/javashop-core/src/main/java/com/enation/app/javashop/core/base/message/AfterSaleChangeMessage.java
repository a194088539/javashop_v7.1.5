package com.enation.app.javashop.core.base.message;

import com.enation.app.javashop.core.aftersale.model.dto.PutInWarehouseDTO;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceStatusEnum;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceTypeEnum;

import java.io.Serializable;
import java.util.List;

/**
 * 售后服务单状态变化消息实体
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-24
 */
public class AfterSaleChangeMessage implements Serializable {

    private static final long serialVersionUID = -8761441924918746501L;

    /**
     * 售后服务单号
     */
    private String serviceSn;

    /**
     * 申请售后服务的订单编号
     */
    private String orderSn;

    /**
     * 售后服务单类型
     */
    private ServiceTypeEnum serviceType;

    /**
     * 售后服务单状态
     */
    private ServiceStatusEnum serviceStatus;

    /**
     * 商品入库信息集合
     */
    private List<PutInWarehouseDTO> storageList;


    public AfterSaleChangeMessage(String serviceSn, ServiceTypeEnum serviceType, ServiceStatusEnum serviceStatus) {
        this.serviceSn = serviceSn;
        this.serviceType = serviceType;
        this.serviceStatus = serviceStatus;
    }

    public AfterSaleChangeMessage(String serviceSn, String orderSn, ServiceTypeEnum serviceType, ServiceStatusEnum serviceStatus, List<PutInWarehouseDTO> storageList) {
        this.serviceSn = serviceSn;
        this.orderSn = orderSn;
        this.serviceType = serviceType;
        this.serviceStatus = serviceStatus;
        this.storageList = storageList;
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

    public ServiceTypeEnum getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceTypeEnum serviceType) {
        this.serviceType = serviceType;
    }

    public ServiceStatusEnum getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(ServiceStatusEnum serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public List<PutInWarehouseDTO> getStorageList() {
        return storageList;
    }

    public void setStorageList(List<PutInWarehouseDTO> storageList) {
        this.storageList = storageList;
    }

    @Override
    public String toString() {
        return "AfterSaleChangeMessage{" +
                "serviceSn='" + serviceSn + '\'' +
                ", orderSn='" + orderSn + '\'' +
                ", serviceType=" + serviceType +
                ", serviceStatus=" + serviceStatus +
                ", storageList=" + storageList +
                '}';
    }
}
