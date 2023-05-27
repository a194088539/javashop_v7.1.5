package com.enation.app.javashop.seller.api.passport;

import com.enation.app.javashop.core.client.system.SmsClient;
import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.core.passport.service.PassportManager;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.Validator;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;


/**
 * 商家验证码处理
 *
 * @author zh
 * @version v7.0
 * @since v7.0
 * 2018年3月23日 上午10:12:12
 */
@RestController
@RequestMapping("/seller/check")
@Api(description = "登录注册必要校验API")
@Validated
public class PassportSellerController {

    @Autowired
    private PassportManager passportManager;
    @Autowired
    private MemberManager memberManager;
    @Autowired
    private SmsClient smsClient;

    @GetMapping(value = "/smscode/{mobile}")
    @ApiOperation(value = "验证手机验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scene", value = "业务类型", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sms_code", value = "验证码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "手机号码", required = true, dataType = "String", paramType = "path"),
    })
    public String checkSmsCode(@NotEmpty(message = "业务场景不能为空") String scene, @PathVariable("mobile") String mobile, @Valid @ApiIgnore @NotEmpty(message = "验证码不能为空") String smsCode) {
        boolean isPass = smsClient.valid(scene, mobile, smsCode);
        if (!isPass) {
            throw new ServiceException(MemberErrorCode.E107.code(), "短信验证码不正确");
        }
        return null;

    }

    @GetMapping("/username/{username}")
    @ApiOperation(value = "用户名重复校验")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "path")
    public String checkUserName(@PathVariable("username") String username) {
        Member member = memberManager.getMemberByName(username);
        Map map = new HashMap(16);
        if (member != null) {
            map.put("exist", true);
            map.put("suggests", memberManager.generateMemberUname(username));
            return JsonUtil.objectToJson(map);
        }
        map.put("exist", false);
        return JsonUtil.objectToJson(map);
    }


    @GetMapping("/mobile/{mobile}")
    @ApiOperation(value = "手机号重复校验")
    @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String", paramType = "path")
    public String checkMobile(@PathVariable("mobile") String mobile) {
        boolean isPass = Validator.isMobile(mobile);
        if (!isPass) {
            throw new ServiceException(MemberErrorCode.E107.code(), "手机号码格式不正确");
        }
        Member member = memberManager.getMemberByMobile(mobile);
        Map map = new HashMap(16);
        if (member != null) {
            map.put("exist", true);
            return JsonUtil.objectToJson(map);
        }
        map.put("exist", false);
        return JsonUtil.objectToJson(map);
    }


    @ApiOperation(value = "刷新token")
    @PostMapping("/token")
    @ApiImplicitParam(name = "refresh_token", value = "刷新token", required = true, dataType = "String", paramType = "query")
    public String refreshToken(@ApiIgnore @NotEmpty(message = "刷新token不能为空") String refreshToken) {
        try {
            return passportManager.exchangeToken(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new ServiceException(MemberErrorCode.E109.code(), "当前token已经失效");
        }
    }
}
