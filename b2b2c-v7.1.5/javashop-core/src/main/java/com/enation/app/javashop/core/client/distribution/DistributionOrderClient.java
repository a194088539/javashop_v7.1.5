package com.enation.app.javashop.core.client.distribution;

import com.enation.app.javashop.core.distribution.model.dos.DistributionOrderDO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;


/**
 * 分销Order client
 *
 * @author Chopper
 * @version v1.0
 * @since v6.1
 * 2016年10月2日 下午5:24:14
 */
public interface DistributionOrderClient {

    /**
     * 根据sn获得分销商订单详情
     *
     * @param orderSn 订单编号
     * @return FxOrderDO
     */
    DistributionOrderDO getModelByOrderSn(String orderSn);
    /**
     * 保存一条数据
     *
     * @param distributionOrderDO
     * @return
     */
    DistributionOrderDO add(DistributionOrderDO distributionOrderDO);

    /**
     * 通过订单id，计算出各个级别的返利金额并保存到数据库
     *
     * @param orderSn 订单编号
     * @return 计算结果 true 成功， false 失败
     */
    boolean calCommission(String orderSn);

    /**
     * 根据购买人增加上级人员订单数量
     *
     * @param buyMemberId 购买人会员id
     */
    void addOrderNum(int buyMemberId);


    /**
     * 计算退款时需要退的返利金额
     * @param orderSn
     * @param price
     * @return
     */
    boolean calReturnCommission(String orderSn, double price);


    /**
     * 结算订单
     * @param order
     */
    void confirm(OrderDO order);

}
