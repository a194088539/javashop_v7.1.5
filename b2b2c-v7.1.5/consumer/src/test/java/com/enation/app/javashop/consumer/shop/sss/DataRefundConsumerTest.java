package com.enation.app.javashop.consumer.shop.sss;

import com.enation.app.javashop.core.aftersale.model.dos.RefundDO;
import com.enation.app.javashop.core.statistics.model.dto.RefundData;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;

/**
 * 统计退货数据测试类
 *
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018/5/2 上午11:30
 */
@Rollback(true)
public class DataRefundConsumerTest extends BaseTest {

    @Autowired
    @Qualifier("sssDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private DataRefundConsumer dataRefundConsumer;


    @Before
    public void before() {
        this.daoSupport.execute("TRUNCATE TABLE es_sss_refund_data");
    }

    @Test
    public void testRefund() throws Exception {
        // 准备工作
        RefundDO refundDO = new RefundDO();
        refundDO.setCreateTime(14444444L);
        refundDO.setOrderSn("131321321321");
        refundDO.setRefundPrice(99.99);
        refundDO.setSellerId(123);
        refundDO.setSn("131321321321");

//        RefundChangeMsg refundChangeMsg = new RefundChangeMsg(refundDO, RefundStatusEnum.PASS);
//        dataRefundConsumer.refund(refundChangeMsg);
        
        RefundData expected = new RefundData();
        expected.setCreateTime(14444444L);
        expected.setOrderSn("131321321321");
        expected.setRefundSn("17575757");
        expected.setRefundPrice(99.99);
        expected.setSellerId(123);
        expected.setRefundSn("131321321321");

        RefundData actual = this.daoSupport.queryForObject(RefundData.class, 1);
        Assert.assertEquals(expected.toString(), actual.toString());


    }


}
