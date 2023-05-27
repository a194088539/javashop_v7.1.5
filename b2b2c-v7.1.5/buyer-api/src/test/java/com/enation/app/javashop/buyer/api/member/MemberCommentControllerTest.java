package com.enation.app.javashop.buyer.api.member;

import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.dto.CommentDTO;
import com.enation.app.javashop.core.member.model.enums.CommentGrade;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.core.trade.order.model.vo.OrderOperateAllowable;
import com.enation.app.javashop.core.client.trade.OrderClient;
import com.enation.app.javashop.core.trade.sdk.model.OrderDetailDTO;
import com.enation.app.javashop.core.trade.sdk.model.OrderSkuDTO;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    List<MultiValueMap<String, String>> list = null;

    List<CommentDTO> comments;

    @MockBean
    private OrderClient orderClient;
    @MockBean
    private MemberManager memberManager;

    @Before
    public void insertTestData(){

        String[] names = new String[]{"order_sn","delivery_score","description_score","service_score","message"};
        String[] values1 = new String[]{"","1","1","1","订单编号不能为空"};
        String[] values2 = new String[]{"123456","","1","1","请选择发货速度评分"};
        String[] values3 = new String[]{"123456","6","1","1","发货速度评分有误"};
        String[] values5 = new String[]{"123456","5","","1","请选择描述相符度评分"};
        String[] values6 = new String[]{"123456","5","6","1","描述相符度评分有误"};
        String[] values7 = new String[]{"123456","5","5","","请选择描述服务评分"};
        String[] values8 = new String[]{"123456","5","5","6","服务评分有误"};
        String[] values9 = new String[]{"123456","5","5","5","商品评论不能为空"};
        list = toMultiValueMaps(names,values1,values2,values3,values5,values6,values7,values8,values9);

        comments = new ArrayList<>();
        CommentDTO comment = new CommentDTO();
        comment.setSkuId(1);
        comment.setGrade(CommentGrade.good.name());
        comments.add(comment);

    }

    /**
     * 添加评论
     */
    @Test
    public void testInsert() throws Exception{

        // 必填验证和不正确的值
        for (MultiValueMap<String,String> params  : list){
            String message =  params.get("message").get(0);
            ErrorMessage error  = new ErrorMessage("004",message);
            Map map = new HashMap<>();
            for (String key : params.keySet()){
                map.put(key,params.get(key).get(0));
                if(!"商品评论不能为空".equals(message)){
                    map.put("comments",comments);
                }
            }
            mockMvc.perform(post("/members/comments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.objectToJson(map))
                    .header("Authorization", buyer1))
                    .andExpect(status().is(400))
                    .andExpect(  objectEquals( error ));
        }

        //商品相关评论值不正确
        MultiValueMap<String,String> params = list.get(list.size()-1);
        Map map = new HashMap<>();
        for (String key : params.keySet()){
            map.put(key,params.get(key).get(0));
        }
        comments = new ArrayList<>();
        CommentDTO comment = new CommentDTO();
        comment.setGrade("123");
        comment.setSkuId(1);
        comments.add(comment);
        map.put("comments",comments);
        ErrorMessage error  = new ErrorMessage("004","商品评分不正确");
        mockMvc.perform(post("/members/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(map))
                .header("Authorization", buyer1))
                .andExpect(status().is(400))
                .andExpect(  objectEquals( error ));

        comments = new ArrayList<>();
        comment = new CommentDTO();
        comment.setGrade(CommentGrade.good.name());
        comments.add(comment);
        map.put("comments",comments);
        error  = new ErrorMessage("004","评论的商品不能为空");
        mockMvc.perform(post("/members/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(map))
                .header("Authorization", buyer1))
                .andExpect(status().is(400))
                .andExpect(  objectEquals( error ));

        //不存在或者不是我的订单
        comments = new ArrayList<>();
        comment = new CommentDTO();
        comment.setGrade(CommentGrade.good.name());
        comment.setSkuId(1);
        comments.add(comment);
        map.put("comments",comments);
        error  = new ErrorMessage("200","没有权限");
        mockMvc.perform(post("/members/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(map))
                .header("Authorization", buyer1))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));
        //订单中不存在的商品
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setSn("123456");
        List<OrderSkuDTO> list = new ArrayList<>();
        OrderSkuDTO orderSkuDTO = new OrderSkuDTO();
        orderSkuDTO.setSkuId(2);
        list.add(orderSkuDTO);
        orderDetailDTO.setOrderSkuList(list);
        orderDetailDTO.setMemberId(1);
        OrderOperateAllowable allowableVO = new OrderOperateAllowable();
        allowableVO.setAllowComment(true);
        orderDetailDTO.setOrderOperateAllowableVO(allowableVO);
        when(orderClient.getModel("123456")).thenReturn(orderDetailDTO);

        mockMvc.perform(post("/members/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(map))
                .header("Authorization", buyer1))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));

        //正确添加
        list = new ArrayList<>();
        orderSkuDTO.setSkuId(1);
        list.add(orderSkuDTO);
        when(orderClient.getModel("123456")).thenReturn(orderDetailDTO);
        Member member = new Member();
        member.setMemberId(1);
        member.setFace("http://a.jpg");
        when(memberManager.getModel(1)).thenReturn(member);
        mockMvc.perform(post("/members/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(map))
                .header("Authorization", buyer1))
                .andExpect(status().is(200));

        // 评论后不能再次评论
        allowableVO.setAllowComment(false);
        orderDetailDTO.setOrderOperateAllowableVO(allowableVO);
        when(orderClient.getModel("123456")).thenReturn(orderDetailDTO);
        mockMvc.perform(post("/members/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(map))
                .header("Authorization", buyer1))
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
        mockMvc.perform(get("/members/comments")
                .header("Authorization",buyer1))
                .andExpect(status().is(200));

    }

}
