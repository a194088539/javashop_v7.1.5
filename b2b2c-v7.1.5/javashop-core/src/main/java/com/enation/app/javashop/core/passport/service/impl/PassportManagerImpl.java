package com.enation.app.javashop.core.passport.service.impl;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.base.SceneType;
import com.enation.app.javashop.core.client.member.ShopClient;
import com.enation.app.javashop.core.client.system.SmsClient;
import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.framework.util.*;
import com.enation.app.javashop.core.passport.service.PassportManager;
import com.enation.app.javashop.core.shop.model.dos.ShopRole;
import com.enation.app.javashop.core.shop.model.vo.ShopVO;
import com.enation.app.javashop.core.shop.service.ClerkManager;
import com.enation.app.javashop.core.shop.service.ShopRoleManager;
import com.enation.app.javashop.framework.JavashopConfig;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.ThreadContextHolder;
import com.enation.app.javashop.framework.exception.ResourceNotFoundException;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 会员账号管理实现
 *
 * @author zh
 * @version v7.0
 * @since v7.0
 * 2018-04-8 11:33:56
 */
@Service
public class PassportManagerImpl implements PassportManager {

    @Autowired
    private SmsClient smsClient;
    @Autowired
    private Cache cache;
    @Autowired
    private MemberManager memberManager;
    @Autowired
    private PassportManager passportManager;
    @Autowired
    private ShopClient shopClient;
    @Autowired
    private JavashopConfig javashopConfig;
    @Autowired
    private ShopRoleManager shopRoleManager;
    @Autowired
    private ClerkManager clerkManager;


    @Override
    public void sendSmsCode(String mobile) {
        if (!Validator.isMobile(mobile)) {
            throw new ServiceException(MemberErrorCode.E107.code(), "手机号码格式不正确");
        }
        //发送验证码短信
        smsClient.sendSmsMessage("添加店员", mobile, SceneType.ADD_CLERK);
    }

    @Override
    public void sendRegisterSmsCode(String mobile) {
        if (!Validator.isMobile(mobile)) {
            throw new ServiceException(MemberErrorCode.E107.code(), "手机号码格式不正确！");
        }
        //校验会员是否存在
        Member member = memberManager.getMemberByMobile(mobile);
        if (member != null) {
            throw new ServiceException(MemberErrorCode.E107.code(), "该手机号已经被占用！");
        }
        //发送验证码短信
        smsClient.sendSmsMessage("注册", mobile, SceneType.REGISTER);
    }

    @Override
    public void sendLoginSmsCode(String mobile) {
        if (!Validator.isMobile(mobile)) {
            throw new ServiceException(MemberErrorCode.E107.code(), "手机号码格式不正确！");
        }
        //校验会v员是否存在
        Member member = memberManager.getMemberByMobile(mobile);
        if (member == null) {
            throw new ServiceException(MemberErrorCode.E107.code(), "该手机号未注册！");
        }
        //发送验证码短信
        smsClient.sendSmsMessage("登录", mobile, SceneType.LOGIN);
    }


    @Override
    public String createToken(Member member, int time) {
        ObjectMapper oMapper = new ObjectMapper();
        String token = null;
        //获取店员信息
        com.enation.app.javashop.core.shop.model.dos.Clerk clerkDb = clerkManager.getClerkByMemberId(member.getMemberId());
        //如果店员不为空，则说明他是店铺管理员，需要赋值商家权限
        if (clerkDb != null) {
            Clerk clerk = new Clerk();
            ShopVO shopVO = shopClient.getShop(clerkDb.getShopId());
            clerk.setSellerName(shopVO.getShopName());
            clerk.setSellerId(shopVO.getShopId());
            clerk.setUsername(member.getUname());
            clerk.setUid(member.getMemberId());
            clerk.setUsername(member.getUname());
            clerk.setClerkName(clerkDb.getClerkName());
            clerk.setClerkId(clerkDb.getClerkId());
            clerk.setSelfOperated(shopVO.getSelfOperated());
            //如果是超级店员则赋值超级店员的权限，否则去查询权限赋值
            if (clerkDb.getFounder().equals(1)) {
                clerk.setRole("SUPER_SELLER");
            } else {
                ShopRole shopRole = this.shopRoleManager.getModel(clerkDb.getRoleId());
                clerk.setRole(shopRole.getRoleName());
            }
            Map clerkMap = oMapper.convertValue(clerk, HashMap.class);
            token = Jwts.builder()
                    .setClaims(clerkMap)
                    .setSubject(Role.CLERK.name())
                    .setExpiration(new Date(System.currentTimeMillis() + time * 1000))
                    .signWith(SignatureAlgorithm.HS512, JWTConstant.SECRET)
                    .compact();
            return token;
        } else {
            //如果是会员，则赋值买家权限
            Buyer buyer = new Buyer();
            buyer.setUid(member.getMemberId());
            buyer.setUsername(member.getUname());
            Map buyerMap = oMapper.convertValue(buyer, HashMap.class);
            token = Jwts.builder()
                    .setClaims(buyerMap)
                    .setSubject(Role.BUYER.name())
                    .setExpiration(new Date(System.currentTimeMillis() + time * 1000))
                    .signWith(SignatureAlgorithm.HS512, JWTConstant.SECRET)
                    .compact();
            return token;
        }

    }

