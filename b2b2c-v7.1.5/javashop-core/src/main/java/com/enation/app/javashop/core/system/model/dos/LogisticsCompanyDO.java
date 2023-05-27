package com.enation.app.javashop.core.system.model.dos;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.enation.app.javashop.core.system.model.dto.FormItem;
import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;

import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * 物流公司实体
 *
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-29 15:10:38
 */
@Table(name = "es_logistics_company")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class LogisticsCompanyDO implements Serializable {

    private static final long serialVersionUID = 2885097420270994L;


    @Id(name = "id")
    @ApiModelProperty(name = "id", value = "物流公司id", required = false)
    private Integer id;

    @Column(name = "name")
    @NotEmpty(message = "物流公司名称必填")
    @ApiModelProperty(name = "name", value = "物流公司名称", required = true)
    private String name;

    @Column(name = "code")
    @NotEmpty(message = "物流公司code必填")
    @ApiModelProperty(name = "code", value = "物流公司code", required = true)
    private String code;

    @Column(name = "is_waybill")
    @NotNull(message = "是否支持电子面单必填 1：支持 0：不支持")
    @ApiModelProperty(name = "is_waybill", value = "是否支持电子面单1：支持 0：不支持", required = true)
    private Integer isWaybill;

    @Column(name = "kdcode")
    @ApiModelProperty(name = "kdcode", value = "快递鸟物流公司code", required = true)
    private String kdcode;

    @Column(name = "form_items")
    @ApiModelProperty(name = "form_items", value = "物流公司电子面单表单", hidden = true)
    private String formItems;

    /**是否删除 DELETED：已删除，NORMAL：正常*/
    @Column(name = "delete_status")
    @ApiModelProperty(name="delete_status",value="是否删除 DELETED：已删除，NORMAL：正常",required=false)
    private String deleteStatus;

    /**禁用状态 OPEN：开启，CLOSE：禁用*/
    @Column(name = "disabled")
    @ApiModelProperty(name="disabled",value="禁用状态 OPEN：开启，CLOSE：禁用",required=false)
    private String disabled;

    public LogisticsCompanyDO() {
        super();
    }

    public LogisticsCompanyDO(String name, String code, String kdcode, Integer isWaybill) {
        super();
        this.name = name;
        this.code = code;
        this.kdcode = kdcode;
        this.isWaybill = isWaybill;
    }

    public void setForm(List<FormItem> form) {
        if (form != null||form.size()==0) {
            this.setFormItems(JsonUtil.objectToJson(form));
        }
    }

    public List<FormItem> getForm() {
        if (!StringUtil.isEmpty(this.formItems)) {
            return JsonUtil.jsonToList(formItems, FormItem.class);
        }
        return null;
    }

    @PrimaryKeyField
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getKdcode() {
        return kdcode;
    }

    public Integer getIsWaybill() {
        return isWaybill;
    }

    public void setKdcode(String kdcode) {
        this.kdcode = kdcode;
    }

    public String getFormItems() {
        return formItems;
    }

    public void setFormItems(String formItems) {
        this.formItems = formItems;
    }

    public void setIsWaybill(Integer isWaybill) {
        this.isWaybill = isWaybill;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    @Override
    public String toString() {
        return "LogisticsCompanyDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", isWaybill=" + isWaybill +
                ", kdcode='" + kdcode + '\'' +
                ", formItems='" + formItems + '\'' +
                ", deleteStatus='" + deleteStatus + '\'' +
                ", disabled='" + disabled + '\'' +
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
        LogisticsCompanyDO that = (LogisticsCompanyDO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(code, that.code) &&
                Objects.equals(isWaybill, that.isWaybill) &&
                Objects.equals(kdcode, that.kdcode) &&
                Objects.equals(formItems, that.formItems) &&
                Objects.equals(deleteStatus, that.deleteStatus) &&
                Objects.equals(disabled, that.disabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, isWaybill, kdcode, formItems, deleteStatus, disabled);
    }
}