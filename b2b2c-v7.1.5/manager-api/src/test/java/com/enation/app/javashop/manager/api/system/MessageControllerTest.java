package com.enation.app.javashop.manager.api.system;

import com.enation.app.javashop.core.system.model.dos.Message;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 后台消息测试
 *
 * @author zh
 * @version v7.0
 * @date 18/7/5 上午10:36
 * @since v7.0
 */

public class MessageControllerTest extends BaseTest {

    List<MultiValueMap<String, String>> list = null;

    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport systemDaoSupport;

    /**
     * 发送站内消息
     *
     * @throws Exception
     */
    @Test
    public void addTest() throws Exception {
        //参数校验
        String[] names = new String[]{"title", "content", "member_ids", "send_type", "message"};
        String[] values1 = new String[]{"", "测试内容", "1,2,3", "0", "站内消息标题必须在2-30个字符之间"};
        String[] values2 = new String[]{"测", "测试内容", "1,2,3", "0", "站内消息标题必须在2-30个字符之间"};
        String[] values3 = new String[]{"测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测", "测试标题", "1,2,3", "0", "站内消息标题必须在2-30个字符之间"};
        String[] values4 = new String[]{"测试标题", "", "1,2,3", "0", "站内消息内容不能为空"};
        String[] values5 = new String[]{"测试标题", "测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试测试标题测试标题测试", "1,2,3", "0", "站内消息内容不能超过500个字符"};
        String[] values6 = new String[]{"测试标题", "测试内容", "1,2,3", "", "发送类型不能为空"};
        String[] values7 = new String[]{"测试标题", "测试内容", "1,2,3", "-1", "发送类型参数错误"};
        String[] values8 = new String[]{"测试标题", "测试内容", "1,2,3", "2", "发送类型参数错误"};
        list = toMultiValueMaps(names, values1, values2, values3, values4, values5, values6, values7, values8);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(post("/admin/systems/messages")
                    .header("Authorization", superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //如果发送类型为会员，则会员id为必填
        String[] values9 = new String[]{"测试标题", "测试内容", "", "1", "请指定发送会员"};
        list = toMultiValueMaps(names, values9);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("122", message);
            mockMvc.perform(post("/admin/systems/messages")
                    .header("Authorization", superAdmin)
                    .params(params))
                    .andExpect(status().is(500))
                    .andExpect(objectEquals(error));
        }
        //正确校验
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("title", "测试标题");
        map.add("content", "测试内容");
        map.add("member_ids", "1,2,3,4");
        map.add("send_type", "0");
        String result = mockMvc.perform(post("/admin/systems/messages")
                .header("Authorization", superAdmin)
                .params(map))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        Message message = JsonUtil.jsonToObject(result, Message.class);
        Map dbMap = systemDaoSupport.queryForMap("select title,content,member_ids,send_type from es_message where id = ?", message.getId());
        Assert.assertEquals(formatMap(map), formatMap(dbMap));
    }
}
