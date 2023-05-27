package com.enation.app.javashop.consumer.shop.distribution;

import com.enation.app.javashop.consumer.core.event.AfterSaleChangeEvent;
import com.enation.app.javashop.core.aftersale.model.dos.RefundDO;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceStatusEnum;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceTypeEnum;
import com.enation.app.javashop.core.aftersale.model.vo.ApplyAfterSaleVO;
import com.enation.app.javashop.core.aftersale.service.AfterSaleQueryManager;
import com.enation.app.javashop.core.aftersale.service.AfterSaleRefundManager;
import com.enation.app.javashop.core.base.message.AfterSaleChangeMessage;
import com.enation.app.javashop.core.client.distribution.DistributionOrderClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 分销订单退款
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-11-25
 */

@Component
public class DistributionRefundConsumer implements AfterSaleChangeEvent {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private DistributionOrderClient distributionOrderClient;

    @Autowired
    private AfterSaleQueryManager afterSaleQueryManager;

    @Autowired
    private AfterSaleRefundManager afterSaleRefundManager;

    @Override
    public void afterSaleChange(AfterSaleChangeMessage afterSaleChangeMessage) {

        //如果售后服务单状态为已完成 并且 售后服务单类型为退货或者取消订单
        boolean flag = ServiceStatusEnum.COMPLETED.equals(afterSaleChangeMessage.getServiceStatus()) &&
                (ServiceTypeEnum.RETURN_GOODS.equals(afterSaleChangeMessage.getServiceType()) || ServiceTypeEnum.ORDER_CANCEL.equals(afterSaleChangeMessage.getServiceType()));

        if (flag) {
            //获取售后服务单详细信息
            ApplyAfterSaleVO applyAfterSaleVO = this.afterSaleQueryManager.detail(afterSaleChangeMessage.getServiceSn());

            //获取售后服务退款单信息
            RefundDO refundDO = this.afterSaleRefundManager.getModel(afterSaleChangeMessage.getServiceSn());

            // 售后服务完成时算好各个级别需要退的返利金额 放入数据库
            this.distributionOrderClient.calReturnCommission(applyAfterSaleVO.getOrderSn(), refundDO.getActualPrice());
        }

    }
}
