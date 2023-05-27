package com.enation.app.javashop.core.orderbill.model.vo;

import com.enation.app.javashop.core.orderbill.model.dos.Bill;
import com.enation.app.javashop.core.orderbill.model.enums.BillStatusEnum;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author fk
 * @version v1.0
 * @Description: 账单信息
 * @date 2018/4/27 14:58
 * @since v7.0.0
 */
@Table(name = "es_bill")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BillDetail extends Bill implements Serializable {


    @ApiModelProperty(name = "status_text", value = "状态文本", required = false)
    private String statusText;

    @ApiModelProperty(name = "operate_allowable", value = "允许的操作", required = false)
    private OperateAllowable operateAllowable;


    public OperateAllowable getOperateAllowable() {
        return operateAllowable;
    }

    public void setOperateAllowable(OperateAllowable operateAllowable) {
        this.operateAllowable = operateAllowable;
    }

    public String getStatusText() {

        return BillStatusEnum.valueOf(this.getStatus()).description();
    }
}
