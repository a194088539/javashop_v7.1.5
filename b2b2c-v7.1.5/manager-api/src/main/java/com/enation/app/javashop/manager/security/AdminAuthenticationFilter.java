package com.enation.app.javashop.manager.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by kingapex on 2018/3/10.
 * jwt 验权filter
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/3/10
 */
public class AdminAuthenticationFilter extends GenericFilterBean {

    private AdminAuthenticationService adminAuthenticationService;

    public AdminAuthenticationFilter(AdminAuthenticationService adminAuthenticationService) {
        this.adminAuthenticationService = adminAuthenticationService;
    }


    @Value("${spring.cloud.config.profile:dev}")
    private String profile;


    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        //验权
        adminAuthenticationService.auth(req);

        filterChain.doFilter(request, response);
    }
}