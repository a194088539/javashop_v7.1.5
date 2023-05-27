package com.enation.app.javashop.seller.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by zh on 2018/3/10.
 * jwt 验权filter
 *
 * @author zh
 * @version 1.0
 * @since 7.0.0
 * 2018/3/10
 */
public class SellerAuthenticationFilter extends GenericFilterBean {

    private SellerAuthenticationService sellerAuthenticationService;

    public SellerAuthenticationFilter(SellerAuthenticationService sellerAuthenticationService) {
        this.sellerAuthenticationService = sellerAuthenticationService;
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
        sellerAuthenticationService.auth(req);

        filterChain.doFilter(request, response);
    }
}