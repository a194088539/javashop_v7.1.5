package com.enation.app.javashop.base.api;

import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.base.plugin.express.util.MD5;
import com.enation.app.javashop.core.base.service.SettingManager;
import com.enation.app.javashop.core.goodssearch.model.CustomWords;
import com.enation.app.javashop.core.goodssearch.model.EsSecretSetting;
import com.enation.app.javashop.core.goodssearch.service.CustomWordsManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomWordsBaseControllerTest extends BaseTest {


    @Autowired
    private CustomWordsManager customWordsManager;

    @Autowired
    private SettingManager settingManager;

    private String secretKey = "";

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport daoSupport;

    @Before
    public void setKey(){
        String password = "123456";
        EsSecretSetting secretSetting = new EsSecretSetting();
        secretSetting.setPassword(password);
        secretSetting.setSecretKey(MD5.encode(SettingGroup.ES_SIGN + "_" + MD5.encode(password)));
        secretKey = secretSetting.getSecretKey();
        settingManager.save(SettingGroup.ES_SIGN,secretSetting);
        this.daoSupport.execute("delete from es_custom_words");
    }

    @Test
    public void getCustomWords() throws Exception {

        ErrorMessage error  = new ErrorMessage("310","秘钥验证失败！");

        mockMvc.perform(get("/load-customwords")
                .param("secret_key","123456"))
                .andExpect(status().is(500))
                .andExpect( objectEquals( error ));

        String res = mockMvc.perform(get("/load-customwords")
                .param("secret_key",secretKey))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        Assert.assertEquals(res,"");
        CustomWords customWords = new CustomWords();
        customWords.setName("电脑手机");
        customWordsManager.add(customWords);

        res = mockMvc.perform(get("/load-customwords")
                .param("secret_key",secretKey))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        Assert.assertEquals(res,"电脑手机");
    }
}