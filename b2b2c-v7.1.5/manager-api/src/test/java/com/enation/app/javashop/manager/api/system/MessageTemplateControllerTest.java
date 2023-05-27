package com.enation.app.javashop.manager.api.system;

import com.enation.app.javashop.core.shop.model.enums.ShopStatusEnum;
import com.enation.app.javashop.core.system.model.dto.MessageTemplateDTO;
import com.enation.app.javashop.core.system.service.MessageTemplateManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author zjp
 * @version v7.0
 * @Description 消息模版测试类
 * @ClassName MessageTemplateControllerTest
 * @since v7.0 上午10:29 2018/7/6
 */
@Transactional(value = "systemTransactionManager", rollbackFor = Exception.class)
public class MessageTemplateControllerTest extends BaseTest{

    List<MultiValueMap<String, String>> list = null;
    List<MultiValueMap<String, String>> list1 = null;

    private Integer id;

    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport systemDaoSupport;

    @Autowired
    private MessageTemplateManager messageTemplateManager;

    @Before
    public void insertTestData() throws Exception {
        systemDaoSupport.execute(" delete from es_message_template ");

        Map map = new HashMap();
        map.put("tpl_name","测试消息模版");
        map.put("email_title","测试邮箱标题");
        map.put("sms_state","OPEN");
        map.put("notice_state", "OPEN");
        map.put("email_state","OPEN");
        map.put("content","测试内容");
        map.put("sms_content","测试内容");
        map.put("email_content","测试内容");

        this.systemDaoSupport.insert("es_message_template",map);
        id = this.systemDaoSupport.getLastId("es_message_template");

        //基本参数校验
        String[] names = new String[]{"tpl_name", "email_title", "sms_state", "notice_state", "email_state", "content", "sms_content", "email_content", "message"};
        String[] values1 = new String[]{"", "测试邮箱标题", "OPEN", "OPEN", "OPEN", "测试内容", "测试内容", "测试内容", "模板名称必填"};
        String[] values2 = new String[]{"测试消息模版", "", "OPEN", "OPEN", "OPEN", "测试内容", "测试内容", "测试内容", "邮件标题必填"};
        String[] values3 = new String[]{"测试消息模版", "测试邮箱标题", "", "OPEN", "OPEN", "测试内容", "测试内容", "测试内容", "短信提醒是否开启必填"};
        String[] values4 = new String[]{"测试消息模版", "测试邮箱标题", "OPEN", "", "OPEN", "测试内容", "测试内容", "测试内容", "站内信提醒是否开启必填"};
        String[] values5 = new String[]{"测试消息模版", "测试邮箱标题", "OPEN", "OPEN", "", "测试内容", "测试内容", "测试内容", "邮件提醒是否开启必填"};
        String[] values6 = new String[]{"测试消息模版", "测试邮箱标题", "OPEN", "OPEN", "OPEN", "", "测试内容", "测试内容", "站内信内容必填"};
        String[] values7 = new String[]{"测试消息模版", "测试邮箱标题", "OPEN", "OPEN", "OPEN", "测试内容", "", "测试内容", "短信内容必填"};
        String[] values8 = new String[]{"测试消息模版", "测试邮箱标题", "OPEN", "OPEN", "OPEN", "测试内容", "测试内容", "", "邮件内容必填"};
        list = toMultiValueMaps(names, values1, values2, values3, values4, values5,values6,values7,values8);

        String[] names1 = new String[]{"tpl_name", "email_title", "sms_state", "notice_state", "email_state", "content", "sms_content", "email_content"};
        String[] values9 = new String[]{"测试消息模版", "测试邮箱标题", "OPEN", "OPEN", "OPEN", "测试内容", "测试内容", "测试内容"};
        list1 = toMultiValueMaps(names1,values9);
    }

    @Test
    public void  editTest() throws Exception {
        ErrorMessage error = null;
        //参数性校验
        for (MultiValueMap<String, String> params : list) {
            String message =  params.get("message").get(0);
            error  = new ErrorMessage("004",message);
            mockMvc.perform(put("/admin/systems/message-templates/"+id)
                    .header("Authorization", superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals( error ));
        }
        //正确性校验
        mockMvc.perform(put("/admin/systems/message-templates/"+id)
                .header("Authorization", superAdmin)
                .params(list1.get(0)))
                .andExpect(status().is(200));
        String sql = "select tpl_name, email_title, sms_state, notice_state, email_state, content, sms_content, email_content from es_message_template where id = ? ";
        Map map = this.systemDaoSupport.queryForMap(sql, id);
        Map actual = this.formatMap(map);
        Map expected = this.formatMap(list1.get(0));
        Assert.assertEquals(expected, actual);
    }
}
