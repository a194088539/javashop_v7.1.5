package com.enation.app.javashop.seller.security;

import com.enation.app.javashop.framework.security.AbstractAuthenticationService;
import com.enation.app.javashop.framework.security.model.*;
import com.enation.app.javashop.framework.util.StringUtil;
import com.enation.app.javashop.framework.util.TokenKeyGenerate;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * jwt token 鉴权管理
 * <p>
 * Created by zh on 2018/3/12.
 *
 * @author zh
 * @version 1.0
 * @since 7.0.0
 * 2018/3/12
 */
@SuppressWarnings("AlibabaUndefineMagicConstant")
@Component
public class SellerAuthenticationService extends AbstractAuthenticationService {


    /**
     * 通过claim生成 系统的用户
     *
     * @param claims jwt的claim
     * @return 系统的用户
     */
    private static User buildUser(Claims claims) {
        String subject = claims.getSubject();
        Integer uid = claims.get("uid", Integer.class);
        String username = claims.get("username", String.class);

        if (Role.CLERK.name().equals(subject)) {
            Integer clerkId = claims.get("clerkId", Integer.class);
            String clerkName = claims.get("clerkName", String.class);
            Integer founder = claims.get("founder", Integer.class);
            String role = claims.get("role", String.class);
            Integer sellerId = claims.get("sellerId", Integer.class);
            String sellerName = claims.get("sellerName", String.class);
            Integer selfOperated = claims.get("selfOperated", Integer.class);
            Clerk clerk = new Clerk();
            clerk.setClerkId(clerkId);
            clerk.setClerkName(clerkName);
            clerk.setFounder(founder);
            clerk.setRole(role);
            clerk.setUid(uid);
            clerk.setUsername(username);
            clerk.setSellerName(sellerName);
            clerk.setSellerId(sellerId);
            clerk.setSelfOperated(selfOperated);
            return clerk;
        }
        if (Role.BUYER.name().equals(subject)) {
            Buyer buyer = new Buyer();
            buyer.setUid(uid);
            buyer.setUsername(username);
            return buyer;
        }
        return null;
    }


    @Override
    protected Authentication getAuthentication(String token, String uuid) {
        if (token != null) {
            // parse the token.
            try {
                Claims claims
                        = Jwts.parser()
                        .setSigningKey(JWTConstant.SECRET)
                        .parseClaimsJws(token).getBody();
                User user = buildUser(claims);
                user.setUuid(uuid);
                //用户名
                String clerkName = claims.get("clerkName", String.class);

                List<GrantedAuthority> auths = new ArrayList<>();

                String role = StringUtil.toString(claims.get("role"));

                //如果权限为空证明是普通会员登录
                if (StringUtil.isEmpty(role)) {
                    auths.add(new SimpleGrantedAuthority("ROLE_BUYER"));
                } else {
                    auths.add(new SimpleGrantedAuthority("ROLE_SELLER_" + role));
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(clerkName, null, auths);

                authentication.setDetails(user);

                return authentication;
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error(e);
                }
            }


        }

        return null;

    }

    @Override
    protected String getRealRedisToken(String uuid, Integer uid) {

        return StringUtil.toString(cache.get(TokenKeyGenerate.generateBuyerAccessToken(uuid, uid)));
    }
}
