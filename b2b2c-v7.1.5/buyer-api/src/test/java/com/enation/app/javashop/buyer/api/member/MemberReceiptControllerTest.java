package com.enation.app.javashop.buyer.api.member;

import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.dos.MemberReceipt;
import com.enation.app.javashop.core.member.model.enums.ReceiptTypeEnum;
import com.enation.app.javashop.core.member.model.vo.MemberVO;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.core.member.service.MemberReceiptManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 发票测试类
 *
 * @author zh
 * @version v7.0
 * @date 18/5/26 下午4:20
 * @since v7.0
 */
@Transactional(value = "memberTransactionManager", rollbackFor = Exception.class)
public class MemberReceiptControllerTest extends BaseTest {
    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;
    @Autowired
    private MemberReceiptManager memberReceiptManager;

    /**
     * 会员信息
     */
    private Map tokenMap = null;

    /**
     * 测试参数集合
     */
    private List<MultiValueMap<String, String>> list = null;
    /**
     * 发票id
     */
    private Integer id;


    /**
     * 会员数据准备
     */
    @Before
    public void dataPreparation() throws Exception {
        this.memberDaoSupport.execute("delete from es_member  where uname = 'haobeck' or mobile = '13233653048'");
        this.register("haobeck", StringUtil.md5("123123"),"13233653048");
        tokenMap = this.loginForMap("haobeck", StringUtil.md5("123123"));

    }

    /**
     * 添加增值税普通发票测试
     *
     * @throws Exception
     */
    @Test
    public void addTest() throws Exception {
        //参数校验
        String[] names = new String[]{"receipt_title", "receipt_content", "tax_no", "message"};
        String[] values1 = new String[]{"", "明细", "123123", "发票抬头不能为空"};
        String[] values2 = new String[]{"北京科技有限公司", "", "123123", "发票内容不能为空"};
        String[] values3 = new String[]{"北京科技有限公司", "明细", "", "发票税号不能为空"};

        list = toMultiValueMaps(names, values1, values2, values3);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/members/receipt/ordinary")
                    .params(params)
                    .header("Authorization", tokenMap.get("access_token")))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //会员是否存在校验
        ErrorMessage error = new ErrorMessage("003", "当前会员不存在");
        mockMvc.perform(post("/members/receipt/ordinary")
                .param("receipt_title", "北京科技有限公司")
                .param("receipt_content", "明细")
                .param("tax_no", "1123123123")
                .header("Authorization", seller1))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));
        //正确校验
        for (int i = 0; i < 10; i++) {
            String json = mockMvc.perform(post("/members/receipt/ordinary")
                    .param("receipt_title", "北京科技有限公司")
                    .param("receipt_content", "明细")
                    .param("tax_no", "1123123123")
                    .header("Authorization", tokenMap.get("access_token")))
                    .andExpect(status().is(200))
                    .andReturn().getResponse().getContentAsString();
            if (i == 9) {
                MemberReceipt memberReceipt = JsonUtil.jsonToObject(json, MemberReceipt.class);
                Map dbMap = memberDaoSupport.queryForMap("select receipt_title,receipt_content,tax_no,receipt_type from es_member_receipt where receipt_id = ?", memberReceipt.getReceiptId());
                Map map = new HashMap();
                map.put("receipt_title", "北京科技有限公司");
                map.put("receipt_content", "明细");
                map.put("tax_no", "1123123123");
                map.put("receipt_type", ReceiptTypeEnum.VATORDINARY.name());
                Assert.assertEquals(map, dbMap);
                id = memberReceipt.getReceiptId();
            }
        }
        //发票数上限校验
        error = new ErrorMessage("121", "发票数已达上限10个");
        mockMvc.perform(post("/members/receipt/ordinary")
                .param("receipt_title", "北京科技有限公司")
                .param("receipt_content", "明细")
                .param("tax_no", "1123123123")
                .header("Authorization", tokenMap.get("access_token")))
                .andExpect(status().is(500))
                .andExpect(objectEquals(error));

    }

    /**
     * 修改发票测试用例
     *
     * @throws Exception
     */
    @Test
    public void edit() throws Exception {
        this.addTest();
        //参数校验
        String[] names = new String[]{"receipt_title", "receipt_content", "tax_no", "message"};
        String[] values1 = new String[]{"", "明细", "123123", "发票抬头不能为空"};
        String[] values2 = new String[]{"北京科技有限公司", "", "123123", "发票内容不能为空"};
        String[] values3 = new String[]{"北京科技有限公司", "明细", "", "发票税号不能为空"};
        list = toMultiValueMaps(names, values1, values2, values3);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(put("/members/receipt/" + id + "/ordinary")
                    .params(params)
                    .header("Authorization", tokenMap.get("access_token")))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //修改不存在的发票
        ErrorMessage error = new ErrorMessage("002", "无权操作");
        mockMvc.perform(put("/members/receipt/1111/ordinary")
                .param("receipt_title", "北京科技有限公司")
                .param("receipt_content", "办公用品")
                .param("tax_no", "1")
                .header("Authorization", tokenMap.get("access_token")))
                .andExpect(status().is(401))
                .andExpect(objectEquals(error));
        //正确性校验
        mockMvc.perform(put("/members/receipt/" + id + "/ordinary")
                .param("receipt_title", "北京科技有限公司")
                .param("receipt_content", "办公用品")
                .param("tax_no", "1")
                .header("Authorization", tokenMap.get("access_token")))
                .andExpect(status().is(200));
        Map map = new HashMap();
        map.put("receipt_title", "北京科技有限公司");
        map.put("receipt_content", "办公用品");
        map.put("tax_no", "1");
        Map dbMap = memberDaoSupport.queryForMap("select receipt_title,receipt_content,tax_no from es_member_receipt where receipt_id = ?", id);
        Assert.assertEquals(dbMap, map);
    }


    /**
     * 删除测试
     *
     * @throws Exception
     */
    @Test
    public void deleteTest() throws Exception {
        this.addTest();
        //删除不存在发票信息
        ErrorMessage error = new ErrorMessage("002", "无权操作");
        mockMvc.perform(delete("/members/receipt/1111")
                .header("Authorization", tokenMap.get("access_token")))
                .andExpect(status().is(401))
                .andExpect(objectEquals(error));
        //正确测试
        mockMvc.perform(delete("/members/receipt/" + id)
                .header("Authorization", tokenMap.get("access_token")))
                .andExpect(status().is(200));
        MemberReceipt receipt = this.memberReceiptManager.getModel(id);
        Assert.assertNull(receipt);
    }
}
