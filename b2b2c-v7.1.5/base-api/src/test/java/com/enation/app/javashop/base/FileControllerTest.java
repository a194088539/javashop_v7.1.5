package com.enation.app.javashop.base;

import com.enation.app.javashop.core.base.model.vo.FileVO;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileInputStream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 图片上传测试
 *
 * @author zh
 * @version v1.0
 * @since v7.0
 * 2018年3月28日 下午8:28:20
 */
public class FileControllerTest extends BaseTest {

    /**
     * 测试文件上传方法
     *
     * @throws Exception
     */
    @Test
    public void testUploadFile() throws Exception {
        //文件上传测试
        File file = new File("src/test/java/a.jpg");
        FileInputStream input = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "image/jpeg", IOUtils.toByteArray(input));

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/uploaders/").file(multipartFile))
                .andExpect(status().is(200));

        this.delAllFile("nullstatics");
        this.delFolder("nullstatics");
    }


}
