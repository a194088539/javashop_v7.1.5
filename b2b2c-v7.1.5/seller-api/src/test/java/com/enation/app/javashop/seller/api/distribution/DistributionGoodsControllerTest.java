package com.enation.app.javashop.seller.api.distribution;

import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.base.model.vo.SuccessMessage;
import com.enation.app.javashop.core.client.distribution.DistributionGoodsClient;
import com.enation.app.javashop.core.client.system.SettingClient;
import com.enation.app.javashop.core.distribution.model.dos.DistributionGoods;
import com.enation.app.javashop.core.distribution.model.dos.DistributionSetting;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 分销商品单元测试
 * @author liushuai
 * @version v1.0
 * @since v7.0
 * 2018/8/30 上午9:51
 * @Description:
 *
 */
public class DistributionGoodsControllerTest extends BaseTest {



    @Test
    public void test() throws  Exception{
        //获取设置
        mockMvc.perform(get("/seller/distribution/setting")
                .header("Authorization", seller2))
                .andExpect(status().is(200));
        //获取商品分销设置
        mockMvc.perform(get("/seller/distribution/goods/1234")
                .header("Authorization", seller2))
                .andExpect(status().is(200));
        //获取商品分销设置
        mockMvc.perform(put("/seller/distribution/goods")
                .header("Authorization", seller2))
                .andExpect(status().is(200));
    }



}
