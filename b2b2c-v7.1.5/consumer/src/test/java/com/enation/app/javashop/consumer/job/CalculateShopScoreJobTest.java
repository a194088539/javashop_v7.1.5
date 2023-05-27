package com.enation.app.javashop.consumer.job;

import com.enation.app.javashop.consumer.job.execute.impl.CalculateShopScoreJob;
import com.enation.app.javashop.core.member.model.dos.MemberShopScore;
import com.enation.app.javashop.core.member.model.dto.MemberShopScoreDTO;
import com.enation.app.javashop.core.member.service.MemberShopScoreManager;
import com.enation.app.javashop.core.shop.model.dos.ShopDetailDO;
import com.enation.app.javashop.core.shop.model.enums.ShopStatusEnum;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
/**
 *
 * 店铺评分测试用例
 * @author zhangjiping
 * @version v7.0.0
 * @since v7.0.0
 * 2018年4月9日 下午3:01:22
 */
@Transactional(value = "memberTransactionManager",rollbackFor=Exception.class)
public class CalculateShopScoreJobTest extends BaseTest {
    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport daoSupport ;

    @Autowired
    private CalculateShopScoreJob calculateShopScoreJob;

    @MockBean
    private MemberShopScoreManager memberShopScoreManager;

    @Before
    public void insertTestData() {
        this.daoSupport.execute("delete from es_shop_detail");

        Map map = new HashMap();
        map.put("shop_id",100);
        this.daoSupport.insert("es_shop_detail",map);
    }

    @Test
    public void testEveryDay() throws InterruptedException {
        Thread.sleep(2*1000);
        MemberShopScoreDTO memberShopScore = new MemberShopScoreDTO();
        memberShopScore.setSellerId(100);
        memberShopScore.setDescriptionScore(5.0);
        memberShopScore.setServiceScore(4.0);
        memberShopScore.setDeliveryScore(3.0);
        List<MemberShopScoreDTO> shopScoreList = new ArrayList<>();
        shopScoreList.add(memberShopScore);
        when(memberShopScoreManager.queryEveryShopScore()).thenReturn(shopScoreList);
        this.calculateShopScoreJob.everyDay();
        String sql = "select * from es_shop_detail where shop_id = 100 ";
        ShopDetailDO shopDetailDO = daoSupport.queryForObject(sql, ShopDetailDO.class);
        Assert.assertEquals("5.0",shopDetailDO.getShopDescriptionCredit().toString());
        Assert.assertEquals("4.0",shopDetailDO.getShopServiceCredit().toString());
        Assert.assertEquals("3.0",shopDetailDO.getShopDeliveryCredit().toString());

    }
}
