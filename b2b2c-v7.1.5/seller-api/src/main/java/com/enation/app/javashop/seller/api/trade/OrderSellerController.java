package com.enation.app.javashop.seller.api.trade;

import com.enation.app.javashop.core.base.context.Region;
import com.enation.app.javashop.core.base.context.RegionFormat;
import com.enation.app.javashop.core.client.member.MemberHistoryReceiptClient;
import com.enation.app.javashop.core.trade.cart.model.dos.OrderPermission;
import com.enation.app.javashop.core.trade.order.model.dto.OrderDetailQueryParam;
import com.enation.app.javashop.core.trade.order.model.dto.OrderQueryParam;
import com.enation.app.javashop.core.trade.order.model.enums.PaymentTypeEnum;
import com.enation.app.javashop.core.trade.order.model.vo.*;
import com.enation.app.javashop.core.trade.order.service.OrderOperateManager;
import com.enation.app.javashop.core.trade.order.service.OrderQueryManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.security.model.Seller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 商家订单控制器
 *
 * @author Snow create in 2018/6/13
 * @version v2.0
 * @since v7.0.0
 */

@Api(description = "商家订单API")
@RestController
@RequestMapping("/seller/trade/orders")
@Validated
public class OrderSellerController {

    @Autowired
    private OrderQueryManager orderQueryManager;

    @Autowired
    private OrderOperateManager orderOperateManager;

    @Autowired
    private MemberHistoryReceiptClient memberHistoryReceiptClient;

