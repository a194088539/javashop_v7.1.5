package com.enation.app.javashop.manager.security;

import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.security.AbstractAuthenticationService;
import com.enation.app.javashop.framework.security.model.*;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import com.enation.app.javashop.framework.util.TokenKeyGenerate;
import com.enation.app.javashop.framework.util.UserTokenPrefix;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * Created by kingapex on 2018/3/12.
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/3/12
 */
@SuppressWarnings("AlibabaUndefineMagicConstant")
@Component
public class AdminAuthenticationService extends AbstractAuthenticationService {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Value("${spring.cloud.config.profile:dev}")
    private String profile;

    /**
     * 根据一个 token 生成授权
     *
     * @param token
     * @return 授权
     */
    @Override
    protected Authentication getAuthentication(String token, String uuid) {
        if (token != null) {
            // parse the token.
            try {
                Claims claims
                        = Jwts.parser()
                        .setSigningKey(JWTConstant.SECRET)
                        .parseClaimsJws(token).getBody();
                Admin admin = buildUser(claims);
                Integer disabled = (Integer) cache.get(UserTokenPrefix.ADMIN_DISABLED.getPrefix() + admin.getUid());
                if (disabled != null && disabled == -1) {
                    return null;
                }
                admin.setUuid(uuid);
                //用户名
                String username = claims.get("username", String.class);
                //权限
                String role = claims.get("role", String.class);

                List<GrantedAuthority> auths = new ArrayList<>();

                auths.add(new SimpleGrantedAuthority("ROLE_" + role));

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, auths);

                authentication.setDetails(admin);

                return authentication;
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error(e);
                }
            }


        }

        return null;
    }


    /**
     * 通过claim生成 系统的用户
     *
     * @param claims jwt的claim
     * @return 系统的用户
     */
    private static Admin buildUser(Claims claims) {
        Integer uid = claims.get("uid", Integer.class);
        String username = claims.get("username", String.class);
        Integer founder = claims.get("founder", Integer.class);
        String role = claims.get("role", String.class);
        Admin admin = new Admin();
        admin.setUid(uid);
        admin.setUsername(username);
        admin.setFounder(founder);
        admin.setRole(role);
        return admin;
    }

    @Override
    protected String getRealRedisToken(String uuid, Integer uid) {

        return StringUtil.toString(cache.get(
                TokenKeyGenerate.generateAdminAccessToken(uuid, uid)));
    }


}
