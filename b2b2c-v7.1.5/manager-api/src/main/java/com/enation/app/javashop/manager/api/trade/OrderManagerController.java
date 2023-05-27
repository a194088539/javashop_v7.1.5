package com.enation.app.javashop.manager.api.trade;

import com.enation.app.javashop.core.client.member.MemberHistoryReceiptClient;
import com.enation.app.javashop.core.trade.cart.model.dos.OrderPermission;
import com.enation.app.javashop.core.trade.order.model.dos.OrderLogDO;
import com.enation.app.javashop.core.trade.order.model.dto.OrderQueryParam;
import com.enation.app.javashop.core.trade.order.model.vo.CancelVO;
import com.enation.app.javashop.core.trade.order.model.vo.OrderDetailVO;
import com.enation.app.javashop.core.trade.order.model.vo.OrderLineVO;
import com.enation.app.javashop.core.trade.order.service.OrderLogManager;
import com.enation.app.javashop.core.trade.order.service.OrderOperateManager;
import com.enation.app.javashop.core.trade.order.service.OrderQueryManager;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 平台订单API
 *
 * @author Snow create in 2018/6/13
 * @version v2.0
 * @since v7.0.0
 */
@Api(description = "平台订单API")
@RestController
@RequestMapping("/admin/trade/orders")
@Validated
public class OrderManagerController {

    @Autowired
    private OrderQueryManager orderQueryManager;

    @Autowired
    private OrderOperateManager orderOperateManager;

    @Autowired
    private OrderLogManager orderLogManager;

    @Autowired
    private MemberHistoryReceiptClient memberHistoryReceiptClient;


