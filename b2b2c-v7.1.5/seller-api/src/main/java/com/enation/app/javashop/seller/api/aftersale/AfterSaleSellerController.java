package com.enation.app.javashop.seller.api.aftersale;

import com.enation.app.javashop.core.aftersale.model.dto.AfterSaleQueryParam;
import com.enation.app.javashop.core.aftersale.model.vo.*;
import com.enation.app.javashop.core.aftersale.service.AfterSaleManager;
import com.enation.app.javashop.core.aftersale.service.AfterSaleQueryManager;
import com.enation.app.javashop.core.aftersale.service.SellerCreateTradeManager;
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
import java.util.List;

/**
 * 售后服务相关API
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-24
 */
@Api(description = "售后服务相关API")
@RestController
@RequestMapping("/seller/after-sales")
@Validated
public class AfterSaleSellerController {

    @Autowired
    private AfterSaleManager afterSaleManager;

    @Autowired
    private AfterSaleQueryManager afterSaleQueryManager;

    @Autowired
    private SellerCreateTradeManager sellerCreateTradeManager;

    @ApiOperation(value = "获取申请售后服务记录列表", response = AfterSaleRecordVO.class)
    @GetMapping()
    public Page list(@Valid AfterSaleQueryParam param){
        param.setSellerId(UserContext.getSeller().getSellerId());

        return afterSaleQueryManager.list(param);
    }

    @ApiOperation(value = "获取售后服务详细信息", response = ApplyAfterSaleVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "service_sn", value = "售后服务单号", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping(value = "/detail/{service_sn}")
    public ApplyAfterSaleVO detail(@PathVariable("service_sn") String serviceSn){

        return afterSaleQueryManager.detail(serviceSn);
    }

    @ApiOperation(value = "商家审核售后服务申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "service_sn", value = "售后服务单号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "audit_status", value = "审核状态 PASS：审核通过，REFUSE：审核未通过", required = true, dataType = "String", paramType = "query", allowableValues = "PASS,REFUSE"),
            @ApiImplicitParam(name = "refund_price", value = "退款金额", dataType = "Double", paramType = "query"),
            @ApiImplicitParam(name = "return_addr", value = "退货地址信息", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "audit_remark", value = "商家审核备注", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/audit/{service_sn}")
    public void audit(@PathVariable("service_sn") String serviceSn, @ApiIgnore String auditStatus, @ApiIgnore Double refundPrice, @ApiIgnore String returnAddr, @ApiIgnore String auditRemark){

        this.afterSaleManager.audit(serviceSn, auditStatus, refundPrice, returnAddr, auditRemark);
    }

    @ApiOperation(value = "商家将申请售后服务退还的商品入库")
    @PostMapping(value = "/put-in/warehouse")
    public void putInWarehouse(@Valid @RequestBody PutInWarehouseVO putInWarehouseVO){

        this.afterSaleManager.putInWarehouse(putInWarehouseVO.getServiceSn(), putInWarehouseVO.getStorageList(), putInWarehouseVO.getRemark());
    }

    @ApiOperation(value = "导出售后服务信息", response = AfterSaleExportVO.class)
    @GetMapping(value = "/export")
    public List<AfterSaleExportVO> export(@Valid AfterSaleQueryParam param){
        param.setSellerId(UserContext.getSeller().getSellerId());
        return afterSaleQueryManager.exportAfterSale(param);
    }

    @ApiOperation(value = "商家为售后服务手动创建新订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "service_sn", value = "售后服务单号", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping(value = "/create-order/{service_sn}")
    public void createOrder(@PathVariable("service_sn") String serviceSn){

        this.sellerCreateTradeManager.sellerCreateTrade(serviceSn);
    }

    @ApiOperation(value = "商家关闭售后服务单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "service_sn", value = "售后服务单号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "reason", value = "关闭原因", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/close/{service_sn}")
    public void close(@PathVariable("service_sn") String serviceSn, @ApiIgnore String reason){

        this.afterSaleManager.closeAfterSale(serviceSn, reason);
    }
}
