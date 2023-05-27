package com.enation.app.javashop.manager.api.statistics;

import com.enation.app.javashop.framework.database.DaoSupport;

/**
 * AnalysisTest
 *
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018-04-25 下午2:12
 */
public class StatisticBeforeTest {

    public static void before(DaoSupport daoSupport) {
        //清空已有数据
        daoSupport.execute("delete from es_sss_goods_data");
        daoSupport.execute("delete from es_sss_member_register_data");
        daoSupport.execute("delete from es_sss_order_data");
        daoSupport.execute("delete from es_sss_order_goods_data");
        daoSupport.execute("delete from es_sss_refund_data");
        daoSupport.execute("delete from es_sss_shop_data");

        //构造商品数据
        daoSupport.execute("insert into es_sss_goods_data values(1,1,'测试商品1',1,1,'|0|1|3|',1,1,99,99,1)");
        daoSupport.execute("insert into es_sss_goods_data values(2,2,'测试商品2',2,2,'|0|1|2|',2,2,199,199,1)");
        daoSupport.execute("insert into es_sss_goods_data values(3,3,'测试商品3',2,2,'|0|1|2|',1,2,123,1221,1)");

        //构造会员数据
        daoSupport.execute("insert into es_sss_member_register_data values(1,1,'测试会员','1521963051')");
        daoSupport.execute("insert into es_sss_member_register_data values(2,2,'测试会员2','1521703851')");
        daoSupport.execute("insert into es_sss_member_register_data values(3,3,'测试会员3','1521703851')");
        daoSupport.execute("insert into es_sss_member_register_data values(4,4,'测试会员4','1524382251')");
        daoSupport.execute("insert into es_sss_member_register_data values(5,5,'测试会员5','1523518251')");


        //构造订单数据
        daoSupport.execute("insert into es_sss_order_data values(1,'141421421421',1,'测试用户1',1,'测试店铺1','COMPLETE','PAY_YES',298,2,1,1,'1521963051')");
        daoSupport.execute("insert into es_sss_order_data values(2,'1414214214212',2,'测试用户2',1,'测试店铺1','COMPLETE','PAY_YES',246,2,1,1,'1521963051')");
        daoSupport.execute("insert into es_sss_order_data values(3,'1414214214213',2,'测试用户2',2,'测试店铺2','COMPLETE','PAY_YES',123,1,2,1,'1523518251')");


        daoSupport.execute("insert into es_sss_order_goods_data values(1,'141421421421',1,'测试商品1',1,99,99,'|0|1|3|',1,'1521963051',1)");
        daoSupport.execute("insert into es_sss_order_goods_data values(2,'141421421421',2,'测试商品2',1,199,199,'|0|1|2|',1,'1521963051',1)");
        daoSupport.execute("insert into es_sss_order_goods_data values(3,'1414214214212',3,'测试商品3',2,123,246,'|0|1|2|',1,'1523518251',1)");
        daoSupport.execute("insert into es_sss_order_goods_data values(4,'1414214214213',4,'测试商品4',1,123,123,'|0|1|2|',1,'1523518251',1)");

        //构造退款数据
        daoSupport.execute("insert into es_sss_refund_data values(1,1,'1321312312','141421421421',88,'1521963051')");
        daoSupport.execute("insert into es_sss_refund_data values(2,2,'1321312313','141421421421',12,'1521963051')");
        daoSupport.execute("insert into es_sss_refund_data values(3,2,'1523518251','1414214214213',33,'1523518251')");

        //构造店铺数据
        daoSupport.execute("insert into es_sss_shop_data values(1,1,'测试店铺',123,1)");
        daoSupport.execute("insert into es_sss_shop_data values(2,2,'测试店铺2',123,1)");


    }

}
