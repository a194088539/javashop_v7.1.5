package com.enation.app.javashop.buyer.api.aftersale;

import com.enation.app.javashop.core.aftersale.model.dos.RefundDO;
import com.enation.app.javashop.core.aftersale.model.enums.RefundStatusEnum;
import com.enation.app.javashop.core.client.trade.OrderClient;
import com.enation.app.javashop.core.payment.model.dos.PaymentMethodDO;
import com.enation.app.javashop.core.payment.service.PaymentMethodManager;
import com.enation.app.javashop.core.payment.service.RefundManager;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.order.model.enums.PayStatusEnum;
import com.enation.app.javashop.core.trade.order.model.vo.OrderOperateAllowable;
import com.enation.app.javashop.core.trade.sdk.model.OrderDetailDTO;
import com.enation.app.javashop.core.trade.sdk.model.OrderSkuDTO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author zjp
 * @version v7.0
 * @Description 售后相关单元测试
 * @ClassName AfterSaleControllerTest
 * @since v7.0 上午9:58 2018/5/22
 */
@Transactional(value = "tradeTransactionManager",rollbackFor=Exception.class)
public class AfterSaleControllerTest extends BaseTest{

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @MockBean
    private PaymentMethodManager paymentMethodManager;

    @MockBean
    private RefundManager refundManager;

    @MockBean
    private OrderClient orderClient;

    List<MultiValueMap<String, String>> list1= null;
    List<MultiValueMap<String, String>> list2= null;
    List<MultiValueMap<String, String>> list3= null;
    List<MultiValueMap<String, String>> list4= null;

    @Before
    public void insertTestData(){

//        Map map = new HashMap<>();
//        map.put("member_id","1");
//        map.put("sn","1234567");
//        map.put("refund_status", RefundStatusEnum.APPLY.value());
//        map.put("refuse_type",RefuseTypeEnum.RETURN_MONEY.value());
//        map.put("payment_type", PaymentTypeEnum.ONLINE.value());
//        this.daoSupport.insert("es_refund",map);
//
//        map.put("sn","1234561");
////        map.put("refund_status", RefundStatusEnum.PASS.value());
//        this.daoSupport.insert("es_refund",map);
//
//
//
//        String[] names1 = new String[] {"order_sn","sku_id","return_num","refund_reason","message"};
//        String[] values1 = new String[] {"","99","5","不想买了","订单编号必填"};
//        String[] values4 = new String[] {"123456","99","5","","退款原因必填"};
//        list1 = toMultiValueMaps(names1,values1,values4);
//
//        String[] names2 = new String[] {"order_sn","sku_id","return_num","refund_reason"};
//        String[] values6 = new String[] {"123456","99","5","不想买了"};
//        list2 = toMultiValueMaps(names2,values6);
//
//        String[] names3 = new String[] {"order_sn","refund_reason","message"};
//        String[] values7 = new String[] {"","不想买了","订单编号必填"};
//        String[] values8 = new String[] {"12345678","","退款原因必填"};
//        list3 = toMultiValueMaps(names3,values7,values8);
//
//        String[] names4 = new String[] {"order_sn","refund_reason"};
//        String[] values9 = new String[] {"12345678","不想买了"};
//        list4 = toMultiValueMaps(names4,values9);

    }

