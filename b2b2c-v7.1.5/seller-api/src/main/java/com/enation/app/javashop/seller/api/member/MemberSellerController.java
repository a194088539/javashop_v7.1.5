package com.enation.app.javashop.seller.api.member;


import com.enation.app.javashop.core.member.service.MemberManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;


/**
 * 会员控制器
 *
 * @author zh
 * @version v2.0
 * @since v7.0.0
 * 2018-03-16 11:33:56
 */
@RestController
@RequestMapping("/seller/members")
@Api(description = "会员相关API")
public class MemberSellerController {

    @Autowired
    private MemberManager memberManager;



    @ApiOperation(value = "注销会员登录")
    @PostMapping(value = "/logout")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "会员id", dataType = "int", paramType = "query", required = true)
    })
    public String loginOut(@NotNull(message = "会员id不能为空") Integer uid) {
        this.memberManager.logout(uid);
        return null;
    }


}