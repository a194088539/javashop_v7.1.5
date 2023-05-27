package com.enation.app.javashop.buyer.api.passport;

import com.enation.app.javashop.core.distribution.service.ShortUrlManager;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.plugin.wechat.WechatConnectLoginPlugin;
import com.enation.app.javashop.core.member.service.ConnectManager;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.core.member.service.impl.ConnectManagerImpl;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.util.AESUtil;
import com.enation.app.javashop.framework.validation.annotation.Mobile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Pattern;
import java.util.Map;

/**
 * @author fk
 * @version v2.0
 * @Description: 小程序登录接口
 * @date 2018/11/20 14:56
 * @since v7.0.0
 */
@RestController
@RequestMapping("/passport/mini-program")
@Api(description = "小程序登录api")
@Validated
public class PassportMiniProgramBuyerController {

    @Autowired
    public WechatConnectLoginPlugin wechatConnectLoginPlugin;

    @Autowired
    public ConnectManager connectManager;

    @Autowired
    private MemberManager memberManager;

    @Autowired
    private Cache cache;


    @GetMapping("/auto-login")
    @ApiOperation(value = "小程序自动登录")
    public Map autoLogin(String code, String uuid) {

        //获取sessionkey和openid或者unionid
        String content = wechatConnectLoginPlugin.miniProgramAutoLogin(code);

        return this.connectManager.miniProgramLogin(content, uuid);
    }

    @GetMapping("/decrypt")
    @ApiOperation(value = "加密数据解密验证")
    public Map decrypt(String code, String encryptedData, String uuid, String iv) {

        return connectManager.decrypt(code, encryptedData, uuid, iv);
    }


    @GetMapping("/code-unlimit")
    @ApiOperation(value = "获取微信小程序码")
    @ApiImplicitParam(name = "goods_id", value = "商品id", required = true, dataType = "int", paramType = "query")
    public String getWXACodeUnlimit(@ApiIgnore Integer goodsId) {

        String accessTocken = wechatConnectLoginPlugin.getWXAccessToken();

        return connectManager.getWXACodeUnlimit(accessTocken, goodsId);
    }


    @PostMapping("/distribution")
    @ApiOperation(value = "存储小程序端分销的上级id")
    @ApiImplicitParam(name = "from", value = "上级会员id加密格式", required = true, dataType = "String", paramType = "query")
    public String distribution(String from, @RequestHeader(required = false) String uuid) {

        if (uuid != null) {

            try {
                cache.put(ShortUrlManager.PREFIX + uuid, AESUtil.decrypt(from, ConnectManagerImpl.key));
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        return "";
    }


    @ApiOperation(value = "小程序注册绑定")
    @PostMapping("/register-bind/{uuid}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "唯一标识", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "nick_name", value = "昵称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "face", value = "头像", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sex", value = "性别", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "手机号码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query"),

    })
    public Map binder(@PathVariable("uuid") String uuid, @Length(max = 20, message = "昵称超过长度限制") @ApiIgnore String nickName, String face, Integer sex, @Mobile String mobile, @Pattern(regexp = "[a-fA-F0-9]{32}", message = "密码格式不正确") String password) {
        //执行注册
        Member member = new Member();
        member.setUname("m_" + mobile);
        member.setMobile(mobile);
        member.setPassword(password);
        member.setNickname(nickName);
        member.setFace(face);
        member.setSex(sex);
        memberManager.register(member);
        //执行绑定账号
        Map map = connectManager.mobileBind(mobile, uuid);
        return map;
    }


}
