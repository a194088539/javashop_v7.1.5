package com.enation.app.javashop.base;

import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.FileUtil;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileInputStream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Ueditor相关测试
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/6/8
 */
public class UeditorControllerTest extends BaseTest {


    /**
     * 上传测试
     *
     * @throws Exception
     */
    @Test
    public void testUpload() throws Exception {
        File file = new File("src/test/java/a.jpg");
        FileInputStream input = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("upfile", file.getName(), "image/jpeg", IOUtils.toByteArray(input));

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/ueditor/").file(multipartFile))
                .andExpect(status().is(200));
        this.delAllFile("nullstatics");
        this.delFolder("nullstatics");

    }


    /**
     * 配置文件获取测试
     *
     * @throws Exception
     */
    @Test
    public void testConfig() throws Exception {
        String callback = "callback";
        String content = mockMvc.perform(MockMvcRequestBuilders.get("/ueditor/").param("callback", callback)).andExpect(status().is(200)).andReturn().getResponse().getContentAsString();

        String expectedContent = FileUtil.readFile("/ueditor_config.json");
        expectedContent = "/**/" + callback + "(" + expectedContent + ");";
        Assert.assertEquals(expectedContent, content);

    }


}
