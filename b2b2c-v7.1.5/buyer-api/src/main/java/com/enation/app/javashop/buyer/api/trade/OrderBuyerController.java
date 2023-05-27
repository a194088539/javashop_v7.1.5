package com.enation.app.javashop.buyer.api.trade;

import com.enation.app.javashop.core.client.member.MemberHistoryReceiptClient;
import com.enation.app.javashop.core.trade.TradeErrorCode;
import com.enation.app.javashop.core.trade.cart.model.dos.OrderPermission;
import com.enation.app.javashop.core.trade.order.model.dos.OrderLogDO;
import com.enation.app.javashop.core.trade.order.model.dos.TradeDO;
import com.enation.app.javashop.core.trade.order.model.dto.OrderDetailQueryParam;
import com.enation.app.javashop.core.trade.order.model.dto.OrderQueryParam;
import com.enation.app.javashop.core.trade.order.model.enums.OrderTagEnum;
import com.enation.app.javashop.core.trade.order.model.enums.PaymentTypeEnum;
import com.enation.app.javashop.core.trade.order.model.vo.*;
import com.enation.app.javashop.core.trade.order.service.OrderLogManager;
import com.enation.app.javashop.core.trade.order.service.OrderOperateManager;
import com.enation.app.javashop.core.trade.order.service.OrderQueryManager;
import com.enation.app.javashop.core.trade.order.service.TradeQueryManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.util.StringUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 会员订单相关控制器
 *
 * @author Snow create in 2018/5/14
 * @version v2.0
 * @since v7.0.0
 */

@Api(description = "会员订单API")
@RestController
@RequestMapping("/trade/orders")
@Validated
public class OrderBuyerController {

    @Autowired
    private OrderQueryManager orderQueryManager;

    @Autowired
    private OrderOperateManager orderOperateManager;

    @Autowired
    private TradeQueryManager tradeQueryManager;

    @Autowired
    private MemberHistoryReceiptClient memberHistoryReceiptClient;

    @Autowired
    private OrderLogManager orderLogManager;


