package com.enation.app.javashop.manager.api.system;

import com.enation.app.javashop.core.system.model.dos.SmsPlatformDO;
import com.enation.app.javashop.core.system.model.dos.UploaderDO;
import com.enation.app.javashop.core.system.model.dos.WayBillDO;
import com.enation.app.javashop.core.system.service.SmsPlatformManager;
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
 * 短信网关设置测试
 *
 * @author zh
 * @version v7.0
 * @date 18/6/14 下午3:58
 * @since v7.0
 */
@Transactional(value = "systemTransactionManager", rollbackFor = Exception.class)
public class SmsPlatformControllerTest extends BaseTest {

    @Autowired
    private SmsPlatformManager smsPlatformManager;

    /**
     * 修改短信网关测试
     *
     * @throws Exception
     */
    @Test
    public void editTest() throws Exception {
        String config = "[\n" +
                "        {\n" +
                "          \"name\": \"xh\",\n" +
                "          \"text\": \"域名\",\n" +
                "          \"type\": \"text\",\n" +
                "          \"value\": \"啊啊啊\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"password\",\n" +
                "          \"text\": \"储存空间\",\n" +
                "          \"type\": \"text\",\n" +
                "          \"value\": \"Enation752513\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"productid\",\n" +
                "          \"text\": \"二级路径\",\n" +
                "          \"type\": \"text\",\n" +
                "          \"value\": \"676767\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"username\",\n" +
                "          \"text\": \"密钥id\",\n" +
                "          \"type\": \"text\",\n" +
                "          \"value\": \"yanjiao\"\n" +
                "        }\n" +
                "      ]";
        //bean id 不存在测试
        Map map = new HashMap<>(16);
        map.put("name", "5c短信网关");
        map.put("config_items", JSONArray.fromObject(config));
        ErrorMessage error = new ErrorMessage("003", "该短信方案不存在");
        mockMvc.perform(put("/admin/systems/platforms/bean")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(objectEquals(error))
                .andExpect(status().is(404));
        //正确测试
        mockMvc.perform(put("/admin/systems/platforms/sms5cPlugin")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(status().is(200));
        SmsPlatformDO smsPlatformDO = smsPlatformManager.getSmsPlateform("sms5cPlugin");
        Assert.assertEquals(smsPlatformDO.getConfig().toString(), JSONArray.fromObject(config).toString());
        Assert.assertEquals(smsPlatformDO.getName().toString(), "5c短信网关");

    }


    /**
     * 开启短信网关测试
     *
     * @throws Exception
     */
    @Test
    public void openTest() throws Exception {
        //bean id 不存在测试
        ErrorMessage error = new ErrorMessage("003", "该短信方案不存在");
        mockMvc.perform(put("/admin/systems/platforms/bean/open")
                .header("Authorization", superAdmin))
                .andExpect(objectEquals(error))
                .andExpect(status().is(404));
        //正确测试
        mockMvc.perform(put("/admin/systems/platforms/sms5cPlugin/open")
                .header("Authorization", superAdmin))
                .andExpect(status().is(200));
        SmsPlatformDO smsPlatformDO = smsPlatformManager.getSmsPlateform("sms5cPlugin");
        Assert.assertEquals(smsPlatformDO.getOpen().toString(), "1");

    }
}
