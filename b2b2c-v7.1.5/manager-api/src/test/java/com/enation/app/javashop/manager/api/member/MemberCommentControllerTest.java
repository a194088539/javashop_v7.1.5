package com.enation.app.javashop.manager.api.member;

import com.enation.app.javashop.core.member.model.dos.MemberAsk;
import com.enation.app.javashop.core.member.model.dos.MemberComment;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import io.jsonwebtoken.lang.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v1.0
 * @Description: 会员评论单元测试
 * @date 2018/5/22 11:45
 * @since v7.0.0
 */
@Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class MemberCommentControllerTest extends BaseTest {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport daoSupport;

    Integer commentId;

    @Before
    public void insertTestData() throws Exception {
        MemberComment comment = new MemberComment();
        comment.setMemberId(1);
        comment.setSellerId(4);
        comment.setContent("这个好清洗吗？？");
        comment.setReplyStatus(0);
        daoSupport.insert(comment);
        commentId = this.daoSupport.getLastId("");
        comment.setSellerId(3);
        daoSupport.insert(comment);
    }

    /**
     * 查询列表单元测试
     *
     * @throws Exception
     */
    @Test
    public void testQuery() throws Exception {

        mockMvc.perform(get("/admin/members/comments")
                .header("Authorization",superAdmin))
                .andExpect(status().is(200));
    }

    /**
     * 删除一条咨询单元测试
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {

        mockMvc.perform(delete("/admin/members/comments/"+commentId)
                .header("Authorization",superAdmin))
                .andExpect(status().is(200));

        MemberComment comment = this.daoSupport.queryForObject(MemberComment.class, commentId);
        Assert.isTrue(comment.getStatus() == 0);
    }

}
