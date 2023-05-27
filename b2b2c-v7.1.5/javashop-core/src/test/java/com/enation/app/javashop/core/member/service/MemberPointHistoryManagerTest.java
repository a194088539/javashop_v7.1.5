package com.enation.app.javashop.core.member.service;

import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.dos.MemberPointHistory;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.StringUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

/**
 * 历史积分测试类
 *
 * @author zh
 * @version v7.0
 * @date 18/5/29 下午9:52
 * @since v7.0
 */
@Transactional(value = "memberTransactionManager", rollbackFor = Exception.class)
public class MemberPointHistoryManagerTest extends BaseTest {

    @Autowired
    private MemberManager memberManager;
    @Autowired
    private MemberPointManager memberPointManager;

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    Integer memberId = 0;

    @Before
    public void dataPreparation() throws Exception{
        this.memberDaoSupport.execute("insert into es_member (uname,password,disabled,have_shop,email,mobile,grade_point,consum_point) values(?,?,?,?,?,?,?,?)", "haobeck", StringUtil.md5(StringUtil.md5("123123") + "haobeck"), "0", "0", "310487699@qq.com", "13233653048","0","0");
        memberId = memberDaoSupport.getLastId("es_member");

    }

    /**
     * 积分操作测试方法
     *
     * @throws Exception
     */
    @Test
    public void pointOperationTest() throws Exception {
        //正确测试 增加消费积分 增加等级积分
        MemberPointHistory memberPointHistory = new MemberPointHistory();
        memberPointHistory.setConsumPointType(1);
        memberPointHistory.setConsumPoint(100L);
        memberPointHistory.setGradePointType(1);
        memberPointHistory.setGradePoint(100);
        memberPointHistory.setMemberId(memberId);
        memberPointManager.pointOperation(memberPointHistory);
        Member mb = memberManager.getModel(memberId);
        Assert.assertEquals(mb.getGradePoint().toString(), "100");
        Assert.assertEquals(mb.getConsumPoint().toString(), "100");
        //正确测试 增加消费积分，减少等级积分
        memberPointHistory = new MemberPointHistory();
        memberPointHistory.setConsumPointType(1);
        memberPointHistory.setConsumPoint(100L);
        memberPointHistory.setGradePointType(0);
        memberPointHistory.setGradePoint(150);
        memberPointHistory.setMemberId(memberId);
        memberPointManager.pointOperation(memberPointHistory);
        mb = memberManager.getModel(memberId);
        Assert.assertEquals(mb.getGradePoint().toString(), "0");
        Assert.assertEquals(mb.getConsumPoint().toString(), "200");
        //正确测试 增加消费积分，减少等级积分
        memberPointHistory = new MemberPointHistory();
        memberPointHistory.setConsumPointType(0);
        memberPointHistory.setConsumPoint(500L);
        memberPointHistory.setGradePointType(1);
        memberPointHistory.setGradePoint(300);
        memberPointHistory.setMemberId(memberId);
        memberPointManager.pointOperation(memberPointHistory);
        mb = memberManager.getModel(memberId);
        Assert.assertEquals(mb.getGradePoint().toString(), "300");
        Assert.assertEquals(mb.getConsumPoint().toString(), "0");
        //消费积分类型错误校验
        memberPointHistory = new MemberPointHistory();
        memberPointHistory.setConsumPointType(123);
        memberPointHistory.setConsumPoint(1L);
        thrown.expect(ServiceException.class);
        thrown.expectMessage("消费积分类型不正确");
        memberPointManager.pointOperation(memberPointHistory);
        //消费积分为负积分校验
        memberPointHistory.setConsumPointType(123);
        memberPointHistory.setConsumPoint(-1L);
        thrown.expect(ServiceException.class);
        thrown.expectMessage("消费积分不正确");
        memberPointManager.pointOperation(memberPointHistory);
        //等级积分类型不正确校验
        memberPointHistory = new MemberPointHistory();
        memberPointHistory.setConsumPointType(1);
        memberPointHistory.setConsumPoint(1L);
        memberPointHistory.setGradePointType(123);
        memberPointHistory.setGradePoint(1);
        thrown.expect(ServiceException.class);
        thrown.expectMessage("等级积分类型不正确");
        memberPointManager.pointOperation(memberPointHistory);
        //等级积分不正确校验
        memberPointHistory = new MemberPointHistory();
        memberPointHistory.setConsumPointType(1);
        memberPointHistory.setConsumPoint(1L);
        memberPointHistory.setGradePointType(123);
        memberPointHistory.setGradePoint(-1);
        thrown.expect(ServiceException.class);
        thrown.expectMessage("等级积分不正确");
        memberPointManager.pointOperation(memberPointHistory);

    }


}
