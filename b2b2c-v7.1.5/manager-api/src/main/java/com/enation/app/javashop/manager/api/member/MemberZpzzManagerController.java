package com.enation.app.javashop.manager.api.member;

import com.enation.app.javashop.core.member.model.dos.MemberZpzzDO;
import com.enation.app.javashop.core.member.model.dto.ZpzzQueryParam;
import com.enation.app.javashop.core.member.service.MemberZpzzManager;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * 会员增票资质API
 *
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-09-16
 */
@Api(description = "会员增票资质API")
@RestController
@RequestMapping("/admin/members/zpzz")
@Validated
public class MemberZpzzManagerController {

    @Autowired
    private MemberZpzzManager memberZpzzManager;

    @ApiOperation(value = "查询会员增票资质信息列表", response = MemberZpzzDO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页码", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "每页显示数量", dataType = "int", paramType = "query")
    })
    @GetMapping
    public Page list(@Valid ZpzzQueryParam zpzzQueryParam, @ApiIgnore Integer pageNo,
                     @ApiIgnore Integer pageSize) {
        return this.memberZpzzManager.list(pageNo, pageSize, zpzzQueryParam);
    }

    @ApiOperation(value = "查询会员增票资质详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键ID", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping(value = "/{id}")
    public MemberZpzzDO get(@PathVariable Integer id) {
        return this.memberZpzzManager.get(id);
    }

    @ApiOperation(value = "平台审核会员增票资质申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键ID", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "status", value = "审核状态", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "remark", value = "审核备注", required = false, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "audit/{id}/{status}")
    public String audit(@PathVariable Integer id, @PathVariable String status, String remark) {
        this.memberZpzzManager.audit(id, status, remark);
        return "";
    }
}
