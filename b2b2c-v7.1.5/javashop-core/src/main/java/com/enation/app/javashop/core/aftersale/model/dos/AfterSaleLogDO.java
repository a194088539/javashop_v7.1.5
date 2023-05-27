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
 * 售后日志实体
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-15
 */
@Table(name = "es_as_log")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AfterSaleLogDO implements Serializable {

    private static final long serialVersionUID = -333279504325450280L;

    /**
     * 主键ID
     */
    @Id(name = "id")
    @ApiModelProperty(hidden=true)
    private Integer id;
    /**
     * 售后/退款编号
     */
    @Column(name = "sn")
    @ApiModelProperty(name = "sn", value = "售后/退款编号", required = false)
    private String sn;
    /**
     * 创建时间
     */
    @Column(name = "log_time")
    @ApiModelProperty(name="log_time",value="创建时间",required=false)
    private Long logTime;
    /**
     * 详细信息
     */
    @Column(name = "log_detail")
    @ApiModelProperty(name = "log_detail", value = "详细信息", required = false)
    private String logDetail;
    /**
     * 操作人
     */
    @Column(name = "operator")
    @ApiModelProperty(name = "operator", value = "操作人", required = false)
    private String operator;

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

    public Long getLogTime() {
        return logTime;
    }

    public void setLogTime(Long logTime) {
        this.logTime = logTime;
    }

    public String getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(String logDetail) {
        this.logDetail = logDetail;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AfterSaleLogDO that = (AfterSaleLogDO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(sn, that.sn) &&
                Objects.equals(logTime, that.logTime) &&
                Objects.equals(logDetail, that.logDetail) &&
                Objects.equals(operator, that.operator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sn, logTime, logDetail, operator);
    }

    @Override
    public String toString() {
        return "AfterSaleLogDO{" +
                "id=" + id +
                ", sn='" + sn + '\'' +
                ", logTime=" + logTime +
                ", logDetail='" + logDetail + '\'' +
                ", operator='" + operator + '\'' +
                '}';
    }
}
