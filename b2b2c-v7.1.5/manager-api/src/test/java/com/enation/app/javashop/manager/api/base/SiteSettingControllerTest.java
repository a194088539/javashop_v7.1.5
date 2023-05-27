package com.enation.app.javashop.manager.api.base;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.base.service.SettingManager;
import com.enation.app.javashop.core.system.model.vo.SiteSetting;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 站点设置测试类
 *
 * @author zh
 * @version v7.0
 * @date 18/5/31 上午8:57
 * @since v7.0
 */
@Transactional(value = "systemTransactionManager", rollbackFor = Exception.class)
public class SiteSettingControllerTest extends BaseTest {

    List<MultiValueMap<String, String>> list = null;

    @Autowired
    private Cache cache;

    @Autowired
    private SettingManager settingManager;

    /**
     * 修改或者添加站点设置测试
     *
     * @throws Exception
     */
    @Test
    public void editSiteSettingTest() throws Exception {
        String[] names = new String[]{"site_name", "title", "keywords", "descript", "siteon", "close_reson", "logo", "global_auth_key", "default_img", "test_mode", "message"};
        String[] values1 = new String[]{"javashop", "javashop", "javashop", "javashop", "-1", "javashop", "http://test.com", "aaaaaa", "http://a.jpg", "1", "必须为数字且,1为开启,0为关闭"};
        String[] values2 = new String[]{"javashop", "javashop", "javashop", "javashop", "12", "javashop", "http://test.com", "aaaaaa", "http://a.jpg", "1", "必须为数字且,1为开启,0为关闭"};
        String[] values3 = new String[]{"javashop", "javashop", "javashop", "javashop", "", "javashop", "http://test.com", "aaaaaa", "http://a.jpg", "1", "网站是否关闭不能为空"};
        String[] values4 = new String[]{"javashop", "javashop", "javashop", "javashop", "1", "javashop", "http://test.com", "aaaaaa", "http://a.jpg", "-1", "必须为数字且,1为开启,0为关闭"};
        String[] values5 = new String[]{"javashop", "javashop", "javashop", "javashop", "1", "javashop", "http://test.com", "aaaaaa", "http://a.jpg", "2", "必须为数字且,1为开启,0为关闭"};
        String[] values6 = new String[]{"javashop", "javashop", "javashop", "javashop", "1", "javashop", "http://test.com", "aaaaaa", "http://a.jpg", "", "是否为测试模式不能为空"};


        list = toMultiValueMaps(names, values1, values2, values3, values4, values5, values6);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(put("/admin/settings/site/")
                    .header("Authorization", superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //正确测试
        SiteSetting siteSetting = new SiteSetting();
        siteSetting.setCloseReson("javashop");
        siteSetting.setKeywords("javashop");
        siteSetting.setDescript("javashop");
        siteSetting.setLogo("http://test1.com");
        siteSetting.setSiteName("javashop");
        siteSetting.setTitle("javashop");
        siteSetting.setSiteon(1);
        siteSetting.setGlobalAuthKey("aaaa");
        siteSetting.setDefaultImg("http:..a.jpg");
        siteSetting.setTestMode(1);
        MultiValueMap<String, String> siteSettingMap = objectToMap(siteSetting);
        mockMvc.perform(put("/admin/settings/site/")
                .header("Authorization", superAdmin)
                .params(siteSettingMap))
                .andExpect(status().is(200));
        String siteSettingJson = settingManager.get(SettingGroup.SITE);

        SiteSetting setting = JsonUtil.jsonToObject(siteSettingJson,SiteSetting.class);
        MultiValueMap<String, String> dbSiteSettingMap = objectToMap(setting);
        Assert.assertEquals(siteSettingMap, dbSiteSettingMap);
        cache.remove(CachePrefix.SETTING.getPrefix() + "SITE");
    }


}
