package com.enation.app.javashop.manager.api.pagedata;

import com.enation.app.javashop.core.pagedata.constraint.annotation.ClientAppType;
import com.enation.app.javashop.core.pagedata.constraint.annotation.PageType;
import com.enation.app.javashop.core.pagedata.model.PageData;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import com.enation.app.javashop.core.pagedata.service.PageDataManager;

/**
 * 楼层控制器
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-05-21 16:39:22
 */
@RestController
@RequestMapping("/admin/pages")
@Api(description = "楼层相关API")
@Validated
public class PageDataManagerController {

    @Autowired
    private PageDataManager pageManager;


    @PutMapping(value = "/{page_id}")
    @ApiOperation(value = "修改楼层", response = PageData.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_id", value = "主键", required = true, dataType = "int", paramType = "path")
    })
    public PageData edit(@Valid PageData page, @PathVariable("page_id") Integer pageId) {

        this.pageManager.edit(page, pageId);

        return page;
    }

    @GetMapping(value = "/{page_id}")
    @ApiOperation(value = "查询一个楼层")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_id", value = "要查询的楼层主键", required = true, dataType = "int", paramType = "path")
    })
    public PageData get(@PathVariable("page_id") Integer pageId) {

        PageData page = this.pageManager.getModel(pageId);

        return page;
    }


    @PutMapping(value = "/{client_type}/{page_type}")
    @ApiOperation(value = "使用客户端类型和页面类型修改楼层", response = PageData.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "client_type", value = "要查询的客户端类型 APP/WAP/PC", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "page_type", value = "要查询的页面类型 INDEX 首页/SPECIAL 专题", required = true, dataType = "string", paramType = "path")
    })
    public PageData editByType(@Valid PageData pageData, @ClientAppType @PathVariable("client_type") String clientType, @PageType @PathVariable("page_type") String pageType) {

        pageData.setClientType(clientType);
        pageData.setPageType(pageType);
        this.pageManager.editByType(pageData);

        return pageData;
    }

    @GetMapping(value = "/{client_type}/{page_type}")
    @ApiOperation(value = "使用客户端类型和页面类型查询一个楼层")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "client_type", value = "要查询的客户端类型 APP/WAP/PC", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "page_type", value = "要查询的页面类型 INDEX 首页/SPECIAL 专题", required = true, dataType = "string", paramType = "path")
    })
    public PageData getByType(@ClientAppType @PathVariable("client_type") String clientType,@PageType @PathVariable("page_type") String pageType) {

        PageData page = this.pageManager.getByType(clientType,pageType);

        return page;
    }

}