    @ApiOperation(value = "查询订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_sn", value = "订单编号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ship_name", value = "收货人", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goods_name", value = "商品名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "buyer_name", value = "买家名字", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start_time", value = "开始时间", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "end_time", value = "结束时间", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "seller_id", value = "店铺ID", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "order_status", value = "订单状态", dataType = "String", paramType = "query",
                    allowableValues = "ALL,WAIT_PAY,WAIT_SHIP,WAIT_ROG,CANCELLED,COMPLETE,WAIT_COMMENT,REFUND",
                    example = "ALL:所有订单,WAIT_PAY:待付款,WAIT_SHIP:待发货,WAIT_ROG:待收货," +
                            "CANCELLED:已取消,COMPLETE:已完成,WAIT_COMMENT:待评论,REFUND:售后中"),
            @ApiImplicitParam(name = "member_id", value = "会员ID", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_no", value = "页数", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "条数", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "payment_type", value = "支付方式", dataType = "String", paramType = "query",
                    allowableValues = "ONLINE,COD", example = "ONLINE:在线支付,COD:货到付款"),
            @ApiImplicitParam(name = "client_type", value = "订单来源", dataType = "String", paramType = "query",
                    allowableValues = "PC,WAP,NATIVE,REACT,MINI", example = "PC:pc客户端,WAP:WAP客户端,NATIVE:原生APP,REACT:RNAPP,MINI:小程序")
    })
    @GetMapping()
    public Page list(@ApiIgnore String orderSn, @ApiIgnore String shipName, @ApiIgnore String goodsName, @ApiIgnore String buyerName,
                     @ApiIgnore Long startTime, @ApiIgnore Long endTime, @ApiIgnore String orderStatus,
                     @ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize, @ApiIgnore Integer memberId, @ApiIgnore Integer sellerId,
                     @ApiIgnore String paymentType, @ApiIgnore String clientType) {

        OrderQueryParam param = new OrderQueryParam();
        param.setOrderSn(orderSn);
        param.setShipName(shipName);
        param.setGoodsName(goodsName);
        param.setBuyerName(buyerName);
        param.setOrderStatus(orderStatus);
        param.setStartTime(startTime);
        param.setEndTime(endTime);
        param.setPageNo(pageNo);
        param.setPageSize(pageSize);
        param.setMemberId(memberId);
        param.setSellerId(sellerId);
        param.setPaymentType(paymentType);
        param.setClientType(clientType);

        Page page = this.orderQueryManager.list(param);
        return page;
    }


    @ApiOperation(value = "导出订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_sn", value = "订单编号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ship_name", value = "收货人", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goods_name", value = "商品名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "buyer_name", value = "买家名字", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start_time", value = "开始时间", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "end_time", value = "结束时间", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "seller_id", value = "店铺ID", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "order_status", value = "订单状态", dataType = "String", paramType = "query",
                    allowableValues = "ALL,WAIT_PAY,WAIT_SHIP,WAIT_ROG,CANCELLED,COMPLETE,WAIT_COMMENT,REFUND",
                    example = "ALL:所有订单,WAIT_PAY:待付款,WAIT_SHIP:待发货,WAIT_ROG:待收货," +
                            "CANCELLED:已取消,COMPLETE:已完成,WAIT_COMMENT:待评论,REFUND:售后中"),
            @ApiImplicitParam(name = "payment_type", value = "支付方式", dataType = "String", paramType = "query",
                    allowableValues = "ONLINE,COD", example = "ONLINE:在线支付,COD:货到付款"),
            @ApiImplicitParam(name = "client_type", value = "订单来源", dataType = "String", paramType = "query",
                    allowableValues = "PC,WAP,NATIVE,REACT,MINI", example = "PC:pc客户端,WAP:WAP客户端,NATIVE:原生APP,REACT:RNAPP,MINI:小程序")
    })
    @GetMapping("/export")
    public List<OrderLineVO> export(@ApiIgnore String orderSn, @ApiIgnore String shipName, @ApiIgnore String goodsName, @ApiIgnore String buyerName,
                       @ApiIgnore Long startTime, @ApiIgnore Long endTime, @ApiIgnore String orderStatus, @ApiIgnore Integer sellerId,
                       @ApiIgnore String paymentType, @ApiIgnore String clientType) {

        OrderQueryParam param = new OrderQueryParam();
        param.setOrderSn(orderSn);
        param.setShipName(shipName);
        param.setGoodsName(goodsName);
        param.setBuyerName(buyerName);
        param.setOrderStatus(orderStatus);
        param.setStartTime(startTime);
        param.setEndTime(endTime);
        param.setSellerId(sellerId);
        param.setPaymentType(paymentType);
        param.setClientType(clientType);

        List<OrderLineVO> lineList = this.orderQueryManager.export(param);
        return lineList;
    }


    @ApiOperation(value = "查询单个订单明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_sn", value = "订单编号", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping(value = "/{order_sn}")
    public OrderDetailVO get(@ApiIgnore @PathVariable("order_sn") String orderSn) {
        OrderDetailVO detailVO = this.orderQueryManager.getModel(orderSn, null);
        if (detailVO.getNeedReceipt().intValue() == 1) {
            detailVO.setReceiptHistory(memberHistoryReceiptClient.getReceiptHistory(orderSn));
        }

        return detailVO;
    }


    @ApiOperation(value = "确认收款")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_sn", value = "订单编号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "pay_price", value = "付款金额", dataType = "double", paramType = "query")
    })
    @PostMapping(value = "/{order_sn}/pay")
    public String payOrder(@ApiIgnore @PathVariable("order_sn") String orderSn, @ApiIgnore Double payPrice) {
        this.orderOperateManager.payOrder(orderSn, payPrice, "",OrderPermission.admin);
        return "";
    }


    @ApiOperation(value = "取消订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_sn", value = "订单编号", required = true, dataType = "String", paramType = "path"),
    })
    @PostMapping(value = "/{order_sn}/cancelled")
    public String cancelledOrder(@ApiIgnore @PathVariable("order_sn") String orderSn) {

        CancelVO cancelVO = new CancelVO();
        cancelVO.setReason("管理员取消");
        cancelVO.setOrderSn(orderSn);
        cancelVO.setOperator("平台管理员");

        this.orderOperateManager.cancel(cancelVO, OrderPermission.admin);
        return "";
    }


    @ApiOperation(value = "查询订单日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_sn", value = "订单编号", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping(value = "/{order_sn}/log")
    public List<OrderLogDO> getList(@ApiIgnore @PathVariable("order_sn") String orderSn) {
        List<OrderLogDO> logDOList = this.orderLogManager.listAll(orderSn);
        return logDOList;
    }

}
