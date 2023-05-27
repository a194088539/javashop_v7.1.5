package com.enation.app.javashop.manager.api.pagecreate;


import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.base.model.vo.SuccessMessage;
import com.enation.app.javashop.core.client.system.SettingClient;
import com.enation.app.javashop.core.pagecreate.exception.PageCreateErrorCode;
import com.enation.app.javashop.core.pagecreate.service.PageCreateManager;
import com.enation.app.javashop.core.system.model.vo.PageSetting;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 静态页生成商品数据
 *
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018/3/23 上午12:03
 */
@Rollback(true)
public class PageCreateControllerTest extends BaseTest {


    @MockBean
    private SettingClient settingClient;


    @Autowired
    private PageCreateManager pageCreateManager;

    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport daoSupport;

    @Before
    public void init() {
        //模拟数据
        Mockito.doNothing().when(settingClient).save(SettingGroup.PAGE,new PageSetting());
        Mockito.when(settingClient.get(SettingGroup.PAGE)).thenReturn(JsonUtil.objectToJson(new PageSetting()));

//        Mockito.doCallRealMethod().when(settingService).save();
    }

    @Test
    public void input() throws Exception {
        mockMvc.perform(get("/admin/page-create/input")
                .header("Authorization",superAdmin))
                .andExpect(status().is(200));
    }

    @Test
    public void save() throws Exception {
        //参数为空校验
        ErrorMessage error = new ErrorMessage("004","must not be empty");
        mockMvc.perform(post("/admin/page-create/save")
                .header("Authorization",superAdmin))
                .andExpect(objectEquals(error));
        mockMvc.perform(post("/admin/page-create/save")
                .header("Authorization",superAdmin)
                .param("static_page_address", "v65.javamall.com.cn"))
                .andExpect(objectEquals(error));

        mockMvc.perform(post("/admin/page-create/save")
                .header("Authorization",superAdmin)
                .param("static_page_wap_address", "v65.javamall.com.cn"))
                .andExpect(objectEquals(error));

        PageSetting pageSetting = new PageSetting();
        pageSetting.setStaticPageWapAddress("v65.javamall.com.cn");
        pageSetting.setStaticPageAddress("v65.javamall.com.cn");
        //正常流程校验
        mockMvc.perform(post("/admin/page-create/save")
                .header("Authorization",superAdmin)
                .param("static_page_address", "v65.javamall.com.cn")
                .param("static_page_wap_address", "v65.javamall.com.cn"))
                .andExpect(status().is(200));



    }

    //@Test
    public void create() throws Exception {
        //无效的j静态页选项
        ErrorMessage error = new ErrorMessage(PageCreateErrorCode.E904.code(), PageCreateErrorCode.E904.des());
        mockMvc.perform(post("/admin/page-create/create")
                .header("Authorization",superAdmin))
                .andExpect(objectEquals(error));
        mockMvc.perform(post("/admin/page-create/create")
                .header("Authorization",superAdmin)
                .param("choose_pages", "index"))
                .andExpect(objectEquals(error));
        //正常判定
        mockMvc.perform(post("/admin/page-create/create")
                .header("Authorization",superAdmin).param("choose_pages", "INDEX"))
                .andExpect(objectEquals(new SuccessMessage("任务开始")));

    }

}
