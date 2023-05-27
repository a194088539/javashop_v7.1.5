package com.enation.app.javashop.consumer.shop.aftersale;

import com.enation.app.javashop.consumer.core.event.AfterSaleChangeEvent;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceStatusEnum;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceTypeEnum;
import com.enation.app.javashop.core.aftersale.service.SellerCreateTradeManager;
import com.enation.app.javashop.core.base.message.AfterSaleChangeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 系统自动为用户创建新订单
 * 现阶段针对的是商家审核通过换货和补发商品售后服务申请，系统自动为用户创建新的订单
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-23
 */
@Component
public class CreateNewOrderConsumer implements AfterSaleChangeEvent {

    @Autowired
    private SellerCreateTradeManager sellerCreateTradeManager;

    @Override
    public void afterSaleChange(AfterSaleChangeMessage afterSaleChangeMessage) {

        //如果售后服务单状态为审核通过 并且 售后服务单类型为换货或者补发商品
        boolean flag = ServiceStatusEnum.PASS.equals(afterSaleChangeMessage.getServiceStatus()) &&
                (ServiceTypeEnum.CHANGE_GOODS.equals(afterSaleChangeMessage.getServiceType()) || ServiceTypeEnum.SUPPLY_AGAIN_GOODS.equals(afterSaleChangeMessage.getServiceType()));

        if (flag) {
            //系统自动生成新订单
            this.sellerCreateTradeManager.systemCreateTrade(afterSaleChangeMessage.getServiceSn());
        }

    }
}
