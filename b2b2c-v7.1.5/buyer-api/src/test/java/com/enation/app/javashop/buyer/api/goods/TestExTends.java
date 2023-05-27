package com.enation.app.javashop.buyer.api.goods;

import com.enation.app.javashop.buyer.api.goods.model.TestA;
import com.enation.app.javashop.buyer.api.goods.model.TestB;
import com.enation.app.javashop.buyer.api.goods.model.TestC;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description: 测试继承关系类获取表名问题
 * @author: liuyulei
 * @create: 2019-12-12 14:33
 * @version:1.0
 * @since:7.1.4
 **/
@Transactional(value = "goodsTransactionManager", rollbackFor = Exception.class)
public class TestExTends extends BaseTest {

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport daoSupport;

    private static Integer id ;


    @Before
    public void insertTestData(){
        this.daoSupport.execute("CREATE TABLE `es_test` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,\n" +
                "  `sort` int(10) DEFAULT NULL,\n" +
                "  `mark` varchar(255) COLLATE utf8_bin DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;");
        TestA testA = new TestA();
        testA.setName("test1");
        this.daoSupport.insert(testA);
        id = this.daoSupport.getLastId("");
    }

    @Test
    public void testAdd() throws Exception {
        //测试修改
        TestB testB = new TestB();
        testB.setSort(1);
        testB.setName("update1");
        testB.setId(id);
        this.daoSupport.update(testB,id);
        TestB b = this.daoSupport.queryForObject(TestB.class,id);
        Assert.assertEquals(b.toString(), testB.toString());

        //测试多层子类修改
        TestC testC = new TestC();
        testC.setMark("mark");
        testC.setName("mark");
        testC.setSort(2);
        testC.setId(id);
        this.daoSupport.update(testC,id);


        TestC c = this.daoSupport.queryForObject(TestC.class,id);
        Assert.assertEquals(testC.toString(), c.toString());
        //测试删除
        this.daoSupport.delete(TestC.class,id);


        //测试子类添加
        testC.setMark("AAAAA");
        this.daoSupport.insert(testC);
        id = this.daoSupport.getLastId("");
        testC.setId(id);

        c = this.daoSupport.queryForObject(TestC.class,id);
        Assert.assertEquals(testC.toString(), c.toString());

    }

    @After
    public void testAfter(){
        this.daoSupport.execute("DROP table es_test;");
    }
}
