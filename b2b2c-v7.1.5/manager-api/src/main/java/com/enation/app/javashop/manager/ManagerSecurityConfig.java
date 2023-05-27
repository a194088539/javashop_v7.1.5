package com.enation.app.javashop.manager;

import com.enation.app.javashop.core.client.system.RoleClient;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.manager.security.AdminAuthenticationFilter;
import com.enation.app.javashop.manager.security.AdminAccessDecisionManager;
import com.enation.app.javashop.manager.security.AdminAuthenticationService;
import com.enation.app.javashop.manager.security.AdminSecurityMetadataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Created by kingapex on 2018/3/12.
 * 管理端安全配置
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/3/12
 */
@Configuration
@EnableWebSecurity
public class ManagerSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AdminAuthenticationService adminAuthenticationService;

    @Autowired
    private RoleClient roleClient;

    @Autowired
    private Cache cache;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                //定义验权失败返回格式
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(authenticationEntryPoint).and()
                .authorizeRequests().anyRequest().authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(
                            O fsi) {
                        fsi.setSecurityMetadataSource(new AdminSecurityMetadataSource(roleClient,cache));
                        fsi.setAccessDecisionManager(new AdminAccessDecisionManager());
                        return fsi;
                    }
                }).and().addFilterBefore(new AdminAuthenticationFilter(adminAuthenticationService), UsernamePasswordAuthenticationFilter.class);
        //禁用缓存
        http.headers().cacheControl()
                .and().contentSecurityPolicy("script-src 'self'");


    }

    /**
     * 定义跨域配置
     *
     * @return
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
