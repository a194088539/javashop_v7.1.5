package com.enation.app.javashop.manager.api.system;

import com.enation.app.javashop.core.system.model.dto.FormItem;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import com.enation.app.javashop.framework.database.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.enation.app.javashop.core.system.model.dos.LogisticsCompanyDO;
import com.enation.app.javashop.core.system.service.LogisticsCompanyManager;
import java.util.List;

/**
 * 物流公司控制器
 *
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-29 15:10:38
 */
@RestController
@RequestMapping("/admin/systems/logi-companies")
@Api(description = "物流公司相关API")
@Validated
public class LogisticsCompanyManagerController {

    @Autowired
    private LogisticsCompanyManager logisticsCompanyManager;


    @ApiOperation(value = "查询物流公司列表", response = LogisticsCompanyDO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "每页显示数量", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "物流公司名称", dataType = "String", paramType = "query")
    })
    @GetMapping
    public Page list(@ApiIgnore @NotNull(message = "页码不能为空") Integer pageNo, @ApiIgnore @NotNull(message = "每页数量不能为空") Integer pageSize, @ApiIgnore String name) {

        return this.logisticsCompanyManager.list(pageNo, pageSize, name);
    }


    @ApiOperation(value = "添加物流公司", response = LogisticsCompanyDO.class)
    @PostMapping
    public LogisticsCompanyDO add(@Valid LogisticsCompanyDO logi,@RequestBody @Valid List<FormItem> formItems) {

        logi.setForm(formItems);
        this.logisticsCompanyManager.add(logi);

        return logi;
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "修改物流公司", response = LogisticsCompanyDO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int", paramType = "path")
    })
    public LogisticsCompanyDO edit(@Valid LogisticsCompanyDO logi, @PathVariable("id") Integer id,@RequestBody @Valid List<FormItem> formItems) {

        logi.setForm(formItems);
        this.logisticsCompanyManager.edit(logi, id);

        return logi;
    }


    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除物流公司")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要删除的物流公司主键", required = true, dataType = "int", paramType = "path")
    })
    public String delete(@PathVariable("id") Integer id) {
        this.logisticsCompanyManager.delete(id);

        return "";
    }


    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查询一个物流公司")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要查询的物流公司主键", required = true, dataType = "int", paramType = "path")
    })
    public LogisticsCompanyDO get(@PathVariable("id") Integer id) {
        LogisticsCompanyDO logi = this.logisticsCompanyManager.getModel(id);
        return logi;
    }

    @PostMapping(value = "/{id}")
    @ApiOperation(value = "开启或禁用物流公司")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "物流公司主键ID", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "disabled", value = "状态 OPEN：开启，CLOSE：禁用", required = true, dataType = "String", paramType = "path", allowableValues = "OPEN,CLOSE")
    })
    public String openClose(@PathVariable("id") Integer id, @ApiIgnore String disabled) {
        this.logisticsCompanyManager.openCloseLogi(id, disabled);

        return "";
    }

}