    /**
     * 买家申请退款
     * @throws Exception
     */
    @Test
    public void testRefund() throws Exception {

//        ErrorMessage error = null;
//        //参数性校验
//        for (MultiValueMap<String, String> params : list1) {
//            String message =  params.get("message").get(0);
//            error  = new ErrorMessage("004",message);
//            mockMvc.perform(post("/after-sales/refunds/apply")
//                    .params(params)
//                    .header("Authorization", buyer1))
//                    .andExpect(status().is(400))
//                    .andExpect(objectEquals( error ));
//        }
//
//        //订单无效性校验
//        error  = new ErrorMessage("E604","订单不存在");
//        when(orderClient.getModel("123456")).thenReturn(null);
//        mockMvc.perform(post("/after-sales/refunds/apply")
//                .params(list2.get(0))
//                .header("Authorization", buyer1))
//                .andExpect(status().is(500))
//                .andExpect(objectEquals( error ));
//        OrderDetailDTO orderDetail = new OrderDetailDTO();
//        orderDetail.setMemberId(2);
//        when(orderClient.getModel("123456")).thenReturn(orderDetail);
//        mockMvc.perform(post("/after-sales/refunds/apply")
//                .params(list2.get(0))
//                .header("Authorization", buyer1))
//                .andExpect(status().is(500))
//                .andExpect(objectEquals( error ));
//
//        //货品无效性校验
//        error  = new ErrorMessage("E602","商品不存在");
//        List<OrderSkuDTO> list = new ArrayList<>();
//        OrderSkuDTO orderSku = new OrderSkuDTO();
//        OrderOperateAllowable orderOperateAllowable = new OrderOperateAllowable();
//        orderSku.setNum(1);
//        orderSku.setSkuId(99);
//        orderSku.setPurchasePrice(100.0);
//        orderSku.setServiceStatus(OrderServiceStatusEnum.NOT_APPLY.name());
//        list.add(orderSku);
//        orderDetail.setMemberId(1);
//        orderDetail.setOrderSkuList(list);
//        orderDetail.getOrderSkuList().get(0).setSkuId(1);
//        orderDetail.getOrderSkuList().get(0).setNum(5);
//        orderOperateAllowable.setAllowApplyService(true);
//        orderDetail.setOrderOperateAllowableVO(orderOperateAllowable);
//        when(orderClient.getModel("123456")).thenReturn(orderDetail);
//        mockMvc.perform(post("/after-sales/refunds/apply")
//                .params(list2.get(0))
//                .header("Authorization", buyer1))
//                .andExpect(status().is(500))
//                .andExpect(objectEquals( error ));
//
//        //退款方式必填校验
//        error  = new ErrorMessage("E605","退款方式必填");
//        orderDetail.getOrderSkuList().get(0).setSkuId(99);
//        orderDetail.setPaymentPluginId("alipayDirectPlugin");
//        when(orderClient.getModel("123456")).thenReturn(orderDetail);
//        PaymentMethodDO paymentMethodDO = new PaymentMethodDO();
//        paymentMethodDO.setIsRetrace(0);
//        when(paymentMethodManager.getByPluginId("alipayDirectPlugin")).thenReturn(paymentMethodDO);
//        mockMvc.perform(post("/after-sales/refunds/apply")
//                .params(list2.get(0))
//                .header("Authorization", buyer1))
//                .andExpect(status().is(500))
//                .andExpect(objectEquals( error ));
//
//        //正确性校验
//        orderDetail.getOrderSkuList().get(0).setPurchasePrice(100.0);
//        orderDetail.getOrderSkuList().get(0).setSubtotal(500.0);
//        orderDetail.getOrderSkuList().get(0).setServiceStatus(OrderServiceStatusEnum.NOT_APPLY.name());
//        orderDetail.setSellerId(1);
//        orderDetail.setTradeSn("123456789");
//        orderDetail.setPayOrderNo("987654321");
//        orderDetail.setPayStatus(PayStatusEnum.PAY_YES.value());
//        orderDetail.setOrderStatus(OrderStatusEnum.PAID_OFF.name());
//        orderDetail.setServiceStatus(OrderServiceStatusEnum.NOT_APPLY.name());
//
//
//        when(orderClient.getModel("123456")).thenReturn(orderDetail);
//        paymentMethodDO.setIsRetrace(1);
//        when(paymentMethodManager.getByPluginId("alipayDirectPlugin")).thenReturn(paymentMethodDO);
//        String json = mockMvc.perform(post("/after-sales/refunds/apply")
//                    .params(list2.get(0))
//                    .header("Authorization", buyer1))
//                    .andExpect(status().is(200))
//                    .andReturn().getResponse().getContentAsString();
//        BuyerRefundApplyVO buyerRefundApply = JsonUtil.jsonToObject(json, BuyerRefundApplyVO.class);
//        RefundDO refund = new RefundDO();
//        refund.setOrderSn("123456");
//        refund.setRefundPrice(500.0);
////        refund.setRefundReason("不想买了");
////        refund.setRefundType(RefundTypeEnum.AFTER_SALE.value());
//        refund.setMemberId(1);
//        refund.setSellerId(1);
////        refund.setTradeSn("123456789");
//        refund.setPayOrderNo("987654321");
//        refund.setRefundStatus(RefundStatusEnum.APPLY.value());
////        refund.setRefuseType(RefuseTypeEnum.RETURN_MONEY.value());
//        String sql = "select order_sn,refund_price,refund_reason,refund_type,member_id,seller_id,trade_sn,pay_order_no,refund_status,refuse_type from es_refund where sn = ? ";
//        RefundDO refundDO = this.daoSupport.queryForObject(sql, RefundDO.class, buyerRefundApply.getRefundSn());
//        Assert.assertEquals(refund,refundDO);
    }

