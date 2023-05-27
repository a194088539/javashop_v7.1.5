package com.enation.app.javashop.consumer.shop.distribution;

import com.enation.app.javashop.consumer.job.execute.impl.WithdrawCountJob;
import com.enation.app.javashop.core.distribution.data.DistributionBeforeTest;
import com.enation.app.javashop.core.distribution.model.dos.DistributionDO;
import com.enation.app.javashop.core.distribution.service.DistributionManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;

/**
 * 可提现金额计算
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/23 上午7:46
 */
@Rollback
public class DistributionWithdrawCountJobTest extends BaseTest {


    @Autowired
    @Qualifier("distributionDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private DistributionManager distributionManager;


    @Before
    public void beforeDistribution() {
        DistributionBeforeTest.before(daoSupport);

//        准备定时数据
        this.daoSupport.execute("INSERT INTO `es_distribution_order` VALUES ('5', '2000', 'order_2000', '2', 'test2', '1', null, '1', '1', '10000.00', '1529373768', '100.00', '0.00', null, null, '0', '0.00', '0', '1', 'test',1,1,null), ('6', '2001', 'order_2001', '4', 'test3', '2', '1', '1', '1', '30000.00', null, '300.00', '150.00', '0.00', '0.00', '0', '0.00', '0', '1', 'test',1,1,null)");


    }

    @Autowired
    private WithdrawCountJob withdrawCountJob;

    @Test
    public void everyDay() {
        DistributionDO ddo1 = distributionManager.getDistributor(1);
        DistributionDO ddo2 = distributionManager.getDistributor(2);
        withdrawCountJob.everyDay();


        //校验可提现金额
        DistributionDO afterDdo1 = distributionManager.getDistributor(1);
        DistributionDO afterDdo2 = distributionManager.getDistributor(2);

        Assert.assertEquals(afterDdo1.getCanRebate(), 1117, 1);
        Assert.assertEquals(afterDdo2.getCanRebate(), 1300, 1);

        //再次计算，金额不变
        withdrawCountJob.everyDay();
        afterDdo1 = distributionManager.getDistributor(1);
        afterDdo2 = distributionManager.getDistributor(2);

        Assert.assertEquals(afterDdo1.getCanRebate(), 1117, 1);
        Assert.assertEquals(afterDdo2.getCanRebate(), 1300, 1);


    }
}
