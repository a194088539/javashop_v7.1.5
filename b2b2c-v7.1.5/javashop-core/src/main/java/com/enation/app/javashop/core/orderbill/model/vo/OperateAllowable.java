package com.enation.app.javashop.core.orderbill.model.vo;

import com.enation.app.javashop.core.goods.model.enums.Permission;
import com.enation.app.javashop.core.orderbill.model.enums.BillStatusEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author fk
 * @version v2.0
 * @Description: 商品的操作权限
 * @date 2018/4/916:06
 * @since v7.0.0
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OperateAllowable implements Serializable {

    /**
     * 账单状态
     */
    private BillStatusEnum status;

    /**
     * 权限
     */
    private Permission permission;

    /**
     * 是否允许对账
     */
    @ApiModelProperty(name = "allow_recon", value = "是否允许对账", required = false)
    private Boolean allowRecon;
    /**
     * 是否允许付款
     */
    @ApiModelProperty(name = "allow_pay", value = "是否允许付款", required = false)
    private Boolean allowPay;
    /**
     * 是否允许审核
     */
    @ApiModelProperty(name = "allow_auth", value = "是否允许审核", required = false)
    private Boolean allowAuth;
    /**
     * 是否允许完成
     */
    @ApiModelProperty(name = "allow_complete", value = "是否允许完成", required = false)
    private Boolean allowComplete;

    @ApiModelProperty(name = "allow_next_step", value = "是否允许下一步操作",hidden = true)
    private Boolean allowNextStep;


    public OperateAllowable(BillStatusEnum status, Permission permission) {
        this.status = status;
        this.permission = permission;
    }

    public OperateAllowable() {

    }

    public Boolean getAllowNextStep() {

        switch (status) {
            case OUT:
                //应该对账
                allowNextStep = getAllowRecon();
                break;
            case RECON:
                //应该管理员审核
                allowNextStep = getAllowAuth();
                break;
            case PASS:
                //应该管理员支付
                allowNextStep = getAllowPay();
                break;
            case PAY:
                //完成
                allowNextStep = getAllowComplete();
                break;
            default:
                break;
        }

        return allowNextStep;
    }

    public Boolean getAllowRecon() {
        //对账  卖家权限并且出账状态
        return BillStatusEnum.OUT.equals(status) && Permission.SELLER.equals(permission);
    }

    public Boolean getAllowAuth() {
        //审核  管理员权限并且对账状态
        return BillStatusEnum.RECON.equals(status) && Permission.ADMIN.equals(permission);
    }

    public Boolean getAllowPay() {
        //支付  管理员权限并且审核状态
        return BillStatusEnum.PASS.equals(status) && Permission.ADMIN.equals(permission);
    }

    public Boolean getAllowComplete() {
        //完成  卖家权限或者没有权限 并且 付款状态
        return BillStatusEnum.PAY.equals(status) && (Permission.SELLER.equals(permission)||Permission.CLIENT.equals(permission));
    }
}
