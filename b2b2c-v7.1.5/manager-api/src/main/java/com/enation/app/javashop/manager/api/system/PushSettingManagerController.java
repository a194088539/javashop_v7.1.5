package com.enation.app.javashop.manager.api.system;

import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.base.service.SettingManager;
import com.enation.app.javashop.core.system.model.vo.AppPushSetting;
import com.enation.app.javashop.framework.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 推送设置api
 *
 * @author zh
 * @version v7.0
 * @date 18/5/18 下午6:55
 * @since v7.0
 */
@RestController
@RequestMapping("/admin/systems/push")
@Api(description = "app推送设置")
@Validated
public class PushSettingManagerController {
    @Autowired
    private SettingManager settingManager;


    @GetMapping
    @ApiOperation(value = "获取推送设置", response = AppPushSetting.class)
    public AppPushSetting getPushSetting() {
        String appPushSettingJson = settingManager.get(SettingGroup.PUSH);
        AppPushSetting appPushSetting = JsonUtil.jsonToObject(appPushSettingJson,AppPushSetting.class);
        if (appPushSetting == null) {
            return new AppPushSetting();
        }
        return appPushSetting;
    }

    @PutMapping
    @ApiOperation(value = "修改推送设置", response = AppPushSetting.class)
    public AppPushSetting editPushSetting(@Valid AppPushSetting appPushSetting) {
        settingManager.save(SettingGroup.PUSH, appPushSetting);
        return appPushSetting;
    }

}
