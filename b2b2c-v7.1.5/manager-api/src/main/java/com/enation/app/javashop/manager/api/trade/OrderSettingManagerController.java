package com.enation.app.javashop.manager.api.trade;

import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.client.system.SettingClient;
import com.enation.app.javashop.core.trade.order.model.vo.OrderSettingVO;
import com.enation.app.javashop.framework.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 订单设置相关API
 * @author Snow create in 2018/7/13
 * @version v2.0
 * @since v7.0.0
 */
@Api(description = "订单设置相关API")
@RestController
@RequestMapping("/admin/trade/orders")
@Validated
public class OrderSettingManagerController {


    @Autowired
    private SettingClient settingClient;

    @GetMapping("/setting")
    @ApiOperation(value = "获取订单任务设置信息")
    public OrderSettingVO getOrderSetting(){

        String json = this.settingClient.get(SettingGroup.TRADE);

        return JsonUtil.jsonToObject(json,OrderSettingVO.class);
    }

    @PostMapping("/setting")
    @ApiOperation(value = "保存订单任务设置信息")
    public OrderSettingVO  save(@Valid OrderSettingVO setting){

        System.out.println(setting.toString());
        this.settingClient.save(SettingGroup.TRADE,setting);
        return setting;
    }

}