    /**
     * 买家申请退货
     * @throws Exception
     */
    @Test
    public void testReturnGoods() throws Exception {
//        ErrorMessage error = null;
//        //参数性校验
//        for (MultiValueMap<String, String> params : list1) {
//            String message =  params.get("message").get(0);
//            error  = new ErrorMessage("004",message);
//            mockMvc.perform(post("/after-sales/return-goods/apply")
//                    .params(params)
//                    .header("Authorization", buyer1))
//                    .andExpect(status().is(400))
//                    .andExpect(objectEquals( error ));
//        }
//
//        //订单无效性校验
//        error  = new ErrorMessage("E604","订单不存在");
//        when(orderClient.getModel("123456")).thenReturn(null);
//        mockMvc.perform(post("/after-sales/return-goods/apply")
//                .params(list2.get(0))
//                .header("Authorization", buyer1))
//                .andExpect(status().is(500))
//                .andExpect(objectEquals( error ));
//        error  = new ErrorMessage("E604","订单不存在");
//        OrderDetailDTO orderDetail = new OrderDetailDTO();
//        orderDetail.setMemberId(2);
//        when(orderClient.getModel("123456")).thenReturn(orderDetail);
//        mockMvc.perform(post("/after-sales/return-goods/apply")
//                .params(list2.get(0))
//                .header("Authorization", buyer1))
//                .andExpect(status().is(500))
//                .andExpect(objectEquals( error ));
//
//        //申请售后商品数量有效性校验
//        error  = new ErrorMessage("E607","申请售后货品数量不能大于购买数量");
//        orderDetail.setMemberId(1);
//        List<OrderSkuDTO> list = new ArrayList<>();
//        OrderSkuDTO orderSku = new OrderSkuDTO();
//        OrderOperateAllowable orderOperateAllowable = new OrderOperateAllowable();
//        orderOperateAllowable.setAllowApplyService(true);
//        orderSku.setNum(1);
//        orderSku.setSkuId(99);
//        orderSku.setPurchasePrice(100.0);
//        orderSku.setServiceStatus(OrderServiceStatusEnum.NOT_APPLY.name());
//        list.add(orderSku);
//        orderDetail.setOrderSkuList(list);
//        orderDetail.setOrderOperateAllowableVO(orderOperateAllowable);
//        when(orderClient.getModel("123456")).thenReturn(orderDetail);
//        mockMvc.perform(post("/after-sales/return-goods/apply")
//                .params(list2.get(0))
//                .header("Authorization", buyer1))
//                .andExpect(status().is(500))
//                .andExpect(objectEquals( error ));
//
//        //货品无效性校验
//        error  = new ErrorMessage("E602","商品不存在");
//        orderDetail.getOrderSkuList().get(0).setSkuId(1);
//        orderDetail.getOrderSkuList().get(0).setNum(10);
//        when(orderClient.getModel("123456")).thenReturn(orderDetail);
//        mockMvc.perform(post("/after-sales/return-goods/apply")
//                .params(list2.get(0))
//                .header("Authorization", buyer1))
//                .andExpect(status().is(500))
//                .andExpect(objectEquals( error ));
//
//        //退款方式必填校验
//        error  = new ErrorMessage("E605","退款方式必填");
//        orderDetail.getOrderSkuList().get(0).setSkuId(99);
//        orderDetail.setPaymentPluginId("alipayDirectPlugin");
//        when(orderClient.getModel("123456")).thenReturn(orderDetail);
//        PaymentMethodDO paymentMethodDO = new PaymentMethodDO();
//        paymentMethodDO.setIsRetrace(0);
//        when(paymentMethodManager.getByPluginId("alipayDirectPlugin")).thenReturn(paymentMethodDO);
//        mockMvc.perform(post("/after-sales/return-goods/apply")
//                .params(list2.get(0))
//                .header("Authorization", buyer1))
//                .andExpect(status().is(500))
//                .andExpect(objectEquals( error ));
//
//        //正确性校验
//        orderDetail.getOrderSkuList().get(0).setPurchasePrice(100.0);
//        orderDetail.getOrderSkuList().get(0).setSubtotal(1000.0);
//        orderDetail.getOrderSkuList().get(0).setServiceStatus(OrderServiceStatusEnum.NOT_APPLY.name());
//        orderDetail.setSellerId(1);
//        orderDetail.setTradeSn("123456789");
//        orderDetail.setPayOrderNo("987654321");
//        orderDetail.setPayStatus(PayStatusEnum.PAY_YES.value());
//        orderDetail.setOrderStatus(OrderStatusEnum.PAID_OFF.name());
//        when(orderClient.getModel("123456")).thenReturn(orderDetail);
//        paymentMethodDO.setIsRetrace(1);
//        when(paymentMethodManager.getByPluginId("alipayDirectPlugin")).thenReturn(paymentMethodDO);
//        String json = mockMvc.perform(post("/after-sales/return-goods/apply")
//                .params(list2.get(0))
//                .header("Authorization", buyer1))
//                .andExpect(status().is(200))
//                .andReturn().getResponse().getContentAsString();
//        BuyerRefundApplyVO buyerRefundApply = JsonUtil.jsonToObject(json, BuyerRefundApplyVO.class);
//        RefundDO refund = new RefundDO();
//        refund.setOrderSn("123456");
//        refund.setRefundPrice(500.0);
////        refund.setRefundReason("不想买了");
////        refund.setRefundType(RefundTypeEnum.AFTER_SALE.value());
//        refund.setMemberId(1);
//        refund.setSellerId(1);
////        refund.setTradeSn("123456789");
//        refund.setPayOrderNo("987654321");
//        refund.setRefundStatus(RefundStatusEnum.APPLY.value());
////        refund.setRefuseType(RefuseTypeEnum.RETURN_GOODS.value());
//        String sql = "select order_sn,refund_price,refund_reason,refund_type,member_id,seller_id,trade_sn,pay_order_no,refund_status,refuse_type from es_refund where sn = ? ";
//        RefundDO refundDO = this.daoSupport.queryForObject(sql, RefundDO.class, buyerRefundApply.getRefundSn());
//        Assert.assertEquals(refund,refundDO);
    }



