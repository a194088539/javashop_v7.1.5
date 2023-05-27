package com.enation.app.javashop.manager.api.system;

import com.enation.app.javashop.core.system.model.dos.UploaderDO;
import com.enation.app.javashop.core.system.service.UploaderManager;
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
 * 上传方案设置测试
 *
 * @author zh
 * @version v7.0
 * @date 18/6/13 下午4:31
 * @since v7.0
 */
@Transactional(value = "systemTransactionManager", rollbackFor = Exception.class)
public class UploadControllerTest extends BaseTest {


    @Autowired
    private UploaderManager uploaderManager;

    /**
     * 修改上传方案
     *
     * @throws Exception
     */
    @Test
    public void editTest() throws Exception {

        String json = "[\n" +
                "        {\n" +
                "          \"name\": \"endpoint\",\n" +
                "          \"text\": \"域名\",\n" +
                "          \"type\": \"text\",\n" +
                "          \"value\": \"oss-cn-beijing.aliyuncs.com\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"bucketName\",\n" +
                "          \"text\": \"储存空间\",\n" +
                "          \"type\": \"text\",\n" +
                "          \"value\": \"bucketName\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"picLocation\",\n" +
                "          \"text\": \"二级路径\",\n" +
                "          \"type\": \"text\",\n" +
                "          \"value\": \"picLocation/\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"accessKeyId\",\n" +
                "          \"text\": \"密钥id\",\n" +
                "          \"type\": \"text\",\n" +
                "          \"value\": \"accessKeyId\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"accessKeySecret\",\n" +
                "          \"text\": \"密钥\",\n" +
                "          \"type\": \"text\",\n" +
                "          \"value\": \"accessKeySecret\"\n" +
                "        }\n" +
                "      ]";

        //bean id 不存在测试
        Map map = new HashMap<>(16);
        map.put("name", "本地存储");
        map.put("config_items", JSONArray.fromObject(json));
        ErrorMessage error = new ErrorMessage("003", "该存储方案不存在");
        mockMvc.perform(put("/admin/systems/uploaders/bean")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(objectEquals(error))
                .andExpect(status().is(404));
        //正确测试
        mockMvc.perform(put("/admin/systems/uploaders/localPlugin")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(status().is(200));
        UploaderDO uploaderDO = uploaderManager.getUploader("localPlugin");
        Assert.assertEquals(uploaderDO.getConfig().toString(), JSONArray.fromObject(json).toString());
        Assert.assertEquals(uploaderDO.getName().toString(), "本地存储");


    }

    /**
     * 存储方案开启测试
     *
     * @throws Exception
     */
    @Test
    public void openTest() throws Exception {
        //bean id 不存在测试
        ErrorMessage error = new ErrorMessage("003", "该存储方案不存在");
        mockMvc.perform(put("/admin/systems/uploaders/bean/open")
                .header("Authorization", superAdmin))
                .andExpect(objectEquals(error))
                .andExpect(status().is(404));
        //正确测试
        mockMvc.perform(put("/admin/systems/uploaders/localPlugin/open")
                .header("Authorization", superAdmin))
                .andExpect(status().is(200));
        UploaderDO uploaderDO = uploaderManager.getUploader("localPlugin");
        Assert.assertEquals(uploaderDO.getOpen().toString(), "1");
    }

}