    @Override
    public String exchangeToken(String refreshToken) throws ExpiredJwtException {
        if (refreshToken != null) {
            Claims claims
                    = Jwts.parser()
                    .setSigningKey(JWTConstant.SECRET)
                    .parseClaimsJws(refreshToken).getBody();
            Integer uid = claims.get("uid", Integer.class);
            //uuid
            String uuid = ThreadContextHolder.getHttpRequest().getHeader("uuid");
            //根据uid获取用户,获得当前会员是buyer还是seller
            Member member = this.memberManager.getModel(uid);
            //如果会员token刷新时，会员已经失效，则不颁发新的token
            if(member.getDisabled()==-1){
                throw new ServiceException(MemberErrorCode.E109.code(), "当前token已经失效");
            }

            //从缓存中获取refreshToken
            String key = TokenKeyGenerate.generateBuyerRefreshToken(uuid,uid);
            String token = StringUtil.toString(cache.get(key));
            if (StringUtil.isEmpty(token)) {
                throw new ServiceException(MemberErrorCode.E110.code(), "当前会员已经退出");
            }
            //判断是否过期
            long tokenDate = DateUtil.getDateline(claims.getExpiration().toString(),"EEE MMM dd HH:mm:ss z yyyy");
            long currTime = DateUtil.getDateline();
            if (currTime > tokenDate) {
                throw new ServiceException(MemberErrorCode.E109.code(), "当前token已经失效");
            }
            if (member != null) {
                //判断权限
                String newAccessToken = passportManager.createToken(member, javashopConfig.getAccessTokenTimeout());
                String newRefreshToken = passportManager.createToken(member, javashopConfig.getRefreshTokenTimeout());
                cache.put(TokenKeyGenerate.generateBuyerRefreshToken(uuid,uid), newRefreshToken, javashopConfig.getRefreshTokenTimeout() + 60);
                cache.put(TokenKeyGenerate.generateBuyerAccessToken(uuid,uid), newAccessToken, javashopConfig.getAccessTokenTimeout() + 60);
                cache.put(UserTokenPrefix.MEMBER_DISABLED.getPrefix() + member.getMemberId(), member.getDisabled(), javashopConfig.getAccessTokenTimeout() + 60);
                Map map = new HashMap(16);
                map.put("accessToken", newAccessToken);
                map.put("refreshToken", newRefreshToken);
                return JsonUtil.objectToJson(map);
            }
            throw new ResourceNotFoundException("当前会员不存在");
        }
        throw new ResourceNotFoundException("当前会员不存在");
    }

    @Override
    public void sendFindPasswordCode(String mobile) {
        //校验会员是否存在
        Member member = memberManager.getMemberByMobile(mobile);
        if (member == null) {
            throw new ServiceException(MemberErrorCode.E107.code(), "该手机号未注册");
        }
        smsClient.sendSmsMessage("找回密码", mobile, SceneType.VALIDATE_MOBILE);
    }

    @Override
    public void clearSign(String mobile, String scene) {
        cache.remove(CachePrefix.MOBILE_VALIDATE.getPrefix() + "_" + scene + "_" + mobile);
    }
}

