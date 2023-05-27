package com.enation.app.javashop.core.orderbill.service.impl;

import com.enation.app.javashop.core.orderbill.model.vo.BillResult;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.orderbill.model.dos.BillItem;
import com.enation.app.javashop.core.orderbill.service.BillItemManager;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 结算单项表业务类
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-04-26 15:39:57
 */
@Service
public class BillItemManagerImpl implements BillItemManager {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Override
    public Page list(int page, int pageSize, Integer billId, String billType) {

        String sql = "select * from es_bill_item where bill_id = ? and  item_type = ?";
        Page webPage = this.daoSupport.queryForPage(sql, page, pageSize,BillItem.class, billId, billType );

        return webPage;
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public BillItem add(BillItem billItem) {
        this.daoSupport.insert(billItem);

        return billItem;
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public BillItem edit(BillItem billItem, Integer id) {
        this.daoSupport.update(billItem, id);
        return billItem;
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {
        this.daoSupport.delete(BillItem.class, id);
    }

    @Override
    public BillItem getModel(Integer id) {
        return this.daoSupport.queryForObject(BillItem.class, id);
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateBillItem(Integer sellerId, Integer billId,String startTime, String lastTime) {

        Assert.notNull(sellerId, "卖家id为null");
        Assert.notNull(billId, "结算单id为null");

        String sql = "UPDATE es_bill_item SET bill_id = ? WHERE seller_id = ? AND bill_id IS NULL AND (add_time < ? and add_time>=?)";
        this.daoSupport.execute(sql, billId, sellerId, lastTime,startTime);

    }

    @Override
    public Map<Integer, BillResult> countBillResultMap(String startTime,String lastTime) {
        String sql = "select " +
                " sum(case when payment_type = 'online' and item_type = 'PAYMENT'  then price else 0 end ) online_price ," +
                " sum(case when payment_type = 'online' and item_type = 'REFUND' then price else 0 end ) online_refund_price ," +
                " sum(case when payment_type = 'cod' and item_type = 'PAYMENT' then price else 0 end ) cod_price," +
                " sum(case when payment_type = 'cod' and item_type = 'REFUND' then price else 0 end ) cod_refund_price,seller_id," +
                " sum(case when item_type = 'PAYMENT' then site_coupon_price*coupon_commission else 0 end ) as site_coupon_commi " +
                " from es_bill_item where `status` = 0 and (add_time < ? and add_time >= ?) and bill_id is null group by seller_id ";

        List<BillResult> billList = this.daoSupport.queryForList(sql, BillResult.class, lastTime,startTime);
        Map<Integer, BillResult> billMap = new HashMap<>(billList.size());
        if (StringUtil.isNotEmpty(billList)) {
            for (BillResult bill : billList) {
                billMap.put(bill.getSellerId(), bill);
            }
        }

        return billMap;
    }
}
