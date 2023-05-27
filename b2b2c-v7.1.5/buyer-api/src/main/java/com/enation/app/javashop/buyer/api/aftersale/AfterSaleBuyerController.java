package com.enation.app.javashop.buyer.api.aftersale;

import com.enation.app.javashop.core.aftersale.model.dos.AfterSaleExpressDO;
import com.enation.app.javashop.core.aftersale.model.dto.AfterSaleOrderDTO;
import com.enation.app.javashop.core.aftersale.model.dto.AfterSaleQueryParam;
import com.enation.app.javashop.core.aftersale.model.enums.CreateChannelEnum;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceTypeEnum;
import com.enation.app.javashop.core.aftersale.model.vo.*;
import com.enation.app.javashop.core.aftersale.service.AfterSaleManager;
import com.enation.app.javashop.core.aftersale.service.AfterSaleQueryManager;
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
 * 售后服务相关API
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-24
 */
@Api(description="售后服务相关API")
@RestController
@RequestMapping("/after-sales")
@Validated
public class AfterSaleBuyerController {

    @Autowired
    private AfterSaleManager afterSaleManager;

    @Autowired
    private AfterSaleQueryManager afterSaleQueryManager;

    @ApiOperation(value = "获取申请售后服务记录列表", response = AfterSaleRecordVO.class)
    @GetMapping()
    public Page list(@Valid AfterSaleQueryParam param){
        param.setMemberId(UserContext.getBuyer().getUid());
        param.setCreateChannel(CreateChannelEnum.NORMAL.value());
        return afterSaleQueryManager.list(param);
    }

    @ApiOperation(value = "获取申请售后页面订单收货信息和商品信息", response = AfterSaleOrderDTO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_sn", value = "订单编号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "sku_id" , value = "商品skuID" , required = true , dataType = "int", paramType = "path")
    })
    @GetMapping(value = "/apply/{order_sn}/{sku_id}")
    public AfterSaleOrderDTO applyServiceInfo(@PathVariable("order_sn") String orderSn, @PathVariable("sku_id") Integer skuId){
        return afterSaleQueryManager.applyOrderInfo(orderSn, skuId);
    }

    @ApiOperation(value = "订单申请售后服务--退货申请", response = ReturnGoodsVO.class)
    @PostMapping(value = "/apply/return/goods")
    public ReturnGoodsVO applyReturnGoods(@Valid ReturnGoodsVO returnGoodsVO){
        ApplyAfterSaleVO applyAfterSaleVO = new ApplyAfterSaleVO(returnGoodsVO);
        applyAfterSaleVO.setServiceType(ServiceTypeEnum.RETURN_GOODS.value());
        afterSaleManager.apply(applyAfterSaleVO);
        return returnGoodsVO;
    }

    @ApiOperation(value = "订单申请售后服务--取消订单", response = RefundApplyVO.class)
    @PostMapping(value = "/apply/cancel/order")
    public RefundApplyVO applyCancelOrder(@Valid RefundApplyVO refundApplyVO){
        afterSaleManager.applyCancelOrder(refundApplyVO);
        return refundApplyVO;
    }

    @ApiOperation(value = "订单申请售后服务--换货申请", response = AfterSaleApplyVO.class)
    @PostMapping(value = "/apply/change/goods")
    public AfterSaleApplyVO applyChangeGoods(@Valid AfterSaleApplyVO afterSaleApplyVO){
        ApplyAfterSaleVO applyAfterSaleVO = new ApplyAfterSaleVO(afterSaleApplyVO);
        applyAfterSaleVO.setServiceType(ServiceTypeEnum.CHANGE_GOODS.value());
        afterSaleManager.apply(applyAfterSaleVO);
        return afterSaleApplyVO;
    }

    @ApiOperation(value = "订单申请售后服务--补发申请", response = AfterSaleApplyVO.class)
    @PostMapping(value = "/apply/replace/goods")
    public AfterSaleApplyVO applyReplaceGoods(@Valid AfterSaleApplyVO afterSaleApplyVO){
        ApplyAfterSaleVO applyAfterSaleVO = new ApplyAfterSaleVO(afterSaleApplyVO);
        applyAfterSaleVO.setServiceType(ServiceTypeEnum.SUPPLY_AGAIN_GOODS.value());
        afterSaleManager.apply(applyAfterSaleVO);
        return afterSaleApplyVO;
    }

    @ApiOperation(value = "保存用户填写的物流信息", response = AfterSaleExpressDO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "service_sn", value = "售后服务单号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "courier_company_id" , value = "物流公司ID" , required = true , dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "courier_number" , value = "快递单号" , required = true , dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ship_time" , value = "发货时间" , required = true , dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/apply/ship/{service_sn}")
    public AfterSaleExpressDO applyShip(@PathVariable("service_sn") String serviceSn, @ApiIgnore Integer courierCompanyId, @ApiIgnore String courierNumber, @ApiIgnore Long shipTime){
        AfterSaleExpressDO afterSaleExpressDO = new AfterSaleExpressDO();
        afterSaleExpressDO.setServiceSn(serviceSn);
        afterSaleExpressDO.setCourierCompanyId(courierCompanyId);
        afterSaleExpressDO.setCourierNumber(courierNumber);
        afterSaleExpressDO.setShipTime(shipTime);
        this.afterSaleManager.applyShip(afterSaleExpressDO);
        return afterSaleExpressDO;
    }

    @ApiOperation(value = "获取售后服务详细信息", response = ApplyAfterSaleVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "service_sn", value = "售后服务单号", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping(value = "/detail/{service_sn}")
    public ApplyAfterSaleVO detail(@PathVariable("service_sn") String serviceSn){

        return afterSaleQueryManager.detail(serviceSn);
    }
}
