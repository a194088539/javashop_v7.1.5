package com.enation.app.javashop.manager.api.orderbill;

import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fk
 * @version v1.0
 * @Description: 结算单单元测试
 * @date 2018/4/28 16:21
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager",rollbackFor=Exception.class)
public class OrderBillCreateTest extends BaseTest{

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    /**
     * 准备结算数据，不回滚执行后，调用每月定时任务，就会生成相关结算单
     */
    @Test
    public void create(){
        //取上个月的时间
        Long[] time = DateUtil.getLastMonth();
        String sql = "update es_bill_item set add_time = ? ,bill_id = null ";
        this.daoSupport.execute(sql,time[0]);
    }

}
