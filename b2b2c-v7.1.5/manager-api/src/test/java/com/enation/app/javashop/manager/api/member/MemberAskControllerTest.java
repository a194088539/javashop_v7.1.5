package com.enation.app.javashop.manager.api.member;

import com.enation.app.javashop.core.member.model.dos.MemberAsk;
import com.enation.app.javashop.core.member.model.enums.CommonStatusEnum;
import com.enation.app.javashop.core.system.enums.DeleteStatusEnum;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import io.jsonwebtoken.lang.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.AssertTrue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v1.0
 * @Description: 会员咨询单元测试
 * @date 2018/5/22 11:45
 * @since v7.0.0
 */
@Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class MemberAskControllerTest extends BaseTest {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport daoSupport;

    Integer askId;

    @Before
    public void insertTestData() throws Exception {
        MemberAsk ask = new MemberAsk();
        ask.setMemberId(1);
        ask.setSellerId(4);
        ask.setContent("这个好清洗吗？？");
        ask.setReplyStatus(CommonStatusEnum.NO.value());
        daoSupport.insert(ask);
        askId = this.daoSupport.getLastId("");
        ask.setSellerId(3);
        daoSupport.insert(ask);
    }

    /**
     * 查询列表单元测试
     *
     * @throws Exception
     */
    @Test
    public void testQuery() throws Exception {

        mockMvc.perform(get("/admin/members/asks")
                .header("Authorization",superAdmin))
                .andExpect(status().is(200));
    }

    /**
     * 删除一条咨询单元测试
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {

        mockMvc.perform(delete("/admin/members/asks/"+askId)
                .header("Authorization",superAdmin))
                .andExpect(status().is(200));

        MemberAsk ask = this.daoSupport.queryForObject(MemberAsk.class, askId);
        Assert.isTrue(DeleteStatusEnum.NORMAL.value().equals(ask.getStatus()));
    }

}
