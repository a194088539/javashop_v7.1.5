package com.enation.app.javashop.manager.api;

import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.base.service.SettingManager;
import com.enation.app.javashop.core.system.model.vo.AppPushSetting;
import com.enation.app.javashop.core.system.model.vo.SiteSetting;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * app推送设置测试
 *
 * @author zh
 * @version v7.0
 * @date 18/6/6 下午5:30
 * @since v7.0
 */
@Transactional(value = "systemTransactionManager", rollbackFor = Exception.class)
public class PushSettingControllerTest extends BaseTest {

    @Autowired
    private SettingManager settingManager;

    /**
     * 添加修改app推送设置
     *
     * @throws Exception
     */
    @Test
    public void editPushSetting() throws Exception {
        AppPushSetting appPushSetting = new AppPushSetting();
        appPushSetting.setAndroidGoodsActivity("abc");
        appPushSetting.setAndroidPushKey("abc");
        appPushSetting.setAndroidPushSecret("abc");
        appPushSetting.setIosPushKey("bnm");
        appPushSetting.setIosPushSecret("bnm");
        MultiValueMap<String, String> siteSettingMap = objectToMap(appPushSetting);
        mockMvc.perform(put("/admin/systems/push/")
                .header("Authorization",superAdmin)
                .params(siteSettingMap))
                .andExpect(status().is(200));
        String appPushSettingJson = settingManager.get(SettingGroup.PUSH);
        AppPushSetting setting = JsonUtil.jsonToObject(appPushSettingJson,AppPushSetting.class);
        MultiValueMap<String, String> dbSiteSettingMap = objectToMap(setting);
        Assert.assertEquals(siteSettingMap, dbSiteSettingMap);


    }
}
