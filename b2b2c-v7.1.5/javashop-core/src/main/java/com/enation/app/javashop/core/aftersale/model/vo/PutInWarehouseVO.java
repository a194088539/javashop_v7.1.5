package com.enation.app.javashop.core.aftersale.model.vo;

import com.enation.app.javashop.core.aftersale.model.dto.PutInWarehouseDTO;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 售后服务商品入库VO
 * 主要用于商家对售后服务商品进行入库操作
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-11-13
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PutInWarehouseVO implements Serializable {

    private static final long serialVersionUID = -904350570939849447L;

    /**
     * 售后服务单号
     */
    @ApiModelProperty(name = "service_sn", value = "售后服务单号", required = true, dataType = "String")
    private String serviceSn;
    /**
     * 入库商品信息集合
     */
    @ApiModelProperty(value = "入库商品信息集合")
    @Valid
    private List<PutInWarehouseDTO> storageList;
    /**
     * 入库备注
     */
    @ApiModelProperty(name = "remark", value = "入库备注", dataType = "String")
    private String remark;

    public String getServiceSn() {
        return serviceSn;
    }

    public void setServiceSn(String serviceSn) {
        this.serviceSn = serviceSn;
    }

    public List<PutInWarehouseDTO> getStorageList() {
        return storageList;
    }

    public void setStorageList(List<PutInWarehouseDTO> storageList) {
        this.storageList = storageList;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PutInWarehouseVO that = (PutInWarehouseVO) o;
        return Objects.equals(serviceSn, that.serviceSn) &&
                Objects.equals(storageList, that.storageList) &&
                Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceSn, storageList, remark);
    }

    @Override
    public String toString() {
        return "PutInWarehouseVO{" +
                "serviceSn='" + serviceSn + '\'' +
                ", storageList=" + storageList +
                ", remark='" + remark + '\'' +
                '}';
    }
}
