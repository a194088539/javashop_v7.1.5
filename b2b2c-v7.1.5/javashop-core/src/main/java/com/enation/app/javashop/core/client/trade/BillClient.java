package com.enation.app.javashop.core.client.trade;

import com.enation.app.javashop.core.orderbill.model.dos.BillItem;

/**
 * @author fk
 * @version v2.0
 * @Description: 结算单对外接口
 * @date 2018/7/26 11:21
 * @since v7.0.0
 */
public interface BillClient {

    /**
     * 生成结算单
     * @param startTime
     * @param endTime
     */
    void createBills(Long startTime,Long endTime);

    /**
     * 添加结算单项表
     * @param billItem 结算单项表
     * @return BillItem 结算单项表
     */
    BillItem add(BillItem billItem);


}
