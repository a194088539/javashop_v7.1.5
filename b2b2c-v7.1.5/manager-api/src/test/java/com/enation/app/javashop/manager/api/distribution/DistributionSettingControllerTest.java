package com.enation.app.javashop.manager.api.distribution;

import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.base.service.SettingManager;
import com.enation.app.javashop.core.distribution.model.dos.DistributionSetting;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 分销设置测试
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018/6/12 上午4:48
 * @Description:
 */
@Transactional(value = "distributionTransactionManager", rollbackFor = Exception.class)
public class DistributionSettingControllerTest extends BaseTest {

    @Autowired
    private SettingManager settingManager;

    @Test
    public void editDistributionSettingTest() throws Exception {

        DistributionSetting distributionSetting = new DistributionSetting();
        distributionSetting.setCycle(20);
        distributionSetting.setGoodsModel(1);
        mockMvc.perform(put("/admin/distribution/settings").header("Authorization", superAdmin)
                .param("cycle", "20").param("goods_model", "1"))
                .andExpect(status().is(200));
        String json = settingManager.get(SettingGroup.DISTRIBUTION);

        DistributionSetting setting = JsonUtil.jsonToObject(json,DistributionSetting.class);

        Assert.assertEquals(distributionSetting.toString(), setting.toString());


        distributionSetting = new DistributionSetting();
        distributionSetting.setCycle(30);
        distributionSetting.setGoodsModel(0);
        mockMvc.perform(put("/admin/distribution/settings").header("Authorization", superAdmin)
                .param("cycle", "30").param("goods_model", "0"))
                .andExpect(status().is(200));
        json = settingManager.get(SettingGroup.DISTRIBUTION);

        setting = JsonUtil.jsonToObject(json,DistributionSetting.class);
        Assert.assertEquals(distributionSetting.toString(), setting.toString());
    }

    @Test
    public void getDistributionSettingTest() throws Exception {
        DistributionSetting distributionSetting = new DistributionSetting();
        distributionSetting.setCycle(30);
        distributionSetting.setGoodsModel(0);

        mockMvc.perform(get("/admin/distribution/settings").header("Authorization", superAdmin))
                .andExpect(objectEquals(distributionSetting));

    }
}