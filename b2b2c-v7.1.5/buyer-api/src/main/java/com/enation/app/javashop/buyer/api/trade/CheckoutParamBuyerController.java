package com.enation.app.javashop.buyer.api.trade;

import com.enation.app.javashop.core.member.model.dos.ReceiptHistory;
import com.enation.app.javashop.core.member.model.dto.ReceiptHistoryDTO;
import com.enation.app.javashop.core.member.model.enums.ReceiptTypeEnum;
import com.enation.app.javashop.core.trade.order.model.enums.PaymentTypeEnum;
import com.enation.app.javashop.core.trade.order.model.vo.CheckoutParamVO;
import com.enation.app.javashop.core.trade.order.service.CheckoutParamManager;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.exception.SystemErrorCodeV1;
import com.enation.app.javashop.framework.util.BeanUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 结算参数控制器
 *
 * @author Snow create in 2018/4/8
 * @version v2.0
 * @since v7.0.0
 */
@Api(description = "结算参数接口模块")
@RestController
@RequestMapping("/trade/checkout-params")
@Validated
public class CheckoutParamBuyerController {

    @Autowired
    private CheckoutParamManager checkoutParamManager;



    @ApiOperation(value = "设置收货地址id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address_id", value = "收货地址id", required = true, dataType = "int", paramType = "path"),
    })
    @PostMapping(value = "/address-id/{address_id}")
    public void setAddressId(@NotNull(message = "必须指定收货地址id") @PathVariable(value = "address_id") Integer addressId) {

        //设置收货地址
        this.checkoutParamManager.setAddressId(addressId);

    }



    @ApiOperation(value = "设置支付类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "payment_type", value = "支付类型 在线支付：ONLINE，货到付款：COD", required = true, dataType = "String", paramType = "query", allowableValues = "ONLINE,COD")
    })
    @PostMapping(value = "/payment-type")
    public void setPaymentType(@ApiIgnore @NotNull(message = "必须指定支付类型") String paymentType) {


        PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.valueOf(paymentType.toUpperCase());

        //检测是否支持货到付款
        this.checkoutParamManager.checkCod(paymentTypeEnum);
        this.checkoutParamManager.setPaymentType(paymentTypeEnum);

    }

    @ApiOperation(value = "设置发票信息")
    @PostMapping(value = "/receipt")
    public void setReceipt(@Valid ReceiptHistoryDTO receiptHistoryDTO) {

        ReceiptHistory receiptHistory = new ReceiptHistory();
        BeanUtil.copyProperties(receiptHistoryDTO, receiptHistory);

        if (StringUtil.isEmpty(receiptHistory.getReceiptTitle())) {
            throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "发票抬头不能为空");
        }

        if (!"个人".equals(receiptHistory.getReceiptTitle()) && StringUtil.isEmpty(receiptHistory.getTaxNo())) {
            throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "纳税人识别号不能为空");
        }

        if (StringUtil.isEmpty(receiptHistory.getReceiptContent())) {
            throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "发票内容不能为空");
        }

        //如果发票类型为增值税专用发票
        if (ReceiptTypeEnum.VATOSPECIAL.value().equals(receiptHistory.getReceiptType())) {

            if (StringUtil.isEmpty(receiptHistory.getReceiptMethod())) {
                throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "收票方式不能为空");
            }

            if (StringUtil.isEmpty(receiptHistory.getRegAddr())) {
                throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "注册地址不能为空");
            }

            if (StringUtil.isEmpty(receiptHistory.getRegTel())) {
                throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "注册电话不能为空");
            }

            if (StringUtil.isEmpty(receiptHistory.getBankName())) {
                throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "开户银行不能为空");
            }

            if (StringUtil.isEmpty(receiptHistory.getBankAccount())) {
                throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "银行账户不能为空");
            }

            if (StringUtil.isEmpty(receiptHistory.getMemberMobile())) {
                throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "收票人手机号不能为空");
            }

            if (StringUtil.isEmpty(receiptHistory.getMemberName())) {
                throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "收票人姓名不能为空");
            }

            if (StringUtil.isEmpty(receiptHistory.getDetailAddr())) {
                throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "详细地址不能为空");
            }

            BeanUtil.copyProperties(receiptHistoryDTO.getRegion(), receiptHistory);
        }

        if(ReceiptTypeEnum.ELECTRO.value().equals(receiptHistory.getReceiptType())){
            //电子发票
            if (StringUtil.isEmpty(receiptHistory.getMemberMobile())) {
                throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "收票人手机号不能为空");
            }

        }

        this.checkoutParamManager.setReceipt(receiptHistory);
    }

    @ApiOperation(value = "取消发票")
    @DeleteMapping(value = "/receipt")
    public void delReceipt() {
        checkoutParamManager.deleteReceipt();
    }


    @ApiOperation(value = "设置送货时间")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "receive_time", value = "送货时间", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping(value = "/receive-time")
    public void setReceiveTime(@ApiIgnore @NotNull(message = "必须指定送货时间") String receiveTime) {

        this.checkoutParamManager.setReceiveTime(receiveTime);

    }


    @ApiOperation(value = "设置订单备注")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "remark", value = "订单备注", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping(value = "/remark")
    public void setRemark(String remark) {

        this.checkoutParamManager.setRemark(remark);
    }


    @ApiOperation(value = "获取结算参数", response = CheckoutParamVO.class)
    @ResponseBody
    @GetMapping()
    public CheckoutParamVO get() {
        return this.checkoutParamManager.getParam();
    }

}
