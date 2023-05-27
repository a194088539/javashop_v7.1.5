package com.enation.app.javashop.buyer.api.member;

import com.enation.app.javashop.core.member.model.dos.MemberZpzzDO;
import com.enation.app.javashop.core.member.service.MemberZpzzManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/members/zpzz")
@Validated
public class MemberZpzzBuyerController {

    @Autowired
    private MemberZpzzManager memberZpzzManager;

    @ApiOperation(value = "会员增票资质申请")
    @PostMapping
    public MemberZpzzDO add(@Valid  MemberZpzzDO memberZpzzDO) {

        return memberZpzzManager.add(memberZpzzDO);
    }

    @ApiOperation(value = "会员修改增票资质申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int", paramType = "path")
    })
    @PutMapping(value = "/{id}")
    public MemberZpzzDO edit(@Valid MemberZpzzDO memberZpzzDO, @PathVariable Integer id) {

        return memberZpzzManager.edit(memberZpzzDO, id);
    }

    @ApiOperation(value = "查询会员增票资质详细")
    @GetMapping(value = "/detail")
    public MemberZpzzDO get() {
        return this.memberZpzzManager.get();
    }

    @ApiOperation(value = "删除会员增票资质信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键ID", required = true, dataType = "int", paramType = "path")
    })
    @DeleteMapping(value = "/{id}")
    public String delete(@PathVariable Integer id) {
        this.memberZpzzManager.delete(id);
        return "";
    }
}
