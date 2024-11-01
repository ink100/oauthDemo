package com.daym.oauth2.config;


import com.daym.oauth2.fillter.JwtRequestFilter;
import com.daym.oauth2.security.service.WhitelistUrlService;
import com.daym.oauth2.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.function.IntFunction;

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
    private WhitelistUrlService whitelistService;


    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        List<String> whitelistUrls = whitelistService.getWhitelistUrls();
        String[] whiteUrls = whitelistUrls.stream().toArray(String[]::new);
        http.csrf(csrf -> csrf.disable())
                .authorizeRequests()
                .requestMatchers(whiteUrls)
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtRequestFilter(jwtUtil,whitelistUrls), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    // 配置 AuthenticationManager Bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
