package com.enation.app.javashop.core.sss;

import com.enation.app.javashop.core.client.statistics.SyncopateTableClient;
import com.enation.app.javashop.core.statistics.util.SyncopateUtil;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;

/**
 * 分表测试
 *
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018-06-22 上午9:14
 */
@Rollback
public class SyncopateTableTest extends BaseTest {

    @Autowired
    private SyncopateTableClient syncopateTableClient;

    @Autowired
    @Qualifier("sssDaoSupport")
    private DaoSupport daoSupport;

    @Test
    public void test() {
        Integer year = LocalDate.now().getYear() ;
        this.daoSupport.execute("DROP TABLE IF EXISTS es_sss_order_data_" + year);
        this.daoSupport.execute("DROP TABLE IF EXISTS es_sss_order_goods_data_" + year);
        this.daoSupport.execute("DROP TABLE IF EXISTS es_sss_refund_data_" + year);
        this.daoSupport.execute("TRUNCATE TABLE `es_sss_order_data`");
        this.daoSupport.execute("TRUNCATE TABLE `es_sss_order_goods_data`");
        this.daoSupport.execute("TRUNCATE TABLE `es_sss_refund_data`");

        this.daoSupport.execute("INSERT INTO `es_sss_order_data` VALUES ('1', '18888888', '123', 'buyer', '132', 'seller', '已付款', '已付款', '99.66', '1', '33', '244', '1403414073'), ('2', '13123213', '1321', 'buy', '133', 'sl', 'l', 'l', '93.00', '1', '3', '1', '1434950073'), ('3', '3', '3', '3', '3', '3', '3', '3', '3.00', '3', '3', '3', '1498108473'), ('4', '3', '3', '3', '3', '3', '3', '3', '3.00', '3', '3', '3', '1561180473');");
        syncopateTableClient.everyDay();

//        Assert.assertEquals(1, this.daoSupport.queryForInt("SELECT count(0) FROM information_schema.TABLES WHERE table_name ='es_sss_order_data_" + year + "'"),0);
//        Assert.assertEquals(1, this.daoSupport.queryForInt("SELECT count(0) FROM information_schema.TABLES WHERE table_name ='es_sss_order_goods_data_" + year + "'"),0);
//        Assert.assertEquals(1, this.daoSupport.queryForInt("SELECT count(0) FROM information_schema.TABLES WHERE table_name ='es_sss_refund_data_" + year + "'"),0);

    }

    @Test
    public void testInit() {
        this.daoSupport.execute("TRUNCATE TABLE `es_sss_order_data`");
        this.daoSupport.execute("TRUNCATE TABLE `es_sss_order_goods_data`");
        this.daoSupport.execute("TRUNCATE TABLE `es_sss_refund_data`");

        this.daoSupport.execute("INSERT INTO `es_sss_order_data` VALUES ('1', '18888888', '123', 'buyer', '132', 'seller', '已付款', '已付款', '99.66', '1', '33', '244', '1403414073'), ('2', '13123213', '1321', 'buy', '133', 'sl', 'l', 'l', '93.00', '1', '3', '1', '1434950073'), ('3', '3', '3', '3', '3', '3', '3', '3', '3.00', '3', '3', '3', '1498108473'), ('4', '3', '3', '3', '3', '3', '3', '3', '3.00', '3', '3', '3', '1561180473');");

        SyncopateUtil.init(this.daoSupport);
//
//        //2015年开始，早于2015，则有数据也不进行分表
//        Assert.assertEquals(0, this.daoSupport.queryForInt("SELECT count(0) FROM information_schema.TABLES WHERE table_name ='es_sss_order_data_" + 2014 + "'"),0);
//
//        //2015/2017年数据进行统计
//        Assert.assertEquals(1, this.daoSupport.queryForInt("SELECT count(0) FROM information_schema.TABLES WHERE table_name ='es_sss_order_data_" + 2015 + "'"),0);
//
//        Assert.assertEquals(1, this.daoSupport.queryForInt("SELECT count(0) FROM information_schema.TABLES WHERE table_name ='es_sss_order_data_" + 2017 + "'"),0);
//
//        //即便有数据，超过当前年份，也不会创建
//        Assert.assertEquals(0, this.daoSupport.queryForInt("SELECT count(0) FROM information_schema.TABLES WHERE table_name ='es_sss_order_data_" + 2019 + "'"),0);

    }




}
