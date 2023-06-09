package com.enation.app.javashop.seller.api.passport;

import com.enation.app.javashop.core.base.SceneType;
import com.enation.app.javashop.core.client.system.CaptchaClient;
import com.enation.app.javashop.core.client.system.SmsClient;
import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.member.model.vo.MemberVO;
import com.enation.app.javashop.core.member.model.vo.SellerInfoVO;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.core.passport.service.PassportManager;
import com.enation.app.javashop.core.shop.model.dos.Clerk;
import com.enation.app.javashop.core.shop.service.ClerkManager;
import com.enation.app.javashop.framework.JavashopConfig;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotEmpty;


/**
 * 会员登录注册API
 *
 * @author zh
 * @version v7.0
 * @since v7.0
 * 2018年3月23日 上午10:12:12
 */
@RestController
@RequestMapping("/seller/login")
@Api(description = "商家登录API")
@Validated
public class PassportLoginSellerController {

    @Autowired
    private PassportManager passportManager;
    @Autowired
    private CaptchaClient captchaClient;
    @Autowired
    private MemberManager memberManager;
    @Autowired
    private ClerkManager clerkManager;
    @Autowired
    private SmsClient smsClient;
    @Autowired
    private JavashopConfig javashopConfig;

    @PostMapping(value = "/smscode/{mobile}")
    @ApiOperation(value = "发送验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "uuid客户端的唯一标识", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "captcha", value = "图片验证码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "手机号码", required = true, dataType = "String", paramType = "path"),
    })
    public String sendSmsCode(@NotEmpty(message = "uuid不能为空") String uuid, @NotEmpty(message = "图片验证码不能为空") String captcha, @PathVariable("mobile") String mobile) {
        boolean isPass = captchaClient.valid(uuid, captcha, SceneType.LOGIN.name());
        if (!isPass) {
            throw new ServiceException(MemberErrorCode.E107.code(), "图片验证码不正确");
        }
        passportManager.sendLoginSmsCode(mobile);
        //清清除图片验证码信息
        captchaClient.deleteCode(uuid, captcha, SceneType.LOGIN.name());
        return javashopConfig.getSmscodeTimout() / 60 + "";
    }

    @GetMapping()
    @ApiOperation(value = "用户名（手机号）/密码登录API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "captcha", value = "验证码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "uuid", value = "客户端唯一标识", required = true, dataType = "String", paramType = "query"),
    })
    public MemberVO login(@NotEmpty(message = "用户名不能为空") String username, @NotEmpty(message = "密码不能为空") String password, @NotEmpty(message = "图片验证码不能为空") String captcha, @NotEmpty(message = "uuid不能为空") String uuid) {
        //验证图片验证码是否正确
        boolean isPass = captchaClient.valid(uuid, captcha, SceneType.LOGIN.name());
        if (!isPass) {
            throw new ServiceException(MemberErrorCode.E107.code(), "图片验证码错误");
        }
        SellerInfoVO sellerInfoVO = new SellerInfoVO();
        //返回会员信息
        MemberVO memberVO = memberManager.login(username, password, 2);
        //查询店员
        BeanUtil.copyProperties(memberVO, sellerInfoVO);
        Clerk clerk = clerkManager.getClerkByMemberId(memberVO.getUid());
        if (clerk != null) {
            sellerInfoVO.setRoleId(clerk.getRoleId());
            sellerInfoVO.setFounder(clerk.getFounder());
            return sellerInfoVO;
        }
        return memberVO;

    }

    @GetMapping("/login/{mobile}")
    @ApiOperation(value = "手机号码登录API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "sms_code", value = "手机验证码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "uuid", value = "客户端唯一标识", required = true, dataType = "String", paramType = "query")
    })
    public MemberVO mobileLogin(@PathVariable String mobile, @ApiIgnore @NotEmpty(message = "短信验证码不能为空") String smsCode) {
        boolean isPass = smsClient.valid(SceneType.LOGIN.name(), mobile, smsCode);
        if (!isPass) {
            throw new ServiceException(MemberErrorCode.E107.code(), "短信验证码错误");
        }
        return memberManager.mobileLogin(mobile, 2);

    }
}
