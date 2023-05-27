package com.enation.app.javashop.manager.api.system;

import com.enation.app.javashop.core.system.model.dos.WayBillDO;
import com.enation.app.javashop.core.system.service.WaybillManager;
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
 * 电子面单测试类
 *
 * @author zh
 * @version v7.0
 * @date 18/6/14 下午3:14
 * @since v7.0
 */
@Transactional(value = "systemTransactionManager", rollbackFor = Exception.class)
public class WayBillControllerTest extends BaseTest {
    @Autowired
    private WaybillManager waybillManager;

    /**
     * 修改电子面单
     *
     * @throws Exception
     */
    @Test
    public void edit() throws Exception {
        String config = "[\n" +
                "        {\n" +
                "          \"name\": \"EBusinessID\",\n" +
                "          \"text\": \"电商ID\",\n" +
                "          \"type\": \"text\",\n" +
                "          \"value\": \"EBusinessID\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"AppKey\",\n" +
                "          \"text\": \"密钥\",\n" +
                "          \"type\": \"text\",\n" +
                "          \"value\": \"AppKey\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"ReqURL\",\n" +
                "          \"text\": \"请求url\",\n" +
                "          \"type\": \"text\",\n" +
                "          \"value\": \"ReqURL\"\n" +
                "        }\n" +
                "      ]";
        //bean id 不存在测试
        Map map = new HashMap<>(16);
        map.put("name", "快递鸟");
        map.put("config_items", JSONArray.fromObject(config));
        ErrorMessage error = new ErrorMessage("003", "该电子面单方案不存在");
        mockMvc.perform(put("/admin/systems/waybills/bean")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(objectEquals(error))
                .andExpect(status().is(404));
        //正确测试
        mockMvc.perform(put("/admin/systems/waybills/kdnPlugin")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(status().is(200));
        WayBillDO wayBillDO = waybillManager.getWayBillByBean("kdnPlugin");
        Assert.assertEquals(wayBillDO.getConfig().toString(), JSONArray.fromObject(config).toString());
        Assert.assertEquals(wayBillDO.getName().toString(), "快递鸟");
    }

    /**
     * 开启电子面单
     *
     * @throws Exception
     */
    @Test
    public void openTest() throws Exception {
        //bean id 不存在测试
        ErrorMessage error = new ErrorMessage("003", "该电子面单方案不存在");
        mockMvc.perform(put("/admin/systems/waybills/bean/open")
                .header("Authorization", superAdmin))
                .andExpect(objectEquals(error))
                .andExpect(status().is(404));
        //正确测试
        mockMvc.perform(put("/admin/systems/waybills/kdnPlugin/open")
                .header("Authorization", superAdmin))
                .andExpect(status().is(200));
        WayBillDO wayBillDO = waybillManager.getWayBillByBean("kdnPlugin");
        Assert.assertEquals(wayBillDO.getOpen().toString(), "1");
    }


}
