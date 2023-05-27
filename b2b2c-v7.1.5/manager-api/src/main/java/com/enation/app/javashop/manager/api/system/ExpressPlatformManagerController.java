package com.enation.app.javashop.manager.api.system;

import com.enation.app.javashop.core.system.model.dos.ExpressPlatformDO;
import com.enation.app.javashop.core.system.model.dos.SmsPlatformDO;
import com.enation.app.javashop.core.system.model.vo.ExpressDetailVO;
import com.enation.app.javashop.core.system.model.vo.ExpressPlatformVO;
import com.enation.app.javashop.core.system.service.ExpressPlatformManager;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 快递平台控制器
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-07-11 14:42:50
 */
@RestController
@RequestMapping("/admin/systems/express-platforms")
@Api(description = "快递平台相关API")
public class ExpressPlatformManagerController {

    @Autowired
    private ExpressPlatformManager expressPlatformManager;


    @ApiOperation(value = "查询快递平台列表", response = ExpressPlatformDO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "每页显示数量", required = true, dataType = "int", paramType = "query")
    })
    @GetMapping
    public Page list(@ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize) {

        return this.expressPlatformManager.list(pageNo, pageSize);
    }

    @ApiOperation(value = "修改快递平台", response = SmsPlatformDO.class)
    @PutMapping(value = "/{bean}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bean", value = "快递平台bean id", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "express_platform", value = "快递平台对象", required = true, dataType = "ExpressPlatformVO", paramType = "body")
    })
    public ExpressPlatformVO edit(@PathVariable String bean, @RequestBody @ApiIgnore ExpressPlatformVO expressPlatformVO) {
        expressPlatformVO.setBean(bean);
        return this.expressPlatformManager.edit(expressPlatformVO);
    }

    @ApiOperation(value = "获取快递平台的配置", response = String.class)
    @GetMapping("/{bean}")
    @ApiImplicitParam(name = "bean", value = "快递平台bean id", required = true, dataType = "String", paramType = "path")
    public ExpressPlatformVO getUploadSetting(@PathVariable String bean) {
        return this.expressPlatformManager.getExoressConfig(bean);
    }

    @ApiOperation(value = "开启某个快递平台方案", response = String.class)
    @PutMapping("/{bean}/open")
    @ApiImplicitParam(name = "bean", value = "bean", required = true, dataType = "String", paramType = "path")
    public String open(@PathVariable String bean) {
        this.expressPlatformManager.open(bean);
        return null;
    }

    @ApiOperation(value = "查询物流详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "物流公司id", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "num", value = "快递单号", dataType = "String", paramType = "query"),
    })
    @GetMapping("/express")
    public ExpressDetailVO list(@ApiIgnore Integer id, @ApiIgnore String num) {
        return this.expressPlatformManager.getExpressDetail(id, num);
    }

}