    @ApiOperation(value = "查询会员订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keywords", value = "关键字", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "order_sn", value = "订单编号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "buyer_name", value = "买家姓名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goods_name", value = "商品名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start_time", value = "开始时间", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "end_time", value = "结束时间", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "order_status", value = "订单状态", dataType = "String", paramType = "query",
                    allowableValues = "ALL,WAIT_PAY,WAIT_SHIP,WAIT_ROG,CANCELLED,COMPLETE,WAIT_COMMENT,REFUND",
                    example = "ALL:所有订单,WAIT_PAY:待付款,WAIT_SHIP:待发货,WAIT_ROG:待收货," +
                            "CANCELLED:已取消,COMPLETE:已完成,WAIT_COMMENT:待评论,REFUND:售后中"),
            @ApiImplicitParam(name = "page_no", value = "页数", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "条数", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "client_type", value = "订单来源", dataType = "String", paramType = "query",
                    allowableValues = "PC,WAP,NATIVE,REACT,MINI", example = "PC:pc客户端,WAP:WAP客户端,NATIVE:原生APP,REACT:RNAPP,MINI:小程序")
    })
    @GetMapping()
    public Page<OrderLineVO> list(@ApiIgnore String orderSn, @ApiIgnore String buyerName, @ApiIgnore String goodsName,
                                  @ApiIgnore Long startTime, @ApiIgnore Long endTime, @ApiIgnore String orderStatus,
                                  @ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize, @ApiIgnore String keywords, @ApiIgnore String clientType) {

        Seller seller = UserContext.getSeller();
        OrderQueryParam param = new OrderQueryParam();
        param.setOrderSn(orderSn);
        param.setBuyerName(buyerName);
        param.setGoodsName(goodsName);
        param.setStartTime(startTime);
        param.setEndTime(endTime);
        param.setTag(orderStatus);
        param.setPageNo(pageNo);
        param.setPageSize(pageSize);
        param.setSellerId(seller.getSellerId());
        param.setKeywords(keywords);
        param.setClientType(clientType);

        Page<OrderLineVO> page = this.orderQueryManager.list(param);
        return page;
    }


    @ApiOperation(value = "查询单个订单明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_sn", value = "订单编号", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping(value = "/{order_sn}")
    public OrderDetailVO get(@ApiIgnore @PathVariable("order_sn") String orderSn) {
        Seller seller = UserContext.getSeller();
        OrderDetailQueryParam queryParam = new OrderDetailQueryParam();
        queryParam.setSellerId(seller.getSellerId());
        OrderDetailVO detailVO = this.orderQueryManager.getModel(orderSn, queryParam);

        //在线支付的订单商家不允许确认收款
        if (PaymentTypeEnum.ONLINE.value().equals(detailVO.getPaymentType())) {
            detailVO.getOrderOperateAllowableVO().setAllowPay(false);
        }


        if (detailVO.getNeedReceipt().intValue() == 1) {
            detailVO.setReceiptHistory(memberHistoryReceiptClient.getReceiptHistory(orderSn));
        }

        return detailVO;
    }


    @ApiOperation(value = "查询订单状态的数量")
    @GetMapping(value = "/status-num")
    public OrderStatusNumVO getStatusNum() {
        Seller seller = UserContext.getSeller();
        OrderStatusNumVO orderStatusNumVO = this.orderQueryManager.getOrderStatusNum(null, seller.getSellerId());
        return orderStatusNumVO;
    }


    @ApiOperation(value = "订单发货", notes = "商家对某订单执行发货操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_sn", value = "订单sn", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "ship_no", value = "发货单号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "logi_id", value = "物流公司id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "logi_name", value = "物流公司名称", required = true, dataType = "String", paramType = "query"),
    })
    @ResponseBody
    @PostMapping(value = "/{order_sn}/delivery")
    public String ship(@ApiIgnore @NotNull(message = "必须指定订单编号") @PathVariable(name = "order_sn") String orderSn,
                       @ApiIgnore @NotNull(message = "必须输入发货单号") @Length(max = 20, message = "物流单号不正确") String shipNo,
                       @ApiIgnore @NotNull(message = "必须选择物流公司") Integer logiId,
                       @ApiIgnore String logiName) {

        Seller seller = UserContext.getSeller();
        DeliveryVO delivery = new DeliveryVO();
        delivery.setDeliveryNo(shipNo);
        delivery.setOrderSn(orderSn);
        delivery.setLogiId(logiId);
        delivery.setLogiName(logiName);
        delivery.setOperator("店铺:" + seller.getSellerName());
        orderOperateManager.ship(delivery, OrderPermission.seller);

        return "";
    }


    @ApiOperation(value = "商家修改收货人地址", notes = "商家发货前，可以修改收货人地址信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_sn", value = "订单sn", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "ship_name", value = "收货人姓名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "remark", value = "订单备注", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ship_addr", value = "收货地址", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ship_mobile", value = "收货人手机号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ship_tel", value = "收货人电话", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "receive_time", value = "送货时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "region", value = "地区id", dataType = "int", paramType = "query"),
    })
    @PutMapping(value = "/{order_sn}/address")
    public OrderConsigneeVO updateOrderConsignee(@ApiIgnore @PathVariable(name = "order_sn") String orderSn,
                                                 @ApiIgnore String shipName, @ApiIgnore String remark,
                                                 @ApiIgnore String shipAddr, @ApiIgnore String shipMobile,
                                                 @ApiIgnore String shipTel, @ApiIgnore String receiveTime,
                                                 @RegionFormat Region region) {

        OrderConsigneeVO orderConsignee = new OrderConsigneeVO();
        orderConsignee.setOrderSn(orderSn);
        orderConsignee.setShipName(shipName);
        orderConsignee.setRemark(remark);
        orderConsignee.setShipAddr(shipAddr);
        orderConsignee.setShipMobile(shipMobile);
        orderConsignee.setShipTel(shipTel);
        orderConsignee.setReceiveTime(receiveTime);
        orderConsignee.setRegion(region);
        orderConsignee = this.orderOperateManager.updateOrderConsignee(orderConsignee);
        return orderConsignee;
    }


    @ApiOperation(value = "商家修改订单价格", notes = "买家付款前可以修改订单价格")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_sn", value = "订单sn", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "order_price", value = "订单价格", required = true, dataType = "Double", paramType = "query"),
    })
    @PutMapping(value = "/{order_sn}/price")
    public String updateOrderPrice(@ApiIgnore @PathVariable(name = "order_sn") String orderSn,
                                   @ApiIgnore @NotNull(message = "修改后价格不能为空") @DecimalMin(value = "0.01",message = "最低金额为0.01")Double orderPrice) {
        this.orderOperateManager.updateOrderPrice(orderSn, orderPrice);
        return "";
    }


    @ApiOperation(value = "确认收款")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_sn", value = "订单编号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "pay_price", value = "付款金额", dataType = "double", paramType = "query")
    })
    @PostMapping(value = "/{order_sn}/pay")
    public String payOrder(@ApiIgnore @PathVariable("order_sn") String orderSn, @ApiIgnore Double payPrice) {
        this.orderOperateManager.payOrder(orderSn, payPrice, "", OrderPermission.seller);
        return "";
    }


    @ApiOperation(value = "订单流程图数据", notes = "订单流程图数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_sn", value = "订单sn", required = true, dataType = "String", paramType = "path"),
    })
    @GetMapping(value = "/{order_sn}/flow")
    public List<OrderFlowNode> getOrderStatusFlow(@ApiIgnore @PathVariable(name = "order_sn") String orderSn) {
        List<OrderFlowNode> orderFlowList = this.orderQueryManager.getOrderFlow(orderSn);
        return orderFlowList;
    }


    @ApiOperation(value = "导出订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_sn", value = "订单编号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ship_name", value = "收货人", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goods_name", value = "商品名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "buyer_name", value = "买家名字", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start_time", value = "开始时间", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "end_time", value = "结束时间", dataType = "long", paramType = "query"),
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
                                    @ApiIgnore Long startTime, @ApiIgnore Long endTime, @ApiIgnore String orderStatus,
                                    @ApiIgnore String paymentType, @ApiIgnore String clientType) {

        OrderQueryParam param = new OrderQueryParam();
        param.setOrderSn(orderSn);
        param.setShipName(shipName);
        param.setGoodsName(goodsName);
        param.setBuyerName(buyerName);
        param.setOrderStatus(orderStatus);
        param.setStartTime(startTime);
        param.setEndTime(endTime);
        param.setSellerId(UserContext.getSeller().getSellerId());
        param.setPaymentType(paymentType);
        param.setClientType(clientType);
        List<OrderLineVO> lineList = this.orderQueryManager.export(param);
        return lineList;
    }

    @ApiOperation(value = "取消订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_sn", value = "订单编号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "reason", value = "取消原因", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/{order_sn}/cancel")
    public String cancel(@ApiIgnore @PathVariable("order_sn") String orderSn, String reason) {

        CancelVO cancelVO = new CancelVO();
        cancelVO.setOperator("商家取消");
        cancelVO.setOrderSn(orderSn);
        cancelVO.setReason(reason);
        orderOperateManager.cancel(cancelVO, OrderPermission.seller);

        return "";
    }
}
