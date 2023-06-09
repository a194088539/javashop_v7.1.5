package com.enation.app.javashop.manager.security;

import com.enation.app.javashop.framework.context.AdminUserContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * 管理端访问决策控制<br/>
 * 根据权限数据源提供的权限来判断是否可以通过
 *
 * @author kingapex
 * @version 1.0
 * @see AdminSecurityMetadataSource
 * Created by kingapex on 2018/3/27.
 * @since 7.0.0
 * 2018/3/27
 */
public class AdminAccessDecisionManager implements org.springframework.security.access.AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object object,
                       Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {

        FilterInvocation filterInvocation = (FilterInvocation) object;
        String url = filterInvocation.getRequestUrl();
        //过滤swagger系列
        AntPathMatcher matcher = new AntPathMatcher();
        Boolean result = matcher.match("/swagger-ui.html", url);
        result = result || matcher.match("/v2/api-docs**", url);
        result = result || matcher.match("/configuration/ui", url);
        result = result || matcher.match("/swagger-resources/**", url);
        result = result || matcher.match("/webjars/**", url);
        result = result || matcher.match("/configuration/security", url);
        if (result) {
            return;
        }
        //过滤后台管理员登录
        result = matcher.match("/admin/systems/admin-users/login**", url);
        result = result || matcher.match("/admin/systems/admin-users/token**", url);
        result = result || matcher.match("/admin/systems/admin-users/logout**", url);
        if (result) {
            return;
        }
        if (AdminUserContext.getAdmin() != null) {
            boolean bool = this.adminRolesChecked(url);
            if (bool) {
                return;
            }
        }
        if (CollectionUtils.isEmpty(configAttributes)) {
            throw new AccessDeniedException("not allow");
        }
        Iterator<ConfigAttribute> ite = configAttributes.iterator();
        while (ite.hasNext()) {
            ConfigAttribute ca = ite.next();
            String needRole = ca.getAttribute();
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                if ("ROLE_SUPER_ADMIN".equals(ga.getAuthority())) {
                    return;
                }
                if (ga.getAuthority().equals(needRole)) {
                    //匹配到有对应角色,则允许通过
                    return;
                }
            }
        }
        //该url有配置权限,但是当然登录用户没有匹配到对应权限,则禁止访问
        throw new AccessDeniedException("not allow");
    }

    /**
     * 登录之后通用权限控制
     *
     * @param url api路径
     * @return
     */
    private boolean adminRolesChecked(String url) {
        //精确匹配
        if ("/admin/index/page".equals(url)) {
            return true;
        }
        //正则匹配
        boolean isMatch = Pattern.matches("/admin/systems/roles/[1-9].*", url);
        isMatch = isMatch || Pattern.matches("/regions/[1-9].*", url);
        isMatch = isMatch || Pattern.matches("/uploaders.*", url);
        isMatch = isMatch || Pattern.matches("/admin/index/page.*", url);
        return isMatch;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
