package com.enation.app.javashop.consumer.job;

import com.enation.app.javashop.consumer.job.execute.impl.GoodsGradeJob;
import com.enation.app.javashop.core.client.member.MemberCommentClient;
import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.member.model.vo.GoodsGrade;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

/**
 * 商品评分计算定时任务
 * @author fk create in 2018年7月19日11:15:26
 * @version v1.0
 * @since v7.0.0
 */
@Transactional(value = "goodsTransactionManager",rollbackFor = Exception.class)
public class GoodsGradeJobTest extends BaseTest {

    @MockBean
    private MemberCommentClient memberCommentClient;

    @Autowired
    private GoodsGradeJob goodsGradeJob;

    @Autowired
    private DaoSupport daoSupport;

    Integer goodsId;

    @Before
    public void testData() {

        GoodsDO goods = new GoodsDO();
        goods.setGoodsName("测试商品");
        goods.setGrade(0d);

        this.daoSupport.insert(goods);
        goodsId = this.daoSupport.getLastId("");
    }

    @Test
    public void testEveryDay() {

        //计算商品评分
        List<GoodsGrade> list = new ArrayList<>();
        GoodsGrade goodsGrade = new GoodsGrade();
        goodsGrade.setGoodsId(goodsId);
        goodsGrade.setGoodRate(1d);

        list.add(goodsGrade);

        when(memberCommentClient.queryGoodsGrade()).thenReturn(list);

        this.goodsGradeJob.everyDay();

        Map map = this.daoSupport.queryForMap("select grade from es_goods where goods_id = ?",goodsId);

        String grade = map.get("grade").toString();
        Assert.assertEquals(grade,"100.00");

    }

    @After
    public void cleanDate() {

    }

}
