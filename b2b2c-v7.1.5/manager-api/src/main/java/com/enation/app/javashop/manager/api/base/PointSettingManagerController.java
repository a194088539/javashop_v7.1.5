package com.enation.app.javashop.manager.api.base;

import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.client.system.SettingClient;
import com.enation.app.javashop.core.system.model.vo.PointSetting;
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
 * 积分设置api
 *
 * @author zh
 * @version v7.0
 * @date 18/5/18 下午6:55
 * @since v7.0
 */
@RestController
@RequestMapping("/admin/settings")
@Api(description = "积分设置")
@Validated
public class PointSettingManagerController {
    @Autowired
    private SettingClient settingClient;


    @GetMapping(value = "/point")
    @ApiOperation(value = "获取积分设置", response = PointSetting.class)
    public PointSetting getPointSetting() {
        String pointSettingJson = settingClient.get( SettingGroup.POINT);
        PointSetting pointSetting = JsonUtil.jsonToObject(pointSettingJson,PointSetting.class);
        if (pointSetting == null) {
            return new PointSetting();
        }
        return pointSetting;
    }

    @PutMapping(value = "/point")
    @ApiOperation(value = "修改积分设置", response = PointSetting.class)
    public PointSetting editPointSetting(@Valid PointSetting pointSetting) {
        settingClient.save(SettingGroup.POINT, pointSetting);
        return pointSetting;
    }

}
