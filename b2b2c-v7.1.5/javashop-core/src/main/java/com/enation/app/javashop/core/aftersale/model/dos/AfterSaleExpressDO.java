package com.enation.app.javashop.core.aftersale.model.dos;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 售后物流信息实体
 * 主要用于退货、换货审核通过后，用户邮寄货品时使用
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-15
 */
@Table(name = "es_as_express")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AfterSaleExpressDO implements Serializable {

    private static final long serialVersionUID = -3412149499413072796L;

    /**
     * 主键ID
     */
    @Id(name = "id")
    @ApiModelProperty(hidden=true)
    private Integer id;
    /**
     * 售后服务单号
     */
    @Column(name = "service_sn")
    @ApiModelProperty(name = "service_sn", value = "售后服务单号", required = false)
    private String serviceSn;
    /**
     * 物流单号
     */
    @Column(name = "courier_number")
    @ApiModelProperty(name = "courier_number", value = "物流单号", required = false)
    private String courierNumber;
    /**
     * 物流公司id
     */
    @Column(name = "courier_company_id")
    @ApiModelProperty(name = "courier_company_id", value = "物流公司id", required = false)
    private Integer courierCompanyId;
    /**
     * 物流公司名称
     */
    @Column(name = "courier_company")
    @ApiModelProperty(name = "courier_company", value = "物流公司名称", required = false)
    private String courierCompany;
    /**
     * 发货时间
     */
    @Column(name = "ship_time")
    @ApiModelProperty(name = "ship_time", value = "发货时间", required = false)
    private Long shipTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceSn() {
        return serviceSn;
    }

    public void setServiceSn(String serviceSn) {
        this.serviceSn = serviceSn;
    }

    public String getCourierNumber() {
        return courierNumber;
    }

    public void setCourierNumber(String courierNumber) {
        this.courierNumber = courierNumber;
    }

    public Integer getCourierCompanyId() {
        return courierCompanyId;
    }

    public void setCourierCompanyId(Integer courierCompanyId) {
        this.courierCompanyId = courierCompanyId;
    }

    public String getCourierCompany() {
        return courierCompany;
    }

    public void setCourierCompany(String courierCompany) {
        this.courierCompany = courierCompany;
    }

    public Long getShipTime() {
        return shipTime;
    }

    public void setShipTime(Long shipTime) {
        this.shipTime = shipTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AfterSaleExpressDO that = (AfterSaleExpressDO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(serviceSn, that.serviceSn) &&
                Objects.equals(courierNumber, that.courierNumber) &&
                Objects.equals(courierCompanyId, that.courierCompanyId) &&
                Objects.equals(courierCompany, that.courierCompany) &&
                Objects.equals(shipTime, that.shipTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceSn, courierNumber, courierCompanyId, courierCompany, shipTime);
    }

    @Override
    public String toString() {
        return "AfterSaleExpressDO{" +
                "id=" + id +
                ", serviceSn='" + serviceSn + '\'' +
                ", courierNumber='" + courierNumber + '\'' +
                ", courierCompanyId=" + courierCompanyId +
                ", courierCompany='" + courierCompany + '\'' +
                ", shipTime=" + shipTime +
                '}';
    }
}
