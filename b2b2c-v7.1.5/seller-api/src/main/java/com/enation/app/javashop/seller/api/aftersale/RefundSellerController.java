package com.enation.app.javashop.seller.api.aftersale;

import com.enation.app.javashop.core.aftersale.model.dto.RefundQueryParam;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceOperateTypeEnum;
import com.enation.app.javashop.core.aftersale.model.vo.RefundRecordVO;
import com.enation.app.javashop.core.aftersale.service.AfterSaleRefundManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * 售后退款相关API
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-28
 */
@Api(description="售后退款相关API")
@RestController
@RequestMapping("/seller/after-sales/refund")
@Validated
public class RefundSellerController {

    @Autowired
    private AfterSaleRefundManager afterSaleRefundManager;

    @ApiOperation(value = "获取售后退款单列表", response = RefundRecordVO.class)
    @GetMapping()
    public Page list(@Valid RefundQueryParam param){
        param.setSellerId(UserContext.getSeller().getSellerId());
        return afterSaleRefundManager.list(param);
    }

    @ApiOperation(value = "在线支付订单商家退款")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "service_sn", value = "售后服务单号", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping(value = "/{service_sn}")
    public void refund(@PathVariable("service_sn") String serviceSn){
        this.afterSaleRefundManager.sellerRefund(serviceSn);
    }

    @ApiOperation(value = "货到付款订单商家退款")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "service_sn", value = "售后退款单号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "refund_price", value = "退款金额", required = true, dataType = "Double", paramType = "query"),
            @ApiImplicitParam(name = "remark", value = "售后退款备注", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/cod/{service_sn}")
    public void codOrderRefund(@PathVariable("service_sn") @ApiIgnore String serviceSn, @ApiIgnore Double refundPrice, @ApiIgnore String remark){
        this.afterSaleRefundManager.adminRefund(serviceSn, refundPrice, remark, ServiceOperateTypeEnum.SELLER_REFUND);
    }
}