    /**
     * 买家取消已付款的订单
     * @throws Exception
     */
    @Test
    public void testCancleOrder() throws Exception {
        ErrorMessage error = null;

        //参数性校验
        for (MultiValueMap<String, String> params : list3) {
            String message =  params.get("message").get(0);
            error  = new ErrorMessage("004",message);
            mockMvc.perform(post("/after-sales/refunds/cancel-order")
                    .params(params)
                    .header("Authorization", buyer1))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals( error ));
        }

        //订单无效性校验
        error  = new ErrorMessage("E604","订单不存在");
        when(orderClient.getModel("12345678")).thenReturn(null);
        mockMvc.perform(post("/after-sales/refunds/cancel-order")
                .params(list4.get(0))
                .header("Authorization", buyer1))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));
        OrderDetailDTO orderDetail = new OrderDetailDTO();
        orderDetail.setMemberId(2);
        when(orderClient.getModel("12345678")).thenReturn(orderDetail);
        mockMvc.perform(post("/after-sales/refunds/cancel-order")
                .params(list4.get(0))
                .header("Authorization", buyer1))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));

        //订单操作权限校验
        error  = new ErrorMessage("E601","操作不允许");
        orderDetail.setMemberId(1);
        orderDetail.setOrderStatus(OrderStatusEnum.AFTER_SERVICE.value());
        orderDetail.setPayStatus(PayStatusEnum.PAY_NO.value());
        when(orderClient.getModel("12345678")).thenReturn(orderDetail);
        mockMvc.perform(post("/after-sales/refunds/cancel-order")
                .params(list4.get(0))
                .header("Authorization", buyer1))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));

        //退款方式必填校验
        error  = new ErrorMessage("E605","退款方式必填");
        orderDetail.setOrderStatus(OrderStatusEnum.PAID_OFF.value());
        orderDetail.setPayStatus(PayStatusEnum.PAY_YES.value());
        orderDetail.setPaymentPluginId("alipayDirectPlugin");
        when(orderClient.getModel("12345678")).thenReturn(orderDetail);
        PaymentMethodDO paymentMethodDO = new PaymentMethodDO();
        paymentMethodDO.setIsRetrace(0);
        when(paymentMethodManager.getByPluginId("alipayDirectPlugin")).thenReturn(paymentMethodDO);
        mockMvc.perform(post("/after-sales/refunds/cancel-order")
                .params(list4.get(0))
                .header("Authorization", buyer1))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));
        //正确性校验
        orderDetail.setTradeSn("111111");
        orderDetail.setOrderPrice(100.0);
        List<OrderSkuDTO> orderSkuList = new ArrayList<>();
        OrderSkuDTO orderSkuDTO = new OrderSkuDTO();
        orderSkuDTO.setPurchasePrice(100.0);
        orderSkuDTO.setNum(1);
        orderSkuList.add(orderSkuDTO);
        orderDetail.setOrderSkuList(orderSkuList);
        Map map = new HashMap();
        map.put("result","true");
        when(refundManager.originRefund("111111","12345678",100.0)).thenReturn(map);
        paymentMethodDO.setIsRetrace(1);
        when(paymentMethodManager.getByPluginId("alipayDirectPlugin")).thenReturn(paymentMethodDO);
        mockMvc.perform(post("/after-sales/refunds/cancel-order")
                .params(list4.get(0))
                .header("Authorization", buyer1))
                .andExpect(status().is(200));
    }
}
