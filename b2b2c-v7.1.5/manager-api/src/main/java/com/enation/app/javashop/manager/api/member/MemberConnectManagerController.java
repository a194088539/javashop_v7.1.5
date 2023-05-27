package com.enation.app.javashop.manager.api.member;

import com.enation.app.javashop.core.member.model.dos.ConnectSettingDO;
import com.enation.app.javashop.core.member.model.dto.ConnectSettingDTO;
import com.enation.app.javashop.core.member.model.vo.ConnectSettingVO;
import com.enation.app.javashop.core.member.service.ConnectManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zjp
 * @version v7.0
 * @Description 信任登录设置
 * @ClassName ConnectController
 * @since v7.0 下午4:23 2018/6/15
 */
@Api(description="信任登录设置api")
@RestController
@RequestMapping("/admin/members/connect")
public class MemberConnectManagerController {

    @Autowired
    private ConnectManager connectManager;



    @GetMapping()
    @ApiOperation(value = "获取信任登录配置参数",response = ConnectSettingDO.class)
    public List<ConnectSettingVO> list(){
        return  connectManager.list();
    }

    @PutMapping(value = "/{type}")
    @ApiOperation(value = "修改信任登录参数", response = ConnectSettingDTO.class)
    @ApiImplicitParam(name = "type", value = "用户名", required = true, dataType = "String", paramType = "path",allowableValues = "QQ,ALIPAY,WEIBO,WECHAT")
    public ConnectSettingDTO editConnectSetting(@RequestBody ConnectSettingDTO connectSettingDTO, @PathVariable("type")  String type) {
        connectManager.save(connectSettingDTO);
        return connectSettingDTO;
    }


}
