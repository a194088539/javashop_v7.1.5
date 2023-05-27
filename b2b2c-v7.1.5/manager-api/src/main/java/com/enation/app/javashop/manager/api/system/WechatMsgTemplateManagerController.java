package com.enation.app.javashop.manager.api.system;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.enation.app.javashop.framework.database.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import com.enation.app.javashop.core.system.model.dos.WechatMsgTemplate;
import com.enation.app.javashop.core.system.service.WechatMsgTemplateManager;

/**
 * 微信服务消息模板控制器
 *
 * @author fk
 * @version v7.1.4
 * @since vv7.1.0
 * 2019-06-14 16:42:35
 */
@RestController
@RequestMapping("/admin/systems/wechat-msg-tmp")
@Api(description = "微信服务消息模板相关API")
public class WechatMsgTemplateManagerController {

    @Autowired
    private WechatMsgTemplateManager wechatMsgTemplateManager;


    @ApiOperation(value = "查询微信服务消息模板列表", response = WechatMsgTemplate.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "每页显示数量", required = true, dataType = "int", paramType = "query")
    })
    @GetMapping
    public Page list(@ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize) {

        return this.wechatMsgTemplateManager.list(pageNo, pageSize);
    }

    @ApiOperation(value = "查询微信服务消息模板是否已经同步")
    @GetMapping("/sync")
    public boolean sync(){

        return wechatMsgTemplateManager.isSycn();
    }

    @ApiOperation(value = "同步微信服务消息模板")
    @PostMapping("/sync")
    public String syncMsgTmp(){

        try {
            wechatMsgTemplateManager.sycn();
        } catch (Exception e) {
            return e.getMessage();
        }

        return "";
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查询一个微信服务消息模板")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要查询的微信服务消息模板主键", required = true, dataType = "int", paramType = "path")
    })
    public WechatMsgTemplate get(@PathVariable Integer id) {

        WechatMsgTemplate wechatMsgTemplate = this.wechatMsgTemplateManager.getModel(id);

        return wechatMsgTemplate;
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "修改微信服务消息模板", response = WechatMsgTemplate.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int", paramType = "path")
    })
    public WechatMsgTemplate edit(@Valid WechatMsgTemplate wechatMsgTemplate, @PathVariable Integer id) {

        this.wechatMsgTemplateManager.edit(wechatMsgTemplate, id);

        return wechatMsgTemplate;
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除微信服务消息模板")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要删除的微信服务消息模板主键", required = true, dataType = "int", paramType = "path")
    })
    public String delete(@PathVariable Integer id) {

        this.wechatMsgTemplateManager.delete(id);

        return "";
    }




}