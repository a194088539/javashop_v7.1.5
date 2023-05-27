package com.enation.app.javashop.consumer.shop.trade.consumer;

import com.enation.app.javashop.consumer.core.event.AfterSaleChangeEvent;
import com.enation.app.javashop.core.aftersale.model.dos.AfterSaleGoodsDO;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceTypeEnum;
import com.enation.app.javashop.core.aftersale.model.vo.ApplyAfterSaleVO;
import com.enation.app.javashop.core.aftersale.service.AfterSaleQueryManager;
import com.enation.app.javashop.core.base.message.AfterSaleChangeMessage;
import com.enation.app.javashop.core.promotion.pintuan.service.PintuanOrderManager;
import com.enation.app.javashop.core.trade.cart.model.dos.OrderPermission;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceStatusEnum;
import com.enation.app.javashop.core.trade.order.model.dto.OrderDetailQueryParam;
import com.enation.app.javashop.core.trade.order.model.enums.OrderServiceStatusEnum;
import com.enation.app.javashop.core.trade.order.model.vo.CancelVO;
import com.enation.app.javashop.core.trade.order.model.vo.OrderDetailVO;
import com.enation.app.javashop.core.trade.order.model.vo.OrderSkuVO;
import com.enation.app.javashop.core.trade.order.service.OrderOperateManager;
import com.enation.app.javashop.core.trade.order.service.OrderQueryManager;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author fk
 * @version v2.0
 * @Description: 订单取消后审核消费者
 * @date 2018/9/415:18
 * @since v7.0.0
 */
@Component
public class OrderCancelAuthConsumer implements AfterSaleChangeEvent {

    @Autowired
    private OrderOperateManager orderOperateManager;

    @Autowired
    private PintuanOrderManager pintuanOrderManager;

    @Autowired
    private OrderQueryManager orderQueryManager;

    @Autowired
    private AfterSaleQueryManager afterSaleQueryManager;

    @Override
    public void afterSaleChange(AfterSaleChangeMessage afterSaleChangeMessage) {

        //获取售后服务单详细信息
        ApplyAfterSaleVO applyAfterSaleVO = this.afterSaleQueryManager.detail(afterSaleChangeMessage.getServiceSn());
        //获取售后服务单状态
        ServiceStatusEnum serviceStatus = afterSaleChangeMessage.getServiceStatus();
        //获取售后服务类型
        ServiceTypeEnum serviceType = afterSaleChangeMessage.getServiceType();

        //获取申请售后服务的订单编号
        String orderSn = applyAfterSaleVO.getOrderSn();

        //如果售后服务类型为取消订单并且商家已经审核通过
        if (ServiceTypeEnum.ORDER_CANCEL.equals(serviceType)) {

            //如果售后服务申请已经被商家审核通过
            if (ServiceStatusEnum.COMPLETED.equals(serviceStatus)) {

                //更改订单的状态为已取消
                CancelVO cancelVO = new CancelVO();
                cancelVO.setOperator(applyAfterSaleVO.getMemberName());
                cancelVO.setOrderSn(orderSn);
                cancelVO.setReason(applyAfterSaleVO.getReason());
                orderOperateManager.cancel(cancelVO, OrderPermission.client);

                //判断该订单是否是拼团订单
                if (pintuanOrderManager.getMainOrderBySn(orderSn) != null){
                    pintuanOrderManager.cancelOrder(orderSn);
                }
            }

            //如果售后服务申请已经被商家审核拒绝
            if (ServiceStatusEnum.REFUSE.equals(serviceStatus)) {
                //更改订单的售后服务状态为已失效（取消订单申请如果商家审核未通过，则不允许再次取消订单）
                this.orderOperateManager.updateServiceStatus(orderSn, OrderServiceStatusEnum.EXPIRED);
            }

        } else {
            //如果售后服务申请已经被商家审核拒绝
            if (ServiceStatusEnum.REFUSE.equals(serviceStatus)) {

                //获取申请售后的商品集合
                List<AfterSaleGoodsDO> goodsList = applyAfterSaleVO.getGoodsList();

                //获取申请售后的商品信息（除取消订单之外，其它类型售后服务与申请售后服务的商品都是一对一的关系，因此这里直接取商品集合的第一个值即可）
                AfterSaleGoodsDO goodsDO = goodsList.get(0);

                OrderDetailVO order = orderQueryManager.getModel(orderSn, new OrderDetailQueryParam());

                List<OrderSkuVO> skuList = order.getOrderSkuList();

                for(OrderSkuVO sku :skuList){
                    //判断订单商品集合中的商品skuID是否和申请售后的商品skuID相同，如果相同，则恢复售后状态为未申请
                    if (sku.getSkuId().intValue() == goodsDO.getSkuId().intValue()) {
                        sku.setServiceStatus(OrderServiceStatusEnum.NOT_APPLY.value());
                    }
                }

                //更改订单的item_json数据
                this.orderOperateManager.updateItemJson(JsonUtil.objectToJson(skuList), orderSn);
            }
        }
    }
}
