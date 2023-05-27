package com.enation.app.javashop.consumer.shop.sss;

import com.enation.app.javashop.consumer.shop.sss.DataMemberConsumer;
import com.enation.app.javashop.core.base.message.MemberRegisterMsg;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.statistics.model.dto.MemberRegisterData;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;

/**
 * 统计会员注册测试类
 *
 * @author chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/2 上午11:31
 */

@Rollback(true)
public class DataMemberConsumerTest extends BaseTest {


    @Autowired
    @Qualifier("sssDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private DataMemberConsumer dataMemberConsumer;


    @Test
    public void testMemberRegister() throws Exception {


        this.daoSupport.execute("TRUNCATE TABLE es_sss_member_register_data");
        //构造对象
        Member member = new Member();
        member.setCreateTime(144444444L);
        member.setMemberId(9527);
        member.setUname("AMQP member");

        MemberRegisterMsg memberRegisterMsg = new MemberRegisterMsg();
        memberRegisterMsg.setMember(member);
        dataMemberConsumer.memberRegister(memberRegisterMsg);

        MemberRegisterData memberRegisterData = this.daoSupport.queryForObject("select * from es_sss_member_register_data where member_id = 9527", MemberRegisterData.class);
        memberRegisterData.setId(null);
        MemberRegisterData expected = new MemberRegisterData();
        expected.setCreateTime(144444444L);
        expected.setMemberId(9527);
        expected.setMemberName("AMQP member");

        //断言对象是否存在
        Assert.assertEquals(memberRegisterData.toString(), expected.toString());
    }


}
