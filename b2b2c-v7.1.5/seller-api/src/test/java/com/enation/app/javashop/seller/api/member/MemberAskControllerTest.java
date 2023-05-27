package com.enation.app.javashop.seller.api.member;

import com.enation.app.javashop.core.member.model.dos.MemberAsk;
import com.enation.app.javashop.core.member.model.enums.CommonStatusEnum;
import com.enation.app.javashop.core.member.service.MemberAskManager;
import com.enation.app.javashop.core.system.enums.DeleteStatusEnum;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v1.0
 * @Description: 会员咨询单元测试
 * @date 2018/5/22 10:18
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
        ask.setStatus(DeleteStatusEnum.NORMAL.value());
        daoSupport.insert(ask);

        askId = this.daoSupport.getLastId("");
    }

    /**
     * 咨询回复单元测试
     * @throws Exception
     */
    @Test
    public void testReply() throws Exception {

        //回复内容为空
        ErrorMessage error  = new ErrorMessage("004","请输入回复内容");
        mockMvc.perform(put("/seller/members/asks/"+askId+"/reply")
                .header("Authorization", seller1))
                .andExpect(status().is(400))
                .andExpect(  objectEquals( error ));
        //无权限回复的咨询
        error  = new ErrorMessage("200","无权回复");
        mockMvc.perform(put("/seller/members/asks/"+askId+"/reply")
                .header("Authorization", seller1)
                .param("reply_content","这个很喜欢"))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));
        //正确回复
        mockMvc.perform(put("/seller/members/asks/"+askId+"/reply")
                .header("Authorization", seller2)
                .param("reply_content","这个很喜欢"))
                .andExpect(status().is(200));
        //不可重复回复
        error  = new ErrorMessage("202","不可重复回复");
        mockMvc.perform(put("/seller/members/asks/"+askId+"/reply")
                .header("Authorization", seller2)
                .param("reply_content","这个很喜欢"))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));
    }

    /**
     * 查询咨询列表单元测试
     * @throws Exception
     */
    @Test
    public void testQuery() throws Exception{

        mockMvc.perform(get("/seller/members/asks")
                .header("Authorization", seller2))
                .andExpect(status().is(200));


    }

}
