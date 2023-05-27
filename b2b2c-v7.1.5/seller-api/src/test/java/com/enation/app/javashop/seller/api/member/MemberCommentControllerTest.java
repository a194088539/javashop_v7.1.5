package com.enation.app.javashop.seller.api.member;

import com.enation.app.javashop.core.goods.service.GoodsQueryManager;
import com.enation.app.javashop.core.member.constraint.annotation.GradeType;
import com.enation.app.javashop.core.member.model.dos.MemberComment;
import com.enation.app.javashop.core.member.model.dto.CommentDTO;
import com.enation.app.javashop.core.member.model.enums.CommentGrade;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v1.0
 * @Description: 会员评论单元测试
 * @date 2018/5/22 14:28
 * @since v7.0.0
 */
@Transactional(value = "memberTransactionManager",propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class MemberCommentControllerTest extends BaseTest {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport daoSupport;

    Integer commentId;

    @Before
    public void insertTestData(){

        MemberComment memberComment = new MemberComment();
        memberComment.setContent("此评论默认好评");
        memberComment.setGrade("good");
        memberComment.setReplyStatus(0);
        memberComment.setSellerId(3);
        this.daoSupport.insert(memberComment);
        commentId = this.daoSupport.getLastId("");

    }

    /**
     * 添加评论
     */
    @Test
    public void testReply() throws Exception{

        //回复内容为空
        ErrorMessage error  = new ErrorMessage("004","回复内容不能为空");
        mockMvc.perform(post("/seller/members/comments/"+commentId+"/reply")
                .header("Authorization", seller1))
                .andExpect(status().is(400))
                .andExpect(  objectEquals( error ));
        //无权限回复的评论
        error  = new ErrorMessage("200","无权限回复");
        mockMvc.perform(post("/seller/members/comments/"+commentId+"/reply")
                .header("Authorization", seller2)
                .param("reply","这个很喜欢"))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));
        //正确回复
        mockMvc.perform(post("/seller/members/comments/"+commentId+"/reply")
                .header("Authorization", seller1)
                .param("reply","这个很喜欢"))
                .andExpect(status().is(200));
        //不可重复回复
        error  = new ErrorMessage("200","不能重复回复");
        mockMvc.perform(post("/seller/members/comments/"+commentId+"/reply")
                .header("Authorization", seller1)
                .param("reply","这个很喜欢"))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));
    }

    /**
     * 查询我的评论列表单元测试
     * @throws Exception
     */
    @Test
    public void testQuery() throws Exception {

        // 正确查询
        mockMvc.perform(get("/seller/members/comments")
                .header("Authorization",seller2))
                .andExpect(status().is(200));

    }

}
