package com.enation.app.javashop.consumer.shop.aftersale;

import com.enation.app.javashop.consumer.core.event.AfterSaleChangeEvent;
import com.enation.app.javashop.core.aftersale.model.dos.AfterSaleGoodsDO;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceStatusEnum;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceTypeEnum;
import com.enation.app.javashop.core.aftersale.model.vo.ApplyAfterSaleVO;
import com.enation.app.javashop.core.aftersale.service.AfterSaleQueryManager;
import com.enation.app.javashop.core.base.message.AfterSaleChangeMessage;
import com.enation.app.javashop.core.trade.order.model.dto.OrderDetailQueryParam;
import com.enation.app.javashop.core.trade.order.model.enums.OrderServiceStatusEnum;
import com.enation.app.javashop.core.trade.order.model.vo.OrderDetailVO;
import com.enation.app.javashop.core.trade.order.model.vo.OrderSkuVO;
import com.enation.app.javashop.core.trade.order.service.OrderOperateManager;
import com.enation.app.javashop.core.trade.order.service.OrderQueryManager;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 售后服务单关闭
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-12-05
 */
@Component
public class AfterSaleCloseConsumer implements AfterSaleChangeEvent {

    @Autowired
    private AfterSaleQueryManager afterSaleQueryManager;

    @Autowired
    private OrderQueryManager orderQueryManager;

    @Autowired
    private OrderOperateManager orderOperateManager;

    @Override
    public void afterSaleChange(AfterSaleChangeMessage afterSaleChangeMessage) {
        //获取售后服务单详细信息
        ApplyAfterSaleVO applyAfterSaleVO = this.afterSaleQueryManager.detail(afterSaleChangeMessage.getServiceSn());
        //获取售后服务单状态
        ServiceStatusEnum serviceStatus = afterSaleChangeMessage.getServiceStatus();
        //获取售后服务类型
        ServiceTypeEnum serviceType = afterSaleChangeMessage.getServiceType();

        boolean flag = (ServiceTypeEnum.CHANGE_GOODS.equals(serviceType) || ServiceTypeEnum.SUPPLY_AGAIN_GOODS.equals(serviceType)) && ServiceStatusEnum.CLOSED.equals(serviceStatus);

        //目前只有售后服务类型为换货或补发商品才允许关闭
        if (flag) {

            //获取申请售后的商品集合
            List<AfterSaleGoodsDO> goodsList = applyAfterSaleVO.getGoodsList();

            //获取申请售后的商品信息（除取消订单之外，其它类型售后服务与申请售后服务的商品都是一对一的关系，因此这里直接取商品集合的第一个值即可）
            AfterSaleGoodsDO goodsDO = goodsList.get(0);

            OrderDetailVO order = orderQueryManager.getModel(applyAfterSaleVO.getOrderSn(), new OrderDetailQueryParam());

            List<OrderSkuVO> skuList = order.getOrderSkuList();

            for(OrderSkuVO sku :skuList){
                //判断订单商品集合中的商品skuID是否和申请售后的商品skuID相同，如果相同，则恢复售后状态为未申请
                if (sku.getSkuId().intValue() == goodsDO.getSkuId().intValue()) {
                    sku.setServiceStatus(OrderServiceStatusEnum.NOT_APPLY.value());
                }
            }

            //更改订单的item_json数据
            this.orderOperateManager.updateItemJson(JsonUtil.objectToJson(skuList), applyAfterSaleVO.getOrderSn());
        }
    }
}