    @ApiOperation(value = "查询会员订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goods_name", value = "商品名称关键字", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "key_words", value = "关键字", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "order_status", value = "订单状态", dataType = "String", paramType = "query",
                    allowableValues = "ALL,WAIT_PAY,WAIT_SHIP,WAIT_ROG,CANCELLED,COMPLETE,WAIT_COMMENT,REFUND",
                    example = "ALL:所有订单,WAIT_PAY:待付款,WAIT_SHIP:待发货,WAIT_ROG:待收货," +
                            "CANCELLED:已取消,COMPLETE:已完成,WAIT_COMMENT:待评论,WAIT_CHASE:待追评，REFUND:售后中"),
            @ApiImplicitParam(name = "page_no", value = "页数", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "条数", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "start_time", value = "开始时间", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "end_time", value = "结束时间", dataType = "long", paramType = "query")
    })
    @GetMapping()
    public Page<OrderLineVO> list(@ApiIgnore String keyWords,@ApiIgnore String goodsName, @ApiIgnore String orderStatus,
                                  @ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize, @ApiIgnore Long startTime, @ApiIgnore Long endTime) {

        Page page = null;
        try {
            try {
                if (StringUtil.isEmpty(orderStatus)) {
                    orderStatus = "ALL";
                }
                OrderTagEnum.valueOf(orderStatus);
            } catch (Exception e) {
                throw new ServiceException(TradeErrorCode.E455.code(), "订单状态参数错误");
            }

            Buyer buyer = UserContext.getBuyer();
            OrderQueryParam param = new OrderQueryParam();
            param.setGoodsName(goodsName);
            param.setTag(orderStatus);
            param.setMemberId(buyer.getUid());
            param.setKeywords(keyWords);
            param.setPageNo(pageNo);
            param.setPageSize(pageSize);
            param.setStartTime(startTime);
            param.setEndTime(endTime);

            page = this.orderQueryManager.list(param);

            //货到付款的订单不允许线上支付
            List<OrderLineVO> list = page.getData();
            for(OrderLineVO order : list){
                if(PaymentTypeEnum.COD.value().equals(order.getPaymentType())){
                    order.getOrderOperateAllowableVO().setAllowPay(false);
                }
            }
            page.setData(list);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        return page;
    }


    @ApiOperation(value = "查询单个订单明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_sn", value = "订单编号", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping(value = "/{order_sn}")
    public OrderDetailVO get(@ApiIgnore @PathVariable("order_sn") String orderSn) {
        Buyer buyer = UserContext.getBuyer();
        OrderDetailQueryParam queryParam = new OrderDetailQueryParam();
        queryParam.setBuyerId(buyer.getUid());
        OrderDetailVO detailVO = this.orderQueryManager.getModel(orderSn, queryParam);

        if (detailVO.getNeedReceipt().intValue() == 1) {
            detailVO.setReceiptHistory(memberHistoryReceiptClient.getReceiptHistory(orderSn));
        }

        //货到付款的订单不允许线上支付
        if(PaymentTypeEnum.COD.value().equals(detailVO.getPaymentType())){
            detailVO.getOrderOperateAllowableVO().setAllowPay(false);
        }

        return detailVO;
    }


    @ApiOperation(value = "确认收货")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_sn", value = "订单编号", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping(value = "/{order_sn}/rog")
    public String rog(@ApiIgnore @PathVariable("order_sn") String orderSn) {

        Buyer buyer = UserContext.getBuyer();
        RogVO rogVO = new RogVO();
        rogVO.setOrderSn(orderSn);
        rogVO.setOperator(buyer.getUsername());

        orderOperateManager.rog(rogVO, OrderPermission.buyer);
        return "";
    }


    @ApiOperation(value = "取消订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_sn", value = "订单编号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "reason", value = "取消原因", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping(value = "/{order_sn}/cancel")
    public String cancel(@ApiIgnore @PathVariable("order_sn") String orderSn, String reason) {

        Buyer buyer = UserContext.getBuyer();
        CancelVO cancelVO = new CancelVO();
        cancelVO.setOperator(buyer.getUsername());
        cancelVO.setOrderSn(orderSn);
        cancelVO.setReason(reason);
        orderOperateManager.cancel(cancelVO, OrderPermission.buyer);


        return "";
    }


    @ApiOperation(value = "查询订单状态的数量")
    @GetMapping(value = "/status-num")
    public OrderStatusNumVO getStatusNum() {
        Buyer buyer = UserContext.getBuyer();
        OrderStatusNumVO orderStatusNumVO = this.orderQueryManager.getOrderStatusNum(buyer.getUid(), null);
        return orderStatusNumVO;
    }


    @ApiOperation(value = "根据交易编号查询订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "trade_sn", value = "交易编号", required = true, dataType = "String", paramType = "path"),
    })
    @GetMapping(value = "/{trade_sn}/list")
    public List<OrderDetailVO> getOrderList(@ApiIgnore @PathVariable("trade_sn") String tradeSn) {
        Buyer buyer = UserContext.getBuyer();
        List<OrderDetailVO> orderDetailVOList = this.orderQueryManager.getOrderByTradeSn(tradeSn, buyer.getUid());
        return orderDetailVOList;
    }


    @ApiOperation(value = "根据交易编号或者订单编号查询收银台数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "trade_sn", value = "交易编号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "order_sn", value = "订单编号", dataType = "String", paramType = "query")
    })
    @GetMapping(value = "/cashier")
    public CashierVO getCashier(@ApiIgnore String tradeSn, @ApiIgnore String orderSn) {

        String shipName, shipAddr, shipMobile, shipTel, shipProvince, shipCity, shipCounty, shipTown, payTypeText;
        Double needPayPrice;
        Integer memberId;

        if (tradeSn != null) {

            TradeDO tradeDO = this.tradeQueryManager.getModel(tradeSn);
            shipName = tradeDO.getConsigneeName();
            shipAddr = tradeDO.getConsigneeAddress();
            shipMobile = tradeDO.getConsigneeMobile();
            shipTel = tradeDO.getConsigneeTelephone();
            shipProvince = tradeDO.getConsigneeProvince();
            shipCity = tradeDO.getConsigneeCity();
            shipCounty = tradeDO.getConsigneeCounty();
            shipTown = tradeDO.getConsigneeTown();
            needPayPrice = tradeDO.getTotalPrice();
            payTypeText = tradeDO.getPaymentType();
            memberId = tradeDO.getMemberId();

        } else if (orderSn != null) {

            OrderDetailQueryParam queryParam = new OrderDetailQueryParam();
            queryParam.setBuyerId(UserContext.getBuyer().getUid());

            OrderDetailVO detailVO = this.orderQueryManager.getModel(orderSn, queryParam);
            shipName = detailVO.getShipName();
            shipAddr = detailVO.getShipAddr();
            shipMobile = detailVO.getShipMobile();
            shipTel = detailVO.getShipTel();
            shipProvince = detailVO.getShipProvince();
            shipCity = detailVO.getShipCity();
            shipCounty = detailVO.getShipCounty();
            shipTown = detailVO.getShipTown();
            needPayPrice = detailVO.getNeedPayMoney();
            payTypeText = detailVO.getPaymentType();
            memberId = detailVO.getMemberId();

        } else {
            throw new ServiceException(TradeErrorCode.E455.code(), "参数错误");
        }
        //判断查询的订单是否属于当前登录用户  add by liuyulei 2019-11-05
        Boolean isOwner = UserContext.getBuyer().getUid().equals(memberId);

        CashierVO cashierVO = new CashierVO();
        cashierVO.setShipProvince(shipProvince);
        cashierVO.setShipCity(shipCity);
        cashierVO.setShipCounty(shipCounty);
        cashierVO.setShipTown(shipTown);
        cashierVO.setShipAddr(shipAddr);
        cashierVO.setShipMobile(shipMobile);
        cashierVO.setShipName(shipName);
        cashierVO.setNeedPayPrice(needPayPrice);
        cashierVO.setShipTel(shipTel);
        cashierVO.setPayTypeText(payTypeText);
        cashierVO.setIsOwner(isOwner);
        return cashierVO;
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
