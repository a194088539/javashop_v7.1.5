package com.enation.app.javashop.consumer.shop.distribution;

import com.enation.app.javashop.core.base.message.MemberRegisterMsg;
import com.enation.app.javashop.core.distribution.data.DistributionBeforeTest;
import com.enation.app.javashop.core.distribution.model.dos.DistributionDO;
import com.enation.app.javashop.core.distribution.service.DistributionManager;
import com.enation.app.javashop.core.distribution.service.ShortUrlManager;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;

/**
 * 注册后添加分销商
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/6/13 下午11:33
 */
@Rollback
public class DistributionRegisterConsumerTest extends BaseTest {


    @Autowired
    private Cache cache;


    @Autowired
    private DistributionManager distributionManager;

    @Autowired
    @Qualifier("distributionDaoSupport")
    private DaoSupport daoSupport;


    @Autowired
    private DistributionRegisterConsumer distributionRegisterConsumer;


    @Before
    public void beforeDistribution() {
        DistributionBeforeTest.before(daoSupport);
    }




    @Test
    public void memberRegister() throws Exception {
        Member member = new Member();
        member.setUname("test123");
        member.setMemberId(123);
        MemberRegisterMsg memberRegisterMsg = new MemberRegisterMsg();
        memberRegisterMsg.setMember(member);
        String uuid="uuid_uuid";
        memberRegisterMsg.setUuid(uuid);
        cache.put(ShortUrlManager.PREFIX+uuid, "1");

        distributionRegisterConsumer.memberRegister(memberRegisterMsg);
        DistributionDO ddo = distributionManager.getDistributorByMemberId(123);

        DistributionDO distributionDO = new DistributionDO();
        distributionDO.setMemberName("test123");
        distributionDO.setMemberId(123);
        distributionDO.setPath("|0|1|123|");
        distributionDO.setMemberIdLv1(1);
        distributionDO.setCurrentTplId(1);
        distributionDO.setCurrentTplName("模版1");

        Assert.assertEquals(ddo.toString(), distributionDO.toString());

    }
    @Test
    public void memberRegister1() throws Exception {
        Member member = new Member();
        member.setUname("test123");
        member.setMemberId(123);
        MemberRegisterMsg memberRegisterMsg = new MemberRegisterMsg();
        memberRegisterMsg.setMember(member);
        String uuid="uuid_uuid";
        memberRegisterMsg.setUuid(uuid);
        cache.put(ShortUrlManager.PREFIX+uuid, "3");

        distributionRegisterConsumer.memberRegister(memberRegisterMsg);
        DistributionDO ddo = distributionManager.getDistributorByMemberId(123);

        DistributionDO distributionDO = new DistributionDO();
        distributionDO.setMemberName("test123");
        distributionDO.setMemberId(123);
        distributionDO.setPath("|0|1|2|3|123|");
        distributionDO.setMemberIdLv1(3);
        distributionDO.setMemberIdLv2(2);
        distributionDO.setCurrentTplId(1);
        distributionDO.setCurrentTplName("模版1");

        Assert.assertEquals(ddo.toString(), distributionDO.toString());

    }
}
