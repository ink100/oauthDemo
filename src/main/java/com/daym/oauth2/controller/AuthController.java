package com.daym.oauth2.controller;


import com.daym.oauth2.utils.JwtUtil;
import com.daym.oauth2.vo.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Date: 2024/10/31
 * Time: 11:22
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity< Map<String, String>> login(@RequestBody AuthRequest authRequest) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        Map<String, String> returnMap = jwtUtil.generateTokens(userDetails.getUsername(), "", authRequest.getRemeberMe());
        return ResponseEntity.ok(returnMap);
    }
    @PostMapping("/test/showValue")
    public String showValue() {
        return "返回陈工";
    }
}
