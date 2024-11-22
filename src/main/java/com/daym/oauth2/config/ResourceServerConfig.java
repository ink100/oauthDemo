package com.daym.oauth2.config;


import com.daym.oauth2.security.handler.*;
import com.daym.oauth2.security.provider.UsernamePasswordAuthenticationProvider;
import com.daym.oauth2.security.service.WhitelistUrlService;
import com.daym.oauth2.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.daym.oauth2.filter.JwtRequestFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Date: 2024/10/31
 * Time: 12:59
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Configuration
@EnableWebSecurity
public class ResourceServerConfig {

    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private OAuth2LoginSuccessHandler loginSuccessHandler;

    @Resource
    private OAuth2LoginFailureHandler loginFailureHandler;

    @Resource
    private CustomLogoutSuccessHandler logoutSuccessHandler;

    @Resource
    private SwitchAccountSuccessHandler switchAccountSuccessHandler;

    @Resource
    private WhitelistUrlService whitelistService;

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private JwtAuthenticationEntryPoint unauthorizedHandler;  // 自定义未认证处理器
    @Resource
    private DefaultAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        List<String> whitelistUrls = whitelistService.getWhitelistUrls();
        String[] whiteUrls = whitelistUrls.stream().toArray(String[]::new);
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests
                            .requestMatchers(whiteUrls)  // 允许白名单 URL 不需要认证
                            .permitAll()
                            .anyRequest().authenticated();  // 其他请求需要认证
                })
                .formLogin(form -> form
//                        .loginPage("/login")  // 自定义登录页面
                                .loginProcessingUrl("/login")  // 处理登录请求
//                                .defaultSuccessUrl("/home", true)  // 登录成功后跳转的 URL
//                                .failureUrl("/loginError?error=true")// 登录失败后跳转的 URL
                                .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailureHandler)

                )
             /*   .oauth2Login(oauth2Login -> oauth2Login
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailureHandler))*/

                .logout(logout -> logout.clearAuthentication(true)
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(logoutSuccessHandler)) // 配置登出
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(unauthorizedHandler)
                            .accessDeniedHandler(accessDeniedHandler);
                })
                .addFilterBefore(new JwtRequestFilter(userDetailsService, jwtUtil, whitelistUrls), UsernamePasswordAuthenticationFilter.class)
//                .authenticationProvider(usernamePasswordAuthenticationProvider())
//                .authenticationProvider(phoneNumberAuthenticationProvider())
//                .authenticationProvider(emailAuthenticationProvider())
        ;

        return http.build();
    }

    /**
     * 用户密码登录
     * @return
     */
    @Bean
    public UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider() {
        return new UsernamePasswordAuthenticationProvider();
    }

    // 配置 AuthenticationManager Bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return  new BCryptPasswordEncoder();
    }

}
