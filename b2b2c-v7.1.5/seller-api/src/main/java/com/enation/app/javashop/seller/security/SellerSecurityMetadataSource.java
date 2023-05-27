package com.enation.app.javashop.seller.security;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.client.member.ShopRoleClient;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.UserContext;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 权限数据源提供者<br/>
 * 提供此资源需要的角色集合
 * Created by zh on 2018/3/27.
 *
 * @author zh
 * @version 1.0
 * @since 7.0.0
 * 2018/3/27
 */
public class SellerSecurityMetadataSource implements org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource {

    private ShopRoleClient shopRoleClient;

    private Cache cache;

    public SellerSecurityMetadataSource(ShopRoleClient shopRoleClient, Cache cache) {
        this.shopRoleClient = shopRoleClient;
        this.cache = cache;
    }


    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequestUrl();

        return this.buildAttributes(url);

    }


    private Collection<ConfigAttribute> buildAttributes(String url) {
        List<String> roleList = new ArrayList<>();
        if (UserContext.getSeller() != null) {
            //从缓存中获取各个角色分别对应的菜单权限，缓存中获取不到从数据库获取，放入缓存。
            Map<String, List<String>> roleMap = (Map<String, List<String>>) cache.get(CachePrefix.SHOP_URL_ROLE.getPrefix() + UserContext.getSeller().getSellerId());
            if (roleMap == null) {
                roleMap = shopRoleClient.getRoleMap(UserContext.getSeller().getSellerId());
            }

            if (roleMap != null || roleMap.size() > 0) {
                for (String role : roleMap.keySet()) {
                    List<String> urlList = roleMap.get(role);
                    if (matchUrl(urlList, url)) {
                        roleList.add("ROLE_SELLER_" + role);
                    }
                }
            }
        }
        if (roleList.isEmpty()) {
            //没有匹配到,默认是要超级管理员才能访问
            return SecurityConfig.createList("ROLE_SELLER_SUPER_SELLER");

        } else {
            return SecurityConfig.createList(roleList.toArray(new String[roleList.size()]));
        }

    }


    /**
     * 看一个list 中是否匹配某个url
     *
     * @param expressionList 一个含有正则表达式的list
     * @param url            要匹配的Url
     * @return 是否有可以匹配此url的表达式, 有返回true
     */
    private boolean matchUrl(List<String> expressionList, String url) {

        // 遍历权限
        for (String expression : expressionList) {

            // 匹配权限
            boolean isMatch = Pattern.matches(expression, url);

            if (isMatch) {
                return true;
            }
        }
        return false;
    }



    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
