package com.enation.app.javashop.manager.api.pagedata;

import com.enation.app.javashop.core.pagedata.model.SiteNavigation;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v1.0
 * @Description: 导航栏单元测试
 * @date 2018/6/1 59:53
 * @since v7.0.0
 */
@Transactional(value = "systemTransactionManager",rollbackFor=Exception.class)
public class SiteNavigationControllerTest extends BaseTest {

    List<MultiValueMap<String, String>> list = null;

    List<MultiValueMap<String, String>> list1 = null;

    @Before
    public void insertTestData(){
        String[] names = new String[]{"navigation_name","url","client_type","image","message"};
        String[] values1 = new String[]{"","goods.html","PC","","导航名称不能为空"};
        String[] values2 = new String[]{"积分商城","","PC","","导航地址不能为空"};
        String[] values3 = new String[]{"积分商城","goods.html","","","不正确的客户端类型"};
        String[] values4 = new String[]{"积分商城","goods.html","PC1","","不正确的客户端类型"};
        list = toMultiValueMaps(names,values2,values1,values3,values4);
        String[] values5 = new String[]{"积分商城","goods.html","MOBILE","","移动端导航，图片必传"};
        String[] values6 = new String[]{"快捷的积分商城","goods.html","PC","","导航栏菜单名称已经超出最大限制"};
        list1 = toMultiValueMaps(names,values5,values6);

    }

    /**
     * 添加导航栏
     * @throws Exception
     */
    @Test
    public void testAdd() throws Exception {

        // 必填验证
        for (MultiValueMap<String,String> params  : list){
            String message =  params.get("message").get(0);
            ErrorMessage error  = new ErrorMessage("004",message);
            mockMvc.perform(post("/admin/pages/site-navigations")
                    .header("Authorization",superAdmin)
                    .params( params ))
                    .andExpect(status().is(400))
                    .andExpect(  objectEquals( error ));
        }
        //程序验证不通过
        for (MultiValueMap<String,String> params  : list1){
            String message =  params.get("message").get(0);
            ErrorMessage error  = new ErrorMessage("953",message);
            mockMvc.perform(post("/admin/pages/site-navigations")
                    .header("Authorization",superAdmin)
                    .params( params ))
                    .andExpect(status().is(500))
                    .andExpect(  objectEquals( error ));
        }
        //正确添加
        mockMvc.perform(post("/admin/pages/site-navigations")
                .header("Authorization",superAdmin)
                .param("navigation_name","积分商城")
                .param("url","goods.html")
                .param("client_type","PC"))
                .andExpect(status().is(200));

    }

    /**
     * 修改导航栏
     * @throws Exception
     */
    @Test
    public void testEdit() throws Exception {

        SiteNavigation nav = add();

        // 必填验证
        for (MultiValueMap<String,String> params  : list){
            String message =  params.get("message").get(0);
            ErrorMessage error  = new ErrorMessage("004",message);
            mockMvc.perform(put("/admin/pages/site-navigations/"+nav.getNavigationId())
                    .header("Authorization",superAdmin)
                    .params( params ))
                    .andExpect(status().is(400))
                    .andExpect(  objectEquals( error ));
        }
        //程序验证不通过
        for (MultiValueMap<String,String> params  : list1){
            String message =  params.get("message").get(0);
            ErrorMessage error  = new ErrorMessage("953",message);
            mockMvc.perform(put("/admin/pages/site-navigations/"+nav.getNavigationId())
                    .header("Authorization",superAdmin)
                    .params( params ))
                    .andExpect(status().is(500))
                    .andExpect(  objectEquals( error ));
        }
        //不存在的导航栏
        ErrorMessage error  = new ErrorMessage("953","导航栏不存在，请正确操作");
        mockMvc.perform(put("/admin/pages/site-navigations/-1")
                .header("Authorization",superAdmin)
                .param("navigation_name","积分商城1")
                .param("url","goods.html")
                .param("client_type","PC"))
                .andExpect(objectEquals( error ));
        //正确修改
        mockMvc.perform(put("/admin/pages/site-navigations/"+nav.getNavigationId())
                .header("Authorization",superAdmin)
                .param("navigation_name","积分商城1")
                .param("url","goods.html")
                .param("client_type","PC"))
                .andExpect(status().is(200));
        //查询一个
        mockMvc.perform(get("/admin/pages/site-navigations/"+nav.getNavigationId())
                .header("Authorization",superAdmin))
                .andExpect(status().is(200))
                .andExpect(jsonPath("navigation_name").value("积分商城1"));

    }

    /**
     * 删除导航栏
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {

        ErrorMessage error  = new ErrorMessage("953","导航栏不存在，请正确操作");
        mockMvc.perform(delete("/admin/pages/site-navigations/-1")
                .header("Authorization",superAdmin))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));
        //正确删除
        SiteNavigation nav = add();
        mockMvc.perform(delete("/admin/pages/site-navigations/"+nav.getNavigationId())
                .header("Authorization",superAdmin))
                .andExpect(status().is(200));

    }
    /**
     * 导航栏上下移
     * @throws Exception
     */
    @Test
    public void testSort() throws Exception {

        ErrorMessage error  = new ErrorMessage("953","导航栏不存在，请正确操作");
        mockMvc.perform(put("/admin/pages/site-navigations/-1/up")
                .header("Authorization",superAdmin))
                .andExpect(status().is(500))
                .andExpect(objectEquals( error ));
        //排序关键字不正确
        SiteNavigation nav = add();
        error  = new ErrorMessage("004","不正确的状态 , 应该是 'up', 'down'其中之一");
        mockMvc.perform(put("/admin/pages/site-navigations/"+nav.getNavigationId()+"/up1")
                .header("Authorization",superAdmin))
                .andExpect(status().is(400))
                .andExpect(objectEquals( error ));
        SiteNavigation nav1 = add();
        //正确删除
        mockMvc.perform(put("/admin/pages/site-navigations/"+nav1.getNavigationId()+"/up")
                .header("Authorization",superAdmin))
                .andExpect(status().is(200));

    }

    /**
     * 正确添加一个导航栏
     * @return
     * @throws Exception
     */
    private SiteNavigation add()  throws Exception {

        //正确添加
        String res = mockMvc.perform(post("/admin/pages/site-navigations")
                .header("Authorization",superAdmin)
                .param("navigation_name","积分商城")
                .param("url","goods.html")
                .param("client_type","PC"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        return JsonUtil.jsonToObject(res,SiteNavigation.class);
    }


}
