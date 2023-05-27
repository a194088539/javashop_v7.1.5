package com.enation.app.javashop.manager.api.member;

import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.base.model.dos.SettingsDO;
import com.enation.app.javashop.core.base.service.SettingManager;
import com.enation.app.javashop.core.member.model.dto.ConnectSettingDTO;
import com.enation.app.javashop.core.member.model.enums.ConnectTypeEnum;
import com.enation.app.javashop.core.member.model.vo.ConnectSettingParametersVO;
import com.enation.app.javashop.core.member.model.vo.ConnectSettingVO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author zjp
 * @version v7.0
 * @Description 信任登录后台测试用例
 * @ClassName ConnectControllerTest
 * @since v7.0 下午8:40 2018/6/26
 */
@Transactional(value = "memberTransactionManager", rollbackFor = Exception.class)
public class MemberConnectControllerTest extends BaseTest {
    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport systemDaoSupport;

    @Autowired
    private SettingManager settingManager;

    /**
     * 编辑授权登录参数测试
     *
     * @throws Exception
     */
    @Test
    public void editConnectSettingTest() throws Exception {
        ConnectSettingDTO connectSettingDTO = new ConnectSettingDTO();
        connectSettingDTO.setName("qq");
        connectSettingDTO.setType(ConnectTypeEnum.QQ.value());
        List<ConnectSettingParametersVO> list = JsonUtil.jsonToList("[\n" +
                "      {\n" +
                "        \"name\": \"网页端参数 （PC，WAP，微信网页端）\",\n" +
                "        \"configList\": [\n" +
                "          {\n" +
                "            \"key\": \"qq_pc_app_id\",\n" +
                "            \"name\": \"AppId\",\n" +
                "            \"value\": \"101374797\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"key\": \"qq_pc_app_key\",\n" +
                "            \"name\": \"AppKey\",\n" +
                "            \"value\": \"dd69523f13737bc04209863eb8f48f50\"\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"原生-APP参数(安卓)\",\n" +
                "        \"configList\": [\n" +
                "          {\n" +
                "            \"key\": \"qq_native_android_app_id\",\n" +
                "            \"name\": \"AppId\",\n" +
                "            \"value\": null\n" +
                "          },\n" +
                "          {\n" +
                "            \"key\": \"qq_native_android_app_key\",\n" +
                "            \"name\": \"AppKey\",\n" +
                "            \"value\": null\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"原生-APP参数(IOS)\",\n" +
                "        \"configList\": [\n" +
                "          {\n" +
                "            \"key\": \"qq_native_ios_app_id\",\n" +
                "            \"name\": \"AppId\",\n" +
                "            \"value\": null\n" +
                "          },\n" +
                "          {\n" +
                "            \"key\": \"qq_native_ios_app_key\",\n" +
                "            \"name\": \"AppKey\",\n" +
                "            \"value\": null\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"RN-APP参数(安卓)\",\n" +
                "        \"configList\": [\n" +
                "          {\n" +
                "            \"key\": \"qq_rn_android_app_id\",\n" +
                "            \"name\": \"AppId\",\n" +
                "            \"value\": null\n" +
                "          },\n" +
                "          {\n" +
                "            \"key\": \"qq_rn_android_app_key\",\n" +
                "            \"name\": \"AppKey\",\n" +
                "            \"value\": null\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"RN-APP参数(IOS)\",\n" +
                "        \"configList\": [\n" +
                "          {\n" +
                "            \"key\": \"qq_rn_ios_app_id\",\n" +
                "            \"name\": \"AppId\",\n" +
                "            \"value\": null\n" +
                "          },\n" +
                "          {\n" +
                "            \"key\": \"qq_rn_ios_app_key\",\n" +
                "            \"name\": \"AppKey\",\n" +
                "            \"value\": null\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]", ConnectSettingParametersVO.class);
        connectSettingDTO.setClientList(list);

        mockMvc.perform(put("/admin/members/connect/" + ConnectTypeEnum.QQ.name())
                .header("Authorization", superAdmin)
                .content(JsonUtil.objectToJson(connectSettingDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));


    }


}
