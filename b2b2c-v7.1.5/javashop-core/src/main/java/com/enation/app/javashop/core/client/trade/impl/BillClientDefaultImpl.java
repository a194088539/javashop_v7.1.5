package com.enation.app.javashop.core.client.trade.impl;

import com.enation.app.javashop.core.client.trade.BillClient;
import com.enation.app.javashop.core.orderbill.model.dos.BillItem;
import com.enation.app.javashop.core.orderbill.service.BillItemManager;
import com.enation.app.javashop.core.orderbill.service.BillManager;
import com.enation.app.javashop.framework.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author fk
 * @version v1.0
 * @Description: 结算单对外接口实现
 * @date 2018/7/26 11:22
 * @since v7.0.0
 */
@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class BillClientDefaultImpl implements BillClient {

    @Autowired
    private BillManager billManager;

    @Autowired
    private BillItemManager billItemManager;

    @Override
    public void createBills(Long startTime,Long endTime) {

        this.billManager.createBills(startTime,endTime);
    }

    @Override
    public BillItem add(BillItem billItem) {

        return this.billItemManager.add(billItem);
    }


    public static void main(String[] args) {
        Long[] time = DateUtil.getLastMonth();
        System.out.println(time[0]);
        System.out.println(time[1]);
    }

}
