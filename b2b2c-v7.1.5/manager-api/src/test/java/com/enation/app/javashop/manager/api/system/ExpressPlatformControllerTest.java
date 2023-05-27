package com.enation.app.javashop.manager.api.system;

import com.enation.app.javashop.core.system.model.dos.ExpressPlatformDO;
import com.enation.app.javashop.core.system.model.dos.UploaderDO;
import com.enation.app.javashop.core.system.service.ExpressPlatformManager;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import net.sf.json.JSONArray;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 快递平台测试用例
 *
 * @author zh
 * @version v7.0
 * @date 18/7/14 下午5:51
 * @since v7.0
 */
@Transactional(value = "systemTransactionManager", rollbackFor = Exception.class)
public class ExpressPlatformControllerTest extends BaseTest {

    @Autowired
    private ExpressPlatformManager expressPlatformManager;

    /**
     * 修改快递平台测试
     *
     * @throws Exception
     */
    @Test
    public void editTest() throws Exception {
        String json = "[{\n" +
                "\t\"name\": \"appid\",\n" +
                "\t\"text\": \"appid\",\n" +
                "\t\"type\": \"text\",\n" +
                "\t\"value\": \"245843\"\n" +
                "}, {\n" +
                "\t\"name\": \"app_secret\",\n" +
                "\t\"text\": \"密钥\",\n" +
                "\t\"type\": \"text\",\n" +
                "\t\"value\": \"7a201558454c4137a4d1aa90e0505a7e1\"\n" +
                "}]";

        //bean id 不存在测试
        Map map = new HashMap<>(16);
        map.put("name", "showApi快递");
        map.put("open", "0");
        map.put("config_items", JSONArray.fromObject(json));
        ErrorMessage error = new ErrorMessage("003", "该快递方案不存在");
        mockMvc.perform(put("/admin/systems/express-platforms/bean")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(objectEquals(error))
                .andExpect(status().is(404));
        //正确测试
        mockMvc.perform(put("/admin/systems/express-platforms/showApiPlugin")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(status().is(200));
        ExpressPlatformDO expressPlatformDO = expressPlatformManager.getExpressPlatform("showApiPlugin");
        Assert.assertEquals(expressPlatformDO.getConfig().toString(), JSONArray.fromObject(json).toString());
        Assert.assertEquals(expressPlatformDO.getName().toString(), "showApi快递");

    }


    /**
     * 快递平台方案开启测试
     *
     * @throws Exception
     */
    @Test
    public void openTest() throws Exception {
        //bean id 不存在测试
        ErrorMessage error = new ErrorMessage("003", "该快递平台不存在");
        mockMvc.perform(put("/admin/systems/express-platforms/bean/open")
                .header("Authorization", superAdmin))
                .andExpect(objectEquals(error))
                .andExpect(status().is(404));
        //正确测试
        mockMvc.perform(put("/admin/systems/express-platforms/showApiPlugin/open")
                .header("Authorization", superAdmin))
                .andExpect(status().is(200));
        ExpressPlatformDO expressPlatformDO = expressPlatformManager.getExpressPlatform("showApiPlugin");
        Assert.assertEquals(expressPlatformDO.getOpen().toString(), "1");
    }


}
